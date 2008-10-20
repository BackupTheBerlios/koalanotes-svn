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
package de.berlios.koalanotes.display.notes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.berlios.koalanotes.display.Dialog;
import de.berlios.koalanotes.display.ImageRegistry;

public class AddNoteDialog {
	private Dialog dialog;
	private Text name;
	
	public AddNoteDialog(Shell shell, ImageRegistry imageRegistry,
	                     AddNoteController.AddNoteOKAction okAction,
	                     AddNoteController.AddNoteCancelAction cancelAction) {
		dialog = new Dialog(shell, imageRegistry, "Add Note", okAction, cancelAction);
		Composite mainSection = dialog.getMainSection();
		mainSection.setLayout(new RowLayout());
		name = new Text(dialog.getMainSection(), SWT.LEFT);
		name.setText("mmmm_mmmm_mmmm_"); // make text width 15 ems
		name.setLayoutData(new RowData(name.computeSize(SWT.DEFAULT, SWT.DEFAULT)));
		name.setText("");
	}
	
	public void open() {dialog.open();}
	public void dispose() {dialog.dispose();}
	
	public String getName() {return name.getText();}
	public void setName(String name) {this.name.setText(name);}
}
