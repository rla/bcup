package ee.pri.bcup.client.common.panel;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.common.Testing;

/**
 * Base class for scrollable panels. Uses custom scrollbar.
 * 
 * @author Raivo Laanemets
 */
public class BCupScrollPanel extends BCupTransparentPanel {
	private static final long serialVersionUID = 1L;
	
	private BCupScrollBar scrollBar = new BCupScrollBar();
	private JScrollPane scrollPane = new JScrollPane();

	public BCupScrollPanel(AppletContext appletContext) {
		super(appletContext);
		
		scrollPane.setOpaque(false);
		scrollPane.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		scrollPane.setBorder(null);
		
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getViewport().setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		
		scrollPane.setVerticalScrollBar(scrollBar.getScrollBar());
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		add(scrollPane);
		add(scrollBar);
		
		setBorder(Testing.createComponentBorder());
	}
	
	public void setComponent(JComponent component) {
		scrollPane.getViewport().add(component);
	}
	
	public BCupScrollPanel bounds(int x, int y, int width, int height) {
		scrollPane.setBounds(0, 0, width - 16, height);
		scrollBar.setBounds(width - 12, 0, 12, height);
		setBounds(x, y, width, height);
		return this;
	}
	
	public void scrollDown() {
		scrollBar.scrollDown();
	}

}
