package de.berlios.koalanotes.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteHolder;
import de.berlios.koalanotes.exceptions.KoalaException;

/**
 * An XMLFileStore saves to and loads from a particular XML file.
 */
public class XMLFileStore {
	private static final String ROOT_NODE_NAME = "root";
	
	private File file;
	
	/**
	 * The given File is the what the XMLFileStore will save to and load from, the XMLFileStore
	 * will use only this file for its entire life.
	 */
	public XMLFileStore(File file) {
		this.file = file;
	}
	
	/** Load Notes from storage, putting them into the given NoteHolder. */
	public void loadNotes(NoteHolder noteHolder) {
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(file);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not read file '" + file.getName() + "'.", ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException("Koala Notes could not build a document from file '" + file.getName() + "'.", jdomex);
		}
		Element rootElement = jdomDocument.getRootElement();
		if (!rootElement.getName().equals(ROOT_NODE_NAME)) {
			throw new KoalaException("Koala Notes could not load a document from file '"
			                         + file.getName() + "', the format is incorrect.");
		}
		for (Object el : rootElement.getChildren()) {
			XMLNoteSerializer.createNoteFromElement((Element) el, noteHolder);
		}
	}
	
	/** Save the given Notes to storage. */
	public void saveNotes(List<Note> roots) {
		Element rootElement = new Element(ROOT_NODE_NAME);
		for (Note root : roots) {
			rootElement.addContent(XMLNoteSerializer.createElementFromNote(root));
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
}
