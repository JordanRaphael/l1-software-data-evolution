package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import gui.mainEngine.Gui;
import gui.mainEngine.GuiController;

public class TestShowPLD {

	private GuiController guiController;
	private Gui frame;

	public TestShowPLD() {

		frame = new Gui();
		guiController = new GuiController(frame);
	}

	@Test
	public void testShowPLD() {

		String filename = "filesHandler/inis/Atlas.ini";
		try {
			guiController.importData(filename);
		} catch (RecognitionException | IOException e) {
			e.printStackTrace();
		}

		fakeZoom();
		fakeShowPLD();

		PrintStream fileStream;
		try {
			fileStream = new PrintStream("Test-Files/tmp-atlas-testShowPLD.txt");
			System.setOut(fileStream);

			int numberOfRows = frame.finalRowsZoomArea.length;
			for (int i = 0; i < numberOfRows; i++) {
				for (int j = 0; j < frame.finalRowsZoomArea[i].length; j++) {
					System.out.println(frame.finalRowsZoomArea[i][j]);
				}
			}

			fileStream.close();

			String groundTruth = null;
			String toTest = null;
			try {
				groundTruth = new String(Files.readAllBytes(Paths.get("Test-Files/atlas-testShowPLD.txt")),
						StandardCharsets.UTF_8);
				toTest = new String(Files.readAllBytes(Paths.get("Test-Files/tmp-atlas-testShowPLD.txt")),
						StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			assertEquals(groundTruth, toTest);

			File file = new File("Test-Files/tmp-atlas-testShowPLD.txt");
			file.delete();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("serial")
	private void fakeZoom() {
		frame.tablesSelected = new ArrayList<String>() {
			{
				add("hlt_property");
			}
		};
		guiController.showSelectionToZoomArea(0);

	}

	private void fakeShowPLD() {
		guiController.showGeneralLifetimeIDUAction();
	}

}
