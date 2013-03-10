package ee.pri.bcup.common;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Helper class for testing.
 * 
 * @author Raivo Laanemets
 */
public class Testing {

	/**
	 * Creates border for seeing different components. This
	 * is only for testing. It should return null for no borders.
	 */
	public static Border createComponentBorder() {
		return BorderFactory.createLineBorder(new Color(130, 130, 106));
	}
	
	public static boolean useBackground() {
		return true;
	}
	
	public static Color backgroundColor() {
		return Color.WHITE;
	}
}
