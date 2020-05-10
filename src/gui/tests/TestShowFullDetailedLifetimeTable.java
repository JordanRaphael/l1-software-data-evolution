package gui.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataKeeper;
import data.tableConstructors.TableConstructionAllSquaresIncluded;
import data.tableConstructors.TableConstructionPhases;
import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class TestShowFullDetailedLifetimeTable {
	
	private BusinessLogic businessLogic;
	private Gui gui;
	
	public TestShowFullDetailedLifetimeTable() {
		
		gui = new Gui();
		businessLogic = new BusinessLogic(gui);
		
	}
	
	@Test
	public void testShowFullDetailedLifetimeTable() {
		
		GlobalDataKeeper globalDataKeeper = this.businessLogic.getGlobalDataKeeper();
		String filename = "filesHandler/inis/Atlas.ini";
		try {
			businessLogic.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		
		try {	
			PrintStream fileStream = new PrintStream("Test-Files/show-full-detailed-lifetime-table-atlas-project-test.txt");
			System.setOut(fileStream);
			
			if (!(gui.currentProject == null)) {
				TableConstructionAllSquaresIncluded table = globalDataKeeper.createTableConstructionAllSquaresIncluded();
				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				gui.segmentSizeDetailedTable = table.getSegmentSize();
				gui.tabbedPane.setSelectedIndex(0);
				gui.makeDetailedTable(columns, rows, true);
				
				fileStream.close();
				System.setOut(System.out);
				
			}
			
			fileStream.close();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/show-full-detailed-lifetime-table-atlas-project-test.txt")), StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/show-full-detailed-lifetime-table-atlas-project.txt")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content1, content2);
		
		
	}

}
