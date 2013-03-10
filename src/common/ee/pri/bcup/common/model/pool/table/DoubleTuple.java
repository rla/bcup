package ee.pri.bcup.common.model.pool.table;

import java.io.Serializable;

public final class DoubleTuple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public double x;
	public double y;
	
	public DoubleTuple(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public DoubleTuple() {
		this(0.0, 0.0);
	}
	
	public DoubleTuple multiply(double c) {
		return new DoubleTuple(x * c, y * c);
	}
	
	public DoubleTuple subtract(DoubleTuple tuple) {
		return new DoubleTuple(x - tuple.x, y - tuple.y);
	}
	
	public double distance(DoubleTuple tuple) {
		double x = this.x - tuple.x;
		double y = this.y - tuple.y;
		
		return Math.sqrt(x * x + y * y);
	}
	
	public DoubleTuple add(DoubleTuple tuple) {
		return new DoubleTuple(x + tuple.x, y + tuple.y);
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y);
	}
	
	public DoubleTuple normalize() {
		double length = length();
		
		return new DoubleTuple(x / length, y / length);
	}
	
	public double dot(DoubleTuple tuple) {
		return x * tuple.x + y * tuple.y;
	}
	
	public int getXInt() {
		return (int) x;
	}
	
	public int getYInt() {
		return (int) y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public DoubleTuple rotate(double angle, boolean reverse) {
		return rotateVector(x, y, Math.sin(angle), Math.cos(angle), reverse);
	}
	
	public static DoubleTuple rotateVector(double x, double y, double sine, double cosine, boolean reverse) {   
		if (reverse) {
			return new DoubleTuple(x * cosine + y * sine, y * cosine - x * sine);
		} else {
			return new DoubleTuple(x * cosine - y * sine, y * cosine + x * sine);
		} 
	}
	
}
