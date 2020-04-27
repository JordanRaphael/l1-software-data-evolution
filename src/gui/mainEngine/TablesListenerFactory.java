package gui.mainEngine;

public class TablesListenerFactory {
	
	private String tableType;
	
	public ITablesListenerHandler getTableType(String tableType) {
		
		if(tableType == null) {
			return null;
		}
		if(tableType.equals("Zoom Table")) {
			return new ZoomTableHandler();
			
		}else if (tableType.equals("General Table")) {
			return new GeneralTableHandler();
			
		}
		
		
		return null;
	}

}
