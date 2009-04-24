package de.berlios.koalanotes.display.text;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import de.berlios.koalanotes.display.KoalaResources;

/**
 * Stores a set of the styles used in the document and knows the current style.
 */
public class KoalaStyleManager {
	private static final String DEFAULT_STYLE_NAME = "Default";
	
	/** A map containing the styles used in the document, names may be specified by the user. */
	private TreeMap<String, NamedStyle> namedStyles;
	
	/** The current style to apply to new text entered. */
	private NamedStyle currentStyle;
	
	public KoalaStyleManager() {
		namedStyles = new TreeMap<String, NamedStyle>();
		
		NamedStyle defaultStyle = new NamedStyle(DEFAULT_STYLE_NAME,
		                                         KoalaResources.getDefaultFont().getFontData(),
		                                         false, null, null);
		
		namedStyles.put(DEFAULT_STYLE_NAME, defaultStyle);
		currentStyle = defaultStyle;
	}
	
	public Set<String> getStyleNames() {
		return namedStyles.keySet();
	}
	
	public String getCurrentStyleName() {
		return currentStyle.name;
	}
	
	public FontData[] getCurrentStyleFontData() {
		return currentStyle.fontData;
	}
	
	public boolean getCurrentStyleUnderline() {
		return currentStyle.isUnderline;
	}
	
	public RGB getCurrentStyleForeground() {
		return currentStyle.foreground;
	}
	
	public RGB getCurrentStyleBackground() {
		return currentStyle.background;
	}
	
	public void applyCurrentStyle(StyleRange sr) {
		currentStyle.applyStyle(sr);
	}
	
	public void setCurrentStyle(String styleName) {
		currentStyle = namedStyles.get(styleName);
	}
	
	public void setCurrentStyleDefault() {
		setCurrentStyle(DEFAULT_STYLE_NAME);
	}
	
	public void setCurrentStyle(StyleRange sr) {
		setCurrentStyle(new NamedStyle(sr));
	}
	
	public void setCurrentStyle(StyleRange[] srs) {
		if (srs.length == 1) {
			setCurrentStyle(srs[0]);
			return;
		}
		
		// Tally the votes for most popular style.
		Tally<String> tallyFontName = new Tally<String>();
		Tally<Integer> tallyHeight = new Tally<Integer>();
		Tally<Boolean> tallyBold = new Tally<Boolean>();
		Tally<Boolean> tallyItalic = new Tally<Boolean>();
		Tally<Boolean> tallyUnderline = new Tally<Boolean>();
		Tally<RGB> tallyForeground = new Tally<RGB>();
		Tally<RGB> tallyBackground = new Tally<RGB>();
		for (StyleRange sr : srs) {
			tallyFontName.add(sr.font.getFontData()[0].getName(), sr.length);
			tallyHeight.add(sr.font.getFontData()[0].getHeight(), sr.length);
			tallyBold.add((sr.font.getFontData()[0].getStyle() & SWT.BOLD) == SWT.BOLD, sr.length);
			tallyItalic.add((sr.font.getFontData()[0].getStyle() & SWT.ITALIC) == SWT.ITALIC, sr.length);
			tallyUnderline.add(sr.underline, sr.length);
			if (sr.foreground != null) {
				tallyForeground.add(sr.foreground.getRGB(), sr.length);
			} else {
				tallyForeground.add(null, sr.length);
			}
			if (sr.background != null) {
				tallyBackground.add(sr.background.getRGB(), sr.length);
			} else {
				tallyBackground.add(null, sr.length);
			}
		}

		// Use the most popular style.
		FontData fd = new FontData();
		fd.setName(tallyFontName.getWinner());
		fd.setHeight(tallyHeight.getWinner());
		if (tallyBold.getWinner()) {
			fd.setStyle(SWT.BOLD);
		}
		if (tallyItalic.getWinner()) {
			fd.setStyle(fd.getStyle() | SWT.ITALIC);
		}
		NamedStyle mostPopularStyle = new NamedStyle(new FontData[] {fd},
		                                             tallyUnderline.getWinner(),
		                                             tallyForeground.getWinner(),
		                                             tallyBackground.getWinner());
		setCurrentStyle(mostPopularStyle);
	}
	
	public void setCurrentStyleFontData(FontData[] fontData) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		copyStyle.fontData = fontData;
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleFontHeight(int fontHeight) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		copyStyle.fontData[0].height = fontHeight;
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleFontBold(boolean fontBold) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		if (((copyStyle.fontData[0].style & SWT.BOLD) == SWT.BOLD) != fontBold) {
			copyStyle.fontData[0].style = copyStyle.fontData[0].style ^ SWT.BOLD;
		}
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleFontItalic(boolean fontItalic) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		if (((copyStyle.fontData[0].style & SWT.ITALIC) == SWT.ITALIC) != fontItalic) {
			copyStyle.fontData[0].style = copyStyle.fontData[0].style ^ SWT.ITALIC;
		}
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleUnderline(boolean underline) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		copyStyle.isUnderline = underline;
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleForeground(RGB foreground) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		copyStyle.foreground = foreground;
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	public void setCurrentStyleBackground(RGB background) {
		NamedStyle copyStyle = new NamedStyle(currentStyle);
		copyStyle.background = background;
		copyStyle.generateName();
		setCurrentStyle(copyStyle);
	}
	
	private void setCurrentStyle(NamedStyle newCurrentStyle) {
		
		// See if this style is already managed, if it is then just use the existing style and exit
		// this procedure.
		for (NamedStyle ns : namedStyles.values()) {
			if (ns.equalsIgnoreName(newCurrentStyle)) {
				currentStyle = ns;
				return;
			}
		}
		
		// Check if there is already a style with this name; if so we need to think of a new name.
		int i = 0;
		if (namedStyles.containsKey(newCurrentStyle.name)) {
			i++;
			while (namedStyles.containsKey(newCurrentStyle.name + " " + String.valueOf(i))) {
				i++;
			}
		}
		if (i > 0) {
			newCurrentStyle.name = newCurrentStyle.name + " " + String.valueOf(i);
		}
		
		// Set the new current style.
		currentStyle = newCurrentStyle;
		
		// Add the current style to the set of managed styles.
		namedStyles.put(newCurrentStyle.name, newCurrentStyle);
	}
	
	/**
	 * Each and every style that has been applied to text in the current document will be
	 * represented by a NamedStyle instance stored in the KoalaStyleManager.  The name of the style
	 * may be defined by the user through the style manager interface, or it may be generated based
	 * on the content of the style.  The style is a primary style if it has been specifically
	 * defined and named by the user through the style manager dialog, or if it is the initial
	 * default style.  Secondary styles are created as variations of primary styles, for example if
	 * the current primary style is called "A" and the user presses the Bold button a secondary
	 * style will be created and automatically assigned a name of "A Bold".  A secondary style that
	 * diverges a lot from its base primary style doesn't get named after the base style but instead
	 * is assigned a name to reflect its content.
	 */
	private class NamedStyle {
		
		/** The name of this style, user defined or auto-generated. */
		private String name;
		
		/** Whether this is a primary style. */
		private boolean isPrimaryStyle;
		
		/**
		 * If this is a secondary style that does not diverge too much from its base style, the
		 * style it is based on is stored in this field.
		 */
		private NamedStyle base;
		
		/** Styling information. */
		private FontData[] fontData;
		
		/** Styling information. */
		private boolean isUnderline;
		
		/** Styling information. */
		private RGB foreground;
		
		/** Styling information. */
		private RGB background;
		
		/** Create a primary style with the given name and attributes. */
		private NamedStyle(String name, FontData[] fontData, boolean isUnderline, RGB foreground, RGB background) {
			this.fontData = fontData;
			this.isUnderline = isUnderline;
			this.foreground = foreground;
			this.background = background;
			
			this.isPrimaryStyle = true;
			this.base = null;
			this.name = name;
		}
		
		/** Create a secondary style with the given attributes. */
		private NamedStyle(FontData[] fontData, boolean isUnderline, RGB foreground, RGB background) {
			this.fontData = fontData;
			this.isUnderline = isUnderline;
			this.foreground = foreground;
			this.background = background;
			
			this.isPrimaryStyle = false;
			this.base = null;
			generateName();
		}
		
		/** Create a secondary style with the given attributes. */
		private NamedStyle(StyleRange sr) {
			this.fontData = sr.font.getFontData();
			this.isUnderline = sr.underline;
			if (sr.foreground != null) {
				this.foreground = sr.foreground.getRGB();
			}
			if (sr.background != null) {
				this.background = sr.background.getRGB();
			}
			
			this.isPrimaryStyle = false;
			this.base = null;
			generateName();
		}
		
		/**
		 * Create a secondary style with attributes copied from the given style.  If the given style
		 * is a primary style use it as a base for naming this style, or if the given style itself
		 * has a base then use that.
		 */
		private NamedStyle(NamedStyle copyFrom) {
			fontData = new FontData[copyFrom.fontData.length];
			for (int i = 0; i < copyFrom.fontData.length; i++) {
				fontData[i] = new FontData();
				fontData[i].name = copyFrom.fontData[i].name;
				fontData[i].height = copyFrom.fontData[i].height;
				fontData[i].style = copyFrom.fontData[i].style;
			}
			isUnderline = copyFrom.isUnderline;
			foreground = copyFrom.foreground;
			background = copyFrom.background;
			
			name = copyFrom.name;
			isPrimaryStyle = false;
			if (copyFrom.isPrimaryStyle) {
				base = copyFrom;
			} else if (copyFrom.base != null) {
				base = copyFrom.base;
			}
			generateName();
		}
		
		private void applyStyle(StyleRange sr) {
			sr.font = KoalaResources.getFont(fontData);
			sr.underline = isUnderline;
			if (foreground == null) {
				sr.foreground = null;
			} else {
				sr.foreground = KoalaResources.getColor(foreground);
			}
			if (background == null) {
				sr.background = null;
			} else {
				sr.background = KoalaResources.getColor(background);
			}
		}
		
		/**
		 * Checks equality with the given style, looking at every attribute of the NamedStyle except
		 * the name itself, because we want to prevent a duplicate style from being entered into the
		 * set even if the user gives it a different name.
		 */
		public boolean equalsIgnoreName(NamedStyle ns) {
			if (fontData.length != ns.fontData.length) return false;
			for (int i = 0; i < fontData.length; i++) {
				FontData fd = fontData[i];
				FontData nsfd = ns.fontData[i];
				if (!fd.getName().equals(nsfd.getName())) return false;
				if (fd.getHeight() != nsfd.getHeight()) return false;
				if (fd.getStyle() != nsfd.getStyle()) return false;
			}
			if (isUnderline != ns.isUnderline) return false;
			if (!equals(foreground, ns.foreground)) return false;
			if (!equals(background, ns.background)) return false;
			
			return true;
		}
		
		private boolean equals(RGB a, RGB b) {
			if ((a == null) && (b == null)) {
				return true;
			} else if ((a == null) != (b == null)) {
				return false;
			} else {
				if (a.red != b.red) return false;
				if (a.green != b.green) return false;
				if (a.blue != b.blue) return false;
				return true;
			}
		}
		
		/**
		 * Test to see if this style is similar enough to base, if not, set the base field to null.
		 */
		private void assertSimilarToBase() {
			if (base != null) {
				if (fontData.length != base.fontData.length) {
					base = null;
					return;
				}
				for (int i = 0; i < fontData.length; i++) {
					FontData fd = fontData[i];
					FontData basefd = base.fontData[i];
					if (!fd.getName().equals(basefd.getName())) {
						base = null;
						return;
					}
				}
			}
		}
		
		private void generateName() {
			assertSimilarToBase();
			
			if (base != null) {
				StringBuilder sb = new StringBuilder(base.name);
				sb.append(" ");
				for (int i = 0; i < fontData.length && i < base.fontData.length; i++) {
					FontData fd = fontData[i];
					FontData basefd = base.fontData[i];
					if (fd.getHeight() != basefd.getHeight()) {
						sb.append(fd.getHeight());
						sb.append("pt ");
					}
					if ((fd.getStyle() & SWT.BOLD) != (basefd.getStyle() & SWT.BOLD)) {
						if ((fd.getStyle() & SWT.BOLD) == 0) {
							sb.append("Not ");
						}
						sb.append("Bold ");
					}
					if ((fd.getStyle() & SWT.ITALIC) != (basefd.getStyle() & SWT.ITALIC)) {
						if ((fd.getStyle() & SWT.ITALIC) == 0) {
							sb.append("Not ");
						}
						sb.append("Italic ");
					}
				}
				if (isUnderline != base.isUnderline) {
					if (!isUnderline) {
						sb.append("Not ");
					}
					sb.append("Underline ");
				}
				
				name = sb.substring(0, sb.length() - 1);
				
			} else {
				StringBuilder sb = new StringBuilder();
				for (FontData fd : fontData) {
					sb.append(fd.getName());
					sb.append(" ");
					sb.append(fd.getHeight());
					sb.append("pt ");
					if ((fd.getStyle() & SWT.BOLD) == SWT.BOLD) {
						sb.append("Bold ");
					}
					if ((fd.getStyle() & SWT.ITALIC) == SWT.ITALIC) {
						sb.append("Italic ");
					}
				}
				if (isUnderline) {
					sb.append("Underline ");
				}
				
				name = sb.substring(0, sb.length() - 1);
			}
		}
	}
	
	/**
	 * Keep a tally of the number of votes against each object that is entered, in order to
	 * determine the object with the most votes.
	 */
	private class Tally<T> {
		private HashMap<T, Integer> map;
		private Tally() {
			map = new HashMap<T, Integer>();
		}
		private void add(T object, int votes) {
			int existingValue = 0;
			if (map.get(object) != null) {
				existingValue = map.get(object);
			}
			map.put(object, existingValue + votes);
		}
		private T getWinner() {
			int maxVotes = 0;
			T winner = null;
			for (Entry<T, Integer> entry : map.entrySet()) {
				if (entry.getValue() > maxVotes) {
					maxVotes = entry.getValue();
					winner = entry.getKey();
				}
			}
			return winner;
		}
	}
}
