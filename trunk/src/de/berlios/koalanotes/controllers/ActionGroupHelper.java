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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;

import de.berlios.koalanotes.display.menus.DisableableMenuManager;

/**
 * An ActionGroupHelper holds a subset of the Actions and MenuManagers that are managed by an
 * ActionGroup, it can also hold other ActionGroupHelpers.  The ActionGroupHelper provides handy
 * helper methods to perform operations on all its contained Actions and MenuManagers, including the
 * ones contained in all its nested ActionGroupHelpers.
 */
public class ActionGroupHelper {
	private List<Action> actions;
	private List<DisableableMenuManager> menuManagers;
	private List<ActionGroupHelper> actionGroupHelpers;
	
	public ActionGroupHelper() {
		actions = new LinkedList<Action>();
		menuManagers = new LinkedList<DisableableMenuManager>();
		actionGroupHelpers = new LinkedList<ActionGroupHelper>();
	}
	
	public void add(Action action) {actions.add(action);}
	public void add(DisableableMenuManager menuManager) {menuManagers.add(menuManager);}
	public void add(ActionGroupHelper agh) {actionGroupHelpers.add(agh);}
	
	/**
	 * Set the enabled state of all contained Actions and MenuManagers.
	 */
	public void setEnabled(boolean enabled) {
		for (Action action : actions) {
			action.setEnabled(enabled);
		}
		for (DisableableMenuManager menuManager : menuManagers) {
			menuManager.setEnabled(enabled);
		}
		for (ActionGroupHelper agh : actionGroupHelpers) {
			agh.setEnabled(enabled);
		}
	}
	
	/**
	 * Add all the contained Actions to the given IMenuManager; this method does not add
	 * the Actions contained in nested ActionGroupHelpers.
	 */
	public void addActionsToMenuManager(IMenuManager menuManager) {
		for (Action action : actions) {
			menuManager.add(action);
		}
	}
}
