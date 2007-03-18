package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.display.menus.FileActionGroup;
import de.berlios.koalanotes.display.menus.FileMenuController;
import de.berlios.koalanotes.display.menus.NoteActionGroup;
import de.berlios.koalanotes.display.menus.NoteMenuController;

/**
 * DisplayedDocument holds the state of the display, as well the document being displayed.
 * Specifically DisplayedDocument holds the document, shell, main menu, tree and tab folder.
 * As DisplayedNote is to Note, DisplayedDocument is to Document.
 */
public class DisplayedDocument implements DisplayedNoteHolder {
	private Document document;
	private Shell shell;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	private List<DisplayedNote> displayedNotes; // root notes
	private MenuManager menuBar;
	private MenuManager treeContextMenu;
	private List<ActionGroup> actionGroups; // groups of menu/toolbar items
	
	public DisplayedDocument(Shell shell, Dispatcher dispatcher) {
		
		// Shell
		this.shell = shell;
		shell.setText("Untitled Document - Koala Notes");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		// Document
		document = new Document();
		Note root = new Note("root", document, "");
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		
		// Tree and DisplayedNotes
		this.displayedNotes = new LinkedList<DisplayedNote>();
		tree = new NoteTree(sashForm, dispatcher, this);
		new DisplayedNote(this, tree, root);
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm);
		
		// Finish SashForm
		sashForm.setWeights(new int[] {20, 80});
		
		// Menu Bar and Tree Context Menu
		menuBar = new MenuManager();
		treeContextMenu = new MenuManager();
		tree.setContextMenu(treeContextMenu);
		shell.setMenuBar(menuBar.createMenuBar((Decorations) shell));
		
		// Action Groups
		actionGroups = new LinkedList<ActionGroup>();
		actionGroups.add(new FileActionGroup(dispatcher, this));
		actionGroups.add(new NoteActionGroup(dispatcher, tree));
		for (ActionGroup ag : actionGroups) {
			ag.populateMenuBar(menuBar);
			ag.populateTreeContextMenu(treeContextMenu);
		}
		
		// Controllers
		new FileMenuController(dispatcher, this);
		new NoteMenuController(dispatcher, this);
		new MainController(dispatcher, this);
		new TreeController(dispatcher, tree);
	}
	
	public Document getDocument() {return document;}
	public void setDocument(Document d) {document = d;}
	public Shell getShell() {return shell;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
	public MenuManager getMenuBar() {return menuBar;}
	public MenuManager getTreeContextMenu() {return treeContextMenu;}
	public List<ActionGroup> getActionGroups() {return actionGroups;}
	
	// Implement DisplayedNoteHolder
	public List<DisplayedNote> getDisplayedNotes() {return displayedNotes;}
	public int getDisplayedNoteCount() {return displayedNotes.size();}
	public void addDisplayedNote(DisplayedNote dn) {displayedNotes.add(dn);}
	public void addDisplayedNote(DisplayedNote dn, int index) {displayedNotes.add(index, dn);}
	public void removeDisplayedNote(DisplayedNote dn) {displayedNotes.remove(dn);}
	public NoteHolder getNoteHolder() {return document;}
}
