package de.berlios.koalanotes.data;

import java.util.List;

/**
 * A NoteHolder holds a collection of notes.  Known implementations:
 *   1) Note holds child Notes;
 *   2) Document holds root Notes.
 * 
 * @author alison
 */
public interface NoteHolder {
	public List<Note> getNotes();
	public void addNote(Note note);
	public void addNote(Note note, int index);
	public void removeNote(Note note);
}
