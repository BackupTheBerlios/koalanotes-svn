package de.berlios.koalanotes.persistence;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import de.berlios.koalanotes.KoalaNotes;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * XMLNoteSerializer provides helper methods for serializing Notes to and from XML.
 */
public class XMLNoteSerializer {
	
	// Root element and its attributes.
	private static final String ROOT = "koalanotesdocument";
	private static final String ROOT_VERSION = "version";
	
	// Note element and its attributes.
	private static final String NOTE = "note";
	private static final String NOTE_NAME = "name";
	private static final String NOTE_TEXT = "text";
	
	/**
	 * Creates Note instances from the data stored in jdomDocument, and adds the Notes to holder.
	 */
	public static void createNotesFromJDOMDocument(Document jdomDocument, NoteHolder holder) {
		
		// Get the root element and check the KoalaNotes version.
		Element rootElement = jdomDocument.getRootElement();
		checkElementName(rootElement, ROOT);
		String version = getMandatoryAttributeValue(rootElement, ROOT_VERSION);
		if (!version.equals(KoalaNotes.KOALA_NOTES_VERSION)) {
			throw new KoalaException("The given Koala Notes document is from Koala Notes version "
			                         + version + ", but this Koala Notes is version "
			                         + KoalaNotes.KOALA_NOTES_VERSION + ".  The document cannot "
			                         + "be loaded.");
		}
		
		// If that worked out well, load the notes.
		for (Object el : rootElement.getChildren()) {
			createNoteFromElement((Element) el, holder);
		}
	}
	
	/**
	 * From notes creates a JDOM Document and returns it. 
	 */
	public static Document createJDOMDocumentFromNotes(List<Note> notes) {
		Element rootElement = new Element(ROOT);
		rootElement.setAttribute(ROOT_VERSION, KoalaNotes.KOALA_NOTES_VERSION);
		for (Note note : notes) {
			rootElement.addContent(createElementFromNote(note));
		}
		return new org.jdom.Document(rootElement);
	}
	
	/**
	 * Creates a Note from el, and add it to holder.
	 */
	private static void createNoteFromElement(Element el, NoteHolder holder) {
		checkElementName(el, NOTE);
		String name = getMandatoryAttributeValue(el, NOTE_NAME);
		String text = getMandatoryAttributeValue(el, NOTE_TEXT);
		Note n = new Note(name, holder, text);
		List es = el.getChildren();
		for (Object childElement : es) {
			createNoteFromElement((Element) childElement, n);
		}
	}
	
	/**
	 * From n create an Element and return it.
	 */
	private static Element createElementFromNote(Note n) {
		Element el = new Element(NOTE);
		el.setAttribute(new Attribute(NOTE_NAME, n.getName()));
		el.setAttribute(new Attribute(NOTE_TEXT, n.getText()));
		for (Note childNote : n.getNotes()) {
			el.addContent(createElementFromNote(childNote));
		}
		return el;
	}
	
	private static void checkElementName(Element el, String expectedName) {
		if (!el.getName().equals(expectedName)) {
			throw new KoalaException("Koala Notes was expecting XML element '" + expectedName
			                         + "' but encountered '" + el.getName()
			                         + "'.  The XML document format is incorrect.");
		}
	}
	
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
}
