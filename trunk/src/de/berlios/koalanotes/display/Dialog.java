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
package de.berlios.koalanotes.display;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.INoArgsAction;

/**
 * A very generic dialog box, helpful but not mandatory.
 */
public class Dialog {
	private static final String OK = "Ok";
	private static final String CANCEL = "Cancel";
	
	private Shell myDialog;
	private Composite mainSection;
	private Composite buttonSection;
	private List<Button> buttons;
	
	public Dialog(Shell parent, ImageRegistry imageRegistry, String title) {
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
	
	public Dialog(Shell parent, ImageRegistry imageRegistry, String title,
	              INoArgsAction okAction) {
		this(parent, imageRegistry, title);
		Button okButton = addButton(OK, okAction);
		myDialog.setDefaultButton(okButton);
	}
	
	public Dialog(Shell parent, ImageRegistry imageRegistry, String title,
	              INoArgsAction okAction, INoArgsAction cancelAction) {
		this(parent, imageRegistry, title);
		Button okButton = addButton(OK, okAction);
		addButton(CANCEL, cancelAction);
		myDialog.setDefaultButton(okButton);
	}
	
	public Composite getMainSection() {
		return mainSection;
	}
	
	public Button addButton(String buttonName, final INoArgsAction action) {
		
		// create the button
		Button b = new Button(buttonSection, SWT.NONE);
		b.setText(buttonName);
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				action.invoke();
			}
		});
		
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
