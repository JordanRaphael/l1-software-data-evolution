package data.dataKeeper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProjectManager {

	private String projectDataFolder = null;
	private String filename = null;

	private String projectName = "";
	private String datasetTxt = "";
	private String inputCsv = "";
	private String outputAssessment1 = "";
	private String outputAssessment2 = "";
	private String transitionsFile = "";
	
	public ProjectManager() {

	}

	public void parseFile(String filename) throws FileNotFoundException, IOException {
			
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;

			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				if (line.contains("Project-name")) {
					String[] projectNameTable = line.split(":");
					projectName = projectNameTable[1];
				} else if (line.contains("Dataset-txt")) {
					String[] datasetTxtTable = line.split(":");
					datasetTxt = datasetTxtTable[1];
					this.filename = datasetTxt;
				} else if (line.contains("Input-csv")) {
					String[] inputCsvTable = line.split(":");
					inputCsv = inputCsvTable[1];
				} else if (line.contains("Assessement1-output")) {
					String[] outputAss1 = line.split(":");
					outputAssessment1 = outputAss1[1];
				} else if (line.contains("Assessement2-output")) {
					String[] outputAss2 = line.split(":");
					outputAssessment2 = outputAss2[1];
				} else if (line.contains("Transition-xml")) {
					String[] transitionXmlTable = line.split(":");
					transitionsFile = transitionXmlTable[1];
				}

			}
			;

			br.close();
		} catch (FileNotFoundException e1) {
			throw e1;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void setOutputAssessment2(String outputAssessment2) {

		this.outputAssessment2 = outputAssessment2;
	}

	public String getOutputAssessment2() {

		return outputAssessment2;
	}

	public void setOutputAssessment1(String outputAssessment1) {

		this.outputAssessment1 = outputAssessment1;
	}

	public String getOutputAssessment1() {

		return outputAssessment1;
	}

	public void setInputCsv(String inputCsv) {

		this.inputCsv = inputCsv;
	}

	public String getInputCsv() {

		return inputCsv;
	}

	public void setDatasetTxt(String datasetTxt) {

		this.datasetTxt = datasetTxt;
	}

	public String getDatasetTxt() {

		return datasetTxt;
	}

	public void setProjectName(String projectName) {

		this.projectName = projectName;
	}

	public String getProjectName() {

		return projectName;
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
