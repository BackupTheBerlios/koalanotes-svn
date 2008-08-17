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
package de.berlios.koalanotes.controllers;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;

/**
 * An ActionGroup is a group of KoalaNotes Actions that populates its Actions into the KoalaNotes
 * menus, and takes care of its Actions' state (eg. enabled/disabled) and the states of any menus
 * and submenus created by this ActionGroup.
 */
public interface ActionGroup {
	
	/**
	 * The update method is intended to be called when the state of the application changes, in
	 * order to update the contained Actions' states (eg. enabled/disabled), as well as the
	 * visibility of any of the relevant menus and submenus.
	 */
	public void update();
	
	/** Populate the top menu bar with actions from this ActionGroup. */
	public void populateMenuBar(MenuManager menuBar);
	
	/** Populate the tool bar with actions from this ActionGroup. */
	public void populateCoolBar(CoolBarManager coolBar);
	
	/** Populate the tree context menu with actions from this ActionGroup. */
	public void populateTreeContextMenu(MenuManager treeContextMenu);
}
