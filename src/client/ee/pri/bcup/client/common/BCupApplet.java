package ee.pri.bcup.client.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.Socket;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.MessagePanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.chat.ChatPanel;
import ee.pri.bcup.client.pool.field.PoolFieldPanel;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.common.Version;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.AuthenticationRequestMessage;
import ee.pri.bcup.common.message.client.ClientJoinRoomMessage;
import ee.pri.bcup.common.message.client.ClientLeaveMessage;
import ee.pri.bcup.common.message.server.AuthenticationFailResponseMessage;
import ee.pri.bcup.common.message.server.AuthenticationSuccessResponseMessage;
import ee.pri.bcup.common.message.server.AuthenticationVersionFailResponse;
import ee.pri.bcup.common.message.server.ServerDuplicateUserMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.game.ServerGameExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserveSuccessfulMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeAcceptMessage;
import ee.pri.bcup.common.model.GamePartyType;

/**
 * Base class for BCUP applets. Handles initialization and user authentication and joins
 * into the game room.
 */
public abstract class BCupApplet extends JApplet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BCupApplet.class);
	
	private static final int CLIENT_TIMEOUT = 40000;
	
	private Messages messages;
	private ClientPlayer player;
	private String host;
	private int port;
	private Long userId;
	private String sessionKey;
	private String databaseEnvKey;
	private String language;
	private Long roomId;
	private MessagePanel messagePanel;
	private ChatPanel chatPanel;
	protected PoolFieldPanel fieldPanel;
	private AppletContext context;
	private Backgrounds backgrounds;
	
	public ClientPlayer getPlayer() {
		return player;
	}

	@Override
	public void init() {
		host = getParameter("host");
		port = Integer.valueOf(getParameter("port"));
		userId = Long.valueOf(getParameter("userId"));
		roomId = Long.valueOf(getParameter("roomId"));
		sessionKey = getParameter("sessionKey");
		databaseEnvKey = getParameter("databaseEnvKey");
		language = getParameter("language");
		messages = new Messages();
		messages.setLanguage(language);
		getContentPane().setLayout(new BorderLayout());
		
		log.info("applet initialized");
		
		try {
			player = new ClientPlayer(new Socket(host, port), CLIENT_TIMEOUT);
		} catch (Exception e) {
			errorMessagePanel(getMessage("message.error.connect", e.getMessage()));
			return;
		}
		
		context = createContext(player);
		context.processListeners(this, ListenerScope.APPLET);
		player.setAppletContext(context);
		player.start();

		backgrounds = new Backgrounds(context);
		messagePanel = new MessagePanel(context);
		
		getContentPane().add(messagePanel, BorderLayout.CENTER);
		messagePanel.setMessage(getMessage("message.auth"));
		player.send(new AuthenticationRequestMessage(userId, sessionKey, databaseEnvKey, Version.VERSION));
		
		setBackground(Color.LIGHT_GRAY);
	}
	
	protected abstract AppletContext createContext(ClientPlayer player);
	
	@MessageReceiver(AuthenticationSuccessResponseMessage.class)
	public void receive(AuthenticationSuccessResponseMessage message) {
		player.setName(message.getName());
		player.setId(message.getId());
		afterAuthentication();
	}
	
	@MessageReceiver(AuthenticationFailResponseMessage.class)
	public void receive(AuthenticationFailResponseMessage message) {
		messagePanel.setMessage(getMessage("message.auth.fail"));
	}
	
	@MessageReceiver(AuthenticationVersionFailResponse.class)
	public void receive(AuthenticationVersionFailResponse message) {
		messagePanel.setMessage(getMessage("message.auth.fail.version"));
	}
	
	@MessageReceiver(ServerProposeAcceptMessage.class)
	public void receive(ServerProposeAcceptMessage message) {
		log.debug("propose was accepted");
		startGame(GamePartyType.PLAYER, 0L);
	}
	
	@MessageReceiver(ServerGameObserveSuccessfulMessage.class)
	public void receive(ServerGameObserveSuccessfulMessage message) {
		log.debug("observation request was successful");
		startGame(GamePartyType.OBSERVER, 0L);
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		// FIXME assumes pool game
		PoolAppletContext appletContext = (PoolAppletContext) context;
		PoolGame gameContext = appletContext.getGameContext();
		
		if (gameContext != null
				&& (message.getPlayerId().equals(gameContext.getLeft().getId())
					|| message.getPlayerId().equals(gameContext.getRight().getId()))
				&& gameContext.getWinner() == null) {
			
			// This should be called when one of the players leaves the room.
			// Leaving the room is equivalent of leaving the Applet.
			
			// We do not leave automatically when winner is already found out.
			// (The confirmation button will leave)
			
			setPanel(chatPanel);
			context.destroyGame();
		}
	}
	
	// FIXME same as code above, consider refactoring
	@MessageReceiver(ServerGameExitMessage.class)
	public void receive(ServerGameExitMessage message) {
		// FIXME assumes pool game
		PoolAppletContext appletContext = (PoolAppletContext) context;
		PoolGame gameContext = appletContext.getGameContext();
		
		if (gameContext != null
				&& (message.getPlayerId().equals(gameContext.getLeft().getId())
					|| message.getPlayerId().equals(gameContext.getRight().getId()))
				&& gameContext.getWinner() == null) {
			
			// We just were having a game, exit it.
			
			// We do not leave automatically when winner is already found out.
			// (The confirmation button will leave)
			
			setPanel(chatPanel);
			context.destroyGame();
		}
	}
	
	@MessageReceiver(ServerDuplicateUserMessage.class)
	public void receive(ServerDuplicateUserMessage message) {
		messagePanel.setMessage(getMessage("message.duplicate.user"));
	}
	
	/**
	 * Sets main panel of the applet (currently displayed screen).
	 */
	public void setPanel(JPanel panel) {
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().add(panel, BorderLayout.CENTER);
		((JComponent) getContentPane()).revalidate();
		repaint();
	}
	
	public void acceptGame() {
		log.debug("accepting game");
		startGame(GamePartyType.PLAYER, 0L);
	}
	
	/**
	 * Method that is called after the game proposal is accepted.
	 * @param latencyCorrection correction for timeout (add).
	 */
	public abstract void startGame(GamePartyType partyType, long latencyCorrection);
	
	private void afterAuthentication() {
		
		// FIXME sleep
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// FIXME push into subclass
		chatPanel = new ChatPanel((PoolAppletContext) context);
		setPanel(chatPanel);
		player.send(new ClientJoinRoomMessage(roomId));
	}
	
	/**
	 * Returns localized message by taking into account the current language.
	 */
	public String getMessage(String key, Object... parameters) {
		return messages.getMessage(key, parameters);
	}
	
	protected AppletContext getContext() {
		return context;
	}

	public void disconnected() {
		log.debug("disconnected");
		
		errorMessagePanel(getMessage("message.disconnect"));
		
		log.debug("destroying game context");
		if (context != null) {
			context.destroyGame();
		}
	}
	
	/**
	 * Must be called to show the messages screen.
	 * For example, this should be called when connection with server fails.
	 */
	public void errorMessagePanel(String message) {
		messagePanel.setMessage(message);
		setPanel(messagePanel);
	}

	@Override
	public void destroy() {
		log.debug("destroying applet");
		if (player != null) {
			player.send(new ClientLeaveMessage());
			player.terminate();
		}
		
		if (context != null) {
			context.destroyGame();
			context = null;
		}
	}

	/**
	 * Returns backgrounds manager.
	 */
	public Backgrounds getBackgrounds() {
		return backgrounds;
	}
	
	public void exitGame() {
		log.debug(context.getPlayer() + " exits game, changing panels");
		setPanel(chatPanel);
	}
	
}
