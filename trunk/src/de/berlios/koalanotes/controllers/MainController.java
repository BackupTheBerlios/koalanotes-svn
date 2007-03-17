package de.berlios.koalanotes.controllers;

import java.util.List;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class MainController extends Controller {
	
	private DisplayedDocument dd;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainController.class, methodName);
	}
	
	public MainController(Dispatcher d, DisplayedDocument displayedDocument) {
		super(d);
		this.dd = displayedDocument;
	}
	
	/**
	 * Called when the context of the application has changed, for example a change in the selection
	 * in the tree, the selected tab, or the contents of the clipboard.
	 */
	public static final String CONTEXT_CHANGED = getMethodDescriptor("contextChanged");
	public void contextChanged(Event e) {
		List<ActionGroup> actionGroups = dd.getActionGroups();
		for (ActionGroup ag : actionGroups) {
			ag.update();
		}
		dd.getMenuBar().updateAll(true);
		dd.getTreeContextMenu().updateAll(true);
	}
	
	public static final String DISPLAY_TAB = getMethodDescriptor("displayTab");
	public void displayTab(Event e) {
		List<DisplayedNote> selectedNotes = dd.getTree().getSelectedNotes();
		for (DisplayedNote selectedNote : selectedNotes) {
			selectedNote.displayTab(dd.getTabFolder(), d);
		}
	}
}
