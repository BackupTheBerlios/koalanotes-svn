package de.berlios.koalanotes.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.berlios.koalanotes.persistence.XMLFileStore;

public class Document implements NoteHolder {
	private XMLFileStore store;
	private List<Note> roots;
	
	public Document() {
		roots = new LinkedList<Note>();
	}
	
	public boolean hasStore() {
		return (store != null);
	}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return roots;}
	public void addNote(Note note) {roots.add(note);}
	public void addNote(Note note, int index) {roots.add(index, note);}
	public void removeNote(Note note) {roots.remove(note);}
	
	public List<Note> loadNotes(File file) {
		store = new XMLFileStore(file);
		roots = new LinkedList<Note>();
		store.loadNotes(this);
		return roots;
	}
	
	public void saveNotes() {
		store.saveNotes(roots);
	}
	
	public void saveNotes(File file) {
		store = new XMLFileStore(file);
		saveNotes();
	}
}
