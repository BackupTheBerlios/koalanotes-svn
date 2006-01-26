package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import de.berlios.koalanotes.controllers.Listener;

public class NoteTabFolder {
	private CTabFolder tabFolder;
	private Listener l;
	
	public NoteTabFolder(Composite parent, Listener l) {
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
		this.l = l;
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
	
	public void addNoteTab(DisplayedNote displayedNote) {
		new NoteTab(tabFolder, l, displayedNote);
	}
}
