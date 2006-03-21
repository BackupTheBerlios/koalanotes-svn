package de.berlios.koalanotes.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Listener;

/**
 * A very generic dialog box, helpful but not mandatory.
 */
public class Dialog {
	private static final String OK = "Ok";
	private static final String CANCEL = "Cancel";
	
	private Listener l;
	private Shell myDialog;
	private Composite mainSection;
	private Composite buttonSection;
	
	public Dialog(Shell parent, Listener l) {
		this.l = l;
		myDialog = new Shell(parent, SWT.DIALOG_TRIM);
		myDialog.setLayout(new RowLayout(SWT.VERTICAL));
		mainSection = new Composite(myDialog, SWT.NONE);
		mainSection.setLayout(new RowLayout());
		buttonSection = new Composite(myDialog, SWT.NONE);
		buttonSection.setLayout(new RowLayout());
	}
	
	public Dialog(Shell parent, Listener l,
	              String okControllerMethodDescriptor,
	              String cancelMethodDescriptor) {
		this(parent, l);
		addButton(CANCEL, cancelMethodDescriptor);
		addButton(OK, okControllerMethodDescriptor);
	}
	
	public Composite getMainSection() {
		return mainSection;
	}
	
	public void addButton(String buttonName, String controllerMethodDescriptor) {
		Button b = new Button(buttonSection, SWT.NONE);
		b.setText(buttonName);
		l.mapEvent(b, SWT.Selection, controllerMethodDescriptor);
	}
	
	public void open() {
		myDialog.pack();
		myDialog.open();
	}
}
