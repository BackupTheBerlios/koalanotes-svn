import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import display.DisplayedDocument;

public class KoalaNotes {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		new DisplayedDocument(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
