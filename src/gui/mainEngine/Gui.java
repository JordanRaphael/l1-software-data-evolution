package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.antlr.v4.runtime.RecognitionException;

import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;
import gui.dialogs.EnlargeTable;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;


public class Gui extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JItemsCreator jItemsCreator= new JItemsCreator();
	
	private JPanel contentPane;
	protected JPanel lifeTimePanel = new JPanel();
	protected JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	protected MyTableModel zoomModel = null;

	protected JvTable LifeTimeTable=null;
	protected JvTable zoomAreaTable=null;
	
	
	
	private JScrollPane tmpScrollPane =new JScrollPane();
	protected JScrollPane treeScrollPane= new JScrollPane();
	protected JScrollPane tmpScrollPaneZoomArea =new JScrollPane();
	
	
	
	protected ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	protected GlobalDataKeeper globalDataKeeper=null;

	
	
	protected String[] finalColumns=null;
	protected String[][] finalRows=null;
	
	protected String[] finalColumnsZoomArea=null;
	protected String[][] finalRowsZoomArea=null;
	private String[] firstLevelUndoColumnsZoomArea=null;
	private String[][] firstLevelUndoRowsZoomArea=null;
	protected String currentProject=null;
	protected String project=null;
	
	
	
	protected Integer[] segmentSize=new Integer[4];
	protected Integer[] segmentSizeZoomArea=new Integer[4];
	protected Integer[] segmentSizeDetailedTable=new Integer[3];


	
	protected Float timeWeight=null;
	protected Float changeWeight=null;
	protected Double birthWeight=null;
	protected Double deathWeight=null;
	protected Double changeWeightCl=null;

	protected Integer numberOfPhases=null;
	protected Integer numberOfClusters=null;
	protected Boolean preProcessingTime=null;
	protected Boolean preProcessingChange=null;
	
	public String projectName="";
	public String datasetTxt="";
	public String inputCsv="";
	public String outputAssessment1="";
	public String outputAssessment2="";
	public String transitionsFile="";
	protected ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	protected JTree tablesTree=new JTree();
	protected JPanel sideMenu = jItemsCreator.createJPanel();
	protected JPanel tablesTreePanel=new JPanel();
	private JPanel descriptionPanel=new JPanel();
	protected JLabel treeLabel;
	private JLabel generalTableLabel;
	private JLabel zoomAreaLabel;
	private JLabel descriptionLabel;
	protected JTextArea descriptionText;
	public JButton zoomInButton;
	protected JButton zoomOutButton;
	private JButton uniformlyDistributedButton;
	private JButton notUniformlyDistributedButton;
	protected JButton showThisToPopup;


	protected int[] selectedRowsFromMouse;
	private int selectedColumn=-1;
	protected int selectedColumnZoomArea=-1;
	protected int wholeCol=-1;
	protected int wholeColZoomArea=-1;
	
	public int rowHeight=1;
	public int columnWidth=1;

	private ArrayList<String> tablesSelected = new ArrayList<String>();

	protected boolean showingPld=false;
	
	private JButton undoButton;
	private JMenu mnProject;
	private JMenuItem mntmInfo;
	
	private BusinessLogic businessLogic = new BusinessLogic(Gui.this);

	
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
				businessLogic.createProjectAction();
			}
		});
		mnFile.add(mntmCreateProject);
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				businessLogic.loadProjectAction();								
			}
		});
		mnFile.add(mntmLoadProject);
		
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				businessLogic.editProjectAction();
			}
		});
		mnFile.add(mntmEditProject);
		
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				businessLogic.showLifetimeTableAction();
			}
		});
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 businessLogic.showGeneralLifetimeIDUAction();
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				businessLogic.showGeneralLifetimePhasesPLDAction();
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				businessLogic.showGeneralLifetimePhasesWithClustersPLDAction();
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		
		mnTable.add(mntmShowLifetimeTable);
		
		tablesTreePanel.setBounds(10, 400, 260, 180);
		tablesTreePanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		gl_tablesTreePanel.setVerticalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		
		tablesTreePanel.setLayout(gl_tablesTreePanel);
		
		treeLabel = jItemsCreator.createJLabel("", "Tree", 10, 370, 260, 40, Color.WHITE);
		
		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		gl_descriptionPanel.setVerticalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		
		descriptionPanel.setLayout(gl_descriptionPanel);
		
		descriptionText=new JTextArea();
		descriptionText.setBounds(5, 5, 250, 170);
		descriptionText.setForeground(Color.BLACK);
		descriptionText.setText("");
		descriptionText.setBackground(Color.LIGHT_GRAY);
		
		descriptionPanel.add(descriptionText);
		
		descriptionLabel = jItemsCreator.createJLabel("", "Description", 10, 160, 260, 40, Color.WHITE);
		
		sideMenu.add(treeLabel);
		sideMenu.add(tablesTreePanel);
		
		sideMenu.add(descriptionLabel);
		sideMenu.add(descriptionPanel);

		lifeTimePanel.add(sideMenu);
		
		JButton buttonHelp = jItemsCreator.createJButton("Help", 900,900 , 80, 40);
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
		
		mnProject = new JMenu("Project");
		menuBar.add(mnProject);
		
		mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				businessLogic.infoAction();
			}
		});
		mnProject.add(mntmInfo);
		menuBar.add(buttonHelp);
		
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
		
		
		generalTableLabel = jItemsCreator.createJLabel("Parallel Lives Diagram", "", 300, 0, 150, 30, Color.BLACK);
		
		zoomAreaLabel = jItemsCreator.createJLabel("", "<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>", 1255, 325, 15, 300, Color.BLACK);
		
		zoomInButton = jItemsCreator.createJButton("Zoom In", 1000, 560, 100, 30);		
		
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
					businessLogic.zoomInAction();
			   }
		});
		
		
		zoomOutButton = jItemsCreator.createJButton("Zoom Out", 1110, 560, 100, 30);
		
		zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
					businessLogic.zoomOutAction();
				}	
		});
		
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
		
		
		showThisToPopup = new JButton("Enlarge");
		showThisToPopup.setBounds(800, 560, 100, 30);
		
		showThisToPopup.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				EnlargeTable showEnlargmentPopup= new EnlargeTable(finalRowsZoomArea,finalColumnsZoomArea,segmentSizeZoomArea);
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				
				showEnlargmentPopup.setVisible(true);
								
			}
		});
		
		showThisToPopup.setVisible(false);
		
		undoButton = jItemsCreator.createJButton("Undo", 680, 560, 100, 30);
		
		undoButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				if (firstLevelUndoColumnsZoomArea!=null) {
					finalColumnsZoomArea=firstLevelUndoColumnsZoomArea;
					finalRowsZoomArea=firstLevelUndoRowsZoomArea;
					makeZoomAreaTableForCluster();
				}
				
			}
		});
		
		undoButton.setVisible(false);
				
		uniformlyDistributedButton = jItemsCreator.createJButton("Same Width", 980, 0, 120, 30); 
		
		//TODO Move to BusinessLogic
		uniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
			    LifeTimeTable.uniformlyDistributed(1);
			    
			  } 
		});
		
		uniformlyDistributedButton.setVisible(false);
		
		notUniformlyDistributedButton = jItemsCreator.createJButton("Over Time", 1100, 0, 120, 30); 
		
		notUniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
			    LifeTimeTable.notUniformlyDistributed(globalDataKeeper);
			    
			  } 
		});
		
		notUniformlyDistributedButton.setVisible(false);
		
		lifeTimePanel.add(zoomInButton);
		lifeTimePanel.add(undoButton);
		lifeTimePanel.add(zoomOutButton);
		lifeTimePanel.add(uniformlyDistributedButton);
		lifeTimePanel.add(notUniformlyDistributedButton);
		lifeTimePanel.add(showThisToPopup);

		lifeTimePanel.add(zoomAreaLabel);
		
		lifeTimePanel.add(generalTableLabel);
		
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setBounds(30, 30, 1300, 700);

	}

	//TODO Move makeGeneralTableIDU() to BusinessLogic
	

	//TODO Move to BusinessLogic
protected void makeGeneralTablePhases() {
	uniformlyDistributedButton.setVisible(true);
	
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
	
	
	for(int i=0; i<generalTable.getColumnCount(); i++){
		if(i==0){
			generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);
		}
		else{
			generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
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
	        
	        if(column==wholeCol && wholeCol!=0){
	        	
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
			}

		   }
	});
	
	generalTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					selectedColumn=target1.getSelectedColumn();
					selectedRowsFromMouse=new int[target1.getSelectedRows().length];
					selectedRowsFromMouse=target1.getSelectedRows();
					
					final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
					tablesSelected = new ArrayList<String>();

					for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
						tablesSelected.add((String) generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
					}
					
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
	        if (showingPld) {
		        businessLogic.makeGeneralTableIDU();
			}
	    }
	});
	
	generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
				        clearColumnSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	wholeCol=-1;
				            	generalTable.repaint();
				            	if(showingPld){
				            		businessLogic.makeGeneralTableIDU();
				            	}
				            }
				        });
				        popupMenu.add(clearColumnSelectionItem);
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
								String sSelectedRow=finalRows[0][0];
								System.out.println("?"+sSelectedRow);
				            	tablesSelected=new ArrayList<String>();
				            	for(int i=0; i<finalRows.length; i++)
				            		tablesSelected.add((String) generalTable.getValueAt(i, 0));

				            	if(!sSelectedRow.contains("Cluster ")){
				            		
				            		showSelectionToZoomArea(wholeCol);	
				            	}
				            	else{
				            		showClusterSelectionToZoomArea(wholeCol, "");
				            	}
				            	
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

//TODO Move to BusinessLogic
private void showSelectionToZoomArea(int selectedColumn){
	
	TableConstructionZoomArea table=new TableConstructionZoomArea(globalDataKeeper,tablesSelected,selectedColumn);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	segmentSizeZoomArea = table.getSegmentSize();

	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTable();	
	
}

//TODO Move to BusinessLogic
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
	
	PldConstruction table;
	if (selectedColumn==0) {
		table= new TableConstructionClusterTablesPhasesZoomA(globalDataKeeper,tablesOfCluster);
	}
	else{
		table= new TableConstructionZoomArea(globalDataKeeper,tablesOfCluster,selectedColumn);
	}
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	segmentSizeZoomArea = table.getSegmentSize();
	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTableForCluster();
	
	
}

private void makeZoomAreaTable() {
	showingPld=false;
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	

	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	
	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			
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
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
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
			}
		    
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					selectedColumnZoomArea=target1.getSelectedColumn();
					selectedRowsFromMouse=target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
					for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
						tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}
						
					
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
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
}

private void makeZoomAreaTableForCluster() {
	showingPld=false;
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	undoButton.setVisible(true);
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
		
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	
	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	

	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
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
	        
	        if(column==wholeColZoomArea && wholeColZoomArea!=0){
	        	
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
	        			
		        		
	        			description="Transition "+table.getColumnName(column)+"\n";
		        		
	        			description=description+"Old Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n\n";
		
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
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
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
			}
		   
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						selectedColumnZoomArea=target1.getSelectedColumn();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}
		            	if (zoomTable.getColumnName(selectedColumnZoomArea).contains("Phase")) {

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
				            		firstLevelUndoColumnsZoomArea=finalColumnsZoomArea;
					            	firstLevelUndoRowsZoomArea=finalRowsZoomArea;
				            		showSelectionToZoomArea(selectedColumnZoomArea);
									
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
		            	}
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
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
}

	protected void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[0] && numericValue<=2*segmentSizeDetailedTable[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[0] && numericValue<=3*segmentSizeDetailedTable[0]){
		        			
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[1] && numericValue<=2*segmentSizeDetailedTable[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[1] && numericValue<=3*segmentSizeDetailedTable[1]){
		        			
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[2] && numericValue<=2*segmentSizeDetailedTable[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[2] && numericValue<=3*segmentSizeDetailedTable[2]){
		        			
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
	     
	    JScrollPane detailedScrollPane = jItemsCreator.createJScrollPane(0, 0, 1280, 650);
	    detailedScrollPane.setViewportView(tmpLifeTimeTable);
	    detailedScrollPane.setAlignmentX(0);
	    detailedScrollPane.setAlignmentY(0);
       
	    detailedScrollPane.setCursor(getCursor());
	    
	    JDialog detailedDialog = jItemsCreator.createJDialog(100, 100, 1300, 700);
	    
	    JPanel panelToAdd=new JPanel();
	    
	    GroupLayout gl_panel = new GroupLayout(panelToAdd);
	    gl_panel.setHorizontalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
	    gl_panel.setVerticalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
		panelToAdd.setLayout(gl_panel);
	    
	    panelToAdd.add(detailedScrollPane);
	    detailedDialog.getContentPane().add(panelToAdd);
	    detailedDialog.setVisible(true);
		
		
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

	
	
	
	public void setDescription(String descr){
		descriptionText.setText(descr);
	}
	
	public String getProject() {
		
		return project;
	}
	
	public void setProject( String project) {
		
		this.project = project;
	}

}