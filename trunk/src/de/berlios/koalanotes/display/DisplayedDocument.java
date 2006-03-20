package de.berlios.koalanotes.display;

import java.util.LinkedList;

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

/**
 * DisplayedDocument holds the state of the display, as well the document being displayed.
 * Specifically DisplayedDocument holds the document, shell, main menu, tree and tab folder.
 * As DisplayedNote is to Note, DisplayedDocument is to Document.
 */
public class DisplayedDocument {
	private Document document;
	private Shell shell;
	private MainMenu mainMenu;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	
	public DisplayedDocument(Shell shell, Listener listener, Dispatcher dispatcher) {
		
		// Shell
		this.shell = shell;
		shell.setText("Koala Notes");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Document
		document = new Document();
		Note root = new Note("root", document, "");
		LinkedList<Note> roots = new LinkedList<Note>();
		roots.add(root);
		
		// Menu
		mainMenu = new MainMenu(shell, listener);
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		
		// Tree
		tree = new NoteTree(sashForm, listener);
		tree.loadTree(roots);
		tree.init();
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm, listener);
		
		// Finish SashForm
		sashForm.setWeights(new int[] {20, 80});
		
		// Controllers
		dispatcher.registerController(new MainController(this));
		dispatcher.registerController(new MainMenuController(this));
		dispatcher.registerController(new TreeController(tree));
		dispatcher.registerController(new TreeContextMenuController(tree));
	}
	
	public Document getDocument() {return document;}
	public Shell getShell() {return shell;}
	public MainMenu getMainMenu() {return mainMenu;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
}
