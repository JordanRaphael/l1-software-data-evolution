package gui.mainEngine;

import static org.junit.Assert.*;

import javax.swing.JMenuItem;

import org.junit.Test;

public class GuiTest {

	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public GuiTest() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testGui() {

		fail("Not yet implemented");
	}
	
	@Test
	public void testLoadProject() {
		
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		
	}
	
	@Test
	public void testCreateProject() {
		
	}
	
	@Test
	public void testEditProject() {
		//button.doClick();
	}
	
	@Test
	public void testZoomIn() {
		
	}
	
	@Test
	public void testZoomOut() {
		
	}

}
