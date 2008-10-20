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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTreeDragNDropController;
import de.berlios.koalanotes.display.menus.FileActionGroup;
import de.berlios.koalanotes.display.menus.HelpActionGroup;
import de.berlios.koalanotes.display.menus.NoteActionGroup;
import de.berlios.koalanotes.display.NoteTab;
import de.berlios.koalanotes.display.menus.FileMenuController;
import de.berlios.koalanotes.display.menus.HelpMenuController;
import de.berlios.koalanotes.display.menus.NoteMenuController;

/**
 * The controllers control KoalaNotes, they are the classes that listen and act and direct, they
 * control the life-cycles of their related display objects from birth to death, and they also
 * control the life-cycles of their child controllers.  The controllers live in a tree structure,
 * each controller will have a single parent and may spawn many child controllers.  MainController
 * sits at the top of the controller tree.
 * 
 * Controllers contain actions, these actions are the interface to the controller from the point of
 * view of the display widgets it controls, they are the call-backs from the display widgets to the
 * controller.  Child controllers will have a reference to their parent controller, and may call
 * methods on their parent and as well as make use of the actions of their parent.
 * 
 * @author alison
 */
public class MainController {

	// The one and only... Displayed Document!
	private DisplayedDocument dd;
	
	// groups of menu/toolbar items
	private List<ActionGroup> actionGroups;
	
	// Keep track of unsaved changes.
	private NoteTab noteTabWithModifyListener;
	
	public MainController(Shell shell) {
		
		// Blank document.
		Document blankDocument = new Document();
		new Note("Note 1", blankDocument, ""); // root note
		
		// Displayed Document
		dd = new DisplayedDocument(shell, blankDocument);
		dd.hookUpActions(new ExitingKoalaNotesAction(),
		                 new CoolBarRearrangedAction(),
		                 new ContextChangedAction(),
		                 new DocumentUpdatedAndContextChangedAction(),
		                 new DisplayTabAction(),
						 new NoteTreeDragNDropController(dd, this));
		
		// Controllers
		FileMenuController fmc = new FileMenuController(dd, this);
		NoteMenuController nmc = new NoteMenuController(dd, this);
		HelpMenuController hmc = new HelpMenuController(shell, dd.getImageRegistry());
		
		// Action Groups
		actionGroups = new LinkedList<ActionGroup>();
		actionGroups.add(new FileActionGroup(fmc, dd));
		actionGroups.add(new NoteActionGroup(nmc, dd.getImageRegistry(), dd.getTree()));
		actionGroups.add(new HelpActionGroup(hmc, dd.getImageRegistry()));
		for (ActionGroup ag : actionGroups) {
			ag.populateMenuBar(dd.getMenuBar());
			ag.populateCoolBar(dd.getCoolBar());
			ag.populateTreeContextMenu(dd.getTreeContextMenu());
		}
		
		contextChanged();		
		shell.layout();
	}
	
	public String saveBackup() {
		dd.getTabFolder().saveNoteTabs();
		return dd.getDocument().saveBackup();
	}
	
	/**
	 * Called when the document has been updated, for example when text has been entered into a tab.
	 */
	public void documentUpdated() {
		if (!dd.isModified()) {
			dd.setModified(true);
			contextChanged();
		}
	}
	
	/**
	 * Called when the document has been updated and the context has also been changed, for example
	 * when a note is deleted from the tree.
	 */
	public void documentUpdatedAndContextChanged() {
		if (!dd.isModified()) {
			dd.setModified(true);
		}
		contextChanged();
	}
	
	private void ensureNoteTabWithModifyListenerNull() {
		if (noteTabWithModifyListener != null) {
			if (!noteTabWithModifyListener.isDisposed()) {
				noteTabWithModifyListener.removeNoteModifiedAction();
			}
			noteTabWithModifyListener = null;
		}
	}
	
	/**
	 * Called when the context of the application has changed, for example a change in the selection
	 * in the tree, the selected tab, the contents of the clipboard, or when the document has
	 * unsaved changes.
	 */
	public void contextChanged() {
		for (ActionGroup ag : actionGroups) {
			ag.update();
		}
		dd.getMenuBar().updateAll(true);
		dd.getTreeContextMenu().updateAll(true);
		dd.getCoolBar().update(true);
		ensureNoteTabWithModifyListenerNull();
		if (!dd.isModified()) {
			noteTabWithModifyListener = dd.getTabFolder().getSelectedNoteTab();
			if (noteTabWithModifyListener != null) {
				NoteModifiedAction action = new NoteModifiedAction(noteTabWithModifyListener); 
				noteTabWithModifyListener.addNoteModifiedAction(action);
			}
		}
		dd.setStatusBarText("");
	}
	
	public class DocumentUpdatedAction implements INoArgsAction {
		public void invoke() {
			documentUpdated();
		}
	}
	
	public class DocumentUpdatedAndContextChangedAction implements INoArgsAction {
		public void invoke() {
			documentUpdatedAndContextChanged();
		}
	}
	
	public class ContextChangedAction implements INoArgsAction {
		public void invoke() {
			contextChanged();
		}
	}
	
	public class DisplayTabAction implements INoArgsAction {
		public void invoke() {
			List<DisplayedNote> selectedNotes = dd.getTree().getSelectedNotes();
			for (DisplayedNote selectedNote : selectedNotes) {
				selectedNote.displayTab(dd.getTabFolder(), new TabSelectedAction(), new TabDeselectedAction());
			}
		}
	}
	
	public class TabSelectedAction implements INoArgsAction {
		public void invoke() {
			contextChanged();
		}
	}
	
	public class TabDeselectedAction implements INoArgsAction {
		public void invoke() {
			ensureNoteTabWithModifyListenerNull();
		}
	}
	
	public class NoteModifiedAction implements INoArgsAction {
		NoteTab noteTab;
		private NoteModifiedAction(NoteTab noteTab) {this.noteTab = noteTab;}
		public void invoke() {
			if (noteTab.hasUnsavedChanges()) {
				ensureNoteTabWithModifyListenerNull();
				documentUpdated();
			}
		}
	}
	
	public class CoolBarRearrangedAction implements INoArgsAction {
		private CoolBarRearrangedAction() {}
		public void invoke() {
			dd.getCoolBar().refresh();
			dd.getShell().layout();
		}
	}
	
	/**
	 * Called when the application is about to be closed (either through the close button or the
	 * Exit menu item).
	 */
	public class ExitingKoalaNotesAction {
		private ExitingKoalaNotesAction() {}
		public void invoke(ShellEvent e) {
			if (dd.isModified()) {
				MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
				mb.setText("Confirm Exit");
				mb.setMessage("Exit even though there are unsaved changes?");
				if (mb.open() == SWT.CANCEL) {
					e.doit = false;
				}
			}
		}
	}
}
