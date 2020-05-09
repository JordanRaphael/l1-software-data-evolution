package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.GlobalDataKeeper;

public class ZoomTableHandler implements ITablesListenerHandler {

	private BusinessLogic businessLogic;
	
	public ZoomTableHandler(BusinessLogic businessLogic) {
		
		this.businessLogic = businessLogic;
		
	}
	
	@Override
	public DefaultTableCellRenderer createDefaultTableCellRenderer() {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				Gui gui = businessLogic.getGui();
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
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
									.getNumberOfClusterChangesForOneTr(businessLogic.rowsZoom)
							+ "\n";
					description = description + "Additions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterAdditionsForOneTr(businessLogic.rowsZoom)
							+ "\n";
					description = description + "Deletions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterDeletionsForOneTr(businessLogic.rowsZoom)
							+ "\n";
					description = description + "Updates:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterUpdatesForOneTr(businessLogic.rowsZoom)
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
	
	public MouseAdapter createOneClickMouseAdapter() {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Gui gui = businessLogic.getGui();
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumn = target.getSelectedColumn();
					gui.zoomAreaTable.repaint();
					
				}

			}
		};
	
		return adapter;
	}
		
	
	public MouseAdapter createRightClickAdapter() {

		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Gui gui = businessLogic.getGui();
				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					gui.selectedColumnZoomArea = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					ArrayList<String> tablesSelected = new ArrayList<String>();
					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						tablesSelected.add((String) businessLogic.zoomTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}

				}

			}
		};
		
		return adapter;
		
	}
	
	
	@Override
	public MouseAdapter createReleaseMouseAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MouseAdapter createColumnClickEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
