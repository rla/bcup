package ee.pri.bcup.client.pool.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Special button to exit the game.
 *
 * @author Raivo Laanemets
 */
// FIXME base class for buttons?
public class ExitButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;

	private AppletContext appletContext;
	
	public ExitButton(AppletContext appletContext) {
		this.appletContext = appletContext;
		setText(appletContext.getMessage("field.exit"));
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		appletContext.exitGame();
	}

	public JButton bounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		return this;
	}
	
	
}
