package de.berlios.koalanotes.display;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class KoalaResources {
	public static Font getDefaultFont() {
		return JFaceResources.getDefaultFont();
	}
	
	public static Font getFont(FontData[] fd) {
		FontRegistry fr = JFaceResources.getFontRegistry();
		String fontKey = "";
		for (FontData fdItem : fd) {
			fontKey = fontKey + fdItem.toString() + "|";
		}
		if (!fr.hasValueFor(fontKey)) {
			fr.put(fontKey, fd);
		}
		return fr.get(fontKey);
	}
	
	public static Color getColor(RGB rgb) {
		ColorRegistry cr = JFaceResources.getColorRegistry();
		String colorKey = rgb.toString();
		if (!cr.hasValueFor(colorKey)) {
			cr.put(colorKey, rgb);
		}
		return cr.get(colorKey);
	}
}
