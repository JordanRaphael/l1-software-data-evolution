package gui.tests;


import static org.junit.Assert.assertTrue;

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
		int height = frame.rowHeight;
		int width = frame.columnWidth;
		System.out.println(height);
		System.out.println(width);
		buisnessLogic.zoomInAction();
		//frame.zoomInButton.doClick();
		System.out.println(frame.rowHeight);
		System.out.println(frame.columnWidth);
		assertTrue(frame.rowHeight == height+2);
		assertTrue(frame.columnWidth == width+1);
	}
	
	

}
