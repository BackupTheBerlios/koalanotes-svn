package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;
import de.berlios.koalanotes.display.menus.FileMenu;
import de.berlios.koalanotes.display.menus.FileMenuController;
import de.berlios.koalanotes.display.menus.MenuHelper;
import de.berlios.koalanotes.display.menus.NoteMenu;
import de.berlios.koalanotes.display.menus.NoteMenuController;

public class MainMenu {
	public MainMenu(Shell shell, Listener l, Dispatcher d, DisplayedDocument dd) {
		MenuHelper mh = new MenuHelper(l);
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		FileMenu fileMenu = new FileMenu(menuBar, mh);
		new FileMenuController(d, dd, fileMenu);
		
		NoteMenu noteMenu = new NoteMenu(menuBar, mh, true);
		new NoteMenuController(d, dd, l, noteMenu);
		
		shell.setMenuBar(menuBar);
	}
}
