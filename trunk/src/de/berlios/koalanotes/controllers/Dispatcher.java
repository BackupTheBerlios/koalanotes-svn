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
	
	/** Controllers register and deregister themselves. */
	protected void registerController(Controller c) {controllers.put(c.getControllerSignature(), c);}
	
	/** Controllers register and deregister themselves. */
	protected void deregisterController(Controller c) {controllers.remove(c.getControllerSignature());}
	
	public void invokeControllerMethod(String methodDescriptor, Event e) {
		Controller controller = controllers.get(Controller.getControllerSignature(methodDescriptor));
		if (controller == null) {
			throw new KoalaException("Koala Notes could not find controller '"
			                         + Controller.getControllerSignature(methodDescriptor) + "'.");
		}
		controller.invokeMethod(methodDescriptor, e);
	}
}
