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
package de.berlios.koalanotes.persistence;

import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import de.berlios.koalanotes.KoalaNotes;
import de.berlios.koalanotes.data.DocumentViewSettings;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * XMLSerializer provides helper methods for serializing KoalaNotes data to and from XML.
 */
public class XMLSerializer {
	
	// Root element and its attributes.
	private static final String ROOT = "koalanotesdocument";
	private static final String ROOT_VERSION = "version";
	
	// The two possible subelements of Root.
	private static final String VIEWSETTINGS = "viewsettings";
	private static final String NOTES = "notes";
	
	// View Settings subelement Tab and its attributes.
	private static final String TAB = "tab";
	private static final String TAB_SELECTED = "selected";
	
	// Note Ref element and its attributes.  A Note Ref is how we reference a Note in XML, similar
	// to an absolute file path, it starts at a root note and lists all ancestors down to the Note.
	private static final String NOTEREF = "noteref";
	
	// Note Path subelement Note Path Member and its attributes.
	private static final String NOTEREFMEMBER = "member";
	private static final String NOTEREFMEMBER_INDEX = "index";
	private static final String NOTEREFMEMBER_NAME = "name";
	
	// Note element and its attributes.
	private static final String NOTE = "note";
	private static final String NOTE_NAME = "name";
	private static final String NOTE_TEXT = "text";
	
	/**
	 * Populate the given KoalaNotes document with settings and notes from the given XML document.
	 * 
	 * @throws KoalaException if the KoalaNotes version number in the XML document is inconsistent
	 * with that of the running application, or if the format of the document is incorrect.
	 */
	public static void loadKoalaDocumentFromXML(de.berlios.koalanotes.data.Document koalaDocument,
	                                            Document jdomDocument) {
		
		// Get the root element and check the KoalaNotes version.
		Element rootElement = jdomDocument.getRootElement();
		checkDocumentVersion(rootElement);
		
		// Get the notes element and load the Notes.
		Element notesElement = getMandatoryElement(rootElement, NOTES);
		for (Object el : notesElement.getChildren()) {
			loadNoteFromElement((Element) el, koalaDocument);
		}
		
		// Get the view settings element and if it exists load the DocumentViewSettings.
		Element viewSettingsElement = rootElement.getChild(VIEWSETTINGS);
		if (viewSettingsElement != null) {
			
			// Settings for which notes are displayed in tabs and which tab is selected.
			List<Note> notesOpenInTabs = new LinkedList<Note>();
			int selectedTab = -1;
			int index = 0;
			for (Object o : viewSettingsElement.getChildren()) {
				Element el = (Element) o;
				
				// Tab element.
				checkElementName(el, TAB);
				
				// Resolve the note reference to find the Note loaded in this tab.
				Element noteRef = getMandatoryElement(el, NOTEREF);
				Note note = resolveNoteRef(noteRef, koalaDocument);
				if (note != null) {
					notesOpenInTabs.add(note);
				}
				
				// Check the Tab Selected attribute to see if this tab is the one selected.
				String isSelected = getNonMandatoryAttributeValue(el, TAB_SELECTED,
				                                                  Boolean.toString(false));
				if (Boolean.parseBoolean(isSelected)) {
					selectedTab = index;
				}
				index++;
			}
			
			DocumentViewSettings viewSettings = new DocumentViewSettings();
			viewSettings.setNotesOpenInTabs(notesOpenInTabs);
			viewSettings.setSelectedTab(selectedTab);
			koalaDocument.setViewSettings(viewSettings);
		}
	}
	
	/**
	 * From the given KoalaNotes Document create an XML Document and return it.
	 */
	public static Document saveKoalaDocumentToXML(de.berlios.koalanotes.data.Document koalaDocument) {
		
		// Create root element.
		Element rootElement = new Element(ROOT);
		rootElement.setAttribute(ROOT_VERSION, KoalaNotes.KOALA_NOTES_VERSION);
		
		// Save view settings.
		Element viewSettingsElement = new Element(VIEWSETTINGS);
		rootElement.addContent(viewSettingsElement);
		int index = 0;
		for (Note noteOpenInTab : koalaDocument.getViewSettings().getNotesOpenInTabs()) {
			Element tabElement = new Element(TAB);
			viewSettingsElement.addContent(tabElement);
			if (index == koalaDocument.getViewSettings().getSelectedTab()) {
				tabElement.setAttribute(TAB_SELECTED, Boolean.toString(true));
			}
			index++;
			tabElement.addContent(createNoteRef(noteOpenInTab));
		}
		
		// Save notes.
		rootElement.addContent(saveNotesToElement(koalaDocument.getNotes()));
		
		return new Document(rootElement);
	}
	
	/**
	 * From the given notes create an XML Document and return it. 
	 */
	public static Document saveNotesToXML(List<Note> notes) {
		
		// Create root element.
		Element rootElement = new Element(ROOT);
		rootElement.setAttribute(ROOT_VERSION, KoalaNotes.KOALA_NOTES_VERSION);
		
		// Save notes.
		rootElement.addContent(saveNotesToElement(notes));
		
		return new Document(rootElement);
	}
	
	/**
	 * From the given notes create a Notes Element and return it.
	 */
	private static Element saveNotesToElement(List<Note> notes) {
		Element notesElement = new Element(NOTES);
		for (Note note : notes) {
			notesElement.addContent(saveNoteToElement(note));
		}
		return notesElement;
	}
	
	/**
	 * Creates a Note from el, and add it to holder, recurse for the children of el.
	 * @throws KoalaException If the el is not a note element, or is missing a mandatory attribute.
	 */
	private static void loadNoteFromElement(Element el, NoteHolder holder) {
		checkElementName(el, NOTE);
		String name = getMandatoryAttributeValue(el, NOTE_NAME);
		String text = getMandatoryAttributeValue(el, NOTE_TEXT);
		Note n = new Note(name, holder, text);
		List<?> es = el.getChildren();
		for (Object childElement : es) {
			loadNoteFromElement((Element) childElement, n);
		}
	}
	
	/**
	 * From n create an Element and return it.
	 */
	private static Element saveNoteToElement(Note n) {
		Element el = new Element(NOTE);
		el.setAttribute(NOTE_NAME, n.getName());
		el.setAttribute(NOTE_TEXT, n.getText());
		for (Note childNote : n.getNotes()) {
			el.addContent(saveNoteToElement(childNote));
		}
		return el;
	}
	
	/**
	 * Traverse the Notes in the given NoteHolder using the given note reference, to find a Note.
	 * 
	 * A Note Ref Member has an index and a name.  This helps the note reference be a little more
	 * useful in case the note tree changes without the note path being updated.  This code checks
	 * that the note with the right index also has the right name, and if not will try to find
	 * another note with that name to use instead.
	 * 
	 * @return Note the note described by the note path, or null if not found.
	 */
	private static Note resolveNoteRef(Element noteRef, NoteHolder holder) {
		checkElementName(noteRef, NOTEREF);
		NoteHolder navigatedTo = holder;
		for (Object o : noteRef.getChildren()) {
			Element noteRefMember = (Element) o;
			checkElementName(noteRefMember, NOTEREFMEMBER);
			
			String noteName = getMandatoryAttributeValue(noteRefMember, NOTEREFMEMBER_NAME);
			String noteIndex = getMandatoryAttributeValue(noteRefMember, NOTEREFMEMBER_INDEX);
			int i = Integer.parseInt(noteIndex);
			Note nextup = navigatedTo.getNotes().get(i);
			if (nextup.getName().equals(noteName)) {
				navigatedTo = nextup;
			} else {
				boolean found = false;
				for (Note n : navigatedTo.getNotes()) {
					if (n.getName().equals(noteName)) {
						navigatedTo = n;
						found = true;
					}
				}
				if (!found) return null;
			}
		}
		return (Note) navigatedTo;
	}
	
	/**
	 * Create an XML Note Reference for the given note.  See resolveNoteRef().
	 */
	private static Element createNoteRef(Note note) {
		Element noteRefElement = new Element(NOTEREF);
		List<Element> noteRefMemberElements = new LinkedList<Element>();
		while (note != null) {
			Element noteRefMemberElement = new Element(NOTEREFMEMBER);
			noteRefMemberElement.setAttribute(NOTEREFMEMBER_INDEX, Integer.toString(note.getIndex()));
			noteRefMemberElement.setAttribute(NOTEREFMEMBER_NAME, note.getName());
			noteRefMemberElements.add(0, noteRefMemberElement);
			if (note.getHolder() instanceof Note) {
				note = (Note) note.getHolder();
			} else {
				note = null;
			}
		}
		noteRefElement.addContent(noteRefMemberElements);
		return noteRefElement;
	}
	
	/**
	 * Check that the Koala Notes version is consistent with that of the running application.
	 */
	private static void checkDocumentVersion(Element rootElement) {
		checkElementName(rootElement, ROOT);
		String version = getMandatoryAttributeValue(rootElement, ROOT_VERSION);
		if (!version.equals(KoalaNotes.KOALA_NOTES_VERSION)) {
			throw new KoalaException("The given Koala Notes document is from Koala Notes version "
			                         + version + ", but this Koala Notes is version "
			                         + KoalaNotes.KOALA_NOTES_VERSION + ".  The document cannot "
			                         + "be loaded.");
		}
	}
	
	/**
	 * @throws KoalaException If the element does not have the expected name.
	 */
	private static void checkElementName(Element el, String expectedName) {
		if (!el.getName().equals(expectedName)) {
			throw new KoalaException("Koala Notes was expecting XML element '" + expectedName
			                         + "' but encountered '" + el.getName()
			                         + "'.  The XML document format is incorrect.");
		}
	}
	
	/**
	 * @param el the parent element.
	 * @param elementName the name of the element to get.
	 * @return Element the required element.
	 * @throws KoalaException If the parent element did not have the required child element.
	 */
	private static Element getMandatoryElement(Element el, String elementName) {
		Element childEl = el.getChild(elementName);
		if (childEl == null) {
			throw new KoalaException("Koala Notes was expecting XML element '" + el.getName()
			                         + "' to have child element '" + elementName
			                         + "' but this element did not exist.  "
			                         + "The XML document format is incorrect.");
		}
		return childEl;
	}
	
	/**
	 * @param el the parent element.
	 * @param attributeName the name of the attribute to get.
	 * @return String the value of the required attribute.
	 * @throws KoalaException If the element did not have the required attribute.
	 */
	private static String getMandatoryAttributeValue(Element el, String attributeName) {
		String attVal = el.getAttributeValue(attributeName);
		if (attVal == null) {
			throw new KoalaException("Koala Notes was expecting XML element '" + el.getName()
			                         + "' to have attribute '" + attributeName
			                         + "' but this attribute did not exist.  "
			                         + "The XML document format is incorrect.");
		}
		return attVal;
	}
	
	/**
	 * @param el the parent element.
	 * @param attributeName the name of the attribute to get.
	 * @param defaultValue the value to return if the attribute is not present.
	 * @return String the value of the attribute, or if the attribute was not set return the
	 * defaultValue.
	 */
	private static String getNonMandatoryAttributeValue(Element el, String attributeName,
	                                                    String defaultValue) {
		String attVal = el.getAttributeValue(attributeName);
		if (attVal == null) {
			return defaultValue;
		} else {
			return attVal;
		}
	}
}
