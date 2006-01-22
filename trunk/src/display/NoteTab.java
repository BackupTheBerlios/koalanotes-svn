package display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Text;

import controllers.Listener;
import controllers.MainController;

public class NoteTab {
	private DisplayedNote displayedNote;
	private CTabItem tabItem;
	private Text text;
	
	public NoteTab(final CTabFolder parent, Listener l, DisplayedNote displayedNote) {
		this.displayedNote = displayedNote;
		tabItem = new CTabItem(parent, SWT.NONE);
		tabItem.setText(displayedNote.getName());
		tabItem.setData(this);
		text = new Text(parent, SWT.NONE);
		text.setText(displayedNote.getNote().getText());
		tabItem.setControl(text);
		displayedNote.setTab(this);
		select();
		l.mapEvent(tabItem, SWT.Dispose, MainController.CLOSE_TAB);
	}
	
	public DisplayedNote getDisplayedNote() {return displayedNote;}
	public String getText() {return text.getText();}
	
	public void select() {
		tabItem.getParent().setSelection(tabItem);
	}
	
	public void dispose() {
		tabItem.dispose();
	}
	
	public void disposeContent() {
		text.dispose();
	}
	
	public void setName(String name) {
		tabItem.setText(name);
	}
}
