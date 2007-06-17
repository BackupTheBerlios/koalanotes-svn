package de.berlios.koalanotes.data;

import java.util.LinkedList;
import java.util.List;

/**
 * The View Settings of a Document.  Saved in the document although in the future may be saved
 * separately.
 */
public class DocumentViewSettings {
	private List<Note> notesOpenInTabs;
	private int selectedTab;
	
	public DocumentViewSettings() {
		notesOpenInTabs = new LinkedList<Note>();
		selectedTab = -1;
	}
	
	/** Which notes are open in tabs, ordered from left tab to right tab. */
	public List<Note> getNotesOpenInTabs() {
		return notesOpenInTabs;
	}
	/** Which notes are open in tabs, ordered from left tab to right tab. */
	public void setNotesOpenInTabs(List<Note> notesOpenInTabs) {
		this.notesOpenInTabs = notesOpenInTabs;
	}
	
	/** Which tab is selected. */
	public int getSelectedTab() {
		return selectedTab;
	}
	/** Which tab is selected. */
	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}
}
