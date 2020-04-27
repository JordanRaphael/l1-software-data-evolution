package gui.mainEngine;

import java.awt.event.MouseAdapter;

import javax.swing.table.DefaultTableCellRenderer;

public interface ITablesListenerHandler {

DefaultTableCellRenderer createDefaultTableCellRenderer();
	
	MouseAdapter createOneClickMouseAdapter();
	
	MouseAdapter createReleaseMouseAdapter();
	
	MouseAdapter createColumnClickEvent();
	
	MouseAdapter createRightClickAdapter();
}
