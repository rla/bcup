package ee.pri.bcup.common.message;

/**
 * Scope of message listeners.
 * 
 * @author Raivo Laanemets
 */
public enum ListenerScope {
	
	/**
	 * Scope for entire applet.
	 */
	APPLET,
	
	/**
	 * Scope for server.
	 */
	SERVER,
	
	/**
	 * Scope for a single game.
	 */
	GAME
}
