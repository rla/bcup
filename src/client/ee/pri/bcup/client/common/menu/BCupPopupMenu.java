package ee.pri.bcup.client.common.menu;

import javax.swing.JPopupMenu;

import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Base class for BCUP popup menus. Holds messages.
 */
public class BCupPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	private AppletContext appletContext;

	public BCupPopupMenu(AppletContext appletContext) {
		this.appletContext = appletContext;
	}

	protected String getMessage(String key, Object... parameters) {
		return appletContext.getMessage(key, parameters);
	}

	public AppletContext getAppletContext() {
		return appletContext;
	}
	
}
