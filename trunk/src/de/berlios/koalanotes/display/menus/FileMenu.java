package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The File Menu goes on the main menu bar.
 */
public class FileMenu {
	MenuItem fileOpen;
	MenuItem fileSave;
	MenuItem fileSaveAs;
	
	public FileMenu(Menu menuBar, MenuHelper mh) {
		Menu m = mh.createSubMenu(menuBar, "&File", FileMenuController.INITIALISE_MENU);
		fileOpen = mh.createMenuItem(m, "&Open", FileMenuController.FILE_OPEN);
		fileSave = mh.createMenuItem(m, "&Save", FileMenuController.FILE_SAVE);
		fileSaveAs = mh.createMenuItem(m, "Save &As", FileMenuController.FILE_SAVE_AS);
		mh.setAccelerator(fileSave, SWT.CONTROL | 'S', "Ctrl-S");
	}
	
	public void initialise(boolean hasFile) {
		if (hasFile) fileSave.setEnabled(true); else fileSave.setEnabled(false);
	}
}
