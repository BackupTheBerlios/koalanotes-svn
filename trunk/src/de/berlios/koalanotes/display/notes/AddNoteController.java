package de.berlios.koalanotes.display.notes;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class AddNoteController extends Controller {
	
	private DisplayedDocument dd;
	private AddNoteDialog addNoteDialog;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(AddNoteController.class, methodName);
	}
	
	public AddNoteController(Dispatcher d, DisplayedDocument dd, AddNoteDialog addNoteDialog) {
		super(d);
		this.dd = dd;
		this.addNoteDialog = addNoteDialog;
		addNoteDialog.open();
	}
	
	public static final String OK = getMethodDescriptor("ok");
	public void ok(Event e) {
		DisplayedNote parentDn = dd.getTree().getSelectedNote();
		Note parentNote = parentDn.getNote();
		Note newNote = new Note(addNoteDialog.getName(), parentNote, "");
		DisplayedNote newDn = new DisplayedNote(parentDn, newNote);
		newDn.select();
		newDn.displayTab(dd.getTabFolder());
		addNoteDialog.dispose();
		deregister();
	}
	
	public static final String CANCEL = getMethodDescriptor("cancel");
	public void cancel(Event e) {
		addNoteDialog.dispose();
		deregister();
	}
}
