package de.berlios.koalanotes.display.menus;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.display.ImageRegistry;
import de.berlios.koalanotes.display.help.AboutDialog;

public class HelpMenuController extends Controller {
	private Shell shell;
	private ImageRegistry imageRegistry;
	
	private AboutDialog aboutDialog;
	
	public static String getMethodDescriptor(String methodName) {
		return getMethodDescriptor(HelpMenuController.class, methodName);
	}
	
	public HelpMenuController(Dispatcher d, Shell shell, ImageRegistry imageRegistry) {
		super(d);
		this.shell = shell;
		this.imageRegistry = imageRegistry;
	}

	public static final String HELP_ABOUT = getMethodDescriptor("helpAbout");
	public void helpAbout(Event e) {
		aboutDialog = new AboutDialog(shell, d, imageRegistry);
		aboutDialog.open();
	}
	
	public static final String HELP_ABOUT_OK_PRESSED = getMethodDescriptor("helpAboutOkPressed");
	public void helpAboutOkPressed(Event e) {
		aboutDialog.dispose();
	}
}
