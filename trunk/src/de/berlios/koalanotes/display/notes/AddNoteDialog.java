package de.berlios.koalanotes.display.notes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.Dialog;

public class AddNoteDialog {
	private Dialog dialog;
	private Text name;
	
	public AddNoteDialog(Shell shell, Dispatcher d) {
		dialog = new Dialog(shell, d, AddNoteController.OK, AddNoteController.CANCEL);
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
