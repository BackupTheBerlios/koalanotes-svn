package de.berlios.koalanotes.controllers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * All events come through the Dispatcher to be dispatched to the appropriate controller.
 * 
 * @author alison
 */
public class Dispatcher {
	private Map<String, Controller> controllers;
	
	/** KoalaNotes should have only one Dispatcher instance at a time. */
	public Dispatcher() {
		controllers = new HashMap<String, Controller>();
	}
	
	/** Controllers register and deregister themselves. */
	protected void registerController(Controller c) {controllers.put(c.getControllerSignature(), c);}
	
	/** Controllers register and deregister themselves. */
	protected void deregisterController(Controller c) {controllers.remove(c.getControllerSignature());}
	
	/**
	 * All events come through this method to be dispatched to the appropriate controller.  The
	 * events will most likely have come via a KoalaNotes Action or a KoalaNotes Listener.  If the
	 * event came via an Action, the event argument will be null; if it came via a Listener, the
	 * event argument will be populated.  KoalaNotes Controllers also occasionally call this method.
	 */
	public void invokeControllerMethod(String methodDescriptor, Event event) {
		Controller controller = controllers.get(Controller.getControllerSignature(methodDescriptor));
		if (controller == null) {
			throw new KoalaException("Koala Notes could not find controller '"
			                         + Controller.getControllerSignature(methodDescriptor) + "'.");
		}
		controller.invokeMethod(methodDescriptor, event);
	}
}
