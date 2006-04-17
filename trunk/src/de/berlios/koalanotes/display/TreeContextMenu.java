package de.berlios.koalanotes.display;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.TreeController;
import de.berlios.koalanotes.display.menus.MenuHelper;
import de.berlios.koalanotes.display.menus.NoteMenu;

public class TreeContextMenu {
	
	NoteMenu noteMenu;
	
	public TreeContextMenu(Control parent, Listener l) {
		MenuHelper mh = new MenuHelper(l);
		Menu menu = mh.createPopupMenu(parent, TreeController.INITIALISE_CONTEXT_MENU);
		noteMenu = new NoteMenu(menu, mh, false);
	}
	
	public void initialise(int noteTreeSelectionCount) {
		noteMenu.initialise(noteTreeSelectionCount);
	}
}
