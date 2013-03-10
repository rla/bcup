package ee.pri.bcup.client.pool.model.listener;

public interface GameUserMessageListener {
	void message(String message, boolean confirm);
	void hideMessage();
}
