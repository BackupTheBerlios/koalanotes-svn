package de.berlios.koalanotes.controllers;

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
		Controller controller = controllers.get(Controller.getControllerSignature(methodDescriptor));
		if (controller == null) {
			throw new KoalaException("Koala Notes could not find controller '"
			                         + Controller.getControllerSignature(methodDescriptor) + "'.");
		}
		controller.invokeMethod(methodDescriptor, e);
	}
}
