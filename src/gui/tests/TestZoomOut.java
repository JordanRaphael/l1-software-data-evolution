package gui.tests;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestZoomOut {

	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestZoomOut() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
}
