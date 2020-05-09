package gui.mainEngine;

import java.awt.event.MouseAdapter;

import javax.swing.table.DefaultTableCellRenderer;

public interface ITablesListenerHandler {

	DefaultTableCellRenderer createDefaultTableCellRenderer(BusinessLogic businessLogic);
	
	MouseAdapter createOneClickMouseAdapter(BusinessLogic businessLogic);
	
	MouseAdapter createReleaseMouseAdapter(BusinessLogic businessLogic);
	
	MouseAdapter createColumnClickEvent(BusinessLogic businessLogic);
	
	MouseAdapter createRightClickAdapter(BusinessLogic businessLogic);
}
