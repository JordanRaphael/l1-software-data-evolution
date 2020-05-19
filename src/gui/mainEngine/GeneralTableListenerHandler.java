package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import phaseAnalyzer.commons.Phase;
import tableClustering.clusterExtractor.commons.Cluster;

public class GeneralTableListenerHandler {

	private BusinessLogic businessLogic;
	private Gui gui;
	
	public GeneralTableListenerHandler(BusinessLogic businessLogic, Gui gui) {
		
		this.businessLogic = businessLogic;
		this.gui = gui;
		
	}
	
	
	public MouseAdapter createPhasesMouseClickedAdapter() {
		MouseAdapter adapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 1) {
				JTable target = (JTable) e.getSource();

				gui.selectedRowsFromMouse = target.getSelectedRows();
				gui.selectedColumn = target.getSelectedColumn();
				gui.LifeTimeTable.repaint();
			}

		}
	   };
	   
	   return adapter;
	}


	public MouseAdapter createPhasesMouseClickedButton3Adapter(final JvTable generalTable) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					gui.selectedColumn = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = new int[target1.getSelectedRows().length];
					gui.selectedRowsFromMouse = target1.getSelectedRows();

					final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(), 0);
					gui.tablesSelected = new ArrayList<String>();

					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						gui.tablesSelected
								.add((String) generalTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
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
					popupMenu.show(generalTable, e.getX(), e.getY());

				}

			}
		};
		
		return adapter;
	}


	public MouseAdapter createPhasesMouseColumnClickedAdapter(final JvTable generalTable) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gui.wholeCol = generalTable.columnAtPoint(e.getPoint());
				String name = generalTable.getColumnName(gui.wholeCol);
				System.out.println("Column index selected " + gui.wholeCol + " " + name);
				generalTable.repaint();
				if (gui.showingPld) {
					businessLogic.makeGeneralTableIDU();
				}
			}
		};
		
		return adapter;
		
	}


	public MouseListener createPhasesRightMouseClickedAdapter(final JvTable generalTable) {
		
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
							generalTable.repaint();
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
								gui.tablesSelected.add((String) generalTable.getValueAt(i, 0));

							if (!sSelectedRow.contains("Cluster ")) {

								businessLogic.showSelectionToZoomArea(gui.wholeCol);
							} else {
								businessLogic.showClusterSelectionToZoomArea(gui.wholeCol, "");
							}

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(generalTable, e.getX(), e.getY());

				}
			}

		};
		
		return adapter;
	}


	public DefaultTableCellRenderer createIDUDefaultTableCellRenderer() {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				
				GlobalDataKeeper globalDataKeeper = businessLogic.getGlobalDataKeeper();
				String tmpValue = gui.finalRowsZoomArea[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);

				c.setForeground(fr);
				setOpaque(true);

				if (column == gui.wholeColZoomArea && gui.wholeColZoomArea != 0) {

					String description = "Transition ID:" + table.getColumnName(column) + "\n";
					description = description + "Old Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
					description = description + "New Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

					description = description
							+ "Transition Changes:" + globalDataKeeper.getAllPPLTransitions()
									.get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()
							+ "\n";
					description = description
							+ "Additions:" + globalDataKeeper.getAllPPLTransitions()
									.get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()
							+ "\n";
					description = description
							+ "Deletions:" + globalDataKeeper.getAllPPLTransitions()
									.get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()
							+ "\n";
					description = description
							+ "Updates:" + globalDataKeeper.getAllPPLTransitions()
									.get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()
							+ "\n";

					gui.descriptionText.setText(description);

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				} else if (gui.selectedColumnZoomArea == 0) {

					if (isSelected) {
						Color cl = new Color(255, 69, 0, 100);
						c.setBackground(cl);

						String description = "Table:" + gui.finalRowsZoomArea[row][0] + "\n";
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
						description = description + "Total Changes:" + globalDataKeeper.getAllPPLTables()
								.get(gui.finalRowsZoomArea[row][0]).getTotalChanges() + "\n";

						gui.descriptionText.setText(description);

						return c;

					}
				} else {

					if (gui.selectedFromTree.contains(gui.finalRowsZoomArea[row][0])) {

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);

						return c;
					}

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
						insersionColor = new Color(154, 205, 50, 200);
					} else if (numericValue > 0 && numericValue <= gui.segmentSizeZoomArea[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > gui.segmentSizeZoomArea[3]
							&& numericValue <= 2 * gui.segmentSizeZoomArea[3]) {
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
						c.setBackground(Color.GRAY);
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


	public MouseAdapter createIDUOneMouseClickAdapter(final IDUTableRenderer renderer) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumnZoomArea = target.getSelectedColumn();
					renderer.setSelCol(gui.selectedColumnZoomArea);
					target.getSelectedColumns();

					gui.zoomAreaTable.repaint();
				}
			}
		};
		
		return adapter;
	}


	public MouseAdapter createIDURightClickRowAdapter(final JvTable generalTable) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					target1.getSelectedColumns();
					gui.selectedRowsFromMouse = target1.getSelectedRows();
					System.out.println(target1.getSelectedColumns().length);
					System.out.println(target1.getSelectedRow());
					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						System.out.println(generalTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
					}
					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							gui.selectedFromTree = new ArrayList<String>();
							gui.zoomAreaTable.repaint();
						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(generalTable, e.getX(), e.getY());

				}

			}
		};
		
		return adapter;
	}


	public MouseAdapter createIDURightClickAdapter(final JvTable generalTable, final IDUTableRenderer renderer) {
		
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
							renderer.setWholeCol(gui.wholeColZoomArea);

							generalTable.repaint();
						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(generalTable, e.getX(), e.getY());

				}
			}

		};
		return adapter;
	}
	
	public MouseAdapter createIDUMouseEvent(final JvTable generalTable, final IDUTableRenderer renderer) {
        MouseAdapter adapter = new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e) {
                gui.wholeColZoomArea = generalTable.columnAtPoint(e.getPoint());
                renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));

 

                generalTable.repaint();
            }
        };
        
        return adapter;
    }
	

}
