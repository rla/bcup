package ee.pri.bcup.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ee.pri.bcup.common.model.pool.table.Ball;

@Data
public class Player implements Serializable, Comparable<Player> {
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String name;
	private boolean identified = false;
	private Long level;
	private Long id;
	private boolean game;
	private List<Ball> balls = new ArrayList<Ball>();
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int compareTo(Player o) {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Player ? ((Player) obj).id.equals(id) : false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	

}
