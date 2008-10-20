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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;
import de.berlios.koalanotes.display.menus.NoteMenuController;

public class NoteTree {
	private Tree tree;
	private TreeEditor treeEditor;
	private KeyListener keyListener;
	
	public NoteTree(Composite parent, DisplayedDocument dd) {
		
		// Tree.
		tree = new Tree(parent, SWT.MULTI);
		
		// Text editor for renaming tree nodes.
		treeEditor = new TreeEditor(tree);
		treeEditor.grabHorizontal = true;
		treeEditor.minimumWidth = 50;
	}
	
	/**
	 * 
	 * @param contextChangedAction To notify the main controller when the context changes (eg. when
	 *                             a tree node is selected).
	 * @param duccAction           To notify the main controller when the document is updated (eg.
	 *                             when a tree node is drag-dropped).
	 * @param displayTabAction     To notify the main controller when it is time to display a tab
	 *                             (eg. when a tree node is double-clicked).
	 */
	public void hookUpActions(final MainController.ContextChangedAction contextChangedAction,
	      	                  final MainController.DocumentUpdatedAndContextChangedAction duccAction,
	    	                  final MainController.DisplayTabAction displayTabAction,
	    	                  final NoteTreeDragNDropController noteTreeDragNDropController) {
		
		// Drag-n-drop controller is its own listener, it is more closely tied to the SWT display
		// than the main controller.
		noteTreeDragNDropController.addToNoteTree(this, tree);
		
		// Events.
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent arg0) {
				displayTabAction.invoke();
			}
		});
		tree.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
				contextChangedAction.invoke();
			}
		});
		tree.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				contextChangedAction.invoke();
			}
			public void widgetSelected(SelectionEvent arg0) {
				contextChangedAction.invoke();
			}
		});
	}
	
	// Tree
	
	public boolean hasFocus() {
		return tree.isFocusControl();
	}
	
	public void addKeydownAction(final NoteMenuController.NoteCutOrCopyAttemptAction a) {
		keyListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				a.invoke(e);
			}
		};
		tree.addKeyListener(keyListener);
	}
	
	public void removeKeydownAction() {
		if (keyListener != null) {
			tree.removeKeyListener(keyListener);
		}
	}
	
	
	// Context Menu
	
	public void setContextMenu(MenuManager contextMenu) {
		tree.setMenu(contextMenu.createContextMenu(tree));
	}
	
	
	// Tree Editor
	
	public void initialiseTreeEditor(final TreeController.RenameNoteAction rna,
	                                 final TreeController.FinishRenameNoteAction frna) {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return;
		Text treeTextEditor = new Text(tree, SWT.NONE);
		treeTextEditor.setText(ti.getText());
		treeTextEditor.selectAll();
		treeTextEditor.setFocus();
		treeEditor.setEditor(treeTextEditor, ti);
		treeTextEditor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				rna.invoke();
			}
		});
		treeTextEditor.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent fe) {
				frna.invoke(fe);
			}
		});
		treeTextEditor.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				frna.invoke(ke);
			}
		});
	}
	
	public String getTreeEditorText() {
		return ((Text) treeEditor.getEditor()).getText();
	}
	
	public void disposeTreeEditor() {
		Control treeTextEditor = treeEditor.getEditor();
		if (treeTextEditor != null) {
			treeTextEditor.dispose();
		}
	}
	
	
	// Nodes
	
	public boolean isEmpty() {
		return (tree.getItemCount() == 0);
	}
	
	public int getSelectionCount() {
		return tree.getSelectionCount();
	}
	
	public DisplayedNote getSelectedNote() {
		if (tree.getSelectionCount() == 0) return null;
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return null;
		return ((NoteTreeNode) ti.getData()).getDisplayedNote();
	}
	
	public List<DisplayedNote> getSelectedNotes() {
		TreeItem[] tis = tree.getSelection();
		List<DisplayedNote> result = new LinkedList<DisplayedNote>();
		for (TreeItem ti : tis) {
			result.add(((NoteTreeNode) ti.getData()).getDisplayedNote());
		}
		return result;
	}
	
	public NoteTreeNode addRootNode(DisplayedNote displayedNote) {
		return new NoteTreeNode(tree, displayedNote);
	}
	
	public NoteTreeNode addTreeNode(NoteTreeNode parent, DisplayedNote displayedNote) {
		return new NoteTreeNode(parent, displayedNote);
	}
	
	public void removeAll() {
		tree.removeAll();
	}
	
	// Drag/Cut/Copy
	
	/**
	 * The selection is able to be moved (by drag or cut or copy) if the selected notes are all
	 * under the same immediate parent.
	 */
	public boolean isSelectionValidForMoving() {
		TreeItem[] tis = tree.getSelection();
		if (tis == null || tis.length == 0) {
			return false;
		}
		TreeItem parentItem = tis[0].getParentItem();
		for (TreeItem ti : tis) {
			if (ti.getParentItem() != parentItem) {
				return false;
			}
		}
		return true;
	}
}
