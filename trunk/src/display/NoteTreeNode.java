package display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import data.Note;

public class NoteTreeNode {
	private DisplayedNote displayedNote;
	private TreeItem treeItem;
	
	public NoteTreeNode(Tree parent, Note data) {
		treeItem = new TreeItem(parent, SWT.NONE);
		init(data);
	}
	
	public NoteTreeNode(TreeItem parent, Note data) {
		treeItem = new TreeItem(parent, SWT.NONE);
		init(data);
	}
	
	private void init(Note data) {
		this.displayedNote = new DisplayedNote(this, data);
		treeItem.setText(data.getName());
		treeItem.setData(this);
		for (Note n : data.getChildren()) {
			new NoteTreeNode(treeItem, n);
		}
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	
	public void addChild(Note childData) {
		new NoteTreeNode(treeItem, childData);
	}
	
	public void dispose() {
		if (displayedNote.getTab() != null) displayedNote.getTab().dispose();
		treeItem.dispose();
	}
	
	public void setName(String name) {
		treeItem.setText(name);
	}
}
