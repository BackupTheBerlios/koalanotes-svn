package de.berlios.koalanotes.display;

import java.util.List;

import de.berlios.koalanotes.data.NoteHolder;

/**
 * A DisplayedNoteHolder holds a collection of DisplayedNotes, and has a corresponding NoteHolder.
 * Known implementations:
 *   1) DisplayedNote holds child DisplayedNotes and has a corresponding Note;
 *   2) DisplayedDocument holds root DisplayedNotes and has a corresponding Document.
 * 
 * @author alison
 */
public interface DisplayedNoteHolder {
	public List<DisplayedNote> getDisplayedNotes();
	public int getDisplayedNoteCount();
	public void addDisplayedNote(DisplayedNote dn);
	public void addDisplayedNote(DisplayedNote dn, int index);
	public void removeDisplayedNote(DisplayedNote dn);
	public NoteHolder getNoteHolder();
}
