package ee.pri.bcup.common.model.pool.table;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import ee.pri.bcup.common.util.ImageUtils;


public class Ball implements Serializable {
	private static final long serialVersionUID = 1L;

	public DoubleTuple location;
	private int r;
	public DoubleTuple speed;
	private DoubleTuple accelerationConstant = new DoubleTuple(0.002, 0.002);
	private Image image = null;
	private double mass;
	private BallType type;
	private boolean onTable = true;
	private Ball firstBallCollisionBeforeWallCollision = null;
	private boolean hasWallCollision = false;
	private double horisontalSpin;
	private double verticalSpin;
	private boolean visible = true;

	public Ball(DoubleTuple location, int r, DoubleTuple speed, double mass, BallType type) {
		this.location = location;
		this.r = r;
		this.speed = speed;
		this.mass = mass;
		this.type = type;
		this.image = ImageUtils.loadImage(type.getImageName());
	}

	public void draw(Graphics g) {
		if (visible) {
			g.drawImage(image, location.getXInt() - 14, location.getYInt() - 14, null);
		}
	}
	
	public void drawToLocation(Graphics g, int x, int y) {
		g.drawImage(image, x - 14, y - 14, null);
	}

	public DoubleTuple getLocation() {
		return location;
	}

	public void setLocation(DoubleTuple location) {
		this.location = location;
	}

	public DoubleTuple getSpeed() {
		return speed;
	}

	public void setSpeed(DoubleTuple speed) {
		this.speed = speed;
	}

	public int getR() {
		return r;
	}

	public void ballIntersection(Ball ball) {
		if (!onTable || !ball.onTable) {
			return;
		}
		double distance = location.distance(ball.location);
		if (distance < r + ball.r) {
			if (firstBallCollisionBeforeWallCollision == null
				&& !hasWallCollision) {
				
				firstBallCollisionBeforeWallCollision = ball;
			}
			
			double dx = ball.location.x - location.x;
			double dy = ball.location.y - location.y; 

			double angle = Math.atan2(dy, dx); 
			double sine = Math.sin(angle);
			double cosine = Math.cos(angle);

			DoubleTuple pos0 = new DoubleTuple(0.0, 0.0);
			DoubleTuple pos1 = DoubleTuple.rotateVector(dx, dy, sine, cosine, true);

			DoubleTuple vel0 = DoubleTuple.rotateVector(speed.x, speed.y, sine, cosine, true); 
			      
			DoubleTuple vel1 = DoubleTuple.rotateVector(ball.speed.x, ball.speed.y, sine, cosine, true);

			double vxTotal = vel0.x - vel1.x; 
			vel0.x = ((mass - ball.mass) * vel0.x + 2 * ball.mass * vel1.x) / (mass + ball.mass); 
			vel1.x = vxTotal + vel0.x; 
			      
			double absV = Math.abs(vel0.x) + Math.abs(vel1.x); 
			double overlap = (r + ball.r) - Math.abs(pos0.x - pos1.x); 
			pos0.x += vel0.x / absV * overlap; 
			pos1.x += vel1.x / absV * overlap; 
			     
			DoubleTuple pos0F = DoubleTuple.rotateVector(pos0.x, pos0.y, sine, cosine, false); 
			DoubleTuple pos1F = DoubleTuple.rotateVector(pos1.x, pos1.y, sine, cosine, false); 
			     
			ball.location.x = location.x + pos1F.x; 
			ball.location.y = location.y + pos1F.y; 
			location.x = location.x + pos0F.x; 
			location.y = location.y + pos0F.y; 
			     
			DoubleTuple vel0F = DoubleTuple.rotateVector(vel0.x, vel0.y, sine, cosine, false); 
			DoubleTuple vel1F = DoubleTuple.rotateVector(vel1.x, vel1.y, sine, cosine, false);
			
			speed.x = vel0F.x; 
			speed.y = vel0F.y; 
			ball.speed.x = vel1F.x; 
			ball.speed.y = vel1F.y; 
		}
	}
	
	public void friction() {
		if (!onTable) {
			return;
		}
		speed.x = speed.x - accelerationConstant.x * speed.x;
		speed.y = speed.y - accelerationConstant.y * speed.y;
		
		horisontalSpin -= accelerationConstant.x * horisontalSpin;
		
		if (!isMoving()) {
			speed.x = 0.0;
			speed.y = 0.0;
		}
	}
	
	public boolean isMoving() {
		return !onTable || Math.abs(speed.x) > 0.05 || Math.abs(speed.y) > 0.05;
	}

	public BallType getType() {
		return type;
	}

	public boolean isOnTable() {
		return onTable;
	}

	public void setOnTable(boolean onTable) {
		this.onTable = onTable;
	}

	public void setHasWallCollision(boolean hasWallCollision) {
		this.hasWallCollision = hasWallCollision;
	}

	public Ball getFirstBallCollisionBeforeWallCollision() {
		return firstBallCollisionBeforeWallCollision;
	}

	@Override
	public String toString() {
		return getType().toString();
	}

	public double getHorisontalSpin() {
		return horisontalSpin;
	}

	public void setHorisontalSpin(double horisontalSpin) {
		this.horisontalSpin = horisontalSpin;
	}

	public double getVerticalSpin() {
		return verticalSpin;
	}

	public void setVerticalSpin(double verticalSpin) {
		this.verticalSpin = verticalSpin;
	}

	public void spin() {
		DoubleTuple spinVector = speed.rotate(Math.PI / 2, false);
		speed = speed.add(spinVector.multiply(horisontalSpin));
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Ball && ((Ball) obj).type == type;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
