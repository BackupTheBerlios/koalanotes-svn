package de.berlios.koalanotes.controllers;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.DisplayedNote;
import de.berlios.koalanotes.display.NoteTab;

public class MainController extends Controller {
	
	private DisplayedDocument dd;
	
	// Keep track of unsaved changes
	private Listener modifyListener;
	private NoteTab noteTabWithModifyListener;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(MainController.class, methodName);
	}
	
	public MainController(Dispatcher d, DisplayedDocument displayedDocument) {
		super(d);
		this.dd = displayedDocument;
		modifyListener = new Listener(d, MainController.NOTE_MODIFIED);
	}
	
	/**
	 * Called when the context of the application has changed, for example a change in the selection
	 * in the tree, the selected tab, or the contents of the clipboard, or whether the document has
	 * unsaved changes.
	 */
	public static final String CONTEXT_CHANGED = getMethodDescriptor("contextChanged");
	public void contextChanged(Event e) {
		List<ActionGroup> actionGroups = dd.getActionGroups();
		for (ActionGroup ag : actionGroups) {
			ag.update();
		}
		dd.getMenuBar().updateAll(true);
		dd.getTreeContextMenu().updateAll(true);
		ensureNoteTabWithModifyListenerNull();
		if (!dd.isModified()) {
			noteTabWithModifyListener = dd.getTabFolder().getSelectedNoteTab();
			if (noteTabWithModifyListener != null) {
				noteTabWithModifyListener.addModifyListener(modifyListener);
			}
		}
	}
	
	public static final String DISPLAY_TAB = getMethodDescriptor("displayTab");
	public void displayTab(Event e) {
		List<DisplayedNote> selectedNotes = dd.getTree().getSelectedNotes();
		for (DisplayedNote selectedNote : selectedNotes) {
			selectedNote.displayTab(dd.getTabFolder(), d);
		}
	}
	
	public static final String TAB_SELECTED = getMethodDescriptor("tabSelected");
	public void tabSelected(Event e) {
		contextChanged(e);
	}
	
	public static final String TAB_DESELECTED = getMethodDescriptor("tabDeselected");
	public void tabDeselected(Event e) {
		ensureNoteTabWithModifyListenerNull();
	}
	
	/** This event only gets called by the modifyListener controlled by this controller. */
	private static final String NOTE_MODIFIED = getMethodDescriptor("noteModified");
	public void noteModified(Event e) {
		if (noteTabWithModifyListener.hasUnsavedChanges()) {
			ensureNoteTabWithModifyListenerNull();
			documentUpdated(dd, e);
		}
	}
	
	private void ensureNoteTabWithModifyListenerNull() {
		if (noteTabWithModifyListener != null) {
			if (!noteTabWithModifyListener.isDisposed()) {
				noteTabWithModifyListener.removeModifyListener(modifyListener);
			}
			noteTabWithModifyListener = null;
		}
	}
	
	/**
	 * Called when the application is about to be closed (either through the close button or the
	 * Exit menu item).
	 */
	public static final String EXITING_KOALA_NOTES = getMethodDescriptor("exitingKoalaNotes");
	public void exitingKoalaNotes(Event e) {
		if (dd.isModified()) {
			MessageBox mb = new MessageBox(dd.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			mb.setText("Confirm Exit");
			mb.setMessage("Exit even though there are unsaved changes?");
			if (mb.open() == SWT.CANCEL) {
				e.doit = false;
			}
		}
	}
}
