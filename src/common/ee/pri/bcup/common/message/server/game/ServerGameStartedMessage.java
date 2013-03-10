package ee.pri.bcup.common.message.server.game;

import ee.pri.bcup.common.message.server.ServerMessage;
import ee.pri.bcup.common.model.GameType;


public class ServerGameStartedMessage extends ServerMessage {
	private Long playerAId;
	private Long playerBId;
	private Long gameId;
	private Long hitTimeout;
	private GameType gameType;
	
	public ServerGameStartedMessage() {}
	
	public ServerGameStartedMessage(Long playerAId, Long playerBId, Long gameId, Long hitTimeout, GameType gameType) {
		this.playerAId = playerAId;
		this.playerBId = playerBId;
		this.gameId = gameId;
		this.hitTimeout = hitTimeout;
	}

	public Long getPlayerAId() {
		return playerAId;
	}

	public Long getPlayerBId() {
		return playerBId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setPlayerAId(Long playerAId) {
		this.playerAId = playerAId;
	}

	public void setPlayerBId(Long playerBId) {
		this.playerBId = playerBId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getHitTimeout() {
		return hitTimeout;
	}

	public void setHitTimeout(Long hitTimeout) {
		this.hitTimeout = hitTimeout;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

}
