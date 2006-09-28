package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Very much like its superclass, the JFace MenuManager, but it is possible to disable it.
 */
public class DisableableMenuManager extends MenuManager {
	
	/** Whether this menu is enabled. */
	private boolean enabled;
	
	/** The MenuItem that acts as the heading for this menu. */
	private MenuItem headingItem;
	
	public DisableableMenuManager(String text) {
		super(text);
		enabled = true;
	}
	
	/** Whether this menu is enabled. */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Set the enabled state of this menu.  If this menu has already been loaded then this method
	 * will directly update the enabled state for the menu heading item.
	 */
	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			if (headingItem != null) headingItem.setEnabled(enabled);
		}
	}
	
	/**
	 * Create the MenuItem that acts as the heading for this menu.  DisableableMenuManager needs to
	 * keep a reference to the heading to handle its enabled state.
	 */
	public void fill(Menu parent, int index) {
		super.fill(parent, index);
		headingItem = parent.getItem(index);
		headingItem.setEnabled(enabled);
	}
}
