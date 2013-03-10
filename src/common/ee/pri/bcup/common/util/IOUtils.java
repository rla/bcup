package ee.pri.bcup.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Helper methods for IO.
 *
 * @author Raivo Laanemets
 */
public class IOUtils {

	public static void quietClose(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// This eats the exception.
			}
		}
	}

	public static void quietClose(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// This eats the exception.
			}
		}
	}
}
