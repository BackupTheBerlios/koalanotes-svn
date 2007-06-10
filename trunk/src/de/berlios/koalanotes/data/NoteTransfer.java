package de.berlios.koalanotes.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.koalanotes.exceptions.KoalaException;
import de.berlios.koalanotes.persistence.XMLSerializer;

/**
 * For Dragging-n-dropping a KoalaNotes Document.
 */
public class NoteTransfer extends ByteArrayTransfer {
	private static final String NOTE_TRANSFER_TYPE_NAME = "koala_notes_document";
	private static final int NOTE_TRANSFER_TYPE_ID = registerType(NOTE_TRANSFER_TYPE_NAME);
	
	private static final String ERROR_TEXT = "Koala Notes had an error transferring a Note";
	
	private static NoteTransfer instance = new NoteTransfer();
	
	private NoteTransfer() {}
		
	public static NoteTransfer getInstance() {
		return instance;
	}
	
	public void javaToNative(Object object, TransferData transferData) {

		// If Object contains null or invalid data or TransferData is unsupported, return.
		if (object == null) return;
		if (!(object instanceof Document)) return;
		if (!isSupportedType(transferData)) return;
		
		// Write data to a byte[].
		Document koalaDocument = (Document) object;
		org.jdom.Document jdomDocument = XMLSerializer.saveNotesToXML(koalaDocument.getNotes());
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		XMLOutputter xmlOut = new XMLOutputter(Format.getRawFormat());
		try {
			xmlOut.output(jdomDocument, bytesOut);
		} catch (IOException ioe) {
			throw new KoalaException(ERROR_TEXT, ioe);
		}
		byte[] buffer = bytesOut.toByteArray();
		
		// Super converts byte[] to native.
		super.javaToNative(buffer, transferData);
	}
	
	public Object nativeToJava(TransferData transferData) {
		
		// Super converts TransferData to byte[].  If TransferData is null or unsupported, return.
		if (!isSupportedType(transferData)) return null;
		byte[] buffer = (byte[]) super.nativeToJava(transferData);
		if (buffer == null) return null;
		
		// Convert byte[] to List<Note>
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = new SAXBuilder().build(in);
		} catch (IOException ioex) {
			throw new KoalaException(ERROR_TEXT, ioex);
		} catch (JDOMException jdomex) {
			throw new KoalaException(ERROR_TEXT, jdomex);
		}
		Document koalaDocument = new Document();
		XMLSerializer.loadKoalaDocumentFromXML(koalaDocument, jdomDocument);
		return koalaDocument;
	}
	
	protected String[] getTypeNames() {
		return new String[] {NOTE_TRANSFER_TYPE_NAME};
	}

	protected int[] getTypeIds() {
		return new int[] {NOTE_TRANSFER_TYPE_ID};
	}
}
