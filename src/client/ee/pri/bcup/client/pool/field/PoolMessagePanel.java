package ee.pri.bcup.client.pool.field;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ee.pri.bcup.client.common.panel.BCupPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.listener.GameUserMessageListener;

/**
 * A panel to display messages on the pool field. Messages displayed
 * through this panel will require confirmation (click to OK button).
 * 
 * @author Raivo Laanemets
 */
public class PoolMessagePanel extends BCupPanel implements GameUserMessageListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel messageLabel;
	private JButton button;
	
	public PoolMessagePanel(PoolAppletContext appletContext) {
		super(appletContext);
		
		setLayout(null);
		
		messageLabel = new JLabel();
		messageLabel.setBounds(10, 20, 200, 15);
		messageLabel.setForeground(Color.WHITE);
		add(messageLabel);
		
		button = new JButton("OK");
		button.setForeground(Color.WHITE);
		button.setBorder(null);
		button.setBackground(Color.BLACK);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				((PoolAppletContext) getAppletContext()).getGameContext().confirmUserMessage();
			}
		});
		button.setBounds(70, 50, 80, 30);
		add(button);
		
		setBackground(new Color(0.0f, 0.1f, 0.0f, 0.8f));
		setVisible(false);
		
		appletContext.getGameContext().getPoolFeedback().addGameUserMessageListener(this);
	}

	@Override
	public void message(final String message, final boolean confirm) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setVisible(confirm);
				messageLabel.setText(message);
				setVisible(true);
			}
		});
	}

	@Override
	public void hideMessage() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(false);
			}
		});
	}
	
}
