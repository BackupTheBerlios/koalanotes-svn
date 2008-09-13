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

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;

public class NoteTab implements DisposeListener {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private TextViewer textViewer;
	private TextListener textListener;
	
	public NoteTab(final CTabFolder parent, DisplayedNote displayedNote, Dispatcher d) {
		this.displayedNote = displayedNote;
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		tabItem.addDisposeListener(this);
		
		textViewer = new TextViewer(parent, SWT.WRAP);
		textViewer.setDocument(new Document(displayedNote.getNote().getText()));
		
		//TextPresentation tp = new TextPresentation();
		//tp.addStyleRange(new StyleRange(10, 10, new Color(parent.getDisplay(), new RGB(200,0,200)), new Color(parent.getDisplay(), new RGB(200,200,200)), SWT.BOLD));
		//textViewer.changeTextPresentation(tp, false);
		
		textViewer.getTextWidget().addListener(SWT.FocusIn, new Listener(d, MainController.TAB_SELECTED));
		textViewer.getTextWidget().addListener(SWT.FocusOut, new Listener(d, MainController.TAB_DESELECTED));
		
		tabItem.setControl(textViewer.getTextWidget());
		select();
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	
	public void setName(String name) {tabItem.setText(name);}
	
	public void saveToNote() {
		displayedNote.getNoteWithoutUpdatingFromTab().setText(textViewer.getDocument().get());
	}
	
	public boolean hasUnsavedChanges() {
		return !displayedNote.getNoteWithoutUpdatingFromTab().getText().equals(textViewer.getDocument().get());
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
		textViewer.getTextWidget().dispose();
		displayedNote.tabDisposed();
	}
	
	public void addNoteModifiedAction(MainController.NoteModifiedAction action) {
		textListener = new TextListener(action);
		textViewer.addTextListener(textListener);
	}
	public void removeNoteModifiedAction() {
		if (textListener != null) {
			textViewer.removeTextListener(textListener);
		}
	}
	
	private class TextListener implements ITextListener {
		private MainController.NoteModifiedAction action;
		private TextListener(MainController.NoteModifiedAction action) {
			this.action = action;
		}
		public void textChanged(TextEvent event) {
			action.invoke();
		}
	}
}
