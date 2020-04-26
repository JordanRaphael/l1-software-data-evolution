package gui.tests;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
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
		try {
			frame.importData("/home/jordan/git-reps/l1-software-data-evolution/filesHandler/inis/phpBB.ini");
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		buisnessLogic.zoomInAction();
		assertTrue(frame.rowHeight == height+2);
		assertTrue(frame.columnWidth == width+1);
	}
	
	

}
