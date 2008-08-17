/**
 * Copyright (C) 2008 Alison Farlie
 * 
 * This file is part of KoalaNotes.
 * 
 * KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * KoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with KoalaNotes.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
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
			if (headingItem != null && !headingItem.isDisposed()) headingItem.setEnabled(enabled);
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
	
	@Override
	public void update() {
		super.update();
		if (headingItem != null && !headingItem.isDisposed() && headingItem.isEnabled() != enabled) {
			headingItem.setEnabled(enabled);
		}
	}
	
	@Override
	public void update(boolean arg0) {
		super.update(arg0);
		if (headingItem != null && !headingItem.isDisposed() && headingItem.isEnabled() != enabled) {
			headingItem.setEnabled(enabled);
		}
	}
	
	@Override
	protected void update(boolean arg0, boolean arg1) {
		super.update(arg0, arg1);
		if (headingItem != null && !headingItem.isDisposed() && headingItem.isEnabled() != enabled) {
			headingItem.setEnabled(enabled);
		}
	}
	
	@Override
	public void update(String arg0) {
		super.update(arg0);
		if (headingItem != null && !headingItem.isDisposed() && headingItem.isEnabled() != enabled) {
			headingItem.setEnabled(enabled);
		}
	}
	
	@Override
	public void updateAll(boolean arg0) {
		super.updateAll(arg0);
		if (headingItem != null && !headingItem.isDisposed() && headingItem.isEnabled() != enabled) {
			headingItem.setEnabled(enabled);
		}
	}
}
