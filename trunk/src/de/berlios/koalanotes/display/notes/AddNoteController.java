package de.berlios.koalanotes.display.notes;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class AddNoteController extends Controller {
	
	private DisplayedDocument dd;
	private boolean after;
	private AddNoteDialog addNoteDialog;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(AddNoteController.class, methodName);
	}
	
	/**
	 * @param after true if the new note is to be inserted _after_ the
	 * selected note, false if _under_.
	 */
	public AddNoteController(Dispatcher d, DisplayedDocument dd,
	                         boolean after, AddNoteDialog addNoteDialog) {
		super(d);
		this.dd = dd;
		this.after = after;
		this.addNoteDialog = addNoteDialog;
		addNoteDialog.open();
	}
	
	public static final String OK = getMethodDescriptor("ok");
	public void ok(Event e) {
		DisplayedNote dn = dd.getTree().getSelectedNote();
		Note note = dn.getNote();
		DisplayedNote newDn;
		if (after) {
			Note newNote = new Note(addNoteDialog.getName(), note.getHolder(),
			                        note.getIndex() + 1, "");
			if (dn.getParent() == null) {
				newDn = dd.getTree().addRoot(newNote);
			} else {
				newDn = new DisplayedNote(dn.getParent(), newNote);
			}
		} else {
			Note newNote = new Note(addNoteDialog.getName(), note, 0, "");
			newDn = new DisplayedNote(dn, newNote);
		}
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
