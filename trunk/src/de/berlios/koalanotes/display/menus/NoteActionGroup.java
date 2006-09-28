package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.ActionGroupHelper;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.NoteTree;

public class NoteActionGroup implements ActionGroup {
	
	// the note tree, from which NoteActionGroup derives its state
	private NoteTree tree;
	
	// groupings of actions and submenus
	private ActionGroupHelper allSubmenusAndActions;
	private ActionGroupHelper addNoteSubmenuAndActions;
	private ActionGroupHelper moveNoteSubmenuAndActions;
	private ActionGroupHelper ungroupedActions;
	
	// menu managers
	private MenuManager noteMenu;
	private DisableableMenuManager addNoteSubmenuMenubar;
	private DisableableMenuManager moveNoteSubmenuMenubar;
	private DisableableMenuManager addNoteSubmenuTree;
	private DisableableMenuManager moveNoteSubmenuTree;
	
	// add note actions
	private Action addNoteAfter;
	private Action addNoteUnder;
	
	// move note actions
	private Action moveNoteLeft;
	private Action moveNoteRight;
	private Action moveNoteUp;
	private Action moveNoteDown;
	
	// ungrouped actions
	private Action removeNotes;
	private Action renameNote;
	
	public NoteActionGroup(Dispatcher d, NoteTree tree) {
		this.tree = tree;
		
		// construct actions
		addNoteAfter = new Action(d, NoteMenuController.ADD_NOTE_AFTER, "&After");
		addNoteUnder = new Action(d, NoteMenuController.ADD_NOTE_UNDER, "&Under");
		moveNoteLeft = new Action(d, NoteMenuController.MOVE_NOTE_LEFT, "&Left");
		moveNoteRight = new Action(d, NoteMenuController.MOVE_NOTE_RIGHT, "&Right");
		moveNoteUp = new Action(d, NoteMenuController.MOVE_NOTE_UP, "&Up");
		moveNoteDown = new Action(d, NoteMenuController.MOVE_NOTE_DOWN, "&Down");
		removeNotes = new Action(d, NoteMenuController.REMOVE_NOTES, "&Remove Notes");
		removeNotes.setAccelerator(SWT.DEL);
		renameNote = new Action(d, NoteMenuController.RENAME_NOTE, "Re&name Note");
		
		// construct action group helpers
		allSubmenusAndActions = new ActionGroupHelper();
		addNoteSubmenuAndActions = new ActionGroupHelper();
		moveNoteSubmenuAndActions = new ActionGroupHelper();
		ungroupedActions = new ActionGroupHelper();
		
		// populate action group helpers
		allSubmenusAndActions.add(addNoteSubmenuAndActions);
		allSubmenusAndActions.add(moveNoteSubmenuAndActions);
		allSubmenusAndActions.add(ungroupedActions);
		addNoteSubmenuAndActions.add(addNoteAfter);
		addNoteSubmenuAndActions.add(addNoteUnder);
		moveNoteSubmenuAndActions.add(moveNoteLeft);
		moveNoteSubmenuAndActions.add(moveNoteRight);
		moveNoteSubmenuAndActions.add(moveNoteUp);
		moveNoteSubmenuAndActions.add(moveNoteDown);
		ungroupedActions.add(removeNotes);
		ungroupedActions.add(renameNote);
	}
	
	public void update() {
		
		// If the tree doesn't have focus, no actions from this group can be performed.
		if (!tree.hasFocus()) {
			allSubmenusAndActions.setEnabled(false);
			return;
		}
		
		// If a single tree item is selected, any action can be performed.
		if (tree.hasFocus() && tree.getSelectionCount() == 1) {
			allSubmenusAndActions.setEnabled(true);
			removeNotes.setText("Remove Note");
			
		// Otherwise, most actions cannot be performed.
		} else {
			allSubmenusAndActions.setEnabled(false);
			
			// An empty tree can be added to.
			if (tree.isEmpty()) {
				addNoteSubmenuAndActions.setEnabled(true);
				
			// If multiple items are selected all that can be done is remove them.
			} else if (tree.getSelectionCount() > 0) {
				removeNotes.setEnabled(true);
				removeNotes.setText("Remove Notes");
			}
		}
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		noteMenu = new MenuManager("&Note");
		
		addNoteSubmenuMenubar = new DisableableMenuManager("&Add Note");
		addNoteSubmenuAndActions.addActionsToMenuManager(addNoteSubmenuMenubar);
		addNoteSubmenuAndActions.add(addNoteSubmenuMenubar);
		noteMenu.add(addNoteSubmenuMenubar);
		
		moveNoteSubmenuMenubar = new DisableableMenuManager("&Move Note");
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuMenubar);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuMenubar);
		noteMenu.add(moveNoteSubmenuMenubar);
		
		ungroupedActions.addActionsToMenuManager(noteMenu);
		menuBar.add(noteMenu);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
		addNoteSubmenuTree = new DisableableMenuManager("&Add Note");
		addNoteSubmenuAndActions.addActionsToMenuManager(addNoteSubmenuTree);
		addNoteSubmenuAndActions.add(addNoteSubmenuTree);
		treeContextMenu.add(addNoteSubmenuTree);
		
		moveNoteSubmenuTree = new DisableableMenuManager("&Move Note");
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuTree);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuTree);
		treeContextMenu.add(moveNoteSubmenuTree);
		
		ungroupedActions.addActionsToMenuManager(treeContextMenu);
	}
}
