package ee.pri.bcup.client.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Class for loading and storing localized messages.
 */
public class Messages {
	public Properties messages;
	public String language;
	
	public Messages() {
		this.messages = new Properties();
		try {
			this.messages.load(new InputStreamReader(Messages.class.getResourceAsStream("/messages.properties"), "UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMessage(String key, Object... parameters) {
		if (language == null) {
			throw new RuntimeException("Language is null");
		}
		String propertyKey = key + "." + language;
		
		if (!messages.containsKey(propertyKey)) {
			throw new RuntimeException("Message " + propertyKey + " does not exist"); 
		}
		
		return MessageFormat.format(messages.getProperty(key + "." + language), parameters);
	}
}
