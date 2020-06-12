package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.GlobalDataManager;
import data.dataPPL.pplSQLSchema.PPLTable;
import phaseAnalyzer.commons.Phase;

public class GeneralTableRendererHandler extends TableRenderer{
	
	@Override
	public DefaultTableCellRenderer createTableCellRenderer(final GuiController guiController) {
		
		DefaultTableCellRenderer renderer =  new DefaultTableCellRenderer() {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			
			GlobalDataManager globalDataManager = guiController.getGlobalDataManager();
			
			String tmpValue = guiController.getGui().finalRows[row][column];
			String columnName = table.getColumnName(column);
			Color fr = new Color(0, 0, 0);
			c.setForeground(fr);

			if (column == guiController.getGui().wholeCol && guiController.getGui().wholeCol != 0) {
				Phase phase  = globalDataManager.getPhaseCollectors().get(0).getPhases().get(column - 1);
				String description = table.getColumnName(column) + "\n";
				description = description + "First Transition ID:"
						+ phase.getStartPos() + "\n";
				description = description + "Last Transition ID:"
						+ phase.getEndPos() + "\n";
				description = description + "Total Changes For This Phase:"
						+ phase.getTotalUpdates() + "\n";
				description = description + "Additions For This Phase:" + phase.getTotalAdditionsOfPhase() + "\n";
				description = description + "Deletions For This Phase:" + phase.getTotalDeletionsOfPhase() + "\n";
				description = description + "Updates For This Phase:" + phase.getTotalUpdatesOfPhase() + "\n";

				guiController.getGui().descriptionText.setText(description);

				Color cl = new Color(255, 69, 0, 100);

				c.setBackground(cl);
				return c;
			} else if (guiController.getGui().selectedColumn == 0) {
				if (isSelected) {

					if (guiController.getGui().finalRows[row][0].contains("Cluster")) {
						
						String description = "Cluster:" + guiController.getGui().finalRows[row][0] + "\n";
						description = globalDataManager.getClusterDescription(description, row);
						
						guiController.getGui().descriptionText.setText(description);
						
					} else {
						
						PPLTable tmpTable = globalDataManager.getAllPPLTables().get(guiController.getGui().finalRows[row][0]);
						String description = "Table:" + guiController.getGui().finalRows[row][0] + "\n";
						
						description = description + "Birth Version Name:" + tmpTable.getBirth() + "\n";
						description = description + "Birth Version ID:" + tmpTable.getBirthVersionID() + "\n";
						description = description + "Death Version Name:" + tmpTable.getDeath() + "\n";
						description = description + "Death Version ID:" + tmpTable.getDeathVersionID() + "\n";
						description = description + "Total Changes:" + tmpTable.getTotalChanges() + "\n";
						guiController.getGui().descriptionText.setText(description);

					}

					Color cl = new Color(255, 69, 0, 100);

					c.setBackground(cl);
					return c;
				}
			} else {

				if (guiController.getGui().selectedFromTree.contains(guiController.getGui().finalRows[row][0])) {

					Color cl = new Color(255, 69, 0, 100);
					c.setBackground(cl);

					return c;
				}

				if (isSelected && hasFocus) {

					String description = "";
					if (!table.getColumnName(column).contains("Table name")) {
						
						Phase tmpPhase = globalDataManager.getPhaseCollectors().get(0).getPhases().get(column - 1);
						
						if (guiController.getGui().finalRows[row][0].contains("Cluster")) {
							
							int nameOfTablesSize = globalDataManager.getDataCollectorsManager().getNamesOfTables(row);
							
							description = guiController.getGui().finalRows[row][0] + "\n";
							description = description + "Tables:" + nameOfTablesSize + "\n\n";
							description = description + table.getColumnName(column) + "\n";
							description = description + "First Transition ID:" + tmpPhase.getStartPos() + "\n";
							description = description + "Last Transition ID:" + tmpPhase.getEndPos() + "\n\n";
							description = description + "Total Changes For This Phase:" + tmpValue + "\n";

						} else {
							
							description = table.getColumnName(column) + "\n";
							description = description + "First Transition ID:" + tmpPhase.getStartPos() + "\n";
							description = description + "Last Transition ID:" + tmpPhase.getEndPos() + "\n\n";
							description = description + "Table:" + guiController.getGui().finalRows[row][0] + "\n";
							description = description + "Birth Version Name:"
									+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRows[row][0]).getBirth()
									+ "\n";
							description = description + "Birth Version ID:" + globalDataManager.getAllPPLTables()
									.get(guiController.getGui().finalRows[row][0]).getBirthVersionID() + "\n";
							description = description + "Death Version Name:"
									+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRows[row][0]).getDeath()
									+ "\n";
							description = description + "Death Version ID:" + globalDataManager.getAllPPLTables()
									.get(guiController.getGui().finalRows[row][0]).getDeathVersionID() + "\n";
							description = description + "Total Changes For This Phase:" + tmpValue + "\n";

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
					insersionColor = new Color(154, 205, 50, 200);
				} else if (numericValue > 0 && numericValue <= guiController.getGui().segmentSize[3]) {

					insersionColor = new Color(176, 226, 255);
				} else if (numericValue > guiController.getGui().segmentSize[3] && numericValue <= 2 * guiController.getGui().segmentSize[3]) {
					insersionColor = new Color(92, 172, 238);
				} else if (numericValue > 2 * guiController.getGui().segmentSize[3] && numericValue <= 3 * guiController.getGui().segmentSize[3]) {

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
	public DefaultTableCellRenderer createDefaultTableRenderer(final GuiController guiController) {
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					
					GlobalDataManager globalDataManager = guiController.getGlobalDataManager();
					String tmpValue = guiController.getGui().finalRowsZoomArea[row][column];
					String columnName = table.getColumnName(column);
					Color fr = new Color(0, 0, 0);

					c.setForeground(fr);
					setOpaque(true);

					if (column == guiController.getGui().wholeColZoomArea && guiController.getGui().wholeColZoomArea != 0) {

						String description = "Transition ID:" + table.getColumnName(column) + "\n";
						description = description + "Old Version Name:" + globalDataManager.getAllPPLTransitions()
								.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
						description = description + "New Version Name:" + globalDataManager.getAllPPLTransitions()
								.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

						description = description
								+ "Transition Changes:" + globalDataManager.getAllPPLTransitions()
										.get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()
								+ "\n";
						description = description
								+ "Additions:" + globalDataManager.getAllPPLTransitions()
										.get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()
								+ "\n";
						description = description
								+ "Deletions:" + globalDataManager.getAllPPLTransitions()
										.get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()
								+ "\n";
						description = description
								+ "Updates:" + globalDataManager.getAllPPLTransitions()
										.get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()
								+ "\n";

						guiController.getGui().descriptionText.setText(description);

						Color cl = new Color(255, 69, 0, 100);

						c.setBackground(cl);
						return c;
					} else if (guiController.getGui().selectedColumnZoomArea == 0) {

						if (isSelected) {
							Color cl = new Color(255, 69, 0, 100);
							c.setBackground(cl);

							String description = "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";
							description = description + "Birth Version Name:"
									+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getBirth()
									+ "\n";
							description = description + "Birth Version ID:" + globalDataManager.getAllPPLTables()
									.get(guiController.getGui().finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
							description = description + "Death Version Name:"
									+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getDeath()
									+ "\n";
							description = description + "Death Version ID:" + globalDataManager.getAllPPLTables()
									.get(guiController.getGui().finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
							description = description + "Total Changes:" + globalDataManager.getAllPPLTables()
									.get(guiController.getGui().finalRowsZoomArea[row][0]).getTotalChanges() + "\n";

							guiController.getGui().descriptionText.setText(description);

							return c;

						}
					} else {

						if (guiController.getGui().selectedFromTree.contains(guiController.getGui().finalRowsZoomArea[row][0])) {

							Color cl = new Color(255, 69, 0, 100);

							c.setBackground(cl);

							return c;
						}

						if (isSelected && hasFocus) {

							String description = "";
							if (!table.getColumnName(column).contains("Table name")) {
								description = "Table:" + guiController.getGui().finalRowsZoomArea[row][0] + "\n";

								description = description + "Old Version Name:"
										+ globalDataManager.getAllPPLTransitions()
												.get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()
										+ "\n";
								description = description + "New Version Name:"
										+ globalDataManager.getAllPPLTransitions()
												.get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()
										+ "\n";
								if (globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0]).getTableChanges()
										.getTableAtChForOneTransition(
												Integer.parseInt(table.getColumnName(column))) != null) {
									description = description + "Transition Changes:" + globalDataManager.getAllPPLTables()
											.get(guiController.getGui().finalRowsZoomArea[row][0]).getTableChanges()
											.getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))
											.size() + "\n";
									description = description + "Additions:"
											+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
													.getNumberOfAdditionsForOneTr(
															Integer.parseInt(table.getColumnName(column)))
											+ "\n";
									description = description + "Deletions:"
											+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
													.getNumberOfDeletionsForOneTr(
															Integer.parseInt(table.getColumnName(column)))
											+ "\n";
									description = description + "Updates:"
											+ globalDataManager.getAllPPLTables().get(guiController.getGui().finalRowsZoomArea[row][0])
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
							insersionColor = new Color(154, 205, 50, 200);
						} else if (numericValue > 0 && numericValue <= guiController.getGui().segmentSizeZoomArea[3]) {

							insersionColor = new Color(176, 226, 255);
						} else if (numericValue > guiController.getGui().segmentSizeZoomArea[3]
								&& numericValue <= 2 * guiController.getGui().segmentSizeZoomArea[3]) {
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
}
