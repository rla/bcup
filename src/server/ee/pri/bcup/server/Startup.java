package ee.pri.bcup.server;

import java.io.IOException;

import org.apache.log4j.extras.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Main class that initializes the Spring context of BCup server
 * and then starts the server.
 */
public class Startup {
	
	// FIXME should take from classpath
	public static void main(String[] args) throws IOException {
		DOMConfigurator.configure("conf/server/log4j.xml");
		ApplicationContext context = new FileSystemXmlApplicationContext("conf/server/server.xml");
		context.getBean("serverContext");
	}
}
