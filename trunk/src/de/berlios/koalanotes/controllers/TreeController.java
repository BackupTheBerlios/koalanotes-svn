package de.berlios.koalanotes.controllers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTree;

public class TreeController extends Controller {
	
	private NoteTree tree;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeController.class, methodName);
	}
	
	public TreeController(Dispatcher d, NoteTree tree) {
		super(d);
		this.tree = tree;
	}
	
	public static final String RENAME_NOTE = getMethodDescriptor("renameNote");
	public void renameNote(Event e) {
		String newName = tree.getTreeEditorText();
		DisplayedNote dn = tree.getSelectedNote();
		dn.setName(newName);
	}
	
	public static final String FINISH_RENAME_NOTE = getMethodDescriptor("finishRenameNote");
	public void finishRenameNote(Event e) {
		if ((e.type == SWT.KeyDown && e.keyCode == SWT.CR)
				|| e.type == SWT.FocusOut) {
			tree.disposeTreeEditor();
		}
	}
}
