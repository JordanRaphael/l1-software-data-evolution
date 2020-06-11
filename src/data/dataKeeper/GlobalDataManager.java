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
import data.treeElements.TreeConstructionGeneral;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.ClusterCollector;

public class GlobalDataManager {

	private PPLDataManager pplData;
	private TableChangeManager dbChangesData;
	private DataCollectorsManager dataCollectorsManager;
	
	private ProjectManager projectManager;

	public GlobalDataManager(String filename, String transitionsFile) {
		pplData = new PPLDataManager();
		dbChangesData = new TableChangeManager();
		dataCollectorsManager = new DataCollectorsManager();
		projectManager = new ProjectManager(filename, transitionsFile);
	}

	public GlobalDataManager() {

	}
	
	public ProjectManager getProjectDetailsData() {
	
		return projectManager;
	}

	public void setData() {

		Worker worker = new Worker(projectManager.getFilename(), projectManager.getTransitionsFile());
		try {
			worker.work();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setAllPPLSchemas(worker.getAllPPLSchemas());
		setAllPPLTables(worker.getAllPPLTables());
		setAllPPLTransitions(worker.getAllPPLTransitions());
		setAllTableChanges(worker.getAllTableChanges());
		setAtomicChanges(worker.getAtomicChanges());
		projectManager.setDataFolder(worker.getDataFolder());

	}

	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors) {
		dataCollectorsManager.setPhaseCollectors(phaseCollectors);
	}

	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors) {
		dataCollectorsManager.setClusterCollectors(clusterCollectors);
	}

	private void setAllPPLSchemas(TreeMap<String, PPLSchema> allPPLSchemas) {

		pplData.setAllPPLSchemas(allPPLSchemas);

	}

	private void setAllPPLTables(TreeMap<String, PPLTable> allPPLTables) {
		pplData.setAllPPLTables(allPPLTables);

	}

	private void setAtomicChanges(ArrayList<AtomicChange> atomicChanges) {

		dbChangesData.setAtomicChanges(atomicChanges);

	}

	private void setAllTableChanges(TreeMap<String, TableChange> tableChanges) {

		dbChangesData.setTableChanges(tableChanges);

	}

	private void setAllPPLTransitions(TreeMap<Integer, PPLTransition> allPPLTransitions) {

		pplData.setAllPPLTransitions(allPPLTransitions);

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

	public ArrayList<AtomicChange> getAtomicChanges() {

		return dbChangesData.getAtomicChanges();

	}

	public TreeMap<String, TableChange> getAllTableChanges() {

		return dbChangesData.getTableChanges();

	}

	public TreeMap<String, TableChange> getTmpTableChanges() {

		return dbChangesData.getTableChanges();

	}

	public ArrayList<PhaseCollector> getPhaseCollectors() {
		return dataCollectorsManager.getPhaseCollectors();
	}

	public ArrayList<ClusterCollector> getClusterCollectors() {
		return dataCollectorsManager.getClusterCollectors();
	}

	public void printInfo() {
		pplData.printInfo();
	}
	
	public TreeConstructionGeneral createTreeConstructionGeneral() {
		return new TreeConstructionGeneral(this);
	}
	
	public void populateWithPhases(PhaseAnalyzerMainEngine mainEngine,
			Integer numberOfPhases) {
		mainEngine.parseInput();
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(this);
		setPhaseCollectors(mainEngine.getPhaseCollectors());
	}

}
