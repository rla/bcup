package ee.pri.bcup.common.model.pool.table;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;

public class Hole implements Serializable {
	private static final long serialVersionUID = 1L;

	private DoubleTuple location;
	private int r;
	private Table table;

	public Hole(DoubleTuple location, int r, Table table) {
		this.location = location;
		this.r = r;
		this.table = table;
	}

	public DoubleTuple getLocation() {
		return location;
	}

	public void setLocation(DoubleTuple location) {
		this.location = location;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}
	
	public void draw(Graphics g) {}

	public void ballCheck(final Ball ball) {
		if (!ball.isOnTable()) {
			return;
		}
		double distance = ball.getLocation().distance(location);
		if (distance < 18.0) {
			ball.setSpeed(new DoubleTuple(0.0, 0.0));
			ball.setLocation(location);
			Timer timer = new Timer(0, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ball.setVisible(false);
				}
			});
			timer.setInitialDelay(2000);
			timer.setRepeats(false);
			timer.start();
			// FIXME palli augu kohal
			ball.setOnTable(false);
			table.fellIntoHole(ball);
		}
	}

}
