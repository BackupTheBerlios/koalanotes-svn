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
