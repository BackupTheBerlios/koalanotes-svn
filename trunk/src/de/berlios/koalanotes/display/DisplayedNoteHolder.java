/**
 * Copyright (C) 2008 Alison Farlie
 * 
 * This file is part of KoalaNotes.
 * 
 * KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * KoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with KoalaNotes.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
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
