package dataProccessing;

//import hecate.parser.Delta;
//import hecate.parser.DiffResult;
//import hecate.parser.HecateParser;
//import hecate.sqlSchema.Attribute;
//import hecate.sqlSchema.Schema;
//import hecate.transitions.Deletion;
//import hecate.transitions.Insersion;
//import hecate.transitions.Transition;
//import hecate.transitions.TransitionList;
//import hecate.transitions.Transitions;
//import hecate.transitions.Update;

import gr.uoi.cs.daintiness.hecate.diff.Delta;
import gr.uoi.cs.daintiness.hecate.diff.DiffResult;
import gr.uoi.cs.daintiness.hecate.parser.HecateParser;
import gr.uoi.cs.daintiness.hecate.sql.Attribute;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.Transition;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;
import gr.uoi.cs.daintiness.hecate.transitions.Transitions;
import gr.uoi.cs.daintiness.hecate.transitions.Update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import data.pplTransition.AtomicChange;
import data.pplTransition.TableChange;

public class DiffFromTwoSchemas {
	
	private static String firstSchema=null;
	private static String secondSchema=null;
	private String projectDataFolder=null;
	private static ArrayList<TransitionList> currentTransitions=new ArrayList<TransitionList>();
	private static ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private static TreeMap<String,TableChange> tableChangesForTwoSchemas = new  TreeMap<String,TableChange>();



	
	public DiffFromTwoSchemas(String tmpFirstSchema,String tmpSecondSchema,String tmpProject){
		
		firstSchema=tmpFirstSchema;
		secondSchema=tmpSecondSchema;
		projectDataFolder=tmpProject;
		
	}
	
	
	public void findDifferenciesFromTwoSchemas() throws IOException{
		
		String sStandardString=projectDataFolder+File.separator;
		Transitions trs = new Transitions();
		
		String sFinalString=sStandardString+firstSchema;
    	String sFinalString2=sStandardString+secondSchema;
    	
    	Schema oldSchema = HecateParser.parse(sFinalString);
		oldSchema.setTitle(firstSchema);
		Schema newSchema = HecateParser.parse(sFinalString2);
		newSchema.setTitle(secondSchema);
		
		Delta delta = new Delta();
		
		TransitionList tmpTransitionList =new TransitionList();
		@SuppressWarnings("static-access")
		DiffResult tmpDiffRes=delta.minus(oldSchema, newSchema); 
		tmpTransitionList=tmpDiffRes.tl;
		trs.add(tmpTransitionList);
		
		test(trs);
		
		//calculateChangesforTheseVersions();	
		
		
		
	}
	
	private static void test(Transitions tl) throws FileNotFoundException {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream("TransitionsBetweenTwoVersions.xml"));
			
			
			//***********************************************
			
			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File("TransitionsBetweenTwoVersions.xml");
			Transitions root = (Transitions) u.unmarshal(inputFile);
			
			currentTransitions=(ArrayList<TransitionList>) root.getList();
			
			
		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
			//e.printStackTrace();
		}
	
		
		makeAtomicChanges();	
		makeTableChanges();
	}
	
	private static void makeAtomicChanges(){
		
			ArrayList<Transition> transitions=currentTransitions.get(0).getList();

			
			for(int j=0; j<transitions.size(); j++){
				
				
				Collection<Attribute> tmpAffectedAttributes = transitions.get(j).getAffAttributes();
				
				Iterator<Attribute> lala = tmpAffectedAttributes.iterator();

				while(lala.hasNext()){					
					
					Attribute tmpHecAttribute = (Attribute) lala.next();
					String tmpType;
					
					if(transitions.get(j) instanceof Insersion){
						
						if(transitions.get(j).getType().equals("UpdateTable")){
							tmpType="Addition";
						}
						else{
							tmpType="Addition of New Table";
						}
					}
					else if(transitions.get(j) instanceof Deletion){
						

						if(transitions.get(j).getType().equals("UpdateTable")){
							tmpType="Deletion";
						}
						else{
							tmpType="Deletion of whole Table";
						}
						
					}
					else{
						
						if(transitions.get(j).getType().equals("TypeChange")){
							tmpType="Type Change";
						}
						else{
							tmpType="Key Change";
						}
					}
					
					AtomicChange tmpAtomicChange= new AtomicChange(transitions.get(j).getAffTable().getName(),tmpHecAttribute.getName(),tmpType,firstSchema,secondSchema,j);
					
					atomicChanges.add(tmpAtomicChange);
				
				}
				
			}
		
		for(int i=0; i<atomicChanges.size(); i++){
			System.out.println(atomicChanges.get(i).toString());
		}
		
		
	}
	
	private static void makeTableChanges(){
				
		for(int i=0; i<atomicChanges.size(); i++){
			
			if(tableChangesForTwoSchemas.containsKey(atomicChanges.get(i).getAffectedTableName())){
				
				Integer transitionID=atomicChanges.get(i).getTransitionID();
				
				if(tableChangesForTwoSchemas.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().containsKey(transitionID)){
				
					tableChangesForTwoSchemas.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().get(transitionID).add(atomicChanges.get(i));
				
				}
				else{
					
					ArrayList<AtomicChange> tmpAtomicChanges = new ArrayList<AtomicChange>();
					
					tableChangesForTwoSchemas.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().put(transitionID, tmpAtomicChanges);
					
					tableChangesForTwoSchemas.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().get(transitionID).add(atomicChanges.get(i));
					
				}
				
			}
			else{
				
				TreeMap<Integer,ArrayList<AtomicChange>> tmpAtomicChanges = new TreeMap<Integer,ArrayList<AtomicChange>>();
						
				Integer transitionID=atomicChanges.get(i).getTransitionID();

				tmpAtomicChanges.put(transitionID,new ArrayList<AtomicChange>());
				
				tmpAtomicChanges.get(transitionID).add(atomicChanges.get(i));

				TableChange tmpTableChange= new TableChange(atomicChanges.get(i).getAffectedTableName(), tmpAtomicChanges);
				
				tableChangesForTwoSchemas.put(atomicChanges.get(i).getAffectedTableName(), tmpTableChange);
				
			}
			
		}
		
	
		
	}

	public TreeMap<String,TableChange> getTableChangesForTwoSchemas(){
		
		return tableChangesForTwoSchemas;
		
	}
}
