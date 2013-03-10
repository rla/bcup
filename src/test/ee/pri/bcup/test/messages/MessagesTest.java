package ee.pri.bcup.test.messages;

import junit.framework.TestCase;
import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.client.AuthenticationRequestMessage;
import ee.pri.bcup.common.message.client.ClientJoinRoomMessage;
import ee.pri.bcup.common.message.client.ClientPingMessage;
import ee.pri.bcup.common.message.client.ClientRoomMessage;
import ee.pri.bcup.common.message.client.ClientShutdownMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeCancelMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeRejectMessage;
import ee.pri.bcup.common.message.server.AuthenticationFailResponseMessage;
import ee.pri.bcup.common.message.server.AuthenticationSuccessResponseMessage;
import ee.pri.bcup.common.message.server.ServerJoinRoomMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.ServerPingMessage;
import ee.pri.bcup.common.message.server.ServerRoomMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeFailMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeSuccessMessage;

public class MessagesTest extends TestCase {

	public void testServerRoomMessage() {
		ServerRoomMessage message = new ServerRoomMessage("hey", 1L);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testClientRoomMessage() {
		ClientRoomMessage message = new ClientRoomMessage("hey");
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testClientAuthenticationRequest() {
		AuthenticationRequestMessage message = new AuthenticationRequestMessage(1L, "abcd", "ee", 0);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testServerAuthenticationResponse() {
		AuthenticationSuccessResponseMessage message = new AuthenticationSuccessResponseMessage("john", 1L);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testServerAuthenticationResponseFail() {
		AuthenticationFailResponseMessage message = new AuthenticationFailResponseMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testClientJoinRoomMessage() {
		ClientJoinRoomMessage message = new ClientJoinRoomMessage(1L);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testServerJoinRoomMessage() {
		ServerJoinRoomMessage message = new ServerJoinRoomMessage("john", 1L);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testServerPartRoomMessage() {
		ServerPartRoomMessage message = new ServerPartRoomMessage(1L);
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testClientPingMessage() {
		ClientPingMessage message = new ClientPingMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testServerPingMessage() {
		ServerPingMessage message = new ServerPingMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testClientShutdownMessage() {
		ClientShutdownMessage message = new ClientShutdownMessage("abcd");
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testServerProposeSuccessfulMessage() {
		ServerProposeSuccessMessage message = new ServerProposeSuccessMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testServerProposeFailMessage() {
		ServerProposeFailMessage message = new ServerProposeFailMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testClientCancelProposeMessage() {
		ClientProposeCancelMessage message = new ClientProposeCancelMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
	
	public void testServerProposeByMessage() {
		ServerProposeByOtherMessage message = new ServerProposeByOtherMessage(1L);
		assertEquals(message, Message.parse(message.makeMessage()));
	}
	
	public void testClientRejectProposeMessage() {
		ClientProposeRejectMessage message = new ClientProposeRejectMessage();
		assertEquals(message.getClass(), Message.parse(message.makeMessage()).getClass());
	}
}
