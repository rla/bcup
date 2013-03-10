package ee.pri.bcup.client.pool.model.rules.xml;

import lombok.Data;

import org.apache.commons.jexl2.Script;

@Data
public class XmlRule {
	private String name;
	private String condition;
	private String effect;
	private Script conditionExpression;
	private Script effectExpression;
}
