package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.TreeContextMenuController;
import de.berlios.koalanotes.controllers.TreeController;

public class TreeContextMenu {
	Menu menu;
	
	MenuItem addNoteSubmenu;
	MenuItem addNoteAfter;
	MenuItem addNoteUnder;
	
	MenuItem moveNoteSubmenu;
	MenuItem moveNoteLeft;
	MenuItem moveNoteRight;
	MenuItem moveNoteUp;
	MenuItem moveNoteDown;
	
	MenuItem removeNotes;
	MenuItem renameNote;
	
	public TreeContextMenu(Control parent, Listener l) {
		
		// menu
		menu = new Menu(parent);
		l.mapEvent(menu, SWT.Show, TreeController.INITIALISE_CONTEXT_MENU);
		parent.setMenu(menu);
		
		// add note submenu
		Menu addNoteSubmenuMenu = new Menu(menu);
		addNoteSubmenu = new MenuItem(menu, SWT.CASCADE);
		addNoteSubmenu.setText("Add Note");
		addNoteSubmenu.setMenu(addNoteSubmenuMenu);
		addNoteAfter = createMenuItem(addNoteSubmenuMenu, "After", l, TreeContextMenuController.ADD_NOTE_AFTER);
		addNoteUnder = createMenuItem(addNoteSubmenuMenu, "Under", l, TreeContextMenuController.ADD_NOTE_UNDER);
		
		// move note submenu
		Menu moveNoteSubmenuMenu = new Menu(menu);
		moveNoteSubmenu = new MenuItem(menu, SWT.CASCADE);
		moveNoteSubmenu.setText("Move Note");
		moveNoteSubmenu.setMenu(moveNoteSubmenuMenu);
		moveNoteLeft = createMenuItem(moveNoteSubmenuMenu, "Left", l, TreeContextMenuController.MOVE_NOTE_LEFT);
		moveNoteRight = createMenuItem(moveNoteSubmenuMenu, "Right", l, TreeContextMenuController.MOVE_NOTE_RIGHT);
		moveNoteUp = createMenuItem(moveNoteSubmenuMenu, "Up", l, TreeContextMenuController.MOVE_NOTE_UP);
		moveNoteDown = createMenuItem(moveNoteSubmenuMenu, "Down", l, TreeContextMenuController.MOVE_NOTE_DOWN);
		
		// remove note/s and rename note
		removeNotes = createMenuItem(menu, "Remove Notes", l, TreeContextMenuController.REMOVE_NOTES);
		renameNote = createMenuItem(menu, "Rename Note", l, TreeContextMenuController.RENAME_NOTE);
	}
	
	private MenuItem createMenuItem(Menu parent, String text,
	                            Listener l, String controllerMethod) {
		MenuItem menuItem = new MenuItem(parent, SWT.PUSH);
		menuItem.setText(text);
		l.mapEvent(menuItem, SWT.Selection, controllerMethod);
		return menuItem;
	}
	
	public void initialise(int selectionCount) {
		if (selectionCount == 1) {
			addNoteSubmenu.setEnabled(true);
			moveNoteSubmenu.setEnabled(true);
			removeNotes.setText("Remove Note");
			renameNote.setEnabled(true);
		} else {
			addNoteSubmenu.setEnabled(false);
			moveNoteSubmenu.setEnabled(false);
			removeNotes.setText("Remove Notes");
			renameNote.setEnabled(false);
		}
	}
}
