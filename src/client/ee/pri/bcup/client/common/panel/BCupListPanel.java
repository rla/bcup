package ee.pri.bcup.client.common.panel;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;

import ee.pri.bcup.client.common.context.AppletContext;

/**
 * Bace class for lists (players list, games list, etc).
 * 
 * @author Raivo Laanemets
 */
public abstract class BCupListPanel extends BCupScrollPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private JList list = new JList();
	private DefaultListModel model = new DefaultListModel();

	public BCupListPanel(AppletContext appletContext) {
		super(appletContext);
		
		list.setModel(model);
		list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(this);
		
		list.setOpaque(false);
		list.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		list.setBorder(null);
		
		setComponent(list);
	}
	
	/**
	 * Adds new element to the list.
	 */
	public void addElement(Object e) {
		model.addElement(e);
	}
	
	/**
	 * Removes the given element from the list.
	 */
	public void removeElement(Object e) {
		model.removeElement(e);
	}
	
	/**
	 * Returns the underlying JList component.
	 */
	public JList getList() {
		return list;
	}
	
	/**
	 * Returns true if no items has been selected.
	 */
	public boolean isSelectionEmpty() {
		return list.isSelectionEmpty();
	}
	
	/**
	 * Returns the selected item.
	 */
	public Object getSelectedValue() {
		return list.getSelectedValue();
	}
	
	public void clear() {
		model.clear();
	}
	
	protected abstract void mousePressedItem(MouseEvent e);

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
        if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
            && !isSelectionEmpty()) {

            mousePressedItem(e);
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
