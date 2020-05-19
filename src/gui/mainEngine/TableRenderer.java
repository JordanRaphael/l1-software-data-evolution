package gui.mainEngine;

import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableRenderer {

	private BusinessLogic businessLogic;
	
	private Gui gui;
	
	public TableRenderer(BusinessLogic businessLogic) {
		
		this.businessLogic = businessLogic;
		this.gui = businessLogic.getGui();
	}

	public BusinessLogic getBusinessLogic() {
		return businessLogic;
	}

	public void setBusinessLogic(BusinessLogic businessLogic) {
		this.businessLogic = businessLogic;
	}

	public Gui getGui() {
		return gui;
	}

	public void setGui(Gui gui) {
		this.gui = gui;
	}
	
	public abstract DefaultTableCellRenderer createTableCellRenderer();
	
	public abstract DefaultTableCellRenderer createDefaultTableRenderer();
	
}
