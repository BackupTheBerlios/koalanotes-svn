package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NoteTreeNode {
	private DisplayedNote displayedNote;
	private TreeItem treeItem;
	
	public NoteTreeNode(NoteTreeNode parent, DisplayedNote data) {
		treeItem = new TreeItem(parent.treeItem, SWT.NONE);
		init(data);
	}
	
	public NoteTreeNode(Tree tree, DisplayedNote root) {
		treeItem = new TreeItem(tree, SWT.NONE);
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
}
