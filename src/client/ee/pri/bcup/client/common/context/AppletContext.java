package ee.pri.bcup.client.common.context;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.common.BCupApplet;
import ee.pri.bcup.client.common.ClientPlayer;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.ListenerSet;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.ClientMessage;
import ee.pri.bcup.common.message.client.game.ClientGameExitMessage;
import ee.pri.bcup.common.message.server.ServerJoinRoomMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.game.ServerGameExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameStartedMessage;
import ee.pri.bcup.common.model.Game;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.util.ImageUtils;

/**
 * Context class for a running applet.
 * 
 * @author Raivo Laanemets
 */
public abstract class AppletContext implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(AppletContext.class);
	
	private BCupApplet applet;
	private ClientPlayer player;
	private Map<Long, Player> knownPlayers = new HashMap<Long, Player>();
	private Map<Long, Game> playerGames = new HashMap<Long, Game>();
	private List<Game> games = new ArrayList<Game>();
	private ListenerSet listeners = new ListenerSet();
	
	private ProposeContext proposeContext;

	/**
	 * Constructs new applet context for the given applet and player.
	 */
	public AppletContext(BCupApplet applet, ClientPlayer player) {
		this.applet = applet;
		this.player = player;
		
		player.setListeners(listeners);
		processListeners(this, ListenerScope.APPLET);
		
		proposeContext = new ProposeContext(this);
	}
	
	@MessageReceiver(ServerJoinRoomMessage.class)
	public void receive(ServerJoinRoomMessage message) {
		Player player = new Player();
		player.setId(message.getPlayerId());
		player.setName(message.getName());
		knownPlayers.put(message.getPlayerId(), player);
	}
	
	@MessageReceiver(ServerGameStartedMessage.class)
	public void receive(ServerGameStartedMessage message) {
		Game game = new Game(
			message.getGameId(),
			getPlayer(message.getPlayerAId()),
			getPlayer(message.getPlayerBId()),
			message.getHitTimeout(),
			message.getGameType()
		);
		games.add(game);
		playerGames.put(message.getPlayerAId(), game);
		playerGames.put(message.getPlayerBId(), game);
	}
	
	@MessageReceiver(ServerGameExitMessage.class)
	public void receive(ServerGameExitMessage message) {
		Game game = playerGames.remove(message.getPlayerId());
		if (game != null) {
			playerGames.values().remove(game);
			games.remove(game);
		}
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		Game game = playerGames.remove(message.getPlayerId());
		if (game != null) {
			playerGames.values().remove(game);
			games.remove(game);
		}
	}

	public BCupApplet getApplet() {
		return applet;
	}

	public ClientPlayer getPlayer() {
		return player;
	}
	
	public String getMessage(String key, Object... parameters) {
		return applet.getMessage(key, parameters);
	}

	// FIXME rename argument, this is not user id
	public Player getPlayer(Long userId) {
		return knownPlayers.get(userId);
	}
	
	public boolean hasGame(Long playerId) {
		return playerGames.containsKey(playerId);
	}
	
	public Game getPossibleGame(Long playerId) {
		return playerGames.get(playerId);
	}
	
	/**
	 * Creates buffer for off-screen drawing.
	 * 
	 * @param width width of the buffer.
	 * @param height height of the buffer.
	 */
	public Image createBuffer(int width, int height) {		
		//return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		return applet.createImage(width, height);
	}
	
	protected abstract void destroyGameContext();
	
	public void destroyGame() {
		log.debug("clearing game-scoped listeners");
		listeners.clearScope(ListenerScope.GAME);
		log.debug("running game-dependent context destroy");
		destroyGameContext();
	}

	public void processListeners(Object object, ListenerScope scope) {
		listeners.processListeners(object, scope);
	}

	public List<Game> getGames() {
		return games;
	}

	public ProposeContext getProposeContext() {
		return proposeContext;
	}
	
	/**
	 * Sends the given message to the server.
	 * See also {@link ClientPlayer#send(ee.pri.bcup.common.message.Message)}.
	 */
	public void send(ClientMessage message) {
		player.send(message);
	}
	
	public void acceptGame() {
		log.debug("accepting game");
		applet.acceptGame();
	}
	
	/**
	 * Returns the background image with the given name.
	 */
	public Image getBackground(String name) {
		return applet.getBackgrounds().getBackgound(name);
	}
	
	public Image getImage(String location) {
		return ImageUtils.loadImage(location);
	}
	
	/**
	 * This is called when the player exists the game.
	 */
	public void exitGame() {
		log.debug(player + " exits the game");
		applet.exitGame();
		destroyGame();
		send(new ClientGameExitMessage());
	}

}
