package de.berlios.koalanotes.exceptions;

@SuppressWarnings("serial")
public class KoalaException extends RuntimeException {
	
	public KoalaException(String message) {
		super(message);
	}
	
	public KoalaException(String message, Throwable wrappedException) {
		super(message, wrappedException);
	}
	
	public KoalaException(Throwable wrappedException) {
		super(wrappedException);
	}
}
