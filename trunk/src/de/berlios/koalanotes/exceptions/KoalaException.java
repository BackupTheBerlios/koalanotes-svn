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
