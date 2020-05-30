package data.dataKeeper;

import java.util.ArrayList;

import data.tableConstructors.PldConstruction;
import data.tableConstructors.TableConstructionAllSquaresIncluded;
import data.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import data.tableConstructors.TableConstructionIDU;
import data.tableConstructors.TableConstructionPhases;
import data.tableConstructors.TableConstructionWithClusters;
import data.tableConstructors.TableConstructionZoomArea;
import data.treeElements.TreeConstructionGeneral;
import data.treeElements.TreeConstructionPhasesWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataManipulator {

	public PldConstruction showClusterSelectionToZoomArea(GlobalDataManager globalDataKeeper,
			ArrayList<String> selectedTables, int selectedColumn) {
		ArrayList<String> tablesOfCluster = new ArrayList<String>();
		for (int i = 0; i < selectedTables.size(); i++) {
			String[] selectedClusterSplit = selectedTables.get(i).split(" ");
			int cluster = Integer.parseInt(selectedClusterSplit[1]);
			ArrayList<String> namesOfTables = globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster)
					.getNamesOfTables();
			for (int j = 0; j < namesOfTables.size(); j++) {
				tablesOfCluster.add(namesOfTables.get(j));
			}
			System.out.println(selectedTables.get(i));
		}

		PldConstruction table;
		if (selectedColumn == 0) {
			table = new TableConstructionClusterTablesPhasesZoomA(globalDataKeeper.getAllPPLSchemas(),
					globalDataKeeper.getPhaseCollectors().get(0).getPhases(), tablesOfCluster);
		} else {
			table = createTableConstructionZoomArea(globalDataKeeper, tablesOfCluster, selectedColumn);
		}
		System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: " + table.constructColumns().length + " R: " + table.constructRows().length);

		return table;
	}

	public void populateWithPhases(GlobalDataManager globalDataKeeper, PhaseAnalyzerMainEngine mainEngine,
			Integer numberOfPhases) {
		mainEngine.parseInput();
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(globalDataKeeper);
		globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
	}

	public void populateWithClusters(GlobalDataManager globalDataKeeper, TableClusteringMainEngine mainEngine,
			Integer numberOfClusters) {
		mainEngine.extractClusters(numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainEngine.getClusterCollectors());
		mainEngine.print();
	}

	public TableConstructionPhases createTableConstructionPhases(GlobalDataManager globalDataKeeper) {
		return new TableConstructionPhases(globalDataKeeper.getAllPPLSchemas(),
				globalDataKeeper.getPhaseCollectors().get(0).getPhases());
	}

	public TreeConstructionPhasesWithClusters createTreeConstructionPhasesWithClusters(
			GlobalDataManager globalDataKeeper) {
		return new TreeConstructionPhasesWithClusters(globalDataKeeper);

	}

	public TableConstructionIDU createTableConstructionIDU(GlobalDataManager globalDataKeeper) {
		return new TableConstructionIDU(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
	}

	public TableConstructionWithClusters createTableConstructionWithClusters(GlobalDataManager globalDataKeeper) {
		return new TableConstructionWithClusters(globalDataKeeper.getPhaseCollectors().get(0).getPhases(),
				globalDataKeeper.getClusterCollectors().get(0).getClusters());
	}

	public TreeConstructionGeneral createTreeConstructionGeneral(GlobalDataManager globalDataKeeper) {
		return new TreeConstructionGeneral(globalDataKeeper);
	}

	public TableConstructionZoomArea createTableConstructionZoomArea(GlobalDataManager globalDataKeeper,
			ArrayList<String> tablesOfCluster, int selectedColumn) {
		return new TableConstructionZoomArea(globalDataKeeper.getPhaseCollectors().get(0).getPhases(),
				globalDataKeeper.getAllPPLTransitions(), globalDataKeeper.getAllPPLSchemas(),
				globalDataKeeper.getAllPPLTables(), tablesOfCluster, selectedColumn);
	}

	public TableConstructionAllSquaresIncluded createTableConstructionAllSquaresIncluded(
			GlobalDataManager globalDataKeeper) {
		return new TableConstructionAllSquaresIncluded(globalDataKeeper.getAllPPLSchemas(),
				globalDataKeeper.getAllPPLTransitions());
	}

	public TableConstructionClusterTablesPhasesZoomA createTableConstructionClusterTablesPhasesZoomA(
			GlobalDataManager globalDataKeeper, ArrayList<String> tablesOfCluster) {
		return new TableConstructionClusterTablesPhasesZoomA(globalDataKeeper.getAllPPLSchemas(),
				globalDataKeeper.getPhaseCollectors().get(0).getPhases(), tablesOfCluster);
	}

}
