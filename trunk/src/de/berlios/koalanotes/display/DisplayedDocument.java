/**
 * Copyright (C) 2008 Alison Farlie
 * 
 * This file is part of KoalaNotes.
 * 
 * KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * KoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with KoalaNotes.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.MainController;

import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.display.text.KoalaStyleManager;

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
	private Label statusBar;
	private ImageRegistry imageRegistry;
	private KoalaStyleManager koalaStyleManager;
	
	public DisplayedDocument(Shell shell, Document document) {
		
		this.document = document;
		
		// ImageRegistry, KoalaStyleManager
		imageRegistry = new ImageRegistry(shell.getDisplay());
		koalaStyleManager = new KoalaStyleManager();
		
		// Shell
		this.shell = shell;
		shell.setText("Untitled Document - Koala Notes");
		shell.setImage(imageRegistry.get(ImageRegistry.IMAGE_KOALA_SMALL));
		
		// Shell Layout
		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.marginWidth = 0;
		shellLayout.marginHeight = 0;
		shellLayout.horizontalSpacing = 0;
		shellLayout.verticalSpacing = 0;
		shell.setLayout(shellLayout);
		
		// Menu Bar
		menuBar = new MenuManager();
		shell.setMenuBar(menuBar.createMenuBar((Decorations) shell));
		
		// Cool Bar
		coolBar = new CoolBarManager();
		GridData coolBarLayoutData = new GridData();
		coolBarLayoutData.horizontalAlignment = SWT.FILL;
		CoolBar coolBarControl = coolBar.createControl(shell);
		coolBarControl.setLayoutData(coolBarLayoutData);
		
		// SashForm
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		GridData sashFormLayoutData = new GridData();
		sashFormLayoutData.horizontalAlignment = SWT.FILL;
		sashFormLayoutData.verticalAlignment = SWT.FILL;
		sashFormLayoutData.grabExcessHorizontalSpace = true;
		sashFormLayoutData.grabExcessVerticalSpace = true;
		sashForm.setLayoutData(sashFormLayoutData);
		
		// Status Bar
		statusBar = new Label(shell, SWT.NONE);
		GridData statusBarLayoutData = new GridData();
		statusBarLayoutData.horizontalAlignment = SWT.FILL;
		statusBarLayoutData.verticalIndent = 3;
		statusBar.setLayoutData(statusBarLayoutData);
		
		// Tree and DisplayedNotes
		this.displayedNotes = new LinkedList<DisplayedNote>();
		tree = new NoteTree(sashForm, this);
		for (Note root : document.getNotes()) {
			new DisplayedNote(this, tree, root);
		}
		
		// TabFolder
		tabFolder = new NoteTabFolder(sashForm, this);
		
		// Finish SashForm
		sashForm.setWeights(new int[] {20, 80});
		
		// Tree Context Menu
		treeContextMenu = new MenuManager();
		tree.setContextMenu(treeContextMenu);
	}
	
	public void hookUpActions(final MainController.ExitingKoalaNotesAction exitingKoalaNotesAction,
	                          final MainController.CoolBarRearrangedAction coolBarRearrangedAction,
	                          final MainController.ContextChangedAction contextChangedAction,
	      	                  final MainController.DocumentUpdatedAndContextChangedAction duccAction,
	    	                  final MainController.DisplayTabAction displayTabAction,
	    	                  final NoteTreeDragNDropController noteTreeDragNDropController) {
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				exitingKoalaNotesAction.invoke(e);
			}
		});
		coolBar.getControl().addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent arg0) {
				coolBarRearrangedAction.invoke();
			}
		});
		tree.hookUpActions(contextChangedAction, duccAction, displayTabAction, noteTreeDragNDropController);
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
	public void setStatusBarText(String text) {
		statusBar.setText(text);
	}
	public Shell getShell() {return shell;}
	public NoteTree getTree() {return tree;}
	public NoteTabFolder getTabFolder() {return tabFolder;}
	public MenuManager getMenuBar() {return menuBar;}
	public CoolBarManager getCoolBar() {return coolBar;}
	public MenuManager getTreeContextMenu() {return treeContextMenu;}
	public ImageRegistry getImageRegistry() {return imageRegistry;}
	public KoalaStyleManager getKoalaStyleManager() {return koalaStyleManager;}
	
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
