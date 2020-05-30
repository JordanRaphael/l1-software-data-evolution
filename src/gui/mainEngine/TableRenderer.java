package gui.mainEngine;

import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableRenderer {

	
	public abstract DefaultTableCellRenderer createTableCellRenderer(GuiController guiController);
	
	public abstract DefaultTableCellRenderer createDefaultTableRenderer(GuiController guiController);

	
}
