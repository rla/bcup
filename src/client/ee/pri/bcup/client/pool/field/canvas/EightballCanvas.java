package ee.pri.bcup.client.pool.field.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.field.PoolMessagePanel;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.client.pool.model.listener.HitListener;
import ee.pri.bcup.client.pool.model.listener.TablePaintListener;
import ee.pri.bcup.common.message.client.pool.ClientPoolSpeedMessage;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.DoubleTuple;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.Table;
import ee.pri.bcup.common.model.pool.table.TurnState;
import ee.pri.bcup.common.util.ImageUtils;

public class EightballCanvas extends JPanel
	implements
		MouseMotionListener,
		MouseListener,
		HitListener,
		TablePaintListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(EightballCanvas.class);

	private PoolAppletContext appletContext;
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private int dotX = -1;
	private int dotY = -1;
	private Rectangle startBounds = new Rectangle(45, 45, 150, 240);
	private Rectangle whitePlaceBound = new Rectangle(41, 42, 539, 248);
	private boolean doDrag = false;
	private HitStrengthIndicator hitStrengthIndicator;
	private boolean selectPlaceForWhite = true;
	private int width;
	private int height;
	private BufferedImage background = (BufferedImage) ImageUtils.loadImage("/game-pool-table.png");
	
	private Long timeForFrames = 0L;
	private Long frameCount = 0L;

	public EightballCanvas(PoolAppletContext appletContext) {
		
		setLayout(null);
		
		this.width = appletContext.getCanvasWidth();
		this.height = appletContext.getCanvasHeight();
		
		this.appletContext = appletContext;
		this.buffer = (BufferedImage) appletContext.createBuffer(width, height);
		
		hitStrengthIndicator = new HitStrengthIndicator(appletContext);
		
		bufferGraphics = buffer.getGraphics();
		
		((Graphics2D) bufferGraphics).setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		
		((Graphics2D) bufferGraphics).setRenderingHint(
			RenderingHints.KEY_ALPHA_INTERPOLATION,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
		);
		
		((Graphics2D) bufferGraphics).setRenderingHint(
			RenderingHints.KEY_COLOR_RENDERING,
			RenderingHints.VALUE_COLOR_RENDER_SPEED
		);
		
		((Graphics2D) bufferGraphics).setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
		);
		
		((Graphics2D) bufferGraphics).setRenderingHint(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_SPEED
		);
		
		if (appletContext.getGameContext().getPartyType() != GamePartyType.OBSERVER) {
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		
		appletContext.getGameContext().getPoolFeedback().addHitListener(this);
		appletContext.getGameContext().getPoolFeedback().addTablePaintListener(this);
		
		add(new PoolMessagePanel(appletContext).bounds((width - 220) / 2, 50, 220, 100));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(buffer, 0, 0, null);
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		DoubleTuple position = new DoubleTuple(e.getX(), e.getY());
		
		PoolGame context = appletContext.getGameContext();
		if (context.getHitState() == HitState.PLACE
			&& context.getTurnState() == TurnState.LEFT
			&& context.getGameState() == GameState.START_HIT) {
			
			Ball whiteBall = context.getTable().getWhiteBall();
			if (whiteBall.getLocation().distance(position) < whiteBall.getR()
				&& startBounds.contains(e.getX(), e.getY())) {
				whiteBall.setLocation(position);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		dotX = e.getX();
		dotY = e.getY();
		
		DoubleTuple position = new DoubleTuple(e.getX(), e.getY());
		PoolGame context = appletContext.getGameContext();
		
		if (context.getTurnState() == TurnState.LEFT
			&& context.getHitState() == HitState.PLACE
			&& context.isHadWhite()
			&& selectPlaceForWhite
			&& whitePlaceBound.contains(dotX, dotY)
			&& context.getGameState() != GameState.END
			&& context.getGameState() != GameState.START_HIT) {
			
			Ball whiteBall = context.getTable().getWhiteBall();
			for (Ball ball : context.getTable().getBalls()) {
				if (ball != whiteBall) {
					if (ball.getLocation().distance(position) < ball.getR() + whiteBall.getR() + 1) {
						return;
					}
				}
			}
			
			whiteBall.setOnTable(true);
			whiteBall.setVisible(true);
			whiteBall.setSpeed(new DoubleTuple(0.0, 0.0));
			whiteBall.setLocation(position);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		PoolGame context = appletContext.getGameContext();
		
		if (context.getTurnState() == TurnState.LEFT
			&& context.getHitState() == HitState.PLACE
			&& context.isHadWhite()
			&& selectPlaceForWhite
			&& whitePlaceBound.contains(dotX, dotY)
			&& context.getGameState() != GameState.END) {
					
			selectPlaceForWhite = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		PoolGame context = appletContext.getGameContext();
		
		if (context.getHitState() == HitState.PLACE
			&& context.getTurnState() == TurnState.LEFT
			&& context.getGameState() != GameState.END) {
			
			DoubleTuple position = new DoubleTuple(x, y);
			Ball whiteBall = context.getTable().getWhiteBall();
			doDrag = whiteBall.getLocation().distance(position) < whiteBall.getR();
			
			if (!doDrag && (e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				int locactionX = 0;
				int locactionY = 0;
				if (y > 70) {
					locactionY = y - 20;
				} else {
					locactionY = y + 10;
				}
				if (x < 500) {
					locactionX = x; 
				} else {
					locactionX = x - 100;
				}
				hitStrengthIndicator.setLocation(locactionX, locactionY);
				hitStrengthIndicator.reset();
				hitStrengthIndicator.setVisible(true);
				context.setHitState(HitState.STRENGTH);
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		PoolGame context = appletContext.getGameContext();
		
		if (context.getHitState() == HitState.PLACE
			&& context.getTurnState() == TurnState.LEFT) {
			
			if (doDrag && startBounds.contains(x, y)) {
				context.getTable().getWhiteBall().setLocation(new DoubleTuple(x, y));
			}
		} else if (context.getHitState() == HitState.STRENGTH) {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				hitStrengthIndicator.setVisible(false);
				context.setHitState(HitState.OTHER);
				Ball whiteBall = context.getTable().getWhiteBall();
				DoubleTuple location = whiteBall.getLocation();
				DoubleTuple vector = new DoubleTuple(x - location.x, y - location.y)
					.normalize()
					.multiply(hitStrengthIndicator.getStrenghtMultiplier());
				
				whiteBall.setSpeed(vector);
				double horisontalSpin = context.getSpinHint().calculateHorisontalSpin();
				double verticalSpin = context.getSpinHint().calculateVerticalSpin();
				whiteBall.setHorisontalSpin(horisontalSpin);
				
				log.debug("hit horisontal spin: " + horisontalSpin);
				
				context.hit();
				appletContext.getPlayer().send(new ClientPoolSpeedMessage(
					whiteBall.getLocation().x,
					whiteBall.getLocation().y,
					vector.x,
					vector.y,
					horisontalSpin,
					verticalSpin
				));
			}
		}
	}

	@Override
	public void hit() {
		selectPlaceForWhite = true;
	}

	@Override
	public void paint() {
		paintToBuffer();
		// Safe to call from other threads
		repaint();
	}
	
	private void paintToBuffer() {
		bufferGraphics.clearRect(0, 0, width, height);
		long start = System.nanoTime();
		
		bufferGraphics.drawImage(background, 0, 0, null);
		PoolGame context = appletContext.getGameContext();
		if (context == null) {
			return;
		}
		
		Table table = context.getTable();
		
		for (Ball ball : table.getBalls()) {
			ball.draw(bufferGraphics);
		}
		
		if (context.getHitState() == HitState.PLACE
			&& context.getTurnState() == TurnState.LEFT
			&& context.getGameState() != GameState.END
			&& context.getPartyType() != GamePartyType.OBSERVER) {
			
			bufferGraphics.setColor(Color.WHITE);
			bufferGraphics.fillOval(dotX-3, dotY-3, 6, 6);
		}
		
		if (context.getGameState() == GameState.START_HIT
			&& context.getTurnState() == TurnState.LEFT
			&& context.getPartyType() != GamePartyType.OBSERVER
			&& (context.getHitState() == HitState.PLACE
				|| context.getHitState() == HitState.STRENGTH)) {
			
			bufferGraphics.setColor(new Color(0.3f, 0.3f, 0.3f));
			bufferGraphics.drawLine(startBounds.x + startBounds.width, 30, startBounds.x + startBounds.width, 300);
		}

		if (hitStrengthIndicator.isVisible()) {
			hitStrengthIndicator.draw(bufferGraphics);
		}
		timeForFrames += System.nanoTime() - start;
		
		frameCount++;
		if (frameCount % 1000 == 0) {
			double t1 = ((double) timeForFrames / 1000000) / frameCount;
			log.debug("Frames: " + frameCount + " average time for frame: " + t1);
		}
	}
	
	public EightballCanvas bounds(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		return this;
	}

}
