package de.berlios.koalanotes.data;

import java.util.LinkedList;
import java.util.List;

public class Note implements NoteHolder {
	private String name;
	private Note parent;
	private List<Note> notes;
	private String text;
	
	public Note(String name, NoteHolder holder, String text) {
		this.name = name;
		if (holder instanceof Note) parent = (Note) holder;
		this.notes = new LinkedList<Note>();
		this.text = text;
		holder.addNote(this);
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public Note getParent() {return parent;}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return notes;}
	public void addNote(Note note) {notes.add(note);}
	public void removeNote(Note note) {notes.remove(note);}
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
}
