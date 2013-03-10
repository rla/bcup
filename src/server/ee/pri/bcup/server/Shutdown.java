package ee.pri.bcup.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.client.ClientShutdownMessage;
import ee.pri.bcup.common.net.NetPlayer;

public class Shutdown {

	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		properties.load(Shutdown.class.getResourceAsStream("/server.properties"));
		ShutdownPlayer player = new ShutdownPlayer(new Socket("localhost", 3003), 10000, (String) properties.get("server.shutdown.magic"));
		player.start();
		player.sendShutdownRequest();
		player.terminateAndStop();
	}
	
	private static class ShutdownPlayer extends NetPlayer {
		private static final long serialVersionUID = 1L;
		
		private String magicWord;
		
		public ShutdownPlayer(Socket socket, int clientTimeout, String magicWord) throws IOException {
			super(socket, clientTimeout);
			this.magicWord = magicWord;
		}

		public void sendShutdownRequest() {
			send(new ClientShutdownMessage(magicWord));
		}

		@Override
		public void handleDisconnect() {}

		@Override
		protected void receiveMessage(Message message) {}
		
	}

}
