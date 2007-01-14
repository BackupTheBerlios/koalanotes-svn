package de.berlios.koalanotes.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
	
	private File file;
	
	/**
	 * The given File is the what the XMLFileStore will save to and load from, the XMLFileStore
	 * will use only this file for its entire life.
	 */
	public XMLFileStore(File file) {
		this.file = file;
	}
	
	/**
	 * Load Notes from storage, putting them into the given NoteHolder.
	 * 
	 * @throws KoalaException If the file could not be read from or the xml contained was invalid,
	 * or the document could not be built.
	 */
	public void loadNotes(NoteHolder noteHolder) {
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(file);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not read file '" + file.getName() + "'.", ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException("Koala Notes could not build a document from file '" + file.getName() + "'.", jdomex);
		}
		XMLNoteSerializer.createNotesFromJDOMDocument(jdomDocument, noteHolder);
	}
	
	/**
	 * Save the given Notes to storage.
	 * 
	 * @throws KoalaException If the file could not be found or could be not written to.
	 */
	public void saveNotes(List<Note> roots) {
		org.jdom.Document jdomDocument = XMLNoteSerializer.createJDOMDocumentFromNotes(roots);
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		FileOutputStream fos = null;
		boolean exceptionThrown = false;
		try {
			fos = new FileOutputStream(file);
			out.output(jdomDocument, fos);
		} catch (FileNotFoundException fnfex) {
			exceptionThrown = true;
			throw new KoalaException("Koala Notes could not find file '" + file.getName() + "'.", fnfex);
		} catch (IOException ioex) {
			exceptionThrown = true;
			throw new KoalaException("Koala Notes could not write to file '" + file.getName() + "'.", ioex);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ioex2) {
					if (!exceptionThrown) {
						throw new KoalaException("Koala Notes could not close file '" + file.getName() + "'.", ioex2);
					}
				}
			}
		}
	}
}
