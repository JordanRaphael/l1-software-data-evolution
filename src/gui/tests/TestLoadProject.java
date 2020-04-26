package gui.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		
		String filename = "filesHandler/inis/Atlas.ini";
		
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

		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/load-atlas-project-business-logic-test.txt")), StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/load-atlas-project-test.txt")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content1, content2);
				
	}

}
