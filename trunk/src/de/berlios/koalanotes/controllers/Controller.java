package de.berlios.koalanotes.controllers;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Event;

public abstract class Controller {
	
	private static final Class[] METHOD_ARGS = new Class[] {Event.class};
	private static final String SEPARATOR = ".";
	
	protected static String getMethodDescriptor(Class controllerClass, String methodName) {
		return controllerClass.getCanonicalName() + SEPARATOR + methodName;
	}
	
	protected static String getControllerSignature(String methodDescriptor) {
		return methodDescriptor.substring(0, methodDescriptor.lastIndexOf(SEPARATOR));
	}
	
	protected String getControllerSignature() {
		return getClass().getCanonicalName();
	}
	
	protected Method getMethod(String methodDescriptor) {
		String controllerSignature = getControllerSignature();
		if (!methodDescriptor.startsWith(controllerSignature)) return null;
		int separator = controllerSignature.length();
		try {
			return getClass().getMethod(methodDescriptor.substring(separator + 1), METHOD_ARGS);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
}
