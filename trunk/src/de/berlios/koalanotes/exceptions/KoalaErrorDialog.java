package de.berlios.koalanotes.exceptions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.KoalaNotes;

public class KoalaErrorDialog {
	private MessageBox dialog;
	
	public KoalaErrorDialog(Shell shell, KoalaException ke) {
		initialiseDialog(shell);
		if (ke.getMessage() != null) {
			dialog.setMessage(ke.getMessage());
			if (ke.getCause() != null) {
				dialog.setMessage(ke.getMessage() + "\n" + ke.getCause().getMessage());
			}
		} else if (ke.getCause() != null) {
			dialog.setMessage(ke.getCause().getMessage());
		} else {
			dialog.setMessage("An error occurred.");
		}
	}
	
	public KoalaErrorDialog(Shell shell, Throwable t) {
		initialiseDialog(shell);
		dialog.setMessage("An unexpected error occurred:\n"
		                  + t.getMessage()
		                  + "\nThe application will be shut down."
		                  + "\nFurther information has been printed to the logs at " + KoalaNotes.LOG_LOCATION + ".");
	}
	
	public void open() {dialog.open();}
	
	private void initialiseDialog(Shell shell) {
		dialog = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		dialog.setText("Koala Notes Error");
	}
}
