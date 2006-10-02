package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;

public class DisplayedNote implements DisplayedNoteHolder {
	private NoteTab tab;
	private NoteTreeNode treeNode;
	private Note note;
	
	private DisplayedNoteHolder holder;
	private List<DisplayedNote> displayedNotes;
	
	public DisplayedNote(DisplayedNoteHolder holder, NoteTree tree, Note note) {
		this.note = note;
		this.displayedNotes = new LinkedList<DisplayedNote>();
		this.holder = holder;
		holder.addDisplayedNote(this, note.getIndex());
		if (holder instanceof DisplayedNote) {
			this.treeNode = tree.addTreeNode(((DisplayedNote) holder).treeNode, this);
		} else {
			this.treeNode = tree.addRootNode(this);
		}
		for (Note n : note.getNotes()) {
			new DisplayedNote(this, tree, n);
		}
	}
	
	public DisplayedNoteHolder getHolder() {return holder;}
	
	public Note getNote() {
		if (tab != null) note.setText(tab.getText());
		return note;
	}
	
	// Implement DisplayedNoteHolder
	public List<DisplayedNote> getDisplayedNotes() {return displayedNotes;}
	public void addDisplayedNote(DisplayedNote dn) {displayedNotes.add(dn);}
	public void addDisplayedNote(DisplayedNote dn, int index) {displayedNotes.add(index, dn);}
	public void removeDisplayedNote(DisplayedNote dn) {displayedNotes.remove(dn);}
	public NoteHolder getNoteHolder() {return note;}
	
	public String getName() {return note.getName();}
	public void setName(String name) {
		note.setName(name);
		treeNode.setName(name);
		if (tab != null) tab.setName(name);
	}
	
	public void setSelected(boolean selected) {
		treeNode.setSelected(selected);
		if (selected && (tab != null)) tab.select();
	}
	
	public void displayTab(NoteTabFolder tabFolder, Dispatcher d) {
		if (tab == null) {
			this.tab = tabFolder.addNoteTab(this, d);
		} else {
			tab.select();
		}
	}
	public void tabDisposed() {
		tab = null;
	}
	
	public void deleteSelfAndChildren() {
		while (displayedNotes.size() > 0) {
			DisplayedNote child = displayedNotes.get(0);
			child.deleteSelfAndChildren();
		}
		treeNode.dispose();
		if (tab != null) tab.dispose();
		holder.removeDisplayedNote(this);
		note.getHolder().removeNote(note);
	}
	
	/**
	 * Move this DisplayedNote to a new DisplayedNoteHolder, moves the equivalent Note to the new
	 * NoteHolder, and the NoteTreeNode to its new place in the tree.
	 */
	public void move(DisplayedNoteHolder newHolder, NoteTree tree, int index) {
		
		// Move DisplayedNote
		holder.removeDisplayedNote(this);
		holder = newHolder;
		holder.addDisplayedNote(this, index);
		
		// Move Note
		note.move(newHolder.getNoteHolder(), index);
		
		// Move Tree Node
		moveTreeNode(newHolder, tree);
		treeNode.setSelected(true);
	}
	
	/**
	 * A helper for move(DisplayedNoteHolder, NoteTree, int) to move the NoteTreeNode to its new
	 * parent node; the node actually needs to be destroyed and recreated for this and therefore
	 * so do all its child nodes.
	 */
	private void moveTreeNode(DisplayedNoteHolder newHolder, NoteTree tree) {
		treeNode.dispose();
		if (holder instanceof DisplayedNote) {
			treeNode = tree.addTreeNode(((DisplayedNote) newHolder).treeNode, this);
		} else {
			treeNode = tree.addRootNode(this);
		}
		for (DisplayedNote dn : displayedNotes) {
			dn.moveTreeNode(this, tree);
		}
	}
}
