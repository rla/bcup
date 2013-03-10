package ee.pri.bcup.client.pool.model.rules.xml;

import java.util.List;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.log4j.Logger;

import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.hit.HitResult;
import ee.pri.bcup.client.pool.model.rules.PoolRuleExecutionContext;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.TurnState;

public class XmlRuleEvaluator {
	private static final Logger log = Logger.getLogger(XmlRuleEvaluator.class);
	
	private JexlEngine engine;
	private List<XmlRule> rules;
	private JexlContext jexlContext;
	
	public XmlRuleEvaluator() {
		rules = RuleSetReader.readRules(XmlRuleEvaluator.class.getResourceAsStream("8ball.xml"));
		engine = new JexlEngine();
		jexlContext = new MapContext();
		
		for (XmlRule rule : rules) {
			rule.setConditionExpression(engine.createScript(rule.getCondition()));
			rule.setEffectExpression(engine.createScript(rule.getEffect()));
		}
	}
	
	public void evaluate(PoolRuleExecutionContext context, PoolAppletContext appletContext, HitResult hitResult) {
		log.debug("evaluating game rules");
		
		jexlContext.set("context", context);
		jexlContext.set("game", appletContext.getGameContext());
		jexlContext.set("turn.LEFT", TurnState.LEFT);
		jexlContext.set("turn.RIGHT", TurnState.RIGHT);
		jexlContext.set("state.GAME", GameState.GAME);
		jexlContext.set("state.START_HIT", GameState.START_HIT);
		jexlContext.set("state.START_HIT_DONE", GameState.START_HIT_DONE);
		jexlContext.set("state.END", GameState.END);
		jexlContext.set("hit.PLACE", HitState.PLACE);
		jexlContext.set("party.OBSERVER", GamePartyType.OBSERVER);
		jexlContext.set("appletContext", appletContext);
		jexlContext.set("observerContext", appletContext.getObserverContext());
		jexlContext.set("hitResult", hitResult);
		
		System.out.println(appletContext.getGameContext().isHitTimeout());
		System.out.println(appletContext.getGameContext().getTurnState());
		
		for (XmlRule rule : rules) {
			if ((Boolean) rule.getConditionExpression().execute(jexlContext)) {
				log.debug("executing rule '" + rule.getName() + "'");
				rule.getEffectExpression().execute(jexlContext);
			}
		}
	}
	
	public static void main(String[] args) {
		new XmlRuleEvaluator();
	}
}
