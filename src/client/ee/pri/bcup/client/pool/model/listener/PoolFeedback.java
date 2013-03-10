package ee.pri.bcup.client.pool.model.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to maintain listeners for feedback to UI
 * from the game logics.
 * 
 * @author Raivo Laanemets
 */
public class PoolFeedback implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<HitListener> hitListeners = new ArrayList<HitListener>();
	private List<StatusListener> statusListeners = new ArrayList<StatusListener>();
	private List<GameUserMessageListener> gameUserMessageListeners = new ArrayList<GameUserMessageListener>();
	private List<TablePaintListener> tablePaintListeners = new ArrayList<TablePaintListener>();
	
	public void statusChanged() {
		for (StatusListener listener : statusListeners) {
			listener.updated();
		}
	}
	
	public void addStatusListener(StatusListener listener) {
		statusListeners.add(listener);
	}
	
	public void addGameUserMessageListener(GameUserMessageListener listener) {
		gameUserMessageListeners.add(listener);
	}
	
	public void addTablePaintListener(TablePaintListener listener) {
		tablePaintListeners.add(listener);
	}
	
	public void addHitListener(HitListener listener) {
		hitListeners.add(listener);
	}
	
	public void paintTable() {
		for (TablePaintListener listener : tablePaintListeners) {
			listener.paint();
		}
	}
	
	public void gameUserMessage(String message, boolean confirm) {
		for (GameUserMessageListener listener : gameUserMessageListeners) {
			listener.message(message, confirm);
		}
	}
	
	public void hideMessage() {
		for (GameUserMessageListener listener : gameUserMessageListeners) {
			listener.hideMessage();
		}
	}
	
	public void hit() {
		for (HitListener listener : hitListeners) {
			listener.hit();
		}
	}
	
}
