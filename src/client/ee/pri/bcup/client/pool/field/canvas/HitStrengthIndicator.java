package ee.pri.bcup.client.pool.field.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;

import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;

/**
 * Hit strength class for pool game (backend class).
 * 
 * @author Raivo Laanemets
 */
public class HitStrengthIndicator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Color STRENGTH_COLOR = new Color(0.0f, 1.0f, 0.0f, 0.7f);
	private static final Color STRENGTH_REMAINING_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.7f);
	
	private int hitStrength = 0;
	private int maxStrength = 100;
	private int width = 10;
	private int x;
	private int y;
	private boolean visible = false;
	private Timer timer;
	private long lastMax = 0;
	
	public HitStrengthIndicator(final PoolAppletContext appletContext) {
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (appletContext.getGameContext() == null
					|| appletContext.getGameContext().getGameState() == GameState.END) {
						
					timer.stop();
				}
				
				if (hitStrength < maxStrength) {
					hitStrength += 5;
				} else {
					if (lastMax == 0) {
						lastMax = System.currentTimeMillis();
					} else {
						if (System.currentTimeMillis() - lastMax > 1000
								&& appletContext.getGameContext() != null
								&& appletContext.getGameContext().getHitState() == HitState.STRENGTH) {
							
							appletContext.getGameContext().setHitState(HitState.PLACE);
							visible = false;
						}
					}
				}
			}
		});
		
		timer.start();
	}
	
	public void draw(Graphics g) {
		g.setColor(STRENGTH_COLOR);
		g.fillRect(x, y, hitStrength, width);
		g.setColor(STRENGTH_REMAINING_COLOR);
		g.fillRect(x + hitStrength, y, maxStrength - hitStrength, width);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		hitStrength = 0;
	}
	
	public double getStrenghtMultiplier() {
		return 1.5 * hitStrength / (double) maxStrength;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if (visible) {
			lastMax = 0;
		}
	}

}