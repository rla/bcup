package ee.pri.bcup.client.pool.field;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.BCupPanel;
import ee.pri.bcup.client.common.panel.BCupTransparentPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.SpinHint;
import ee.pri.bcup.common.Testing;

/**
 * Component for applying spin to white ball.
 *
 * @author Raivo Laanemets
 */
public class PoolSpinSelector extends BCupTransparentPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private int size = 60;
	
	private int selectX = 30;
	private int selectY = 30;

	public PoolSpinSelector(AppletContext appletContext) {
		super(appletContext);
		
		setBorder(Testing.createComponentBorder());
		addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.fillOval(0, 0, size - 1, size - 1);
		
		g.setColor(Color.RED);
		g.drawLine(selectX - 6, selectY, selectX + 6, selectY);
		g.drawLine(selectX, selectY - 6, selectX, selectY + 6);
		
		g.setColor(Color.BLACK);
		int centerX = size / 2;
		int centerY = size / 2;
		g.fillOval(centerX - 1, centerY - 1, 2, 2);
	}

	@Override
	public BCupPanel bounds(int x, int y, int width, int height) {
		size = width;
		if (width != height) {
			throw new IllegalArgumentException("Width and height must be equal in spin selector");
		}
		return super.bounds(x, y, width, height);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		
		if (p.distance(new Point(size / 2, size / 2)) < (size / 2) - 1) {
			selectX = p.x;
			selectY = p.y;
			
			SpinHint spinHint = ((PoolAppletContext) getAppletContext()).getGameContext().getSpinHint();
			
			spinHint.setMaxHorisontalSpin(size / 2);
			spinHint.setHorisontalSpin(selectX - (size / 2));
			
			spinHint.setMaxVerticalSpin(size / 2);
			spinHint.setVerticalSpin(selectY - (size / 2));
			
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

}
