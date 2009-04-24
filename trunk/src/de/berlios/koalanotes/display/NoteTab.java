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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.display.text.KoalaStyleManager;
import de.berlios.koalanotes.display.text.KoalaStyledText;

public class NoteTab implements DisposeListener {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private KoalaStyledText koalaStyledText;
	
	public NoteTab(CTabFolder parent,
	               DisplayedNote displayedNote,
	               KoalaStyleManager koalaStyleManager,
	               final MainController.TabSelectedAction tsa,
	               final MainController.TabDeselectedAction tdsa,
	               final MainController.TextSelectionChangedAction tsca) {
		this.displayedNote = displayedNote;
		
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		tabItem.addDisposeListener(this);
		
		koalaStyledText = new KoalaStyledText(parent, tabItem,
		                                      displayedNote.getNote().getText(),
		                                      koalaStyleManager,
		                                      tsa, tdsa, tsca);
		
		select();
	}
	
	// Disposing
	
	/** Call the dispose method of the tab item. */
	public void dispose() {
		tabItem.dispose();
	}
	
	/**
	 * Implements DisposeListener.widgetDisposed.  When the dispose event of the tab item is heard: 
	 * make sure the text is written back to the document, dispose the styled text widget and
	 * notify the DisplayedNote that the tab is no longer showing.
	 */
	public void widgetDisposed(DisposeEvent e) {
		saveToNote();
		koalaStyledText.dispose();
		displayedNote.tabDisposed();
	}
	
	public boolean isDisposed() {
		return tabItem.isDisposed();
	}
	
	// Getters and Setters
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	public KoalaStyledText getKoalaStyledText() {return koalaStyledText;}
	
	public void setName(String name) {tabItem.setText(name);}
	
	// Saving
	
	public void saveToNote() {
		displayedNote.getNoteWithoutUpdatingFromTab().setText(koalaStyledText.getText());
	}
	
	public boolean hasUnsavedChanges() {
		return !displayedNote.getNoteWithoutUpdatingFromTab().getText().equals(koalaStyledText.getText());
	}
	
	// Selecting
	
	public void select() {
		tabItem.getParent().setSelection(tabItem);
	}
	
	// Listeners
	
	public void addNoteModifiedAction(MainController.NoteModifiedAction action) {
		koalaStyledText.addNoteModifiedAction(action);
	}
	public void removeNoteModifiedAction() {
		koalaStyledText.removeNoteModifiedAction();
	}
}
