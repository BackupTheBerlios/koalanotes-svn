package de.berlios.koalanotes.controllers;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * JFace's Action customised to the KoalaNotes listener/dispatcher/controller framework.  All events
 * in KoalaNotes are picked up by either a KoalaNotes Action or a KoalaNotes Listener, and are then
 * sent to the Dispatcher to be dispatched to a Controller.
 * 
 * @author alison
 */
public class Action extends org.eclipse.jface.action.Action {
	private Dispatcher dispatcher;
	private String controllerMethodDescriptor;
	
	/** See org.eclipse.jface.action.Action. */
	protected Action(Dispatcher dispatcher, String controllerMethod) {
		super();
		init(dispatcher, controllerMethod);
	}
    
	/** See org.eclipse.jface.action.Action. */
	protected Action(Dispatcher dispatcher, String controllerMethod, String text) {
		super(text);
		init(dispatcher, controllerMethod);
	}
	
	/** See org.eclipse.jface.action.Action. */
	protected Action(Dispatcher dispatcher, String controllerMethod, String text, ImageDescriptor image) {
		super(text, image);
		init(dispatcher, controllerMethod);
	}
	
	/** See org.eclipse.jface.action.Action. */
	protected Action(Dispatcher dispatcher, String controllerMethod, String text, int style) {
		super(text, style);
		init(dispatcher, controllerMethod);
	}
	
	private void init(Dispatcher dispatcher, String controllerMethod) {
		this.dispatcher = dispatcher;
		this.controllerMethodDescriptor = controllerMethod;
	}
	
	@Override
	public void run() {
		dispatcher.invokeControllerMethod(controllerMethodDescriptor, null);
	}
}
