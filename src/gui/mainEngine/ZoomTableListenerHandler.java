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

public class ZoomTableListenerHandler {

	public MouseAdapter createClusterOneClickHandler(final Gui gui) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumnZoomArea = target.getSelectedColumn();
					gui.zoomAreaTable.repaint();
				}

			}
		};
		
		return adapter;
	}

	public MouseAdapter createClusterRightClickHandler(final JvTable zoomTable, final Gui gui) {
		
		MouseAdapter adapter = new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					JTable target1 = (JTable) e.getSource();
					gui.selectedColumnZoomArea = target1.getSelectedColumn();
					gui.selectedRowsFromMouse = target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());

					gui.tablesSelected = new ArrayList<String>();

					for (int rowsSelected = 0; rowsSelected < gui.selectedRowsFromMouse.length; rowsSelected++) {
						gui.tablesSelected.add((String) zoomTable.getValueAt(gui.selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(gui.tablesSelected.get(rowsSelected));
					}
					if (zoomTable.getColumnName(gui.selectedColumnZoomArea).contains("Phase")) {

						final JPopupMenu popupMenu = new JPopupMenu();
						JMenuItem showDetailsItem = new JMenuItem("Show Details");
						showDetailsItem.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent e) {
								gui.firstLevelUndoColumnsZoomArea = gui.finalColumnsZoomArea;
								gui.firstLevelUndoRowsZoomArea = gui.finalRowsZoomArea;
								gui.getGuiController().showSelectionToZoomArea(gui.selectedColumnZoomArea);

							}
						});
						popupMenu.add(showDetailsItem);
						popupMenu.show(zoomTable, e.getX(), e.getY());
					}

				}
			}
		};
		
		return adapter;
	}

	public MouseAdapter createClusterColumnClickedHandler(final JvTable zoomTable, final Gui gui) {
		
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

	public MouseAdapter createClusterColumnRightClickHandler(final JvTable zoomTable, final Gui gui) {
		MouseAdapter adapter = new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					System.out.println("Right Click");

					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					showDetailsItem.addActionListener(new ActionListener() {

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

	
	public MouseListener createZoomAreaMouseClickedHandler(final Gui gui) {
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumnZoomArea = target.getSelectedColumn();
					gui.zoomAreaTable.repaint();
				}
			}
		};
		
		return adapter;
	}

	public MouseAdapter createZoomAreaRightClickHandler(final Gui gui) {
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();

					gui.selectedRowsFromMouse = target.getSelectedRows();
					gui.selectedColumnZoomArea = target.getSelectedColumn();
					gui.zoomAreaTable.repaint();
				}
			}
		};
		
		return adapter;
	}

	public MouseAdapter createZoomAreaColumnRightClickHandler(final JvTable zoomTable, final Gui gui) {
		
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

	public MouseAdapter createZoomAreaRightClickReleasedHandler(final JvTable zoomTable, final Gui gui) {
		
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
		
}