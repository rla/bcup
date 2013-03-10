package ee.pri.bcup.test.server;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ee.pri.bcup.server.exception.NoUserException;

public class ServerContextTest extends TestCase {

	public void testContext() throws NoUserException {
		ApplicationContext context = new FileSystemXmlApplicationContext("conf/server.xml");
		assertNotNull(context.getBean("server"));
	}
}
