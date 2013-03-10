package ee.pri.bcup.client.pool.field;

import java.io.IOException;

import ee.pri.bcup.client.common.Backgrounds;
import ee.pri.bcup.client.common.panel.BCupBackgroundedPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.field.canvas.EightballCanvas;
import ee.pri.bcup.client.pool.field.chat.PoolChatInput;
import ee.pri.bcup.client.pool.field.chat.PoolChatTextPanel;
import ee.pri.bcup.client.pool.field.chat.PoolObserversList;

/**
 * Panel to display pool game field.
 */
public class PoolFieldPanel extends BCupBackgroundedPanel {
	
	private static final long serialVersionUID = 1L;
	
	public PoolFieldPanel(PoolAppletContext appletContext) throws IOException {
		super(appletContext, Backgrounds.BACKGROUND_FIELD);
		
		int fieldX = (PoolAppletContext.WIDTH - appletContext.getCanvasWidth()) / 2;
		
		add(new PoolPlayerStatusPanel(appletContext, false).bounds(10, 10, 200, 60));
		add(new PoolPlayerStatusPanel(appletContext, true).bounds(800 - 200 - 10, 10, 200, 60));
		
		add(new PoolPlayerBallsStatus(appletContext, false).bounds(10, 100, 25, 325));
		add(new PoolPlayerBallsStatus(appletContext, true).bounds(800 - 10 - 25, 100, 25, 325));
		
		add(new PoolChatTextPanel(appletContext).bounds(10, 447, 800 - 10 - 200 - 20, 536 - 447));
		
		PoolObserversList observersList = new PoolObserversList(appletContext);
		add(observersList.bounds(800 - 10 - 200, 447, 200, 536 - 447));
		
		PoolChatInput input = new PoolChatInput(appletContext);
		input.setPlayers(observersList);
		
		add(input.bounds(10, 556, 800 - 20, 21));
		
		add(new EightballCanvas(appletContext).bounds(fieldX, 98, appletContext.getCanvasWidth(), appletContext.getCanvasHeight()));
		
		add(new ExitButton(appletContext).bounds(500, 10, 70, 20));
		add(new PoolSpinSelector(appletContext).bounds(PoolAppletContext.WIDTH / 2 - 30, 10, 60, 60));
	}

}
