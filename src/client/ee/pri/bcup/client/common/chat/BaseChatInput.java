package ee.pri.bcup.client.common.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.BCupListPanel;
import ee.pri.bcup.client.common.panel.BCupPanel;
import ee.pri.bcup.client.common.panel.BCupTransparentPanel;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.model.Player;

/**
 * Base class for text input. Consists of text input field,
 * a button, and a checkbox to send private messages.
 *
 * @author Raivo Laanemets
 */
public abstract class BaseChatInput extends BCupTransparentPanel {
	private static final long serialVersionUID = 1L;
	
	private BCupListPanel players;
	private JToggleButton checkBox;
	private BaseChatTextInput input;
	private JButton button;

	public BaseChatInput(final AppletContext appletContext) {
		super(appletContext);
		
		button = new JButton(getMessage("chat.send"));
		add(button);
		
		checkBox = new JCheckBox("");
		checkBox.setBorder(null);
		add(checkBox);
		
		input = new BaseChatTextInput() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void doWithText(String text) {
				if (checkBox.isSelected()) {
					if (players.isSelectionEmpty()) {
						JOptionPane.showMessageDialog(BaseChatInput.this, getMessage("chat.send.choose.user"));
					} else {
						Player to = (Player) players.getSelectedValue();
						if (!to.equals(appletContext.getPlayer())) {
							sendToSelected(to, text);
						}
					}
				} else {
					send(text);
				}
			}
		};
		input.setBackground(new Color(0xECECEC));
		input.setBorder(Testing.createComponentBorder());
		
		add(input);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkBox.isSelected()) {
					if (players.isSelectionEmpty()) {
						JOptionPane.showMessageDialog(BaseChatInput.this, getMessage("chat.send.choose.user"));
					} else {
						Player to = (Player) players.getSelectedValue();
						if (!to.equals(appletContext.getPlayer())) {
							sendToSelected(to, input.getText());
						}
					}
				} else {
					send(input.getText());
				}
			}
		});
	}

	/**
	 * Sets list component that is used for obtaining the user
	 * for which to send private message.
	 */
	public void setPlayers(BCupListPanel players) {
		this.players = players;
	}
	
	/**
	 * Override this to send to the selected player.
	 */
	protected abstract void sendToSelected(Player player, String text);
	
	/**
	 * Override this to send to whole room or game players.
	 */
	protected abstract void send(String text);

	@Override
	public BCupPanel bounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		
		input.setBounds(0, 0, width - 100, height);
		
		int checkWidth = 13;
		int checkHeight = 13;
		int checkX = width - 95;
		
		checkBox.setBounds(checkX, (height - checkHeight) / 2, checkWidth, checkHeight);
		
		int buttonX = checkX + checkWidth + 5;
		button.setBounds(buttonX, 0, width - buttonX, height);
		
		return this;
	}
	
	

}
