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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTab;

public class MainController extends Controller {
	
	private DisplayedDocument dd;
	
	// Keep track of unsaved changes
	private NoteTab noteTabWithModifyListener;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainController.class, methodName);
	}
	
	public MainController(Dispatcher d, DisplayedDocument displayedDocument) {
		super(d);
		this.dd = displayedDocument;
	}
	
	/**
	 * Called when the context of the application has changed, for example a change in the selection
	 * in the tree, the selected tab, or the contents of the clipboard, or whether the document has
	 * unsaved changes.
	 */
	public static final String CONTEXT_CHANGED = getMethodDescriptor("contextChanged");
	public void contextChanged(Event e) {
		List<ActionGroup> actionGroups = dd.getActionGroups();
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
				NoteModifiedAction action = new NoteModifiedAction(this, dd, noteTabWithModifyListener); 
				noteTabWithModifyListener.addNoteModifiedAction(action);
			}
		}
		dd.setStatusBarText("");
	}
	
	public static final String DISPLAY_TAB = getMethodDescriptor("displayTab");
	public void displayTab(Event e) {
		List<DisplayedNote> selectedNotes = dd.getTree().getSelectedNotes();
		for (DisplayedNote selectedNote : selectedNotes) {
			selectedNote.displayTab(dd.getTabFolder(), d);
		}
	}
	
	public static final String TAB_SELECTED = getMethodDescriptor("tabSelected");
	public void tabSelected(Event e) {
		contextChanged(e);
	}
	
	public static final String TAB_DESELECTED = getMethodDescriptor("tabDeselected");
	public void tabDeselected(Event e) {
		ensureNoteTabWithModifyListenerNull();
	}
	
	public class NoteModifiedAction {
		MainController mc;
		DisplayedDocument dd;
		NoteTab noteTab;
		
		private NoteModifiedAction(MainController mc, DisplayedDocument dd, NoteTab noteTab) {
			this.mc = mc;
			this.dd = dd;
			this.noteTab = noteTab;
		}
		
		public void invoke() {
			if (noteTab.hasUnsavedChanges()) {
				mc.ensureNoteTabWithModifyListenerNull();
				mc.documentUpdated(dd, null);
			}
		}
	}
	
	private void ensureNoteTabWithModifyListenerNull() {
		if (noteTabWithModifyListener != null) {
			if (!noteTabWithModifyListener.isDisposed()) {
				noteTabWithModifyListener.removeNoteModifiedAction();
			}
			noteTabWithModifyListener = null;
		}
	}
	
	public static final String COOL_BAR_REARRANGED = getMethodDescriptor("coolBarRearranged");
	public void coolBarRearranged(Event e) {
		dd.getCoolBar().refresh();
		dd.getShell().layout();
	}
	
	/**
	 * Called when the application is about to be closed (either through the close button or the
	 * Exit menu item).
	 */
	public static final String EXITING_KOALA_NOTES = getMethodDescriptor("exitingKoalaNotes");
	public void exitingKoalaNotes(Event e) {
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
