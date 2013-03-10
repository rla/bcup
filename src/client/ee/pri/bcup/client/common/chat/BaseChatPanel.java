package ee.pri.bcup.client.common.chat;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.BCupScrollPanel;

/**
 * Base class for chat panels. Chat panel contains
 * area for text and a scrollbar.
 * 
 * @author Raivo Laanemets
 */
public abstract class BaseChatPanel extends BCupScrollPanel {
	private static final long serialVersionUID = 1L;
	
	private BaseChatTextArea textArea = new BaseChatTextArea();
	
	public BaseChatPanel(AppletContext appletContext) {
		super(appletContext);
		setComponent(textArea);
	}
	
	protected void appendText(String text) {
		textArea.appendText(text);
		scrollDown();
	}

}
