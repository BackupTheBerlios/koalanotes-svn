package de.berlios.koalanotes;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.DisplayedDocument;

public class KoalaNotes {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Dispatcher dispatcher = new Dispatcher();
		new DisplayedDocument(shell, dispatcher);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
