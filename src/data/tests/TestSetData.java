package data.tests;

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

public class TestSetData {
	private BusinessLogic businessLogic;
	private Gui frame;
	private GlobalDataKeeper globalDataKeeper;
	
	public TestSetData() {
		
		frame = new Gui();
		businessLogic = new BusinessLogic(frame);
	}
	
	@Test
	public void testSetData() {
		String filename = "filesHandler/inis/Atlas.ini";
		try {
			businessLogic.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		
		globalDataKeeper = businessLogic.getGlobalDataKeeper();
		PrintStream fileStream;
		try {
			fileStream = new PrintStream("Test-Files/tmp-atlas-to-test.txt");
			System.setOut(fileStream);
			
			System.out.println(globalDataKeeper.getAllPPLSchemas().toString());
			System.out.println(globalDataKeeper.getAllPPLTables().toString());
			System.out.println(globalDataKeeper.getAtomicChanges().toString());
			System.out.println(globalDataKeeper.getAllTableChanges().toString());
			System.out.println(globalDataKeeper.getTmpTableChanges().toString());
			System.out.println(globalDataKeeper.getAllPPLTransitions().toString());
			System.out.println(globalDataKeeper.getDataFolder().toString());
			System.out.println(globalDataKeeper.getPhaseCollectors().toString());
			System.out.println(globalDataKeeper.getClusterCollectors().toString());
			
			fileStream.close();
			
			String groundTruth = null;
			String toTest = null;
			try {
				groundTruth = new String(Files.readAllBytes(Paths.get("Test-Files/atlas-testSetData.txt")), StandardCharsets.UTF_8);
				toTest = new String(Files.readAllBytes(Paths.get("Test-Files/tmp-atlas-to-test.txt")), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			assertEquals(groundTruth, toTest);
			
			
			File file = new File("Test-Files/tmp-atlas-to-test.txt");
			file.delete();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
}
