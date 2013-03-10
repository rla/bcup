package ee.pri.bcup.common;

public class BCupException extends Exception {
	private static final long serialVersionUID = 1L;

	public BCupException() {
		super();
	}

	public BCupException(String message, Throwable cause) {
		super(message, cause);
	}

	public BCupException(String message) {
		super(message);
	}

	public BCupException(Throwable cause) {
		super(cause);
	}
	
}
