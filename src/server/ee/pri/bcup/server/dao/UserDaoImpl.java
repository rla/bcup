package ee.pri.bcup.server.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ee.pri.bcup.server.exception.NoUserException;

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {
	private Map<String, DatabaseEnv> databaseContext;
	
	@Required
	public void setDatabaseContext(Map<String, DatabaseEnv> databaseContext) {
		this.databaseContext = databaseContext;
	}

	public String getUsername(Long id, String sessionKey, String databaseEnvKey) throws NoUserException {
		// FIXME here and in the method below are
		// database queries commented out since there
		// is no database.
		
		switch (id.intValue()) {
		case 1: return "User 1";
		case 2: return "User 2";
		case 3: return "User 3";
		default:
			return "Nameless";
		}
		
		/*
		DatabaseEnv env = databaseContext.get(databaseEnvKey);
		try {
			return (String) getJdbcTemplate().queryForObject(
				" SELECT name" +
				" FROM " + env.getDatabaseName() + ".pool" + env.getTablePrefix() + "_players" +
				" WHERE id = ? AND phpsessid = ?", new Object[] {id, sessionKey}, String.class);
		} catch (EmptyResultDataAccessException e) {
			throw new NoUserException();
		}*/
	}

	public String getUsername(Long id, String databaseEnvKey) throws NoUserException {
		switch (id.intValue()) {
		case 1: return "User 1";
		case 2: return "User 2";
		case 3: return "User 3";
		default:
			return "Nameless";
		}
		
		/*
		DatabaseEnv env = databaseContext.get(databaseEnvKey);
		try {
			return (String) getJdbcTemplate().queryForObject(
				" SELECT name" +
				" FROM " + env.getDatabaseName() + ".pool" + env.getTablePrefix() + "_players" +
				" WHERE id = ?", new Object[] {id}, String.class);
		} catch (EmptyResultDataAccessException e) {
			throw new NoUserException();
		}*/
	}
}
