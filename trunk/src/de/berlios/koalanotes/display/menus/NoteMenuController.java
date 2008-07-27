package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteTransfer;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.DisplayedNoteHolder;
import de.berlios.koalanotes.display.notes.AddNoteController;
import de.berlios.koalanotes.display.notes.AddNoteDialog;

public class NoteMenuController extends Controller {
	private DisplayedDocument dd;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(NoteMenuController.class, methodName);
	}
	
	public NoteMenuController(Dispatcher d, DisplayedDocument dd) {
		super(d);
		this.dd = dd;
	}
	
	public static final String NEW_CHILD_NOTE = getMethodDescriptor("newChildNote");
	public void newChildNote(Event e) {
		AddNoteDialog and = new AddNoteDialog(dd.getShell(), d, dd.getImageRegistry());
		new AddNoteController(d, dd, false, and);
	}
	
	public static final String NEW_SIBLING_NOTE = getMethodDescriptor("newSiblingNote");
	public void newSiblingNote(Event e) {
		AddNoteDialog and = new AddNoteDialog(dd.getShell(), d, dd.getImageRegistry());
		new AddNoteController(d, dd, true, and);
	}
	
	public static final String CUT_NOTE = getMethodDescriptor("cutNote");
	public void cutNote(Event e) {
		cutNCopyHelper(true);
		documentUpdatedAndContextChanged(dd, e);
	}
	
	public static final String COPY_NOTE = getMethodDescriptor("copyNote");
	public void copyNote(Event e) {
		cutNCopyHelper(false);
		documentUpdatedAndContextChanged(dd, e);
	}
	
	private void cutNCopyHelper(boolean cut) {
		if (!dd.getTree().isSelectionValidForMoving()) {
			return;
		}
		Document clipboardDocument = new Document();
		int i = 0;
		for (DisplayedNote dn : dd.getTree().getSelectedNotes()) {
			dn.getNote().copy(clipboardDocument, i);
			i++;
			if (cut) {
				dn.delete();
			}
		}
		Clipboard cb = new Clipboard(dd.getShell().getDisplay());
		try {
			cb.setContents(new Object[] {clipboardDocument},
			               new Transfer[] {NoteTransfer.getInstance()});
		} finally {
			cb.dispose();
		}
	}
	
	public static final String CUT_OR_COPY_ATTEMPT = getMethodDescriptor("cutOrCopyAttempt");
	public void cutOrCopyAttempt(Event e) {
		if ((e.stateMask == SWT.CONTROL)
			&& (e.keyCode == 'X' || e.keyCode == 'x' || e.keyCode == 'C' || e.keyCode == 'c')) {
			MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.ICON_WARNING);
			mb.setText("Unsupported Operation");
			mb.setMessage("Koala Notes does not allow Cut, Copy or Move for multiple items unless all "
			              + "the items selected are under the same direct parent.");
			mb.open();
		}
	}
	
	public static final String PASTE_CHILD_NOTE = getMethodDescriptor("pasteChildNote");
	public void pasteChildNote(Event e) {
		pasteHelper(true);
		documentUpdatedAndContextChanged(dd, e);
	}
	
	public static final String PASTE_SIBLING_NOTE = getMethodDescriptor("pasteSiblingNote");
	public void pasteSiblingNote(Event e) {
		pasteHelper(false);
		documentUpdatedAndContextChanged(dd, e);
	}
	
	private void pasteHelper(boolean child) {
		
		// Get the DisplayedNoteHolder and index that the notes should be pasted into.
		DisplayedNoteHolder holder = null;
		int index = 0;
		if (dd.getTree().getSelectionCount() != 1) {
			holder = dd;
			index = holder.getDisplayedNoteCount();
		} else if (child) {
			holder = dd.getTree().getSelectedNote();
			index = 0;
		} else {
			holder = dd.getTree().getSelectedNote().getHolder();
			index = dd.getTree().getSelectedNote().getNote().getIndex();
		}
		
		// Get the new notes from the clipboard.
		Object clipboardData = null;
		Clipboard cb = new Clipboard(dd.getShell().getDisplay());
		try {
			clipboardData = cb.getContents(NoteTransfer.getInstance());
		} finally {
			cb.dispose();
		}
		if (clipboardData == null) {
			return;
		}
		Document clipboardDocument = (Document) clipboardData;
		
		// Place the new notes.
		for (Note copyMe : clipboardDocument.getNotes()) {
			Note displayMe = copyMe.copy(holder.getNoteHolder(), index);
			new DisplayedNote(holder, dd.getTree(), displayMe);
			index++;
		}
	}
	
	public static final String DELETE_NOTES = getMethodDescriptor("removeNotes");
	public void removeNotes(Event e) {
		int selectedNoteCount = dd.getTree().getSelectedNotes().size();
		String confirmMessage;
		if (selectedNoteCount == 0) {
			return;
		} else if (selectedNoteCount == 1) {
			confirmMessage = "Are you sure you want to delete this note?";
		} else {
			confirmMessage = "Are you sure you want to delete these notes?";
		}
		MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
		mb.setText("Confirm Delete");
		mb.setMessage(confirmMessage);
		if (mb.open() == SWT.CANCEL) {
			return;
		}
		while (dd.getTree().getSelectedNotes().size() > 0) {
			DisplayedNote removeMe = dd.getTree().getSelectedNotes().get(0);
			removeMe.delete();
		}
		documentUpdatedAndContextChanged(dd, e);
	}
	
	public static final String MOVE_NOTE_LEFT = getMethodDescriptor("moveNoteLeft");
	public void moveNoteLeft(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		if (!(moveMe.getHolder() instanceof DisplayedNote)) return;
		DisplayedNote holder = (DisplayedNote) moveMe.getHolder();
		DisplayedNoteHolder newHolder = holder.getHolder();
		moveMe.move(newHolder, dd.getTree(), holder.getNote().getIndex() + 1);
		documentUpdated(dd, e);
	}
	
	public static final String MOVE_NOTE_RIGHT = getMethodDescriptor("moveNoteRight");
	public void moveNoteRight(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex();
		if (index == 0) return;
		DisplayedNote newHolder = moveMe.getHolder().getDisplayedNotes().get(index - 1);
		moveMe.move(newHolder, dd.getTree(), 0);
		documentUpdated(dd, e);
	}
	
	public static final String MOVE_NOTE_UP = getMethodDescriptor("moveNoteUp");
	public void moveNoteUp(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex() - 1;
		if (index == -1) {
			index = moveMe.getHolder().getDisplayedNoteCount() - 1;
		}
		moveMe.move(moveMe.getHolder(), dd.getTree(), index);
		documentUpdated(dd, e);
	}
	
	public static final String MOVE_NOTE_DOWN = getMethodDescriptor("moveNoteDown");
	public void moveNoteDown(Event e) {
		DisplayedNote moveMe = dd.getTree().getSelectedNote();
		int index = moveMe.getNote().getIndex() + 1;
		if (index == moveMe.getHolder().getDisplayedNoteCount()) {
			index = 0;
		}
		moveMe.move(moveMe.getHolder(), dd.getTree(), index);
		documentUpdated(dd, e);
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		dd.getTree().initialiseTreeEditor(d);
	}
}
