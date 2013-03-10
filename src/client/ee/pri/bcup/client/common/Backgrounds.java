package ee.pri.bcup.client.common;

import java.awt.Image;

import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Helper class for managing backgrounds. This class
 * is responsible for preloading them.
 *
 * @author Raivo Laanemets
 */
public class Backgrounds {	
	public static final String BACKGROUND_MESSAGES = "/screen_message.png";
	public static final String BACKGROUND_CHAT = "/screen_chat.png";
	public static final String BACKGROUND_FIELD = "/screen_game.png";
	
	private AppletContext appletContext;

	/**
	 * Creates new instance of backgrounds manager.
	 */
	public Backgrounds(AppletContext appletContext) {
		this.appletContext = appletContext;
	}
	
	public Image getBackgound(String name) {
		return appletContext.getImage(name);
	}

}
