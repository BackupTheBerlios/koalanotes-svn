package de.berlios.koalanotes.display;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.berlios.koalanotes.KoalaNotes;

public class ImageRegistry {
	
	public static final String IMAGE_KOALA_SMALL = "IMAGE_KOALA_SMALL";
	public static final String IMAGE_KOALA_BIG = "IMAGE_KOALA_BIG";
	
	public static final String ACTION_ICON_FILE_SAVE = "ACTION_ICON_FILE_SAVE";
	
	private org.eclipse.jface.resource.ImageRegistry jfaceImageRegistry;
	
	public ImageRegistry(Display display) {
		jfaceImageRegistry = new org.eclipse.jface.resource.ImageRegistry(display);
		put(IMAGE_KOALA_SMALL, "../../../koala16.png");
		put(IMAGE_KOALA_BIG, "../../../koala200.png");
		
		put(ACTION_ICON_FILE_SAVE, "../../../save.png");
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
