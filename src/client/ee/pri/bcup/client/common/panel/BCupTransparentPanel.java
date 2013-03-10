package ee.pri.bcup.client.common.panel;

import java.awt.Color;

import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Base class for transparent panels.
 * 
 * @author Raivo Laanemets
 */
public class BCupTransparentPanel extends BCupPanel {
	private static final long serialVersionUID = 1L;

	public BCupTransparentPanel(AppletContext appletContext) {
		super(appletContext);
		
		setLayout(null);
		setOpaque(false);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		setBorder(null);
	}
}
