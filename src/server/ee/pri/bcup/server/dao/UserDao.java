package ee.pri.bcup.server.dao;

import ee.pri.bcup.server.exception.NoUserException;

public interface UserDao {
	String getUsername(Long id, String sessionKey, String databaseEnvKey) throws NoUserException;
	String getUsername(Long id, String databaseEnvKey) throws NoUserException;
}
