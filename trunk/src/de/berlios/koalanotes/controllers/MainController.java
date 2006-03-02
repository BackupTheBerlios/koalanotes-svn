package de.berlios.koalanotes.controllers;

import java.util.List;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;

public class MainController extends Controller {
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainController.class, methodName);
	}
	
	public MainController(DisplayedDocument displayedDocument) {
		super(displayedDocument);
	}
	
	public static final String DISPLAY_TAB = getMethodDescriptor("displayTab");
	public void displayTab(Event e) {
		List<DisplayedNote> selectedNotes = dd.getTree().getSelectedNotes();
		for (DisplayedNote selectedNote : selectedNotes) {
			selectedNote.displayTab(dd.getTabFolder());
		}
	}
}
