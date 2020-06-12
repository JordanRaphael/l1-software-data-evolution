package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataManager;
import gui.mainEngine.Gui;
import gui.mainEngine.GuiController;

public class TestEditProject {

	private GuiController guiController;
	private Gui frame;

	public TestEditProject() {

		frame = new Gui();
		guiController = new GuiController(frame);

	}

	@Test
	public void testEditProject() {

		String fileName = "filesHandler/inis/Atlas.ini";

		File file = new File(fileName);
		System.out.println(file.toString());
		frame.setProject(file.getName());

		fileName = file.toString();
		System.out.println("!!" + frame.getProject());

		GlobalDataManager globalDataManager = new GlobalDataManager();

		try {
			globalDataManager.getProjectDataManager().parseFile(fileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(globalDataManager.getProjectDataManager().getProjectName());

		frame.setProject(file.getName());
		System.out.println(frame.getProject());

		PrintStream stream = null;
		try {
			stream = new PrintStream(new File("Test-Files/edit-atlas-project-test.txt"));
			System.setOut(stream);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			guiController.importData("filesHandler/inis/Atlas.ini");
			stream.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		} catch (RecognitionException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}

		String content1 = null;
		String content2 = null;
		try {
			content1 = new String(Files.readAllBytes(Paths.get("Test-Files/edit-atlas-project-test.txt")),
					StandardCharsets.UTF_8);
			content2 = new String(Files.readAllBytes(Paths.get("Test-Files/edit-atlas-project.txt")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(content2, content1);

	}
}
