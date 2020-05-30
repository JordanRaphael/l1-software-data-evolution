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

public class GlobalDataKeeper {

	private TreeMap<String, PPLSchema> allPPLSchemas = null;
	private TreeMap<String, PPLTable> allPPLTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String, TableChange> tableChanges = null;
	private TreeMap<String, TableChange> tableChangesForTwo = null;
	private TreeMap<Integer, PPLTransition> allPPLTransitions = null;
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;

	private String projectDataFolder = null;
	private String filename = null;
	private String transitionsFile = "";

	public GlobalDataKeeper(String fl, String transitionsFile) {
		allPPLSchemas = new TreeMap<String, PPLSchema>();
		allPPLTables = new TreeMap<String, PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String, TableChange>();
		tableChangesForTwo = new TreeMap<String, TableChange>();
		allPPLTransitions = new TreeMap<Integer, PPLTransition>();
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
		filename = fl;
		this.transitionsFile = transitionsFile;
	}

	public GlobalDataKeeper() {

	}

	public void setData() {

		Worker w = new Worker(filename, transitionsFile);
		try {
			w.work();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setAllPPLSchemas(w.getAllPPLSchemas());
		setAllPPLTables(w.getAllPPLTables());
		setAllPPLTransitions(w.getAllPPLTransitions());
		setAllTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setDataFolder(w.getDataFolder());

	}

	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors) {
		this.phaseCollectors = phaseCollectors;
	}

	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors) {
		this.clusterCollectors = clusterCollectors;
	}

	private void setAllPPLSchemas(TreeMap<String, PPLSchema> tmpAllPPLSchemas) {

		allPPLSchemas = tmpAllPPLSchemas;

	}

	private void setAllPPLTables(TreeMap<String, PPLTable> tmpAllTables) {
		allPPLTables = tmpAllTables;

	}

	private void setAtomicChanges(ArrayList<AtomicChange> tmpAtomicChanges) {

		atomicChanges = tmpAtomicChanges;

	}

	private void setAllTableChanges(TreeMap<String, TableChange> tmpTableChanges) {

		tableChanges = tmpTableChanges;

	}

	private void setAllPPLTransitions(TreeMap<Integer, PPLTransition> tmpAllPPLTransitions) {

		allPPLTransitions = tmpAllPPLTransitions;

	}

	private void setDataFolder(String tmpProjectDataFolder) {
		projectDataFolder = tmpProjectDataFolder;
	}

	public TreeMap<String, PPLSchema> getAllPPLSchemas() {

		return allPPLSchemas;

	}

	public TreeMap<String, PPLTable> getAllPPLTables() {

		return allPPLTables;

	}

	public ArrayList<AtomicChange> getAtomicChanges() {

		return atomicChanges;

	}

	public TreeMap<String, TableChange> getAllTableChanges() {

		return tableChanges;

	}

	public TreeMap<String, TableChange> getTmpTableChanges() {

		return tableChangesForTwo;

	}

	public TreeMap<Integer, PPLTransition> getAllPPLTransitions() {

		return allPPLTransitions;

	}

	public String getDataFolder() {
		return projectDataFolder;
	}

	public ArrayList<PhaseCollector> getPhaseCollectors() {
		return phaseCollectors;
	}

	public ArrayList<ClusterCollector> getClusterCollectors() {
		return clusterCollectors;
	}

	public void printInfo() {
		System.out.println("Schemas:" + allPPLSchemas.size());
		System.out.println("Transitions:" + allPPLTransitions.size());
		System.out.println("Tables:" + allPPLTables.size());
	}

	public PldConstruction showClusterSelectionToZoomArea(ArrayList<String> selectedTables, int selectedColumn) {
		ArrayList<String> tablesOfCluster = new ArrayList<String>();
		for (int i = 0; i < selectedTables.size(); i++) {
			String[] selectedClusterSplit = selectedTables.get(i).split(" ");
			int cluster = Integer.parseInt(selectedClusterSplit[1]);
			ArrayList<String> namesOfTables = clusterCollectors.get(0).getClusters().get(cluster).getNamesOfTables();
			for (int j = 0; j < namesOfTables.size(); j++) {
				tablesOfCluster.add(namesOfTables.get(j));
			}
			System.out.println(selectedTables.get(i));
		}

		PldConstruction table;
		if (selectedColumn == 0) {
			table = new TableConstructionClusterTablesPhasesZoomA(allPPLSchemas, phaseCollectors.get(0).getPhases(),
					tablesOfCluster);
		} else {
			table = createTableConstructionZoomArea(tablesOfCluster, selectedColumn);
		}
		System.out.println("Schemas: " + allPPLSchemas.size());
		System.out.println("C: " + table.constructColumns().length + " R: " + table.constructRows().length);

		return table;
	}

	public TableConstructionPhases createTableConstructionPhases() {
		return new TableConstructionPhases(allPPLSchemas, getPhaseCollectors().get(0).getPhases());
	}

	public TreeConstructionPhasesWithClusters createTreeConstructionPhasesWithClusters() {
		return new TreeConstructionPhasesWithClusters(this);

	}

	public TableConstructionIDU createTableConstructionIDU() {
		return new TableConstructionIDU(allPPLSchemas, allPPLTransitions);
	}

	public TableConstructionWithClusters createTableConstructionWithClusters() {
		return new TableConstructionWithClusters(phaseCollectors.get(0).getPhases(),
				getClusterCollectors().get(0).getClusters());
	}

	public TreeConstructionGeneral createTreeConstructionGeneral() {
		return new TreeConstructionGeneral(this);
	}

	public TableConstructionZoomArea createTableConstructionZoomArea(ArrayList<String> tablesOfCluster,
			int selectedColumn) {
		return new TableConstructionZoomArea(phaseCollectors.get(0).getPhases(), allPPLTransitions, allPPLSchemas,
				allPPLTables, tablesOfCluster, selectedColumn);
	}

	public TableConstructionAllSquaresIncluded createTableConstructionAllSquaresIncluded() {
		return new TableConstructionAllSquaresIncluded(allPPLSchemas, allPPLTransitions);
	}

	public TableConstructionClusterTablesPhasesZoomA createTableConstructionClusterTablesPhasesZoomA(
			ArrayList<String> tablesOfCluster) {
		return new TableConstructionClusterTablesPhasesZoomA(allPPLSchemas, getPhaseCollectors().get(0).getPhases(),
				tablesOfCluster);
	}

	public void populateWithPhases(PhaseAnalyzerMainEngine mainEngine, Integer numberOfPhases) {
		mainEngine.parseInput();
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(this);
		setPhaseCollectors(mainEngine.getPhaseCollectors());
	}

	public void populateWithClusters(TableClusteringMainEngine mainEngine, Integer numberOfClusters) {
		mainEngine.extractClusters(numberOfClusters);
		setClusterCollectors(mainEngine.getClusterCollectors());
		mainEngine.print();
	}

}
