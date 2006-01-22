package display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import controllers.Listener;

public class NoteTabFolder {
	private CTabFolder tabFolder;
	private Listener l;
	
	public NoteTabFolder(Composite parent, Listener l) {
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
		this.l = l;
	}
	
	public List<NoteTab> getOpenNoteTabs() {
		List<NoteTab> tabs = new LinkedList<NoteTab>();
		for (CTabItem tab : tabFolder.getItems()) {
			tabs.add((NoteTab) tab.getData());
		}
		return tabs;
	}
	
	public void addNoteTab(DisplayedNote displayedNote) {
		new NoteTab(tabFolder, l, displayedNote);
	}
}
