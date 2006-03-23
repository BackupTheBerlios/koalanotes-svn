package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;

import de.berlios.koalanotes.data.Note;

public class DisplayedNote {
	private NoteTab tab;
	private NoteTreeNode treeNode;
	private Note note;
	
	private DisplayedNote parent;
	private List<DisplayedNote> children;
	
	public DisplayedNote(DisplayedNote parent, Note note) {
		this.note = note;
		this.children = new LinkedList<DisplayedNote>();
		this.parent = parent;
		parent.children.add(note.getIndex(), this);
		this.treeNode = new NoteTreeNode(parent.treeNode, this);
		init();
	}
	
	public DisplayedNote(Tree tree, Note root) {
		this.note = root;
		this.children = new LinkedList<DisplayedNote>();
		this.treeNode = new NoteTreeNode(tree, this);
		init();
	}
	
	private void init() {
		for (Note n : note.getNotes()) {
			new DisplayedNote(this, n);
		}
	}
	
	public DisplayedNote getParent() {return parent;}
	
	public Note getNote() {
		if (tab != null) note.setText(tab.getText());
		return note;
	}
	
	public String getName() {return note.getName();}
	public void setName(String name) {
		note.setName(name);
		treeNode.setName(name);
		if (tab != null) tab.setName(name);
	}
	
	public void select() {
		treeNode.select();
		if (tab != null) tab.select();
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
		note.getHolder().removeNote(note);
	}
}
