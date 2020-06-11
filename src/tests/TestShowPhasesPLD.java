package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.tableConstructors.TableConstructionPhases;
import gui.mainEngine.Gui;
import gui.mainEngine.GuiController;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class TestShowPhasesPLD {

	private GuiController guiController;
	private Gui gui;

	public TestShowPhasesPLD() {

		gui = new Gui();
		guiController = new GuiController(gui);
	}

	@Test
	public void testShowPhasesPLD() {

		String filename = "filesHandler/inis/Atlas.ini";
		try {
			guiController.importData(filename);
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
					this.gui.outputAssessment1, this.gui.outputAssessment2, this.gui.timeWeight, this.gui.changeWeight,
					this.gui.preProcessingTime, this.gui.preProcessingChange);

			mainEngine.parseInput();
			System.out.println("\n\n\n");
			mainEngine.extractPhases(this.gui.numberOfPhases);
			mainEngine.connectTransitionsWithPhases(guiController.getGlobalDataKeeper());
			guiController.getGlobalDataKeeper().setPhaseCollectors(mainEngine.getPhaseCollectors());

			if (guiController.getGlobalDataKeeper().getPhaseCollectors().size() != 0) {
				TableConstructionPhases table = guiController.getGlobalDataKeeper()
						.createTableConstructionPhases();

				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				this.gui.segmentSize = table.getSegmentSize();
				System.out.println("Schemas: " + guiController.getGlobalDataKeeper().getAllPPLSchemas().size());
				System.out.println("C: " + columns.length + " R: " + rows.length);

				this.gui.finalColumns = columns;
				this.gui.finalRows = rows;
				this.gui.tabbedPane.setSelectedIndex(0);
				this.guiController.makeGeneralTablePhases();
				this.guiController.fillPhasesTree();
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