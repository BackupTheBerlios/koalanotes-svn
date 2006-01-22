package controllers;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;

import display.DisplayedDocument;
import display.DisplayedNote;
import display.NoteTab;
import display.NoteTreeNode;

public class MainController extends Controller {
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainController.class, methodName);
	}
	
	public MainController(DisplayedDocument displayedDocument) {
		super(displayedDocument);
	}
	
	public static final String DISPLAY_TAB = getMethodDescriptor("displayTab");
	public void displayTab(Event e) {
		List<NoteTreeNode> treeNodes = dd.getTree().getSelectedNodes();
		for (NoteTreeNode treeNode : treeNodes) {
			DisplayedNote dn = treeNode.getDisplayedNote();
			if (dn.getTab() == null) {
				dd.getTabFolder().addNoteTab(dn);
			} else {
				dn.getTab().select();
			}
		}
	}
	
	public static final String CLOSE_TAB = getMethodDescriptor("closeTab");
	public void closeTab(Event e) {
		NoteTab noteTab = (NoteTab) ((CTabItem) e.widget).getData();
		DisplayedNote dn = noteTab.getDisplayedNote();
		noteTab.disposeContent();
		dn.setTab(null);
	}
	
	public static final String FILE_OPEN = getMethodDescriptor("fileOpen");
	public void fileOpen(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.OPEN);
		String filePath = fileDialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			dd.getTree().loadTree(dd.getDocument().loadNotes(file));
			dd.getShell().setText(file.getName() + " - Koala Notes");
		}
		
	}
	
	public static final String FILE_SAVE = getMethodDescriptor("fileSave");
	public void fileSave(Event e) {
		updateNotesFromOpenNoteTabs();
		dd.getDocument().saveNotes();
	}
	
	public static final String FILE_SAVE_AS = getMethodDescriptor("fileSaveAs");
	public void fileSaveAs(Event e) {
		FileDialog fileDialog = new FileDialog(dd.getShell(), SWT.SAVE);
		String filePath = fileDialog.open();
		if (filePath != null) {
			File file = new File(filePath);
			updateNotesFromOpenNoteTabs();
			dd.getDocument().saveNotes(file);
			dd.getShell().setText(file.getName() + " - Koala Notes");
		}
	}
	
	/** Helper for fileSave() and fileSaveAs(). */
	private void updateNotesFromOpenNoteTabs() {
		List<NoteTab> tabs = dd.getTabFolder().getOpenNoteTabs();
		for (NoteTab tab : tabs) {
			tab.getDisplayedNote().getNote();
		}
	}
}
