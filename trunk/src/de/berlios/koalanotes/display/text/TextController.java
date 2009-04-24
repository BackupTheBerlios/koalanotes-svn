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
package de.berlios.koalanotes.display.text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.FontDialog;

import de.berlios.koalanotes.controllers.INoArgsAction;
import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.display.DisplayedDocument;

public class TextController {
	private MainController mc;
	private DisplayedDocument dd;
	private KoalaStyleManager ksm;
	
	public TextController(MainController mc, DisplayedDocument dd) {
		this.mc = mc;
		this.dd = dd;
		this.ksm = dd.getKoalaStyleManager();
	}
	
	public class ChooseFontAction implements INoArgsAction {
		public void invoke() {
			FontDialog fontDialog = new FontDialog(dd.getShell());
			if (fontDialog.open() != null) {
				ApplyStyleHelper<FontData[]> h = new ApplyStyleHelper<FontData[]>(fontDialog.getFontList()) {
					void applyStyleWorker() {
						ksm.setCurrentStyleFontData(styleInfo);
					}
				};
				h.applyStyle();
			}
		}
	}
	
	public class ChooseForeColorAction implements INoArgsAction {
		public void invoke() {
			ColorDialog colorDialog = new ColorDialog(dd.getShell());
			RGB rgb = colorDialog.open();
			if (rgb != null) {
				ApplyStyleHelper<RGB> h = new ApplyStyleHelper<RGB>(rgb) {
					void applyStyleWorker() {
						ksm.setCurrentStyleForeground(styleInfo);
					}
				};
				h.applyStyle();
			}
		}
	}
	
	public class ChooseBackColorAction implements INoArgsAction {
		public void invoke() {
			ColorDialog colorDialog = new ColorDialog(dd.getShell());
			RGB rgb = colorDialog.open();
			if (rgb != null) {
				ApplyStyleHelper<RGB> h = new ApplyStyleHelper<RGB>(rgb) {
					void applyStyleWorker() {
						ksm.setCurrentStyleBackground(styleInfo);
					}
				};
				h.applyStyle();
			}
		}
	}
	
	public class BoldAction implements INoArgsAction {
		public void invoke() {
			Boolean setBold = ((ksm.getCurrentStyleFontData()[0].getStyle() & SWT.BOLD) == 0);
			ApplyStyleHelper<Boolean> h = new ApplyStyleHelper<Boolean>(setBold) {
				void applyStyleWorker() {
					ksm.setCurrentStyleFontBold(styleInfo);
				}
			};
			h.applyStyle();
		}
	}
	
	public class ItalicAction implements INoArgsAction {
		public void invoke() {
			Boolean setItalic = ((ksm.getCurrentStyleFontData()[0].getStyle() & SWT.ITALIC) == 0);
			ApplyStyleHelper<Boolean> h = new ApplyStyleHelper<Boolean>(setItalic) {
				void applyStyleWorker() {
					ksm.setCurrentStyleFontItalic(styleInfo);
				}
			};
			h.applyStyle();
		}
	}
	
	public class UnderlineAction implements INoArgsAction {
		public void invoke() {
			Boolean setUnderline = !ksm.getCurrentStyleUnderline();
			ApplyStyleHelper<Boolean> h = new ApplyStyleHelper<Boolean>(setUnderline) {
				void applyStyleWorker() {
					ksm.setCurrentStyleUnderline(styleInfo);
				}
			};
			h.applyStyle();
		}
	}
	
	public class ChooseStyleAction implements INoArgsAction {
		private String styleName;
		public ChooseStyleAction(String styleName) {
			this.styleName = styleName;
		}
		public void invoke() {
			KoalaStyledText kst = dd.getTabFolder().getSelectedNoteTab().getKoalaStyledText();
			
			if (styleName != null) { // the styleName is null for the topmost menu item
				ksm.setCurrentStyle(styleName);
			}
			
			StyleRange[] srs = kst.getSelectedStyleRanges();
			if (srs == null) {
				return;
			} else if (srs.length == 0) {
				return;
			}
			
			StyleRange[] newsrs = new StyleRange[srs.length];
			for (int i = 0; i < srs.length; i++) {
				newsrs[i] = (StyleRange) srs[i].clone();
				ksm.applyCurrentStyle(newsrs[i]);
			}
			kst.replaceSelectedStyleRanges(newsrs);
			
			mc.documentUpdatedAndTextSelectionChanged();
		}
	}
	
	private abstract class ApplyStyleHelper<T> {
		T styleInfo;
		
		private ApplyStyleHelper(T styleInfo) {
			this.styleInfo = styleInfo;
		}
		
		abstract void applyStyleWorker();
		
		private void applyStyle() {
			KoalaStyledText kst = dd.getTabFolder().getSelectedNoteTab().getKoalaStyledText();
			String currentStyleName = ksm.getCurrentStyleName();
			
			StyleRange[] srs = kst.getSelectedStyleRanges();
			if (srs == null) {
				applyStyleWorker();
				return;
			} else if (srs.length == 0) {
				applyStyleWorker();
				return;
			}
			
			StyleRange[] newsrs = new StyleRange[srs.length];
			for (int i = 0; i < srs.length; i++) {
				newsrs[i] = (StyleRange) srs[i].clone();
				ksm.setCurrentStyle(newsrs[i]);
				applyStyleWorker();
				ksm.applyCurrentStyle(newsrs[i]);
			}
			kst.replaceSelectedStyleRanges(newsrs);
			
			ksm.setCurrentStyle(currentStyleName);
			applyStyleWorker();
			
			mc.documentUpdatedAndTextSelectionChanged();
		}
	}
}
