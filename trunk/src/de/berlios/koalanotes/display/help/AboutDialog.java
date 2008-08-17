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
package de.berlios.koalanotes.display.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.fill = true;
		rl.spacing = 20;
		mainSection.setLayout(rl);
		
		Label imageLabel = new Label(mainSection, SWT.LEFT);
		Image image = imageRegistry.get(ImageRegistry.IMAGE_KOALA_BIG);
		imageLabel.setImage(image);

		Composite textSection = new Composite(mainSection, SWT.LEFT);
		RowLayout rl2 = new RowLayout(SWT.VERTICAL);
		rl2.fill = true;
		textSection.setLayout(rl2);
		
		Label copyrightLabel = new Label(textSection, SWT.LEFT | SWT.WRAP);
		RowData rd = new RowData(300, SWT.DEFAULT);
		copyrightLabel.setLayoutData(rd);
		StringBuilder sb = new StringBuilder();
		sb.append("Koala Notes\n");
		sb.append("Version: Alpha\n");
		sb.append("Copyright (C) 2008 Alison Farlie\n");
		copyrightLabel.setText(sb.toString());
		
		Text gplText = new Text(textSection, SWT.LEFT | SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		RowData rd2 = new RowData(300, 100);
		gplText.setLayoutData(rd2);
		sb = new StringBuilder();
		sb.append("KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n");
		sb.append("\nKoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n");
		sb.append("\nA copy of the GNU General Public License has been distributed with KoalaNotes at docs/gpl.txt. It can also be found at http://www.gnu.org/licenses/.\n");
		gplText.setText(sb.toString());
		
		Label thirdPartyLabel = new Label(textSection, SWT.LEFT | SWT.WRAP);
		RowData rd3 = new RowData(300, SWT.DEFAULT);
		thirdPartyLabel.setLayoutData(rd3);
		sb = new StringBuilder();
		sb.append("\nKoalaNotes is written in Java, and uses JDOM, SWT and JFace.  Licensing information for these products is distributed with KoalaNotes, with an overview at readme.html.\n");
		thirdPartyLabel.setText(sb.toString());
	}
	
	public void open() {dialog.open();}
	public void dispose() {dialog.dispose();}
}
