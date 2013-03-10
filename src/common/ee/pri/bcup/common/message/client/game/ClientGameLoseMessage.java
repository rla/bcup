package ee.pri.bcup.common.message.client.game;

/**
 * Client will send this message if he/she loses. Must be
 * sent before {@link ClientGameReturnedMessage}.
 *
 * @author Raivo Laanemets
 */
public class ClientGameLoseMessage extends ClientGameMessage {}