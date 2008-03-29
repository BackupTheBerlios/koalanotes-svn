package de.berlios.koalanotes.display.menus;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.DocumentViewSettings;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class FileMenuController extends Controller {
	private DisplayedDocument dd;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(FileMenuController.class, methodName);
	}
	
	public FileMenuController(Dispatcher d, DisplayedDocument dd) {
		super(d);
		this.dd = dd;
	}
	
	public static final String FILE_NEW = getMethodDescriptor("fileNew");
	public void fileNew(Event e) {
		dd.getTabFolder().closeNoteTabs();
		dd.getTree().removeAll();
		dd.getDisplayedNotes().clear();
		dd.setDocument(new Document());
		Note root = new Note("root", dd.getDocument(), "");
		new DisplayedNote(dd, dd.getTree(), root);
		dd.getShell().setText("Untitled Document - Koala Notes");
		dd.setModified(false);
		contextChanged(e);
		dd.setStatusBarText("A brand new document.");
	}
	
	public static final String FILE_OPEN = getMethodDescriptor("fileOpen");
	public void fileOpen(Event e) {
		
		// Get file path from dialog.
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.OPEN);
		String filePath = fileDialog.open();
		if (filePath == null) return;
		
		// Open the file.
		dd.setStatusBarText("Opening document...");
		File file = new File(filePath);
		
		// Clear tab folder, tree and DisplayedNote list.
		dd.getTabFolder().closeNoteTabs();
		dd.getTree().removeAll();
		dd.getDisplayedNotes().clear();
		
		// Load the new document.
		dd.setDocument(new Document(file));
		
		// Load the notes.
		List<Note> notes = dd.getDocument().getNotes();
		for (Note root : notes) {
			new DisplayedNote(dd, dd.getTree(), root);
		}
		
		// Load the view settings.
		DocumentViewSettings viewSettings = dd.getDocument().getViewSettings();
		for (Note note : viewSettings.getNotesOpenInTabs()) {
			DisplayedNote dn = dd.findDisplayedNoteForNote(note);
			dn.displayTab(dd.getTabFolder(), d);
		}
		dd.getTabFolder().setSelectedNoteTab(viewSettings.getSelectedTab());
		
		// Set the window title.
		dd.getShell().setText(file.getName() + " - Koala Notes");
		dd.setModified(false);
		
		contextChanged(e);
		dd.setStatusBarText("Document opened.");
	}
	
	public static final String FILE_SAVE = getMethodDescriptor("fileSave");
	public void fileSave(Event e) {
		dd.setStatusBarText("Saving document...");
		dd.getTabFolder().saveNoteTabs();
		dd.getDocument().saveDocument();
		dd.setModified(false);
		contextChanged(e);
		dd.setStatusBarText("Document saved.");
	}
	
	public static final String FILE_SAVE_AS = getMethodDescriptor("fileSaveAs");
	public void fileSaveAs(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.SAVE);
		String filePath = fileDialog.open();
		if (filePath != null) {
			dd.setStatusBarText("Saving document...");
			File file = new File(filePath);
			dd.getTabFolder().saveNoteTabs();
			dd.getDocument().saveDocument(file);
			dd.getShell().setText(file.getName() + " - Koala Notes");
			dd.setModified(false);
			contextChanged(e);
			dd.setStatusBarText("Document saved.");
		}
	}
	
	public static final String FILE_EXIT = getMethodDescriptor("fileExit");
	public void fileExit(Event e) {
		dd.getShell().close();
	}
}
