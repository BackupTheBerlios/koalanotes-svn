package de.berlios.koalanotes.controllers;

import org.eclipse.swt.widgets.Event;

/**
 * SWT's Listener customised to the KoalaNotes listener/dispatcher/controller framework.  All events
 * in KoalaNotes are picked up by either a KoalaNotes Action or a KoalaNotes Listener, and are then
 * sent to the Dispatcher to be dispatched to a Controller.
 *  
 * @author alison
 */
public class Listener implements org.eclipse.swt.widgets.Listener {
	private Dispatcher dispatcher;
	private String controllerMethodDescriptor;
	
	public Listener(Dispatcher dispatcher, String controllerMethodDescriptor) {
		this.dispatcher = dispatcher;
		this.controllerMethodDescriptor = controllerMethodDescriptor;
	}
	
	public void handleEvent(Event e) {
		dispatcher.invokeControllerMethod(controllerMethodDescriptor, e);
	}
}
