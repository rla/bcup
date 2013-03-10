package ee.pri.bcup.common.message.client;


/**
 * Message that client sends when he/she disconnects
 * from the server. (closes applet - should be when
 * Applet's destroy method is called)
 *
 * @author Raivo Laanemets
 */
public class ClientLeaveMessage extends ClientMessage {}
