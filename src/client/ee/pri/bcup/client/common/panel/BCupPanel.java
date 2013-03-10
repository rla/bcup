package ee.pri.bcup.client.common.panel;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.common.ClientPlayer;
import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Base class for BCUP ui components. Holds localized messages.
 */
public class BCupPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(BCupPanel.class);

	private AppletContext appletContext;

	public BCupPanel(AppletContext appletContext) {
		this.appletContext = appletContext;
		if (appletContext == null) {
			log.fatal("applet context is null");
			throw new RuntimeException("applet context is null");
		}
	}

	protected String getMessage(String key, Object... parameters) {
		return appletContext.getMessage(key, parameters);
	}

	protected AppletContext getAppletContext() {
		return appletContext;
	}
	
	protected ClientPlayer getPlayer() {
		return appletContext.getPlayer();
	}
	
	public BCupPanel bounds(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		return this;
	}
	
}
