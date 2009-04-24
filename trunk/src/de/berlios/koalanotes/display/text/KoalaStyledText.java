package de.berlios.koalanotes.display.text;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import de.berlios.koalanotes.controllers.MainController;
import de.berlios.koalanotes.controllers.MainController.NoteModifiedAction;

/**
 * A wrapper around org.eclipse.jface.TextViewer.
 */
public class KoalaStyledText implements ExtendedModifyListener, ISelectionChangedListener {
	KoalaStyleManager koalaStyleManager;

	/** KoalaStyledText is a wrapper for this JFace TextViewer widget. */
	private TextViewer textViewer;

	/** The NoteModifiedAction if it is currently registered. */
	private NoteModifiedAction noteModifiedAction;

	public KoalaStyledText(CTabFolder tabFolder, CTabItem tabItem, String text,
	                       KoalaStyleManager koalaStyleManager,
	                       final MainController.TabSelectedAction tabSelectedAction,
						   final MainController.TabDeselectedAction tabDeselectedAction,
						   final MainController.TextSelectionChangedAction textSelectionChangedAction) {
		this.koalaStyleManager = koalaStyleManager;
		
		textViewer = new TextViewer(tabFolder, SWT.WRAP | SWT.V_SCROLL);
		textViewer.setDocument(new Document(text));

		tabItem.setControl(textViewer.getControl());

		textViewer.getTextWidget().addExtendedModifyListener(this);
		textViewer.addPostSelectionChangedListener(this);
		textViewer.getTextWidget().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				tabSelectedAction.invoke();
			}

			public void focusLost(FocusEvent event) {
				tabDeselectedAction.invoke();
			}
		});
		textViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				textSelectionChangedAction.invoke();
			}
		});
	}

	public void dispose() {
		textViewer.getTextWidget().dispose();
	}
	
	
	
	//
	// Getters
	//

	public String getText() {
		return textViewer.getDocument().get();
	}

	public boolean isFocusControl() {
		return textViewer.getTextWidget().isFocusControl();
	}
	
	public StyleRange[] getSelectedStyleRanges() {
		int start = textViewer.getSelectedRange().x;
		int length = textViewer.getSelectedRange().y;
		return textViewer.getTextWidget().getStyleRanges(start, length);
	}

	
	
	//
	// Setters
	//
	
	public void replaceSelectedStyleRanges(StyleRange[] srs) {
		int start = textViewer.getSelectedRange().x;
		int length = textViewer.getSelectedRange().y;
		textViewer.getTextWidget().replaceStyleRanges(start, length, srs);
	}

	
	
	//
	// Listener Methods
	//

	/**
	 * Implements ISelectionChangedListener so when the user navigates to a new location in the
	 * text we can tell the style manager to modify the current style.
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		TextSelection ts = (TextSelection) textViewer.getSelection();
		String entireText = textViewer.getTextWidget().getText();
		int start = ts.getOffset();
		int length = ts.getLength();
		
		// Figure out the current style.
		if (entireText.length() == 0) {

			// If there is no text in the widget, use a default style.
			koalaStyleManager.setCurrentStyleDefault();

		} else if (length > 0) {
			
			// If this is a selection of a block of text, use the most popular style in that block.
			StyleRange[] srs = textViewer.getTextWidget().getStyleRanges(ts.getOffset(), ts.getLength());
			koalaStyleManager.setCurrentStyle(srs);

		} else {

			// Otherwise this is a zero length selection so copy the style of adjacent text.
			boolean usePrevious;
			if (start == 0) {
				// If at the start of the text, use the subsequent style.
				usePrevious = false;
			} else if (start + length == entireText.length()) {
				// If at the end of the text, use the previous style.
				usePrevious = true;
			} else if (!entireText.substring(start - 1, start).trim().isEmpty()) {
				// If the previous character is non-white-space, use its style.
				usePrevious = true;
			} else if (!entireText.substring(start + length, start + length + 1).trim().isEmpty()) {
				// If the subsequent character is non-white-space, use its style.
				usePrevious = false;
			} else {
				// Otherwise just use the previous character's style.
				usePrevious = true;
			}
			if (usePrevious) {
				koalaStyleManager.setCurrentStyle(textViewer.getTextWidget().getStyleRangeAtOffset(start - 1));
			} else {
				koalaStyleManager.setCurrentStyle(textViewer.getTextWidget().getStyleRangeAtOffset(start + length));
			}
		}
	}

	/**
	 * Implements ExtendedModifyListener so we can style text as the user types.
	 */
	public void modifyText(ExtendedModifyEvent event) {
		
		// Assign the new text an appropriate style.
		StyleRange sr = new StyleRange();
		sr.start = event.start;
		sr.length = event.length;
		koalaStyleManager.applyCurrentStyle(sr);
		textViewer.getTextWidget().setStyleRange(sr);
		
		// If a NoteModifiedAction is registered, invoke it.
		if (noteModifiedAction != null) {
			noteModifiedAction.invoke();
		}
	}

	public void addNoteModifiedAction(final MainController.NoteModifiedAction action) {
		noteModifiedAction = action;
	}
	public void removeNoteModifiedAction() {
		noteModifiedAction = null;
	}
}
