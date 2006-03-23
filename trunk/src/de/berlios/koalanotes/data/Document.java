package de.berlios.koalanotes.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.koalanotes.exceptions.KoalaException;

public class Document implements NoteHolder {
	
	private File file;
	private List<Note> roots;
	
	public Document() {
		this.roots = new LinkedList<Note>();
	}
	
	public boolean hasFile() {
		return (file != null);
	}
	
	// Implement NoteHolder
	public List<Note> getNotes() {return roots;}
	public void addNote(Note note) {roots.add(note);}
	public void addNote(Note note, int index) {roots.add(index, note);}
	public void removeNote(Note note) {roots.remove(note);}
	
	public List<Note> loadNotes(File file) {
		this.file = file;
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(file);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not read file '" + file.getName() + "'.", ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException("Koala Notes could not build a document from file '" + file.getName() + "'.", jdomex);
		}
		Element rootElement = jdomDocument.getRootElement();
		if (!rootElement.getName().equals("root")) {
			throw new KoalaException("Koala Notes could not load a document from file '"
			                         + file.getName() + "', the format is incorrect.");
		}
		roots = new LinkedList<Note>();
		for (Object el : rootElement.getChildren()) {
			createNoteFromElement((Element) el, this);
		}
		return roots;
	}
	
	public void saveNotes() {
		Element rootElement = new Element("root");
		for (Note root : roots) {
			rootElement.addContent(createElementFromNote(root));
		}
		org.jdom.Document jdomDocument = new org.jdom.Document(rootElement);
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
	private Note createNoteFromElement(Element el, NoteHolder holder) {
		if (!el.getName().equals("note")) {
			throw new KoalaException("Koala Notes could not load a document from file '"
									 + file.getName() + "', the format is incorrect.");
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
	
	/** Helper for saveNotes(). */
	private Element createElementFromNote(Note n) {
		Element el = new Element("note");
		el.setAttribute(new Attribute("name", n.getName()));
		el.setAttribute(new Attribute("text", n.getText()));
		for (Note childNote : n.getNotes()) {
			el.addContent(createElementFromNote(childNote));
		}
		return el;
	}
}
