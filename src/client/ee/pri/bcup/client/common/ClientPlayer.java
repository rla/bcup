package ee.pri.bcup.client.common;

import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.log.Logger;
import ee.pri.bcup.common.message.ListenerSet;
import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.client.ClientPingMessage;
import ee.pri.bcup.common.message.server.ServerPingMessage;
import ee.pri.bcup.common.net.NetPlayer;

public class ClientPlayer extends NetPlayer {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ClientPlayer.class);
	
	private AppletContext appletContext;
	private ListenerSet listeners;

	public ClientPlayer(Socket socket, int clientTimeout) throws IOException {
		super(socket, clientTimeout);
	}

	@Override
	public void handleDisconnect() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				appletContext.getApplet().disconnected();
			}
		});
	}

	@Override
	protected void receiveMessage(final Message message) {
		if (message instanceof ServerPingMessage) {
			ClientPingMessage pingMessage = new ClientPingMessage();
			send(pingMessage);
		} else {
			log.debug(getName() + " receives " + message);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (listeners != null) {
					listeners.message(message);
				}
			}
		});
	}

	public void setAppletContext(AppletContext appletContext) {
		this.appletContext = appletContext;
	}

	@Override
	public void send(Message message) {
		if (!(message instanceof ClientPingMessage)) {
			log.debug(getName() + ": sending: " + message);
		}
		super.send(message);
	}

	/**
	 * Sets message listeners that this player should forward
	 * messages to.
	 */
	public void setListeners(ListenerSet listeners) {
		this.listeners = listeners;
	}

}
