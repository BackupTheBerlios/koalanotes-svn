package controllers;

import org.eclipse.swt.widgets.Event;

import data.Note;
import display.DisplayedDocument;
import display.NoteTreeNode;

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
		NoteTreeNode parentTreeNode = dd.getTree().getSelectedNode();
		Note parentNote = parentTreeNode.getDisplayedNote().getNote();
		Note newNote = new Note("new", parentNote, "");
		parentTreeNode.addChild(newNote);
	}
	
	public static final String REMOVE_NOTES = getMethodDescriptor("removeNotes");
	public void removeNotes(Event e) {
		for (NoteTreeNode node : dd.getTree().getSelectedNodes()) {
			node.dispose();
		}
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		dd.getTree().initialiseTreeEditor();
	}
}
