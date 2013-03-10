package ee.pri.bcup.server.context;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.server.ServerJoinRoomMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.ServerRoomMessage;
import ee.pri.bcup.common.message.server.ServerRoomPrivateMessage;
import ee.pri.bcup.common.message.server.game.ServerGameExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserveSuccessfulMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverMessage;
import ee.pri.bcup.common.message.server.game.ServerGamePrivateMessage;
import ee.pri.bcup.common.message.server.game.ServerGameStartedMessage;
import ee.pri.bcup.common.message.server.game.ServerGameTextMessage;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.server.dao.GameDao;
import ee.pri.bcup.server.dao.RoomDao;
import ee.pri.bcup.server.dao.StatisticsDao;
import ee.pri.bcup.server.model.ServerGame;
import ee.pri.bcup.server.model.ServerPlayer;
import ee.pri.bcup.server.model.ServerRoom;

/**
 * Helper class for managing rooms.
 *
 * @author Raivo Laanemets
 */
public class RoomManager implements DisconnectListener, InitializingBean {
	private static final Logger log = Logger.getLogger(RoomManager.class);
	
	private GameDao gameDao;
	private RoomDao roomDao;
	private StatisticsDao statisticsDao;
	private ServerContext serverContext;
	
	@Required
	public void setServerContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	@Required
	public void setStatisticsDao(StatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}

	@Required
	public void setRoomDao(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Required
	public void setGameDao(GameDao gameDao) {
		this.gameDao = gameDao;
	}

	/**
	 * Removes the given user from its room if he/she
	 * was there and sends others a message about it.
	 */
	@Override
	public void disconnected(ServerPlayer player) {
		ServerRoom room = player.getRoom();
		
		if (room != null) {
			log.debug("removing player " + player + " from room " + room);
			room.getPlayers().remove(player);
			
			room.send(new ServerPartRoomMessage(player.getId()));
		}
		
		ServerGame game = player.getGame();
		if (game != null) {
			statisticsDao.finish(game.getDatabaseGameId());
			gameDao.delete(game);
			log.debug("removing game " + game + " from room " + room);
			
			room.getGames().remove(game);
		}
	}
	
	@Required
	public void setRooms(List<ServerRoom> rooms) {
		for (ServerRoom room : rooms) {
			// FIXME ignores predefined room id-s
			roomDao.store(room);
		}
	}

	/**
	 * Joins the given player with the given room.
	 * Sends the list of active games to the newly joined player.
	 */
	public void join(ServerPlayer player, ServerRoom room) {		
		log.debug(player + " joins " + room);
		
		player.setRoom(room);
		room.getPlayers().add(player);
		room.send(new ServerJoinRoomMessage(player.getName(), player.getId()));
		
		// Important! Player names must be sent before games.
		
		for (ServerPlayer roomPlayer : room.getPlayers()) {
			if (!player.equals(roomPlayer)) {
				player.send(new ServerJoinRoomMessage(roomPlayer.getName(), roomPlayer.getId()));
			}
		}
		
		for (ServerGame game : room.getGames()) {
			player.send(new ServerGameStartedMessage(
				game.getFirst().getId(),
				game.getSecond().getId(),
				game.getId(),
				game.getHitTimeout(),
				game.getGameType()
			));
		}
	}
	
	/**
	 * Register game between the two given players.
	 */
	public void start(ServerPlayer initiator, ServerPlayer target, Long hitTimeout, GameType gameType) {
		log.info("starting game between players " + initiator + " and " + target);
		
		ServerRoom room = initiator.getRoom();
		
		ServerGame game = new ServerGame(initiator, target);
		game.setHitTimeout(hitTimeout);
		game.setGameType(gameType);
		gameDao.store(game);
		
		room.getGames().add(game);
		
		initiator.setGame(game);
		target.setGame(game);
		
		game.setDatabaseGameId(statisticsDao.gameStarted(initiator.getUserId(), target.getUserId()));
		
		Message.sendToAll(room.getPlayers(), new ServerGameStartedMessage(
			initiator.getId(),
			target.getId(),
			game.getId(),
			hitTimeout,
			gameType
		));
	}
	
	/**
	 * Observes the given game.
	 */
	public void observe(ServerPlayer observer, ServerGame game) {
		log.info("adding observer " + observer + " to game " + game);
		
		game.getObservers().add(observer);
		observer.setGame(game);
			
		observer.send(new ServerGameObserveSuccessfulMessage());
		
		Message.sendToAll(game.getAll(), new ServerGameObserverMessage(observer.getId()));
	}

	/**
	 * Forwards player message to all people that are
	 * in the same room.
	 */
	public void message(ServerPlayer player, String message) {		
		log.info(player + " > " + player.getRoom() + ":" + message);
		Message.sendToAll(player.getRoom().getPlayers(), new ServerRoomMessage(message, player.getId()));
		
		// FIXME server debugging
		if ("!debug".equals(message)) {
			player.send(new ServerRoomMessage(serverContext.createDebugString(), player.getId()));
		}
	}
	
	/**
	 * Informs other people in the room that the player
	 * has returned from a game.
	 */
	public void exitGame(ServerPlayer player) {
		ServerRoom room = player.getRoom();
		ServerGame game = player.getGame();
		
		// Detect role
		if (game.isObserver(player)) {
			
			log.info(player + " exited from game as an observer");
			player.setGame(null);
			game.removeObserver(player);
			
			ServerGameObserverExitMessage message = new ServerGameObserverExitMessage();
			message.setPlayerId(player.getId());
			Message.sendToAll(game.getAll(), message);
			
		} else if (game.isPlayer(player)) {
			
			log.info(player + " exited from game as a player");
			gameDao.delete(game);
			room.getGames().remove(game);
		
			statisticsDao.finish(game.getDatabaseGameId());
			
			ServerGameExitMessage message = new ServerGameExitMessage();
			message.setPlayerId(player.getId());
			Message.sendToAll(room.getPlayers(), message);
			
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("room manager created");
	}

	public void text(ServerPlayer player, String message) {
		ServerGame game = player.getGame();
		Message.sendToAll(game.getAll(), new ServerGameTextMessage(player.getId(), message));
	}

	/**
	 * Sends text message from one player to another.
	 */
	public void sendPrivateMessage(ServerPlayer from, ServerPlayer to, String text) {
		ServerRoomPrivateMessage message = new ServerRoomPrivateMessage();
		message.setFromId(from.getId());
		message.setToId(to.getId());
		message.setText(text);
		
		to.send(message);
		
		// To yourself too
		message = new ServerRoomPrivateMessage();
		message.setFromId(from.getId());
		message.setToId(to.getId());
		message.setText(text);
		
		from.send(message);
	}

	/**
	 * Sends text message from one player to another (in game).
	 * This is identical to {@link RoomManager#sendPrivateMessage(ServerPlayer, ServerPlayer, String)}
	 * but uses other type of message.
	 */
	public void sendGamePrivateMessage(ServerPlayer from, ServerPlayer to, String text) {
		ServerGamePrivateMessage message = new ServerGamePrivateMessage();
		message.setFromId(from.getId());
		message.setToId(to.getId());
		message.setText(text);
		
		to.send(message);
		
		// To yourself too
		message = new ServerGamePrivateMessage();
		message.setFromId(from.getId());
		message.setToId(to.getId());
		message.setText(text);
		
		from.send(message);
	}

	/**
	 * Handles a case when the player loses game.
	 */
	public void lost(ServerPlayer player) {
		ServerPlayer winner = player.getGame().getOpponent(player);
		statisticsDao.won(winner.getGame().getDatabaseGameId(), winner.getUserId());
	}
	
}
