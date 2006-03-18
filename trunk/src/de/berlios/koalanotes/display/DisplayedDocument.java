package de.berlios.koalanotes.display;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;

public class DisplayedDocument {
	private Document document;
	private Listener listener;
	private Shell shell;
	private MainMenu mainMenu;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	
	public DisplayedDocument(Shell shell) {
		
		// Shell
		this.shell = shell;
		shell.setText("Koala Notes");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Document
		document = new Document();
		Note root = new Note("root", document, "");
		LinkedList<Note> roots = new LinkedList<Note>();
		roots.add(root);
		
		// Listener, Dispatcher, Controllers
		Dispatcher dispatcher = new Dispatcher(this);
		listener = new Listener(dispatcher);
		
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
		
		sashForm.setWeights(new int[] {20, 80});
	}
	
	public Document getDocument() {return document;}
	public Listener getListener() {return listener;}
	public Shell getShell() {return shell;}
	public MainMenu getMainMenu() {return mainMenu;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
}
