package gui.tests;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestEditProject {
	
	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestEditProject() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
}
