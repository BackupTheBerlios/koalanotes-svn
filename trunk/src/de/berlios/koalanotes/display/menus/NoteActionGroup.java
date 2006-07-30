package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.NoteTree;

public class NoteActionGroup implements ActionGroup {
	private NoteTree tree;
	
	MenuManager noteMenu;
	MenuManager addNoteSubmenu;
	MenuManager moveNoteSubmenu;
	MenuManager addNoteTreeSubmenu;
	MenuManager moveNoteTreeSubmenu;
	
	Action addNoteAfter;
	Action addNoteUnder;
	
	Action moveNoteLeft;
	Action moveNoteRight;
	Action moveNoteUp;
	Action moveNoteDown;
	
	Action removeNotes;
	Action renameNote;
	
	public NoteActionGroup(Dispatcher d, NoteTree tree) {
		this.tree = tree;
		addNoteAfter = new Action(d, NoteMenuController.ADD_NOTE_AFTER, "&After");
		addNoteUnder = new Action(d, NoteMenuController.ADD_NOTE_UNDER, "&Under");
		moveNoteLeft = new Action(d, NoteMenuController.MOVE_NOTE_LEFT, "&Left");
		moveNoteRight = new Action(d, NoteMenuController.MOVE_NOTE_RIGHT, "&Right");
		moveNoteUp = new Action(d, NoteMenuController.MOVE_NOTE_UP, "&Up");
		moveNoteDown = new Action(d, NoteMenuController.MOVE_NOTE_DOWN, "&Down");
		removeNotes = new Action(d, NoteMenuController.REMOVE_NOTES, "&Remove Notes");
		removeNotes.setAccelerator(SWT.DEL);
		renameNote = new Action(d, NoteMenuController.RENAME_NOTE, "Re&name Note");
	}
	
	public void update() {
		if (tree.hasFocus()) {
			removeNotes.setEnabled(true);
			if (tree.getSelectionCount() == 1) {
				addNoteSubmenu.setVisible(true);
				moveNoteSubmenu.setVisible(true);
				removeNotes.setText("Remove Note");
				renameNote.setEnabled(true);
			} else {
				addNoteSubmenu.setVisible(false);
				moveNoteSubmenu.setVisible(false);
				removeNotes.setText("Remove Notes");
				renameNote.setEnabled(false);
			}
		} else {
			addNoteSubmenu.setVisible(false);
			moveNoteSubmenu.setVisible(false);
			removeNotes.setEnabled(false);
			renameNote.setEnabled(false);
		}
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		noteMenu = new MenuManager("&Note");
		menuBar.add(noteMenu);
		
		addNoteSubmenu = new MenuManager("&Add Note");
		addNoteSubmenu.add(addNoteAfter);
		addNoteSubmenu.add(addNoteUnder);
		noteMenu.add(addNoteSubmenu);
		
		moveNoteSubmenu = new MenuManager("&Move Note");
		moveNoteSubmenu.add(moveNoteLeft);
		moveNoteSubmenu.add(moveNoteRight);
		moveNoteSubmenu.add(moveNoteUp);
		moveNoteSubmenu.add(moveNoteDown);
		noteMenu.add(moveNoteSubmenu);
		
		noteMenu.add(removeNotes);
		noteMenu.add(renameNote);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
		addNoteTreeSubmenu = new MenuManager("&Add Note");
		addNoteTreeSubmenu.add(addNoteAfter);
		addNoteTreeSubmenu.add(addNoteUnder);
		treeContextMenu.add(addNoteTreeSubmenu);
		
		moveNoteTreeSubmenu = new MenuManager("&Move Note");
		moveNoteTreeSubmenu.add(moveNoteLeft);
		moveNoteTreeSubmenu.add(moveNoteRight);
		moveNoteTreeSubmenu.add(moveNoteUp);
		moveNoteTreeSubmenu.add(moveNoteDown);
		treeContextMenu.add(moveNoteTreeSubmenu);
		
		treeContextMenu.add(removeNotes);
		treeContextMenu.add(renameNote);
	}
}
