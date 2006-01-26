package de.berlios.koalanotes.controllers;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.NoteTab;

public class MainMenuController extends Controller {
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainMenuController.class, methodName);
	}
	
	public MainMenuController(DisplayedDocument displayedDocument) {
		super(displayedDocument);
	}
	
	public static final String INITIALISE_MENU = getMethodDescriptor("initialiseMenu");
	public void initialiseMenu(Event e) {
		dd.getMainMenu().initialise(dd.getDocument().hasFile());
	}
	
	public static final String FILE_OPEN = getMethodDescriptor("fileOpen");
	public void fileOpen(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.OPEN);
		String filePath = fileDialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			dd.getTree().loadTree(dd.getDocument().loadNotes(file));
			dd.getShell().setText(file.getName() + " - Koala Notes");
		}
		
	}
	
	public static final String FILE_SAVE = getMethodDescriptor("fileSave");
	public void fileSave(Event e) {
		updateNotesFromOpenNoteTabs();
		dd.getDocument().saveNotes();
	}
	
	public static final String FILE_SAVE_AS = getMethodDescriptor("fileSaveAs");
	public void fileSaveAs(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.SAVE);
		String filePath = fileDialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			updateNotesFromOpenNoteTabs();
			dd.getDocument().saveNotes(file);
			dd.getShell().setText(file.getName() + " - Koala Notes");
		}
	}
	
	/** Helper for fileSave() and fileSaveAs(). */
	private void updateNotesFromOpenNoteTabs() {
		List<NoteTab> tabs = dd.getTabFolder().getOpenNoteTabs();
		for (NoteTab tab : tabs) {
			tab.getDisplayedNote().getNote();
		}
	}
}
