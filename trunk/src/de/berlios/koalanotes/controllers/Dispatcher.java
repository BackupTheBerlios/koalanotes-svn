package de.berlios.koalanotes.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.exceptions.KoalaException;

public class Dispatcher {
	private Map<String, Controller> controllers;
	public Dispatcher() {
		controllers = new HashMap<String, Controller>();
	}
	
	public void registerController(Controller c) {controllers.put(c.getControllerSignature(), c);}
	public void deregisterController(String controllerSignature) {controllers.remove(controllerSignature);}
	
	public void invokeControllerMethod(String methodDescriptor, Event e) {
		
		// Get the controller and method.
		Controller controller = controllers.get(Controller.getControllerSignature(methodDescriptor));
		if (controller == null) {
			throw new KoalaException("Koala Notes could not find controller '"
			                         + Controller.getControllerSignature(methodDescriptor) + "'.");
		}
		Method method = controller.getMethod(methodDescriptor);
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
