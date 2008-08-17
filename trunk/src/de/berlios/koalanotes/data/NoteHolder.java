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
