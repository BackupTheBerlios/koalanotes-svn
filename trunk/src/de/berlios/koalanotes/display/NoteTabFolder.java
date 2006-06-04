package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class NoteTabFolder {
	private CTabFolder tabFolder;
	
	public NoteTabFolder(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
	}
	
	public void saveNoteTabs() {
		for (CTabItem tab : tabFolder.getItems()) {
			((NoteTab) tab.getData()).getDisplayedNote().getNote();
		}
	}
	
	public void closeNoteTabs() {
		for (CTabItem tab : tabFolder.getItems()) {
			((NoteTab) tab.getData()).dispose();
		}
	}
	
	public NoteTab addNoteTab(DisplayedNote displayedNote) {
		return new NoteTab(tabFolder, displayedNote);
	}
}
