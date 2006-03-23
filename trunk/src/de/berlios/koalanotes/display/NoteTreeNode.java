package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NoteTreeNode {
	private DisplayedNote displayedNote;
	private TreeItem treeItem;
	
	public NoteTreeNode(NoteTreeNode parent, DisplayedNote data) {
		treeItem = new TreeItem(parent.treeItem, SWT.NONE, data.getNote().getIndex());
		init(data);
	}
	
	public NoteTreeNode(Tree tree, DisplayedNote root) {
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
	
	public void select() {
		treeItem.getParent().setSelection(new TreeItem[] {treeItem});
	}
	
	public void dispose() {
		treeItem.dispose();
	}
	
	public void moveAfter(NoteTreeNode siblingBefore) {
		Tree tree = treeItem.getParent();
		treeItem.dispose();
		TreeItem treeItemBefore = siblingBefore.treeItem;
		TreeItem parent = treeItemBefore.getParentItem();
		if (parent != null) {
			int index = parent.indexOf(treeItemBefore);
			treeItem = new TreeItem(parent, SWT.NONE, index);
		} else {
			int index = tree.indexOf(treeItemBefore);
			treeItem = new TreeItem(tree, SWT.NONE, index);
		}
	}
	
	public void moveUnder(NoteTreeNode parent) {
		treeItem.dispose();
		treeItem = new TreeItem(parent.treeItem, SWT.NONE, 0);
	}
	
	public void moveToTopRoot() {
		Tree tree = treeItem.getParent();
		treeItem.dispose();
		treeItem = new TreeItem(tree, SWT.NONE, 0);
	}
}
