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
package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.data.DocumentViewSettings;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.text.KoalaStyleManager;

public class NoteTabFolder {
	private DisplayedDocument displayedDocument;
	
	// SWT tab folder
	private CTabFolder tabFolder;
	
	public NoteTabFolder(Composite parent, DisplayedDocument displayedDocument) {
		this.displayedDocument = displayedDocument;
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
	}
	
	public NoteTab getSelectedNoteTab() {
		if (tabFolder.getSelection() == null) {
			return null;
		} else {
			return (NoteTab) tabFolder.getSelection().getData();
		}
	}
	public void setSelectedNoteTab(int index) {
		tabFolder.setSelection(index);
	}
	
	/**
	 * Save the contents of the NoteTabs back to their corresponding Notes, and save what tabs are
	 * open and which tab is selected back to the DocumentViewSettings.
	 */
	public void saveNoteTabs() {
		List<Note> notesOpenInTabs = new LinkedList<Note>();
		
		// Iterate through each tab.
		for (CTabItem tab : tabFolder.getItems()) {
			
			// Record the note for the tab.
			NoteTab noteTab = ((NoteTab) tab.getData());
			Note note = noteTab.getDisplayedNote().getNote();
			notesOpenInTabs.add(note);
			
			// Save the note tab contents back to the note.
			noteTab.saveToNote();
		}
		
		// Save the view settings.
		DocumentViewSettings viewSettings = displayedDocument.getDocument().getViewSettings();
		viewSettings.setNotesOpenInTabs(notesOpenInTabs);
		viewSettings.setSelectedTab(tabFolder.getSelectionIndex());
	}
	
	public void closeNoteTabs() {
		for (CTabItem tab : tabFolder.getItems()) {
			((NoteTab) tab.getData()).dispose();
		}
	}
	
	public NoteTab addNoteTab(DisplayedNote displayedNote,
	                          KoalaStyleManager koalaStyleManager,
	                          MainController.TabSelectedAction tsa,
	                          MainController.TabDeselectedAction tdsa,
	                          MainController.TextSelectionChangedAction tsca) {
		return new NoteTab(tabFolder, displayedNote, koalaStyleManager, tsa, tdsa, tsca);
	}
}
