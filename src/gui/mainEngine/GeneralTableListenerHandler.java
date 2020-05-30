package gui.mainEngine;

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

import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class GeneralTableListenerHandler {

	private Gui gui;
	
	public GeneralTableListenerHandler(Gui gui) {
		
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
								gui.getGuiController().showClusterSelectionToZoomArea(gui.selectedColumn, sSelectedRow);

							} else {
								gui.getGuiController().showSelectionToZoomArea(gui.selectedColumn);
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
					gui.getGuiController().makeGeneralTableIDU();
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
								gui.getGuiController().makeGeneralTableIDU();
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

								gui.getGuiController().showSelectionToZoomArea(gui.wholeCol);
							} else {
								gui.getGuiController().showClusterSelectionToZoomArea(gui.wholeCol, "");
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