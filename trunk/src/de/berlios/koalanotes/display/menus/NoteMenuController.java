package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.DisplayedNoteHolder;
import de.berlios.koalanotes.display.notes.AddNoteController;
import de.berlios.koalanotes.display.notes.AddNoteDialog;

public class NoteMenuController extends Controller {
	
	private Dispatcher d;
	private DisplayedDocument dd;
	private Listener l;
	private NoteMenu noteMenu; // the instance of NoteMenu that resides on the main menu bar
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(NoteMenuController.class, methodName);
	}
	
	public NoteMenuController(Dispatcher d, DisplayedDocument dd, Listener l, NoteMenu noteMenu) {
		super(d);
		this.d = d;
		this.dd = dd;
		this.l = l;
		this.noteMenu = noteMenu;
	}
	
	public static final String INITIALISE_MENU = getMethodDescriptor("initialiseMenu");
	public void initialiseMenu(Event e) {
		noteMenu.initialise(dd.getTree().getSelectionCount());
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
	
	public static final String MOVE_NOTE_LEFT = getMethodDescriptor("moveNoteLeft");
	public void moveNoteLeft(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		if (!(moveMe.getHolder() instanceof DisplayedNote)) return;
		DisplayedNote holder = (DisplayedNote) moveMe.getHolder();
		DisplayedNoteHolder newHolder = holder.getHolder();
		moveMe.move(newHolder, dd.getTree(), holder.getNote().getIndex() + 1);
	}
	
	public static final String MOVE_NOTE_RIGHT = getMethodDescriptor("moveNoteRight");
	public void moveNoteRight(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex();
		if (index == 0) return;
		DisplayedNote newHolder = moveMe.getHolder().getDisplayedNotes().get(index - 1);
		moveMe.move(newHolder, dd.getTree(), newHolder.getDisplayedNotes().size());
	}
	
	public static final String MOVE_NOTE_UP = getMethodDescriptor("moveNoteUp");
	public void moveNoteUp(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex();
		if (index == 0) return;
		moveMe.move(moveMe.getHolder(), dd.getTree(), index - 1);
	}
	
	public static final String MOVE_NOTE_DOWN = getMethodDescriptor("moveNoteDown");
	public void moveNoteDown(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex();
		if (index == moveMe.getHolder().getDisplayedNotes().size() - 1) return;
		moveMe.move(moveMe.getHolder(), dd.getTree(), index + 1);
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
