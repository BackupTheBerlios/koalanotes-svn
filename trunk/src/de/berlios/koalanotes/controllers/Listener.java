package de.berlios.koalanotes.controllers;

import java.util.HashMap;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

public class Listener implements org.eclipse.swt.widgets.Listener {
	private HashMap<Widget, HashMap<Integer, String>> srcToEvtToMethod;
	private Controller anyController;
	
	public Listener(Controller anyController) {
		this.anyController = anyController;
		srcToEvtToMethod = new HashMap<Widget, HashMap<Integer, String>>();
	}
	
	public void mapEvent(Widget source, int eventType, String controllerMethod) {
		source.addListener(eventType, this);
		HashMap<Integer, String> evtToMethod = srcToEvtToMethod.get(source);
		if (evtToMethod == null) {
			evtToMethod = new HashMap<Integer, String>();
			srcToEvtToMethod.put(source, evtToMethod);
		}
		evtToMethod.put(eventType, controllerMethod);
	}
	
	public void handleEvent(Event e) {
		String methodDescriptor = srcToEvtToMethod.get(e.widget).get(e.type);
		anyController.invokeControllerMethod(methodDescriptor, e);
	}
	
	public void removeMappingFor(Widget source) {
		srcToEvtToMethod.remove(source);
	}
}
