package gui.tests;

import org.junit.Test;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestEditProject {
	
	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestEditProject() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testEditProject() {
		
		
		//ActionEvent actionEvent = new ActionEvent("test", 0, "TestProjectName");
		
	}
	
}
