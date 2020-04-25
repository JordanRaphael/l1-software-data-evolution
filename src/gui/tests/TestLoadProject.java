package gui.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestLoadProject {

	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestLoadProject() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}
	
	@Test
	public void testLoadProject() {
		
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

}
