package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.ActionContributionItem;
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
	private DisableableMenuManager addNoteSubmenuMenuBar;
	private DisableableMenuManager addNoteSubmenuTree;
	private DisableableMenuManager moveNoteSubmenuMenuBar;
	private DisableableMenuManager moveNoteSubmenuTree;
	
	// contribution items (need to keep track of contribution items for actions that can be invisible)
	private ActionContributionItem addNoteContributionMenuBar;
	private ActionContributionItem addNoteContributionTree;
	
	// add note actions
	private Action addNote; // only visible when the tree is empty
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
		addNote = new Action(d, NoteMenuController.ADD_NOTE_AFTER, "&Add");
		addNoteAfter = new Action(d, NoteMenuController.ADD_NOTE_AFTER, "&After");
		addNoteUnder = new Action(d, NoteMenuController.ADD_NOTE_UNDER, "&Under");
		moveNoteLeft = new Action(d, NoteMenuController.MOVE_NOTE_LEFT, "&Left");
		moveNoteRight = new Action(d, NoteMenuController.MOVE_NOTE_RIGHT, "&Right");
		moveNoteUp = new Action(d, NoteMenuController.MOVE_NOTE_UP, "&Up");
		moveNoteDown = new Action(d, NoteMenuController.MOVE_NOTE_DOWN, "&Down");
		removeNotes = new Action(d, NoteMenuController.REMOVE_NOTES, "&Remove Notes");
		renameNote = new Action(d, NoteMenuController.RENAME_NOTE, "Re&name Note");
		
		// set action shortcut keys
		moveNoteLeft.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_LEFT);
		moveNoteRight.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_RIGHT);
		moveNoteUp.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_UP);
		moveNoteDown.setAccelerator(SWT.CONTROL | SWT.SHIFT | SWT.ARROW_DOWN);
		removeNotes.setAccelerator(SWT.DEL);
		
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
		
		// construct action contribution items
		addNoteContributionMenuBar = new ActionContributionItem(addNote);
		addNoteContributionMenuBar.setVisible(false);
		addNoteContributionTree = new ActionContributionItem(addNote);
		addNoteContributionTree.setVisible(false);
		
		// construct submenus
		addNoteSubmenuMenuBar = new DisableableMenuManager("&Add Note");
		addNoteSubmenuTree = new DisableableMenuManager("&Add Note");
		moveNoteSubmenuMenuBar = new DisableableMenuManager("&Move Note");
		moveNoteSubmenuTree = new DisableableMenuManager("&Move Note");
		
		// populate submenus
		addNoteSubmenuAndActions.addActionsToMenuManager(addNoteSubmenuMenuBar);
		addNoteSubmenuAndActions.addActionsToMenuManager(addNoteSubmenuTree);
		addNoteSubmenuAndActions.add(addNoteSubmenuMenuBar);
		addNoteSubmenuAndActions.add(addNoteSubmenuTree);
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuMenuBar);
		moveNoteSubmenuAndActions.addActionsToMenuManager(moveNoteSubmenuTree);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuMenuBar);
		moveNoteSubmenuAndActions.add(moveNoteSubmenuTree);
	}
	
	public void update() {
		
		// STEP 1: Make sure the correct actions are visible.
		
		// If the tree is empty, only a single add action is needed, if not then a pair of add
		// actions should be visible.
		if (tree.isEmpty()) {
			allSubmenusAndActions.setEnabled(false);
			addNoteSubmenuMenuBar.setVisible(false);
			addNoteSubmenuTree.setVisible(false);
			addNoteContributionMenuBar.setVisible(true);
			addNoteContributionTree.setVisible(true);
			return;
		}
		if (addNoteContributionMenuBar.isVisible()) {
			addNoteSubmenuMenuBar.setVisible(true);
			addNoteSubmenuTree.setVisible(true);
			addNoteContributionMenuBar.setVisible(false);
			addNoteContributionTree.setVisible(false);
			return;
		}
		
		// STEP 2: Make sure the correct actions are enabled.
		
		// If the tree doesn't have focus, no actions from this group can be performed.
		if (!tree.hasFocus()) {
			allSubmenusAndActions.setEnabled(false);
		
		// If a single tree item is selected, any action can be performed.
		} else if (tree.getSelectionCount() == 1) {
			allSubmenusAndActions.setEnabled(true);
			removeNotes.setText("Remove Note");
			
		// Otherwise, most actions cannot be performed.
		} else {
			allSubmenusAndActions.setEnabled(false);
				
			// If multiple items are selected all that can be done is remove them.
			if (tree.getSelectionCount() > 0) {
				removeNotes.setEnabled(true);
				removeNotes.setText("Remove Notes");
			}
		}
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		noteMenu = new MenuManager("&Note");
		noteMenu.add(addNoteContributionMenuBar);
		noteMenu.add(addNoteSubmenuMenuBar);
		noteMenu.add(moveNoteSubmenuMenuBar);
		ungroupedActions.addActionsToMenuManager(noteMenu);
		menuBar.add(noteMenu);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
		treeContextMenu.add(addNoteContributionTree);
		treeContextMenu.add(addNoteSubmenuTree);
		treeContextMenu.add(moveNoteSubmenuTree);
		ungroupedActions.addActionsToMenuManager(treeContextMenu);
	}
}
