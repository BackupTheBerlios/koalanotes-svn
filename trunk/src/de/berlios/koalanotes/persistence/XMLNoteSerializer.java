package de.berlios.koalanotes.persistence;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * XMLNoteSerializer provides helper methods for serializing Notes to and from XML.
 */
public class XMLNoteSerializer {
	private static final String ROOT_NODE_NAME = "root";
	
	/**
	 * Creates Note instances from the data stored in jdomDocument, and adds the Notes to holder.
	 */
	public static void createNotesFromJDOMDocument(Document jdomDocument, NoteHolder holder) {
		Element rootElement = jdomDocument.getRootElement();
		if (!rootElement.getName().equals(ROOT_NODE_NAME)) {
			throw new KoalaException("Koala Notes cannot load a document from XML element '"
			                         + rootElement.getName()
			                         + "', the XML document format is incorrect.");
		}
		for (Object el : rootElement.getChildren()) {
			createNoteFromElement((Element) el, holder);
		}
	}
	
	/**
	 * From notes creates a JDOM Document and returns it. 
	 */
	public static Document createJDOMDocumentFromNotes(List<Note> notes) {
		Element rootElement = new Element(ROOT_NODE_NAME);
		for (Note note : notes) {
			rootElement.addContent(createElementFromNote(note));
		}
		return new org.jdom.Document(rootElement);
	}
	
	/**
	 * Creates a Note from el, and add it to holder.
	 */
	private static void createNoteFromElement(Element el, NoteHolder holder) {
		if (!el.getName().equals("note")) {
			throw new KoalaException("Koala Notes cannot load a note from XML element "
			                         + el.getName()
			                         + "', the XML document format is incorrect.");
		}
		String name = el.getAttributeValue("name");
		String text = el.getAttributeValue("text");
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
		Element el = new Element("note");
		el.setAttribute(new Attribute("name", n.getName()));
		el.setAttribute(new Attribute("text", n.getText()));
		for (Note childNote : n.getNotes()) {
			el.addContent(createElementFromNote(childNote));
		}
		return el;
	}
}
