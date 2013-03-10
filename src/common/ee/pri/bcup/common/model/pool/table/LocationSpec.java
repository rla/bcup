package ee.pri.bcup.common.model.pool.table;

import lombok.Data;

@Data
public class LocationSpec {
	private Double x;
	private Double y;
	
	public static LocationSpec fromDoubleTuple(DoubleTuple doubleTuple) {
		LocationSpec spec = new LocationSpec();
		spec.x = doubleTuple.x;
		spec.y = doubleTuple.y;
		
		return spec;
	}
	
	public DoubleTuple toDoubleTuple() {
		return new DoubleTuple(x, y);
	}
}
