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
package de.berlios.koalanotes.controllers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;

import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTree;

public class TreeController {
	
	private NoteTree tree;
	private MainController mc;
	
	public TreeController(NoteTree tree, MainController mc) {
		this.tree = tree;
		this.mc = mc;
		tree.initialiseTreeEditor(new RenameNoteAction(), new FinishRenameNoteAction());
	}
	
	public class RenameNoteAction implements INoArgsAction {
		public void invoke() {
			String newName = tree.getTreeEditorText();
			DisplayedNote dn = tree.getSelectedNote();
			dn.setName(newName);
			mc.documentUpdated();
		}
	}
	
	public class FinishRenameNoteAction {
		public void invoke(KeyEvent e) {
			if (e.keyCode == SWT.CR) {
				tree.disposeTreeEditor();
			}
		}
		public void invoke(FocusEvent e) {
			tree.disposeTreeEditor();
		}
	}
}
