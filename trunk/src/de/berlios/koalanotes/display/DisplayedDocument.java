package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.MainMenuController;
import de.berlios.koalanotes.controllers.TreeContextMenuController;
import de.berlios.koalanotes.controllers.TreeController;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;

/**
 * DisplayedDocument holds the state of the display, as well the document being displayed.
 * Specifically DisplayedDocument holds the document, shell, main menu, tree and tab folder.
 * As DisplayedNote is to Note, DisplayedDocument is to Document.
 */
public class DisplayedDocument implements DisplayedNoteHolder {
	private Document document;
	private Shell shell;
	private MainMenu mainMenu;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	private List<DisplayedNote> displayedNotes; // root notes
	
	public DisplayedDocument(Shell shell, Listener listener, Dispatcher dispatcher) {
		
		// Shell
		this.shell = shell;
		shell.setText("Koala Notes");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Document
		document = new Document();
		Note root = new Note("root", document, "");
		
		// Menu
		mainMenu = new MainMenu(shell, listener);
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		
		// Tree and DisplayedNotes
		this.displayedNotes = new LinkedList<DisplayedNote>();
		tree = new NoteTree(sashForm, listener);
		new DisplayedNote(this, tree, root);
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm, listener);
		
		// Finish SashForm
		sashForm.setWeights(new int[] {20, 80});
		
		// Controllers
		new MainController(dispatcher, this);
		new MainMenuController(dispatcher, this);
		new TreeController(dispatcher, tree);
		new TreeContextMenuController(dispatcher, this, listener);
	}
	
	public Document getDocument() {return document;}
	public Shell getShell() {return shell;}
	public MainMenu getMainMenu() {return mainMenu;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
	
	// Implement DisplayedNoteHolder
	public List<DisplayedNote> getDisplayedNotes() {return displayedNotes;}
	public void addDisplayedNote(DisplayedNote dn) {displayedNotes.add(dn);}
	public void addDisplayedNote(DisplayedNote dn, int index) {displayedNotes.add(index, dn);}
	public void removeDisplayedNote(DisplayedNote dn) {displayedNotes.remove(dn);}
	public NoteHolder getNoteHolder() {return document;}
}
