package de.berlios.koalanotes.controllers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTree;

public class TreeController extends Controller {
	
	private NoteTree tree;
	private Dispatcher dispatcher;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(TreeController.class, methodName);
	}
	
	public TreeController(NoteTree tree, Dispatcher dispatcher) {
		this.tree = tree;
		this.dispatcher = dispatcher;
	}
	
	public static final String INITIALISE_CONTEXT_MENU = getMethodDescriptor("initialiseContextMenu");
	public void initialiseContextMenu(Event e) {
		dispatcher.registerController(new TreeContextMenuController(tree));
		tree.initialiseContextMenu();
	}
	
	public static final String DISPOSE_CONTEXT_MENU = getMethodDescriptor("disposeContextMenu");
	public void disposeContextMenu(Event e) {
		String controllerSig = Controller.getControllerSignature(TreeContextMenuController.ADD_NOTE);
		dispatcher.deregisterController(controllerSig);
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
