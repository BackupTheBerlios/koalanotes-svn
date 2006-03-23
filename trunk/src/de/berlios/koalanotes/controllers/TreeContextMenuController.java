package de.berlios.koalanotes.controllers;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.notes.AddNoteController;
import de.berlios.koalanotes.display.notes.AddNoteDialog;

public class TreeContextMenuController extends Controller {
	
	private Dispatcher d;
	private DisplayedDocument dd;
	private Listener l;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeContextMenuController.class, methodName);
	}
	
	public TreeContextMenuController(Dispatcher d, DisplayedDocument dd, Listener l) {
		super(d);
		this.d = d;
		this.dd = dd;
		this.l = l;
	}
	
	public static final String ADD_NOTE_AFTER = getMethodDescriptor("addNoteAfter");
	public void addNoteAfter(Event e) {
		AddNoteDialog and = new AddNoteDialog(dd.getShell(), l);
		new AddNoteController(d, dd, true, and);
	}
	
	public static final String ADD_NOTE_UNDER = getMethodDescriptor("addNoteUnder");
	public void addNoteUnder(Event e) {
		AddNoteDialog and = new AddNoteDialog(dd.getShell(), l);
		new AddNoteController(d, dd, false, and);
	}
	
	public static final String REMOVE_NOTES = getMethodDescriptor("removeNotes");
	public void removeNotes(Event e) {
		for (DisplayedNote removeMe : dd.getTree().getSelectedNotes()) {
			removeMe.deleteSelfAndChildren();
		}
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		dd.getTree().initialiseTreeEditor();
	}
}
