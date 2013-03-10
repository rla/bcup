package ee.pri.bcup.common.model.pool.table;

import java.awt.Graphics;
import java.io.Serializable;



public class Wall implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DoubleTuple p1;
	private DoubleTuple p2;

	public Wall(DoubleTuple p1, DoubleTuple p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public void draw(Graphics g) {}
	
	public void ballIntersection(Ball ball) {
		double xDelta = p2.x - p1.x;
		double yDelta = p2.y - p1.y;

		double u = ((ball.getLocation().x - p1.x) * xDelta + (ball.getLocation().y - p1.y) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		DoubleTuple closestPoint;
		if (u < 0) {
		    closestPoint = p1;
		} else if (u > 1) {
		    closestPoint = p2;
		} else {
		    closestPoint = new DoubleTuple(p1.x + u * xDelta, p1.y + u * yDelta);
		}
		
		double distance = closestPoint.distance(ball.getLocation());
		
		if (distance <= ball.getR()) {
			DoubleTuple wallNormal = new DoubleTuple(-yDelta, xDelta).normalize();
			ball.setSpeed(ball.getSpeed().subtract(wallNormal.multiply(2 * wallNormal.dot(ball.getSpeed()))));
			ball.setHasWallCollision(true);
			DoubleTuple toWallVector = closestPoint.subtract(ball.getLocation()).multiply(-ball.getR() / (double) 100.0);
			ball.setLocation(new DoubleTuple(ball.getLocation().x + toWallVector.x, ball.getLocation().y + toWallVector.y));
		}
	}
	
	public double getDistanceToPoint(DoubleTuple p) {
		double xDelta = p2.x - p1.x;
		double yDelta = p2.y - p1.y;

		final double u = ((p.x - p1.x) * xDelta + (p.y - p1.y) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final DoubleTuple closestPoint;
		if (u < 0) {
		    closestPoint = p1;
		} else if (u > 1) {
		    closestPoint = p2;
		} else {
		    closestPoint = new DoubleTuple(p1.x + u * xDelta, p1.y + u * yDelta);
		}

		return closestPoint.distance(p);
	}
	
}
