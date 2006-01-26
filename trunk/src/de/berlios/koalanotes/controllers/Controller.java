package de.berlios.koalanotes.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.exceptions.KoalaException;

public abstract class Controller {
	
	private static final Class[] METHOD_ARGS = new Class[] {Event.class};
	private static final String SEPARATOR = ".";
	
	protected DisplayedDocument dd;
	
	public Controller(DisplayedDocument displayedDocument) {
		this.dd = displayedDocument;
	}
	
	protected static String getMethodDescriptor(Class controller, String methodName) {
		return controller.getCanonicalName() + SEPARATOR + methodName;
	}
	
	public void invokeControllerMethod(String methodDescriptor, Event e) {
		
		// Get the controller and method.
		Controller controller = null;
		Method method = null;
		int separator = methodDescriptor.lastIndexOf(SEPARATOR);
		String methodName = methodDescriptor.substring(separator + 1);
		Iterator<Controller> iter = dd.getControllers().iterator();
		while (iter.hasNext() && (method == null)) {
			controller = iter.next();
			if (methodDescriptor.startsWith(controller.getClass().getCanonicalName())) {
				try {
					method = controller.getClass().getMethod(methodName, METHOD_ARGS);
				} catch (NoSuchMethodException ex) {
					throw new KoalaException("Koala Notes could not find method '" + methodDescriptor + "'.", ex);
				}
			}
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
