package ee.pri.bcup.common.message.server;

/**
 * Message which is sent to indicate to the user that
 * another user with the same id has been already connected.
 * (One person tries to join multiple times).
 *
 * @author Raivo Laanemets
 */
public class ServerDuplicateUserMessage extends ServerMessage {}
