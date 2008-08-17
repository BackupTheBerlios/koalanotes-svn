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
package de.berlios.koalanotes.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A dialog to display a KoalaNotes error, handles KoalaExceptions and Throwables alike.  I have
 * tried to prevent this class from having too many dependencies on the rest of KoalaNotes, because
 * this needs to work when other parts of KoalaNotes have failed.
 */
public class KoalaErrorDialog {
	private Shell dialog;
	private Label messageLabel;
	private Text detailText;
	private Button detailButton;
	private FormData detailTextLayoutData;
	
	public KoalaErrorDialog(Shell shell, KoalaException ke) {
		initialiseDialog(shell);
		setMessage(getFirstMessage(ke));
		setDetail(ke);
	}
	
	public KoalaErrorDialog(Shell shell, Throwable t) {
		initialiseDialog(shell);
		setMessage("An unexpected error occurred:\n"
                   + getFirstMessage(t)
                   + "\nThe application will be shut down.");
		setDetail(t);
	}
	
	public KoalaErrorDialog(Shell shell, Throwable t, String documentBackupLocation) {
		initialiseDialog(shell);
		String backupMessage = "A backup of your document could not be saved.";
		if (documentBackupLocation != null) {
			backupMessage = "A backup of your document has been saved at " + documentBackupLocation;
		}
		setMessage("An unexpected error occurred:\n"
                   + getFirstMessage(t)
                   + "\nThe application will be shut down.\n"
                   + backupMessage);
		setDetail(t);
	}
	
	private String getFirstMessage(Throwable t) {
		if (t.getMessage() != null && t.getMessage().length() > 0) {
			return t.getMessage();
		} else if (t.getCause() != null) {
			return getFirstMessage(t.getCause());
		} else {
			return "An error occurred.";
		}
	}
	
	public void open() {
		dialog.pack();
		dialog.open();
	}
	
	private void initialiseDialog(Shell shell) {
		
		// dialog
		dialog = new Shell(shell, SWT.SHELL_TRIM);
		dialog.setText("Koala Notes Error");
		
		// dialog layout
		FormLayout dialogLayout = new FormLayout();
		dialogLayout.marginHeight = 10;
		dialogLayout.marginWidth = 10;
		dialog.setLayout(dialogLayout);
		
		// message label
		messageLabel = new Label(dialog, SWT.WRAP);
		
		// detail text
		detailText = new Text(dialog, SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		detailText.setVisible(false);
		
		// detail button
		detailButton = new Button(dialog, SWT.NONE);
		detailButton.setText("Show Detail");
		detailButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				detailText.setVisible(!detailText.isVisible());
				if (detailTextLayoutData.height == 0) {
					detailTextLayoutData.height = 100;
					detailButton.setText("Hide Detail");
				} else {
					detailTextLayoutData.height = 0;
					detailButton.setText("Show Detail");
				}
				dialog.pack();
			}
		});
		
		// ok button
		Button okButton = new Button(dialog, SWT.NONE);
		okButton.setText("Ok");
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				dialog.close();
			}
		});
		
		// message label layout
		FormData messageLabelLayoutData = new FormData();
		messageLabelLayoutData.top = new FormAttachment(0);
		messageLabelLayoutData.left = new FormAttachment(0);
		messageLabelLayoutData.right = new FormAttachment(100);
		messageLabel.setLayoutData(messageLabelLayoutData);
		
		// detail button layout
		FormData detailButtonLayoutData = new FormData();
		detailButtonLayoutData.top = new FormAttachment(messageLabel);
		detailButtonLayoutData.right = new FormAttachment(100);
		detailButton.setLayoutData(detailButtonLayoutData);
		
		// ok button layout
		FormData okButtonFormData = new FormData();
		okButtonFormData.bottom = new FormAttachment(100);
		okButtonFormData.right = new FormAttachment(100);
		okButtonFormData.width = detailButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		okButton.setLayoutData(okButtonFormData);
		
		// detail text layout
		detailTextLayoutData = new FormData();
		detailTextLayoutData.top = new FormAttachment(detailButton);
		detailTextLayoutData.bottom = new FormAttachment(okButton, -10);
		detailTextLayoutData.left = new FormAttachment(0);
		detailTextLayoutData.right = new FormAttachment(100);
		detailTextLayoutData.height = 0;
		detailTextLayoutData.width = 200;
		detailText.setLayoutData(detailTextLayoutData);
	}
	
	private void setMessage(String message) {
		messageLabel.setText(message);
	}
	
	private void setDetail(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		detailText.setText(sw.toString());
	}
}
