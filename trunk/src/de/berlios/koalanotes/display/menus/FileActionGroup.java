package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Document;

public class FileActionGroup implements ActionGroup {
	private Document document;
	
	private MenuManager fileMenu;
	
	private Action open;
	private Action save;
	private Action saveAs;
	
	public FileActionGroup(Dispatcher d, Document document) {
		this.document = document;
		open = new Action(d, FileMenuController.FILE_OPEN, "&Open");
		save = new Action(d, FileMenuController.FILE_SAVE, "&Save");
		save.setAccelerator(SWT.CONTROL | 's');
		saveAs = new Action(d, FileMenuController.FILE_SAVE_AS, "&SaveAs");
	}
	
	public void update() {
		if (document.hasFile()) save.setEnabled(true); else save.setEnabled(false);
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		fileMenu = new MenuManager("File");
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		menuBar.add(fileMenu);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
}
