-- FIXME add indexes for needed queries.

CREATE TABLE pool_statistics (

	id INT NOT NULL AUTO_INCREMENT
		COMMENT 'Game ID',
		
	started_at INT NOT NULL
		COMMENT 'The Unix timestamp of the game start date',
		
	finished_at INT DEFAULT NULL
		COMMENT 'The Unix timestamp of the game end date, might be NULL if the game is unfinished',
		
	starter_id INT NOT NULL
		COMMENT 'User ID of the game starter',
		
	opponent_id INT NOT NULL
		COMMENT 'User ID of the opponent',
		
	winner_id INT DEFAULT NULL
		COMMENT 'User ID of the winner, must be either game starter ID or opponent ID. Null when no winner.',
		
	PRIMARY KEY(id)

) COMMENT = 'Table for pool game statistics';

-- latest 20 finished games with winners and losers

CREATE VIEW pool_latest_games AS
SELECT
	s.started_at AS started_at,
	(SELECT name FROM pool_players w WHERE w.id = s.winner_id) AS winner_name,
	CASE WHEN s.winner_id = s.starter_id
		THEN (SELECT name FROM pool_players l WHERE l.id = s.opponent_id)
		ELSE (SELECT name FROM pool_players l WHERE l.id = s.starter_id)
	END AS loser_name
FROM pool_statistics s
WHERE s.winner_id IS NOT NULL
ORDER BY started_at DESC
LIMIT 20