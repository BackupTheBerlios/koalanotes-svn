package de.berlios.koalanotes.controllers;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTree;

public class TreeContextMenuController extends Controller {
	
	private NoteTree tree;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeContextMenuController.class, methodName);
	}
	
	public TreeContextMenuController(NoteTree tree) {
		this.tree = tree;
	}
	
	public static final String ADD_NOTE = getMethodDescriptor("addNote");
	public void addNote(Event e) {
		DisplayedNote parentDisplayed = tree.getSelectedNote();
		Note parentNote = parentDisplayed.getNote();
		Note newNote = new Note("new", parentNote, "");
		new DisplayedNote(parentDisplayed, newNote);
	}
	
	public static final String REMOVE_NOTES = getMethodDescriptor("removeNotes");
	public void removeNotes(Event e) {
		for (DisplayedNote removeMe : tree.getSelectedNotes()) {
			removeMe.deleteSelfAndChildren();
		}
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		tree.initialiseTreeEditor();
	}
}
