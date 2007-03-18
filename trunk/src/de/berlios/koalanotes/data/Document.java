package de.berlios.koalanotes.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.berlios.koalanotes.KoalaNotes;
import de.berlios.koalanotes.exceptions.KoalaException;
import de.berlios.koalanotes.persistence.XMLFileStore;

public class Document implements NoteHolder {
	private XMLFileStore store;
	private List<Note> roots;
	
	public Document() {
		roots = new LinkedList<Note>();
	}
	
	/**
	 * Load the Notes from the given xml file and return them (keeps a reference to the file and
	 * the Notes).
	 * 
	 * @throws KoalaException If the file could not be read from or the xml contained was invalid,
	 * or the document could not be built.
	 */
	
	public Document(File file) {
		store = new XMLFileStore(file);
		roots = new LinkedList<Note>();
		store.loadNotes(this);
	}
	
	public boolean hasStore() {
		return (store != null);
	}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return roots;}
	public void addNote(Note note) {roots.add(note);}
	public void addNote(Note note, int index) {roots.add(index, note);}
	public void removeNote(Note note) {roots.remove(note);}
	
	/**
	 * Save the currently loaded Notes to the given file (the given file is remembered for future
	 * storage).
	 * 
	 * @throws KoalaException If the file could not be found or could not be written to.
	 */
	public void saveNotes(File file) {
		store = new XMLFileStore(file);
		saveNotes();
	}
	
	/**
	 * Save the currently loaded Notes to the currently used storage.
	 * 
	 * @throws KoalaException If there is no storage currently in use, or if there was a problem
	 * writing to it.
	 */
	public void saveNotes() {
		if (store == null) {
			throw new KoalaException("Koala Notes does not know where to save to.");
		}
		store.saveNotes(roots);
	}
	
	/**
	 * Save a backup of the currently loaded Notes, return a description of the backup location.
	 */
	public String saveBackup() {
		XMLFileStore backupStore;
		if (store == null) {
			File f = new File(KoalaNotes.DEFAULT_BACKUP_PATH);
			backupStore = new XMLFileStore(f);
		} else {
			File f = new File(store.getFileAbsolutePath() + ".bak");
			backupStore = new XMLFileStore(f);
		}
		backupStore.saveNotes(roots);
		return backupStore.getFileAbsolutePath();
	}
}
