package ee.pri.bcup.client.pool.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ee.pri.bcup.client.common.panel.BCupTransparentPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.client.pool.model.listener.StatusListener;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.TurnState;

/**
 * Panel for displaying statuses of both competing players.
 * 
 * @author Raivo Laanemets
 */
public class PoolPlayerStatusPanel extends BCupTransparentPanel implements StatusListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel statusLabel;
	private JLabel timeLabel;
	private JLabel nameLabel;
	private String status = "0";
	private boolean right;
	private Timer timer;

	public PoolPlayerStatusPanel(PoolAppletContext appletContext, boolean right) {
		super(appletContext);
		
		this.right = right;
		
		PoolGame gameContext = appletContext.getGameContext();
		
		nameLabel = new JLabel(getMessage(
			"pool.field.status.player.name",
			right ?
				gameContext.getRight().getName() :
				gameContext.getLeft().getName()
		));
		
		nameLabel.setBounds(10, 5, 170, 15);
		timeLabel = new JLabel(getMessage("pool.field.status.player.time", ""));
		timeLabel.setBounds(10, 25, 170, 15);
		statusLabel = new JLabel(getMessage("pool.field.status.player.status", status));
		statusLabel.setBounds(10, 45, 170, 15);
		
		add(statusLabel);
		add(timeLabel);
		add(nameLabel);
		
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PoolAppletContext appletContext = (PoolAppletContext) getAppletContext();

				if (appletContext.getGameContext() == null
					|| appletContext.getGameContext().getGameState() == GameState.END) {
					
					timer.stop();
					return;
				}
				
				PoolGame gameContext = appletContext.getGameContext();
				if (gameContext.getHitState() == HitState.PLACE) {
					if (gameContext.getTurnState() == TurnState.LEFT) {
						if (PoolPlayerStatusPanel.this.right) {
							timeLabel.setText(getMessage("pool.field.status.player.time", getMessage("pool.field.status.time.other")));
						} else {
							timeLabel.setText(getMessage("pool.field.status.player.time", formatTime(gameContext)));
						}
					} else {
						if (PoolPlayerStatusPanel.this.right) {
							timeLabel.setText(getMessage("pool.field.status.player.time", formatTime(gameContext)));
						} else {
							timeLabel.setText(getMessage("pool.field.status.player.time", getMessage("pool.field.status.time.other")));
						}
					}
				}
			}
		});
		
		timer.start();
		
		appletContext.getGameContext().getPoolFeedback().addStatusListener(this);
		
		setBorder(Testing.createComponentBorder());
	}
	
	private String formatTime(PoolGame poolGame) {
		long elapsedTime = poolGame.getHitTimer().getRemainingTime() / 1000;
		
		if (elapsedTime < 0) {
			return "0:00";
		}
		
		long minutes = elapsedTime / 60;
		long seconds = elapsedTime % 60;
		
		return minutes + ":" + ((seconds < 10) ? "0" + seconds : seconds);
	}

	@Override
	public void updated() {
		if (right) {
			status = String.valueOf(((PoolAppletContext) getAppletContext()).getGameContext().getRight().getBalls().size());
		} else {
			status = String.valueOf(((PoolAppletContext) getAppletContext()).getGameContext().getLeft().getBalls().size());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText(getMessage("pool.field.status.player.status", status));
			}
		});
	}
	
}
