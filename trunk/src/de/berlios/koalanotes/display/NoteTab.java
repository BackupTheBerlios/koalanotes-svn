package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Text;

import de.berlios.koalanotes.controllers.Listener;

public class NoteTab implements DisposeListener {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private Text text;
	
	public NoteTab(final CTabFolder parent, Listener l, DisplayedNote displayedNote) {
		this.displayedNote = displayedNote;
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		tabItem.addDisposeListener(this);
		text = new Text(parent, SWT.MULTI | SWT.WRAP);
		text.setText(displayedNote.getNote().getText());
		tabItem.setControl(text);
		select();
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	public String getText() {return text.getText();}
	
	public void select() {
		tabItem.getParent().setSelection(tabItem);
	}
	
	public void setName(String name) {
		tabItem.setText(name);
	}
	
	public void dispose() {
		tabItem.dispose();
	}
	
	public void widgetDisposed(DisposeEvent e) {
		text.dispose();
		displayedNote.tabDisposed();
	}
}
