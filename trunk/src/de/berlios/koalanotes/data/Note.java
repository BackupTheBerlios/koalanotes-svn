package de.berlios.koalanotes.data;

import java.util.LinkedList;
import java.util.List;

public class Note implements NoteHolder {
	private String name;
	private NoteHolder holder;
	private List<Note> notes;
	private String text;
	
	public Note(String name, NoteHolder holder, String text) {
		this.name = name;
		this.holder = holder;
		this.notes = new LinkedList<Note>();
		this.text = text;
		holder.addNote(this);
	}
	
	public Note(String name, NoteHolder holder, int index, String text) {
		this.name = name;
		this.holder = holder;
		this.notes = new LinkedList<Note>();
		this.text = text;
		holder.addNote(this, index);
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public NoteHolder getHolder() {return holder;}
	public int getIndex() {return holder.getNotes().indexOf(this);}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return notes;}
	public void addNote(Note note) {notes.add(note);}
	public void addNote(Note note, int index) {notes.add(index, note);}
	public void removeNote(Note note) {notes.remove(note);}
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
}
