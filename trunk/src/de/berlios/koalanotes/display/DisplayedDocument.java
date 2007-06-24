package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.display.menus.FileActionGroup;
import de.berlios.koalanotes.display.menus.FileMenuController;
import de.berlios.koalanotes.display.menus.HelpActionGroup;
import de.berlios.koalanotes.display.menus.HelpMenuController;
import de.berlios.koalanotes.display.menus.NoteActionGroup;
import de.berlios.koalanotes.display.menus.NoteMenuController;

/**
 * DisplayedDocument holds the state of the display, as well the document being displayed.
 * Specifically DisplayedDocument holds the document, shell, main menu, tree and tab folder.
 * As DisplayedNote is to Note, DisplayedDocument is to Document.
 */
public class DisplayedDocument implements DisplayedNoteHolder {
	private Document document;
	private boolean isModified; // whether there are unsaved changes to the document
	private Shell shell;
	private NoteTree tree;
	private NoteTabFolder tabFolder;
	private List<DisplayedNote> displayedNotes; // root notes
	private MenuManager menuBar;
	private CoolBarManager coolBar;
	private MenuManager treeContextMenu;
	private List<ActionGroup> actionGroups; // groups of menu/toolbar items
	private ImageRegistry imageRegistry;
	
	public DisplayedDocument(Shell shell, Dispatcher dispatcher) {
		
		// ImageRegistry
		imageRegistry = new ImageRegistry(shell.getDisplay());
		
		// Shell
		this.shell = shell;
		shell.setText("Untitled Document - Koala Notes");
		shell.setImage(imageRegistry.get(ImageRegistry.IMAGE_KOALA_SMALL));
		shell.addListener(SWT.Close, new Listener(dispatcher, MainController.EXITING_KOALA_NOTES));
		
		// Shell Layout
		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.horizontalSpacing = 0;
		shellLayout.verticalSpacing = 0;
		shell.setLayout(shellLayout);
		
		// Document
		document = new Document();
		Note root = new Note("root", document, "");
		
		// Cool Bar
		coolBar = new CoolBarManager();
		GridData coolBarLayoutData = new GridData();
		coolBarLayoutData.horizontalAlignment = SWT.FILL;
		coolBarLayoutData.grabExcessHorizontalSpace = true;
		coolBarLayoutData.verticalAlignment = SWT.TOP;
		coolBarLayoutData.grabExcessVerticalSpace = false;
		CoolBar coolBarControl = coolBar.createControl(shell);
		coolBarControl.setLayoutData(coolBarLayoutData);
		coolBarControl.addListener(SWT.MouseUp, new Listener(dispatcher, MainController.COOL_BAR_REARRANGED));
		
		// Menu Bar
		menuBar = new MenuManager();
		shell.setMenuBar(menuBar.createMenuBar((Decorations) shell));
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		GridData sashFormLayoutData = new GridData();
		sashFormLayoutData.horizontalAlignment = SWT.FILL;
		sashFormLayoutData.verticalAlignment = SWT.FILL;
		sashFormLayoutData.grabExcessVerticalSpace = true;
		sashForm.setLayoutData(sashFormLayoutData);
		
		// Tree and DisplayedNotes
		this.displayedNotes = new LinkedList<DisplayedNote>();
		tree = new NoteTree(sashForm, dispatcher, this);
		new DisplayedNote(this, tree, root);
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm, this);
		
		// Finish SashForm
		sashForm.setWeights(new int[] {20, 80});
		
		// Tree Context Menu
		treeContextMenu = new MenuManager();
		tree.setContextMenu(treeContextMenu);
		
		// Action Groups
		actionGroups = new LinkedList<ActionGroup>();
		actionGroups.add(new FileActionGroup(dispatcher, this));
		actionGroups.add(new NoteActionGroup(dispatcher, imageRegistry, tree));
		actionGroups.add(new HelpActionGroup(dispatcher, imageRegistry));
		for (ActionGroup ag : actionGroups) {
			ag.populateMenuBar(menuBar);
			ag.populateCoolBar(coolBar);
			ag.populateTreeContextMenu(treeContextMenu);
		}
		
		// Controllers
		new FileMenuController(dispatcher, this);
		new NoteMenuController(dispatcher, this);
		new HelpMenuController(dispatcher, shell, imageRegistry);
		MainController mc = new MainController(dispatcher, this);
		new TreeController(dispatcher, this);
		
		mc.contextChanged(null);
		shell.layout();
	}
	
	public Document getDocument() {return document;}
	public void setDocument(Document d) {document = d;}
	public boolean isModified() {return isModified;}
	public void setModified(boolean isModified) {
		this.isModified = isModified;
		boolean hasStar = shell.getText().endsWith("*");
		if (isModified && !hasStar) {
			shell.setText(shell.getText() + " *");
		} else if (!isModified && hasStar) {
			shell.setText(shell.getText().substring(0, shell.getText().length() - 2));
		}
	}
	public Shell getShell() {return shell;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
	public MenuManager getMenuBar() {return menuBar;}
	public CoolBarManager getCoolBar() {return coolBar;}
	public MenuManager getTreeContextMenu() {return treeContextMenu;}
	public List<ActionGroup> getActionGroups() {return actionGroups;}
	public ImageRegistry getImageRegistry() {return imageRegistry;}
	
	// Implement DisplayedNoteHolder
	public List<DisplayedNote> getDisplayedNotes() {return displayedNotes;}
	public int getDisplayedNoteCount() {return displayedNotes.size();}
	public void addDisplayedNote(DisplayedNote dn) {displayedNotes.add(dn);}
	public void addDisplayedNote(DisplayedNote dn, int index) {displayedNotes.add(index, dn);}
	public void removeDisplayedNote(DisplayedNote dn) {displayedNotes.remove(dn);}
	public NoteHolder getNoteHolder() {return document;}
	
	/**
	 * Find the displayed note for the given note, or null if not found.
	 */
	public DisplayedNote findDisplayedNoteForNote(Note note) {
		List<Note> ancestorsTopFirst = new LinkedList<Note>();
		while (note != null) {
			ancestorsTopFirst.add(0, note);
			if (note.getHolder() instanceof Note) {
				note = (Note) note.getHolder();
			} else {
				note = null;
			}
		}
		List<DisplayedNote> dns = displayedNotes;
		DisplayedNote displayedNote = null;
		for (Note n : ancestorsTopFirst) {
			displayedNote = null;
			for (DisplayedNote dn : dns) {
				if (dn.getNote() == n) {
					displayedNote = dn;
					dns = displayedNote.getDisplayedNotes();
					break;
				}
			}
			if (displayedNote == null) return null;
		}
		return displayedNote;
	}
}
