package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.server.UID;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import gui.tableElements.commons.JvTable;

public class EventListenerHandler {

	private BusinessLogic businessLogic;
	private Gui gui;
	
	public EventListenerHandler(BusinessLogic businessLogic, Gui gui) {
		
		this.businessLogic = businessLogic;
		this.gui = gui;
		
	}
	
	public TreeSelectionListener createTreeSelectionListener() {
		
		TreeSelectionListener listener = (new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		});
		
		return listener;
		
	}
	
	public ActionListener  createProject() {
		
		ActionListener listener =  new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				businessLogic.createProjectAction();
			}
		};
		
		return listener;
		
	}
	
	public MouseAdapter createOneClickAdapter(final JvTable table) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumn = target.getSelectedColumn();
					table.repaint();
				}

			}
		};
		
		return adapter;
	}
	
	public MouseAdapter createReleaseMouseAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					gui.selectedColumn = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = new int[target1.getSelectedRows().length];
					gui.selectedRowsFromMouse = target1.getSelectedRows();

					final String sSelectedRow = (String) gui.generalTable.getValueAt(target1.getSelectedRow(), 0);
					gui.tablesSelected = new ArrayList<String>();

					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						gui.tablesSelected.add((String) gui.generalTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
					}

					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent le) {
							if (sSelectedRow.contains("Cluster ")) {
								businessLogic.showClusterSelectionToZoomArea(gui.selectedColumn, sSelectedRow);

							} else {
								businessLogic.showSelectionToZoomArea(gui.selectedColumn);
							}
						}
					});
					popupMenu.add(showDetailsItem);
					JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
					clearSelectionItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent le) {

							gui.selectedFromTree = new ArrayList<String>();
							gui.LifeTimeTable.repaint();
						}
					});
					popupMenu.add(clearSelectionItem);
					popupMenu.show(gui.generalTable, e.getX(), e.getY());

				}
			}
		};
		
		return adapter;
		
	}
	
	public  MouseAdapter createColumnClickEvent() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gui.wholeCol = gui.generalTable.columnAtPoint(e.getPoint());
				String name = gui.generalTable.getColumnName(gui.wholeCol);
				System.out.println("Column index selected " + gui.wholeCol + " " + name);
				gui.generalTable.repaint();
				if (gui.showingPld) {
					businessLogic.makeGeneralTableIDU();
				}
			}
		};
		
		return adapter;
	}
	
	public MouseAdapter createRightClickAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
					clearColumnSelectionItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							gui.wholeCol = -1;
							gui.generalTable.repaint();
							if (gui.showingPld) {
								businessLogic.makeGeneralTableIDU();
							}
						}
					});
					popupMenu.add(clearColumnSelectionItem);
					JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String sSelectedRow = gui.finalRows[0][0];
							System.out.println("?" + sSelectedRow);
							gui.tablesSelected = new ArrayList<String>();
							for (int i = 0; i < gui.finalRows.length; i++)
								gui.tablesSelected.add((String) gui.generalTable.getValueAt(i, 0));

							if (!sSelectedRow.contains("Cluster ")) {

								businessLogic.showSelectionToZoomArea(gui.wholeCol);
							} else {
								businessLogic.showClusterSelectionToZoomArea(gui.wholeCol, "");
							}

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(gui.generalTable, e.getX(), e.getY());

				}

			}

		}; 
		
		
		return adapter;
		
	}

	public MouseAdapter createZoomTableRightClickAdapter(final JvTable zoomTable) {

		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					gui.selectedColumnZoomArea = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						tablesSelected.add((String) zoomTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}

				}

			}
		};
		
		return adapter;
		
	}
	
	public DefaultTableCellRenderer createGeneralTableDefaultTableCellRenderer() {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				String tmpValue = gui.finalRows[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);
				
				GlobalDataKeeper globalDataKeeper = businessLogic.getGlobalDataKeeper();
				
				if (column == gui.wholeCol && gui.wholeCol != 0) {

					String description = table.getColumnName(column) + "\n";
					
					description = description + "First Transition ID:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getStartPos()
							+ "\n";
					description = description + "Last Transition ID:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getEndPos()
							+ "\n";
					description = description + "Total Changes For This Phase:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getTotalUpdates()
							+ "\n";
					description = description + "Additions For This Phase:" + globalDataKeeper.getPhaseCollectors()
							.get(0).getPhases().get(column - 1).getTotalAdditionsOfPhase() + "\n";
					description = description + "Deletions For This Phase:" + globalDataKeeper.getPhaseCollectors()
							.get(0).getPhases().get(column - 1).getTotalDeletionsOfPhase() + "\n";
					description = description + "Updates For This Phase:" + globalDataKeeper.getPhaseCollectors().get(0)
							.getPhases().get(column - 1).getTotalUpdatesOfPhase() + "\n";

					gui.descriptionText.setText(description);

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				} else if (gui.selectedColumn == 0) {
					if (isSelected) {

						if (gui.finalRows[row][0].contains("Cluster")) {
							String description = "Cluster:" + gui.finalRows[row][0] + "\n";

							description = description + "Birth Version Name:" + globalDataKeeper.getClusterCollectors()
									.get(0).getClusters().get(row).getBirthSqlFile() + "\n";
							description = description + "Birth Version ID:"
									+ globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirth()
									+ "\n";
							description = description + "Death Version Name:" + globalDataKeeper.getClusterCollectors()
									.get(0).getClusters().get(row).getDeathSqlFile() + "\n";
							description = description + "Death Version ID:"
									+ globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeath()
									+ "\n";
							description = description + "Tables:" + globalDataKeeper.getClusterCollectors().get(0)
									.getClusters().get(row).getNamesOfTables().size() + "\n";
							description = description + "Total Changes:" + globalDataKeeper.getClusterCollectors()
									.get(0).getClusters().get(row).getTotalChanges() + "\n";

							gui.descriptionText.setText(description);
						} else {
							String description = "Table:" + gui.finalRows[row][0] + "\n";

							description = description + "Birth Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getBirth() + "\n";
							description = description + "Birth Version ID:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getBirthVersionID()
									+ "\n";
							description = description + "Death Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getDeath() + "\n";
							description = description + "Death Version ID:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getDeathVersionID()
									+ "\n";
							description = description + "Total Changes:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getTotalChanges()
									+ "\n";
							gui.descriptionText.setText(description);

						}

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}
				} else {

					if (gui.selectedFromTree.contains(gui.finalRows[row][0])) {

						Color cl = new Color(255, 69, 0, 100);
						c.setBackground(cl);

						return c;
					}

					if (isSelected && hasFocus) {

						String description = "";
						if (!table.getColumnName(column).contains("Table name")) {

							if (gui.finalRows[row][0].contains("Cluster")) {

								description = gui.finalRows[row][0] + "\n";
								description = description + "Tables:" + globalDataKeeper.getClusterCollectors().get(0)
										.getClusters().get(row).getNamesOfTables().size() + "\n\n";

								description = description + table.getColumnName(column) + "\n";
								description = description + "First Transition ID:" + globalDataKeeper
										.getPhaseCollectors().get(0).getPhases().get(column - 1).getStartPos() + "\n";
								description = description + "Last Transition ID:" + globalDataKeeper
										.getPhaseCollectors().get(0).getPhases().get(column - 1).getEndPos() + "\n\n";
								description = description + "Total Changes For This Phase:" + tmpValue + "\n";

							} else {
								description = table.getColumnName(column) + "\n";
								description = description + "First Transition ID:" + globalDataKeeper
										.getPhaseCollectors().get(0).getPhases().get(column - 1).getStartPos() + "\n";
								description = description + "Last Transition ID:" + globalDataKeeper
										.getPhaseCollectors().get(0).getPhases().get(column - 1).getEndPos() + "\n\n";
								description = description + "Table:" + gui.finalRows[row][0] + "\n";
								description = description + "Birth Version Name:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getBirth() + "\n";
								description = description + "Birth Version ID:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getBirthVersionID()
										+ "\n";
								description = description + "Death Version Name:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getDeath() + "\n";
								description = description + "Death Version ID:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRows[row][0]).getDeathVersionID()
										+ "\n";
								description = description + "Total Changes For This Phase:" + tmpValue + "\n";

							}

							gui.descriptionText.setText(description);

						}

						Color cl = new Color(255, 69, 0, 100);
						c.setBackground(cl);

						return c;
					}
				}

				try {
					int numericValue = Integer.parseInt(tmpValue);
					Color insersionColor = null;
					setToolTipText(Integer.toString(numericValue));

					if (numericValue == 0) {
						insersionColor = new Color(154, 205, 50, 200);
					} else if (numericValue > 0 && numericValue <= gui.segmentSize[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > gui.segmentSize[3] && numericValue <= 2 * gui.segmentSize[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * gui.segmentSize[3] && numericValue <= 3 * gui.segmentSize[3]) {

						insersionColor = new Color(28, 134, 238);
					} else {
						insersionColor = new Color(16, 78, 139);
					}
					c.setBackground(insersionColor);

					return c;
				} catch (Exception e) {

					if (tmpValue.equals("")) {
						c.setBackground(Color.gray);
						return c;
					} else {
						if (columnName.contains("v")) {
							c.setBackground(Color.lightGray);
							setToolTipText(columnName);
						} else {
							Color tableNameColor = new Color(205, 175, 149);
							c.setBackground(tableNameColor);
						}
						return c;
					}
				}
			}

			
		}; 
		
		return renderer;
		
	}

	public DefaultTableCellRenderer createZoomTableCellRenderer(final String[][] rowsZoom) {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				String tmpValue = gui.finalRowsZoomArea[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);
				GlobalDataKeeper globalDataKeeper = businessLogic.getGlobalDataKeeper();

				if (column == gui.wholeColZoomArea) {

					String description = "Transition ID:" + table.getColumnName(column) + "\n";
					description = description + "Old Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
					description = description + "New Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

					description = description + "Transition Changes:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterChangesForOneTr(rowsZoom)
							+ "\n";
					description = description + "Additions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterAdditionsForOneTr(rowsZoom)
							+ "\n";
					description = description + "Deletions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterDeletionsForOneTr(rowsZoom)
							+ "\n";
					description = description + "Updates:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterUpdatesForOneTr(rowsZoom)
							+ "\n";

					gui.descriptionText.setText(description);
					Color cl = new Color(255, 69, 0, 100);
					c.setBackground(cl);

					return c;
				} else if (gui.selectedColumnZoomArea == 0) {
					if (isSelected) {
						String description = "Table:" + gui.finalRowsZoomArea[row][0] + "\n";
						description = description + "Birth Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirth() + "\n";
						description = description + "Birth Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirthVersionID()
								+ "\n";
						description = description + "Death Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeath() + "\n";
						description = description + "Death Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeathVersionID()
								+ "\n";
						description = description + "Total Changes:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0])
										.getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)),
												Integer.parseInt(table.getColumnName(table.getColumnCount() - 1)))
								+ "\n";
						gui.descriptionText.setText(description);

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}
				} else {
					if (isSelected && hasFocus) {

						String description = "";
						if (!table.getColumnName(column).contains("Table name")) {
							description = "Table:" + gui.finalRowsZoomArea[row][0] + "\n";

							description = description + "Old Version Name:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()
									+ "\n";
							description = description + "New Version Name:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()
									+ "\n";
							if (globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getTableChanges()
									.getTableAtChForOneTransition(
											Integer.parseInt(table.getColumnName(column))) != null) {
								description = description + "Transition Changes:" + globalDataKeeper.getAllPPLTables()
										.get(gui.finalRowsZoomArea[row][0]).getTableChanges()
										.getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))
										.size() + "\n";
								description = description + "Additions:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0])
												.getNumberOfAdditionsForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";
								description = description + "Deletions:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0])
												.getNumberOfDeletionsForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";
								description = description + "Updates:"
										+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0])
												.getNumberOfUpdatesForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";

							} else {
								description = description + "Transition Changes:0" + "\n";
								description = description + "Additions:0" + "\n";
								description = description + "Deletions:0" + "\n";
								description = description + "Updates:0" + "\n";

							}

							gui.descriptionText.setText(description);
						}

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}

				}

				try {
					int numericValue = Integer.parseInt(tmpValue);
					Color insersionColor = null;
					setToolTipText(Integer.toString(numericValue));

					if (numericValue == 0) {
						insersionColor = new Color(0, 100, 0);
					} else if (numericValue > 0 && numericValue <= gui.segmentSizeZoomArea[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > gui.segmentSizeZoomArea[3] && numericValue <= 2 * gui.segmentSizeZoomArea[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * gui.segmentSizeZoomArea[3]
							&& numericValue <= 3 * gui.segmentSizeZoomArea[3]) {

						insersionColor = new Color(28, 134, 238);
					} else {
						insersionColor = new Color(16, 78, 139);
					}
					c.setBackground(insersionColor);

					return c;
				} catch (Exception e) {

					if (tmpValue.equals("")) {
						c.setBackground(Color.DARK_GRAY);
						return c;
					} else {
						if (columnName.contains("v")) {
							c.setBackground(Color.lightGray);
							setToolTipText(columnName);
						} else {
							Color tableNameColor = new Color(205, 175, 149);
							c.setBackground(tableNameColor);
						}
						return c;
					}

				}
			}
		}; 
		
		return renderer;
	}

	public MouseAdapter createGuiTablesTreeRightMouseListener() {
		
		MouseAdapter adapter = new MouseAdapter() {
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
		};
		
		return adapter;
	}

	public MouseAdapter createZoomTableMouseClickedAdapter(final JvTable zoomTable) {

		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gui.wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
				String name = zoomTable.getColumnName(gui.wholeColZoomArea);
				System.out.println("Column index selected " + gui.wholeCol + " " + name);
				zoomTable.repaint();
			}
		};
		
		return adapter;
	}

	public MouseAdapter createZoomTableRightClickAdapter2(final JvTable zoomTable) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							gui.wholeColZoomArea = -1;
							zoomTable.repaint();
						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(zoomTable, e.getX(), e.getY());

				}
			}

		};
		
		return adapter;
	}

	public TreeSelectionListener createTreeSelectionListener2() {
		
		TreeSelectionListener listener = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ae) {
				TreePath selection = ae.getPath();
				gui.selectedFromTree.add(selection.getLastPathComponent().toString());
				System.out.println(selection.getLastPathComponent().toString() + " is selected");

			}
		};
		
		return listener;
	}

	public MouseAdapter createTreeSelectionListener3() {
		
		MouseAdapter adapter = new MouseAdapter() {
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
		};
		
		return adapter;
	}

	public DefaultTableCellRenderer createZoomTableCellRenderer2() {
		
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				String tmpValue = gui.finalRowsZoomArea[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);
				GlobalDataKeeper globalDataKeeper = businessLogic.getGlobalDataKeeper();

				if (column == gui.wholeColZoomArea && gui.wholeColZoomArea != 0) {

					String description = table.getColumnName(column) + "\n";
					description = description + "First Transition ID:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getStartPos()
							+ "\n";
					description = description + "Last Transition ID:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getEndPos()
							+ "\n";
					description = description + "Total Changes For This Phase:"
							+ globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(column - 1).getTotalUpdates()
							+ "\n";
					description = description + "Additions For This Phase:" + globalDataKeeper.getPhaseCollectors()
							.get(0).getPhases().get(column - 1).getTotalAdditionsOfPhase() + "\n";
					description = description + "Deletions For This Phase:" + globalDataKeeper.getPhaseCollectors()
							.get(0).getPhases().get(column - 1).getTotalDeletionsOfPhase() + "\n";
					description = description + "Updates For This Phase:" + globalDataKeeper.getPhaseCollectors().get(0)
							.getPhases().get(column - 1).getTotalUpdatesOfPhase() + "\n";

					gui.descriptionText.setText(description);

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				} else if (gui.selectedColumnZoomArea == 0) {
					if (isSelected) {

						String description = "Table:" + gui.finalRowsZoomArea[row][0] + "\n";
						description = description + "Birth Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirth() + "\n";
						description = description + "Birth Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirthVersionID()
								+ "\n";
						description = description + "Death Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeath() + "\n";
						description = description + "Death Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeathVersionID();
								// TODO Auto-generated method stub			+ "\n";
						description = description + "Total Changes:"
								+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getTotalChanges()
								+ "\n";
						gui.descriptionText.setText(description);

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}
				} else {

					if (isSelected && hasFocus) {

						String description = "";
						if (!table.getColumnName(column).contains("Table name")) {

							description = "Transition " + table.getColumnName(column) + "\n";

							description = description + "Old Version:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()
									+ "\n";
							description = description + "New Version:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()
									+ "\n\n";

							description = description + "Table:" + gui.finalRowsZoomArea[row][0] + "\n";
							description = description + "Birth Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getBirth()
									+ "\n";
							description = description + "Birth Version ID:" + globalDataKeeper.getAllPPLTables()
									.get(gui.finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
							description = description + "Death Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(gui.finalRowsZoomArea[row][0]).getDeath()
									+ "\n";
							description = description + "Death Version ID:" + globalDataKeeper.getAllPPLTables()
									.get(gui.finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
							description = description + "Total Changes For This Phase:" + tmpValue + "\n";

							gui.descriptionText.setText(description);

						}

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}

				}

				try {
					int numericValue = Integer.parseInt(tmpValue);
					Color insersionColor = null;
					setToolTipText(Integer.toString(numericValue));

					if (numericValue == 0) {
						insersionColor = new Color(0, 100, 0);
					} else if (numericValue > 0 && numericValue <= gui.segmentSizeZoomArea[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > gui.segmentSizeZoomArea[3] && numericValue <= 2 * gui.segmentSizeZoomArea[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * gui.segmentSizeZoomArea[3]
							&& numericValue <= 3 * gui.segmentSizeZoomArea[3]) {

						insersionColor = new Color(28, 134, 238);
					} else {
						insersionColor = new Color(16, 78, 139);
					}
					c.setBackground(insersionColor);

					return c;
				} catch (Exception e) {

					if (tmpValue.equals("")) {
						c.setBackground(Color.DARK_GRAY);
						return c;
					} else {
						if (columnName.contains("v")) {
							c.setBackground(Color.lightGray);
							setToolTipText(columnName);
						} else {
							Color tableNameColor = new Color(205, 175, 149);
							c.setBackground(tableNameColor);
						}
						return c;
					}

				}
			}
		};
		
		return renderer;
	}
	
		
}