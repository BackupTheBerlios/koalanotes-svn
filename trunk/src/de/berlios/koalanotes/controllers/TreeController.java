package de.berlios.koalanotes.controllers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class TreeController extends Controller {
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeController.class, methodName);
	}
	
	public TreeController(DisplayedDocument displayedDocument) {
		super(displayedDocument);
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		String newName = dd.getTree().getTreeEditorText();
		DisplayedNote dn = dd.getTree().getSelectedNote();
		dn.getNote().setName(newName);
		dn.setName(newName);
	}
	
	public static final String FINISH_RENAME_NOTE = getMethodDescriptor("finishRenameNote");
	public void finishRenameNote(Event e) {
		if ((e.type == SWT.KeyDown && e.keyCode == SWT.CR)
				|| e.type == SWT.FocusOut) {
			dd.getTree().disposeTreeEditor();
		}
	}
}
