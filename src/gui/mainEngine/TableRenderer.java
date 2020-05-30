package gui.mainEngine;

import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableRenderer {

	private GuiController guiController;
	
	private Gui gui;
	
	public TableRenderer(GuiController businessLogic) {
		
		this.guiController = businessLogic;
		this.gui = businessLogic.getGui();
	}

	public GuiController getGuiController() {
		return guiController;
	}

	public void setBusinessLogic(GuiController businessLogic) {
		this.guiController = businessLogic;
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
