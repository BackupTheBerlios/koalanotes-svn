package data;

import java.util.LinkedList;
import java.util.List;

public class Note {
	private String name;
	
	private Note parent;
	private List<Note> children;
	
	private String text;
	
	public Note(String name, Note parent, String text) {
		this.name = name;
		this.parent = parent;
		this.children = new LinkedList<Note>();
		this.text = text;
		
		if (parent != null) parent.addChild(this);
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public Note getParent() {return parent;}
	public List<Note> getChildren() {return children;}
	public void addChild(Note note) {children.add(note);}
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
}
