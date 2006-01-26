package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.controllers.MainController;

public class MainMenu {
	public MainMenu(Shell shell, Listener l) {
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		Menu fileMenu = createMenu(menuBar, "&File");
		createMenuItem(fileMenu, "Open", l, MainController.FILE_OPEN);
		createMenuItem(fileMenu, "Save", l, MainController.FILE_SAVE);
		createMenuItem(fileMenu, "Save As", l, MainController.FILE_SAVE_AS);
		shell.setMenuBar(menuBar);
	}
	
	private Menu createMenu(Menu menuBar, String text) {
		Menu menu = new Menu(menuBar);
		MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
		menuHeader.setText("&File");
		menuHeader.setMenu(menu);
		return menu;
	}
	
	private void createMenuItem(Menu parent, String text,
	                            Listener l, String controllerMethod) {
		MenuItem menuItem = new MenuItem(parent, SWT.CASCADE);
		menuItem.setText(text);
		l.mapEvent(menuItem, SWT.Selection, controllerMethod);
	}
}
