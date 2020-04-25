package gui.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import gui.dialogs.CreateProjectJDialog;
import gui.mainEngine.BusinessLogic;
import gui.mainEngine.Gui;

public class TestCreateProject {
	
	private BusinessLogic buisnessLogic;
	private Gui frame;
	
	public TestCreateProject() {
		
		frame = new Gui();
		buisnessLogic = new BusinessLogic(frame);
		
	}

	@Test
	public void testCreateProject() {
		CreateProjectJDialog createProjectDialog = new CreateProjectJDialog("TestProjectName", "TestDatasetTxt", "TestInputCsv", "TestAss1", "TestAss2", "TestTransXml");
		createProjectDialog.getOkButton().doClick();
		File file = createProjectDialog.getFile();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				System.out.println(data);
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		//"TestProjectName", "TestDatasetTxt", "TestInputCsv", "TestAss1", "TestAss2", "TestTransXml"
	}
	
}
