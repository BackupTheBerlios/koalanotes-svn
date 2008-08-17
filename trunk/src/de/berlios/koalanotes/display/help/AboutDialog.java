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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.Dialog;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.menus.HelpMenuController;
import de.berlios.koalanotes.exceptions.KoalaException;

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
		rl2.spacing = 20;
		textSection.setLayout(rl2);
		
		Label copyrightLabel = new Label(textSection, SWT.LEFT | SWT.WRAP);
		RowData rd = new RowData(300, SWT.DEFAULT);
		copyrightLabel.setLayoutData(rd);
		StringBuilder sb = new StringBuilder();
		sb.append("Koala Notes\n");
		sb.append("Version: Alpha\n");
		sb.append("Copyright (C) 2008 Alison Farlie");
		copyrightLabel.setText(sb.toString());

		File f = new File(""); // to get the file path for the local links
		OpenURL openURL = new OpenURL();
		
		ScrolledComposite scroller = new ScrolledComposite(textSection, SWT.V_SCROLL | SWT.BORDER);
		RowData rd2 = new RowData(300, 100);
		scroller.setLayoutData(rd2);
		Link gplLink = new Link(scroller, SWT.LEFT | SWT.WRAP);
		scroller.setContent(gplLink);		
		sb = new StringBuilder();
		sb.append("KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n");
		sb.append("\nKoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n");
		sb.append("\nA copy of the GNU General Public License has been distributed with KoalaNotes at");
		sb.append(" <a href=\"file://" + f.getAbsolutePath() + "/docs/gpl.txt\">/docs/gpl.text</a>.");
		sb.append("It can also be found at <a href=\"http://www.gnu.org/licenses/\">http://www.gnu.org/licenses/</a>.");
		gplLink.setText(sb.toString());
		gplLink.addSelectionListener(openURL);
		gplLink.setSize(gplLink.computeSize(300, SWT.DEFAULT));

		Link thirdPartyLink = new Link(textSection, SWT.LEFT | SWT.WRAP);
		RowData rd3 = new RowData(300, SWT.DEFAULT);
		thirdPartyLink.setLayoutData(rd3);
		sb = new StringBuilder();
		sb.append("KoalaNotes is written in <a href=\"http://java.sun.com\">Java</a>,");
		sb.append(" and uses <a href=\"http://www.jdom.org/\">JDOM</a>,  ");
		sb.append(" <a href=\"http://www.eclipse.org/swt\">SWT</a>,");
		sb.append(" and <a href=\"http://www.eclipse.org\">JFace</a>.");
		sb.append(" Licensing information for these products is distributed with KoalaNotes,");
		sb.append(" with an overview at");
		sb.append(" <a href=\"file://" + f.getAbsolutePath() + "/readme.html\">readme.html</a>.");
		thirdPartyLink.setText(sb.toString());
		thirdPartyLink.addSelectionListener(openURL);
	}
	
	private class OpenURL implements SelectionListener {
		public OpenURL() {}
		public void widgetDefaultSelected(SelectionEvent e) {}
		public void widgetSelected(SelectionEvent e) {
			try {
				Desktop.getDesktop().browse(new URI(e.text.toString()));
			} catch (IOException ioe) {
				throw new KoalaException("Koala Notes failed while attempting to open the URL " + e.text.toString(), ioe);		
			} catch (URISyntaxException urise) {
				throw new KoalaException("Koala Notes failed while attempting to open the URL " + e.text.toString(), urise);
			}
		}
	}	
	
	public void open() {dialog.open();}
	public void dispose() {dialog.dispose();}
}
