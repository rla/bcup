package ee.pri.bcup.common.model.pool.table;

import java.util.ArrayList;
import java.util.List;

import ee.pri.bcup.common.model.GameType;

/**
 * Data object for representing pool table with holes, walls and balls.
 * 
 * @author Raivo Laanemets
 */
public class Table {
	private List<Ball> balls = new ArrayList<Ball>();
	private List<Wall> walls = new ArrayList<Wall>();
	private List<Hole> holes = new ArrayList<Hole>();
	private Ball whiteBall;
	
	private List<Ball> ballsInHoles = new ArrayList<Ball>();
	
	private GameType gameType;
	
	public Table(GameType gameType) {
		this.gameType = gameType;
		
		constructWalls();
		constructHoles();
		constructStartPositions();
	}
	
	/**
	 * Resets the table into start position.
	 */
	public void reset() {
		balls = new ArrayList<Ball>();
		ballsInHoles = new ArrayList<Ball>();
		constructStartPositions();
	}
	
	/**
	 * Cleans holes from balls.
	 */
	public void cleanHoles() {
		ballsInHoles = new ArrayList<Ball>();
	}
	
	private void constructStartPositions() {
		int baseX = 445;
		int baseY = 164;
		int incX = 18;
		int incY = 11;
		
		Ball whiteBall = new Ball(new DoubleTuple(100, baseY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.WHITE);
		balls.add(whiteBall);
		this.whiteBall = whiteBall;
		
		balls.add(new Ball(new DoubleTuple(baseX, baseY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL1));
		
		balls.add(new Ball(new DoubleTuple(baseX + incX, baseY - incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL12));
		balls.add(new Ball(new DoubleTuple(baseX + incX, baseY + incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL7));
		
		if (gameType == GameType.POOL_8BALL) {
			balls.add(new Ball(new DoubleTuple(baseX + 2 * incX, baseY - 2 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL9));
			balls.add(new Ball(new DoubleTuple(baseX + 2 * incX, baseY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL8));
			balls.add(new Ball(new DoubleTuple(baseX + 2 * incX, baseY + 2 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL15));
			
			balls.add(new Ball(new DoubleTuple(baseX + 3 * incX, baseY - 3 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL14));
			balls.add(new Ball(new DoubleTuple(baseX + 3 * incX, baseY - incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL3));
			balls.add(new Ball(new DoubleTuple(baseX + 3 * incX, baseY + incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL10));
			balls.add(new Ball(new DoubleTuple(baseX + 3 * incX, baseY + 3 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL6));
			
			balls.add(new Ball(new DoubleTuple(baseX + 4 * incX, baseY - 4 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL5));
			balls.add(new Ball(new DoubleTuple(baseX + 4 * incX, baseY - 2 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL4));
			balls.add(new Ball(new DoubleTuple(baseX + 4 * incX, baseY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL13));
			balls.add(new Ball(new DoubleTuple(baseX + 4 * incX, baseY + 2 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL2));
			balls.add(new Ball(new DoubleTuple(baseX + 4 * incX, baseY + 4 * incY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL11));
		} else {
			balls.add(new Ball(new DoubleTuple(baseX + 2 * incX, baseY), 10, new DoubleTuple(0.0, 0.0), 1.0, BallType.BALL8));
		}
		
	}
	
	private void constructWalls() {
		// Upper side
		walls.add(new Wall(new DoubleTuple(12, 12), new DoubleTuple(55, 30)));
		walls.add(new Wall(new DoubleTuple(55, 30), new DoubleTuple(285, 30)));
		walls.add(new Wall(new DoubleTuple(285, 30), new DoubleTuple(310, 10)));
		walls.add(new Wall(new DoubleTuple(310, 10), new DoubleTuple(335, 30)));
		walls.add(new Wall(new DoubleTuple(335, 30), new DoubleTuple(565, 30)));
		walls.add(new Wall(new DoubleTuple(565, 30), new DoubleTuple(608, 12)));
		
		// Right side
		walls.add(new Wall(new DoubleTuple(608, 12), new DoubleTuple(590, 55)));
		walls.add(new Wall(new DoubleTuple(590, 55), new DoubleTuple(590, 275)));
		walls.add(new Wall(new DoubleTuple(590, 275), new DoubleTuple(608, 318)));
		
		// Bottom side
		walls.add(new Wall(new DoubleTuple(608, 318), new DoubleTuple(565, 300)));
		walls.add(new Wall(new DoubleTuple(565, 300), new DoubleTuple(335, 300)));
		walls.add(new Wall(new DoubleTuple(335, 300), new DoubleTuple(310, 320)));
		walls.add(new Wall(new DoubleTuple(310, 320), new DoubleTuple(285, 300)));
		walls.add(new Wall(new DoubleTuple(285, 300), new DoubleTuple(55, 300)));
		walls.add(new Wall(new DoubleTuple(55, 300), new DoubleTuple(12, 318)));
		
		// Left side
		walls.add(new Wall(new DoubleTuple(12, 318), new DoubleTuple(30, 275)));
		walls.add(new Wall(new DoubleTuple(30, 275), new DoubleTuple(30, 55)));
		walls.add(new Wall(new DoubleTuple(30, 55), new DoubleTuple(12, 12)));
	}
	
	private void constructHoles() {
		holes.add(new Hole(new DoubleTuple(24, 24), 16, this));
		holes.add(new Hole(new DoubleTuple(310, 20), 16, this));
		holes.add(new Hole(new DoubleTuple(596, 24), 16, this));
		
		holes.add(new Hole(new DoubleTuple(596, 306), 16, this));
		holes.add(new Hole(new DoubleTuple(310, 310), 16, this));
		holes.add(new Hole(new DoubleTuple(24, 306), 16, this));
	}

	public Ball getWhiteBall() {
		return whiteBall;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public List<Hole> getHoles() {
		return holes;
	}

	public List<Ball> getBallsInHoles() {
		return ballsInHoles;
	}

	public void fellIntoHole(Ball ball) {
		ballsInHoles.add(ball);
	}
	
	public Ball lookupBall(BallType type) {
		for (Ball ball : balls) {
			if (ball.getType() == type) {
				return ball;
			}
		}
		
		return null;
	}

}
