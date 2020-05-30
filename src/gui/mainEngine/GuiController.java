package gui.mainEngine;

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
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.antlr.v4.runtime.RecognitionException;

import data.dataKeeper.DataManipulator;
import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;
import data.tableConstructors.PldConstruction;
import data.tableConstructors.TableConstructionAllSquaresIncluded;
import data.tableConstructors.TableConstructionIDU;
import data.tableConstructors.TableConstructionPhases;
import data.tableConstructors.TableConstructionWithClusters;
import data.tableConstructors.TableConstructionZoomArea;
import data.treeElements.TreeConstructionGeneral;
import data.treeElements.TreeConstructionPhases;
import data.treeElements.TreeConstructionPhasesWithClusters;
import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.ParametersJDialog;
import gui.dialogs.ProjectInfoDialog;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class GuiController {

	private Gui gui;
	private DataManipulator dataManipulator;
	private GlobalDataKeeper globalDataKeeper;
	private GeneralTableListenerHandler generalTableListenerHandler;
	private ZoomTableListenerHandler zoomTableListenerHandler;
	private TablesFactory tablesFactory;
	protected String[][] rowsZoom = null;
	protected final JvTable zoomTable = null;
	private IDUTableRenderer renderer;
	private JItemsHandler jItemsHandler;

	public GuiController(Gui gui) {
		this.dataManipulator = new DataManipulator();
		this.gui = gui;
		this.generalTableListenerHandler = new GeneralTableListenerHandler(this, gui);
		this.zoomTableListenerHandler = new ZoomTableListenerHandler(this, gui);
		this.jItemsHandler = new JItemsHandler();
		this.tablesFactory = new TablesFactory();
	}

	public GlobalDataKeeper getGlobalDataKeeper() {

		return globalDataKeeper;
	}

	public Gui getGui() {

		return gui;
	}

	public IDUTableRenderer getRenderer() {

		return renderer;
	}

	public GeneralTableListenerHandler getEventListenerHandler() {

		return generalTableListenerHandler;
	}

	public String[][] getRowsZoom() {

		return rowsZoom;
	}

	protected void notUniformlyDistributedButtonMouseListener() {
		gui.LifeTimeTable.notUniformlyDistributed(globalDataKeeper);
	}

	public void makeGeneralTablePhases() {
		gui.uniformlyDistributedButton.setVisible(true);

		gui.notUniformlyDistributedButton.setVisible(true);

		int numberOfColumns = gui.finalRows[0].length;
		int numberOfRows = gui.finalRows.length;

		gui.selectedRows = new ArrayList<Integer>();

		String[][] rows = new String[numberOfRows][numberOfColumns];

		for (int i = 0; i < numberOfRows; i++) {

			rows[i][0] = gui.finalRows[i][0];

		}

		gui.generalModel = new MyTableModel(gui.finalColumns, rows);

		final JvTable generalTable = new JvTable(gui.generalModel);

		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));

		for (int i = 0; i < generalTable.getColumnCount(); i++) {
			if (i == 0) {
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);
			} else {
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
			}
		}

		TableRenderer tableRenderer = tablesFactory.getTableType("General Table", this);
		generalTable.setDefaultRenderer(Object.class, tableRenderer.createTableCellRenderer());

		generalTable.addMouseListener(generalTableListenerHandler.createPhasesMouseClickedAdapter());

		generalTable.addMouseListener(generalTableListenerHandler.createPhasesMouseClickedButton3Adapter(generalTable));

		generalTable.getTableHeader()
				.addMouseListener(generalTableListenerHandler.createPhasesMouseColumnClickedAdapter(generalTable));

		generalTable.getTableHeader()
				.addMouseListener(generalTableListenerHandler.createPhasesRightMouseClickedAdapter(generalTable));

		gui.LifeTimeTable = generalTable;

		gui.tmpScrollPane.setViewportView(gui.LifeTimeTable);
		jItemsHandler.setJScrollPanePosition(gui.tmpScrollPane, 300, 30, 950, 265);

		gui.lifeTimePanel.setCursor(gui.getCursor());
		gui.lifeTimePanel.add(gui.tmpScrollPane);

	}

	protected void fillTree() {

		TreeConstructionGeneral tc = dataManipulator.createTreeConstructionGeneral(globalDataKeeper);

		gui.tablesTree = new JTree();
		gui.tablesTree = tc.constructTree();
		gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");
			}
		});

		gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
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

		gui.treeScrollPane.setViewportView(gui.tablesTree);
		jItemsHandler.setJScrollPanePosition(gui.treeScrollPane, 5, 5, 250, 170);

		gui.tablesTreePanel.add(gui.treeScrollPane);
		gui.treeLabel.setText("General Tree");

		gui.sideMenu.revalidate();
		gui.sideMenu.repaint();

	}

	protected void createProjectAction() {
		CreateProjectJDialog createProjectDialog = new CreateProjectJDialog("", "", "", "", "", "");

		createProjectDialog.setModal(true);
		createProjectDialog.setVisible(true);

		if (createProjectDialog.getConfirmation()) {

			createProjectDialog.setVisible(false);

			File file = createProjectDialog.getFile();
			System.out.println(file.toString());
			gui.project = file.getName();
			String fileName = file.toString();
			System.out.println("!!" + gui.project);

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
		if (!(gui.currentProject == null)) {

			System.out.println("Project Name:" + gui.projectName);
			System.out.println("Dataset txt:" + gui.datasetTxt);
			System.out.println("Input Csv:" + gui.inputCsv);
			System.out.println("Output Assessment1:" + gui.outputAssessment1);
			System.out.println("Output Assessment2:" + gui.outputAssessment2);
			System.out.println("Transitions File:" + gui.transitionsFile);

			globalDataKeeper.printInfo();

			ProjectInfoDialog infoDialog = new ProjectInfoDialog(gui.projectName, gui.datasetTxt, gui.inputCsv,
					gui.transitionsFile, globalDataKeeper.getAllPPLSchemas().size(),
					globalDataKeeper.getAllPPLTransitions().size(), globalDataKeeper.getAllPPLTables().size());

			infoDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

	protected void showGeneralLifetimePhasesWithClustersPLDAction() {
		gui.wholeCol = -1;
		if (!(gui.project == null)) {

			ParametersJDialog jD = new ParametersJDialog(true);

			jD.setModal(true);
			jD.setVisible(true);

			if (jD.getConfirmation()) {

				gui.timeWeight = jD.getTimeWeight();
				gui.changeWeight = jD.getChangeWeight();
				gui.preProcessingTime = jD.getPreProcessingTime();
				gui.preProcessingChange = jD.getPreProcessingChange();
				gui.numberOfPhases = jD.getNumberOfPhases();
				gui.numberOfClusters = jD.getNumberOfClusters();
				gui.birthWeight = jD.geBirthWeight();
				gui.deathWeight = jD.getDeathWeight();
				gui.changeWeightCl = jD.getChangeWeightCluster();

				System.out.println(gui.timeWeight + " " + gui.changeWeight);

				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(gui.inputCsv, gui.outputAssessment1,
						gui.outputAssessment2, gui.timeWeight, gui.changeWeight, gui.preProcessingTime,
						gui.preProcessingChange);

				dataManipulator.populateWithPhases(globalDataKeeper, mainEngine, gui.numberOfPhases);

				TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper, gui.birthWeight,
						gui.deathWeight, gui.changeWeightCl);

				dataManipulator.populateWithClusters(globalDataKeeper, mainEngine2, gui.numberOfClusters);

				if (globalDataKeeper.getPhaseCollectors().size() != 0) {
					TableConstructionWithClusters table = dataManipulator
							.createTableConstructionWithClusters(globalDataKeeper);
					final String[] columns = table.constructColumns();
					final String[][] rows = table.constructRows();
					gui.segmentSize = table.getSegmentSize();
					System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: " + columns.length + " R: " + rows.length);

					gui.finalColumns = columns;
					gui.finalRows = rows;
					gui.tabbedPane.setSelectedIndex(0);
					makeGeneralTablePhases();
					fillClustersTree();

				} else {
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		} else {

			JOptionPane.showMessageDialog(null, "Please select a project first!");

		}

	}

	protected void showClusterSelectionToZoomArea(int selectedColumn, String selectedCluster) {

		PldConstruction table = dataManipulator.showClusterSelectionToZoomArea(globalDataKeeper, gui.tablesSelected,
				selectedColumn);

		final String[] columns = table.constructColumns();
		final String[][] rows = table.constructRows();
		gui.segmentSizeZoomArea = table.getSegmentSize();
		gui.finalColumnsZoomArea = columns;
		gui.finalRowsZoomArea = rows;
		gui.tabbedPane.setSelectedIndex(0);

		makeZoomAreaTableForCluster();

	}

	protected void makeZoomAreaTable() {
		gui.showingPld = false;
		int numberOfColumns = gui.finalRowsZoomArea[0].length;
		int numberOfRows = gui.finalRowsZoomArea.length;

		rowsZoom = new String[numberOfRows][numberOfColumns];

		for (int i = 0; i < numberOfRows; i++) {

			rowsZoom[i][0] = gui.finalRowsZoomArea[i][0];
		}

		gui.zoomModel = new MyTableModel(gui.finalColumnsZoomArea, rowsZoom);

		final JvTable zoomTable = new JvTable(gui.zoomModel);

		zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		zoomTable.setShowGrid(false);
		zoomTable.setIntercellSpacing(new Dimension(0, 0));

		for (int i = 0; i < zoomTable.getColumnCount(); i++) {
			if (i == 0) {
				zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);

			} else {

				zoomTable.getColumnModel().getColumn(i).setPreferredWidth(20);
				zoomTable.getColumnModel().getColumn(i).setMaxWidth(20);
				zoomTable.getColumnModel().getColumn(i).setMinWidth(20);
			}
		}

		TableRenderer tableRenderer = tablesFactory.getTableType("Zoom Table", this);
		// zoomTable.setDefaultRenderer(Object.class,
		// zoomTableListenerHandler.createZoomAreaDefaultTableRenderer());
		zoomTable.setDefaultRenderer(Object.class, tableRenderer.createDefaultTableRenderer());

		zoomTable.addMouseListener(zoomTableListenerHandler.createZoomAreaMouseClickedHandler());

		zoomTable.addMouseListener(zoomTableListenerHandler.createZoomAreaRightClickHandler());

		zoomTable.getTableHeader()
				.addMouseListener(zoomTableListenerHandler.createZoomAreaColumnRightClickHandler(zoomTable));

		zoomTable.getTableHeader()
				.addMouseListener(zoomTableListenerHandler.createZoomAreaRightClickReleasedHandler(zoomTable));

		gui.zoomAreaTable = zoomTable;
		gui.tmpScrollPaneZoomArea.setViewportView(gui.zoomAreaTable);

		jItemsHandler.setJScrollPanePosition(gui.tmpScrollPaneZoomArea, 300, 300, 950, 250);

		gui.lifeTimePanel.setCursor(gui.getCursor());
		gui.lifeTimePanel.add(gui.tmpScrollPaneZoomArea);

	}

	protected void showGeneralLifetimePhasesPLDAction() {
		if (!(gui.project == null)) {
			gui.wholeCol = -1;
			ParametersJDialog jD = new ParametersJDialog(false);

			jD.setModal(true);

			jD.setVisible(true);

			if (jD.getConfirmation()) {

				gui.timeWeight = jD.getTimeWeight();
				gui.changeWeight = jD.getChangeWeight();
				gui.preProcessingTime = jD.getPreProcessingTime();
				gui.preProcessingChange = jD.getPreProcessingChange();
				gui.numberOfPhases = jD.getNumberOfPhases();

				System.out.println(gui.timeWeight + " " + gui.changeWeight);

				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(gui.inputCsv, gui.outputAssessment1,
						gui.outputAssessment2, gui.timeWeight, gui.changeWeight, gui.preProcessingTime,
						gui.preProcessingChange);

				dataManipulator.populateWithPhases(globalDataKeeper, mainEngine, gui.numberOfPhases);

				if (globalDataKeeper.getPhaseCollectors().size() != 0) {
					TableConstructionPhases table = dataManipulator.createTableConstructionPhases(globalDataKeeper);
					final String[] columns = table.constructColumns();
					final String[][] rows = table.constructRows();
					gui.segmentSize = table.getSegmentSize();
					System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: " + columns.length + " R: " + rows.length);

					gui.finalColumns = columns;
					gui.finalRows = rows;
					gui.tabbedPane.setSelectedIndex(0);
					makeGeneralTablePhases();
					fillPhasesTree();

				} else {
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		} else {

			JOptionPane.showMessageDialog(null, "Please select a project first!");

		}

	}

	public void fillClustersTree() {

		TreeConstructionPhasesWithClusters tc = dataManipulator
				.createTreeConstructionPhasesWithClusters(globalDataKeeper);
		gui.tablesTree = tc.constructTree();

		gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");
			}
		});

		gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
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

		gui.treeScrollPane.setViewportView(gui.tablesTree);
		jItemsHandler.setJScrollPanePosition(gui.treeScrollPane, 5, 5, 250, 170);

		gui.tablesTreePanel.add(gui.treeScrollPane);
		gui.treeLabel.setText("Clusters Tree");

		gui.sideMenu.revalidate();
		gui.sideMenu.repaint();

	}

	public void showGeneralLifetimeIDUAction() {
		if (!(gui.currentProject == null)) {
			gui.zoomInButton.setVisible(true);
			gui.zoomOutButton.setVisible(true);

			TableConstructionIDU table = dataManipulator.createTableConstructionIDU(globalDataKeeper);

			final String[] columns = table.constructColumns();
			final String[][] rows = table.constructRows();
			gui.segmentSizeZoomArea = table.getSegmentSize();
			System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("C: " + columns.length + " R: " + rows.length);

			gui.finalColumnsZoomArea = columns;
			gui.finalRowsZoomArea = rows;
			gui.tabbedPane.setSelectedIndex(0);
			makeGeneralTableIDU();
			fillTree();

		} else {
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

	public void fillPhasesTree() {

		TreeConstructionPhases tc = new TreeConstructionPhases(this.globalDataKeeper);
		gui.tablesTree = tc.constructTree();

		gui.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");
			}
		});

		gui.tablesTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
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

		gui.treeScrollPane.setViewportView(gui.tablesTree);
		jItemsHandler.setJScrollPanePosition(gui.treeScrollPane, 5, 5, 250, 170);

		gui.tablesTreePanel.add(gui.treeScrollPane);
		gui.treeLabel.setText("Phases Tree");

		gui.sideMenu.revalidate();
		gui.sideMenu.repaint();

	}

	protected void fillTable() {
		TableConstructionIDU table = dataManipulator.createTableConstructionIDU(globalDataKeeper);
		final String[] columns = table.constructColumns();
		final String[][] rows = table.constructRows();
		gui.segmentSizeZoomArea = table.getSegmentSize();

		gui.finalColumnsZoomArea = columns;
		gui.finalRowsZoomArea = rows;
		gui.tabbedPane.setSelectedIndex(0);
		makeGeneralTableIDU();

		gui.timeWeight = (float) 0.5;
		gui.changeWeight = (float) 0.5;
		gui.preProcessingTime = false;
		gui.preProcessingChange = false;
		if (globalDataKeeper.getAllPPLTransitions().size() < 56) {
			gui.numberOfPhases = 40;
		} else {
			gui.numberOfPhases = 56;
		}
		gui.numberOfClusters = 14;

		System.out.println(gui.timeWeight + " " + gui.changeWeight);

		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(gui.inputCsv, gui.outputAssessment1,
				gui.outputAssessment2, gui.timeWeight, gui.changeWeight, gui.preProcessingTime,
				gui.preProcessingChange);

		dataManipulator.populateWithPhases(globalDataKeeper, mainEngine, gui.numberOfPhases);

		Double b = new Double(0.3);
		Double d = new Double(0.3);
		Double c = new Double(0.3);

		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper, b, d, c);

		dataManipulator.populateWithClusters(globalDataKeeper, mainEngine2, gui.numberOfClusters);

		if (globalDataKeeper.getPhaseCollectors().size() != 0) {
			TableConstructionWithClusters tableP = dataManipulator
					.createTableConstructionWithClusters(globalDataKeeper);
			final String[] columnsP = tableP.constructColumns();
			final String[][] rowsP = tableP.constructRows();
			gui.segmentSize = tableP.getSegmentSize();
			gui.finalColumns = columnsP;
			gui.finalRows = rowsP;
			gui.tabbedPane.setSelectedIndex(0);
			makeGeneralTablePhases();
			fillClustersTree();
		}
		System.out.println("Schemas:" + globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("Transitions:" + globalDataKeeper.getAllPPLTransitions().size());
		System.out.println("Tables:" + globalDataKeeper.getAllPPLTables().size());

	}

	protected void showLifetimeTableAction() {

		if (!(gui.currentProject == null)) {

			TableConstructionAllSquaresIncluded table = dataManipulator
					.createTableConstructionAllSquaresIncluded(globalDataKeeper);
			final String[] columns = table.constructColumns();
			final String[][] rows = table.constructRows();
			gui.segmentSizeDetailedTable = table.getSegmentSize();
			gui.tabbedPane.setSelectedIndex(0);
			gui.makeDetailedTable(columns, rows, true);

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
		int returnVal = fcOpen1.showDialog(gui, "Open");

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fcOpen1.getSelectedFile();
			System.out.println(file.toString());
			gui.project = file.getName();
			fileName = file.toString();
			System.out.println("!!" + gui.project);

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
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}

			System.out.println(gui.projectName);

			CreateProjectJDialog createProjectDialog = new CreateProjectJDialog(gui.projectName, gui.datasetTxt,
					gui.inputCsv, gui.outputAssessment1, gui.outputAssessment2, gui.transitionsFile);

			createProjectDialog.setModal(true);
			createProjectDialog.setVisible(true);

			if (createProjectDialog.getConfirmation()) {

				createProjectDialog.setVisible(false);

				file = createProjectDialog.getFile();
				System.out.println(file.toString());
				gui.project = file.getName();
				fileName = file.toString();
				System.out.println("!!" + gui.project);

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
		int returnVal = fcOpen1.showDialog(gui, "Open");

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fcOpen1.getSelectedFile();
			System.out.println(file.toString());
			gui.project = file.getName();
			fileName = file.toString();
			System.out.println("!!" + gui.project);
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
		gui.rowHeight = gui.rowHeight - 2;
		gui.columnWidth = gui.columnWidth - 1;
		if (gui.rowHeight < 1) {
			gui.rowHeight = 1;
		}
		if (gui.columnWidth < 1) {
			gui.columnWidth = 1;
		}
		gui.zoomAreaTable.setZoom(gui.rowHeight, gui.columnWidth);
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

		globalDataKeeper = new GlobalDataKeeper(gui.datasetTxt, gui.transitionsFile);
		globalDataKeeper.setData();
		System.out.println(globalDataKeeper.getAllPPLTables().size());

		System.out.println(fileName);

		fillTable();
		fillTree();

		gui.currentProject = fileName;

	}

	public void makeGeneralTableIDU() {

		PldRowSorter sorter = new PldRowSorter(gui.finalRowsZoomArea, globalDataKeeper);
		gui.finalRowsZoomArea = sorter.sortRows();

		gui.showingPld = true;
		gui.zoomInButton.setVisible(true);
		gui.zoomOutButton.setVisible(true);

		gui.showThisToPopup.setVisible(true);

		int numberOfColumns = gui.finalRowsZoomArea[0].length;
		int numberOfRows = gui.finalRowsZoomArea.length;

		gui.selectedRows = new ArrayList<Integer>();

		String[][] rows = new String[numberOfRows][numberOfColumns];

		for (int i = 0; i < numberOfRows; i++) {
			rows[i][0] = gui.finalRowsZoomArea[i][0];
		}

		gui.zoomModel = new MyTableModel(gui.finalColumnsZoomArea, rows);

		final JvTable generalTable = new JvTable(gui.zoomModel);
		gui.generalTable = generalTable;

		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		if (gui.rowHeight < 1) {
			gui.rowHeight = 1;
		}
		if (gui.columnWidth < 1) {
			gui.columnWidth = 1;
		}

		for (int i = 0; i < generalTable.getRowCount(); i++) {
			generalTable.setRowHeight(i, gui.rowHeight);

		}

		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));

		for (int i = 0; i < generalTable.getColumnCount(); i++) {
			if (i == 0) {
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(gui.columnWidth);

			} else {
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(gui.columnWidth);
			}
		}

		int start = -1;
		int end = -1;
		if (globalDataKeeper.getPhaseCollectors() != null && gui.wholeCol != -1 && gui.wholeCol != 0) {
			start = globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(gui.wholeCol - 1).getStartPos();
			end = globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(gui.wholeCol - 1).getEndPos();
		}

		if (gui.wholeCol != -1) {
			for (int i = 0; i < generalTable.getColumnCount(); i++) {
				if (!(generalTable.getColumnName(i).equals("Table name"))) {
					if (Integer.parseInt(generalTable.getColumnName(i)) >= start
							&& Integer.parseInt(generalTable.getColumnName(i)) <= end) {

						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());

					}
				}
			}
		}

		renderer = new IDUTableRenderer(gui, gui.finalRowsZoomArea, globalDataKeeper, gui.segmentSize);

		TableRenderer tableRenderer = tablesFactory.getTableType("General Table", this);
		generalTable.setDefaultRenderer(Object.class, tableRenderer.createDefaultTableRenderer());

		generalTable.addMouseListener(generalTableListenerHandler.createIDUOneMouseClickAdapter(renderer));

		generalTable.addMouseListener(generalTableListenerHandler.createIDURightClickRowAdapter(generalTable));

		generalTable.getTableHeader()
				.addMouseListener(generalTableListenerHandler.createIDUMouseEvent(generalTable, renderer));

		generalTable.getTableHeader()
				.addMouseListener(generalTableListenerHandler.createIDURightClickAdapter(generalTable, renderer));

		gui.zoomAreaTable = generalTable;
		gui.tmpScrollPaneZoomArea.setViewportView(gui.zoomAreaTable);
		jItemsHandler.setJScrollPanePosition(gui.tmpScrollPaneZoomArea, 300, 300, 950, 250);

		gui.lifeTimePanel.setCursor(gui.getCursor());
		gui.lifeTimePanel.add(gui.tmpScrollPaneZoomArea);

	}

	public void showSelectionToZoomArea(int selectedColumn) {

		System.out.println("selectedColumn " + selectedColumn + " tablesSelected " + gui.tablesSelected);
		TableConstructionZoomArea table = dataManipulator.createTableConstructionZoomArea(globalDataKeeper,
				gui.tablesSelected, selectedColumn);
		final String[] columns = table.constructColumns();
		final String[][] rows = table.constructRows();
		gui.segmentSizeZoomArea = table.getSegmentSize();

		System.out.println("Schemas: " + globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: " + columns.length + " R: " + rows.length);

		gui.finalColumnsZoomArea = columns;
		gui.finalRowsZoomArea = rows;
		gui.tabbedPane.setSelectedIndex(0);
		makeZoomAreaTable();

	}

	protected void makeZoomAreaTableForCluster() {
		gui.showingPld = false;
		int numberOfColumns = gui.finalRowsZoomArea[0].length;
		int numberOfRows = gui.finalRowsZoomArea.length;
		gui.undoButton.setVisible(true);

		rowsZoom = new String[numberOfRows][numberOfColumns];

		for (int i = 0; i < numberOfRows; i++) {

			rowsZoom[i][0] = gui.finalRowsZoomArea[i][0];

		}

		gui.zoomModel = new MyTableModel(gui.finalColumnsZoomArea, rowsZoom);

		final JvTable zoomTable = new JvTable(gui.zoomModel);

		zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		zoomTable.setShowGrid(false);
		zoomTable.setIntercellSpacing(new Dimension(0, 0));

		for (int i = 0; i < zoomTable.getColumnCount(); i++) {
			if (i == 0) {
				zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);

			} else {

				zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
				zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
				zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
			}
		}

		TableRenderer tableRenderer = tablesFactory.getTableType("Zoom Table", this);
		zoomTable.setDefaultRenderer(Object.class, tableRenderer.createTableCellRenderer());

		zoomTable.addMouseListener(zoomTableListenerHandler.createClusterOneClickHandler());

		zoomTable.addMouseListener(zoomTableListenerHandler.createClusterRightClickHandler(zoomTable));

		zoomTable.getTableHeader()
				.addMouseListener(zoomTableListenerHandler.createClusterColumnClickedHandler(zoomTable));

		zoomTable.getTableHeader()
				.addMouseListener(zoomTableListenerHandler.createClusterColumnRightClickHandler(zoomTable));

		gui.zoomAreaTable = zoomTable;

		gui.tmpScrollPaneZoomArea.setViewportView(gui.zoomAreaTable);
		jItemsHandler.setJScrollPanePosition(gui.tmpScrollPaneZoomArea, 300, 300, 950, 250);

		gui.lifeTimePanel.setCursor(gui.getCursor());
		gui.lifeTimePanel.add(gui.tmpScrollPaneZoomArea);

	}

}
