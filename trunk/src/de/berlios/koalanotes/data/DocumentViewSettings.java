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
