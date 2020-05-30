package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import data.dataKeeper.GlobalDataManager;

public class ZoomTableRendererHandler extends TableRenderer{

	@Override
	public DefaultTableCellRenderer createTableCellRenderer(final GuiController guiController) {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				GlobalDataManager globalDataKeeper = guiController.getGlobalDataKeeper();
				String tmpValue = guiController.getGui().finalRowsZoomArea[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);

				if (column == guiController.getGui().wholeColZoomArea && guiController.getGui().wholeColZoomArea != 0) {

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

					guiController.getGui().descriptionText.setText(description);

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				} else if (guiController.getGui().selectedColumnZoomArea == 0) {
					if (isSelected) {

						String description = "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";
						description = description + "Birth Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirth() + "\n";
						description = description + "Birth Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirthVersionID()
								+ "\n";
						description = description + "Death Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeath() + "\n";
						description = description + "Death Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeathVersionID()
								+ "\n";
						description = description + "Total Changes:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getTotalChanges()
								+ "\n";
						guiController.getGui().descriptionText.setText(description);

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

							description = description + "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";
							description = description + "Birth Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirth()
									+ "\n";
							description = description + "Birth Version ID:" + globalDataKeeper.getAllPPLTables()
									.get(guiController.getGui().finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
							description = description + "Death Version Name:"
									+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeath()
									+ "\n";
							description = description + "Death Version ID:" + globalDataKeeper.getAllPPLTables()
									.get(guiController.getGui().finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
							description = description + "Total Changes For This Phase:" + tmpValue + "\n";

							guiController.getGui().descriptionText.setText(description);

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
					} else if (numericValue > 0 && numericValue <= guiController.getGui().segmentSizeZoomArea[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > guiController.getGui().segmentSizeZoomArea[3] && numericValue <= 2 * guiController.getGui().segmentSizeZoomArea[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * guiController.getGui().segmentSizeZoomArea[3]
							&& numericValue <= 3 * guiController.getGui().segmentSizeZoomArea[3]) {

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

	
	@Override
	public DefaultTableCellRenderer createDefaultTableRenderer(final GuiController guiController) {
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				
				GlobalDataManager globalDataKeeper = guiController.getGlobalDataKeeper();
				String tmpValue = guiController.getGui().finalRowsZoomArea[row][column];
				String columnName = table.getColumnName(column);
				Color fr = new Color(0, 0, 0);
				c.setForeground(fr);

				if (column == guiController.getGui().wholeColZoomArea) {

					String description = "Transition ID:" + table.getColumnName(column) + "\n";
					description = description + "Old Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
					description = description + "New Version Name:" + globalDataKeeper.getAllPPLTransitions()
							.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

					description = description + "Transition Changes:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterChangesForOneTr(guiController.getRowsZoom())
							+ "\n";
					description = description + "Additions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterAdditionsForOneTr(guiController.getRowsZoom())
							+ "\n";
					description = description + "Deletions:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterDeletionsForOneTr(guiController.getRowsZoom())
							+ "\n";
					description = description + "Updates:"
							+ globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column)))
									.getNumberOfClusterUpdatesForOneTr(guiController.getRowsZoom())
							+ "\n";

					guiController.getGui().descriptionText.setText(description);
					Color cl = new Color(255, 69, 0, 100);
					c.setBackground(cl);

					return c;
				} else if (guiController.getGui().selectedColumnZoomArea == 0) {
					if (isSelected) {
						String description = "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";
						description = description + "Birth Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirth() + "\n";
						description = description + "Birth Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirthVersionID()
								+ "\n";
						description = description + "Death Version Name:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeath() + "\n";
						description = description + "Death Version ID:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeathVersionID()
								+ "\n";
						description = description + "Total Changes:"
								+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
										.getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)),
												Integer.parseInt(table.getColumnName(table.getColumnCount() - 1)))
								+ "\n";
						guiController.getGui().descriptionText.setText(description);

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					}
				} else {
					if (isSelected && hasFocus) {

						String description = "";
						if (!table.getColumnName(column).contains("Table name")) {
							description = "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";

							description = description + "Old Version Name:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()
									+ "\n";
							description = description + "New Version Name:"
									+ globalDataKeeper.getAllPPLTransitions()
											.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()
									+ "\n";
							if (globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getTableChanges()
									.getTableAtChForOneTransition(
											Integer.parseInt(table.getColumnName(column))) != null) {
								description = description + "Transition Changes:" + globalDataKeeper.getAllPPLTables()
										.get(guiController.getGui().finalRowsZoomArea[row][0]).getTableChanges()
										.getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))
										.size() + "\n";
								description = description + "Additions:"
										+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
												.getNumberOfAdditionsForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";
								description = description + "Deletions:"
										+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
												.getNumberOfDeletionsForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";
								description = description + "Updates:"
										+ globalDataKeeper.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
												.getNumberOfUpdatesForOneTr(
														Integer.parseInt(table.getColumnName(column)))
										+ "\n";

							} else {
								description = description + "Transition Changes:0" + "\n";
								description = description + "Additions:0" + "\n";
								description = description + "Deletions:0" + "\n";
								description = description + "Updates:0" + "\n";

							}

							guiController.getGui().descriptionText.setText(description);
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
					} else if (numericValue > 0 && numericValue <= guiController.getGui().segmentSizeZoomArea[3]) {

						insersionColor = new Color(176, 226, 255);
					} else if (numericValue > guiController.getGui().segmentSizeZoomArea[3] && numericValue <= 2 * guiController.getGui().segmentSizeZoomArea[3]) {
						insersionColor = new Color(92, 172, 238);
					} else if (numericValue > 2 * guiController.getGui().segmentSizeZoomArea[3]
							&& numericValue <= 3 * guiController.getGui().segmentSizeZoomArea[3]) {

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
