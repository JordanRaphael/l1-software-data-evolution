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

}
