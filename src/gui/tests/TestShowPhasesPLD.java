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
import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class TestShowPhasesPLD {

	private BusinessLogic businessLogic;
	private Gui gui;
	
	public TestShowPhasesPLD() {
		
		gui = new Gui();
		businessLogic = new BusinessLogic(gui);
		
	}
	
	@Test
	public void testShowPhasesPLD() {
		
		GlobalDataKeeper globalDataKeeper = this.businessLogic.getGlobalDataKeeper();
		String filename = "filesHandler/inis/Atlas.ini";
		try {
			businessLogic.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}
		
		try {	
			PrintStream fileStream = new PrintStream("Test-Files/show-phases-with-clusters-pld-atlas-project-test.txt");
			System.setOut(fileStream);
			
			gui.timeWeight = (float) 0.5;
			gui.changeWeight = (float) 0.5;
			gui.preProcessingTime = false;
			gui.preProcessingChange = false;
			gui.numberOfPhases = 56;
			gui.numberOfClusters = 10;
			gui.birthWeight = 0.333;
			gui.deathWeight = 0.333;
			gui.changeWeightCl = 0.333;
	
			System.out.println(this.gui.timeWeight + " " + this.gui.changeWeight);

			PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,
					this.gui.outputAssessment1, this.gui.outputAssessment2, this.gui.timeWeight,
					this.gui.changeWeight, this.gui.preProcessingTime, this.gui.preProcessingChange);

			mainEngine.parseInput();
			System.out.println("\n\n\n");
			mainEngine.extractPhases(this.gui.numberOfPhases);
			mainEngine.connectTransitionsWithPhases(globalDataKeeper);
			globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
			TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,
					this.gui.birthWeight, this.gui.deathWeight, this.gui.changeWeightCl);
			mainEngine2.extractClusters(this.gui.numberOfClusters);
			globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
			mainEngine2.print();

			if (globalDataKeeper.getPhaseCollectors().size() != 0) {
				TableConstructionWithClusters table = new TableConstructionWithClusters(globalDataKeeper);
				final String[] columns = table.constructColumns();
				final String[][] rows = table.constructRows();
				this.gui.segmentSize = table.getSegmentSize();
				System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
				System.out.println("C: " + columns.length + " R: " + rows.length);

				this.gui.finalColumns = columns;
				this.gui.finalRows = rows;
				this.gui.tabbedPane.setSelectedIndex(0);
				businessLogic.makeGeneralTablePhases();
				businessLogic.fillClustersTree();
				
				fileStream.close();
				System.setOut(System.out);
				
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-with-clusters-pld-atlas-project-test.txt")), StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/show-phases-with-clusters-pld-atlas-project.txt")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content1, content2);
		
	}

}
