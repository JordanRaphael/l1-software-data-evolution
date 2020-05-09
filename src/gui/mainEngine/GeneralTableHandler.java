package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.GlobalDataKeeper;

public class GeneralTableHandler implements ITablesListenerHandler {
	
	private BusinessLogic businessLogic;
	
	public GeneralTableHandler(BusinessLogic businessLogic) {
		
		this.businessLogic = businessLogic;
		
	}
	
	@Override
	public DefaultTableCellRenderer createDefaultTableCellRenderer() {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				
				Gui gui = businessLogic.getGui();
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
				} else if (gui.selectedColumn == 0){
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

	@Override
	public MouseAdapter createOneClickMouseAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Gui gui = businessLogic.getGui();
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

	@Override
	public MouseAdapter createReleaseMouseAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Gui gui = businessLogic.getGui();
				if (e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("Right Click");

					final JTable target1 = (JTable) e.getSource();
					gui.selectedColumn = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = new int[target1.getSelectedRows().length];
					gui.selectedRowsFromMouse = target1.getSelectedRows();

					String sSelectedRow = (String) gui.generalTable.getValueAt(target1.getSelectedRow(), 0);
					gui.tablesSelected = new ArrayList<String>();

					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						gui.tablesSelected.add((String) gui.generalTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
					}

					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent le) {
							Gui gui = businessLogic.getGui();
							String sSelectedRow = (String) gui.generalTable.getValueAt(target1.getSelectedRow(), 0);
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
							Gui gui = businessLogic.getGui();
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

	@Override
	public MouseAdapter createColumnClickEvent() {
			
			MouseAdapter adapter = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Gui gui = businessLogic.getGui();
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

	@Override
	public MouseAdapter createRightClickAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click Phase Details");

					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
					clearColumnSelectionItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Gui gui = businessLogic.getGui();
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
							Gui gui = businessLogic.getGui();
							final String sSelectedRow = gui.finalRows[0][0];
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
					Gui gui = businessLogic.getGui();
					popupMenu.add(showDetailsItem);
					popupMenu.show(gui.generalTable, e.getX(), e.getY());

				}

			}

		}; 
		
		
		return adapter;
		
	}
	
}
