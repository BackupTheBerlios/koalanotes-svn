package de.berlios.koalanotes.display.menus;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class FileMenuController extends Controller {
	
	private DisplayedDocument dd;
	private FileMenu fileMenu;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(FileMenuController.class, methodName);
	}
	
	public FileMenuController(Dispatcher d, DisplayedDocument dd, FileMenu fileMenu) {
		super(d);
		this.dd = dd;
		this.fileMenu = fileMenu;
	}
	
	public static final String INITIALISE_MENU = getMethodDescriptor("initialiseMenu");
	public void initialiseMenu(Event e) {
		fileMenu.initialise(dd.getDocument().hasFile());
	}
	
	public static final String FILE_OPEN = getMethodDescriptor("fileOpen");
	public void fileOpen(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.OPEN);
		String filePath = fileDialog.open();
		if (filePath == null) return;
		dd.getTabFolder().closeNoteTabs();
		dd.getTree().removeAll();
		File file = new File(filePath);
		List<Note> notes = dd.getDocument().loadNotes(file);
		for (Note root : notes) {
			new DisplayedNote(dd, dd.getTree(), root);
		}
		dd.getShell().setText(file.getName() + " - Koala Notes");
	}
	
	public static final String FILE_SAVE = getMethodDescriptor("fileSave");
	public void fileSave(Event e) {
		dd.getTabFolder().saveNoteTabs();
		dd.getDocument().saveNotes();
	}
	
	public static final String FILE_SAVE_AS = getMethodDescriptor("fileSaveAs");
	public void fileSaveAs(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.SAVE);
		String filePath = fileDialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			dd.getTabFolder().saveNoteTabs();
			dd.getDocument().saveNotes(file);
			dd.getShell().setText(file.getName() + " - Koala Notes");
		}
	}
}
