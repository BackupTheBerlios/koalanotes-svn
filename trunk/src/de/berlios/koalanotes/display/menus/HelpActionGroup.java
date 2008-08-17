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

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.ImageRegistry;

public class HelpActionGroup implements ActionGroup {
	
	private MenuManager helpMenu;
	
	private Action about;

	public HelpActionGroup(Dispatcher d, ImageRegistry imageRegistry) {
		about = new Action(d, HelpMenuController.HELP_ABOUT, "&About");
		about.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.IMAGE_KOALA_SMALL));
	}
	
	public void update() {
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		helpMenu = new MenuManager("&Help");
		helpMenu.add(about);
		menuBar.add(helpMenu);
	}
	
	public void populateCoolBar(CoolBarManager coolBar) {
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
}
