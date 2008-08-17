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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.koalanotes.data.Document;
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
	
	public String getFileAbsolutePath() {
		return file.getAbsolutePath();
	}
	
	/**
	 * Populate the given KoalaNotes document with settings and notes from the XML file store.
	 * 
	 * @throws KoalaException If the file could not be read from or the xml contained was invalid,
	 * or the document could not be built.
	 */
	public void loadKoalaDocument(Document koalaDocument) {
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(file);
		} catch (IOException ioex) {
			throw new KoalaException("Koala Notes could not read file '" + file.getName() + "'.", ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException("Koala Notes could not build a document from file '" + file.getName() + "'.", jdomex);
		}
		XMLSerializer.loadKoalaDocumentFromXML(koalaDocument, jdomDocument);
	}
	
	/**
	 * Save the given KoalaNotes document to storage.
	 * 
	 * @throws KoalaException If the file could not be found or could be not written to.
	 */
	public void saveKoalaDocument(Document koalaDocument) {
		org.jdom.Document jdomDocument = XMLSerializer.saveKoalaDocumentToXML(koalaDocument);
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
