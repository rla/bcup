package ee.pri.bcup.common.model;

/**
 * Possible roles for proposal.
 *
 * @author Raivo Laanemets
 */
public enum ProposeRole {

	/**
	 * In this role we are proposing to another player.
	 */
	INITIATOR,
	
	/**
	 * In this role we are proposed by another player.
	 */
	TARGET
}
