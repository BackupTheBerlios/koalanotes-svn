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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Event;

import de.berlios.koalanotes.display.DisplayedDocument;
import de.berlios.koalanotes.exceptions.KoalaException;

public abstract class Controller {
	
	private static final String SEPARATOR = ".";
	
	protected Dispatcher d;
	
	public Controller(Dispatcher d) {
		this.d = d;
		d.registerController(this);
	}
	
	protected void deregister() {
		d.deregisterController(this);
	}
	
	protected static String getMethodDescriptor(Class<? extends Controller> controllerClass, String methodName) {
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
			method = getClass().getMethod(methodDescriptor.substring(separator + 1), Event.class);
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
	
	protected void contextChanged(Event e) {
		d.invokeControllerMethod(MainController.CONTEXT_CHANGED, e);
	}
	
	protected void documentUpdated(DisplayedDocument dd, Event e) {
		if (!dd.isModified()) {
			dd.setModified(true);
			d.invokeControllerMethod(MainController.CONTEXT_CHANGED, e);
		}
	}
	
	protected void documentUpdatedAndContextChanged(DisplayedDocument dd, Event e) {
		if (!dd.isModified()) {
			dd.setModified(true);
		}
		d.invokeControllerMethod(MainController.CONTEXT_CHANGED, e);
	}
}
