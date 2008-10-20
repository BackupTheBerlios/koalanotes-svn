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

import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.INoArgsAction;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.help.AboutDialog;

public class HelpMenuController {
	private Shell shell;
	private ImageRegistry imageRegistry;
	
	private AboutDialog aboutDialog;
	
	public HelpMenuController(Shell shell, ImageRegistry imageRegistry) {
		this.shell = shell;
		this.imageRegistry = imageRegistry;
	}

	public class HelpAboutAction implements INoArgsAction {
		public void invoke() {
			aboutDialog = new AboutDialog(shell, imageRegistry, new HelpAboutOKPressedAction());
			aboutDialog.open();
		}
	}
	
	public class HelpAboutOKPressedAction implements INoArgsAction {
		public void invoke() {
			aboutDialog.dispose();
		}
	}
}
