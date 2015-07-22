package dataProccessing;

import gr.uoi.cs.daintiness.hecate.diff.Delta;
import gr.uoi.cs.daintiness.hecate.diff.DiffResult;
import gr.uoi.cs.daintiness.hecate.parser.HecateParser;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
//import hecate.parser.Delta;
//import hecate.parser.DiffResult;
//import hecate.parser.HecateParser;
//import hecate.sqlSchema.Schema;
//import hecate.transitions.Deletion;
//import hecate.transitions.Insersion;
//import hecate.transitions.Transition;
//import hecate.transitions.TransitionList;
//import hecate.transitions.Transitions;
//import hecate.transitions.Update;

import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.Transition;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;
import gr.uoi.cs.daintiness.hecate.transitions.Transitions;
import gr.uoi.cs.daintiness.hecate.transitions.Update;
//import hecate.transitions.TransitionList;
//import hecate.transitions.Transitions;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ImportSchemas {
	
	private static ArrayList<Schema> allSchemas = new ArrayList<Schema>();

	private String filepath=null;
	private String transitionsFile=null;
	private static ArrayList<TransitionList> allTransitions = new ArrayList<TransitionList>();

	
	public ImportSchemas(String tmpFilepath,String transitionsFile) {
		filepath=tmpFilepath;
		this.transitionsFile=transitionsFile;
	}

	@SuppressWarnings("static-access")
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
			if (line == null) 
				break;
			sAllSchemas.add(line);
		};

		String sStandardString = path + File.separator;

		Transitions trs = new Transitions();

		
		
		for(int i=0; i<sAllSchemas.size(); i++) {
			if(i==sAllSchemas.size()-1) {
				String sFinalString2=sStandardString+sAllSchemas.get(i);
				Schema schema=HecateParser.parse(sFinalString2);
				allSchemas.add(schema);
				break;
			}
			String sFinalString=sStandardString+sAllSchemas.get(i).trim();

			allSchemas.add(HecateParser.parse(sFinalString));
			
			String sFinalString2=sStandardString+sAllSchemas.get(i+1).trim();

			Schema oldSchema = HecateParser.parse(sFinalString);
			
			
			Schema newSchema = HecateParser.parse(sFinalString2);

			
			Delta delta = new Delta();

			TransitionList tmpTransitionList =new TransitionList();
			DiffResult tmpDiffResult=new DiffResult();
			tmpDiffResult=delta.minus(oldSchema, newSchema); 

			tmpTransitionList=tmpDiffResult.tl;
			trs.add(tmpTransitionList);

		}
		br.close();
		makeTransitions(trs);
		//makePPLSchemas();
		//makeReport();
		//makeAllTables();
		//makeAtomicChanges();
		//makeTableChanges();
		//makePPLTransitions();
		//matchTableChangesPPLTables();
		
	}


	public  void makeTransitions(Transitions tl) throws IOException {
		try {



			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream(transitionsFile));


			//***********************************************

			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File(transitionsFile);
			Transitions root = (Transitions) u.unmarshal(inputFile);


			allTransitions=(ArrayList<TransitionList>) root.getList();

			

		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
	}
	
	public ArrayList<Schema> getAllHecSchemas(){

		return allSchemas;

	}

	public ArrayList<TransitionList> getAllTransitions(){

		return allTransitions;

	}
	
	
	@SuppressWarnings("unused")
	private static void makeReport() throws IOException{
		
		
		for(int i=0; i<allTransitions.size(); i++){
			
			TransitionList currentTransitionList=allTransitions.get(i);
			
			String oldVersion = currentTransitionList.getOldVersion();
			
			String newVersion = currentTransitionList.getNewVersion();
			
			String content = "";
			 
			File file = new File(newVersion+"-"+oldVersion+"Report.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			ArrayList<Transition> currentTransitions = currentTransitionList.getList();
			
			for(int j=0; j<currentTransitions.size(); j++){
				
				//Transition currentTransition = currentTransitions.get(i);
				
				
				if(currentTransitions.get(j) instanceof Insersion){
					

					
					if(currentTransitions.get(j).getType().equals("UpdateTable")){
						
						
						//content=content+currentTransitions.get(j).affectedTable+",";

						for(int k=0; k<currentTransitions.get(j).getNumOfAffAttributes(); k++){
							
							content=content+currentTransitions.get(j).getAffTable().getName()+","+"ADD_ATTRIBUTE"+"\n";
						
						}
						
					}
					else{
						
						content=content+"/*"+currentTransitions.get(j).getAffTable().getName()+","+"ADD_SELF"+"*/"+"\n";

						
					}
					
				}
				else if(currentTransitions.get(j) instanceof Deletion){
					

					if(currentTransitions.get(j).getType().equals("UpdateTable")){
						
						//content=content+currentTransitions.get(j).affectedTable+",";
						
						for(int k=0; k<currentTransitions.get(j).getNumOfAffAttributes(); k++){
						
							content=content+currentTransitions.get(j).getAffTable().getName()+","+"DELETE_ATTRIBUTE"+"\n";
						
						}
						
					}
					else{
						
						content=content+"/*"+currentTransitions.get(j).getAffTable().getName()+","+"DELETE_SELF"+"*/"+"\n";

						
					}
					
				}
				else{
					
					if(currentTransitions.get(j).getType().equals("TypeChange")){
						
						//content=content+currentTransitions.get(j).affectedTable+",";
						
						for(int k=0; k<currentTransitions.get(j).getNumOfAffAttributes(); k++){
						
							content=content+"/*"+currentTransitions.get(j).getAffTable().getName()+","+"UPDATE_ATTRIBUTE"+"*/"+"\n";
						
						}
						
					}
					else{
						
						for(int k=0; k<currentTransitions.get(j).getNumOfAffAttributes(); k++){
							
							content=content+"/*"+currentTransitions.get(j).getAffTable().getName()+","+"UPDATE_SELF"+"*/"+"\n";
						
						}
						
						
					}
					
				}
				
				//System.out.println(currentTransition.getType());
				
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();		
			
		}
		
	}
	

}
