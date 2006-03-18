package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;

import de.berlios.koalanotes.data.Note;

public class DisplayedNote {
	private NoteTab tab;
	private NoteTreeNode treeNode;
	private Note note;
	
	private List<DisplayedNote> children;
	
	public DisplayedNote(DisplayedNote parent, Note note) {
		parent.children.add(this);
		this.note = note;
		this.treeNode = new NoteTreeNode(parent.treeNode, this);
		init();
	}
	
	public DisplayedNote(Tree tree, Note root) {
		this.note = root;
		this.treeNode = new NoteTreeNode(tree, this);
		init();
	}
	
	private void init() {
		this.children = new LinkedList<DisplayedNote>();
		for (Note n : note.getNotes()) {
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
	
	public void deleteSelfAndChildren() {
		for (DisplayedNote child : children) {
			child.deleteSelfAndChildren();
		}
		treeNode.dispose();
		if (tab != null) tab.dispose();
		note.getParent().removeNote(note);
	}
}
