package gui.mainEngine;

//try to extract relationship beetween gui and pplSchema and pplTransition
import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.ParametersJDialog;
import gui.tableElements.JvTable;
import gui.tableElements.MyTableModel;
import gui.tableElements.TableConstructionAllSquaresIncluded;
import gui.tableElements.TableConstructionIDU;
import gui.tableElements.TableConstructionPhases;
import gui.tableElements.TableConstructionPhasesClusterTables;
import gui.tableElements.TableConstructionWithClusters;
import gui.tableElements.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.treeElements.TreeConstructionGeneral;
import gui.treeElements.TreeConstructionPhases;
import gui.treeElements.TreeConstructionPhasesWithClusters;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import org.antlr.v4.runtime.RecognitionException;
import org.jfree.chart.ChartPanel;

import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import results.Results;
import results.ResultsDataKeeper;
import tableClustering.engine.TableClusteringMainEngine;
import algorithms.Algorithm;
import algorithms.FindCoChanges;
import data.dataKeeper.GlobalDataKeeper;
import data.pplTransition.AtomicChange;


public class Gui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel lifeTimePanel = new JPanel();
	private JPanel statistics = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	private MyTableModel zoomModel = null;

	private JvTable LifeTimeTable=null;
	private JvTable zoomAreaTable=null;
	
	
	private JLabel labelLongLivedTables=new JLabel();
	private JLabel labelMostUpdatedTables=new JLabel();
	private JLabel labelMostUpdatedAttributes=new JLabel();
	private JLabel labelMostIntensiveTables=new JLabel();
	
	private JTextArea jTextAreaMostUpdatedAttributes=new JTextArea();
	private JTextArea jTextAreaMostIntensiveTables=new JTextArea();
	
	
	@SuppressWarnings("rawtypes")
	private JList jListKLongLivedTables=new JList();
	@SuppressWarnings("rawtypes")
	private JList jListKMostUpdatedTables=new JList();
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModelLongLivedTables=new DefaultListModel();
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModelMostUpdatedTables=new DefaultListModel();
	private JScrollPane tmpScrollPane =new JScrollPane();
	private JScrollPane treeScrollPane= new JScrollPane();
	private JScrollPane tmpScrollPaneZoomArea =new JScrollPane();
	private JScrollPane jScrollPaneKLongLivedTables=null;
	private JScrollPane jScrollPaneKMostUpdatedTables=null;
	private JScrollPane jScrollPaneKMostUpdatedAttributes=null;
	private JScrollPane jScrollPaneKMostIntensiveTables=null;
	
	
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	private GlobalDataKeeper globalDataKeeper=null;
	private ResultsDataKeeper resultsDataKeeper = null;

	private ChartPanel chartPanel=null;
	private ChartPanel chartPanelPie=null;
	private ChartPanel chartPanelPie2=null;
	
	private String[] finalColumns=null;
	private String[][] finalRows=null;
	private String[] finalColumnsZoomArea=null;
	private String[][] finalRowsZoomArea=null;
	private String currentProject=null;
	private String currentProjectDataFolder=null;
	private String project=null;
	
	private Float[][] mostIntensiveInsersions=null;
	
	
	private Integer[] segmentSize=new Integer[3];
	
	private Results resultsLLTR=null;
	private Results resultsPOCR=null;
	private Results resultsMUTR=null;
	private Results resultsMUAR=null;
	private Results resultsITFTR=null;
	
	private Algorithm finalLongLivedTables=null;
	private Algorithm finalMostUpdatedTables=null;
	private Algorithm finalMostUpdatedAttributes=null;
	private Algorithm intensiveTables=null;
	private Algorithm changes=null;
	
	private boolean levelizedTable;
	
	private Float timeWeight=null;
	private Float changeWeight=null;
	private Double birthWeight=null;
	private Double deathWeight=null;
	private Double changeWeightCl=null;

	private Integer numberOfPhases=null;
	private Integer numberOfClusters=null;
	private Boolean preProcessingTime=null;
	private Boolean preProcessingChange=null;
	
	private String projectName="";
	private String datasetTxt="";
	private String inputCsv="";
	private String outputAssessment1="";
	private String outputAssessment2="";
	private String transitionsFile="";
	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	private JTree tablesTree=new JTree();
	private JPanel sideMenu=new JPanel();
	private JPanel tablesTreePanel=new JPanel();
	private JPanel descriptionPanel=new JPanel();
	private JLabel treeLabel;
	private JLabel generalTableLabel;
	private JLabel zoomAreaLabel;
	private JLabel descriptionLabel;
	private JTextArea descriptionText;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton showGridButton;
	private JButton uniformlyDistributedButton;
	private JButton notUniformlyDistributedButton;


	private int[] selectedRowsFromMouse;
	private int selectedColumn=-1;
	private int selectedColumnZoomArea=-1;
	private int selectedRow;

	private int[] selectedColumnsFromMouse;
	private int wholeCol=-1;
	private int wholeColZoomArea=-1;
	
	private int rowHeight=1;
	private int columnWidth=1;

	private ArrayList<String> tablesSelected = new ArrayList<String>();

	private boolean showGrid=false;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					//return;
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
		
	
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

				createProjectDialog.setModal(true);
				
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation()){
					
					createProjectDialog.setVisible(false);
					
					File file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            project=file.getName();
		            String fileName=file.toString();
		            System.out.println("!!"+project);
		          

				
				
				
					try {
						importData(fileName);
					} catch (IOException e) {
						//e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					} catch (RecognitionException e) {
						
						//e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
					
				}
				
		            
				
			}
		});
		mnFile.add(mntmCreateProject);
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String fileName=null;
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcOpen1.getSelectedFile();
		            System.out.println(file.toString());
		            project=file.getName();
		            fileName=file.toString();
		            System.out.println("!!"+project);
		          

				
				}
				else{
					return;
				}
				
				
				try {
					importData(fileName);
				} catch (IOException e) {
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {
					
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}
				
			}
		});
		mnFile.add(mntmLoadProject);
		
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String fileName=null;
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcOpen1.getSelectedFile();
		            System.out.println(file.toString());
		            project=file.getName();
		            fileName=file.toString();
		            System.out.println("!!"+project);
		          
		            BufferedReader br;
					try {
						br = new BufferedReader(new FileReader(fileName));
						String line;
						
						while(true) {
							line = br.readLine();
							if (line == null) 
								break;
							//System.out.println(line);
							if(line.contains("Project-name")){
								String[] projectNameTable=line.split(":");
								projectName=projectNameTable[1];
							}
							else if(line.contains("Dataset-txt")){
								String[] datasetTxtTable=line.split(":");
								datasetTxt=datasetTxtTable[1];
							}
							else if(line.contains("Input-csv")){
								String[] inputCsvTable=line.split(":");
								inputCsv=inputCsvTable[1];
							}
							else if(line.contains("Assessement1-output")){
								String[] outputAss1=line.split(":");
								outputAssessment1=outputAss1[1];
							}
							else if(line.contains("Assessement2-output")){
								String[] outputAss2=line.split(":");
								outputAssessment2=outputAss2[1];
							}
							else if(line.contains("Transition-xml")){
								String[] transitionXmlTable=line.split(":");
								transitionsFile=transitionXmlTable[1];
							}
							
							
						};	
						
						br.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					}
					
					System.out.println(projectName);
					
					CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(projectName,datasetTxt,inputCsv,outputAssessment1,outputAssessment2,transitionsFile);
				
					createProjectDialog.setModal(true);
					
					
					createProjectDialog.setVisible(true);
					
					if(createProjectDialog.getConfirmation()){
						
						createProjectDialog.setVisible(false);
						
						file = createProjectDialog.getFile();
			            System.out.println(file.toString());
			            project=file.getName();
			            fileName=file.toString();
			            System.out.println("!!"+project);
			          

					
					
					
						try {
							importData(fileName);
						} catch (IOException e) {
							//e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
							return;
						} catch (RecognitionException e) {
							
							//e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
							return;
						}
						
						
					}
					
				
				}
				else{
					return;
				}

				
				
		            
				
			}
		});
		mnFile.add(mntmEditProject);
		
		JMenuItem mntmExportDataImage = new JMenuItem("Export Image");
		mntmExportDataImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Rectangle r=new Rectangle(30, 30, 1300, 700);
				
				BufferedImage img=robot.createScreenCapture(r);
				
				try {
					ImageIO.write(img, "jpg", new File("1.jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnFile.add(mntmExportDataImage);
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(currentProject==null)){
					TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					segmentSize=table.getSegmentSize();
					finalColumns=columns;
					finalRows=rows;
					tabbedPane.setSelectedIndex(0);
					makeDetailedTable(columns,rows,true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		
		JMenuItem mntmShowGeneralLifetime = new JMenuItem("Show General LifeTime Table");
		mntmShowGeneralLifetime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 if(!(currentProject==null)){
					TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					segmentSize=table.getSegmentSize();
					System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					finalColumns=columns;
					finalRows=rows;
					tabbedPane.setSelectedIndex(0);
					makeGeneralTable();
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnTable.add(mntmShowGeneralLifetime);
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 if(!(currentProject==null)){
					zoomInButton.setVisible(true);
					zoomOutButton.setVisible(true);
					TableConstructionIDU table=new TableConstructionIDU(globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					segmentSize=table.getSegmentSize();
					System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					finalColumns=columns;
					finalRows=rows;
					tabbedPane.setSelectedIndex(0);
					makeGeneralTableIDU();
					fillTree();
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!(project==null)){
					
					ParametersJDialog jD=new ParametersJDialog(false);
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
			            timeWeight = jD.getTimeWeight();
			            changeWeight = jD.getChangeWeight();
			            preProcessingTime = jD.getPreProcessingTime();
			            preProcessingChange = jD.getPreProcessingChange();
					    numberOfPhases = jD.getNumberOfPhases();
			            
			            System.out.println(timeWeight+" "+changeWeight);
			            
						PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);
	
						
						
						//for(int i=0; i<PhaseExtractionParameters.DATASET_AR.size(); i++){
							
						mainEngine.parseInput();		
						System.out.println("\n\n\n");
						mainEngine.extractPhases(numberOfPhases);
						try {
							mainEngine.extractReportAssessment1();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							mainEngine.extractReportAssessment2();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//}
						
						/*try {
							mainEngine.extractWinnersReport();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						mainEngine.connectTransitionsWithPhases(globalDataKeeper);
						globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
						
						
						if(globalDataKeeper.getPhaseCollectors().size()!=0){
							TableConstructionPhases table=new TableConstructionPhases(globalDataKeeper);
							final String[] columns=table.constructColumns();
							final String[][] rows=table.constructRows();
							segmentSize=table.getSegmentSize();
							System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
							System.out.println("C: "+columns.length+" R: "+rows.length);
		
							finalColumns=columns;
							finalRows=rows;
							tabbedPane.setSelectedIndex(0);
							makeGeneralTablePhases();
							fillPhasesTree();
						}
						else{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
						}
					}
				}
				else{
					
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					
				}
				
				
				/*
				 if(!(currentProject==null)){
					if(globalDataKeeper.getPhaseCollectors().size()!=0){
						TableConstructionPhases table=new TableConstructionPhases(globalDataKeeper);
						final String[] columns=table.constructColumns();
						final String[][] rows=table.constructRows();
						segmentSize=table.getSegmentSize();
						System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						finalColumns=columns;
						finalRows=rows;
						tabbedPane.setSelectedIndex(0);
						makeGeneralTablePhases();
						fillPhasesTree();
					}
					else{
						JOptionPane.showMessageDialog(null, "Extract Phases first");
					}
				}
				else{
					if(currentProject==null){
						JOptionPane.showMessageDialog(null, "Select a Project first");
					}
					
					return;
				}*/
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!(project==null)){
					
					ParametersJDialog jD=new ParametersJDialog(true);
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
			            timeWeight = jD.getTimeWeight();
			            changeWeight = jD.getChangeWeight();
			            preProcessingTime = jD.getPreProcessingTime();
			            preProcessingChange = jD.getPreProcessingChange();
					    numberOfPhases = jD.getNumberOfPhases();
					    numberOfClusters = jD.getNumberOfClusters();
					    birthWeight=jD.geBirthWeight();
					    deathWeight=jD.getDeathWeight();
					    changeWeightCl=jD.getChangeWeightCluster();
			            
			            System.out.println(timeWeight+" "+changeWeight);
			            
						PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);
						
						//for(int i=0; i<PhaseExtractionParameters.DATASET_AR.size(); i++){
							
						mainEngine.parseInput();		
						System.out.println("\n\n\n");
						mainEngine.extractPhases(numberOfPhases);
						try {
							mainEngine.extractReportAssessment1();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							mainEngine.extractReportAssessment2();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//}
						
						/*try {
							mainEngine.extractWinnersReport();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						mainEngine.connectTransitionsWithPhases(globalDataKeeper);
						globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
						TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,birthWeight,deathWeight,changeWeightCl);
						mainEngine2.extractClusters(numberOfClusters);
						globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
						mainEngine2.print();
						
						if(globalDataKeeper.getPhaseCollectors().size()!=0){
							TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
							final String[] columns=table.constructColumns();
							final String[][] rows=table.constructRows();
							segmentSize=table.getSegmentSize();
							System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
							System.out.println("C: "+columns.length+" R: "+rows.length);
		
							finalColumns=columns;
							finalRows=rows;
							tabbedPane.setSelectedIndex(0);
							makeGeneralTablePhases();
							fillClustersTree();
						}
						else{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
						}
					}
				}
				else{
					
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					
				}
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		JMenuItem mntmShowLifetimeTable_1 = new JMenuItem("Show LifeTime Table With Selected Level");
		mntmShowLifetimeTable_1.addActionListener(new ActionListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void actionPerformed(ActionEvent arg0) {
				if(!(currentProject==null)){
					final JDialog  insertLevelDialog=new JDialog();
					insertLevelDialog.setBounds(400,350,200, 200);
					insertLevelDialog.getContentPane().setLayout(null);
					
					JLabel  label=new JLabel("Choose Level");
					label.setBounds(55, 10, 80, 30);
					insertLevelDialog.getContentPane().add(label);
					
					String[] levels={"3","4","5","6","7","8","9","10","11","12"};
					
					final JComboBox jComboBoxLevels=new JComboBox(levels);
					jComboBoxLevels.setBounds(67,45,60,22);
					jComboBoxLevels.setSelectedIndex(0);
					
					insertLevelDialog.getContentPane().add(jComboBoxLevels);
					
					JButton insertKOK=new JButton("OK");
					insertKOK.setBounds(65, 85 , 60, 20);
					
					insertKOK.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							
							/*String selectedLevel=(String)jComboBoxLevels.getSelectedItem();
							
							int level=Integer.parseInt(selectedLevel);
							
							ArrayList<Schema> versionsWithLevel=calculateNumberofVersions(level);
							
							LevelizedLifeTime levelizedLifeTime=new LevelizedLifeTime(versionsWithLevel);
							
							for(int i=0;i<versionsWithLevel.size();i++){
								System.out.println(versionsWithLevel.get(i).getName());
							}
							
							try {
								levelizedLifeTime.makeTransitions();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							};
							
							insertLevelDialog.setVisible(false);
							
							LevelizedSchemas=levelizedLifeTime.getLevelizedSchemas();
							LevelizedTransitions=levelizedLifeTime.getLevelizedTransitions();
							
							TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(LevelizedSchemas, LevelizedTransitions);
							segmentSize=table.getSegmentSize();
							finalColumns=table.constructColumns();
							finalRows=table.constructRows();
							tabbedPane.setSelectedIndex(0);
							makeDetailedTable(finalColumns, finalRows,false);
							
							*/
						}
						
	
						
					});
					insertLevelDialog.getContentPane().add(insertKOK);
					
					insertLevelDialog.setVisible(true);
				
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		//mnTable.add(mntmShowLifetimeTable_1);
		mnTable.add(mntmShowLifetimeTable);
		
		/*JMenuItem mntmSwapRows = new JMenuItem("Swap Two Rows of Detailed Table (S)");
		mntmSwapRows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(selectedRows.isEmpty()||selectedRows.size()==1){
					
					JOptionPane.showMessageDialog(null, "Choose two rows first!!!");
					
					return;
					
				}
				
				String[] tmpValues=new String[LifeTimeTable.getColumnCount()];
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					
					tmpValues[i]=(String) LifeTimeTable.getValueAt(selectedRows.get(0), i);
					
					
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(0)][i]=(String) LifeTimeTable.getValueAt(selectedRows.get(1), i);
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(1)][i]=tmpValues[i];
				}
				
				makeDetailedTable(finalColumns,finalRows,true);
				
				selectedRows=new ArrayList<Integer>();
				
				
			}
		});
		mnTable.add(mntmSwapRows);
		
		*/

		sideMenu.setBounds(0, 0, 280, 600);
		sideMenu.setBackground(Color.DARK_GRAY);
		
		
		
		GroupLayout gl_sideMenu = new GroupLayout(sideMenu);
		gl_sideMenu.setHorizontalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_sideMenu.setVerticalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 743, Short.MAX_VALUE)
		);
		
		sideMenu.setLayout(gl_sideMenu);
		
		tablesTreePanel.setBounds(10, 400, 260, 180);
		tablesTreePanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_tablesTreePanel.setVerticalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 743, Short.MAX_VALUE)
		);
		
		tablesTreePanel.setLayout(gl_tablesTreePanel);
		
		treeLabel=new JLabel();
		treeLabel.setBounds(10, 370, 260, 40);
		treeLabel.setForeground(Color.WHITE);
		treeLabel.setText("Tree");
		
		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_descriptionPanel.setVerticalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
				//.addGap(0, 743, Short.MAX_VALUE)
		);
		
		descriptionPanel.setLayout(gl_descriptionPanel);
		
		descriptionText=new JTextArea();
		descriptionText.setBounds(5, 5, 250, 170);
		descriptionText.setForeground(Color.BLACK);
		descriptionText.setText("");
		descriptionText.setBackground(Color.LIGHT_GRAY);
		
		descriptionPanel.add(descriptionText);
		
		
		descriptionLabel=new JLabel();
		descriptionLabel.setBounds(10, 160, 260, 40);
		descriptionLabel.setForeground(Color.WHITE);
		descriptionLabel.setText("Description");
		
		

		
		

		sideMenu.add(treeLabel);
		sideMenu.add(tablesTreePanel);
		
		sideMenu.add(descriptionLabel);
		sideMenu.add(descriptionPanel);

		
		
	   //TreeConstructionGeneral tc=new TreeConstructionGeneral(globalDataKeeper);
	   // tablesTree=tc.constructTree();
		//tablesTreePanel.add(scr);
		//JScrollPane sp = new JScrollPane(jtree);
		
		   

		lifeTimePanel.add(sideMenu);
		
		
		
		
//		
//		
//		JMenu mnStatistics = new JMenu("Statistics");
//		menuBar.add(mnStatistics);
//		
//		JMenuItem mntmShowLongLivedTables = new JMenuItem("Show K Long Lived Tables");
//		mntmShowLongLivedTables.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				if(!(currentProject==null)){
//					//assistantList=new ArrayList<PPLTable>();
//					//longLivedTables=new ArrayList<PPLTable>();
//					
//					final JDialog  insertKDialog=new JDialog();
//					insertKDialog.setBounds(400,350,200, 200);
//					insertKDialog.getContentPane().setLayout(null);
//					
//					JLabel  label=new JLabel("Insert K");
//					label.setBounds(67, 10, 80, 30);
//					insertKDialog.getContentPane().add(label);
//					
//					final JTextField insertKTextField=new JTextField("");
//					insertKTextField.setBounds(67, 55, 40, 20);
//					insertKDialog.getContentPane().add(insertKTextField);
//					
//					JButton insertKOK=new JButton("OK");
//					insertKOK.setBounds(62,120 , 60, 20);
//					
//					insertKOK.addActionListener(new ActionListener() {
//						@SuppressWarnings("unchecked")
//						public void actionPerformed(ActionEvent arg0) {
//							int kNumber;
//							
//							String kString=insertKTextField.getText();
//							
//							if(kString.equals("")){
//								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
//								return;
//							}
//							try{
//				                kNumber=Integer.parseInt(kString);
//				            } catch(NumberFormatException nfe) {
//				                JOptionPane.showMessageDialog(null, "K must be numeric");
//				                return;
//				            }
//							
//							insertKDialog.setVisible(false);
//							
//							tabbedPane.setSelectedIndex(1);
//							
//							finalLongLivedTables=new LongLivedTablesAlgo(globalDataKeeper.getAllPPLSchemas(),kNumber);
//							
//							
//							ResultsFactory rf = new ResultsFactory("LongLivedTablesResults");
//							resultsLLTR=rf.createResult();
//							resultsLLTR=finalLongLivedTables.compute();
//							resultsDataKeeper.setLongLivedTables(resultsLLTR.getResults());
//							
//						
//							showKLongLivedTables();
//							
//							if(resultsDataKeeper.getLongLivedTables().size()==0){
//								JOptionPane.showMessageDialog(null, "Calculate K Long Lived Tables first");
//								return;
//							}
//							
//							
//							statistics.add(chartPanel);
//							statistics.revalidate();
//							statistics.repaint();
//							
//						
//						}
//	
//						
//					});
//					insertKDialog.getContentPane().add(insertKOK);
//					
//					
//					insertKDialog.setVisible(true);
//				
//				}
//				else{
//					JOptionPane.showMessageDialog(null, "Select a Project first");
//					return;
//				}
//				
//				
//			}
//		});
//		mnStatistics.add(mntmShowLongLivedTables);
//		
//		JMenuItem mntmShowKMostUpdatedTable = new JMenuItem("Show K Most Updated Tables");
//		mntmShowKMostUpdatedTable.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				if(!(currentProject==null)){
//					final JDialog  insertKDialog=new JDialog();
//					insertKDialog.setBounds(400,350,200, 200);
//					insertKDialog.getContentPane().setLayout(null);
//					
//					JLabel  label=new JLabel("Insert K");
//					label.setBounds(67, 10, 80, 30);
//					insertKDialog.getContentPane().add(label);
//					
//					final JTextField insertKTextField=new JTextField("");
//					insertKTextField.setBounds(67, 55, 40, 20);
//					insertKDialog.getContentPane().add(insertKTextField);
//					
//					JButton insertKOK=new JButton("OK");
//					insertKOK.setBounds(62,120 , 60, 20);
//					
//					insertKOK.addActionListener(new ActionListener() {
//						@SuppressWarnings("unchecked")
//						public void actionPerformed(ActionEvent arg0) {
//							int kNumber;
//							String kString=insertKTextField.getText();
//							
//							if(kString.equals("")){
//								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
//								return;
//							}
//							try{
//				                kNumber=Integer.parseInt(kString);
//				            } catch(NumberFormatException nfe) {
//				                JOptionPane.showMessageDialog(null, "K must be numeric");
//				                return;
//				            }
//							
//							insertKDialog.setVisible(false);
//							
//							TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
//							table.constructColumns();
//							table.constructRows();
//							
//							tabbedPane.setSelectedIndex(1);
//							
//							finalMostUpdatedTables=new MostUpdatedTablesAlgo(globalDataKeeper.getAllPPLSchemas(), kNumber);
//							
//							ResultsFactory rf = new ResultsFactory("MostUpdatedTablesResults");
//							resultsMUTR=rf.createResult();
//							resultsMUTR=finalMostUpdatedTables.compute();
//							
//							resultsDataKeeper.setMostUpdatedTables(resultsMUTR.getResults());
//							
//							showKMostUpdatedTables();
//							
//							if(resultsDataKeeper.getMostUpdatedTables().size()==0){
//								JOptionPane.showMessageDialog(null, "Calculate K Most Updated Tables first");
//								return;
//							}
//							
//							
//							try {
//								
//								
//							} catch (Exception e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//							
//							JLabel searchVersion=new JLabel("Search Version With Version ID");
//							searchVersion.setBounds(10, 320, 180, 30);
//							final JTextField id=new JTextField();
//							id.setBounds(48, 360, 50, 20);
//							JButton search=new JButton("Search");
//							search.setBounds(30, 405, 80, 30);
//							search.addActionListener(new ActionListener() {
//								public void actionPerformed(ActionEvent e) {
//									int idNumber;
//									String k=id.getText();
//									try{
//										idNumber=Integer.parseInt(k);
//										
//									}
//									catch(Exception ex){
//										JOptionPane.showMessageDialog(null,"id must be Numeric !!!");
//										return;
//									}
//									
//									JOptionPane.showMessageDialog(null,globalDataKeeper.getAllPPLSchemas().get(idNumber).getName() );
//									
//								}
//						
//							});
//							
//							statistics.add(search);
//							statistics.add(searchVersion);
//							statistics.add(id);
//							statistics.add(chartPanel);
//							statistics.revalidate();
//							statistics.repaint();
//						
//						}
//	
//						
//					});
//					insertKDialog.getContentPane().add(insertKOK);
//	
//					insertKDialog.setVisible(true);
//				}
//				else{
//					JOptionPane.showMessageDialog(null, "Select a Project first");
//					return;
//				}
//				
//			}
//		});
//		mnStatistics.add(mntmShowKMostUpdatedTable);
//		
//		JMenuItem mntmShowKMostUpdatedAttributes = new JMenuItem("Show K Most Updated Attributes");
//		mntmShowKMostUpdatedAttributes.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				if(!(currentProject==null)){
//					final JDialog  insertKDialog=new JDialog();
//					insertKDialog.setBounds(400,350,200, 200);
//					insertKDialog.getContentPane().setLayout(null);
//					
//					JLabel  label=new JLabel("Insert K");
//					label.setBounds(67, 10, 80, 30);
//					insertKDialog.getContentPane().add(label);
//					
//					final JTextField insertKTextField=new JTextField("");
//					insertKTextField.setBounds(67, 55, 40, 20);
//					insertKDialog.getContentPane().add(insertKTextField);
//					
//					JButton insertKOK=new JButton("OK");
//					insertKOK.setBounds(62,120 , 60, 20);
//					
//					insertKOK.addActionListener(new ActionListener() {
//						@SuppressWarnings("unchecked")
//						public void actionPerformed(ActionEvent arg0) {
//							int kNumber;
//							String kString=insertKTextField.getText();
//							
//							if(kString.equals("")){
//								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
//								return;
//							}
//							try{
//				                kNumber=Integer.parseInt(kString);
//				            } catch(NumberFormatException nfe) {
//				                JOptionPane.showMessageDialog(null, "K must be numeric");
//				                return;
//				            }
//							
//							insertKDialog.setVisible(false);
//							
//							tabbedPane.setSelectedIndex(1);
//							
//							finalMostUpdatedAttributes=new MostUpdatedAttributesAlgo(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions(),kNumber);
//							
//							ResultsFactory rf = new ResultsFactory("MostUpdatedAttributesResults");
//							resultsMUAR= rf.createResult();
//							resultsMUAR=finalMostUpdatedAttributes.compute();
//							
//							resultsDataKeeper.setMostUpdatedAttributes(resultsMUAR.getResults());
//							
//						
//							showKMostUpdatedAttributes();
//							
//							if(resultsDataKeeper.getMostUpdatedAttributes().size()==0){
//								JOptionPane.showMessageDialog(null, "Calculate K Most Updated Attributes first");
//								return;
//							}
//							
//							/*for(int i=0;i<7;i++){
//								assistantList.add(elderTables.get(i));
//							}*/
//							
//							statistics.add(chartPanel);
//							statistics.revalidate();
//							statistics.repaint();
//						
//						}
//	
//						
//					});
//					insertKDialog.getContentPane().add(insertKOK);
//	
//					insertKDialog.setVisible(true);
//				}
//				else{
//					JOptionPane.showMessageDialog(null, "Select a Project first");
//					return;
//				}
//			}
//		});
//		mnStatistics.add(mntmShowKMostUpdatedAttributes);
//		
//		JMenuItem mntmShowKMost = new JMenuItem("Show K Most Intensive Tables Between Two Schemas");
//		mntmShowKMost.addActionListener(new ActionListener() {
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			public void actionPerformed(ActionEvent arg0) {
//				
//				if(currentProject==null){
//					JOptionPane.showMessageDialog(null, "Select a Project first");
//					return;
//					
//				}
//				
//				final JDialog  insertKDialog=new JDialog();
//				insertKDialog.setBounds(400,350,400, 200);
//				insertKDialog.getContentPane().setLayout(null);
//				
//				JLabel  label=new JLabel("From Version");
//				label.setBounds(10, 0, 80, 30);
//				insertKDialog.getContentPane().add(label);
//				
//				String[] allSchemasNames=new String[globalDataKeeper.getAllPPLSchemas().size()];
//				Set<String> alSchNa = globalDataKeeper.getAllPPLSchemas().keySet();
//				int howMany=0;
//				for(String s:alSchNa){
//					allSchemasNames[howMany]=s;
//					howMany++;
//
//				}
//				
//				
//				
//				final JComboBox jComboBoxAllSchemas=new JComboBox(allSchemasNames);
//				jComboBoxAllSchemas.setBounds(10,30,180,30);
//				jComboBoxAllSchemas.setSelectedIndex(0);
//				
//				insertKDialog.getContentPane().add(jComboBoxAllSchemas);
//				
//				JLabel  label2=new JLabel("To Version");
//				label2.setBounds(10, 60, 80, 30);
//				insertKDialog.getContentPane().add(label2);
//				
//				
//				final JComboBox jComboBoxAllSchemas2=new JComboBox(allSchemasNames);
//				jComboBoxAllSchemas2.setBounds(10,90,180,30);
//				jComboBoxAllSchemas2.setSelectedIndex(0);
//				
//				insertKDialog.getContentPane().add(jComboBoxAllSchemas2);
//				
//				
//				JLabel  label3=new JLabel("Insert K");
//				label3.setBounds(250, 10, 80, 30);
//				insertKDialog.getContentPane().add(label3);
//				
//				final JTextField insertKTextField=new JTextField("");
//				insertKTextField.setBounds(250, 40, 40, 20);
//				insertKDialog.getContentPane().add(insertKTextField);
//				
//				JButton insertKOK=new JButton("OK");
//				insertKOK.setBounds(250,90 , 60, 20);
//				
//				insertKOK.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent arg0) {
//						int kNumber;
//						String kString=insertKTextField.getText();
//						
//						if(kString.equals("")){
//							JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
//							return;
//						}
//						try{
//			                kNumber=Integer.parseInt(kString);
//			            } catch(NumberFormatException nfe) {
//			                JOptionPane.showMessageDialog(null, "K must be numeric");
//			                return;
//			            }
//						
//						if(jComboBoxAllSchemas.getSelectedItem()==null || jComboBoxAllSchemas2.getSelectedItem()==null){
//							JOptionPane.showMessageDialog(null, " You must select both FROM and TO versions ");
//							return;
//						}
//						
//						
//						String fromVersion=(String)jComboBoxAllSchemas.getSelectedItem();
//						String toVersion=(String)jComboBoxAllSchemas2.getSelectedItem();
//						
//						//PPLSchema firstSchema=null;
//						//PPLSchema secondSchema=null;
//						
//						//firstSchema=globalDataKeeper.getAllPPLSchemas().get(fromVersion);
//						//secondSchema=globalDataKeeper.getAllPPLSchemas().get(toVersion);
//							
//						insertKDialog.setVisible(false);
//						
//						tabbedPane.setSelectedIndex(1);
//						System.out.println("!!!!"+fromVersion+" "+toVersion);
//						
//						GlobalDataKeeper dataKeeperDiff = new GlobalDataKeeper(fromVersion,toVersion,currentProjectDataFolder);
//						dataKeeperDiff.setDataForTwoVersions();
//				
////						DiffFromTwoSchemas diffForTwoSch= new DiffFromTwoSchemas(fromVersion, toVersion, currentProjectDataFolder);
////						try {
////							diffForTwoSch.findDifferenciesFromTwoSchemas();
////						} catch (IOException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
//						//TreeMap<String,TableChange> tbChForTwoSch=dataKeeperDiff.getTmpTableChanges();
//						IntensiveTablesFromTwoSchemasAlgo  finalmostIntensiveTables=new IntensiveTablesFromTwoSchemasAlgo(globalDataKeeper.getAllPPLSchemas().get(fromVersion),
//								globalDataKeeper.getAllPPLSchemas().get(toVersion),dataKeeperDiff.getTmpTableChanges(), kNumber);
//					
//						
//						intensiveTables=finalmostIntensiveTables;
//						
//						ResultsFactory rf = new ResultsFactory("IntensiveTablesFromTwoSchemaResults");
//						resultsITFTR=rf.createResult();
//						resultsITFTR=intensiveTables.compute();
//						
//						resultsDataKeeper.setMostIntensiveTables(resultsITFTR.getResults());
//						
//						
//					
//						showKMostIntensiveTablesBetweenTwoSchemas(fromVersion,toVersion);
//						
//						if(resultsDataKeeper.getMostIntensiveTables().size()==0){
//							JOptionPane.showMessageDialog(null, "Calculate K Most Intensive Tables first");
//							return;
//						}
//						
//						
//						statistics.add(chartPanel);
//						//statistics.add(chartPanelPie);
//						//statistics.add(chartPanelPie2);
//						statistics.revalidate();
//						statistics.repaint();
//					
//					}
//
//					
//				});
//				insertKDialog.getContentPane().add(insertKOK);
//				
//				
//				
//				insertKDialog.setVisible(true);
//				
//			}
//		});
//		mnStatistics.add(mntmShowKMost);
//		
//		JMenuItem mntmShowKMost_1 = new JMenuItem("Show K Most Intensive Changes by Percentage");
//		mntmShowKMost_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				if(currentProject==null){
//					JOptionPane.showMessageDialog(null, "Select a Project first");
//					return;
//					
//				}
//				final JDialog  insertKDialog=new JDialog();
//				insertKDialog.setBounds(400,350,200, 200);
//				insertKDialog.getContentPane().setLayout(null);
//				
//				JLabel  label=new JLabel("Insert K");
//				label.setBounds(67, 10, 80, 30);
//				insertKDialog.getContentPane().add(label);
//				
//				final JTextField insertKTextField=new JTextField("");
//				insertKTextField.setBounds(67, 55, 40, 20);
//				insertKDialog.getContentPane().add(insertKTextField);
//				
//				JButton insertKOK=new JButton("OK");
//				insertKOK.setBounds(62,120 , 60, 20);
//				
//				insertKOK.addActionListener(new ActionListener() {
//					@SuppressWarnings("unchecked")
//					public void actionPerformed(ActionEvent arg0) {
//						String kString=insertKTextField.getText();
//						
//						if(kString.equals("")){
//							JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
//							return;
//						}
//						try{
//			            } catch(NumberFormatException nfe) {
//			                JOptionPane.showMessageDialog(null, "K must be numeric");
//			                return;
//			            }
//						
//						insertKDialog.setVisible(false);
//						
//						tabbedPane.setSelectedIndex(1);
//						
//						changes=new PercentageOfChangesAlgo(globalDataKeeper.getAllPPLSchemas(),globalDataKeeper.getAllPPLTransitions());
//						
//						
//						ResultsFactory rf = new ResultsFactory("");
//						resultsPOCR=rf.createResult();
//						resultsPOCR=changes.compute();
//						
//						resultsDataKeeper.setPercentageOfChangesAboutTables(resultsPOCR.getResults("lala"));
//					
//						
//						if(mostIntensiveInsersions==null){
//							JOptionPane.showMessageDialog(null, "Calculate K Most Intensive Changes by percentage first");
//							return;
//						}
//											
//						statistics.add(chartPanel);
//						statistics.add(chartPanelPie);
//						statistics.add(chartPanelPie2);
//						statistics.revalidate();
//						statistics.repaint();
//					
//					}
//
//					
//				});
//				insertKDialog.getContentPane().add(insertKOK);
//
//				insertKDialog.setVisible(true);
//				
//				
//			}
//		});
//		mnStatistics.add(mntmShowKMost_1);
//		
		JButton buttonHelp=new JButton("Help");
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
									"the SQL files of the dataset that you want to use."+"\n" +
									"The .txt file must have EXACTLY the same name with the folder " +
									"that contains the DDL Scripts of the dataset."+ "\n" +
									"Both .txt file and dataset folder must be in the same folder.";
				JOptionPane.showMessageDialog(null,message); 				
			}
		});
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		JButton coChangesBtn=new JButton("Co-Changes");
		coChangesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper);
				table.constructColumns();
				table.constructRows();
				
				
				
				FindCoChanges coChanges=new FindCoChanges(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions(), globalDataKeeper.getAllPPLTables(),project);

				coChanges.compute("computeAndExport");
				
				
				
			}
		});
		coChangesBtn.setBounds(900,900 , 160, 40);
		menuBar.add(coChangesBtn);
		/*
		JButton extractPhasesBtn = new JButton("Extract Phases");
		extractPhasesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!(project==null)){
				
					ParametersJDialog jD=new ParametersJDialog();
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
			            timeWeight = jD.getTimeWeight();
			            changeWeight = jD.getChangeWeight();
			            preProcessingTime = jD.getPreProcessingTime();
			            preProcessingChange = jD.getPreProcessingChange();
					    numberOfPhases = jD.getNumberOfPhases();
			            
			            System.out.println(timeWeight+" "+changeWeight);
			            
						PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);
	
						Float b=new Float(0.3);
						Float d=new Float(0.3);
						Float c=new Float(0.3);

						
						//for(int i=0; i<PhaseExtractionParameters.DATASET_AR.size(); i++){
							
						mainEngine.parseInput();		
						System.out.println("\n\n\n");
						mainEngine.extractPhases(numberOfPhases);
						try {
							mainEngine.extractReportAssessment1();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							mainEngine.extractReportAssessment2();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//}
						
						
						
						mainEngine.connectTransitionsWithPhases(globalDataKeeper);
						globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
						TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,b,d,c);
						mainEngine2.extractClusters(globalDataKeeper.getAllPPLTables().size()/8);
						globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
						mainEngine2.print();
					}
				}
				else{
					
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					
				}
				
			}
		});
		extractPhasesBtn.setBounds(900,900 , 160, 40);
		menuBar.add(extractPhasesBtn);
		*/
		
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);
		
		
		tabbedPane.addTab("LifeTime Table", null, lifeTimePanel, null);
		
		GroupLayout gl_lifeTimePanel = new GroupLayout(lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_lifeTimePanel.setVerticalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
		lifeTimePanel.setLayout(gl_lifeTimePanel);
		
		
		generalTableLabel=new JLabel("Parallel Lives Diagram");
		generalTableLabel.setBounds(300, 0, 150, 30);
		generalTableLabel.setForeground(Color.BLACK);
		
		zoomAreaLabel=new JLabel();
		zoomAreaLabel.setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
		zoomAreaLabel.setBounds(1255, 325, 15, 300);
		zoomAreaLabel.setForeground(Color.BLACK);
		
		zoomInButton = new JButton("Zoom In");
		zoomInButton.setBounds(1000, 560, 100, 30);
		
		
		
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				rowHeight=rowHeight+2;
				columnWidth=columnWidth+1;
				zoomAreaTable.setZoom(rowHeight,columnWidth);
				//makeGeneralTableIDU();
				//LifeTimeTable.revalidate();
				//tmpScrollPane.revalidate();
				//LifeTimeTable.repaint();
				//tmpScrollPane.repaint();
				//lifeTimePanel.repaint();

				
			}
		});
		
		zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.setBounds(1110, 560, 100, 30);
		
		zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				rowHeight=rowHeight-2;
				columnWidth=columnWidth-1;
				if(rowHeight<1){
					rowHeight=1;
				}
				if (columnWidth<1) {
					columnWidth=1;
				}
				zoomAreaTable.setZoom(rowHeight,columnWidth);
				
			}
		});
		
		//zoomInButton = new JButton("Zoom Out");
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
		/*
		showGridButton = new JButton("Show Grid"); 
		showGridButton.setBounds(800, 560, 100, 30);
		
		showGridButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				showGrid=!showGrid;
			    zoomAreaTable.showGrid(showGrid);
			    
			  } 
		});
		
		
		showGridButton.setVisible(false);
		*/
		uniformlyDistributedButton = new JButton("Same Width"); 
		uniformlyDistributedButton.setBounds(980, 0, 120, 30);
		
		uniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				//showGrid=!showGrid;
			    LifeTimeTable.uniformlyDistributed(1);
			    
			  } 
		});
		
		uniformlyDistributedButton.setVisible(false);
		
		notUniformlyDistributedButton = new JButton("Over Time"); 
		notUniformlyDistributedButton.setBounds(1100, 0, 120, 30);
		
		notUniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				//showGrid=!showGrid;
			    LifeTimeTable.notUniformlyDistributed(globalDataKeeper);
			    
			  } 
		});
		
		notUniformlyDistributedButton.setVisible(false);
		
		lifeTimePanel.add(zoomInButton);
		lifeTimePanel.add(zoomOutButton);
		lifeTimePanel.add(uniformlyDistributedButton);
		lifeTimePanel.add(notUniformlyDistributedButton);
		//lifeTimePanel.add(showGridButton);

		lifeTimePanel.add(zoomAreaLabel);
		
		lifeTimePanel.add(generalTableLabel);
		
	
		
		//generalTableLabel.setText("Tree");
		
//		tabbedPane.addTab("Statistics", null, statistics, null);
//		
//		GroupLayout gl_statistics = new GroupLayout(statistics);
//		gl_statistics.setHorizontalGroup(
//			gl_statistics.createParallelGroup(Alignment.LEADING)
//				.addGap(0, 1469, Short.MAX_VALUE)
//		);
//		gl_statistics.setVerticalGroup(
//			gl_statistics.createParallelGroup(Alignment.LEADING)
//				.addGap(0, 703, Short.MAX_VALUE)
//		);
//		statistics.setLayout(gl_statistics);
		
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setBounds(30, 30, 1300, 700);

		
	}
	
	
	private void makeGeneralTable() {
		
		int numberOfColumns=finalRows[0].length;
		int numberOfRows=finalRows.length;
		
		selectedRows=new ArrayList<Integer>();
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=finalRows[i][0];
			
		}
		
		generalModel=new MyTableModel(finalColumns, rows);
		
		final JvTable generalTable=new JvTable(generalModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
				generalTable.getColumnModel().getColumn(0).setMinWidth(150);
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(20);
				generalTable.getColumnModel().getColumn(i).setMaxWidth(20);
				generalTable.getColumnModel().getColumn(i).setMinWidth(20);
			}
		}
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=finalRows[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		      
		        if(selectedColumn==0){
		        	if (isSelected){
		        	/*if(hasFocus)
		        	{		        	
		        		System.out.println(row+" "+column);
		        		c.setBackground(Color.YELLOW);
		        		return c;
		        	}*/
		        	//else{
		        		//System.out.println(row+" "+column);
		        		c.setBackground(Color.YELLOW);
		        		return c;
		        	}
		        }
		        else{
		        	
		        	
		        	if (isSelected && hasFocus){
			        	
		        		c.setBackground(Color.YELLOW);
		        		return c;
			        }
		        	
		        }
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0&& numericValue<=segmentSize[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSize[0] && numericValue<=2*segmentSize[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSize[0] && numericValue<=3*segmentSize[0]){
		        			
		        			insersionColor=new Color(0,201,87);
		        		}
		        		else{
		        			insersionColor=new Color(0,100,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("U")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue> 0&& numericValue<=segmentSize[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>segmentSize[1] && numericValue<=2*segmentSize[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*segmentSize[1] && numericValue<=3*segmentSize[1]){
		        			
		        			insersionColor=new Color(28,134,238);
		        		}
		        		else{
		        			insersionColor=new Color(16,78,139);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSize[2] && numericValue<=2*segmentSize[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSize[2] && numericValue<=3*segmentSize[2]){
		        			
		        			insersionColor=new Color(205,0,0);
		        		}
		        		else{
		        			insersionColor=new Color(139,0,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	
			        //else if (column == 0){
			          //  c.setBackground(new Color(0xDDDDD));}
			        //else{ 
			          //  c.setBackground(new Color(0xFFFFFF));
			    	//}
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		
		        		if(tmpValue.equals("")){
		        			c.setBackground(Color.black);
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
			         
			         selectedRowsFromMouse = target.getSelectedRows();
			         selectedColumn = target.getSelectedColumn();
			         LifeTimeTable.repaint();
			         //System.out.println(selectedColumn);
				}
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         
			         selectedRowsFromMouse = target.getSelectedRows();
			         selectedColumn = target.getSelectedColumn();
			         //System.out.println(selectedColumn);
			         makeDetailedTable(finalColumns, finalRows,levelizedTable);
			         
			         LifeTimeTable.setCellSelectionEnabled(true);
			         LifeTimeTable.changeSelection(selectedRowsFromMouse[0], selectedColumn, false, false);
			         LifeTimeTable.requestFocus();
			         
			      }
			   }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");
						//if (e.getClickCount() == 1) {

							JTable target1 = (JTable)e.getSource();
							selectedColumn=target1.getSelectedColumn();
							selectedRowsFromMouse=target1.getSelectedRows();
							System.out.println(target1.getSelectedColumn());
							System.out.println(target1.getSelectedRow());
							for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
								System.out.println(generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							}
							if(target1.getSelectedColumn()==0){
								final JPopupMenu popupMenu = new JPopupMenu();
						        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
						        showDetailsItem.addActionListener(new ActionListener() {
	
						            @Override
						            public void actionPerformed(ActionEvent e) {
						                JOptionPane.showMessageDialog(null, "Right-click performed on table and choose DELETE");
						            }
						        });
						        popupMenu.add(showDetailsItem);
						        popupMenu.show(generalTable, e.getX(),e.getY());
						        
						        
							}
						//}
					}
				
			   }
		});
		
		
		LifeTimeTable=generalTable;
		
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
        tmpScrollPane.setBounds(300,0,950,300);
        tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
		
	
		
	}
	
private void makeGeneralTableIDU() {
	
		uniformlyDistributedButton.setVisible(false);
		zoomInButton.setVisible(true);
		zoomOutButton.setVisible(true);
		//showGridButton.setVisible(true);
		notUniformlyDistributedButton.setVisible(false);
		
		int numberOfColumns=finalRowsZoomArea[0].length;
		int numberOfRows=finalRowsZoomArea.length;
		
		selectedRows=new ArrayList<Integer>();
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=finalRowsZoomArea[i][0];
			
		}
		
		generalModel=new MyTableModel(finalColumnsZoomArea, rows);
		
		final JvTable generalTable=new JvTable(generalModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		//generalTable.setZoom(rowHeight,columnWidth);
		
		System.out.println(columnWidth+" "+rowHeight);
		
		if(rowHeight<1){
			rowHeight=1;
		}
		if (columnWidth<1) {
			columnWidth=1;
		}
		
		for(int i=0; i<generalTable.getRowCount(); i++){
				generalTable.setRowHeight(i, rowHeight);
				
		}

		//generalTable.setGridColor(new Color(0,0,0,100));
		
		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
				//generalTable.getColumnModel().getColumn(0).setMaxWidth(columnWidth);
				//generalTable.getColumnModel().getColumn(0).setMinWidth(columnWidth);
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
				//generalTable.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
				//generalTable.getColumnModel().getColumn(i).setMinWidth(columnWidth);
			}
		}
		
		int start=-1;
		int end=-1;
		if (globalDataKeeper.getPhaseCollectors()!=null && wholeCol!=-1) {
			start=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(wholeCol-1).getStartPos();
			end=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(wholeCol-1).getEndPos();
		}

		System.out.println(start);

		
		if(wholeCol!=-1){
			for(int i=0; i<generalTable.getColumnCount(); i++){
				//System.out.println(start+" "+generalTable.getColumnName(i));
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){
			
						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        
		        
		        
		        
		        String tmpValue=finalRowsZoomArea[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        if(column==wholeColZoomArea){
		        	
		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
		        	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		
        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
        			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
        			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

        			
	        		descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(selectedColumnZoomArea==0){
		    		
		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		
		        		String description="Table:"+finalRowsZoomArea[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getTotalChanges()+"\n";

		        		
		        		descriptionText.setText(description);
		        		
		        		return c;
		        		
		        		
		        	}
		        }
		        else{


		        	if(selectedFromTree.contains(finalRowsZoomArea[row][0])){


		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
			      
		        	
		        	if (isSelected && hasFocus){

		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
			        		description="Table:"+finalRowsZoomArea[row][0]+"\n";
			        		
			        		description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
			        		if(globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
			        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			
			        		}
			        		else{
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";
			        			
			        		}
			        		
			        		descriptionText.setText(description);
		        		}
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
			        }
		        	/*
		        	if(selectedColumn==column && selectedRow==row){
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
		        	}*/
		        	
		        	
		        }

		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		}
	        		else if(numericValue> 0&& numericValue<=segmentSize[1]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>segmentSize[1] && numericValue<=2*segmentSize[1]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*segmentSize[1] && numericValue<=3*segmentSize[1]){
	        			
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
			         
			         selectedRowsFromMouse = target.getSelectedRows();
			         selectedColumn = target.getSelectedColumn();
			         //selectedRow = target.getSelectedRow();

			         selectedColumnsFromMouse=target.getSelectedColumns();
			         
			         
			         
			         zoomAreaTable.repaint();
			         //System.out.println(selectedColumn);
				}
				
			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");
						//if (e.getClickCount() == 1) {

							JTable target1 = (JTable)e.getSource();
							//selectedColumn=target1.getSelectedColumn();
							selectedColumnsFromMouse=target1.getSelectedColumns();
							selectedRowsFromMouse=target1.getSelectedRows();
							System.out.println(target1.getSelectedColumns().length);
							System.out.println(target1.getSelectedRow());
							for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
								System.out.println(generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							}
							//if(target1.getSelectedColumn()==0){
								final JPopupMenu popupMenu = new JPopupMenu();
						        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
						        showDetailsItem.addActionListener(new ActionListener() {
	
						            @Override
						            public void actionPerformed(ActionEvent e) {
						            	selectedFromTree=new ArrayList<String>();
						            	zoomAreaTable.repaint();
						            }
						        });
						        popupMenu.add(showDetailsItem);
						        popupMenu.show(generalTable, e.getX(),e.getY());
						        						        
						        //generalTable.setComponentPopupMenu(popupMenu);
						        
							//}
						//}
					}
				
			   }
		});
		
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        wholeColZoomArea = generalTable.columnAtPoint(e.getPoint());
		        String name = generalTable.getColumnName(wholeColZoomArea);
		        System.out.println("Column index selected " + wholeColZoomArea + " " + name);
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
					            	wholeColZoomArea=-1;
					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		zoomAreaTable=generalTable;
		//LifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);
		
		
		
	}

private void makeGeneralTablePhases() {
	uniformlyDistributedButton.setVisible(true);
	//zoomInButton.setVisible(false);
	//zoomOutButton.setVisible(false);
	//showGridButton.setVisible(false);
	notUniformlyDistributedButton.setVisible(true);

	
	int numberOfColumns=finalRows[0].length;
	int numberOfRows=finalRows.length;
	
	selectedRows=new ArrayList<Integer>();
	
	String[][] rows=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rows[i][0]=finalRows[i][0];
		
	}
	
	generalModel=new MyTableModel(finalColumns, rows);
	
	final JvTable generalTable=new JvTable(generalModel);
	
	generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	generalTable.setShowGrid(false);
	generalTable.setIntercellSpacing(new Dimension(0, 0));
	System.out.println("HM?"+generalTable.getColumnCount());
	for(int i=0; i<generalTable.getColumnCount(); i++){
		if(i==0){
			generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);
			//generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
			//generalTable.getColumnModel().getColumn(0).setMinWidth(150);
		}
		else{
			//int tot=750/globalDataKeeper.getAllPPLTransitions().size();
			//int sizeOfColumn=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(i-1).getSize()*tot;
			
			generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
			//generalTable.getColumnModel().getColumn(i).setMaxWidth(sizeOfColumn);
			//generalTable.getColumnModel().getColumn(i).setMinWidth(70);
		}
	}
	
	generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=finalRows[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeCol){
	        	
	        	String description=table.getColumnName(column)+"\n";
	          	description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
        		description=description+"Total Changes For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
        		description=description+"Additions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
        		description=description+"Deletions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
        		description=description+"Updates For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";
	        	/*description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
    			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";
*/
    			/*description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
        			
        		*/
        		descriptionText.setText(description);
	        	
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        }
	        else if(selectedColumn==0){
	        	if (isSelected){
	        		
	        		if(finalRows[row][0].contains("Cluster")){
		        		String description="Cluster:"+finalRows[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirth()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeath()+"\n";
		        		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getTotalChanges()+"\n";
		        		
		        		descriptionText.setText(description);
	        		}
	        		else{
		        		String description="Table:"+finalRows[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getTotalChanges()+"\n";
		        		descriptionText.setText(description);

	        		}

	        		
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	
	        	if(selectedFromTree.contains(finalRows[row][0])){

	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		
	        		return c;
	        	}
	        	
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
	        			
		        		if(finalRows[row][0].contains("Cluster")){

			        		description=finalRows[row][0]+"\n";
			        		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n\n";

			        		description=description+table.getColumnName(column)+"\n";
			        		description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getStartPos()+"\n";
			        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getEndPos()+"\n\n";
			        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";

			        		/*if(globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
			        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			
			        		}
			        		else{
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";
			        			
			        		}*/
			        		
		        		}
		        		else{
		        			description=table.getColumnName(column)+"\n";
			        		description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getStartPos()+"\n";
			        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getEndPos()+"\n\n";
		        			description=description+"Table:"+finalRows[row][0]+"\n";
			        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
			        		
		        		}
		        		
		        		descriptionText.setText(description);

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
        		else if(numericValue> 0&& numericValue<=segmentSize[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSize[3] && numericValue<=2*segmentSize[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSize[3] && numericValue<=3*segmentSize[3]){
        			
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
        			c.setBackground(Color.gray);
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
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumn = target.getSelectedColumn();
		         LifeTimeTable.repaint();
		         //System.out.println(selectedColumn);
			}
//		      if (e.getClickCount() == 2) {
//		         JTable target = (JTable)e.getSource();
//		         
//		         selectedRowsFromMouse = target.getSelectedRows();
//		         selectedColumn = target.getSelectedColumn();
//		         //System.out.println(selectedColumn);
//		         makeDetailedTable(finalColumns, finalRows,levelizedTable);
//		         
//		         LifeTimeTable.setCellSelectionEnabled(true);
//		         LifeTimeTable.changeSelection(selectedRowsFromMouse[0], selectedColumn, false, false);
//		         LifeTimeTable.requestFocus();
//		         
//		      }
		   }
	});
	
	generalTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					System.out.println("Right Click");
					//if (e.getClickCount() == 1) {

						JTable target1 = (JTable)e.getSource();
						selectedColumn=target1.getSelectedColumn();
						selectedRowsFromMouse=new int[target1.getSelectedRows().length];
						selectedRowsFromMouse=target1.getSelectedRows();
						
						final String sSelectedColumn=generalTable.getColumnName(selectedColumn);
						final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						}
						//Arrays.fill(selectedRowsFromMouse, (Integer) null);
						//if(target1.getSelectedColumn()==0){
							JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent le) {
					            	if(sSelectedRow.contains("Cluster ")){
					            		showClusterSelectionToZoomArea(selectedColumn,sSelectedRow);

					            	}
					            	else{
					            		showSelectionToZoomArea(selectedColumn);
					            	}
					               // selectedRowsFromMouse=new int[];
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
					        clearSelectionItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent le) {
					            	
					            	selectedFromTree=new ArrayList<String>();
					            	LifeTimeTable.repaint();
					            }
					        });
					        popupMenu.add(clearSelectionItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					        //generalTable.setComponentPopupMenu(popupMenu);
					        
						//}
					//}
				}
			
		   }
	});
	
	generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        wholeCol = generalTable.columnAtPoint(e.getPoint());
	        String name = generalTable.getColumnName(wholeCol);
	        System.out.println("Column index selected " + wholeCol + " " + name);
	        generalTable.repaint();
	        makeGeneralTableIDU();
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
				            	wholeCol=-1;
				            	generalTable.repaint();
				            	makeGeneralTableIDU();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	LifeTimeTable=generalTable;
	
	tmpScrollPane.setViewportView(LifeTimeTable);
	tmpScrollPane.setAlignmentX(0);
	tmpScrollPane.setAlignmentY(0);
    tmpScrollPane.setBounds(300,30,950,265);
    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPane);
	
	
	
}



private void showSelectionToZoomArea(int selectedColumn){
	
	TableConstructionZoomArea table=new TableConstructionZoomArea(globalDataKeeper,tablesSelected,selectedColumn);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	//segmentSize=table.getSegmentSize();
	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTable();
	
	
	
}

private void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster){
	
	
	ArrayList<String> tablesOfCluster=new ArrayList<String>();
	for(int i=0; i <tablesSelected.size(); i++){
		String[] selectedClusterSplit= tablesSelected.get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
		ArrayList<String> namesOfTables=globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
		System.out.println(tablesSelected.get(i));
	}
	//ArrayList<String> tablesOfCluster = globalDataKeeper.getClusterCollectors().get(0).getClusters().get(Integer.parseInt(selectedClusterSplit[1])).getNamesOfTables();
	
	//TableConstructionZoomArea table=new TableConstructionZoomArea(globalDataKeeper,tablesOfCluster,selectedColumn);
	TableConstructionPhasesClusterTables table=new TableConstructionPhasesClusterTables(globalDataKeeper,tablesOfCluster);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	//segmentSize=table.getSegmentSize();
	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	//makeZoomAreaTable();
	makeZoomAreaTableForCluster();
	
	
}

private void makeZoomAreaTable() {
	
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	
	//selectedRows=new ArrayList<Integer>();
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
		
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	//zoomTable.setBackground(Color.DARK_GRAY);

	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	
	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			//generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
			//generalTable.getColumnModel().getColumn(0).setMinWidth(150);
		}
		else{
			int tot;
			if(selectedColumn>0){
				tot=800/globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(selectedColumn-1).getSize();
			}
			else{
				tot=800/globalDataKeeper.getAllPPLTransitions().size();
			}
			//int sizeOfColumn=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(i-1).getSize()*tot;
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(20);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea){
	        	
	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
	        	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom)+"\n";
    			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom)+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom)+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom)+"\n";

    		
	        	
	        	descriptionText.setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);
        		
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		//wholeCol=-1;
	        		String description="Table:"+finalRowsZoomArea[row][0]+"\n";
	        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
	        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
	        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
	        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
	        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
	        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
	        		descriptionText.setText(description);
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	if (isSelected && hasFocus){
		        	//Color lala=new Color(207,0,255,255);
	        		//wholeCol=-1;

	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
		        		description="Table:"+finalRowsZoomArea[row][0]+"\n";
		        		
		        		description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
		        		if(globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
		        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
		        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			
		        		}
		        		else{
		        			description=description+"Transition Changes:0"+"\n";
		        			description=description+"Additions:0"+"\n";
		        			description=description+"Deletions:0"+"\n";
		        			description=description+"Updates:0"+"\n";
		        			
		        		}
		        		
		        		descriptionText.setText(description);
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
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=segmentSize[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSize[3] && numericValue<=2*segmentSize[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSize[3] && numericValue<=3*segmentSize[3]){
        			
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
        			c.setBackground(Color.DARK_GRAY);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
		         //System.out.println(selectedColumn);
			}
		    /*if (e.getClickCount() == 2) {
		         JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         //System.out.println(selectedColumn);
		         makeDetailedTable(finalColumns, finalRows,levelizedTable);
		         
		         //LifeTimeTable.setCellSelectionEnabled(true);
		         //LifeTimeTable.changeSelection(selectedRowsFromMouse[0], selectedColumn, false, false);
		         //LifeTimeTable.requestFocus();
		         
		     }*/
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					//if (e.getClickCount() == 1) {

						JTable target1 = (JTable)e.getSource();
						selectedColumnZoomArea=target1.getSelectedColumn();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						final String sSelectedColumn=zoomTable.getColumnName(selectedColumnZoomArea);
						final ArrayList<String> tablesSelected = new ArrayList<String>();
						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}
						//if(target1.getSelectedColumn()==0){
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Right Click");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	JOptionPane.showMessageDialog(zoomTable, "Right click");
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
					        
						//}
					//}
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + wholeCol + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	//zoomAreaTable.setBackground(Color.DARK_GRAY);
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,350,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	//tmpScrollPaneZoomArea.setBackground(Color.DARK_GRAY);
	//tmpScrollPaneZoomArea.getViewport().setBackground(Color.GRAY);

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}

private void makeZoomAreaTableForCluster() {
	
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	
	//selectedRows=new ArrayList<Integer>();
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
		
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	//zoomTable.setBackground(Color.DARK_GRAY);
	
	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	

	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			//generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
			//generalTable.getColumnModel().getColumn(0).setMinWidth(150);
		}
		else{
			int tot=800/globalDataKeeper.getAllPPLTransitions().size();
			int sizeOfColumn=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(i-1).getSize()*tot;
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(sizeOfColumn);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(sizeOfColumn);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea){
	        	
	        	String description=table.getColumnName(column)+"\n";
	          	description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
        		description=description+"Total Changes For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
        		description=description+"Additions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
        		description=description+"Deletions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
        		description=description+"Updates For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";
	        	/*description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
    			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";
*/
    			/*description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRows[row][0]).
    					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
        			
        		*/
        		descriptionText.setText(description);
	        	
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		
	        		
		        		String description="Table:"+finalRowsZoomArea[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getTotalChanges()+"\n";
		        		descriptionText.setText(description);

	        	
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	
	        	
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
	        			
		        		
	        			description=table.getColumnName(column)+"\n";
		        		description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        				get(column-1).getStartPos()+"\n";
		        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        				get(column-1).getEndPos()+"\n\n";
	        			description=description+"Table:"+finalRowsZoomArea[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
		        		
		        		descriptionText.setText(description);

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
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=segmentSize[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSize[3] && numericValue<=2*segmentSize[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSize[3] && numericValue<=3*segmentSize[3]){
        			
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
        			c.setBackground(Color.DARK_GRAY);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
		         //System.out.println(selectedColumn);
			}
		    /*if (e.getClickCount() == 2) {
		         JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         //System.out.println(selectedColumn);
		         makeDetailedTable(finalColumns, finalRows,levelizedTable);
		         
		         //LifeTimeTable.setCellSelectionEnabled(true);
		         //LifeTimeTable.changeSelection(selectedRowsFromMouse[0], selectedColumn, false, false);
		         //LifeTimeTable.requestFocus();
		         
		     }*/
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					//if (e.getClickCount() == 1) {

						JTable target1 = (JTable)e.getSource();
						selectedColumnZoomArea=target1.getSelectedColumn();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						//final String sSelectedColumn=zoomTable.getColumnName(selectedColumnZoomArea);
						//final ArrayList<String> tablesSelected = new ArrayList<String>();
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}
						//if(target1.getSelectedColumn()==0){
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Right Click");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
				            		showSelectionToZoomArea(selectedColumnZoomArea);
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
					        
						//}
					//}
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + wholeCol + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	//zoomAreaTable.setBackground(Color.DARK_GRAY);
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,350,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	//tmpScrollPaneZoomArea.setBackground(Color.DARK_GRAY);
	//tmpScrollPaneZoomArea.getViewport().setBackground(Color.GRAY);

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}

	private void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
		levelizedTable=levelized;
		detailedModel=new MyTableModel(columns,rows);
		
		final JvTable tmpLifeTimeTable= new JvTable(detailedModel);
		
		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(levelized==true){
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					if(tmpLifeTimeTable.getColumnName(i).contains("v")){
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(100);
					}
					else{
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(25);
					}
				}
			}
		}
		else{
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					
					tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(20);
					
				}
			}
		}
		
		tmpLifeTimeTable.setName("LifeTimeTable");
		
		
		tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        
		        if(selectedColumn==0){
		        	if (isSelected){
		        		Color cl = new Color(255,69,0, 100);

		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        }
		        else{
		        	if (isSelected && hasFocus){
			        	
		        		c.setBackground(Color.YELLOW);
		        		return c;
			        }
		        	
		        }
		        
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSize[0] && numericValue<=2*segmentSize[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSize[0] && numericValue<=3*segmentSize[0]){
		        			
		        			insersionColor=new Color(0,201,87);
		        		}
		        		else{
		        			insersionColor=new Color(0,100,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("U")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>segmentSize[1] && numericValue<=2*segmentSize[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*segmentSize[1] && numericValue<=3*segmentSize[1]){
		        			
		        			insersionColor=new Color(28,134,238);
		        		}
		        		else{
		        			insersionColor=new Color(16,78,139);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSize[2] && numericValue<=2*segmentSize[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSize[2] && numericValue<=3*segmentSize[2]){
		        			
		        			insersionColor=new Color(205,0,0);
		        		}
		        		else{
		        			insersionColor=new Color(139,0,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		
		        		if(tmpValue.equals("")){
		        			c.setBackground(Color.black);
			        		return c; 
		        		}
		        		else{
		        			if(columnName.contains("v")){
		        				c.setBackground(Color.lightGray);
		        				if(levelized==false){
		        					setToolTipText(columnName);
		        				}
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
		
		tmpLifeTimeTable.setOpaque(true);
	    
		tmpLifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener());
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	    tmpLifeTimeTable.addKeyListener(new MyKeyListener());
	    
	    
	    tmpLifeTimeTable.addMouseListener(new MouseAdapter() {
			
			   public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			    	  JTable target = (JTable)e.getSource();
				         
				      int row = target.getSelectedRow();
				      int column = target.getSelectedColumn();
				         
				      makeGeneralTable();
				         
				      LifeTimeTable.setCellSelectionEnabled(true);
				         
				      LifeTimeTable.changeSelection(row, column, false, false);
				      LifeTimeTable.requestFocus();
			    	  
			         
			         
			      }
			   }
		});
	    
	    
	    LifeTimeTable=tmpLifeTimeTable;
	
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
        tmpScrollPane.setBounds(300,0,950,300);
        tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
		
		
	}
	
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow=LifeTimeTable.getSelectedRow();
            
            selectedRows.add(selectedRow);
     
        }
    }
	
	private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
           
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			
			if(selectedRows.isEmpty()||selectedRows.size()==1){
				
				JOptionPane.showMessageDialog(null, "Choose two rows first!!!");
				
				return;
				
			}
			
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_S){
				String[] tmpValues=new String[LifeTimeTable.getColumnCount()];
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					tmpValues[i]=(String) LifeTimeTable.getValueAt(selectedRows.get(0), i);
					
					
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(0)][i]=(String) LifeTimeTable.getValueAt(selectedRows.get(1), i);
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(1)][i]=tmpValues[i];
				}
				
				makeDetailedTable(finalColumns,finalRows,true);
				
				selectedRows=new ArrayList<Integer>();
			}	
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void importData(String fileName) throws IOException, RecognitionException {
		
		//Worker w = new Worker(fileName);
		//w.work();
		
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String line;
		
		while(true) {
			line = br.readLine();
			if (line == null) 
				break;
			//System.out.println(line);
			if(line.contains("Project-name")){
				String[] projectNameTable=line.split(":");
				projectName=projectNameTable[1];
			}
			else if(line.contains("Dataset-txt")){
				String[] datasetTxtTable=line.split(":");
				datasetTxt=datasetTxtTable[1];
			}
			else if(line.contains("Input-csv")){
				String[] inputCsvTable=line.split(":");
				inputCsv=inputCsvTable[1];
			}
			else if(line.contains("Assessement1-output")){
				String[] outputAss1=line.split(":");
				outputAssessment1=outputAss1[1];
			}
			else if(line.contains("Assessement2-output")){
				String[] outputAss2=line.split(":");
				outputAssessment2=outputAss2[1];
			}
			else if(line.contains("Transition-xml")){
				String[] transitionXmlTable=line.split(":");
				transitionsFile=transitionXmlTable[1];
			}
			
			
		};	
		
		br.close();
		
		
		System.out.println("Project Name:"+projectName);
		System.out.println("Dataset txt:"+datasetTxt);
		System.out.println("Input Csv:"+inputCsv);
		System.out.println("Output Assessment1:"+outputAssessment1);
		System.out.println("Output Assessment2:"+outputAssessment2);
		System.out.println("Transitions File:"+transitionsFile);

		//globalDataKeeper=null;
		globalDataKeeper=new GlobalDataKeeper(datasetTxt,transitionsFile);
		globalDataKeeper.setData();
		System.out.println(globalDataKeeper.getAllPPLTables().size());
		resultsDataKeeper = new ResultsDataKeeper();
		//= w.getDataKeeper();
		//System.out.println("!!"+globalDataKeeper.getDataFolder());
		
//		ImportSchemas filesToImportData=new ImportSchemas(fileName);
//		filesToImportData.loadDataset();
//		ArrayList<Schema> allHecSchemas = filesToImportData.getAllHecSchemas();
		//allTransitions=filesToImportData.getAllTransitions();

		
//		PPLSchemasConstruction pplSchemas = new PPLSchemasConstruction(allHecSchemas);
//		pplSchemas.makePPLSchemas();
		//allPPLSchemas=data.getAllPPLSchemas();
		
//		PPLTablesConstruction pplTables = new PPLTablesConstruction(allPPLSchemas);
//		pplTables.makeAllPPLTables();
		//allTables=data.getAllPPLTables();
		
//		System.out.println(allTables.size());
//		AtomicChangeConstruction atomicChangesC = new AtomicChangeConstruction(allTransitions);
//		atomicChangesC.makeAtomicChanges();
		//ArrayList<AtomicChange> atomicChanges=data.getAtomicChanges();
		
//		TableChangeConstruction tableChangesC = new TableChangeConstruction(atomicChanges, allTables);
//		tableChangesC.makeTableChanges();
		//TreeMap<String,TableChange> tableChanges=data.getAllTableChanges();
		
//		PPLTransitionConstruction pplTransitionC = new PPLTransitionConstruction(allPPLSchemas, tableChanges);
//		pplTransitionC.makePPLTransitions();
		//allPPLTransitions=data.getAllPPLTransitions();
		

//		for (Map.Entry<String,PPLTable> tr : globalDataKeeper.getAllPPLTables().entrySet()) {
//			
//			PPLTable lala=tr.getValue();
//			
//			TableChange letssee=lala.getTableChanges();
//
//			TreeMap<String,ArrayList<AtomicChange>> kaka=letssee.getTableAtomicChanges();
//			
//			for(Map.Entry<String, ArrayList<AtomicChange>> trr:kaka.entrySet()){
//				
//				ArrayList<AtomicChange> jaja1=trr.getValue();
//				
//				for(int i=0; i<jaja1.size(); i++){
//					
//					System.out.println("!!: "+jaja1.get(i).toString());
//					
//				}
//
//			}
//			
//			
//			
//			
//			System.out.println("\n");
//			
//		}
		
        System.out.println(fileName);

        fillTable();
        fillTree();

		currentProject=fileName;
		currentProjectDataFolder=globalDataKeeper.getDataFolder();
		
	}
	
	public void fillTable() {
		TableConstructionIDU table=new TableConstructionIDU(globalDataKeeper);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		segmentSize=table.getSegmentSize();
		System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		finalColumnsZoomArea=columns;
		finalRowsZoomArea=rows;
		tabbedPane.setSelectedIndex(0);
		makeGeneralTableIDU();
		
		timeWeight = (float)0.5;
        changeWeight = (float)0.5;
        preProcessingTime = false;
        preProcessingChange = false;
        if(globalDataKeeper.getAllPPLTransitions().size()<56){
        	numberOfPhases=40;
        }
        else{
        	numberOfPhases = 56;
        }
	    numberOfClusters =14;
        
        System.out.println(timeWeight+" "+changeWeight);
        
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);

		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);

		
		//for(int i=0; i<PhaseExtractionParameters.DATASET_AR.size(); i++){
			
		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		try {
			mainEngine.extractReportAssessment1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mainEngine.extractReportAssessment2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//}
		
		/*try {
			mainEngine.extractWinnersReport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		mainEngine.connectTransitionsWithPhases(globalDataKeeper);
		globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
		System.out.println("HOW MANY?"+globalDataKeeper.getPhaseCollectors().get(0).getSize());
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,b,d,c);
		mainEngine2.extractClusters(numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
		mainEngine2.print();
		
		if(globalDataKeeper.getPhaseCollectors().size()!=0){
			TableConstructionWithClusters tableP=new TableConstructionWithClusters(globalDataKeeper);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
			segmentSize=tableP.getSegmentSize();
			System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("CP: "+columnsP.length+" RP: "+rowsP.length);

			finalColumns=columnsP;
			finalRows=rowsP;
			tabbedPane.setSelectedIndex(0);
			makeGeneralTablePhases();
			fillClustersTree();
		}
		
	}
	
	public void fillTree(){
		
		 TreeConstructionGeneral tc=new TreeConstructionGeneral(globalDataKeeper);
		 tablesTree=new JTree();
		 tablesTree=tc.constructTree();
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							//if (e.getClickCount() == 1) {
								JTree target1 = (JTree)e.getSource();
								//selectedColumn=target1.getSelectedColumn();
								
								//final String lala=target1.getLastSelectedPathComponent().toString();
								
								//System.out.println(selectedFromTree);
								
								//if(target1.getSelectedColumn()==0){
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							                LifeTimeTable.repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 //=new JScrollPane(tablesTree,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			//     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);
		 
		 treeLabel.setText("General Tree");

		 //sideMenu.removeAll();
		 //sideMenu.add(tablesTreePanel);
		 sideMenu.revalidate();
		 sideMenu.repaint();
		
		
	}
	
	public void fillPhasesTree(){
		
		 TreeConstructionPhases tc=new TreeConstructionPhases(globalDataKeeper);
		 tablesTree=tc.constructTree();
		 //tablesTree.repaint();
		 
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							//if (e.getClickCount() == 1) {
								JTree target1 = (JTree)e.getSource();
								//selectedColumn=target1.getSelectedColumn();
								
								//final String lala=target1.getLastSelectedPathComponent().toString();
								
								//System.out.println(selectedFromTree);
								
								//if(target1.getSelectedColumn()==0){
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							                LifeTimeTable.repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);
		 
		 treeLabel.setText("Phases Tree");

		 
		 //sideMenu.removeAll();
		 //sideMenu.add(tablesTreePanel);
		 sideMenu.revalidate();
		 sideMenu.repaint();
		
	}
	
	public void fillClustersTree(){
		
		 TreeConstructionPhasesWithClusters tc=new TreeConstructionPhasesWithClusters(globalDataKeeper);
		 //tablesTree=new JTree();
		 tablesTree=tc.constructTree();
		 //tablesTree.repaint();
		 
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							//if (e.getClickCount() == 1) {
								JTree target1 = (JTree)e.getSource();
								//selectedColumn=target1.getSelectedColumn();
								
								//final String lala=target1.getLastSelectedPathComponent().toString();
								
								//System.out.println(selectedFromTree);
								
								//if(target1.getSelectedColumn()==0){
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							                LifeTimeTable.repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);

		 treeLabel.setText("Clusters Tree");

		 
		 //sideMenu.removeAll();
		 //sideMenu.add(tablesTreePanel);
		 sideMenu.revalidate();
		 sideMenu.repaint();
		 
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void showKLongLivedTables(){
		
		labelLongLivedTables.setText("");
		labelLongLivedTables.setText(" Long Lived Tables");
		labelLongLivedTables.setBounds(15,0,170,50);
		
		jListKLongLivedTables.setBounds(0, 0, 180, 200);
		Color color=new Color(000,191,255);
		jListKLongLivedTables.setBackground(color);
		jListKLongLivedTables.setModel(listModelLongLivedTables);
		jListKLongLivedTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		MouseListener mouseListener = new MouseAdapter() {  
			public void mouseClicked(MouseEvent e) {  
				if (e.getClickCount() == 2) {  
					int index = jListKLongLivedTables.locationToIndex(e.getPoint());  
					System.out.println("Double clicked on Item " + index);
					/*if(assistantList.size()!=0){
						assistantList.remove(assistantList.size()-1);
						assistantList.add(resultsDataKeeper.getLongLivedTables().get(index));
						
						statistics.remove(chartPanel);
						
						chart=new LongLivedTablesVisualization();
						chart.draw(resultsLLTR);
						chartPanel=chart.getChart();
						
						statistics.revalidate();
						statistics.repaint();
						statistics.add(chartPanel);
					}*/
				}  
			}  
		};  
		jListKLongLivedTables.addMouseListener(mouseListener); 
		
		jScrollPaneKLongLivedTables=new JScrollPane(jListKLongLivedTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKLongLivedTables.setBounds(20, 40, 180, 200);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelLongLivedTables);
		statistics.add(jScrollPaneKLongLivedTables);
		
		for(int i=0; i<resultsDataKeeper.getLongLivedTables().size(); i++){
			listModelLongLivedTables.removeAllElements();
		}
		
		for(int i=0; i<resultsDataKeeper.getLongLivedTables().size(); i++){
			listModelLongLivedTables.add(i, resultsDataKeeper.getLongLivedTables().get(i).getName());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void showKMostUpdatedTables(){
		
		labelMostUpdatedTables.setText("");
		labelMostUpdatedTables.setText(" Most Updated Tables");
		labelMostUpdatedTables.setBounds(15,0,200,50);
		
		jListKMostUpdatedTables.setBounds(0, 0, 230, 200);
		Color color=new Color(000,191,255);
		jListKMostUpdatedTables.setBackground(color);
		jListKMostUpdatedTables.setModel(listModelMostUpdatedTables);
		jListKMostUpdatedTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		 
		
		
		MouseListener mouseListener = new MouseAdapter() {  
			public void mouseClicked(MouseEvent e) {  
				if (e.getClickCount() == 2) {  
					int index = jListKMostUpdatedTables.locationToIndex(e.getPoint());  
					System.out.println("Double clicked on Item " + index);
					/*if(assistantListLineChart.size()!=0){
						assistantListLineChart.remove(assistantListLineChart.size()-1);
						ArrayList<PPLTable> tmp=assistantListLineChart;
						
						assistantListLineChart=new ArrayList<PPLTable>();
						
						for(int k=0;k<tmp.size();k++){
							assistantListLineChart.add(tmp.get(k));
						}
						
						int found=0;
						
						for(int i=0;i<assistantListLineChart.size();i++){
							if(resultsDataKeeper.getMostUpdatedTables().get(index).equals(assistantListLineChart.get(i))){
								found=1;
							}
						}
						
						if(found==0){
							assistantListLineChart.add(resultsDataKeeper.getMostUpdatedTables().get(index));
						}
						else{
							found=0;
						}
						
						statistics.remove(chartPanel);
						
						try {
							l=new MostUpdatedTablesVisualization(globalDataKeeper.getAllPPLTransitions());
							l.draw(resultsMUTR);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						chartPanel=l.getChart();
						
						statistics.revalidate();
						statistics.repaint();
						statistics.add(chartPanel);
					}*/
				}  
			}  
		};
		jListKMostUpdatedTables.addMouseListener(mouseListener); 
		
		
		jScrollPaneKMostUpdatedTables=new JScrollPane(jListKMostUpdatedTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostUpdatedTables.setBounds(20, 40, 230, 200);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostUpdatedTables);
		statistics.add(jScrollPaneKMostUpdatedTables);
		
		for(int i=0; i<resultsDataKeeper.getMostUpdatedTables().size(); i++){
			listModelMostUpdatedTables.removeAllElements();
		}
		
		for(int i=0; i<resultsDataKeeper.getMostUpdatedTables().size(); i++){
			listModelMostUpdatedTables.add(i, resultsDataKeeper.getMostUpdatedTables().get(i).getName());
		}
		
	}
	
	public void showKMostUpdatedAttributes(){
		
		labelMostUpdatedAttributes.setText("");
		labelMostUpdatedAttributes.setText(" Most Updated Attributes");
		labelMostUpdatedAttributes.setBounds(15,0,200,50);
		
		
		jTextAreaMostUpdatedAttributes.setEditable(false);
		jTextAreaMostUpdatedAttributes.setBounds(0, 1, 600, 300);
		Color color=new Color(000,191,255);
		jTextAreaMostUpdatedAttributes.setBackground(color);
		jTextAreaMostUpdatedAttributes.setTabSize(20);
		jTextAreaMostUpdatedAttributes.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		String assistant="Attribute Name"+"\tNumber Of Changes"+"\tExists In Table";
		for(int i=0; i<resultsDataKeeper.getMostUpdatedAttributes().size(); i++){
			
			assistant=assistant+"\n"+resultsDataKeeper.getMostUpdatedAttributes().get(i).getName()+"\t"
					+ resultsDataKeeper.getMostUpdatedAttributes().get(i).getTotalAttributeChanges()
					+"\t"+resultsDataKeeper.getMostUpdatedAttributes().get(i).getTable().getName();
			
		}
		
		jTextAreaMostUpdatedAttributes.setText(assistant);
		
		jScrollPaneKMostUpdatedAttributes=new JScrollPane(jTextAreaMostUpdatedAttributes,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostUpdatedAttributes.setBounds(20, 40, 600, 300);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostUpdatedAttributes);
		statistics.add(jScrollPaneKMostUpdatedAttributes);
		
	}
	
	public void showKMostIntensiveTablesBetweenTwoSchemas(String firstSchema,String secondSchema){
		
		
		
		labelMostIntensiveTables.setText("");
		labelMostIntensiveTables.setText(" Most Intensive Tables From Version "+firstSchema+" To Version "+secondSchema);
		labelMostIntensiveTables.setBounds(15,0,450,50);
		
		
		jTextAreaMostIntensiveTables.setEditable(false);
		jTextAreaMostIntensiveTables.setBounds(0, 1,450, 300);
		Color color=new Color(000,191,255);
		jTextAreaMostIntensiveTables.setBackground(color);
		jTextAreaMostIntensiveTables.setTabSize(20);
		jTextAreaMostIntensiveTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		String assistant="Table Name"+"\tNumber Of Changes";
		for(int i=0; i<resultsDataKeeper.getMostIntensiveTables().size(); i++){
			
			assistant=assistant+"\n"+resultsDataKeeper.getMostIntensiveTables().get(i).getName()+"\t"
					+ resultsDataKeeper.getMostIntensiveTables().get(i).getCurrentChanges();
			
		}
		
		jTextAreaMostIntensiveTables.setText(assistant);
		
		jScrollPaneKMostIntensiveTables=new JScrollPane(jTextAreaMostIntensiveTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostIntensiveTables.setBounds(20, 40, 450, 300);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostIntensiveTables);
		statistics.add(jScrollPaneKMostIntensiveTables);
		
		
	}
}
