package ee.pri.bcup.client.pool.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import ee.pri.bcup.client.common.UIConfiguration;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.BCupTransparentPanel;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeAcceptMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeCancelByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeFailMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeRejectMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeSuccessMessage;
import ee.pri.bcup.common.model.Player;

/**
 * Class responsible for displaying game proposal
 * messages and control buttons.
 *
 * @author Raivo Laanemets
 */
public class ChatProposalPanel extends BCupTransparentPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton disposeButton;
	private JButton acceptButton;
	private JButton rejectButton;
	private JLabel textLabel;

	public ChatProposalPanel(final AppletContext appletContext) {
		super(appletContext);
		
		appletContext.processListeners(this, ListenerScope.APPLET);
		
		disposeButton = new JButton(getMessage("chat.proposal.button.dispose"));
		disposeButton.setBounds(100, 40, 86, 33);
		disposeButton.setVisible(false);
		disposeButton.setBackground(UIConfiguration.BUTTON_CANCEL_COLOR);
		disposeButton.setBorder(null);
		disposeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				disposeButton.setVisible(false);
				textLabel.setVisible(false);
				
				appletContext.getProposeContext().cancelPropose();
			}
			
		});
		add(disposeButton);
		
		acceptButton = new JButton(getMessage("chat.proposal.button.accept"));
		acceptButton.setBounds(100, 40, 86, 33);
		acceptButton.setVisible(false);
		acceptButton.setBackground(UIConfiguration.BUTTON_OK_COLOR);
		acceptButton.setBorder(null);
		acceptButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				textLabel.setVisible(false);
				acceptButton.setVisible(false);
				rejectButton.setVisible(false);
				disposeButton.setVisible(false);
				
				appletContext.getProposeContext().acceptPropose();
			}
			
		});
		add(acceptButton);
		
		rejectButton = new JButton(getMessage("chat.proposal.button.reject"));
		rejectButton.setBounds(196, 40, 86, 33);
		rejectButton.setVisible(false);
		rejectButton.setBackground(UIConfiguration.BUTTON_CANCEL_COLOR);
		rejectButton.setBorder(null);
		rejectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				textLabel.setVisible(false);
				rejectButton.setVisible(false);
				acceptButton.setVisible(false);
				
				appletContext.getProposeContext().rejectPropose();
			}
			
		});
		add(rejectButton);
		
		textLabel = new JLabel();
		textLabel.setForeground(Color.WHITE);
		textLabel.setBounds(5, 5, 495, 15);
		
		add(textLabel);
		
		// FIXME actually uses white border
		if (Testing.createComponentBorder() != null) {
			setBorder(BorderFactory.createLineBorder(Color.WHITE));
		}
	}
	
	/**
	 * This is called when server aknowledges that the proposal
	 * was successfully sent.
	 */
	@MessageReceiver(ServerProposeSuccessMessage.class)
	public void receive(ServerProposeSuccessMessage message) {
		Player player = getAppletContext().getProposeContext().getProposePartner();
		
		textLabel.setText(getMessage("chat.proposal.mytext", player.getName()));
		disposeButton.setVisible(true);
		textLabel.setVisible(true);
	}
	
	/**
	 * This is called when the propose to the given player fails.
	 * This happens when the player is already proposed by or proposes to someone.
	 */
	@MessageReceiver(ServerProposeFailMessage.class)
	public void receive(ServerProposeFailMessage message) {
		Player player = getAppletContext().getProposeContext().getProposePartner();
		
		textLabel.setVisible(true);
		textLabel.setText(getMessage("chat.proposal.fail", player.getName()));
		disposeButton.setVisible(false);
	}
	
	/**
	 * This method is called when other player proposes
	 * to the current player.
	 */
	@MessageReceiver(ServerProposeByOtherMessage.class)
	public void receive(ServerProposeByOtherMessage message) {
		Long id = message.getPlayerIdFrom();
		Player player = getAppletContext().getPlayer(id);
		
		textLabel.setVisible(true);
		textLabel.setText(getMessage("chat.proposal.text", player.getName(), message.getHitTimeout()));
		acceptButton.setVisible(true);
		rejectButton.setVisible(true);
	}
	
	/**
	 * This is called when the other player that i'm currently proposing,
	 * rejects the proposal.
	 */
	@MessageReceiver(ServerProposeRejectMessage.class)
	public void receive(ServerProposeRejectMessage message) {
		Player player = getAppletContext().getProposeContext().getProposePartner();
		
		textLabel.setVisible(true);
		textLabel.setText(getMessage("chat.proposal.reject", player));
		disposeButton.setVisible(false);
	}
	
	/**
	 * This is called when the other player disposed his/her propose.
	 */
	@MessageReceiver(ServerProposeCancelByOtherMessage.class)
	public void receive(ServerProposeCancelByOtherMessage message) {
		Player player = getAppletContext().getProposeContext().getProposePartner();
		
		textLabel.setVisible(true);
		textLabel.setText(getMessage("chat.proposal.cancel", player));
		acceptButton.setVisible(false);
		rejectButton.setVisible(false);
	}
	
	@MessageReceiver(ServerProposeAcceptMessage.class)
	public void receive(ServerProposeAcceptMessage message) {
		textLabel.setVisible(false);
		acceptButton.setVisible(false);
		rejectButton.setVisible(false);
		disposeButton.setVisible(false);
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		if (getAppletContext().getProposeContext().isProposeActive()
				&& message.getPlayerId().equals(getAppletContext().getProposeContext().getProposePartner().getId())) {
			
			textLabel.setVisible(true);
			acceptButton.setVisible(false);
			rejectButton.setVisible(false);
			disposeButton.setVisible(false);
			
			textLabel.setText(getMessage("chat.proposal.left"));
		}
	}
	
}
