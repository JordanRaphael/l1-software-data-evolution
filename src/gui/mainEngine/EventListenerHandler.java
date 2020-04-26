package gui.mainEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class EventListenerHandler {

	private BusinessLogic businessLogic;
	private Gui gui;
	
	public EventListenerHandler(BusinessLogic businessLogic, Gui gui) {
		
		this.businessLogic = businessLogic;
		this.gui = gui;
		
	}
	
	public TreeSelectionListener createTreeSelectionListener() {
		
		TreeSelectionListener listener = (new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		});
		
		return listener;
		
	}
	
	public ActionListener  createProject() {
		
		ActionListener listener =  new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				businessLogic.createProjectAction();
			}
		};
		
		return listener;
		
	}
	
	
}
