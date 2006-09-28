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
