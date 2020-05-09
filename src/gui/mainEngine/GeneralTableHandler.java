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

	@Override
	public DefaultTableCellRenderer createDefaultTableCellRenderer(final BusinessLogic businessLogic) {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				String tmpValue = businessLogic.gui.finalRows[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);
				
				GlobalDataKeeper globalDataKeeper = businessLogic.getGlobalDataKeeper();
				
				if (column == businessLogic.gui.wholeCol && businessLogic.gui.wholeCol != 0) {

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

					businessLogic.gui.descriptionText.setText(description);

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				} else if (businessLogic.gui.selectedColumn == 0){
					if (isSelected) {

						if (businessLogic.gui.finalRows[row][0].contains("Cluster")) {
							String description = "Cluster:" + businessLogic.gui.finalRows[row][0] + "\n";

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

							businessLogic.gui.descriptionText.setText(description);
						} else {
							String description = "Table:" + businessLogic.gui.finalRows[row][0] + "\n";

							description = description + "Birth Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getBirth() + "\n";
							description = description + "Birth Version ID:"
									+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getBirthVersionID()
									+ "\n";
							description = description + "Death Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getDeath() + "\n";
							description = description + "Death Version ID:"
									+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getDeathVersionID()
									+ "\n";
							description = description + "Total Changes:"
									+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getTotalChanges()
									+ "\n";
							businessLogic.gui.descriptionText.setText(description);

						}

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}
				} else {

					if (businessLogic.gui.selectedFromTree.contains(businessLogic.gui.finalRows[row][0])) {

						Color cl = new Color(255, 69, 0, 100);
						c.setBackground(cl);

						return c;
					}
					if (isSelected && hasFocus) {

						String description = "";
						if (!table.getColumnName(column).contains("Table name")) {

							if (businessLogic.gui.finalRows[row][0].contains("Cluster")) {

								description = businessLogic.gui.finalRows[row][0] + "\n";
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
								description = description + "Table:" + businessLogic.gui.finalRows[row][0] + "\n";
								description = description + "Birth Version Name:"
										+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getBirth() + "\n";
								description = description + "Birth Version ID:"
										+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getBirthVersionID()
										+ "\n";
								description = description + "Death Version Name:"
										+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getDeath() + "\n";
								description = description + "Death Version ID:"
										+ globalDataKeeper.getAllPPLTables().get(businessLogic.gui.finalRows[row][0]).getDeathVersionID()
										+ "\n";
								description = description + "Total Changes For This Phase:" + tmpValue + "\n";

							}

							businessLogic.gui.descriptionText.setText(description);

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
					} else if (numericValue > 0 && numericValue <= businessLogic.gui.segmentSize[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > businessLogic.gui.segmentSize[3] && numericValue <= 2 * businessLogic.gui.segmentSize[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * businessLogic.gui.segmentSize[3] && numericValue <= 3 * businessLogic.gui.segmentSize[3]) {

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
	public MouseAdapter createOneClickMouseAdapter(final BusinessLogic businessLogic) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					businessLogic.gui.selectedRowsFromMouse = target.getSelectedRows();
					businessLogic.gui.selectedColumn = target.getSelectedColumn();
					businessLogic.gui.LifeTimeTable.repaint();
					System.out.println("createOneClickeMouseAdapter");
					System.out.println(target.getSelectedColumn());
					for (int num : target.getSelectedColumns()) {
						System.out.println(num);
					}
						
						
				}

			}
		};
	
		return adapter;
	}

	@Override
	public MouseAdapter createReleaseMouseAdapter(final BusinessLogic businessLogic) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					businessLogic.gui.selectedColumn = target1.getSelectedColumn();
					businessLogic.gui.selectedRowsFromMouse = new int[target1.getSelectedRows().length];
					businessLogic.gui.selectedRowsFromMouse = target1.getSelectedRows();

					final String sSelectedRow = (String) businessLogic.gui.generalTable.getValueAt(target1.getSelectedRow(), 0);
					businessLogic.gui.tablesSelected = new ArrayList<String>();

					for (int rowsSelected = 0; rowsSelected < businessLogic.gui.selectedRowsFromMouse.length; rowsSelected++) {
						businessLogic.gui.tablesSelected.add((String) businessLogic.gui.generalTable.getValueAt(businessLogic.gui.selectedRowsFromMouse[rowsSelected], 0));
					}

					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent le) {
							if (sSelectedRow.contains("Cluster ")) {
								businessLogic.showClusterSelectionToZoomArea(businessLogic.gui.selectedColumn, sSelectedRow);

							} else {
								businessLogic.showSelectionToZoomArea(businessLogic.gui.selectedColumn);
							}
						}
					});
					popupMenu.add(showDetailsItem);
					JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
					clearSelectionItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent le) {

							businessLogic.gui.selectedFromTree = new ArrayList<String>();
							businessLogic.gui.LifeTimeTable.repaint();
						}
					});
					popupMenu.add(clearSelectionItem);
					popupMenu.show(businessLogic.gui.generalTable, e.getX(), e.getY());

				}
			}
		};
		
		return adapter;
		
	}

	@Override
	public MouseAdapter createColumnClickEvent(final BusinessLogic businessLogic) {
			
			MouseAdapter adapter = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					businessLogic.gui.wholeCol = businessLogic.gui.generalTable.columnAtPoint(e.getPoint());
					String name = businessLogic.gui.generalTable.getColumnName(businessLogic.gui.wholeCol);
					System.out.println("createColumnClickEvent");
					System.out.println("Column index selected " + businessLogic.gui.wholeCol + " " + name);
					businessLogic.gui.generalTable.repaint();
					if (businessLogic.gui.showingPld) {
						businessLogic.makeGeneralTableIDU();
					}
				}
			};
		
			return adapter;
		
	}

	@Override
	public MouseAdapter createRightClickAdapter(final BusinessLogic businessLogic) {

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
							businessLogic.gui.wholeCol = -1;
							businessLogic.gui.generalTable.repaint();
							if (businessLogic.gui.showingPld) {
								businessLogic.makeGeneralTableIDU();
							}
						}
					});
					popupMenu.add(clearColumnSelectionItem);
					JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					showDetailsItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String sSelectedRow = businessLogic.gui.finalRows[0][0];
							System.out.println("?" + sSelectedRow);
							businessLogic.gui.tablesSelected = new ArrayList<String>();
							for (int i = 0; i < businessLogic.gui.finalRows.length; i++)
								businessLogic.gui.tablesSelected.add((String) businessLogic.gui.generalTable.getValueAt(i, 0));

							if (!sSelectedRow.contains("Cluster ")) {

								businessLogic.showSelectionToZoomArea(businessLogic.gui.wholeCol);
							} else {
								businessLogic.showClusterSelectionToZoomArea(businessLogic.gui.wholeCol, "");
							}

						}
					});
					popupMenu.add(showDetailsItem);
					popupMenu.show(businessLogic.gui.generalTable, e.getX(), e.getY());

				}

			}

		}; 
		
		
		return adapter;
		
	}
	

}
