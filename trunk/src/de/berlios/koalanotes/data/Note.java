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
	
	public Note copy(NoteHolder newHolder, int index) {
		Note newNote = new Note(name, newHolder, index, text);
		int i = 0;
		for (Note n : notes) {
			n.copy(newNote, i);
			i++;
		}
		return newNote;
	}
	
	public void move(NoteHolder newHolder, int index) {
		holder.removeNote(this);
		holder = newHolder;
		holder.addNote(this, index);
	}
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
}
