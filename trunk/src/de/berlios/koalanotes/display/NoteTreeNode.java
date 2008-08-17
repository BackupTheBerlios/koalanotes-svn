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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NoteTreeNode {
	private DisplayedNote displayedNote;
	private TreeItem treeItem;
	
	/** A NoteTreeNode should only be created by the NoteTree. */
	protected NoteTreeNode(NoteTreeNode parent, DisplayedNote data) {
		treeItem = new TreeItem(parent.treeItem, SWT.NONE, data.getNote().getIndex());
		init(data);
	}
	
	/** A NoteTreeNode should only be created by the NoteTree. */
	protected NoteTreeNode(Tree tree, DisplayedNote root) {
		treeItem = new TreeItem(tree, SWT.NONE, root.getNote().getIndex());
		init(root);
	}
	
	private void init(DisplayedNote data) {
		displayedNote = data;
		treeItem.setText(data.getName());
		treeItem.setData(this);
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	
	public void setName(String name) {
		treeItem.setText(name);
	}
	
	public boolean isSelected() {
		TreeItem[] selection = treeItem.getParent().getSelection();
		for (TreeItem selected : selection) {
			if (treeItem.equals(selected)) return true;
		}
		return false;
	}
	
	public void setSelected(boolean selected) {
		if (isSelected() == selected) {
			return; // this is important as the selection is stored as an array rather than a set,
			        // and we don't want the tree item to be in there multiple times
		}
		List<TreeItem> selectionTemp = Arrays.asList(treeItem.getParent().getSelection());
		List<TreeItem> selection = new LinkedList<TreeItem>(selectionTemp);
		if (selected) {
			selection.add(treeItem);
		} else {
			selection.remove(treeItem);
		}
		TreeItem[] newSelection = selection.toArray(new TreeItem[selection.size()]);
		treeItem.getParent().setSelection(newSelection);
	}
	
	public void dispose() {
		treeItem.dispose();
	}
}
