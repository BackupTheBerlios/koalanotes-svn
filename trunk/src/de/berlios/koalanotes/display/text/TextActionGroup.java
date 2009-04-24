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
package de.berlios.koalanotes.display.text;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.ActionGroupHelper;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.NoteTabFolder;
import de.berlios.koalanotes.display.menus.DisableableMenuManager;

public class TextActionGroup implements ActionGroup {
	
	// the note tree, from which TextActionGroup derives its state
	private NoteTabFolder tabFolder;
	private KoalaStyleManager koalaStyleManager;
	
	// groupings of actions
	private ActionGroupHelper allActions;
	
	// menu managers
	private DisableableMenuManager textMenu;
	
	// actions
	private Action chooseStyle;
	private Action chooseFont;
	private Action bold;
	private Action italic;
	private Action underline;
	private Action chooseForeColor;
	private Action chooseBackColor;
	
	public TextActionGroup(TextController tc, NoteTabFolder tabFolder,
	                       KoalaStyleManager koalaStyleManager, ImageRegistry imageRegistry) {
		this.tabFolder = tabFolder;
		this.koalaStyleManager = koalaStyleManager;
		
		// construct actions
		chooseStyle = new Action(tc.new ChooseStyleAction(null), "Style", IAction.AS_DROP_DOWN_MENU);
		chooseStyle.setMenuCreator(new ChooseStyleMenuCreator(tc, koalaStyleManager));
		chooseFont = new Action(tc.new ChooseFontAction(), "Font...");
		bold = new Action(tc.new BoldAction(), "Bold", IAction.AS_CHECK_BOX);
		italic = new Action(tc.new ItalicAction(), "Italic", IAction.AS_CHECK_BOX);
		underline = new Action(tc.new UnderlineAction(), "Underline", IAction.AS_CHECK_BOX);
		chooseForeColor = new Action(tc.new ChooseForeColorAction(), "Color...");
		chooseBackColor = new Action(tc.new ChooseBackColorAction(), "Background Color...");
		
		// set action shortcut keys
		bold.setAccelerator(SWT.CONTROL | 'B');
		italic.setAccelerator(SWT.CONTROL | 'I');
		underline.setAccelerator(SWT.CONTROL | 'U');
		
		// set icons
		bold.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.IMAGE_KOALA_SMALL));
		italic.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.IMAGE_KOALA_SMALL));
		underline.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.IMAGE_KOALA_SMALL));
		
		// construct action group helpers
		allActions = new ActionGroupHelper();
		
		// populate action group helpers
		allActions.add(chooseStyle);
		allActions.add(chooseFont);
		allActions.add(bold);
		allActions.add(italic);
		allActions.add(underline);
		allActions.add(chooseForeColor);
		allActions.add(chooseBackColor);
	}
	
	public void update() {
		// Actions from this group are visible if and only if a rich text widget is in focus.			
		if (tabFolder.getSelectedNoteTab() == null
			|| !tabFolder.getSelectedNoteTab().getKoalaStyledText().isFocusControl()) {
			
			textMenu.setEnabled(false);
			allActions.setEnabled(false);
		} else {
			textMenu.setEnabled(true);
			allActions.setEnabled(true);
			
			chooseStyle.setText(koalaStyleManager.getCurrentStyleName());
			FontData fd = koalaStyleManager.getCurrentStyleFontData()[0];
			bold.setChecked((fd.getStyle() & SWT.BOLD) == SWT.BOLD);
			italic.setChecked((fd.getStyle() & SWT.ITALIC) == SWT.ITALIC);
			underline.setChecked(koalaStyleManager.getCurrentStyleUnderline());
		}
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		textMenu = new DisableableMenuManager("&Text");
		textMenu.add(chooseFont);
		textMenu.add(bold);
		textMenu.add(italic);
		textMenu.add(underline);
		textMenu.add(chooseForeColor);
		textMenu.add(chooseBackColor);
		menuBar.add(textMenu);
	}
	
	public void populateCoolBar(CoolBarManager coolBar) {
		ToolBarManager tbm = new ToolBarManager();
		tbm.add(chooseStyle);
		tbm.add(chooseFont);
		tbm.add(bold);
		tbm.add(italic);
		tbm.add(underline);
		tbm.add(chooseForeColor);
		tbm.add(chooseBackColor);
		coolBar.add(tbm);
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
	
	private class ChooseStyleMenuCreator implements IMenuCreator {
		private TextController tc;
		private KoalaStyleManager koalaStyleManager;
		private MenuManager menu;
		
		private ChooseStyleMenuCreator(TextController tc, KoalaStyleManager koalaStyleManager) {
			this.tc = tc;
			this.koalaStyleManager = koalaStyleManager;
		}
		
		/** implements IMenuCreator.dispose() */
		public void dispose() {
			if (menu != null) {
				menu.dispose();
			}
		}
		
		/** implements IMenuCreator.getMenu(Control parent) */
		public Menu getMenu(Control parent) {
			dispose();
			populateMenu();
			return menu.createContextMenu(parent);
		}
		
		/** implements IMenuCreator.getMenu(Menu parent): does nothing, never called */
		public Menu getMenu(Menu parent) {
			return null;
		}
		
		private void populateMenu() {
			menu = new MenuManager("Style");
			for (String s : koalaStyleManager.getStyleNames()) {
				menu.add(new Action(tc.new ChooseStyleAction(s), s));
			}
		}
	}
}
