package ee.pri.bcup.client.common.panel;

import java.awt.Graphics;
import java.awt.Image;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.common.Testing;

/**
 * Base class for panels with background image and no layout manager.
 * 
 * @author Raivo Laanemets
 */
public class BCupBackgroundedPanel extends BCupPanel {
	private static final long serialVersionUID = 1L;
	
	private Image background;

	public BCupBackgroundedPanel(AppletContext appletContext, String background) {
		super(appletContext);
		
		setLayout(null);
		if (background != null && Testing.useBackground()) {
			System.out.println("APPLETCTX: " + appletContext);
			this.background = appletContext.getBackground(background);
		} else {
			setBackground(Testing.backgroundColor());
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (background != null) {
			g.drawImage(background, 0, 0, null);
		}
	}

}
