package gui.mainEngine;

public class TablesFactory {
	

	private String tableType;

	public TableRenderer getTableType(String tableType, GuiController guiController) {

		if(tableType == null) {
			return null;
		}
		if(tableType.equals("Zoom Table")) {
			return new ZoomTableRendererHandler(guiController);

		}else if (tableType.equals("General Table")) {
			return new GeneralTableRendererHandler(guiController);

		}


		return null;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
}
