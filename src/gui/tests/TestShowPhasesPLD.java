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
import data.tableConstructors.TableConstructionPhases;
import data.tableConstructors.TableConstructionWithClusters;
import gui.mainEngine.GuiController;
import gui.mainEngine.Gui;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class TestShowPhasesPLD {

	private GuiController businessLogic;
	private Gui gui;
	
	public TestShowPhasesPLD() {
		
		gui = new Gui();
		businessLogic = new GuiController(gui);
		
	}
	
	
	@Test
	public void testShowPhasesPLD() {

		String filename = "filesHandler/inis/Atlas.ini";
		try {
			businessLogic.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}

		try {	
			PrintStream fileStream = new PrintStream("Test-Files/show-phases-pld-atlas-project-test.txt");
			System.setOut(fileStream);
			gui.timeWeight = (float) 0.5;
			gui.changeWeight = (float) 0.5;
			gui.preProcessingTime = false;
			gui.preProcessingChange = false;
			gui.numberOfPhases = 50;

			System.out.println(this.gui.timeWeight + " " + this.gui.changeWeight);

			PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,
					this.gui.outputAssessment1, this.gui.outputAssessment2, this.gui.timeWeight,
					this.gui.changeWeight, this.gui.preProcessingTime, this.gui.preProcessingChange);

			mainEngine.parseInput();
			System.out.println("\n\n\n");
			mainEngine.extractPhases(this.gui.numberOfPhases);
			mainEngine.connectTransitionsWithPhases(businessLogic.getGlobalDataKeeper());
			businessLogic.getGlobalDataKeeper().setPhaseCollectors(mainEngine.getPhaseCollectors());

			if (businessLogic.getGlobalDataKeeper().getPhaseCollectors().size() != 0) {
				TableConstructionPhases table = businessLogic.getGlobalDataKeeper().createTableConstructionPhases();
				
				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				this.gui.segmentSize = table.getSegmentSize();
				System.out.println("Schemas: " + businessLogic.getGlobalDataKeeper().getAllPPLSchemas().size());
				System.out.println("C: " + columns.length + " R: " + rows.length);

				this.gui.finalColumns = columns;
				this.gui.finalRows = rows;
				this.gui.tabbedPane.setSelectedIndex(0);
				this.businessLogic.makeGeneralTablePhases();
				this.businessLogic.fillPhasesTree();
			}

			fileStream.close();
		}catch (Exception e) {
			// TODO: handle exception
		}

		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-pld-atlas-project-test.txt")), StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-pld-atlas-project.txt")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content1, content2);

	}

}