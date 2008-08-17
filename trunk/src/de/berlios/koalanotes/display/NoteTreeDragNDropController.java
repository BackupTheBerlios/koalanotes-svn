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
package de.berlios.koalanotes.display;

import java.util.List;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.berlios.koalanotes.controllers.Controller;
import de.berlios.koalanotes.controllers.Dispatcher;
import de.berlios.koalanotes.data.Document;
import de.berlios.koalanotes.data.Note;
import de.berlios.koalanotes.data.NoteTransfer;

/**
 * A NoteTreeDragNDropSupport takes care of all drags and drops on the NoteTree.  It implements
 * DragSourceListener and DropTargetListener (via DropTargetAdapter).  When a drag and drop
 * happens to the NoteTree, the NoteTreeDragSupport methods get called in the following order:
 * 
 * dragStart
 * dragOver
 * dropAccept
 * dragSetData
 * drop
 * dragFinished
 */
class NoteTreeDragNDropController extends Controller implements DragSourceListener, DropTargetListener {
	private NoteTree noteTree;
	private Tree tree;
	private DisplayedDocument dd;
	
	/** Whether there is a drag in progress (true between calls to dragStart and dragFinished). */
	private boolean dragInProgress;
	
	/** Whether the drop being processed is onto the same tree as the drag started. */
	private boolean isLocalDrop;
	
	/** Whether the drop being processed is for a move or a copy. */
	private boolean isDropMove;
	
	/**
	 * @param noteTree the NoteTree
	 * @param tree the SWT Tree used by the NoteTree
	 * @param dd the DisplayedDocument to hold DisplayedNotes that get copied or moved to root level
	 */
	NoteTreeDragNDropController(Dispatcher d, DisplayedDocument dd, NoteTree noteTree, Tree tree) {
		super(d);
		this.dd = dd;
		this.noteTree = noteTree;
		this.tree = tree;
		dragInProgress = false;
		isLocalDrop = false;
		isDropMove = false;
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] types = new Transfer[] {NoteTransfer.getInstance()};
		DragSource dragSource = new DragSource(tree, operations);
		DropTarget dropTarget = new DropTarget(tree, operations);
		dragSource.setTransfer(types);
		dropTarget.setTransfer(types);
		dragSource.addDragListener(this);
		dropTarget.addDropListener(this);
	}
	
	/**
	 * Implements DragSourceListener.dragStart().
	 * 
	 * The drag can only be started if NoteTree.isSelectionValidForMoving returns true.
	 */
	public void dragStart(DragSourceEvent event) {
		dragInProgress = noteTree.isSelectionValidForMoving();
		event.doit = dragInProgress;
		if (dragInProgress) {
			dd.setStatusBarText("Dragging...");
		} else {
			dd.setStatusBarText("Selection is not valid for moving, the notes being moved must all be under the same parent.");
		}
	}
	
	/** Implements DragSourceListener.dragFinished(). */
	public void dragFinished(DragSourceEvent event) {
		dragInProgress = false;
		isLocalDrop = false;
		isDropMove = false;
	}
	
	/** Blank implementation of DropTargetListener.dragEnter(). */
	public void dragEnter(DropTargetEvent event) {}
	
	/** Blank implementation of DropTargetListener.dragLeave(). */
	public void dragLeave(DropTargetEvent event) {}
	
	/** Blank implementation of DropTargetListener.dragOperationChanged(). */
	public void dragOperationChanged(DropTargetEvent event) {}
	
	/** Implements DropTargetListener.dropAccept(). */
	public void dropAccept(DropTargetEvent event) {
		
		// We know it's a local drop if a drag is in progress when this method is called.
		isLocalDrop = dragInProgress;
		
		// For some reason the SWT event will soon forget whether it's a DROP_MOVE or a DROP_COPY,
		// so I record it here.  If it's not a local drop the it must be a copy.
		isDropMove = (isLocalDrop && (event.detail == DND.DROP_MOVE));
	}
	
	/**
	 * Implements DropTargetListener.dragOver().
	 * 
	 * With the given cursor location, decide whether the drop data would be added above, below or
	 * as a child of the tree item under the cursor, and give the user visual feedback on this.  If
	 * the mouse is hovering over the top 25% of the tree item then show a line above above the tree
	 * item, if hovering over the bottom 25% then show a line below, and over the middle 50% select
	 * the item.
	 */
	public void dragOver(DropTargetEvent event) {
		event.feedback = getDropAction(event);
	}
	
	/**
	 * Implements DragSourceListener.dragSetData().
	 * 
	 * If the drop is local (i.e. within the same KoalaNotes window), then no data need be set.
	 * Otherwise a NoteTransfer object is created containing an XML format of the drag data.
	 */
	public void dragSetData(DragSourceEvent event) {
		if (isLocalDrop) {
			return;
		}
		Document transferDocument = new Document();
		int i = 0;
		for (DisplayedNote dn : noteTree.getSelectedNotes()) {
			dn.getNote().copy(transferDocument, i);
			i++;
		}
		event.data = transferDocument;
	}
	
	/**
	 * Implements DropTargetListener.drop().
	 * 
	 * When note/s are dropped on the tree, move or copy them to the location at which they were
	 * dropped.  More specifically, if the drop was on a note, append them to that note's children,
	 * or if it was on the space at the bottom, append them as root notes.
	 */
	public void drop(DropTargetEvent event) {
		int dropAction = getDropAction(event);
		
		// Get the DisplayedNote that was dropped on.
		DisplayedNote dropLocation = null;
		TreeItem ti = (TreeItem) event.item;
		if (ti != null) dropLocation = ((NoteTreeNode) ti.getData()).getDisplayedNote();
		
		// Get the DisplayedNoteHolder and index that the data should be put in.
		DisplayedNoteHolder holder = null;
		int index = 0;
		if (dropLocation == null) {
			holder = dd;
			index = holder.getDisplayedNoteCount();
		} else if (dropAction == DND.FEEDBACK_SELECT) {
			holder = dropLocation;
			index = 0;
		} else {
			holder = dropLocation.getHolder();
			index = dropLocation.getNote().getIndex();
			if (dropAction == DND.FEEDBACK_INSERT_AFTER) {
				index++;
			}
		}
		
		// If it's a move (moves are always local) tell all the currently selected DisplayedNotes to
		// move themselves to the new location, but watch out for changing indices when they get
		// removed from their previous location.
		if (isDropMove) {
			List<DisplayedNote> displayedNotes = noteTree.getSelectedNotes();
			
			// First pass: cancel if any of the moves are illegal.
			for (DisplayedNote dragMe : displayedNotes) {
				
				// Can't move a parent to it's own child.
				DisplayedNoteHolder parent = holder;
				while (parent instanceof DisplayedNote) {
					if (parent == dragMe) return;
					parent = ((DisplayedNote) parent).getHolder();
				}
			}
			
			// Second pass: do the moves.
			for (DisplayedNote dragMe : displayedNotes) {
				
				// If the move is to a larger index within the same parent, then the destination
				// index needs to be adjusted down one.  For example if the user wants to drag a
				// tree item from index 0 to 1, the previous code in this method would calculate the
				// destination index as 2 since it doesn't take into account the item being moved.
				if ((dragMe.getHolder() == holder) && (dragMe.getNote().getIndex() < index)) {
					index--; 
				}
				
				// Do the move then increase the index by one for the next note.
				dragMe.move(holder, noteTree, index);
				index++;
			}
		
		// Otherwise it's a copy, which may or may not be local.
		} else {
			
			// If it's a local copy, then copy the notes currently selected in the tree, otherwise
			// use the notes in the event data.
			if (isLocalDrop) {
				for (DisplayedNote copyMe : noteTree.getSelectedNotes()) {
					Note displayMe = copyMe.getNote().copy(holder.getNoteHolder(), index);
					new DisplayedNote(holder, noteTree, displayMe);
					index++;
				}
			} else {
				Document transferDocument = (Document) event.data;
				for (Note copyMe : transferDocument.getNotes()) {
					Note displayMe = copyMe.copy(holder.getNoteHolder(), index);
					new DisplayedNote(holder, noteTree, displayMe);
					index++;
				}
			}
		}
		
		// Move complete, which means the document has been updated and the context changed.
		documentUpdatedAndContextChanged(dd, null);
		if (isDropMove) {
			dd.setStatusBarText("Notes moved.");
		} else {
			dd.setStatusBarText("Notes pasted.");
		}
	}
	
	/**
	 * With the given cursor location, decide whether the drop data would be added above, below or
	 * as a child of the tree item under the cursor.  If the mouse is hovering over the top 25% of
	 * the tree item then return DND.FEEDBACK_INSERT_BEFORE, if hovering over the bottom 25% then
	 * return DND.FEEDBACK_INSERT_AFTER, and over the middle 50% return DND.FEEDBACK_SELECT.  If
	 * there is no tree item under the cursor then the drop data will be added as a child of the
	 * last item in the tree so return DND.FEEDBACK_INSERT_AFTER.  This method is called by
	 * dragOver() and drop().
	 */
	private int getDropAction(DropTargetEvent event) {
		TreeItem item = (TreeItem) event.item;
		if (event.item == null) return DND.FEEDBACK_INSERT_AFTER;
		int itemHeight = item.getBounds().height;
		int itemQuarterHeight = itemHeight / 4;
		int treeItemY = tree.toDisplay(item.getBounds().x, item.getBounds().y).y;
		if (event.y < treeItemY + itemQuarterHeight) {
			return DND.FEEDBACK_INSERT_BEFORE;
		} else if (event.y < treeItemY + itemHeight - itemQuarterHeight) {
			return DND.FEEDBACK_SELECT;
		} else {
			return DND.FEEDBACK_INSERT_AFTER;
		}
	}
}
