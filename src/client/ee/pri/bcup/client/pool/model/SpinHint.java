package ee.pri.bcup.client.pool.model;


/**
 * Helper class to store spin selector value.
 *
 * @author Raivo Laanemets
 */
public class SpinHint {
	private int maxHorisontalSpin = 30;
	private int horisontalSpin = 0;
	
	private int maxVerticalSpin = 30;
	private int verticalSpin = 0;
	
	/**
	 * Returns the max horisontal spin value.
	 */
	public int getMaxHorisontalSpin() {
		return maxHorisontalSpin;
	}
	
	/**
	 * Sets the max horisontal spin value.
	 */
	public void setMaxHorisontalSpin(int maxHspin) {
		this.maxHorisontalSpin = maxHspin;
	}
	
	/**
	 * Returns the actual horisontal spin value.
	 */
	public int getHorisontalSpin() {
		return horisontalSpin;
	}
	
	/**
	 * Sets the actual horisontal spin value.
	 */
	public void setHorisontalSpin(int hSpin) {
		this.horisontalSpin = hSpin;
	}
	
	/**
	 * Returns the max vertical spin.
	 */
	public int getMaxVerticalSpin() {
		return maxVerticalSpin;
	}

	/**
	 * Sets the max vertical spin.
	 */
	public void setMaxVerticalSpin(int maxVerticalSpin) {
		this.maxVerticalSpin = maxVerticalSpin;
	}

	/**
	 * Returns the vertical spin (as in selector).
	 */
	public int getVerticalSpin() {
		return verticalSpin;
	}

	/**
	 * Sets the vertical spin.
	 */
	public void setVerticalSpin(int verticalSpin) {
		this.verticalSpin = verticalSpin;
	}

	/**
	 * Returns normalized horisontal spin value;
	 */
	public double calculateHorisontalSpin() {
		// Uses quadratic formula.
		double x = (double) horisontalSpin / maxHorisontalSpin;
		return -0.002 * (x * x) * Math.signum(x);
	}
	
	/**
	 * Returns normalized vertical spin value;
	 */
	public double calculateVerticalSpin() {
		// Uses quadratic formula.
		double x = (double) verticalSpin / maxVerticalSpin;
		return -0.002 * (x * x) * Math.signum(x);
	}
	
}
