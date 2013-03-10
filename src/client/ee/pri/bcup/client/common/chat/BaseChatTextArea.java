package ee.pri.bcup.client.common.chat;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class BaseChatTextArea extends JEditorPane {
	private static final long serialVersionUID = 1L;

	public BaseChatTextArea() {
		super("text/html", "<html><body></body></html>");
		setEditable(false);
		
		setOpaque(false);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		setBorder(null);
	}
	
	public void appendText(String text) {
		HTMLDocument doc = (HTMLDocument) getDocument();
		HTMLEditorKit kit = (HTMLEditorKit) getEditorKit();
		
		try {
			kit.insertHTML(
				doc,
				doc.getEndPosition().getOffset() - 1,
				"<font size=3>" + text + "</font>",
				0,
				0,
				null
			);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
