package gui.mainEngine;

public class TablesFactory {
	

	private String tableType;

	public TableRenderer getTableType(String tableType) {

		if(tableType == null) {
			return null;
		}
		if(tableType.equals("Zoom Table")) {
			return new ZoomTableRendererHandler();

		}else if (tableType.equals("General Table")) {
			return new GeneralTableRendererHandler();

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
