package display;

import data.Note;

public class DisplayedNote {
	private NoteTab tab;
	private NoteTreeNode treeNode;
	private Note note;
	
	public DisplayedNote(NoteTreeNode treeNode, Note note) {
		this.treeNode = treeNode;
		this.note = note;
	}
	
	public String getName() {return note.getName();}
	public NoteTab getTab() {return tab;}
	public NoteTreeNode getTreeNode() {return treeNode;}
	
	public Note getNote() {
		if (tab != null) note.setText(tab.getText());
		return note;
	}
	
	public void setTab(NoteTab tab) {this.tab = tab;}
}
