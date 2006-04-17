package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * There is an instance of Note Menu on the main menu bar and another instance is part
 * of the tree context menu.  At the moment both instances are the same, but should they
 * become more different in the future I would still like to try and keep them in this
 * single class if possible.
 */
public class NoteMenu {
	
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
	
	/**
	 * Construct the NoteMenu either to be its own menu off the main menu bar, or
	 * to be a section of the tree context menu.
	 */
	public NoteMenu(Menu parent, MenuHelper mh, boolean forMainMenuBar) {
		if (forMainMenuBar) {
			parent = mh.createSubMenu(parent, "&Note", NoteMenuController.INITIALISE_MENU);
		}
		appendAddNoteSubmenu(parent, mh);
		appendMoveNoteSubmenu(parent, mh);
		appendRemoveAndRenameMenuItems(parent, mh);
	}
	
	private void appendAddNoteSubmenu(Menu menu, MenuHelper mh) {
		Menu m = mh.createSubMenu(menu, "&Add Note");
		addNoteSubmenu = m.getParentItem();
		addNoteAfter = mh.createMenuItem(m, "&After", NoteMenuController.ADD_NOTE_AFTER);
		addNoteUnder = mh.createMenuItem(m, "&Under", NoteMenuController.ADD_NOTE_UNDER);
	}
	
	private void appendMoveNoteSubmenu(Menu menu, MenuHelper mh) {
		Menu m = mh.createSubMenu(menu, "&Move Note");
		moveNoteSubmenu = m.getParentItem();
		moveNoteLeft = mh.createMenuItem(m, "&Left", NoteMenuController.MOVE_NOTE_LEFT);
		moveNoteRight = mh.createMenuItem(m, "&Right", NoteMenuController.MOVE_NOTE_RIGHT);
		moveNoteUp = mh.createMenuItem(m, "&Up", NoteMenuController.MOVE_NOTE_UP);
		moveNoteDown = mh.createMenuItem(m, "&Down", NoteMenuController.MOVE_NOTE_DOWN);
	}
	
	private void appendRemoveAndRenameMenuItems(Menu menu, MenuHelper mh) {
		removeNotes = mh.createMenuItem(menu, "&Remove Notes", NoteMenuController.REMOVE_NOTES);
		renameNote = mh.createMenuItem(menu, "Re&name Note", NoteMenuController.RENAME_NOTE);
	}
	
	public void initialise(int noteTreeSelectionCount) {
		if (noteTreeSelectionCount == 1) {
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
