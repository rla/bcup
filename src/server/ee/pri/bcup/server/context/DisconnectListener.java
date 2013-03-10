package ee.pri.bcup.server.context;

import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper listener interface for classes that need to
 * activate when player leaves or gets disconnected.
 *
 * @author Raivo Laanemets
 */
public interface DisconnectListener {
	
	/**
	 * This is called when the player disconnects.
	 */
	void disconnected(ServerPlayer player);
}
