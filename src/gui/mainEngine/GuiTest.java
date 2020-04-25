package gui.mainEngine;

import static org.junit.Assert.*;

import javax.swing.JMenuItem;

import org.junit.Test;

public class GuiTest {

	private BuisnessLogic buisnessLogic;
	private Gui frame;
	
	public GuiTest() {
		
		buisnessLogic = new BuisnessLogic();
		frame = new Gui();
	}
	
	@Test
	public void testGui() {
		//Gui gui = new Gui();
		fail("Not yet implemented");
	}
	
	public void testLoadProject() {
		
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		
	}
	
	public void testCreateProject() {
		
	}
	
	public void testEditProject() {
		//button.doClick();
	}
	
	public void testZoomIn() {
		
	}
	
	public void testZoomOut() {
		
	}

}
