package de.berlios.koalanotes.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.berlios.koalanotes.KoalaNotes;
import de.berlios.koalanotes.exceptions.KoalaException;
import de.berlios.koalanotes.persistence.XMLFileStore;

public class Document implements NoteHolder {
	private XMLFileStore store;
	
	private DocumentViewSettings viewSettings;
	private List<Note> roots;
	
	public Document() {
		viewSettings = new DocumentViewSettings();
		roots = new LinkedList<Note>();
	}
	
	/**
	 * Load view settings and notes from the given xml file.
	 * 
	 * @throws KoalaException If the file could not be read from or the xml contained was invalid,
	 * or the document could not be built.
	 */
	public Document(File file) {
		store = new XMLFileStore(file);
		
		viewSettings = new DocumentViewSettings();
		roots = new LinkedList<Note>();
		
		store.loadKoalaDocument(this);
	}
	
	public boolean hasStore() {
		return (store != null);
	}
	
	// View Settings.
	public DocumentViewSettings getViewSettings() {return viewSettings;}
	public void setViewSettings(DocumentViewSettings viewSettings) {this.viewSettings = viewSettings;}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return roots;}
	public void addNote(Note note) {roots.add(note);}
	public void addNote(Note note, int index) {roots.add(index, note);}
	public void removeNote(Note note) {roots.remove(note);}
	
	/**
	 * Save the currently loaded view settings and notes to the given file (the given file is
	 * remembered for future storage).
	 * 
	 * @throws KoalaException If the file could not be found or could not be written to.
	 */
	public void saveDocument(File file) {
		store = new XMLFileStore(file);
		saveDocument();
	}
	
	/**
	 * Save the currently loaded view settings and notes to the currently used storage.
	 * 
	 * @throws KoalaException If there is no storage currently in use, or if there was a problem
	 * writing to it.
	 */
	public void saveDocument() {
		if (store == null) {
			throw new KoalaException("Koala Notes does not know where to save to.");
		}
		store.saveKoalaDocument(this);
	}
	
	/**
	 * Save a backup of the currently loaded view settings and notes, return a description of the
	 * backup location.
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
		backupStore.saveKoalaDocument(this);
		return backupStore.getFileAbsolutePath();
	}
}
