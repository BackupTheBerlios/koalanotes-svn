package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;

public class NoteTree {
	private Tree tree;
	private TreeEditor treeEditor;
	
	public NoteTree(Composite parent, Dispatcher d) {
		
		// Tree and context menu.
		tree = new Tree(parent, SWT.MULTI);
		
		// Text editor for renaming tree nodes.
		treeEditor = new TreeEditor(tree);
		treeEditor.grabHorizontal = true;
		treeEditor.minimumWidth = 50;
		
		// Events.
		tree.addListener(SWT.MouseDoubleClick, new Listener(d, MainController.DISPLAY_TAB));
		Listener contextChangedListener = new Listener(d, MainController.CONTEXT_CHANGED);
		tree.addListener(SWT.FocusIn, contextChangedListener);
		tree.addListener(SWT.Selection, contextChangedListener);
	}
	
	
	// Context Menu
	
	public void setContextMenu(MenuManager contextMenu) {
		tree.setMenu(contextMenu.createContextMenu(tree));
	}
	
	
	// Tree Editor
	
	public void initialiseTreeEditor(Dispatcher d) {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return;
		Text treeTextEditor = new Text(tree, SWT.NONE);
		treeTextEditor.setText(ti.getText());
		treeTextEditor.selectAll();
		treeTextEditor.setFocus();
		treeEditor.setEditor(treeTextEditor, ti);
		treeTextEditor.addListener(SWT.Modify, new Listener(d, TreeController.RENAME_NOTE));
		Listener finishRenameListener = new Listener(d, TreeController.FINISH_RENAME_NOTE);
		treeTextEditor.addListener(SWT.FocusOut, finishRenameListener);
		treeTextEditor.addListener(SWT.KeyDown, finishRenameListener);
	}
	
	public String getTreeEditorText() {
		return ((Text) treeEditor.getEditor()).getText();
	}
	
	public void disposeTreeEditor() {
		Control treeTextEditor = treeEditor.getEditor();
		if (treeTextEditor != null) {
			treeTextEditor.dispose();
		}
	}
	
	
	// Nodes
	
	public NoteTreeNode addRootNode(DisplayedNote displayedNote) {
		return new NoteTreeNode(tree, displayedNote);
	}
	
	public NoteTreeNode addTreeNode(NoteTreeNode parent, DisplayedNote displayedNote) {
		return new NoteTreeNode(parent, displayedNote);
	}
	
	public void removeAll() {
		tree.removeAll();
	}
	
	public int getSelectionCount() {
		return tree.getSelectionCount();
	}
	
	public DisplayedNote getSelectedNote() {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return null;
		return ((NoteTreeNode) ti.getData()).getDisplayedNote();
	}
	
	public List<DisplayedNote> getSelectedNotes() {
		TreeItem[] tis = tree.getSelection();
		List<DisplayedNote> result = new LinkedList<DisplayedNote>();
		for (TreeItem ti : tis) {
			result.add(((NoteTreeNode) ti.getData()).getDisplayedNote());
		}
		return result;
	}
}
