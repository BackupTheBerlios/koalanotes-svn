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
package de.berlios.koalanotes;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.exceptions.KoalaErrorDialog;
import de.berlios.koalanotes.exceptions.KoalaException;

public class KoalaNotes {
	public static final String KOALA_NOTES_VERSION = "0.1";
	public static final String LOG_LOCATION = "System.out";
	public static final String DEFAULT_BACKUP_PATH = "backup.bak";
	
	public static void main(String[] args) {
		
		// Initialise and display.
		Display display = new Display();
		Shell shell = new Shell(display);
		MainController mc = new MainController(shell);
		shell.open();
		
		// Main event loop.
		while (!shell.isDisposed()) {
			
			// Do the stuff.
			try {
				if (!display.readAndDispatch()) display.sleep();
				
			// If a KoalaException is thrown just display an error dialog and keep going, as we
			// assume KoalaExceptions have been properly dealt with.
			} catch (KoalaException ke) {
				new KoalaErrorDialog(shell, ke).open();
				
			// If any other exception or error is thrown then print the stack trace, save a backup
			// of the document, display an error dialog and shut down the application.
			} catch (Throwable t) {
				String backupLocation = null;
				try {
					backupLocation = mc.saveBackup();
				} catch (Throwable t2) {
					t2.printStackTrace();
				}
				t.printStackTrace();
				new KoalaErrorDialog(shell, t, backupLocation).open();
				shell.dispose();
			}
		}
		display.dispose();
	}
}
