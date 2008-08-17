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
package de.berlios.koalanotes.display.notes;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class AddNoteController extends Controller {
	
	private DisplayedDocument dd;
	private boolean sibling;
	private AddNoteDialog addNoteDialog;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(AddNoteController.class, methodName);
	}
	
	/**
	 * @param after true if the new note is to be inserted _after_ the
	 * selected note, false if _under_.
	 */
	public AddNoteController(Dispatcher d, DisplayedDocument dd,
	                         boolean sibling, AddNoteDialog addNoteDialog) {
		super(d);
		this.dd = dd;
		this.sibling = sibling;
		this.addNoteDialog = addNoteDialog;
		addNoteDialog.open();
	}
	
	public static final String OK = getMethodDescriptor("ok");
	public void ok(Event e) {
		DisplayedNote newDn;
		
		// If the tree is empty or has no items selected the new note is added to the bottom.
		if (dd.getTree().isEmpty() || dd.getTree().getSelectionCount() == 0) {
			Note newNote = new Note(addNoteDialog.getName(), dd.getDocument(), "");
			newDn = new DisplayedNote(dd, dd.getTree(), newNote);
			
		// If the tree has items then the new note is added near the selected note.
		} else {
			DisplayedNote dn = dd.getTree().getSelectedNote();
			Note note = dn.getNote();
			if (sibling) { // add sibling
				Note newNote = new Note(addNoteDialog.getName(), note.getHolder(),
				                        note.getIndex(), "");
				newDn = new DisplayedNote(dn.getHolder(), dd.getTree(), newNote);
			} else { // add child
				Note newNote = new Note(addNoteDialog.getName(), note, 0, "");
				newDn = new DisplayedNote(dn, dd.getTree(), newNote);
			}
			dn.setSelected(false);
		}
		
		// The new note is selected and its contents diplayed in a tab.
		newDn.setSelected(true);
		newDn.displayTab(dd.getTabFolder(), d);
		
		// The Add Note Dialog is disposed and the Add Note Controller is deregistered.
		addNoteDialog.dispose();
		deregister();
		
		documentUpdated(dd, e);
	}
	
	public static final String CANCEL = getMethodDescriptor("cancel");
	public void cancel(Event e) {
		addNoteDialog.dispose();
		deregister();
	}
}
