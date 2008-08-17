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
import org.eclipse.swt.widgets.Text;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;

public class NoteTab implements DisposeListener {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private Text text;
	
	public NoteTab(final CTabFolder parent, DisplayedNote displayedNote, Dispatcher d) {
		this.displayedNote = displayedNote;
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		tabItem.addDisposeListener(this);
		text = new Text(parent, SWT.MULTI | SWT.WRAP);
		text.setText(displayedNote.getNote().getText());
		text.addListener(SWT.FocusIn, new Listener(d, MainController.TAB_SELECTED));
		text.addListener(SWT.FocusOut, new Listener(d, MainController.TAB_DESELECTED));
		tabItem.setControl(text);
		select();
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	
	public void setName(String name) {tabItem.setText(name);}
	
	public void addModifyListener(Listener l) {
		text.addListener(SWT.Modify, l);
	}
	public void removeModifyListener(Listener l) {
		text.removeListener(SWT.Modify, l);
	}
	
	public void saveToNote() {
		displayedNote.getNoteWithoutUpdatingFromTab().setText(text.getText());
	}
	
	public boolean hasUnsavedChanges() {
		return !displayedNote.getNoteWithoutUpdatingFromTab().getText().equals(text.getText());
	}
	
	public void select() {
		tabItem.getParent().setSelection(tabItem);
	}
	
	public boolean isDisposed() {
		return tabItem.isDisposed();
	}
	
	public void dispose() {
		tabItem.dispose();
	}
	
	public void widgetDisposed(DisposeEvent e) {
		text.dispose();
		displayedNote.tabDisposed();
	}
}
