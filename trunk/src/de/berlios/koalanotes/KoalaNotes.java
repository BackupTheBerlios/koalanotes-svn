package de.berlios.koalanotes;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.exceptions.KoalaErrorDialog;
import de.berlios.koalanotes.exceptions.KoalaException;

public class KoalaNotes {
	public static final String KOALA_NOTES_VERSION = "0.1";
	public static final String LOG_LOCATION = "System.out";
	
	public static void main(String[] args) {
		
		// Initialise and display.
		Display display = new Display();
		Shell shell = new Shell(display);
		Dispatcher dispatcher = new Dispatcher();
		new DisplayedDocument(shell, dispatcher);
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
				
			// If any other exception or error is thrown then show an error dialog, print the stack
			// trace, and shut down the application.
			} catch (Throwable t) {
				new KoalaErrorDialog(shell, t).open();
				t.printStackTrace();
				shell.dispose();
			}
		}
		display.dispose();
	}
}
