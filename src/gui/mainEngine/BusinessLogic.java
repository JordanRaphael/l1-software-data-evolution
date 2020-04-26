package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import org.antlr.v4.runtime.RecognitionException;

import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;
import data.treeElements.TreeConstructionGeneral;
import data.treeElements.TreeConstructionPhases;
import data.treeElements.TreeConstructionPhasesWithClusters;
import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.ParametersJDialog;
import gui.dialogs.ProjectInfoDialog;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class BusinessLogic {

	public Gui gui;

	public BusinessLogic(Gui gui) {
		this.gui = gui;
	}

	protected void fillTree() {

		TreeConstructionGeneral tc = new TreeConstructionGeneral(this.gui.globalDataKeeper);
		this.gui.tablesTree = new JTree();
		this.gui.tablesTree = tc.constructTree();
		this.gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		});

		this.gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click Tree");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							gui.LifeTimeTable.repaint();

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(gui.tablesTree, e.getX(), e.getY());

				}

			}
		});

		this.gui.treeScrollPane.setViewportView(this.gui.tablesTree);

		this.gui.treeScrollPane.setBounds(5, 5, 250, 170);
		this.gui.treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.gui.treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.gui.tablesTreePanel.add(this.gui.treeScrollPane);

		this.gui.treeLabel.setText("General Tree");

		this.gui.sideMenu.revalidate();
		this.gui.sideMenu.repaint();

	}

	protected void createProjectAction() {
		CreateProjectJDialog createProjectDialog = new CreateProjectJDialog("", "", "", "", "", "");

		createProjectDialog.setModal(true);

		createProjectDialog.setVisible(true);

		if (createProjectDialog.getConfirmation()) {

			createProjectDialog.setVisible(false);

			File file = createProjectDialog.getFile();
			System.out.println(file.toString());
			this.gui.project = file.getName();
			String fileName = file.toString();
			System.out.println("!!" + this.gui.project);

			try {
				importData(fileName);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
				return;
			} catch (RecognitionException e) {

				JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
				return;
			}

		}

	}

	protected void infoAction() {
		if (!(this.gui.currentProject == null)) {

			System.out.println("Project Name:" + this.gui.projectName);
			System.out.println("Dataset txt:" + this.gui.datasetTxt);
			System.out.println("Input Csv:" + this.gui.inputCsv);
			System.out.println("Output Assessment1:" + this.gui.outputAssessment1);
			System.out.println("Output Assessment2:" + this.gui.outputAssessment2);
			System.out.println("Transitions File:" + this.gui.transitionsFile);

			System.out.println("Schemas:" + this.gui.globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("Transitions:" + this.gui.globalDataKeeper.getAllPPLTransitions().size());
			System.out.println("Tables:" + this.gui.globalDataKeeper.getAllPPLTables().size());

			ProjectInfoDialog infoDialog = new ProjectInfoDialog(this.gui.projectName, this.gui.datasetTxt,
					this.gui.inputCsv, this.gui.transitionsFile, this.gui.globalDataKeeper.getAllPPLSchemas().size(),
					this.gui.globalDataKeeper.getAllPPLTransitions().size(),
					this.gui.globalDataKeeper.getAllPPLTables().size());

			infoDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

	protected void showGeneralLifetimePhasesWithClustersPLDAction() {
		this.gui.wholeCol = -1;
		if (!(this.gui.project == null)) {

			ParametersJDialog jD = new ParametersJDialog(true);

			jD.setModal(true);

			jD.setVisible(true);

			if (jD.getConfirmation()) {

				this.gui.timeWeight = jD.getTimeWeight();
				this.gui.changeWeight = jD.getChangeWeight();
				this.gui.preProcessingTime = jD.getPreProcessingTime();
				this.gui.preProcessingChange = jD.getPreProcessingChange();
				this.gui.numberOfPhases = jD.getNumberOfPhases();
				this.gui.numberOfClusters = jD.getNumberOfClusters();
				this.gui.birthWeight = jD.geBirthWeight();
				this.gui.deathWeight = jD.getDeathWeight();
				this.gui.changeWeightCl = jD.getChangeWeightCluster();

				System.out.println(this.gui.timeWeight + " " + this.gui.changeWeight);

				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,
						this.gui.outputAssessment1, this.gui.outputAssessment2, this.gui.timeWeight,
						this.gui.changeWeight, this.gui.preProcessingTime, this.gui.preProcessingChange);

				mainEngine.parseInput();
				System.out.println("\n\n\n");
				mainEngine.extractPhases(this.gui.numberOfPhases);
				mainEngine.connectTransitionsWithPhases(this.gui.globalDataKeeper);
				this.gui.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
				TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.gui.globalDataKeeper,
						this.gui.birthWeight, this.gui.deathWeight, this.gui.changeWeightCl);
				mainEngine2.extractClusters(this.gui.numberOfClusters);
				this.gui.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
				mainEngine2.print();

				if (this.gui.globalDataKeeper.getPhaseCollectors().size() != 0) {
					TableConstructionWithClusters table = new TableConstructionWithClusters(this.gui.globalDataKeeper);
					final String[] columns = table.constructColumns();
					final String[][] rows = table.constructRows();
					this.gui.segmentSize = table.getSegmentSize();
					System.out.println("Schemas: " + this.gui.globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: " + columns.length + " R: " + rows.length);

					this.gui.finalColumns = columns;
					this.gui.finalRows = rows;
					this.gui.tabbedPane.setSelectedIndex(0);
					this.gui.makeGeneralTablePhases();
					fillClustersTree();
				} else {
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		} else {

			JOptionPane.showMessageDialog(null, "Please select a project first!");

		}

	}

	protected void showGeneralLifetimePhasesPLDAction() {
		if (!(this.gui.project == null)) {
			this.gui.wholeCol = -1;
			ParametersJDialog jD = new ParametersJDialog(false);

			jD.setModal(true);

			jD.setVisible(true);

			if (jD.getConfirmation()) {

				this.gui.timeWeight = jD.getTimeWeight();
				this.gui.changeWeight = jD.getChangeWeight();
				this.gui.preProcessingTime = jD.getPreProcessingTime();
				this.gui.preProcessingChange = jD.getPreProcessingChange();
				this.gui.numberOfPhases = jD.getNumberOfPhases();

				System.out.println(this.gui.timeWeight + " " + this.gui.changeWeight);

				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,
						this.gui.outputAssessment1, this.gui.outputAssessment2, this.gui.timeWeight,
						this.gui.changeWeight, this.gui.preProcessingTime, this.gui.preProcessingChange);

				mainEngine.parseInput();
				System.out.println("\n\n\n");
				mainEngine.extractPhases(this.gui.numberOfPhases);
				mainEngine.connectTransitionsWithPhases(this.gui.globalDataKeeper);
				this.gui.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());

				if (this.gui.globalDataKeeper.getPhaseCollectors().size() != 0) {
					TableConstructionPhases table = new TableConstructionPhases(this.gui.globalDataKeeper);
					final String[] columns = table.constructColumns();
					final String[][] rows = table.constructRows();
					this.gui.segmentSize = table.getSegmentSize();
					System.out.println("Schemas: " + this.gui.globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: " + columns.length + " R: " + rows.length);

					this.gui.finalColumns = columns;
					this.gui.finalRows = rows;
					this.gui.tabbedPane.setSelectedIndex(0);
					this.gui.makeGeneralTablePhases();
					fillPhasesTree();
				} else {
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		} else {

			JOptionPane.showMessageDialog(null, "Please select a project first!");

		}
	}

	protected void fillClustersTree() {

		TreeConstructionPhasesWithClusters tc = new TreeConstructionPhasesWithClusters(this.gui.globalDataKeeper);
		this.gui.tablesTree = tc.constructTree();

		this.gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		});

		this.gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click Tree");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							gui.LifeTimeTable.repaint();

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(gui.tablesTree, e.getX(), e.getY());

				}

			}
		});

		this.gui.treeScrollPane.setViewportView(this.gui.tablesTree);

		this.gui.treeScrollPane.setBounds(5, 5, 250, 170);
		this.gui.treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.gui.treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.gui.tablesTreePanel.add(this.gui.treeScrollPane);

		this.gui.treeLabel.setText("Clusters Tree");

		this.gui.sideMenu.revalidate();
		this.gui.sideMenu.repaint();

	}

	protected void showGeneralLifetimeIDUAction() {
		if (!(this.gui.currentProject == null)) {
			this.gui.zoomInButton.setVisible(true);
			this.gui.zoomOutButton.setVisible(true);
			TableConstructionIDU table = new TableConstructionIDU(this.gui.globalDataKeeper);
			final String[] columns = table.constructColumns();
			final String[][] rows = table.constructRows();
			this.gui.segmentSizeZoomArea = table.getSegmentSize();
			System.out.println("Schemas: " + this.gui.globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("C: " + columns.length + " R: " + rows.length);

			this.gui.finalColumnsZoomArea = columns;
			this.gui.finalRowsZoomArea = rows;
			this.gui.tabbedPane.setSelectedIndex(0);
			makeGeneralTableIDU();
			fillTree();

		} else {
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

	public void fillPhasesTree() {

		TreeConstructionPhases tc = new TreeConstructionPhases(this.gui.globalDataKeeper);
		this.gui.tablesTree = tc.constructTree();

		this.gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		});

		this.gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click Tree");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							gui.LifeTimeTable.repaint();

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(gui.tablesTree, e.getX(), e.getY());

				}

			}
		});

		this.gui.treeScrollPane.setViewportView(this.gui.tablesTree);
		this.gui.treeScrollPane.setBounds(5, 5, 250, 170);
		this.gui.treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.gui.treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.gui.tablesTreePanel.add(this.gui.treeScrollPane);

		this.gui.treeLabel.setText("Phases Tree");

		this.gui.sideMenu.revalidate();
		this.gui.sideMenu.repaint();

	}

	protected void fillTable() {
		TableConstructionIDU table = new TableConstructionIDU(this.gui.globalDataKeeper);
		final String[] columns = table.constructColumns();
		final String[][] rows = table.constructRows();
		this.gui.segmentSizeZoomArea = table.getSegmentSize();

		this.gui.finalColumnsZoomArea = columns;
		this.gui.finalRowsZoomArea = rows;
		this.gui.tabbedPane.setSelectedIndex(0);
		makeGeneralTableIDU();

		this.gui.timeWeight = (float) 0.5;
		this.gui.changeWeight = (float) 0.5;
		this.gui.preProcessingTime = false;
		this.gui.preProcessingChange = false;
		if (this.gui.globalDataKeeper.getAllPPLTransitions().size() < 56) {
			this.gui.numberOfPhases = 40;
		} else {
			this.gui.numberOfPhases = 56;
		}
		this.gui.numberOfClusters = 14;

		System.out.println(this.gui.timeWeight + " " + this.gui.changeWeight);

		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv, this.gui.outputAssessment1,
				this.gui.outputAssessment2, this.gui.timeWeight, this.gui.changeWeight, this.gui.preProcessingTime,
				this.gui.preProcessingChange);

		Double b = new Double(0.3);
		Double d = new Double(0.3);
		Double c = new Double(0.3);

		mainEngine.parseInput();
		System.out.println("\n\n\n");
		mainEngine.extractPhases(this.gui.numberOfPhases);

		mainEngine.connectTransitionsWithPhases(this.gui.globalDataKeeper);
		this.gui.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.gui.globalDataKeeper, b, d, c);
		mainEngine2.extractClusters(this.gui.numberOfClusters);
		this.gui.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
		mainEngine2.print();

		if (this.gui.globalDataKeeper.getPhaseCollectors().size() != 0) {
			TableConstructionWithClusters tableP = new TableConstructionWithClusters(this.gui.globalDataKeeper);
			final String[] columnsP = tableP.constructColumns();
			final String[][] rowsP = tableP.constructRows();
			this.gui.segmentSize = tableP.getSegmentSize();
			this.gui.finalColumns = columnsP;
			this.gui.finalRows = rowsP;
			this.gui.tabbedPane.setSelectedIndex(0);
			this.gui.makeGeneralTablePhases();
			fillClustersTree();
		}
		System.out.println("Schemas:" + this.gui.globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("Transitions:" + this.gui.globalDataKeeper.getAllPPLTransitions().size());
		System.out.println("Tables:" + this.gui.globalDataKeeper.getAllPPLTables().size());

	}

	protected void showLifetimeTableAction() {
		if (!(this.gui.currentProject == null)) {
			TableConstructionAllSquaresIncluded table = new TableConstructionAllSquaresIncluded(
					this.gui.globalDataKeeper);
			final String[] columns = table.constructColumns();
			final String[][] rows = table.constructRows();
			this.gui.segmentSizeDetailedTable = table.getSegmentSize();
			this.gui.tabbedPane.setSelectedIndex(0);
			this.gui.makeDetailedTable(columns, rows, true);
		} else {
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

	protected void editProjectAction() {

		String fileName = null;
		File dir = new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(this.gui, "Open");

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fcOpen1.getSelectedFile();
			System.out.println(file.toString());
			this.gui.project = file.getName();
			fileName = file.toString();
			System.out.println("!!" + this.gui.project);

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(fileName));
				String line;

				while (true) {
					line = br.readLine();
					if (line == null)
						break;
					if (line.contains("Project-name")) {
						String[] projectNameTable = line.split(":");
						this.gui.projectName = projectNameTable[1];
					} else if (line.contains("Dataset-txt")) {
						String[] datasetTxtTable = line.split(":");
						this.gui.datasetTxt = datasetTxtTable[1];
					} else if (line.contains("Input-csv")) {
						String[] inputCsvTable = line.split(":");
						this.gui.inputCsv = inputCsvTable[1];
					} else if (line.contains("Assessement1-output")) {
						String[] outputAss1 = line.split(":");
						this.gui.outputAssessment1 = outputAss1[1];
					} else if (line.contains("Assessement2-output")) {
						String[] outputAss2 = line.split(":");
						this.gui.outputAssessment2 = outputAss2[1];
					} else if (line.contains("Transition-xml")) {
						String[] transitionXmlTable = line.split(":");
						this.gui.transitionsFile = transitionXmlTable[1];
					}

				}
				;

				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}

			System.out.println(this.gui.projectName);

			CreateProjectJDialog createProjectDialog = new CreateProjectJDialog(this.gui.projectName,
					this.gui.datasetTxt, this.gui.inputCsv, this.gui.outputAssessment1, this.gui.outputAssessment2,
					this.gui.transitionsFile);

			createProjectDialog.setModal(true);

			createProjectDialog.setVisible(true);

			if (createProjectDialog.getConfirmation()) {

				createProjectDialog.setVisible(false);

				file = createProjectDialog.getFile();
				System.out.println(file.toString());
				this.gui.project = file.getName();
				fileName = file.toString();
				System.out.println("!!" + this.gui.project);

				try {
					importData(fileName);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {

					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}

			}

		} else {
			return;
		}

	}

	protected void loadProjectAction() {

		String fileName = null;
		File dir = new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(this.gui, "Open");

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fcOpen1.getSelectedFile();
			System.out.println(file.toString());
			this.gui.project = file.getName();
			fileName = file.toString();
			System.out.println("!!" + this.gui.project);
			System.out.println(fileName);

		} else {
			return;
		}
		try {
			importData(fileName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		} catch (RecognitionException e) {

			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}

	}

	public void zoomInAction() {
		gui.rowHeight = gui.rowHeight + 2;
		gui.columnWidth = gui.columnWidth + 1;
		gui.zoomAreaTable.setZoom(gui.rowHeight, gui.columnWidth);
	}

	public void zoomOutAction() {
		this.gui.rowHeight = this.gui.rowHeight - 2;
		this.gui.columnWidth = this.gui.columnWidth - 1;
		if (this.gui.rowHeight < 1) {
			this.gui.rowHeight = 1;
		}
		if (this.gui.columnWidth < 1) {
			this.gui.columnWidth = 1;
		}
		this.gui.zoomAreaTable.setZoom(this.gui.rowHeight, this.gui.columnWidth);
	}

	
	public void importData(String fileName) throws IOException, RecognitionException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));

		String line;

		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			if (line.contains("Project-name")) {
				String[] projectNameTable = line.split(":");
				gui.projectName = projectNameTable[1];
			} else if (line.contains("Dataset-txt")) {
				String[] datasetTxtTable = line.split(":");
				gui.datasetTxt = datasetTxtTable[1];
			} else if (line.contains("Input-csv")) {
				String[] inputCsvTable = line.split(":");
				gui.inputCsv = inputCsvTable[1];
			} else if (line.contains("Assessement1-output")) {
				String[] outputAss1 = line.split(":");
				gui.outputAssessment1 = outputAss1[1];
			} else if (line.contains("Assessement2-output")) {
				String[] outputAss2 = line.split(":");
				gui.outputAssessment2 = outputAss2[1];
			} else if (line.contains("Transition-xml")) {
				String[] transitionXmlTable = line.split(":");
				gui.transitionsFile = transitionXmlTable[1];
			}

		}
		;

		br.close();

		System.out.println("Project Name:" + gui.projectName);
		System.out.println("Dataset txt:" + gui.datasetTxt);
		System.out.println("Input Csv:" + gui.inputCsv);
		System.out.println("Output Assessment1:" + gui.outputAssessment1);
		System.out.println("Output Assessment2:" + gui.outputAssessment2);
		System.out.println("Transitions File:" + gui.transitionsFile);

		this.gui.globalDataKeeper = new GlobalDataKeeper(gui.datasetTxt, gui.transitionsFile);
		this.gui.globalDataKeeper.setData();
		System.out.println(this.gui.globalDataKeeper.getAllPPLTables().size());

		System.out.println(fileName);

		fillTable();
		fillTree();

		gui.currentProject = fileName;

	}
	
	
	protected void makeGeneralTableIDU() {
		
		PldRowSorter sorter=new PldRowSorter(gui.finalRowsZoomArea, gui.globalDataKeeper);
		
		gui.finalRowsZoomArea = sorter.sortRows();
	    
		gui.showingPld = true;
		gui.zoomInButton.setVisible(true);
		gui.zoomOutButton.setVisible(true);
		
		gui.showThisToPopup.setVisible(true);
		
		int numberOfColumns = gui.finalRowsZoomArea[0].length;
		int numberOfRows = gui.finalRowsZoomArea.length;
		
		gui.selectedRows = new ArrayList<Integer>();
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0] = gui.finalRowsZoomArea[i][0];
			
		}
		
		gui.zoomModel = new MyTableModel(gui.finalColumnsZoomArea, rows);
		
		final JvTable generalTable=new JvTable(gui.zoomModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(gui.rowHeight < 1){
			gui.rowHeight = 1;
		}
		if (gui.columnWidth < 1) {
			gui.columnWidth = 1;
		}
		
		for(int i=0; i<generalTable.getRowCount(); i++){
				generalTable.setRowHeight(i, gui.rowHeight);
				
		}

		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(gui.columnWidth);
				
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(gui.columnWidth);
				
			}
		}
		
		int start=-1;
		int end=-1;
		if (gui.globalDataKeeper.getPhaseCollectors()!=null && gui.wholeCol!=-1 && gui.wholeCol!=0) {
			start = gui.globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(gui.wholeCol-1).getStartPos();
			end = gui.globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(gui.wholeCol-1).getEndPos();
		}


		if(gui.wholeCol!=-1){
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){
			
						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		final IDUTableRenderer renderer = new IDUTableRenderer(gui, gui.finalRowsZoomArea, gui.globalDataKeeper, gui.segmentSize);
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue = gui.finalRowsZoomArea[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        if(column == gui.wholeColZoomArea && gui.wholeColZoomArea!=0){
		        	
		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
		        	description=description+"Old Version Name:" + gui.globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:" + gui.globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		
        			description=description+"Transition Changes:" + gui.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
        			description=description+"Additions:" + gui.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
        			description=description+"Deletions:" + gui.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
        			description=description+"Updates:" + gui.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

        			
        			gui.descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(gui.selectedColumnZoomArea==0){
		    		
		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		
		        		String description="Table:" + gui.finalRowsZoomArea[row][0]+"\n";
		        		description = description+"Birth Version Name:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description = description+"Birth Version ID:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description = description+"Death Version Name:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description = description+"Death Version ID:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description = description+"Total Changes:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getTotalChanges()+"\n";

		        		gui.descriptionText.setText(description);
		        		
		        		return c;
		        		
		        	}
		        }
		        else{

		        	if( gui.selectedFromTree.contains(gui.finalRowsZoomArea[row][0])){

		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        				      		        	
		        	if (isSelected && hasFocus){

		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
			        		description="Table:" + gui.finalRowsZoomArea[row][0]+"\n";
			        		
			        		description=description+"Old Version Name:" + gui.globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version Name:" + gui.globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
			        		if(gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
			        			description=description+"Transition Changes:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:" + gui.globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			
			        		}
			        		else{
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";
			        			
			        		}
			        		
			        		gui.descriptionText.setText(description);
		        		}
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
			        }
		        }

		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		}
	        		else if(numericValue > 0 && numericValue <= gui.segmentSizeZoomArea[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue > gui.segmentSizeZoomArea[3] && numericValue <= 2 * gui.segmentSizeZoomArea[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue > 2*gui.segmentSizeZoomArea[3] && numericValue <= 3*gui.segmentSizeZoomArea[3]){
	        			
	        			insersionColor=new Color(28,134,238);
	        		}
	        		else{
	        			insersionColor=new Color(16,78,139);
	        		}
	        		c.setBackground(insersionColor);
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		

	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.GRAY);
	        			return c; 
	        		}
	        		else{
	        			if(columnName.contains("v")){
	        				c.setBackground(Color.lightGray);
	        				setToolTipText(columnName);
	        			}
	        			else{
	        				Color tableNameColor=new Color(205,175,149);
	        				c.setBackground(tableNameColor);
	        			}
		        		return c; 
	        		}
		        }
		    }
		});
				
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumnZoomArea = target.getSelectedColumn();
			         renderer.setSelCol(gui.selectedColumnZoomArea);
			         target.getSelectedColumns();
			         
			         gui.zoomAreaTable.repaint();
				}
				
			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						target1.getSelectedColumns();
						gui.selectedRowsFromMouse = target1.getSelectedRows();
						System.out.println(target1.getSelectedColumns().length);
						System.out.println(target1.getSelectedRow());
						for(int rowsSelected=0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++){
							System.out.println(generalTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	gui.selectedFromTree=new ArrayList<String>();
				            	gui.zoomAreaTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						        						        
						    
					}
				
			   }
		});
		
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	gui.wholeColZoomArea = generalTable.columnAtPoint(e.getPoint());
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        generalTable.repaint();
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	gui.wholeColZoomArea = -1;
							        renderer.setWholeCol(gui.wholeColZoomArea);

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
		   }
		    
		});
		
		gui.zoomAreaTable = generalTable;
		gui.tmpScrollPaneZoomArea.setViewportView(gui.zoomAreaTable);
		gui.tmpScrollPaneZoomArea.setAlignmentX(0);
		gui.tmpScrollPaneZoomArea.setAlignmentY(0);
		gui.tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		gui.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gui.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		gui.lifeTimePanel.setCursor(gui.getCursor());
		gui.lifeTimePanel.add(gui.tmpScrollPaneZoomArea);
		
	}

}
