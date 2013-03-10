package ee.pri.bcup.common.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the game between two players.
 */
// FIXME move to client model
// FIXME unify with server model
@Data
@EqualsAndHashCode(of = "id")
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Long id;
	private final Player first;
	private final Player second;
	private final Long hitTimeout;
	private final GameType gameType;
	
	public Game(Long id, Player first, Player second, Long hitTimeout, GameType gameType) {
		this.id = id;
		this.first = first;
		this.second = second;
		this.hitTimeout = hitTimeout;
		this.gameType = gameType;
	}
	
	@Override
	public String toString() {
		return first + " - " + second;
	}
	
}
