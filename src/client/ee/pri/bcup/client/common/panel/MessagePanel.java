package ee.pri.bcup.client.common.panel;

import javax.swing.JLabel;

import ee.pri.bcup.client.common.Backgrounds;
import ee.pri.bcup.client.common.context.AppletContext;

public class MessagePanel extends BCupBackgroundedPanel {
	private static final long serialVersionUID = 1L;

	private JLabel messageLabel;

	public MessagePanel(AppletContext appletContext) {
		super(appletContext, Backgrounds.BACKGROUND_MESSAGES);

		messageLabel = new JLabel();
		messageLabel.setBounds(250, 300, 519, 20);
		add(messageLabel);
	}

	public void setMessage(String message) {
		messageLabel.setText(message);
	}

}
