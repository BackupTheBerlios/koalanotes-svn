package de.berlios.koalanotes.display.menus;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;

import de.berlios.koalanotes.controllers.Action;
import de.berlios.koalanotes.controllers.ActionGroup;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.ImageRegistry;

public class HelpActionGroup implements ActionGroup {
	
	private MenuManager helpMenu;
	
	private Action about;

	public HelpActionGroup(Dispatcher d, ImageRegistry imageRegistry) {
		about = new Action(d, HelpMenuController.HELP_ABOUT, "&About");
		about.setImageDescriptor(imageRegistry.getDescriptor(ImageRegistry.IMAGE_KOALA_SMALL));
	}
	
	public void update() {
	}
	
	public void populateMenuBar(MenuManager menuBar) {
		helpMenu = new MenuManager("&Help");
		helpMenu.add(about);
		menuBar.add(helpMenu);
	}
	
	public void populateCoolBar(CoolBarManager coolBar) {
	}
	
	public void populateTreeContextMenu(MenuManager treeContextMenu) {
	}
}
