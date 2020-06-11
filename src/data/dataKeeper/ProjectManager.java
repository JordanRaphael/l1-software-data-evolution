package data.dataKeeper;

public class ProjectManager {

	private String projectDataFolder = null;
	private String filename = null;
	private String transitionsFile = "";

	public ProjectManager(String filename, String transitionsFile) {
		
		this.filename = filename;
		this.transitionsFile = transitionsFile;
	}

	public String getFilename() {
		
		return filename;
	}
	
	public String getTransitionsFile() {
		
		return transitionsFile;
	}
	
	public void setDataFolder(String projectDataFolder) {
		
		this.projectDataFolder = projectDataFolder;
	}
	
	public String getDataFolder() {
		
		return projectDataFolder;
	}
	
}
