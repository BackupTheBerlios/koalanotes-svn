package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
	private List<Button> buttons;
	
	public Dialog(Shell parent, Listener l) {
		this.l = l;
		myDialog = new Shell(parent, SWT.DIALOG_TRIM);
		RowLayout myDialogLayout = new RowLayout(SWT.VERTICAL);
		myDialogLayout.fill = true;
		myDialog.setLayout(myDialogLayout);
		mainSection = new Composite(myDialog, SWT.NONE);
		buttonSection = new Composite(myDialog, SWT.NONE);
		buttonSection.setLayout(new FormLayout());
		buttons = new LinkedList<Button>();
	}
	
	public Dialog(Shell parent, Listener l,
	              String okControllerMethodDescriptor,
	              String cancelMethodDescriptor) {
		this(parent, l);
		Button okButton = addButton(OK, okControllerMethodDescriptor);
		addButton(CANCEL, cancelMethodDescriptor);
		myDialog.setDefaultButton(okButton);
	}
	
	public Composite getMainSection() {
		return mainSection;
	}
	
	public Button addButton(String buttonName, String controllerMethodDescriptor) {
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0);
		if (buttons.size() == 0) {
			fdata.right = new FormAttachment(100);
		} else {
			fdata.right = new FormAttachment(buttons.get(buttons.size() - 1));
		}
		fdata.bottom = new FormAttachment(100);
		Button b = new Button(buttonSection, SWT.NONE);
		b.setText(buttonName);
		b.setLayoutData(fdata);
		l.mapEvent(b, SWT.Selection, controllerMethodDescriptor);
		buttons.add(b);
		return b;
	}
	
	public void open() {
		myDialog.pack();
		myDialog.open();
	}
	
	public void dispose() {
		myDialog.dispose();
	}
}
