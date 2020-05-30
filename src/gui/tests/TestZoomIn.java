package gui.tests;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import gui.mainEngine.GuiController;
import gui.mainEngine.Gui;

public class TestZoomIn {
	
	private GuiController guiController;
	private Gui frame;
	
	public TestZoomIn() {
		
		frame = new Gui();
		guiController = new GuiController(frame);
		
	}
	
	@Test
	public void testZoomIn() {
		try {
			guiController.importData("filesHandler/inis/phpBB.ini");
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		frame.rowHeight = 1;
		frame.columnWidth = 1;
		int height = frame.rowHeight;
		int width = frame.columnWidth;
		guiController.zoomInAction();
		assertTrue(frame.rowHeight == height+2);
		assertTrue(frame.columnWidth == width+1);
	}
	
	

}
