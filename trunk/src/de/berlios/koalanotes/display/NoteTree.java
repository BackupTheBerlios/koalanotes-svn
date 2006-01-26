package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.TreeController;

import de.berlios.koalanotes.data.Note;

public class NoteTree {
	private Tree tree;
	private TreeContextMenu contextMenu;
	private TreeEditor treeEditor;
	private Listener l;
	
	public NoteTree(Shell shell, Composite parent, Listener l) {
		
		// Tree and context menu.
		tree = new Tree(parent, SWT.MULTI);
		contextMenu = new TreeContextMenu(shell, tree, l);
		
		// Text editor for renaming tree nodes.
		treeEditor = new TreeEditor(tree);
		treeEditor.grabHorizontal = true;
		treeEditor.minimumWidth = 50;
		
		// Events.
		this.l = l;
		l.mapEvent(tree, SWT.MouseDoubleClick, MainController.DISPLAY_TAB);
	}
	
	public void init() {
		tree.getItem(0).setExpanded(true);
	}
	
	
	// Context Menu
	
	public void initialiseContextMenu() {
		contextMenu.initialise(tree.getSelectionCount());
	}
	
	
	// Tree Editor
	
	public void initialiseTreeEditor() {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return;
		
		// HERE: might have to use a Rectangle for this border.
		// Also should make it so as soon as the text box loses
		// focus the editor goes away.
		
		Text treeTextEditor = new Text(tree, SWT.NONE);
		treeTextEditor.setText(ti.getText());
		treeTextEditor.selectAll();
		treeTextEditor.setFocus();
		treeEditor.setEditor(treeTextEditor, ti);
		l.mapEvent(treeTextEditor, SWT.Modify, TreeController.RENAME_NOTE);
		l.mapEvent(treeTextEditor, SWT.FocusOut, TreeController.FINISH_RENAME_NOTE);
		l.mapEvent(treeTextEditor, SWT.KeyDown, TreeController.FINISH_RENAME_NOTE);
	}
	
	public String getTreeEditorText() {
		return ((Text) treeEditor.getEditor()).getText();
	}
	
	public void disposeTreeEditor() {
		Control treeTextEditor = treeEditor.getEditor();
		if (treeTextEditor != null) {
			l.removeMappingFor(treeTextEditor);
			treeTextEditor.dispose();
		}
	}
	
	
	// Nodes
	
	public void loadTree(Note root) {
		tree.removeAll();
		new NoteTreeNode(tree, root);
		init();
	}
	
	public NoteTreeNode getRoot() {
		return (NoteTreeNode) tree.getTopItem().getData();
	}
	
	public int getSelectionCount() {
		return tree.getSelectionCount();
	}
	
	public NoteTreeNode getSelectedNode() {
		TreeItem ti = tree.getSelection()[0];
		if (ti == null) return null;
		return (NoteTreeNode) ti.getData();
	}
	
	public List<NoteTreeNode> getSelectedNodes() {
		TreeItem[] tis = tree.getSelection();
		List<NoteTreeNode> result = new LinkedList<NoteTreeNode>();
		for (TreeItem ti : tis) {
			result.add((NoteTreeNode) ti.getData());
		}
		return result;
	}
}
