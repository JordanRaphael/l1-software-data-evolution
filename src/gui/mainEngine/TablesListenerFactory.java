package gui.mainEngine;

public class TablesListenerFactory {
	
	private String tableType;
	
	public ITablesListenerHandler getTableType(String tableType, BusinessLogic businessLogic) {
		
		if(tableType == null) {
			return null;
		}
		if(tableType.equals("Zoom Table")) {
			return new ZoomTableHandler(businessLogic);
			
		}else if (tableType.equals("General Table")) {
			return new GeneralTableHandler(businessLogic);
			
		}
		
		
		return null;
	}

}
