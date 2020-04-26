package gui.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestZoomOut {

	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestZoomOut() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testZoomOut() {
		try {
			frame.importData("filesHandler/inis/phpBB.ini");
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		frame.rowHeight = 10;
		frame.columnWidth = 10;
		int height = frame.rowHeight;
		int width = frame.columnWidth;
		buisnessLogic.zoomOutAction();
		assertTrue(frame.rowHeight == height-2);
		assertTrue(frame.columnWidth == width-1);
		
		frame.rowHeight = 2;
		frame.columnWidth = 1;
		height = frame.rowHeight;
		width = frame.columnWidth;
		buisnessLogic.zoomOutAction();
		assertTrue(frame.rowHeight == 1);
		assertTrue(frame.columnWidth == 1);
		
	}
}
