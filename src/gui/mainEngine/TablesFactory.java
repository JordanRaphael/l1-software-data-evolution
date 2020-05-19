package gui.mainEngine;

public class TablesFactory {
	

	private String tableType;

	public TableRenderer getTableType(String tableType, BusinessLogic businessLogic, Gui gui) {

		if(tableType == null) {
			return null;
		}
		if(tableType.equals("Zoom Table")) {
			return new ZoomTableRendererHandler(businessLogic, gui);

		}else if (tableType.equals("General Table")) {
			return new GeneralTableRendererHandler(businessLogic, gui);

		}


		return null;
	}
}
