package ee.pri.bcup.common.model.pool.table;


public enum BallType {
	
	WHITE(false, true, null, "/balls/white.png"),
	
	BALL1(false, false, BallOwnershipKind.A, "/balls/1.png"),
	BALL2(false, false, BallOwnershipKind.A, "/balls/2.png"),
	BALL3(false, false, BallOwnershipKind.A, "/balls/3.png"),
	BALL4(false, false, BallOwnershipKind.A, "/balls/4.png"),
	BALL5(false, false, BallOwnershipKind.A, "/balls/5.png"),
	BALL6(false, false, BallOwnershipKind.A, "/balls/6.png"),
	BALL7(false, false, BallOwnershipKind.A, "/balls/7.png"),
	
	BALL8(true, false, null, "/balls/8.png"),
	
	BALL9(false, false, BallOwnershipKind.B, "/balls/9.png"),
	BALL10(false, false, BallOwnershipKind.B, "/balls/10.png"),
	BALL11(false, false, BallOwnershipKind.B, "/balls/11.png"),
	BALL12(false, false, BallOwnershipKind.B, "/balls/12.png"),
	BALL13(false, false, BallOwnershipKind.B, "/balls/13.png"),
	BALL14(false, false, BallOwnershipKind.B, "/balls/14.png"),
	BALL15(false, false, BallOwnershipKind.B, "/balls/15.png");
	
	private String imageName;
	private boolean black;
	private boolean white;
	private BallOwnershipKind kind;
	
	private BallType(boolean black, boolean white, BallOwnershipKind kind, String imageName) {
		this.black = black;
		this.white = white;
		this.kind = kind;
		this.imageName = imageName;
	}

	public boolean isBlack() {
		return black;
	}

	public boolean isWhite() {
		return white;
	}

	public BallOwnershipKind getKind() {
		return kind;
	}

	public String getImageName() {
		return imageName;
	}
	
}
