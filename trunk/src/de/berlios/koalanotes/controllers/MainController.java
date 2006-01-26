package de.berlios.koalanotes.controllers;

import java.util.List;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTab;
import de.berlios.koalanotes.display.NoteTreeNode;

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
}
