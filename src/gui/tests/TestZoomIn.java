package gui.tests;

import org.junit.Test;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestZoomIn {
	
	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestZoomIn() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testZoomIn() {
		//buisnessLogic.gui.zoom
	}
	
	

}
