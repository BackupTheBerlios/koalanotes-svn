package de.berlios.koalanotes.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.exceptions.KoalaException;

public abstract class Controller {
	
	private static final Class[] METHOD_ARGS = new Class[] {Event.class};
	private static final String SEPARATOR = ".";
	
	private Dispatcher d;
	
	public Controller(Dispatcher d) {
		this.d = d;
		d.registerController(this);
	}
	
	protected void deregister() {
		d.deregisterController(this);
	}
	
	protected static String getMethodDescriptor(Class controllerClass, String methodName) {
		return controllerClass.getCanonicalName() + SEPARATOR + methodName;
	}
	
	protected static String getControllerSignature(String methodDescriptor) {
		return methodDescriptor.substring(0, methodDescriptor.lastIndexOf(SEPARATOR));
	}
	
	protected String getControllerSignature() {
		return getClass().getCanonicalName();
	}
	
	protected void invokeMethod(String methodDescriptor, Event e) {
		
		// Get the method.
		Method method = null;
		String controllerSignature = getControllerSignature();
		if (!methodDescriptor.startsWith(controllerSignature)) {
			throw new KoalaException("Wrong controller nominated to execute '" + methodDescriptor + "'.");
		}
		int separator = controllerSignature.length();
		try {
			method = getClass().getMethod(methodDescriptor.substring(separator + 1), METHOD_ARGS);
		} catch (NoSuchMethodException ex) {
			throw new KoalaException("Koala Notes could not find method '" + methodDescriptor + "'.");
		}
		
		// Invoke the method.
		try {
			method.invoke(this, new Object[] {e});
		} catch (IllegalAccessException iaex) {
			throw new KoalaException("Koala Notes could not access method '" + methodDescriptor + "'.", iaex);
		} catch (InvocationTargetException itex) {
			if (itex.getCause() instanceof RuntimeException) {
				throw (RuntimeException) itex.getCause();
			} else throw new KoalaException(itex.getCause());
		}
	}
}
