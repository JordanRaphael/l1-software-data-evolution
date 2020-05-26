package gui.mainEngine;

import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableRenderer {

	private GuiController businessLogic;
	
	private Gui gui;
	
	public TableRenderer(GuiController businessLogic) {
		
		this.businessLogic = businessLogic;
		this.gui = businessLogic.getGui();
	}

	public GuiController getBusinessLogic() {
		return businessLogic;
	}

	public void setBusinessLogic(GuiController businessLogic) {
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
