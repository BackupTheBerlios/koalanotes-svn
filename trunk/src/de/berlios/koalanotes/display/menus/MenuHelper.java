package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.TreeController;

/**
 * A helper used by the menu classes when they are creating themselves.
 */
public class MenuHelper {
	
	private Listener l;
	
	public MenuHelper(Listener l) {
		this.l = l;
	}
	
	public Menu createPopupMenu(Control parent, String onShow) {
		Menu menu = new Menu(parent);
		l.mapEvent(menu, SWT.Show, TreeController.INITIALISE_CONTEXT_MENU);
		parent.setMenu(menu);
		return menu;
	}
	
	public Menu createSubMenu(Menu parent, String text) {
		Menu menu = new Menu(parent);
		MenuItem menuHeader = new MenuItem(parent, SWT.CASCADE);
		menuHeader.setText(text);
		menuHeader.setMenu(menu);
		return menu;
	}
	
	public Menu createSubMenu(Menu parent, String text, String onShow) {
		Menu menu = createSubMenu(parent, text);
		l.mapEvent(menu, SWT.Show, onShow);
		return menu;
	}
	
	public MenuItem createMenuItem(Menu parent, String text, String onSelect) {
		MenuItem menuItem = new MenuItem(parent, SWT.CASCADE);
		menuItem.setText(text);
		l.mapEvent(menuItem, SWT.Selection, onSelect);
		return menuItem;
	}
	
	/**
	 * The accelerator is the keyboard shortcut for the menu item, for example
	 * SWT.CONTROL | SWT.SHIFT | 'A', which should have accelerator label "Ctrl-Shift-A".
	 */
	public void setAccelerator(MenuItem menuItem, int accelerator, String acceleratorLabel) {
		menuItem.setText(menuItem.getText() + " \t " + acceleratorLabel);
		menuItem.setAccelerator(accelerator);
	}
}
