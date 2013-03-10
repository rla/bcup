package ee.pri.bcup.common.model.pool.table;

public enum BallOwnershipKind {
	A,
	B;
	
	public BallOwnershipKind getOpposite() {
		if (this == A) {
			return B;
		} else {
			return A;
		}
	}
}
