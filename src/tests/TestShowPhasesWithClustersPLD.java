package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.DataCollectorsManager;
import data.dataKeeper.GlobalDataManager;
import data.tableConstructors.TableConstructionPhases;
import gui.mainEngine.Gui;
import gui.mainEngine.GuiController;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class TestShowPhasesWithClustersPLD {

	private GlobalDataManager globalDataManager;
	private GuiController guiController;
	private Gui gui;

	public TestShowPhasesWithClustersPLD() {

		gui = new Gui();
		guiController = new GuiController(gui);
	}

	@Test
	public void testShowPhasesPLD() {

		String filename = "filesHandler/inis/Atlas.ini";
		try {
			globalDataManager = new GlobalDataManager();
			
			globalDataManager.getProjectDataManager().parseFile(filename);
			
			System.out.println("Project Name:" + globalDataManager.getProjectDataManager().getProjectName());
			System.out.println("Dataset txt:" + globalDataManager.getProjectDataManager().getDatasetTxt());
			System.out.println("Input Csv:" + globalDataManager.getProjectDataManager().getInputCsv());
			System.out.println("Output Assessment1:" + globalDataManager.getProjectDataManager().getOutputAssessment1());
			System.out.println("Output Assessment2:" + globalDataManager.getProjectDataManager().getOutputAssessment2());
			System.out.println("Transitions File:" + globalDataManager.getProjectDataManager().getTransitionsFile());

			globalDataManager.setData();
			System.out.println(globalDataManager.getAllPPLTables().size());

			System.out.println(filename);

			guiController.setGlobalDataManager(globalDataManager);
			guiController.fillTable();
			guiController.fillTree();

			gui.currentProject = filename;
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

			PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(globalDataManager.getProjectDataManager().getInputCsv(), globalDataManager.getProjectDataManager().getOutputAssessment1(),
					globalDataManager.getProjectDataManager().getOutputAssessment2(), gui.timeWeight, gui.changeWeight, gui.preProcessingTime,
					gui.preProcessingChange);

			mainEngine.parseInput();
			System.out.println("\n\n\n");
			mainEngine.extractPhases(gui.numberOfPhases);
			mainEngine.connectTransitionsWithPhases(guiController.getGlobalDataManager());
			DataCollectorsManager dataCollectorsManager = guiController.getGlobalDataManager().getDataCollectorsManager();
			dataCollectorsManager.setPhaseCollectors(mainEngine.getPhaseCollectors());

			if (guiController.getGlobalDataManager().getPhaseCollectors().size() != 0) {

				TableConstructionPhases table = guiController.getGlobalDataManager()
						.createTableConstructionPhases();

				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				this.gui.segmentSize = table.getSegmentSize();
				System.out.println("Schemas: " + guiController.getGlobalDataManager().getAllPPLSchemas().size());
				System.out.println("C: " + columns.length + " R: " + rows.length);

				gui.finalColumns = columns;
				gui.finalRows = rows;
				gui.tabbedPane.setSelectedIndex(0);
				guiController.makeGeneralTablePhases();
				guiController.fillPhasesTree();
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