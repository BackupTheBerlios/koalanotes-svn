package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

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
	private TreeContextMenu contextMenu;
	private TreeEditor treeEditor;
	private Dispatcher d;
	
	public NoteTree(Composite parent, Dispatcher d) {
		
		// Tree and context menu.
		tree = new Tree(parent, SWT.MULTI);
		contextMenu = new TreeContextMenu(tree, d);
		
		// Text editor for renaming tree nodes.
		treeEditor = new TreeEditor(tree);
		treeEditor.grabHorizontal = true;
		treeEditor.minimumWidth = 50;
		
		// Events.
		this.d = d;
		tree.addListener(SWT.MouseDoubleClick, new Listener(d, MainController.DISPLAY_TAB));
	}
	
	
	// Context Menu
	
	public void initialiseContextMenu() {
		contextMenu.initialise(tree.getSelectionCount());
	}
	
	
	// Tree Editor
	
	public void initialiseTreeEditor() {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return;
		Text treeTextEditor = new Text(tree, SWT.NONE);
		treeTextEditor.setText(ti.getText());
		treeTextEditor.selectAll();
		treeTextEditor.setFocus();
		treeEditor.setEditor(treeTextEditor, ti);
		treeTextEditor.addListener(SWT.Modify, new Listener(d, TreeController.RENAME_NOTE));
		treeTextEditor.addListener(SWT.FocusOut, new Listener(d, TreeController.FINISH_RENAME_NOTE));
		treeTextEditor.addListener(SWT.KeyDown, new Listener(d, TreeController.FINISH_RENAME_NOTE));
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
