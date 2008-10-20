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
package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.MessageBox;

import de.berlios.koalanotes.controllers.INoArgsAction;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteTransfer;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.DisplayedNoteHolder;
import de.berlios.koalanotes.display.notes.AddNoteController;

public class NoteMenuController {
	private DisplayedDocument dd;
	private MainController mc;
	
	public NoteMenuController(DisplayedDocument dd, MainController mc) {
		this.dd = dd;
		this.mc = mc;
	}
	
	public class NoteNewChildAction implements INoArgsAction {
		public void invoke() {
			new AddNoteController(dd, mc, false);
		}
	}
	
	public class NoteNewSiblingAction implements INoArgsAction {
		public void invoke() {
			new AddNoteController(dd, mc, true);
		}
	}
	
	public class NoteCutAction implements INoArgsAction {
		public void invoke() {
			cutNCopyHelper(true);
			mc.documentUpdatedAndContextChanged();
		}
	}
	
	public class NoteCopyAction implements INoArgsAction {
		public void invoke() {
			cutNCopyHelper(false);
			mc.documentUpdatedAndContextChanged();
		}
	}
	
	private void cutNCopyHelper(boolean cut) {
		if (!dd.getTree().isSelectionValidForMoving()) {
			return;
		}
		Document clipboardDocument = new Document();
		int i = 0;
		for (DisplayedNote dn : dd.getTree().getSelectedNotes()) {
			dn.getNote().copy(clipboardDocument, i);
			i++;
			if (cut) {
				dn.delete();
			}
		}
		Clipboard cb = new Clipboard(dd.getShell().getDisplay());
		try {
			cb.setContents(new Object[] {clipboardDocument},
			               new Transfer[] {NoteTransfer.getInstance()});
		} finally {
			cb.dispose();
		}
	}
	
	public class NoteCutOrCopyAttemptAction {
		public void invoke(KeyEvent e) {
			if ((e.stateMask == SWT.CONTROL)
			&& (e.keyCode == 'X' || e.keyCode == 'x' || e.keyCode == 'C' || e.keyCode == 'c')) {
				MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.ICON_WARNING);
				mb.setText("Unsupported Operation");
				mb.setMessage("Koala Notes does not allow Cut, Copy or Move for multiple items unless all "
				              + "the items selected are under the same direct parent.");
				mb.open();
			}
		}
	}
	
	public class NotePasteChildAction implements INoArgsAction {
		public void invoke() {
			pasteHelper(true);
			mc.documentUpdatedAndContextChanged();
		}
	}
	
	public class NotePasteSiblingAction implements INoArgsAction {
		public void invoke() {
			pasteHelper(false);
			mc.documentUpdatedAndContextChanged();
		}
	}
	
	private void pasteHelper(boolean child) {
		
		// Get the DisplayedNoteHolder and index that the notes should be pasted into.
		DisplayedNoteHolder holder = null;
		int index = 0;
		if (dd.getTree().getSelectionCount() != 1) {
			holder = dd;
			index = holder.getDisplayedNoteCount();
		} else if (child) {
			holder = dd.getTree().getSelectedNote();
			index = 0;
		} else {
			holder = dd.getTree().getSelectedNote().getHolder();
			index = dd.getTree().getSelectedNote().getNote().getIndex();
		}
		
		// Get the new notes from the clipboard.
		Object clipboardData = null;
		Clipboard cb = new Clipboard(dd.getShell().getDisplay());
		try {
			clipboardData = cb.getContents(NoteTransfer.getInstance());
		} finally {
			cb.dispose();
		}
		if (clipboardData == null) {
			return;
		}
		Document clipboardDocument = (Document) clipboardData;
		
		// Place the new notes.
		for (Note copyMe : clipboardDocument.getNotes()) {
			Note displayMe = copyMe.copy(holder.getNoteHolder(), index);
			new DisplayedNote(holder, dd.getTree(), displayMe);
			index++;
		}
	}
	
	public class NoteRemoveAction implements INoArgsAction {
		public void invoke() {
			int selectedNoteCount = dd.getTree().getSelectedNotes().size();
			String confirmMessage;
			if (selectedNoteCount == 0) {
				return;
			} else if (selectedNoteCount == 1) {
				confirmMessage = "Are you sure you want to delete this note?";
			} else {
				confirmMessage = "Are you sure you want to delete these notes?";
			}
			MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			mb.setText("Confirm Delete");
			mb.setMessage(confirmMessage);
			if (mb.open() == SWT.CANCEL) {
				return;
			}
			while (dd.getTree().getSelectedNotes().size() > 0) {
				DisplayedNote removeMe = dd.getTree().getSelectedNotes().get(0);
				removeMe.delete();
			}
			mc.documentUpdatedAndContextChanged();
		}
	}
	
	public class NoteMoveLeftAction implements INoArgsAction {
		public void invoke() {
			DisplayedNote moveMe = dd.getTree().getSelectedNote();
			if (!(moveMe.getHolder() instanceof DisplayedNote)) return;
			DisplayedNote holder = (DisplayedNote) moveMe.getHolder();
			DisplayedNoteHolder newHolder = holder.getHolder();
			moveMe.move(newHolder, dd.getTree(), holder.getNote().getIndex() + 1);
			mc.documentUpdated();
		}
	}
	
	public class NoteMoveRightAction implements INoArgsAction {
		public void invoke() {
			DisplayedNote moveMe = dd.getTree().getSelectedNote();
			int index = moveMe.getNote().getIndex();
			if (index == 0) return;
			DisplayedNote newHolder = moveMe.getHolder().getDisplayedNotes().get(index - 1);
			moveMe.move(newHolder, dd.getTree(), 0);
			mc.documentUpdated();
		}
	}
	
	public class NoteMoveUpAction implements INoArgsAction {
		public void invoke() {
			DisplayedNote moveMe = dd.getTree().getSelectedNote();
			int index = moveMe.getNote().getIndex() - 1;
			if (index == -1) {
				index = moveMe.getHolder().getDisplayedNoteCount() - 1;
			}
			moveMe.move(moveMe.getHolder(), dd.getTree(), index);
			mc.documentUpdated();
		}
	}
	
	public class NoteMoveDownAction implements INoArgsAction {
		public void invoke() {
			DisplayedNote moveMe = dd.getTree().getSelectedNote();
			int index = moveMe.getNote().getIndex() + 1;
			if (index == moveMe.getHolder().getDisplayedNoteCount()) {
				index = 0;
			}
			moveMe.move(moveMe.getHolder(), dd.getTree(), index);
			mc.documentUpdated();
		}
	}
	
	public class NoteRenameAction implements INoArgsAction {
		public void invoke() {
			new TreeController(dd.getTree(), mc);
		}
	}
}
