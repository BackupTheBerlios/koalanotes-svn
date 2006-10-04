package de.berlios.koalanotes.persistence;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * XMLNoteSerializer provides helper methods for serializing Notes to and from XML.
 */
public class XMLNoteSerializer {
	
	public static Note createNoteFromElement(Element el, NoteHolder holder) {
		if (!el.getName().equals("note")) {
			throw new KoalaException("Koala Notes cannot load a note from XML element "
			                         + el.getName() + ".");
		}
		String name = el.getAttributeValue("name");
		String text = el.getAttributeValue("text");
		Note n = new Note(name, holder, text);
		List es = el.getChildren();
		for (Object childElement : es) {
			createNoteFromElement((Element) childElement, n);
		}
		return n;
	}
	
	public static Element createElementFromNote(Note n) {
		Element el = new Element("note");
		el.setAttribute(new Attribute("name", n.getName()));
		el.setAttribute(new Attribute("text", n.getText()));
		for (Note childNote : n.getNotes()) {
			el.addContent(createElementFromNote(childNote));
		}
		return el;
	}
}
