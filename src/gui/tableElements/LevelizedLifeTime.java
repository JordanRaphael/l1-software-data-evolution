package gui.tableElements;
//package gui;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//
//import javax.swing.JOptionPane;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
//
//import parser.Delta;
//import parser.DiffResult;
//import sqlSchema.Schema;
//import transitions.Deletion;
//import transitions.Insersion;
//import transitions.TransitionList;
//import transitions.Transitions;
//import transitions.Update;
//
//public class LevelizedLifeTime {
//	
//	private ArrayList<Schema> selectedSchemas=new ArrayList<Schema>(); 
//	private static ArrayList<TransitionList> currentTransitions=new ArrayList<TransitionList>();
//	
//	public LevelizedLifeTime(ArrayList<Schema> tmpSelectedSchemas){
//		selectedSchemas=tmpSelectedSchemas;
//	}
//	
//	public void makeTransitions() throws FileNotFoundException{
//		
//		Transitions trs = new Transitions();
//	    
//		for(int i=0; i<selectedSchemas.size(); i++){
//        	
//        	if(i==selectedSchemas.size()-1){
//        		//String sFinalString2=sStandardString+sAllSchemas[i];
//            	//HecateParser parser2 = new HecateParser(sFinalString2);
//            	//Schema schema=parser2.getSchema();
//            	//schema.setTitle(sAllSchemas[i]);
//            	//AllSchemas.add(schema);
//        		break;
//        	}
//        	
//        	Schema oldSchema = selectedSchemas.get(i);
//    		oldSchema.setTitle(selectedSchemas.get(i).getName());
//    		Schema newSchema = selectedSchemas.get(i+1);
//    		newSchema.setTitle(selectedSchemas.get(i+1).getName());
//    		
//    		Delta delta = new Delta();
//    		
//    		TransitionList tmpTransitionList =new TransitionList();
//    		
//    		@SuppressWarnings("static-access")
//			DiffResult tmpDiffRes=delta.minus(oldSchema, newSchema); 
//    		
//    		tmpTransitionList=tmpDiffRes.tl;
//    		trs.add(tmpTransitionList);
//    		
//        }
//       
//       	exportJAXB(trs);
//		
//	}
//	
//	private static void exportJAXB(Transitions tl) throws FileNotFoundException {
//		try {
//			
//			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(tl, new FileOutputStream("LevelizedTransitions.xml"));
//			
//			
//			//***********************************************
//			
//			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
//			Unmarshaller u=jaxbContext1.createUnmarshaller();
//			File inputFile=new File("LevelizedTransitions.xml");
//			Transitions root = (Transitions) u.unmarshal(inputFile);
//			
//			
//			currentTransitions=(ArrayList<TransitionList>) root.getList();
//			
//			
//			
//		} catch (JAXBException e) {
//			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
//			return;
//			//e.printStackTrace();
//		}
//	}
//	
//	public ArrayList<Schema> getLevelizedSchemas(){
//		
//		return selectedSchemas;
//		
//	}
//	
//	public ArrayList<TransitionList> getLevelizedTransitions(){
//		
//		return currentTransitions;
//		
//	}
//	
//
//}
