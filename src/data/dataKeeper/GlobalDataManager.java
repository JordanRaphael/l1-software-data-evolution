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
import phaseAnalyzer.commons.PhaseCollector;
import tableClustering.clusterExtractor.commons.ClusterCollector;

public class GlobalDataManager {

	private PPLData pplData;
	private DBChangesData dbChangesData;
	private CollectorsData collectorsData;
	
	private ProjectDetailsData projectDetailsData;
	private DataManipulator dataManipulator;

	public GlobalDataManager(String filename, String transitionsFile) {
		projectDetailsData = new ProjectDetailsData(filename, transitionsFile);
		dataManipulator = new DataManipulator(this);
		dbChangesData = new DBChangesData();
		pplData = new PPLData();
	}

	public GlobalDataManager() {

	}

	public void setData() {

		Worker worker = new Worker(projectDetailsData.getFilename(), projectDetailsData.getTransitionsFile());
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
		projectDetailsData.setDataFolder(worker.getDataFolder());

	}

	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors) {
		collectorsData.setPhaseCollectors(phaseCollectors);
	}

	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors) {
		collectorsData.setClusterCollectors(clusterCollectors);
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
		return collectorsData.getPhaseCollectors();
	}

	public ArrayList<ClusterCollector> getClusterCollectors() {
		return collectorsData.getClusterCollectors();
	}

	public DataManipulator getDataManipulator() {
		return dataManipulator;
	}

	public void printInfo() {
		System.out.println("Schemas:" + pplData.getAllPPLSchemas().size());
		System.out.println("Transitions:" + pplData.getAllPPLTransitions().size());
		System.out.println("Tables:" + pplData.getAllPPLTables().size());
	}

}
