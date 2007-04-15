package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import de.berlios.koalanotes.controllers.Dispatcher;

public class NoteTabFolder {
	
	// SWT tab folder
	private CTabFolder tabFolder;
	
	public NoteTabFolder(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
	}
	
	public NoteTab getSelectedNoteTab() {
		if (tabFolder.getSelection() == null) {
			return null;
		} else {
			return (NoteTab) tabFolder.getSelection().getData();
		}
	}
	
	public void saveNoteTabs() {
		for (CTabItem tab : tabFolder.getItems()) {
			((NoteTab) tab.getData()).saveToNote();
		}
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
