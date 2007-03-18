package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.DisplayedDocument;

public class FileActionGroup implements ActionGroup {
	private DisplayedDocument dd;
	
	private MenuManager fileMenu;
	
	private Action newDocument;
	private Action open;
	private Action save;
	private Action saveAs;
	private Action exit;
	
	public FileActionGroup(Dispatcher d, DisplayedDocument dd) {
		this.dd = dd;
		newDocument = new Action(d, FileMenuController.FILE_NEW, "&New");
		open = new Action(d, FileMenuController.FILE_OPEN, "&Open");
		save = new Action(d, FileMenuController.FILE_SAVE, "&Save");
		save.setAccelerator(SWT.CONTROL | 'S');
		saveAs = new Action(d, FileMenuController.FILE_SAVE_AS, "Save &As");
		exit = new Action(d, FileMenuController.FILE_EXIT, "E&xit");
	}
	
	public void update() {
		if (dd.getDocument().hasStore()) save.setEnabled(true); else save.setEnabled(false);
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		fileMenu = new MenuManager("&File");
		fileMenu.add(newDocument);
		fileMenu.add(open);
		fileMenu.add(new Separator());
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(new Separator());
		fileMenu.add(exit);
		menuBar.add(fileMenu);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
}
