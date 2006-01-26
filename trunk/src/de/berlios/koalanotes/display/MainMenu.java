package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainMenuController;

public class MainMenu {
	
	MenuItem fileOpen;
	MenuItem fileSave;
	MenuItem fileSaveAs;
	
	public MainMenu(Shell shell, Listener l) {
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		Menu fileMenu = createMenu(menuBar, "&File", l);
		fileOpen = createMenuItem(fileMenu, "&Open", l, MainMenuController.FILE_OPEN);
		fileSave = createMenuItem(fileMenu, "&Save", l, MainMenuController.FILE_SAVE);
		fileSaveAs = createMenuItem(fileMenu, "Save &As", l, MainMenuController.FILE_SAVE_AS);
		shell.setMenuBar(menuBar);
	}
	
	private Menu createMenu(Menu menuBar, String text, Listener l) {
		Menu menu = new Menu(menuBar);
		l.mapEvent(menu, SWT.Show, MainMenuController.INITIALISE_MENU);
		MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
		menuHeader.setText(text);
		menuHeader.setMenu(menu);
		return menu;
	}
	
	private MenuItem createMenuItem(Menu parent, String text,
	                                Listener l, String controllerMethod) {
		MenuItem menuItem = new MenuItem(parent, SWT.CASCADE);
		menuItem.setText(text);
		l.mapEvent(menuItem, SWT.Selection, controllerMethod);
		return menuItem;
	}
	
	public void initialise(boolean hasFile) {
		if (hasFile) fileSave.setEnabled(true); else fileSave.setEnabled(false);
	}
}
