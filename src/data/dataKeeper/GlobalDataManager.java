package data.dataKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;
import data.tableConstructors.PldConstruction;
import data.tableConstructors.TableConstructionAllSquaresIncluded;
import data.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import data.tableConstructors.TableConstructionIDU;
import data.tableConstructors.TableConstructionPhases;
import data.tableConstructors.TableConstructionWithClusters;
import data.tableConstructors.TableConstructionZoomArea;
import data.treeElements.TreeConstructionGeneral;
import data.treeElements.TreeConstructionPhasesWithClusters;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class GlobalDataManager {

	private PPLDataManager pplData;
	private TableChangeManager tableChangeManager;
	private DataCollectorsManager dataCollectorsManager;
	
	private ProjectManager projectManager;

	public GlobalDataManager() {
		
		pplData = new PPLDataManager();
		tableChangeManager = new TableChangeManager();
		dataCollectorsManager = new DataCollectorsManager();
		projectManager = new ProjectManager();
	}

	public ProjectManager getProjectDataManager() {
	
		return projectManager;
	}
	
	public DataCollectorsManager getDataCollectorsManager() {
		
		return dataCollectorsManager;
	}
	
	public TableChangeManager getTableChangeManager() {
		
		return tableChangeManager;
	}

	public void setData() {
		
		Worker worker = new Worker(projectManager.getFilename(), projectManager.getTransitionsFile());
		try {
			worker.work();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pplData.setAllPPLSchemas(worker.getAllPPLSchemas());
		pplData.setAllPPLTables(worker.getAllPPLTables());
		pplData.setAllPPLTransitions(worker.getAllPPLTransitions());
		tableChangeManager.setAllTableChanges(worker.getAllTableChanges());
		tableChangeManager.setAtomicChanges(worker.getAtomicChanges());
		projectManager.setDataFolder(worker.getDataFolder());

	}
	
	
	public PldConstruction showClusterSelectionToZoomArea(ArrayList<String> selectedTables, int selectedColumn) {
		
		ArrayList<String> tablesOfCluster = new ArrayList<String>();
		
		for (int i = 0; i < selectedTables.size(); i++) {
			String[] selectedClusterSplit = selectedTables.get(i).split(" ");
			int cluster = Integer.parseInt(selectedClusterSplit[1]);
			ArrayList<String> namesOfTables = getClusterCollectors().get(0).getClusters().get(cluster)
					.getNamesOfTables();
			for (int j = 0; j < namesOfTables.size(); j++) {
				tablesOfCluster.add(namesOfTables.get(j));
			}
			System.out.println(selectedTables.get(i));
		}

		PldConstruction table;	
		if (selectedColumn == 0) {
			table = new TableConstructionClusterTablesPhasesZoomA(getAllPPLSchemas(),
					getPhaseCollectors().get(0).getPhases(), tablesOfCluster);
		} else {
			table = createTableConstructionZoomArea(tablesOfCluster, selectedColumn);
		}
		
		System.out.println("Schemas: " + getAllPPLSchemas().size());
		System.out.println("C: " + table.constructColumns().length + " R: " + table.constructRows().length);

		return table;
	}
	
	
	/*Cannot be moved*/
	public ArrayList<PhaseCollector> getPhaseCollectors() {
		
		return dataCollectorsManager.getPhaseCollectors();
	}
	
	/*Cannot be moved*/
	public ArrayList<ClusterCollector> getClusterCollectors() {
		
		return dataCollectorsManager.getClusterCollectors();
	}
	
	
	public String getClusterDescription(String description, int row) {
		
		description = dataCollectorsManager.getClusterDescription(description, row);
		
		return description;
	}
	
	
	public void populateWithPhases(PhaseAnalyzerMainEngine mainEngine, Integer numberOfPhases) {
		
		dataCollectorsManager.populateWithPhases(mainEngine, numberOfPhases, this);
	}
	
	public void populateWithClusters(TableClusteringMainEngine mainEngine, Integer numberOfClusters) {
		
		dataCollectorsManager.populateWithClusters(mainEngine, numberOfClusters);
	}
	
	
	public TreeMap<String, PPLSchema> getAllPPLSchemas() {

		return pplData.getAllPPLSchemas();
	}
	

	public TreeMap<String, PPLTable> getAllPPLTables() {

		return pplData.getAllPPLTables();
	}
	

	public TreeMap<Integer, PPLTransition> getAllPPLTransitions() {

		return pplData.getAllPPLTransitions();
	}
	
	public void printInfo() {
		
		pplData.printInfo();
	}
	
	
	public TableConstructionAllSquaresIncluded createTableConstructionAllSquaresIncluded() {
		
		return new TableConstructionAllSquaresIncluded(getAllPPLSchemas(), getAllPPLTransitions());
	}
	
	
	public TableConstructionZoomArea createTableConstructionZoomArea(ArrayList<String> tablesOfCluster, int selectedColumn) {
		
		return new TableConstructionZoomArea(getPhaseCollectors().get(0).getPhases(),
				getAllPPLTransitions(), getAllPPLSchemas(),
				getAllPPLTables(), tablesOfCluster, selectedColumn);
	}
	
	
	public TableConstructionWithClusters createTableConstructionWithClusters() {
		
		return dataCollectorsManager.createTableConstructionWithClusters();
	}

	
	public TableConstructionPhases createTableConstructionPhases() {
		
		return new TableConstructionPhases(getAllPPLSchemas(), getPhaseCollectors().get(0).getPhases());
	}
	
	
	public TableConstructionIDU createTableConstructionIDU() {
		
		return new TableConstructionIDU(getAllPPLSchemas(), getAllPPLTransitions());
	}

	public String getPPLTablesDescription(String area) {
		
		String description = pplData.getPPLTablesDescription(area);
		
		return description;
	}
	

}
