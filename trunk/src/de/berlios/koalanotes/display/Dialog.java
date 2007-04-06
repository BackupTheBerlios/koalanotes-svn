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

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.controllers.Listener;

/**
 * A very generic dialog box, helpful but not mandatory.
 */
public class Dialog {
	private static final String OK = "Ok";
	private static final String CANCEL = "Cancel";
	
	private Dispatcher d;
	private Shell myDialog;
	private Composite mainSection;
	private Composite buttonSection;
	private List<Button> buttons;
	
	public Dialog(Shell parent, Dispatcher d, ImageRegistry imageRegistry, String title) {
		this.d = d;
		myDialog = new Shell(parent, SWT.DIALOG_TRIM);
		myDialog.setText(title);
		myDialog.setImage(imageRegistry.get(ImageRegistry.IMAGE_KOALA_SMALL));
		RowLayout myDialogLayout = new RowLayout(SWT.VERTICAL);
		myDialogLayout.fill = true;
		myDialog.setLayout(myDialogLayout);
		mainSection = new Composite(myDialog, SWT.NONE);
		buttonSection = new Composite(myDialog, SWT.NONE);
		buttonSection.setLayout(new FormLayout());
		buttons = new LinkedList<Button>();
	}
	
	public Dialog(Shell parent, Dispatcher d, ImageRegistry imageRegistry, String title,
	              String okControllerMethodDescriptor) {
		this(parent, d, imageRegistry, title);
		Button okButton = addButton(OK, okControllerMethodDescriptor);
		myDialog.setDefaultButton(okButton);
	}
	
	public Dialog(Shell parent, Dispatcher d, ImageRegistry imageRegistry, String title,
	              String okControllerMethodDescriptor,
	              String cancelMethodDescriptor) {
		this(parent, d, imageRegistry, title);
		Button okButton = addButton(OK, okControllerMethodDescriptor);
		addButton(CANCEL, cancelMethodDescriptor);
		myDialog.setDefaultButton(okButton);
	}
	
	public Composite getMainSection() {
		return mainSection;
	}
	
	public Button addButton(String buttonName, String controllerMethodDescriptor) {
		
		// create the button
		Button b = new Button(buttonSection, SWT.NONE);
		b.setText(buttonName);
		b.addListener(SWT.Selection, new Listener(d, controllerMethodDescriptor));
		
		// create the layout data
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0);
		if (buttons.size() == 0) {
			fdata.right = new FormAttachment(100);
		} else {
			fdata.right = new FormAttachment(buttons.get(buttons.size() - 1));
		}
		fdata.bottom = new FormAttachment(100);
		fdata.width = b.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (fdata.width < 80) {
			fdata.width = 80;
		}
		b.setLayoutData(fdata);
		
		// add the button
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
