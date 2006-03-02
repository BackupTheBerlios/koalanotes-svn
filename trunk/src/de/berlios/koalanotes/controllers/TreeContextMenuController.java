package de.berlios.koalanotes.controllers;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class TreeContextMenuController extends Controller {
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeContextMenuController.class, methodName);
	}
	
	public TreeContextMenuController(DisplayedDocument displayedDocument) {
		super(displayedDocument);
	}
	
	public static final String INITIALISE_MENU = getMethodDescriptor("initialiseMenu");
	public void initialiseMenu(Event e) {
		dd.getTree().initialiseContextMenu();
	}
	
	public static final String ADD_NOTE = getMethodDescriptor("addNote");
	public void addNote(Event e) {
		DisplayedNote parentDisplayed = dd.getTree().getSelectedNote();
		Note parentNote = parentDisplayed.getNote();
		Note newNote = new Note("new", parentNote, "");
		new DisplayedNote(parentDisplayed, newNote);
	}
	
	public static final String REMOVE_NOTES = getMethodDescriptor("removeNotes");
	public void removeNotes(Event e) {
		for (DisplayedNote removeMe : dd.getTree().getSelectedNotes()) {
			removeMe.disposeDisplayedWidgets(); // remove visual evidence of note
			removeMe.getNote().removeFromParent(); // remove note from underlying data
		}
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		dd.getTree().initialiseTreeEditor();
	}
}
