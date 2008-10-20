/**
 * Copyright (C) 2008 Alison Farlie
 * 
 * This file is part of KoalaNotes.
 * 
 * KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * KoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with KoalaNotes.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.display.ImageRegistry;

public class FileActionGroup implements ActionGroup {
	private DisplayedDocument dd;
	
	private MenuManager fileMenu;
	
	private Action newDocument;
	private Action open;
	private Action save;
	private Action saveAs;
	private Action exit;
	
	public FileActionGroup(FileMenuController fmc, DisplayedDocument dd) {
		this.dd = dd;
		newDocument = new Action(fmc.new FileNewAction(), "&New");
		open = new Action(fmc.new FileOpenAction(), "&Open");
		save = new Action(fmc.new FileSaveAction(), "&Save");
		save.setAccelerator(SWT.CONTROL | 'S');
		save.setImageDescriptor(dd.getImageRegistry().getDescriptor(ImageRegistry.ACTION_ICON_FILE_SAVE));
		saveAs = new Action(fmc.new FileSaveAsAction(), "Save &As");
		exit = new Action(fmc.new FileExitAction(), "E&xit");
	}
	
	public void update() {
		save.setEnabled(dd.isModified() && dd.getDocument().hasStore());
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
	
	public void populateCoolBar(CoolBarManager coolBar) {
		ToolBarManager tbm = new ToolBarManager();
		tbm.add(save);
		coolBar.add(tbm);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
}
