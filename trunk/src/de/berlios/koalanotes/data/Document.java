package de.berlios.koalanotes.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.koalanotes.exceptions.KoalaException;

public class Document {
	File file;
	Note root;
	
	public Document(Note root) {
		this.root = root;
	}
	
	public boolean hasFile() {
		return (file != null);
	}
	
	public Note loadNotes() {
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(file);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not read file '" + file.getName() + "'.", ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException("Koala Notes could not build a document from file '" + file.getName() + "'.", jdomex);
		}
		Element rootElement = jdomDocument.getRootElement();
		root = createNoteFromElement(rootElement, null);
		return root;
	}
	
	public Note loadNotes(File file) {
		this.file = file;
		return loadNotes();
	}
	
	public void saveNotes() {
		org.jdom.Document jdomDocument = new org.jdom.Document(createElementFromNote(root));
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			FileOutputStream fos = new FileOutputStream(file);
			out.output(jdomDocument, fos);
		} catch (FileNotFoundException fnfex) {
			throw new KoalaException("Koala Notes could not find file '" + file.getName() + "'.", fnfex);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not write to file '" + file.getName() + "'.", ioex);
		}
	}
	
	public void saveNotes(File file) {
		this.file = file;
		saveNotes();
	}
	
	/** Helper for loadNotes(). */
	private Note createNoteFromElement(Element el, Note parent) {
		if (!el.getName().equals("note")) {
			throw new KoalaException("Koala Notes could not build a document from file '"
			                         + file.getName() + "': expected a note element but got a "
			                         + el.getName() + ".");
		}
		String name = el.getAttributeValue("name");
		String text = el.getAttributeValue("text");
		Note n = new Note(name, parent, text);
		List es = el.getChildren();
		for (Object childElement : es) {
			createNoteFromElement((Element) childElement, n);
		}
		return n;
	}
	
	/** Helper for saveNotes(). */
	private Element createElementFromNote(Note n) {
		Element el = new Element("note");
		el.setAttribute(new Attribute("name", n.getName()));
		el.setAttribute(new Attribute("text", n.getText()));
		for (Note childNote : n.getChildren()) {
			el.addContent(createElementFromNote(childNote));
		}
		return el;
	}
}
