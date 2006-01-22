package display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import controllers.Controller;
import controllers.Listener;
import controllers.MainController;
import controllers.TreeContextMenuController;
import controllers.TreeController;

import data.Document;
import data.Note;

public class DisplayedDocument {
	private Document document;
	private Listener listener;
	private List<Controller> controllers;
	private Shell shell;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	
	public DisplayedDocument(Shell shell) {
		
		// Shell
		this.shell = shell;
		shell.setText("Koala Notes");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Document
		Note root = new Note("root", null, "");
		document = new Document(root);
		
		// Controllers
		controllers = new LinkedList<Controller>();
		controllers.add(new MainController(this));
		controllers.add(new TreeContextMenuController(this));
		controllers.add(new TreeController(this));
		
		// Listener
		listener = new Listener(controllers.get(0));
		
		// Menu
		new MainMenu(shell, listener);
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		
		// Tree
		tree = new NoteTree(shell, sashForm, listener);
		tree.loadTree(root);
		tree.init();
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm, listener);
		
		sashForm.setWeights(new int[] {20, 80});
	}
	
	public Document getDocument() {return document;}
	public Listener getListener() {return listener;}
	public List<Controller> getControllers() {return controllers;}
	public Shell getShell() {return shell;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
}
