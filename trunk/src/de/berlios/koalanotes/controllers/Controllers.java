package de.berlios.koalanotes.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.exceptions.KoalaException;

public class Controllers {
	private List<Controller> controllers;
	public Controllers(DisplayedDocument dd) {
		controllers = new LinkedList<Controller>();
		controllers.add(new MainController(dd));
		controllers.add(new MainMenuController(dd));
		controllers.add(new TreeContextMenuController(dd));
		controllers.add(new TreeController(dd));
	}
	
	public void invokeControllerMethod(String methodDescriptor, Event e) {
		
		// Get the controller and method.
		Controller controller = null;
		Method method = null;
		Iterator<Controller> iter = controllers.iterator();
		while (iter.hasNext() && (method == null)) {
			controller = iter.next();
			method = controller.getMethod(methodDescriptor);
		}
		if (method == null) {
			throw new KoalaException("Koala Notes could not find method '" + methodDescriptor + "'.");
		}
		
		// Invoke the method.
		try {
			method.invoke(controller, new Object[] {e});
		} catch (IllegalAccessException iaex) {
			throw new KoalaException("Koala Notes could not access method '" + methodDescriptor + "'.", iaex);
		} catch (InvocationTargetException itex) {
			if (itex.getCause() instanceof RuntimeException) {
				throw (RuntimeException) itex.getCause();
			} else throw new KoalaException(itex.getCause());
		}
	}
}
