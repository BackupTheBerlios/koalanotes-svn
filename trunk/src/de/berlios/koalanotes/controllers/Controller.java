package de.berlios.koalanotes.controllers;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;

public abstract class Controller {
	
	private static final Class[] METHOD_ARGS = new Class[] {Event.class};
	private static final String SEPARATOR = ".";
	
	protected DisplayedDocument dd;
	
	public Controller(DisplayedDocument displayedDocument) {
		this.dd = displayedDocument;
	}
	
	protected static String getMethodDescriptor(Class clazz, String methodName) {
		return clazz.getCanonicalName() + SEPARATOR + methodName;
	}
	
	protected Method getMethod(String methodDescriptor) {
		String className = getClass().getCanonicalName();
		if (!methodDescriptor.startsWith(className)) return null;
		int separator = className.length();
		try {
			return getClass().getMethod(methodDescriptor.substring(separator + 1), METHOD_ARGS);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
}
