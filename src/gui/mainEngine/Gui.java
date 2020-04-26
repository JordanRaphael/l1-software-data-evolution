package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import gui.dialogs.EnlargeTable;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class Gui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JItemsCreator jItemsCreator = new JItemsCreator();

	private JPanel contentPane;
	protected JPanel lifeTimePanel = new JPanel();
	protected JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	private MyTableModel detailedModel = null;
	protected MyTableModel generalModel = null;
	protected MyTableModel zoomModel = null;

	protected JvTable LifeTimeTable = null;
	protected JvTable zoomAreaTable = null;

	protected JScrollPane tmpScrollPane = new JScrollPane();
	protected JScrollPane treeScrollPane = new JScrollPane();
	protected JScrollPane tmpScrollPaneZoomArea = new JScrollPane();

	protected ArrayList<Integer> selectedRows = new ArrayList<Integer>();

	protected String[] finalColumns = null;
	protected String[][] finalRows = null;

	protected String[] finalColumnsZoomArea = null;
	protected String[][] finalRowsZoomArea = null;
	protected String[] firstLevelUndoColumnsZoomArea = null;
	protected String[][] firstLevelUndoRowsZoomArea = null;
	protected String currentProject = null;
	protected String project = null;

	protected Integer[] segmentSize = new Integer[4];
	protected Integer[] segmentSizeZoomArea = new Integer[4];
	protected Integer[] segmentSizeDetailedTable = new Integer[3];

	protected Float timeWeight = null;
	protected Float changeWeight = null;
	protected Double birthWeight = null;
	protected Double deathWeight = null;
	protected Double changeWeightCl = null;

	protected Integer numberOfPhases = null;
	protected Integer numberOfClusters = null;
	protected Boolean preProcessingTime = null;
	protected Boolean preProcessingChange = null;

	public String projectName = "";
	public String datasetTxt = "";
	public String inputCsv = "";
	public String outputAssessment1 = "";
	public String outputAssessment2 = "";
	public String transitionsFile = "";
	protected ArrayList<String> selectedFromTree = new ArrayList<String>();

	protected JTree tablesTree = new JTree();
	protected JPanel sideMenu = jItemsCreator.createJPanel();
	protected JPanel tablesTreePanel = new JPanel();
	private JPanel descriptionPanel = new JPanel();
	protected JLabel treeLabel;
	private JLabel generalTableLabel;
	private JLabel zoomAreaLabel;
	private JLabel descriptionLabel;
	protected JTextArea descriptionText;
	public JButton zoomInButton;
	protected JButton zoomOutButton;
	protected JButton uniformlyDistributedButton;
	protected JButton notUniformlyDistributedButton;
	protected JButton showThisToPopup;

	protected int[] selectedRowsFromMouse;
	protected int selectedColumn = -1;
	protected int selectedColumnZoomArea = -1;
	protected int wholeCol = -1;
	protected int wholeColZoomArea = -1;

	public int rowHeight = 1;
	public int columnWidth = 1;

	protected ArrayList<String> tablesSelected = new ArrayList<String>();

	protected boolean showingPld = false;

	protected JButton undoButton;
	private JMenu mnProject;
	private JMenuItem mntmInfo;

	BusinessLogic businessLogic = new BusinessLogic(Gui.this);

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
					// return;
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
		gl_tablesTreePanel.setHorizontalGroup(gl_tablesTreePanel.createParallelGroup(Alignment.LEADING));
		gl_tablesTreePanel.setVerticalGroup(gl_tablesTreePanel.createParallelGroup(Alignment.LEADING));

		tablesTreePanel.setLayout(gl_tablesTreePanel);

		treeLabel = jItemsCreator.createJLabel("", "Tree", 10, 370, 260, 40, Color.WHITE);

		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);

		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(gl_descriptionPanel.createParallelGroup(Alignment.LEADING));
		gl_descriptionPanel.setVerticalGroup(gl_descriptionPanel.createParallelGroup(Alignment.LEADING));

		descriptionPanel.setLayout(gl_descriptionPanel);

		descriptionText = new JTextArea();
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

		JButton buttonHelp = jItemsCreator.createJButton("Help", 900, 900, 80, 40);
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = "To open a project, you must select a .txt file that contains the names ONLY of "
						+ "the SQL files of the dataset that you want to use." + "\n"
						+ "The .txt file must have EXACTLY the same name with the folder "
						+ "that contains the DDL Scripts of the dataset." + "\n"
						+ "Both .txt file and dataset folder must be in the same folder.";
				JOptionPane.showMessageDialog(null, message);
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
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE));

		tabbedPane.addTab("LifeTime Table", null, lifeTimePanel, null);

		GroupLayout gl_lifeTimePanel = new GroupLayout(lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(
				gl_lifeTimePanel.createParallelGroup(Alignment.LEADING).addGap(0, 1469, Short.MAX_VALUE));
		gl_lifeTimePanel.setVerticalGroup(
				gl_lifeTimePanel.createParallelGroup(Alignment.LEADING).addGap(0, 743, Short.MAX_VALUE));
		lifeTimePanel.setLayout(gl_lifeTimePanel);

		generalTableLabel = jItemsCreator.createJLabel("Parallel Lives Diagram", "", 300, 0, 150, 30, Color.BLACK);

		zoomAreaLabel = jItemsCreator.createJLabel("", "<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>", 1255,
				325, 15, 300, Color.BLACK);

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

				EnlargeTable showEnlargmentPopup = new EnlargeTable(finalRowsZoomArea, finalColumnsZoomArea,
						segmentSizeZoomArea);
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);

				showEnlargmentPopup.setVisible(true);

			}
		});

		showThisToPopup.setVisible(false);

		undoButton = jItemsCreator.createJButton("Undo", 680, 560, 100, 30);

		undoButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (firstLevelUndoColumnsZoomArea != null) {
					finalColumnsZoomArea = firstLevelUndoColumnsZoomArea;
					finalRowsZoomArea = firstLevelUndoRowsZoomArea;
					businessLogic.makeZoomAreaTableForCluster();
				}

			}
		});

		undoButton.setVisible(false);

		uniformlyDistributedButton = jItemsCreator.createJButton("Same Width", 980, 0, 120, 30);

		// TODO Move to BusinessLogic
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
				businessLogic.notUniformlyDistributedButtonMouseListener();
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


	protected void makeDetailedTable(String[] columns, String[][] rows, final boolean levelized) {

		detailedModel = new MyTableModel(columns, rows);

		final JvTable tmpLifeTimeTable = new JvTable(detailedModel);

		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		if (levelized == true) {
			for (int i = 0; i < tmpLifeTimeTable.getColumnCount(); i++) {
				if (i == 0) {
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				} else {
					if (tmpLifeTimeTable.getColumnName(i).contains("v")) {
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(100);
					} else {
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(25);
					}
				}
			}
		} else {
			for (int i = 0; i < tmpLifeTimeTable.getColumnCount(); i++) {
				if (i == 0) {
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				} else {

					tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(20);

				}
			}
		}

		tmpLifeTimeTable.setName("LifeTimeTable");

		tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				String tmpValue = (String) table.getValueAt(row, column);
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);

				if (selectedColumn == 0) {
					if (isSelected) {
						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);

						return c;
					}
				} else {
					if (isSelected && hasFocus) {

						c.setBackground(Color.YELLOW);
						return c;
					}

				}

				try {
					int numericValue = Integer.parseInt(tmpValue);
					Color insersionColor = null;

					if (columnName.equals("I")) {
						if (numericValue == 0) {
							insersionColor = new Color(255, 231, 186);
						} else if (numericValue > 0 && numericValue <= segmentSizeDetailedTable[0]) {

							insersionColor = new Color(193, 255, 193);
						} else if (numericValue > segmentSizeDetailedTable[0]
								&& numericValue <= 2 * segmentSizeDetailedTable[0]) {
							insersionColor = new Color(84, 255, 159);
						} else if (numericValue > 2 * segmentSizeDetailedTable[0]
								&& numericValue <= 3 * segmentSizeDetailedTable[0]) {

							insersionColor = new Color(0, 201, 87);
						} else {
							insersionColor = new Color(0, 100, 0);
						}
						c.setBackground(insersionColor);
					}

					if (columnName.equals("U")) {
						if (numericValue == 0) {
							insersionColor = new Color(255, 231, 186);
						} else if (numericValue > 0 && numericValue <= segmentSizeDetailedTable[1]) {

							insersionColor = new Color(176, 226, 255);
						} else if (numericValue > segmentSizeDetailedTable[1]
								&& numericValue <= 2 * segmentSizeDetailedTable[1]) {
							insersionColor = new Color(92, 172, 238);
						} else if (numericValue > 2 * segmentSizeDetailedTable[1]
								&& numericValue <= 3 * segmentSizeDetailedTable[1]) {

							insersionColor = new Color(28, 134, 238);
						} else {
							insersionColor = new Color(16, 78, 139);
						}
						c.setBackground(insersionColor);
					}

					if (columnName.equals("D")) {
						if (numericValue == 0) {
							insersionColor = new Color(255, 231, 186);
						} else if (numericValue > 0 && numericValue <= segmentSizeDetailedTable[2]) {

							insersionColor = new Color(255, 106, 106);
						} else if (numericValue > segmentSizeDetailedTable[2]
								&& numericValue <= 2 * segmentSizeDetailedTable[2]) {
							insersionColor = new Color(255, 0, 0);
						} else if (numericValue > 2 * segmentSizeDetailedTable[2]
								&& numericValue <= 3 * segmentSizeDetailedTable[2]) {

							insersionColor = new Color(205, 0, 0);
						} else {
							insersionColor = new Color(139, 0, 0);
						}
						c.setBackground(insersionColor);
					}

					return c;
				} catch (Exception e) {

					if (tmpValue.equals("")) {
						c.setBackground(Color.black);
						return c;
					} else {
						if (columnName.contains("v")) {
							c.setBackground(Color.lightGray);
							if (levelized == false) {
								setToolTipText(columnName);
							}
						} else {
							Color tableNameColor = new Color(205, 175, 149);
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

		JPanel panelToAdd = new JPanel();

		GroupLayout gl_panel = new GroupLayout(panelToAdd);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING));
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

			int selectedRow = LifeTimeTable.getSelectedRow();

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

	public void setDescription(String descr) {
		descriptionText.setText(descr);
	}

	public String getProject() {

		return project;
	}

	public void setProject(String project) {

		this.project = project;
	}

}