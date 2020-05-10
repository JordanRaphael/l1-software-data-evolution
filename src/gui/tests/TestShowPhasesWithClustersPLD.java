package gui.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataKeeper;
import data.tableConstructors.TableConstructionPhases;
import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class TestShowPhasesWithClustersPLD {

	private BusinessLogic businessLogic;
	private Gui gui;

	public TestShowPhasesWithClustersPLD() {

		gui = new Gui();
		businessLogic = new BusinessLogic(gui);

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

			System.out.println(gui.timeWeight + " " + gui.changeWeight);

			PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(gui.inputCsv, gui.outputAssessment1,
					gui.outputAssessment2, gui.timeWeight, gui.changeWeight, gui.preProcessingTime,
					gui.preProcessingChange);

			mainEngine.parseInput();
			System.out.println("\n\n\n");
			mainEngine.extractPhases(gui.numberOfPhases);
			mainEngine.connectTransitionsWithPhases(businessLogic.getGlobalDataKeeper());
			businessLogic.getGlobalDataKeeper().setPhaseCollectors(mainEngine.getPhaseCollectors());

			if (businessLogic.getGlobalDataKeeper().getPhaseCollectors().size() != 0) {
				
				TableConstructionPhases table = businessLogic.getGlobalDataKeeper().createTableConstructionPhases();
				
				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				this.gui.segmentSize = table.getSegmentSize();
				System.out.println("Schemas: " + businessLogic.getGlobalDataKeeper().getAllPPLSchemas().size());
				System.out.println("C: " + columns.length + " R: " + rows.length);

				gui.finalColumns = columns;
				gui.finalRows = rows;
				gui.tabbedPane.setSelectedIndex(0);
				businessLogic.makeGeneralTablePhases();
				businessLogic.fillPhasesTree();
			}

			fileStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-pld-atlas-project-test.txt")),
					StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-pld-atlas-project.txt")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content1, content2);

	}

}