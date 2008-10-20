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
 * A JFace Action wrapped around a KoalaNotes INoArgsAction, so the action can be placed in the
 * menu and toolbar managers, which also use JFace.
 * 
 * @author alison
 */
public class Action extends org.eclipse.jface.action.Action {
	private INoArgsAction action;
	
	/** See org.eclipse.jface.action.Action. */
	public Action(INoArgsAction action) {
		super();
		init(action);
	}
    
	/** See org.eclipse.jface.action.Action. */
	public Action(INoArgsAction action, String text) {
		super(text);
		init(action);
	}
	
	/** See org.eclipse.jface.action.Action. */
	public Action(INoArgsAction action, String text, ImageDescriptor image) {
		super(text, image);
		init(action);
	}
	
	/** See org.eclipse.jface.action.Action. */
	public Action(INoArgsAction action, String text, int style) {
		super(text, style);
		init(action);
	}
	
	private void init(INoArgsAction action) {
		this.action = action;
	}
	
	@Override
	public void run() {
		action.invoke();
	}
}
