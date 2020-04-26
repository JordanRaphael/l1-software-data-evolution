package gui.tests;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestZoomIn {
	
	private BusinessLogic businessLogic;
	private Gui frame;
	
	public TestZoomIn() {
		
		frame = new Gui();
		businessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testZoomIn() {
		try {
			businessLogic.importData("filesHandler/inis/phpBB.ini");
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		frame.rowHeight = 1;
		frame.columnWidth = 1;
		int height = frame.rowHeight;
		int width = frame.columnWidth;
		businessLogic.zoomInAction();
		assertTrue(frame.rowHeight == height+2);
		assertTrue(frame.columnWidth == width+1);
	}
	
	

}
