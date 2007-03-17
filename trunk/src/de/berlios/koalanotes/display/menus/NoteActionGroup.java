package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.ActionGroupHelper;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.NoteTransfer;
import de.berlios.koalanotes.display.NoteTree;

public class NoteActionGroup implements ActionGroup {
	
	// the note tree, from which NoteActionGroup derives its state
	private NoteTree tree;
	
	// the display, to access the clipboard from which NoteActionGroup derives its state
	private Display display;
	
	// groupings of actions and submenus
	private ActionGroupHelper allSubmenusAndActions;
	private ActionGroupHelper newNoteActions;
	private ActionGroupHelper moveNoteSubmenuAndActions;
	private ActionGroupHelper clipboardActions;
	private ActionGroupHelper ungroupedActions;
	
	// menu managers
	private MenuManager noteMenu;
	private DisableableMenuManager moveNoteSubmenuMenuBar;
	private DisableableMenuManager moveNoteSubmenuTree;
	
	// add note actions
	private Action newChildNote;
	private Action newSiblingNote;
	
	// clipboard actions
	private Action cutNotes;
	private Action copyNotes;
	private Action pasteChildNotes;
	private Action pasteSiblingNotes;
	private Action deleteNotes;
	
	// move note actions
	private Action moveNoteLeft;
	private Action moveNoteRight;
	private Action moveNoteUp;
	private Action moveNoteDown;
	
	// ungrouped actions
	private Action renameNote;
	
	// keyboard listeners
	private Listener cutOrCopyAttemptListener;
	
	public NoteActionGroup(Dispatcher d, NoteTree tree) {
		this.tree = tree;
		
		// construct actions
		newChildNote = new Action(d, NoteMenuController.NEW_CHILD_NOTE, "New Child Note");
		newSiblingNote = new Action(d, NoteMenuController.NEW_SIBLING_NOTE, "New Sibling Note");
		cutNotes = new Action(d, NoteMenuController.CUT_NOTE, "Cu&t");
		copyNotes = new Action(d, NoteMenuController.COPY_NOTE, "&Copy");
		pasteChildNotes = new Action(d, NoteMenuController.PASTE_CHILD_NOTE, "&Paste As Child");
		pasteSiblingNotes = new Action(d, NoteMenuController.PASTE_SIBLING_NOTE, "Paste As Si&bling");
		deleteNotes = new Action(d, NoteMenuController.DELETE_NOTES, "&Delete");
		moveNoteLeft = new Action(d, NoteMenuController.MOVE_NOTE_LEFT, "&Left");
		moveNoteRight = new Action(d, NoteMenuController.MOVE_NOTE_RIGHT, "&Right");
		moveNoteUp = new Action(d, NoteMenuController.MOVE_NOTE_UP, "&Up");
		moveNoteDown = new Action(d, NoteMenuController.MOVE_NOTE_DOWN, "&Down");
		renameNote = new Action(d, NoteMenuController.RENAME_NOTE, "Re&name");
		
		// construct keyboard listeners
		cutOrCopyAttemptListener = new Listener(d, NoteMenuController.CUT_OR_COPY_ATTEMPT);
		
		// set action shortcut keys
		cutNotes.setAccelerator(SWT.CONTROL | 'X');
		copyNotes.setAccelerator(SWT.CONTROL | 'C');
		pasteChildNotes.setAccelerator(SWT.CONTROL | 'V');
		pasteSiblingNotes.setAccelerator(SWT.CONTROL | 'B');
		moveNoteLeft.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_LEFT);
		moveNoteRight.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_RIGHT);
		moveNoteUp.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_UP);
		moveNoteDown.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_DOWN);
		deleteNotes.setAccelerator(SWT.DEL);
		
		// construct action group helpers
		allSubmenusAndActions = new ActionGroupHelper();
		newNoteActions = new ActionGroupHelper();
		clipboardActions = new ActionGroupHelper();
		moveNoteSubmenuAndActions = new ActionGroupHelper();
		ungroupedActions = new ActionGroupHelper();
		
		// populate action group helpers
		allSubmenusAndActions.add(newNoteActions);
		allSubmenusAndActions.add(moveNoteSubmenuAndActions);
		allSubmenusAndActions.add(clipboardActions);
		allSubmenusAndActions.add(ungroupedActions);
		newNoteActions.add(newChildNote);
		newNoteActions.add(newSiblingNote);
		clipboardActions.add(cutNotes);
		clipboardActions.add(copyNotes);
		clipboardActions.add(pasteChildNotes);
		clipboardActions.add(pasteSiblingNotes);
		clipboardActions.add(deleteNotes);
		moveNoteSubmenuAndActions.add(moveNoteLeft);
		moveNoteSubmenuAndActions.add(moveNoteRight);
		moveNoteSubmenuAndActions.add(moveNoteUp);
		moveNoteSubmenuAndActions.add(moveNoteDown);
		ungroupedActions.add(renameNote);
		
		// construct submenus
		moveNoteSubmenuMenuBar = new DisableableMenuManager("&Move");
		moveNoteSubmenuTree = new DisableableMenuManager("&Move");
		
		// populate submenus
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuMenuBar);
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuTree);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuMenuBar);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuTree);
	}
	
	public void update() {
		tree.removeKeydownListener(cutOrCopyAttemptListener);
		
		// If the tree doesn't have focus, no actions from this group can be performed.
		if (!tree.hasFocus()) {
			allSubmenusAndActions.setEnabled(false);
		
		// If the tree is empty, or there are no tree items selected, then only New and Paste can
		// be performed.
		} else if (tree.isEmpty() || tree.getSelectionCount() == 0) {
			allSubmenusAndActions.setEnabled(false);
			newChildNote.setEnabled(true);
			pasteChildNotes.setEnabled(updateHelperClipboardHasUseableContents());
		
		// If a single tree item is selected, any action can be performed.
		} else if (tree.getSelectionCount() == 1) {
			allSubmenusAndActions.setEnabled(true);
			pasteChildNotes.setEnabled(updateHelperClipboardHasUseableContents());
			pasteSiblingNotes.setEnabled(updateHelperClipboardHasUseableContents());
		
		// If multiple items are selected, they can be deleted, and if the selected items are all
		// under one parent, they can be cut and copied.
		} else {
			allSubmenusAndActions.setEnabled(false);
			deleteNotes.setEnabled(true);
			if (tree.isSelectionValidForMoving()) {
				cutNotes.setEnabled(true);
				copyNotes.setEnabled(true);
			} else {
				tree.addKeydownListener(cutOrCopyAttemptListener);
			}
		}
	}
	
	private boolean updateHelperClipboardHasUseableContents() {
		Object clipboardData = null;
		Clipboard cb = new Clipboard(display);
		try {
			clipboardData = cb.getContents(NoteTransfer.getInstance());
		} finally {
			cb.dispose();
		}
		if (clipboardData == null) {
			return false;
		}
		return (clipboardData instanceof Document);
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		noteMenu = new MenuManager("&Note");
		newNoteActions.addActionsToMenuManager(noteMenu);
		noteMenu.add(new Separator());
		clipboardActions.addActionsToMenuManager(noteMenu);
		noteMenu.add(new Separator());
		noteMenu.add(moveNoteSubmenuMenuBar);
		ungroupedActions.addActionsToMenuManager(noteMenu);
		menuBar.add(noteMenu);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
		newNoteActions.addActionsToMenuManager(treeContextMenu);
		treeContextMenu.add(new Separator());
		clipboardActions.addActionsToMenuManager(treeContextMenu);
		treeContextMenu.add(new Separator());
		treeContextMenu.add(moveNoteSubmenuTree);
		ungroupedActions.addActionsToMenuManager(treeContextMenu);
	}
}
