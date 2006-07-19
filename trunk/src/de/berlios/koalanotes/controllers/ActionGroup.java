package de.berlios.koalanotes.controllers;

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
	
	/** Populate the tree context menu with actions from this ActionGroup. */
	public void populateTreeContextMenu(MenuManager treeContextMenu);
}
