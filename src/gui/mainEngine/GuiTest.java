package gui.mainEngine;

import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JMenuItem;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import java.io.File;  // Import the File class

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
		
		GuiTest test = new GuiTest();
		String filename = "/home/vagosdim/Desktop/Semester2/Software-Reengineering/filesHandler/inis/Atlas.ini";
		
		try {
			
			PrintStream fileStream = new PrintStream("Test-Files/load-atlas-project-business-logic-test.txt");
			System.setOut(fileStream);
			buisnessLogic.gui.importData(filename);
			fileStream.close();
			
			fileStream = new PrintStream("Test-Files/load-atlas-project-test.txt");
			System.setOut(fileStream);
			frame.importData(filename);
			fileStream.close();
			
			
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file1 = new File("Test-Files/load-atlas-project-business-logic.txt-test");
		File file2 = new File("Test-Files/load-atlas-project-test.txt");

		assertEquals(file1, file2 );
				
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
