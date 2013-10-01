package transitions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



import parser.Delta;
import parser.HecateParser;
import sqlSchema.Schema;

/**
 * 
 *
 */
public class ImportData {

	private static ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private static ArrayList<TransitionList> AllTransitions=new ArrayList<TransitionList>();
	private String filepath=null;
	private String 	projectDataFolder=null;

	public ImportData(String tmpFilepath) {
		filepath=tmpFilepath;
	}

	public void loadDataset() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(filepath));
		File f = new File(filepath);
		String dataset = (f.getName().split("\\."))[0];
		String d = f.getParent();
		f = new File(d);
		String path = f.getAbsolutePath() + File.separator + dataset;

		ArrayList<String> sAllSchemas = new ArrayList<String>();
		String line;
		
		while(true) {
			line = br.readLine();
			if (line == null) break;
			sAllSchemas.add(br.readLine());
		};

		String sStandardString = path + File.separator;

		for(int i=0; i<sAllSchemas.size(); i++) {
			System.out.println(sAllSchemas.get(i));
		}

		Transitions trs = new Transitions();

		for(int i=0; i<sAllSchemas.size(); i++) {
			if(i==sAllSchemas.size()-1) {
				String sFinalString2=sStandardString+sAllSchemas.get(i);
				Schema schema=HecateParser.parse(sFinalString2);
				AllSchemas.add(schema);
				break;
			}
			String sFinalString=sStandardString+sAllSchemas.get(i).trim();

			AllSchemas.add(HecateParser.parse(sFinalString));

			String sFinalString2=sStandardString+sAllSchemas.get(i+1).trim();

			Schema oldSchema = HecateParser.parse(sFinalString);
			Schema newSchema = HecateParser.parse(sFinalString2);

			Delta delta = new Delta();

			TransitionList tmpTransitionList =new TransitionList();
			tmpTransitionList=delta.minus(oldSchema, newSchema); 
			trs.add(tmpTransitionList);

		}
		br.close();
		makeTransitions(trs);
	}


	public static void makeTransitions(Transitions tl) throws FileNotFoundException {
		try {



			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream("Transitions.xml"));


			//***********************************************

			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File("Transitions.xml");
			Transitions root = (Transitions) u.unmarshal(inputFile);


			AllTransitions=(ArrayList<TransitionList>) root.getList();



		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
	}

	public ArrayList<Schema> getAllSchemas(){

		return AllSchemas;

	}

	public ArrayList<TransitionList> getAllTransitions(){

		return AllTransitions;

	}

	public String getDataFolder(){
		return projectDataFolder;
	}

}
