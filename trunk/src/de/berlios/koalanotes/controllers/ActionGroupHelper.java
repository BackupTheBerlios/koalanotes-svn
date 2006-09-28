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
