package ee.pri.bcup.server.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import ee.pri.bcup.common.Version;
import ee.pri.bcup.common.message.server.AuthenticationFailResponseMessage;
import ee.pri.bcup.common.message.server.AuthenticationSuccessResponseMessage;
import ee.pri.bcup.common.message.server.AuthenticationVersionFailResponse;
import ee.pri.bcup.common.message.server.ServerDuplicateUserMessage;
import ee.pri.bcup.server.dao.DatabaseEnv;
import ee.pri.bcup.server.dao.PlayerDao;
import ee.pri.bcup.server.dao.UserDao;
import ee.pri.bcup.server.exception.NoUserException;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for doing user authentication.
 *
 * @author Raivo Laanemets
 */
public class AuthenticationManager implements InitializingBean {
	private static final Logger log = Logger.getLogger(AuthenticationManager.class);
	
	private UserDao userDao;
	private PlayerDao playerDao;
	
	/**
	 * Authenticates the given player.
	 * 
	 * @param player player to authenticate
	 * @param userId user id in BCup database
	 * @param databaseEnvKey database environment
	 */
	public void authenticate(ServerPlayer player, Long userId, String databaseEnvKey, Integer version) {
		if (!DatabaseEnv.isValidKey(databaseEnvKey)) {
			throw new RuntimeException("Invalid database env key: " + databaseEnvKey);
		}
		
		if (version == null || !version.equals(Version.VERSION)) {
			player.send(new AuthenticationVersionFailResponse());
		} else if (playerDao.hasUserWithId(userId)) {
			player.send(new ServerDuplicateUserMessage());
		} else {
			try {
				// FIXME ignoreerib phpsessid-d
				String name = userDao.getUsername(userId, databaseEnvKey);
				
				player.setName(name);
				player.setIdentified(true);
				player.setUserId(userId);
				player.send(new AuthenticationSuccessResponseMessage(name, player.getId()));
				log.info("User with id " + userId + " authenticated");
			} catch (NoUserException e) {
				log.info("User with id " + userId + " does not exist or session key is wrong");
				player.send(new AuthenticationFailResponseMessage());
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("authentication manager created");
	}

	@Required
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Required
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}
	
}
