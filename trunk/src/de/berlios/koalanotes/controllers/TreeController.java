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
import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class TreeController extends Controller {
	
	private DisplayedDocument dd;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeController.class, methodName);
	}
	
	public TreeController(Dispatcher d, DisplayedDocument dd) {
		super(d);
		this.dd = dd;
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		String newName = dd.getTree().getTreeEditorText();
		DisplayedNote dn = dd.getTree().getSelectedNote();
		dn.setName(newName);
		documentUpdated(dd, e);
	}
	
	public static final String FINISH_RENAME_NOTE = getMethodDescriptor("finishRenameNote");
	public void finishRenameNote(Event e) {
		if ((e.type == SWT.KeyDown && e.keyCode == SWT.CR)
				|| e.type == SWT.FocusOut) {
			dd.getTree().disposeTreeEditor();
		}
	}
}
