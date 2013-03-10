package ee.pri.bcup.client.common.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import ee.pri.bcup.common.Testing;

/**
 * Custom scroll bar for {@link BCupScrollPanel}.
 * 
 * @author Raivo Laanemets
 */
public class BCupScrollBar extends JPanel
	implements MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private JScrollBar scrollBar = new JScrollBar();
	private double pos = 1.0;
	private int scrollBitHeight = 8;
	
	public BCupScrollBar() {
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// FIXME scroll bar border color
		//setBackground(new Color(0xD4D400));
		setBorder(Testing.createComponentBorder());
	}
	
	protected void setPos(double pos) {
		this.pos = pos;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pos = e.getY() / (double) getBounds().height;
		
		int max = scrollBar.getMaximum();
		int min = scrollBar.getMinimum();

		scrollBar.setValue((int) (min + (max - min) * pos));
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		pos = e.getY() / (double) getBounds().height;
		
		int max = scrollBar.getMaximum();
		int min = scrollBar.getMinimum();

		scrollBar.setValue((int) (min + (max - min) * pos));
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	protected void paintComponent(Graphics g) {
		Rectangle bounds = getBounds();
		
		g.setColor(new Color(0xFFFFD1));
		g.fillRect(0, 0, bounds.width, bounds.height);
		
		g.setColor(new Color(0xBABA60));
		g.drawRect(0, 0, bounds.width-1, bounds.height-1);
		
		int start = (int) (pos * bounds.height);
		if (start < 0) {
			start = 0;
		}
		if (start > bounds.height - scrollBitHeight) {
			start = bounds.height - scrollBitHeight;
		}
		g.setColor(new Color(0x4A4A00));
		g.fillRect(0, start, bounds.width, scrollBitHeight);
	}

	public JScrollBar getScrollBar() {
		return scrollBar;
	}
	
	public void scrollDown() {
		setPos(1.0);
		repaint();
	}

}