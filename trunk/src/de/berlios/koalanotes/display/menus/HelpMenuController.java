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

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.help.AboutDialog;

public class HelpMenuController extends Controller {
	private Shell shell;
	private ImageRegistry imageRegistry;
	
	private AboutDialog aboutDialog;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(HelpMenuController.class, methodName);
	}
	
	public HelpMenuController(Dispatcher d, Shell shell, ImageRegistry imageRegistry) {
		super(d);
		this.shell = shell;
		this.imageRegistry = imageRegistry;
	}

	public static final String HELP_ABOUT = getMethodDescriptor("helpAbout");
	public void helpAbout(Event e) {
		aboutDialog = new AboutDialog(shell, d, imageRegistry);
		aboutDialog.open();
	}
	
	public static final String HELP_ABOUT_OK_PRESSED = getMethodDescriptor("helpAboutOkPressed");
	public void helpAboutOkPressed(Event e) {
		aboutDialog.dispose();
	}
}
