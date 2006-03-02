package de.berlios.koalanotes.display;

import org.eclipse.swt.widgets.Tree;

import de.berlios.koalanotes.data.Note;

public class DisplayedNote {
	private NoteTab tab;
	private NoteTreeNode treeNode;
	private Note note;
	
	public DisplayedNote(DisplayedNote parent, Note note) {
		this.note = note;
		this.treeNode = new NoteTreeNode(parent.treeNode, this);
		for (Note n : note.getChildren()) {
			new DisplayedNote(this, n);
		}
	}
	
	public DisplayedNote(Tree tree, Note root) {
		this.note = root;
		this.treeNode = new NoteTreeNode(tree, this);
		for (Note n : note.getChildren()) {
			new DisplayedNote(this, n);
		}
	}
	
	public Note getNote() {
		if (tab != null) note.setText(tab.getText());
		return note;
	}
	
	public String getName() {return note.getName();}
	public void setName(String name) {
		treeNode.setName(name);
		if (tab != null) tab.setName(name);
	}
	
	public void displayTab(NoteTabFolder tabFolder) {
		if (tab == null) {
			this.tab = tabFolder.addNoteTab(this);
		} else {
			tab.select();
		}
	}
	public void tabDisposed() {
		tab = null;
	}
	
	public void disposeDisplayedWidgets() {
		treeNode.dispose();
		if (tab != null) tab.dispose();
	}
}
