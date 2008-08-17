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
package de.berlios.koalanotes.display;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.berlios.koalanotes.KoalaNotes;

public class ImageRegistry {
	
	public static final String IMAGE_KOALA_SMALL = "IMAGE_KOALA_SMALL";
	public static final String IMAGE_KOALA_BIG = "IMAGE_KOALA_BIG";
	
	public static final String ACTION_ICON_FILE_SAVE = "ACTION_ICON_FILE_SAVE";
	public static final String ACTION_ICON_NOTE_NEW_CHILD = "ACTION_ICON_NOTE_NEW_CHILD";
	public static final String ACTION_ICON_NOTE_NEW_SIBLING = "ACTION_ICON_NOTE_NEW_SIBLING";
	
	private org.eclipse.jface.resource.ImageRegistry jfaceImageRegistry;
	
	public ImageRegistry(Display display) {
		jfaceImageRegistry = new org.eclipse.jface.resource.ImageRegistry(display);
		put(IMAGE_KOALA_SMALL, "/images/koala16.png");
		put(IMAGE_KOALA_BIG, "/images/koala200.png");
		
		put(ACTION_ICON_FILE_SAVE, "/images/file_save.png");
		put(ACTION_ICON_NOTE_NEW_CHILD, "/images/notes_new_child.png");
		put(ACTION_ICON_NOTE_NEW_SIBLING, "/images/notes_new_sibling.png");
	}
	
	private void put(String key, String filename) {
		ImageDescriptor d = ImageDescriptor.createFromFile(KoalaNotes.class, filename);
		jfaceImageRegistry.put(key, d);
	}
	
	public Image get(String key) {
		Image image = jfaceImageRegistry.get(key);
		if (image == null) {
			image = jfaceImageRegistry.getDescriptor(key).createImage();
			jfaceImageRegistry.put(key, image);
		}
		return image;
	}
	
	public ImageDescriptor getDescriptor(String key) {
		jfaceImageRegistry.getDescriptor(key).createImage();
		return jfaceImageRegistry.getDescriptor(key);
	}
}
