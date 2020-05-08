package gui.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataKeeper;
import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestShowGeneralLifetimeIDUAction {

	private BusinessLogic businessLogic;
	private Gui frame;
	private GlobalDataKeeper globalDataKeeper;
	
	public TestShowGeneralLifetimeIDUAction() {
		
		frame = new Gui();
		businessLogic = new BusinessLogic(frame);
	}
	
	@Test
	public void testShowGeneralLifetimeIDUAction() {
		
		String filename = "filesHandler/inis/Atlas.ini";
		try {
			businessLogic.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		
		globalDataKeeper = businessLogic.getGlobalDataKeeper();
		PrintStream fileStream;
		try {
			//fileStream = new PrintStream("Test-Files/tmp-atlas-to-test.txt");
			String description = businessLogic.gui.getDescription();
			System.out.println(description);
			fileStream = new PrintStream("Test-Files/atlas-tmp-testShowGeneralLifetimeIDUAction.txt");
			System.setOut(fileStream);
			
			System.out.println(description);
			
			fileStream.close();

			fakeClick();
			businessLogic.showGeneralLifetimeIDUAction();
			
			description = businessLogic.gui.getDescription();
			System.out.println(description);
			fileStream = new PrintStream("Test-Files/atlas-testShowGeneralLifetimeIDUAction.txt");
			System.setOut(fileStream);
			
			System.out.println(description);
			
			fileStream.close();
			
			String groundTruth = null;
			String toTest = null;
			try {
				groundTruth = new String(Files.readAllBytes(Paths.get("Test-Files/atlas-testShowGeneralLifetimeIDUAction.txt")), StandardCharsets.UTF_8);
				toTest = new String(Files.readAllBytes(Paths.get("Test-Files/atlas-tmp-testShowGeneralLifetimeIDUAction.txt")), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			assertEquals(groundTruth, toTest);
			
			
			File file = new File("Test-Files/atlas-tmp-testShowGeneralLifetimeIDUAction.txt");
			file.delete();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void fakeClick() {
		businessLogic.gui.wholeCol = 5;
		String name = businessLogic.gui.generalTable.getColumnName(businessLogic.gui.wholeCol);
		System.out.println("FAKE METHOD Column index selected " + businessLogic.gui.wholeCol + " " + name);
		businessLogic.gui.generalTable.repaint();
		if (businessLogic.gui.showingPld) {
			businessLogic.makeGeneralTableIDU();
		}
	}
	
}
