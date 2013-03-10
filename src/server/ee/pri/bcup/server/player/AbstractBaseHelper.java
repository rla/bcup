package ee.pri.bcup.server.player;

import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.model.ServerGame;
import ee.pri.bcup.server.model.ServerPlayer;
import ee.pri.bcup.server.model.ServerRoom;

/**
 * Base class for helpers. Helper classes are instantiated
 * one per server player instance and are used as a thin
 * layer between the player and the manager.
 * 
 * One main responsibility of these helper classes are to
 * convert entity identifiers into actual domain entities
 * by using methods {@link AbstractBaseHelper#getGame(Long)},
 * {@link AbstractBaseHelper}{@link #getPlayer(Long)} and
 * {@link AbstractBaseHelper#getRoom(Long)}.
 *
 * @author Raivo Laanemets
 */
public abstract class AbstractBaseHelper {
	protected ServerPlayer player;
	protected ServerContext context;
	
	public AbstractBaseHelper(ServerPlayer player, ServerContext context) {
		this.player = player;
		this.context = context;
	}
	
	/**
	 * Returns player with the given id.
	 */
	protected ServerPlayer getPlayer(Long playerId) {
		return context.getPlayerDao().find(playerId);
	}
	
	/**
	 * Returns the game with the given id.
	 */
	protected ServerGame getGame(Long gameId) {
		return context.getGameDao().find(gameId);
	}
	
	/**
	 * Returns the room with the given id.
	 */
	protected ServerRoom getRoom(Long roomId) {
		return context.getRoomDao().find(roomId);
	}
}
