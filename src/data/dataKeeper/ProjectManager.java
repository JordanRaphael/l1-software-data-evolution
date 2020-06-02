package data.dataKeeper;

public class ProjectManager {

	private String projectDataFolder = null;
	private String filename = null;
	private String transitionsFile = "";

	protected ProjectManager(String filename, String transitionsFile) {
		this.filename = filename;
		this.transitionsFile = transitionsFile;
	}

	protected String getFilename() {
		return filename;
	}
	
	protected String getTransitionsFile() {
		return transitionsFile;
	}
	
	protected void setDataFolder(String projectDataFolder) {
		this.projectDataFolder = projectDataFolder;
	}
	
	public String getDataFolder() {
		return projectDataFolder;
	}
	
}