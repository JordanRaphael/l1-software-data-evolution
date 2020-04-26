package gui.mainEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.server.UID;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

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
	
	public MouseAdapter createOneClickAdapter() {
		
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
	
	
	
	
}
