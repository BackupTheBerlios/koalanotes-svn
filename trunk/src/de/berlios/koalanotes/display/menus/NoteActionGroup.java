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
package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.ActionGroupHelper;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.NoteTransfer;
import de.berlios.koalanotes.display.ImageRegistry;
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
	private DisableableMenuManager noteMenu;
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
	private NoteMenuController.NoteCutOrCopyAttemptAction cutOrCopyAttemptAction;
	
	public NoteActionGroup(NoteMenuController nmc, ImageRegistry imageRegistry, NoteTree tree) {
		this.tree = tree;
		
		// construct actions
		newChildNote = new Action(nmc.new NoteNewChildAction(), "New Child Note");
		newSiblingNote = new Action(nmc.new NoteNewSiblingAction(), "New Sibling Note");
		cutNotes = new Action(nmc.new NoteCutAction(), "Cu&t");
		copyNotes = new Action(nmc.new NoteCopyAction(), "&Copy");
		pasteChildNotes = new Action(nmc.new NotePasteChildAction(), "&Paste As Child");
		pasteSiblingNotes = new Action(nmc.new NotePasteSiblingAction(), "Paste As Si&bling");
		deleteNotes = new Action(nmc.new NoteRemoveAction(), "&Delete");
		moveNoteLeft = new Action(nmc.new NoteMoveLeftAction(), "&Left");
		moveNoteRight = new Action(nmc.new NoteMoveRightAction(), "&Right");
		moveNoteUp = new Action(nmc.new NoteMoveUpAction(), "&Up");
		moveNoteDown = new Action(nmc.new NoteMoveDownAction(), "&Down");
		renameNote = new Action(nmc.new NoteRenameAction(), "Re&name");
		
		// construct keyboard listeners
		cutOrCopyAttemptAction = nmc.new NoteCutOrCopyAttemptAction();
		
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
		
		// set icons
		newChildNote.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.ACTION_ICON_NOTE_NEW_CHILD));
		newSiblingNote.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.ACTION_ICON_NOTE_NEW_SIBLING));
		
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
		tree.removeKeydownAction();
		
		noteMenu.setEnabled(tree.hasFocus());
		
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
				tree.addKeydownAction(cutOrCopyAttemptAction);
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
		noteMenu = new DisableableMenuManager("&Note");
		newNoteActions.addActionsToMenuManager(noteMenu);
		noteMenu.add(new Separator());
		clipboardActions.addActionsToMenuManager(noteMenu);
		noteMenu.add(new Separator());
		noteMenu.add(moveNoteSubmenuMenuBar);
		ungroupedActions.addActionsToMenuManager(noteMenu);
		menuBar.add(noteMenu);
	}
	
	public void populateCoolBar(CoolBarManager coolBar) {
		ToolBarManager tbm = new ToolBarManager();
		tbm.add(newChildNote);
		tbm.add(newSiblingNote);
		coolBar.add(tbm);
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
