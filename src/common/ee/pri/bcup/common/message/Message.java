package ee.pri.bcup.common.message;

import java.util.Collection;

import ee.pri.bcup.common.message.serialize.SimpleSerializer;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.net.NetPlayer;

public abstract class Message {

	public String makeMessage() {
		return jsonMessage(this);
	}
	
	@Override
	public String toString() {
		return jsonMessage(this);
	}

	@SuppressWarnings("unchecked")
	public static Message parse(String message) {
		
		int atPos = message.indexOf('@');
		if (atPos == -1) {
			System.err.println("Invalid message: " + message);
			return null;
		}
		String className = message.substring(0, atPos);
		
		try {
			Class<? extends Message> clazz = (Class<? extends Message>) Class.forName(className);
			return SimpleSerializer.fromJson(message.substring(atPos + 1), clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String jsonMessage(Message message) {
		String className = message.getClass().getCanonicalName();
		
		return className + "@" + SimpleSerializer.toJson(message);
	}
	
	/**
	 * Sends the given message to all specified players.
	 */
	public static void sendToAll(Collection<? extends NetPlayer> players, Message message) {
		for (NetPlayer player : players) {
			player.send(message);
		}
	}
	
	/**
	 * Sends the given message to all specified players except the one.
	 */
	public static void sendToAllExcept(Collection<? extends NetPlayer> players, Player except, Message message) {
		for (NetPlayer player : players) {
			if (!player.equals(except)) {
				player.send(message);
			}
		}
	}
}
