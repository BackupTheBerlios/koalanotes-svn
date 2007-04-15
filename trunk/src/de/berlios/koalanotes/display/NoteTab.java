package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Text;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;

public class NoteTab implements DisposeListener {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private Text text;
	
	public NoteTab(final CTabFolder parent, DisplayedNote displayedNote, Dispatcher d) {
		this.displayedNote = displayedNote;
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		tabItem.addDisposeListener(this);
		text = new Text(parent, SWT.MULTI | SWT.WRAP);
		text.setText(displayedNote.getNote().getText());
		text.addListener(SWT.FocusIn, new Listener(d, MainController.TAB_SELECTED));
		text.addListener(SWT.FocusOut, new Listener(d, MainController.TAB_DESELECTED));
		tabItem.setControl(text);
		select();
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	
	public void setName(String name) {tabItem.setText(name);}
	
	public void addModifyListener(Listener l) {
		text.addListener(SWT.Modify, l);
	}
	public void removeModifyListener(Listener l) {
		text.removeListener(SWT.Modify, l);
	}
	
	public void saveToNote() {
		displayedNote.getNoteWithoutUpdatingFromTab().setText(text.getText());
	}
	
	public boolean hasUnsavedChanges() {
		return !displayedNote.getNoteWithoutUpdatingFromTab().getText().equals(text.getText());
	}
	
	public void select() {
		tabItem.getParent().setSelection(tabItem);
	}
	
	public boolean isDisposed() {
		return tabItem.isDisposed();
	}
	
	public void dispose() {
		tabItem.dispose();
	}
	
	public void widgetDisposed(DisposeEvent e) {
		text.dispose();
		displayedNote.tabDisposed();
	}
}
