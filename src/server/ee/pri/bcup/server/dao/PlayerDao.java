package ee.pri.bcup.server.dao;

import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Data access class for keeping player instances in the server.
 *
 * @author Raivo Laanemets
 */
public class PlayerDao extends AbstractMemoryDao<ServerPlayer> {
	
	/**
	 * Check if the user with the given user id exists in the current system.
	 * @param userId user id in BCup database.
	 */
	public boolean hasUserWithId(Long userId) {
		// FIXME might be slow for lot of users
		for (ServerPlayer player : all()) {
			if (userId.equals(player.getUserId())) {
				return true;
			}
		}
		
		return false;
	}
}
