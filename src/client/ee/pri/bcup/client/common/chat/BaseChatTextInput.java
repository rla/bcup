package ee.pri.bcup.client.common.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public abstract class BaseChatTextInput extends JTextField implements ActionListener {
	private static final long serialVersionUID = 1L;

	public BaseChatTextInput() {
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (!"".equals(getText())) {
			doWithText(getText());
		}
		setText("");
	}
	
	protected abstract void doWithText(String text);
	
	public BaseChatTextInput bounds(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		return this;
	}
}
