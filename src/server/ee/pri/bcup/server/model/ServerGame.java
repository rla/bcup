package ee.pri.bcup.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.EqualsAndHashCode;
import edu.emory.mathcs.backport.java.util.Arrays;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.server.dao.Entity;

@EqualsAndHashCode(of = "id")
public class ServerGame implements Comparable<ServerGame>, Entity {
	
	private Long id;
	private ServerPlayer first;
	private ServerPlayer second;
	private List<ServerPlayer> observers = new CopyOnWriteArrayList<ServerPlayer>();
	private Long hitTimeout;
	private Long databaseGameId;
	private GameType gameType;

	public ServerGame(ServerPlayer first, ServerPlayer second) {
		this.first = first;
		this.second = second;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ServerPlayer getFirst() {
		return first;
	}

	public ServerPlayer getSecond() {
		return second;
	}
	
	public List<ServerPlayer> getObservers() {
		return observers;
	}
	
	public ServerPlayer getOpponent(ServerPlayer player) {
		if (player.equals(first)) {
			return second;
		} else if (player.equals(second)) {
			return first;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ServerPlayer> getAll() {
		List<ServerPlayer> list = new ArrayList<ServerPlayer>(observers);
		list.addAll(Arrays.asList(new ServerPlayer[] {first, second}));
		
		return list;
	}
	
	@Override
	public int compareTo(ServerGame o) {
		return id.compareTo(o.id);
	}

	public Long getHitTimeout() {
		return hitTimeout;
	}

	public void setHitTimeout(Long hitTimeout) {
		this.hitTimeout = hitTimeout;
	}
	
	public Long getDatabaseGameId() {
		return databaseGameId;
	}

	public void setDatabaseGameId(Long databaseGameId) {
		this.databaseGameId = databaseGameId;
	}
	
	/**
	 * Checks whether the given player is observer.
	 */
	public boolean isObserver(ServerPlayer player) {
		return observers.contains(player);
	}
	
	/**
	 * Returns whether the given player takes part in this game
	 * but not as an observer.
	 */
	public boolean isPlayer(ServerPlayer player) {
		return first.equals(player) || second.equals(player);
	}
	
	/**
	 * Removes the given player from the list of
	 * this game observers.
	 */
	public void removeObserver(ServerPlayer player) {
		observers.remove(player);
	}
	
	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	@Override
	public String toString() {
		return "first=" + first + ",second=" + second + ",observers=" + observers;
	}
	
}
