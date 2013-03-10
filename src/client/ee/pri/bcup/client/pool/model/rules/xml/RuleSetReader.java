package ee.pri.bcup.client.pool.model.rules.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RuleSetReader {
	
	public static List<XmlRule> readRules(InputStream inputStream) {
		try {
			return readRulesInternal(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("cannot read rules", e);
		}
	}

	// FIXME clean code
	private static List<XmlRule> readRulesInternal(InputStream inputStream)
		throws ParserConfigurationException, SAXException, IOException {
		
		List<XmlRule> rules = new ArrayList<XmlRule>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(inputStream);
		doc.getDocumentElement().normalize();

		NodeList nodeLst = doc.getElementsByTagName("reegel");

		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node fstNode = nodeLst.item(s);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				String condition = readCondition(fstElmnt);
				String effect = readResult(fstElmnt);
				
				XmlRule rule = new XmlRule();
				
				rule.setCondition(condition);
				rule.setEffect(effect);
				rule.setName(fstElmnt.getAttribute("nimi"));
				
				rules.add(rule);
			}
		}

		return rules;
	}
	
	private static String readCondition(Element element) {
		element = (Element) element.getElementsByTagName("tingimus").item(0);
		element = (Element) element.getElementsByTagName("avaldis").item(0);
		return element.getChildNodes().item(0).getNodeValue();
	}
	
	private static String readResult(Element element) {
		element = (Element) element.getElementsByTagName("tulemus").item(0);
		element = (Element) element.getElementsByTagName("avaldis").item(0);
		return element.getChildNodes().item(0).getNodeValue();
	}
}
