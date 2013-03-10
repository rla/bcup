package ee.pri.bcup.server.model;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.server.ServerMessage;
import ee.pri.bcup.server.dao.Entity;

@EqualsAndHashCode(of = "id")
public class ServerRoom implements Serializable, Comparable<ServerRoom>, Entity {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ServerRoom.class);

	private Long id;
	private String name;
	private List<ServerPlayer> players = new CopyOnWriteArrayList<ServerPlayer>();
	private List<ServerGame> games = new CopyOnWriteArrayList<ServerGame>();
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<ServerPlayer> getPlayers() {
		return players;
	}
	
	public List<ServerGame> getGames() {
		return games;
	}
	
	/**
	 * Sends message for each person in the room.
	 */
	public void send(ServerMessage message) {
		log.debug("Sending message " + message + " to " + players);
		Message.sendToAll(players, message);
	}
	
	@Override
	public int compareTo(ServerRoom o) {
		return id.compareTo(o.id);
	}

	@Override
	public String toString() {
		return "room(games=" + games + ",players=" + players + ")";
	}

}
