package ee.pri.bcup.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * DAO for game statistics.
 *
 * @author Raivo Laanemets
 */
public class StatisticsDao extends JdbcDaoSupport {
	private static final Logger log = Logger.getLogger(StatisticsDao.class);
	
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		log.debug("statistics dao created");
	}

	/**
	 * Stores in the database that the new game has started.
	 * 
	 * @param starterId user ID of the starter.
	 * @param opponentId user ID of the opponent.
	 */
	public Long gameStarted(final Long starterId, final Long opponentId) {
		log.debug("game start between " + starterId + " and " + opponentId);
		
		// FIXME here and in the method below are
		// database queries commented out since there
		// is no database.
		
		return new Random().nextLong();
		
		/*
		return (Long) getJdbcTemplate().execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO pool_statistics (started_at, starter_id, opponent_id)" +
					"VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				
				statement.setLong(1, new Date().getTime() / 1000);
				statement.setLong(2, starterId);
				statement.setLong(3, opponentId);
				
				return statement;
			}
		}, new PreparedStatementCallback() {
			@Override
			public Object doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
				statement.execute();
				ResultSet resultSet = statement.getGeneratedKeys();
				try {
					if (resultSet.next()) {
						return resultSet.getLong(1);
					} else {
						throw new RuntimeException("Cannot obtain game ID");
					}
				} finally {
					resultSet.close();
				}
			}
		});*/
	}

	/**
	 * Sets the winner and end timestamp of the game in database.
	 * 
	 * @param databaseGameId
	 * @param id
	 */
	public void won(final Long databaseGameId, final Long winnerId) {
		log.debug("player " + winnerId + " won game " + databaseGameId);
		/*
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(
					"UPDATE pool_statistics SET finished_at = ?, winner_id = ? WHERE id = ?"
				);
				
				statement.setLong(1, new Date().getTime() / 1000);
				statement.setLong(2, winnerId);
				statement.setLong(3, databaseGameId);
				
				return statement;
			}
		})*/;
	}

	public void finish(final Long databaseGameId) {
		log.debug("finishing game " + databaseGameId + " without winner");		
		/*
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(
					"UPDATE pool_statistics SET finished_at = ? WHERE id = ?"
				);
				
				statement.setLong(1, new Date().getTime() / 1000);
				statement.setLong(2, databaseGameId);
				
				return statement;
			}
		});*/
	}
	
}
