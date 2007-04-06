package de.berlios.koalanotes.display.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.Dialog;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.menus.HelpMenuController;

public class AboutDialog {
	private Dialog dialog;
	
	public AboutDialog(Shell shell, Dispatcher d, ImageRegistry imageRegistry) {
		dialog = new Dialog(shell, d, imageRegistry, "About Koala Notes",
		                    HelpMenuController.HELP_ABOUT_OK_PRESSED);
		Composite mainSection = dialog.getMainSection();
		RowLayout rl = new RowLayout();
		rl.fill = true;
		rl.spacing = 20;
		mainSection.setLayout(rl);
		
		Label imageLabel = new Label(dialog.getMainSection(), SWT.LEFT);
		Image image = imageRegistry.get(ImageRegistry.IMAGE_KOALA_BIG);
		imageLabel.setImage(image);
		
		Label label = new Label(dialog.getMainSection(), SWT.LEFT);
		StringBuilder sb = new StringBuilder();
		sb.append("\nKoala Notes\n");
		sb.append("\nVersion: Alpha\n");
		sb.append("\n(c) Copyright Alison Farlie 2007.\nAll rights reserved.");
		label.setText(sb.toString());
	}
	
	public void open() {dialog.open();}
	public void dispose() {dialog.dispose();}
}
