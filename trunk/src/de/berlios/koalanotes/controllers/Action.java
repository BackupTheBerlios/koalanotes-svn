/**
 * Copyright (C) 2008 Alison Farlie
 * 
 * This file is part of KoalaNotes.
 * 
 * KoalaNotes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * KoalaNotes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with KoalaNotes.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
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
	public Action(Dispatcher dispatcher, String controllerMethod) {
		super();
		init(dispatcher, controllerMethod);
	}
    
	/** See org.eclipse.jface.action.Action. */
	public Action(Dispatcher dispatcher, String controllerMethod, String text) {
		super(text);
		init(dispatcher, controllerMethod);
	}
	
	/** See org.eclipse.jface.action.Action. */
	public Action(Dispatcher dispatcher, String controllerMethod, String text, ImageDescriptor image) {
		super(text, image);
		init(dispatcher, controllerMethod);
	}
	
	/** See org.eclipse.jface.action.Action. */
	public Action(Dispatcher dispatcher, String controllerMethod, String text, int style) {
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
