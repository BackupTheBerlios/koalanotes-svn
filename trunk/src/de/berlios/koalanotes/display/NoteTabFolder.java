package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.DocumentViewSettings;
import de.berlios.koalanotes.data.Note;

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
	
	public NoteTab addNoteTab(DisplayedNote displayedNote, Dispatcher d) {
		return new NoteTab(tabFolder, displayedNote, d);
	}
}
