package ee.pri.bcup.common.model.pool.table;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "type")
public class BallSpec implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BallType type;
	private LocationSpec location;
	private boolean mine;
	private boolean opponents;
}