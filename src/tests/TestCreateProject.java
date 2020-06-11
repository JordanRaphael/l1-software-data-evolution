package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import gui.dialogs.CreateProjectJDialog;

public class TestCreateProject {
	
	@SuppressWarnings("resource")
	@Test
	public void testCreateProject() {
		CreateProjectJDialog createProjectDialog = new CreateProjectJDialog("TestProjectName", "TestDatasetTxt", "TestInputCsv", "TestAss1", "TestAss2", "TestTransXml");
		String[] projectData = {"Project-name:TestProjectName","Dataset-txt:TestDatasetTxt","Input-csv:TestInputCsv","Assessement1-output:TestAss1","Assessement2-output:TestAss2","Transition-xml:TestTransXml"};
		createProjectDialog.getOkButton().doClick();
		File file = createProjectDialog.getFile();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			int lineCounter = 0;
			
			while (scanner.hasNextLine()) {
				if (lineCounter >= projectData.length) {
					fail("File sizes are different.");
					return;
				}
				String data = scanner.nextLine();
				assertEquals(data,projectData[lineCounter]);
				lineCounter ++;
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
