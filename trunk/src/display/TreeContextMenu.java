package display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import controllers.Listener;
import controllers.TreeContextMenuController;

public class TreeContextMenu {
	Menu menu;
	
	MenuItem addNote;
	MenuItem removeNotes;
	MenuItem renameNote;
	
	public TreeContextMenu(Shell shell, Control parent, Listener l) {
		
		// menu
		menu = new Menu(shell, SWT.POP_UP);
		l.mapEvent(menu, SWT.Show, TreeContextMenuController.INITIALISE_MENU);
		parent.setMenu(menu);
		
		// menu items
		addNote = createMenuItem(menu, "Add Note", l, TreeContextMenuController.ADD_NOTE);
		removeNotes = createMenuItem(menu, "Remove Notes", l, TreeContextMenuController.REMOVE_NOTES);
		renameNote = createMenuItem(menu, "Rename Note", l, TreeContextMenuController.RENAME_NOTE);
	}
	
	private MenuItem createMenuItem(Menu parent, String text,
	                            Listener l, String controllerMethod) {
		MenuItem menuItem = new MenuItem(parent, SWT.PUSH);
		menuItem.setText(text);
		l.mapEvent(menuItem, SWT.Selection, controllerMethod);
		return menuItem;
	}
	
	public void initialise(int selectionCount) {
		if (selectionCount == 1) {
			addNote.setEnabled(true);
			removeNotes.setText("Remove Note");
			renameNote.setEnabled(true);
		} else {
			addNote.setEnabled(false);
			removeNotes.setText("Remove Notes");
			renameNote.setEnabled(false);
		}
	}
}
