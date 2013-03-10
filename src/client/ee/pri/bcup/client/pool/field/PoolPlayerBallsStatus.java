package ee.pri.bcup.client.pool.field;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import ee.pri.bcup.client.common.panel.BCupTransparentPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.listener.StatusListener;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.model.pool.table.Ball;

public class PoolPlayerBallsStatus extends BCupTransparentPanel implements StatusListener {
	private static final long serialVersionUID = 1L;
	
	private boolean right;

	public PoolPlayerBallsStatus(PoolAppletContext appletContext, boolean right) {
		super(appletContext);
		
		this.right = right;
		
		appletContext.getGameContext().getPoolFeedback().addStatusListener(this);
		
		setBorder(Testing.createComponentBorder());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int y = 310;
		
		PoolAppletContext appletContext = (PoolAppletContext) getAppletContext();
		
		List<Ball> balls = right ?
				appletContext.getGameContext().getRight().getBalls() :
				appletContext.getGameContext().getLeft().getBalls();
				
		for (Ball ball : new ArrayList<Ball>(balls)) {
			ball.drawToLocation(g, 13, y);
			y -= 22;
		}
	}

	@Override
	public void updated() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}
	
}
