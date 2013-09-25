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
    
    public ImportData(String tmpFilepath){
    	
    	filepath=tmpFilepath;
    	
    }
	
	
	public void loadDataset() throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(filepath));
        String sAllInput="";
        String sbuf = br.readLine();
        int flag=0;
        
        while(sbuf != null){
        	  if(flag==0){
        		  sAllInput = sAllInput+sbuf;
        		  sbuf = br.readLine();
        	  }
        	  else{
        		  sAllInput = sAllInput+"\n"+sbuf;
        		  sbuf = br.readLine();
        	  }
        	  flag=1;
        } 

       String[] sAllSchemas=sAllInput.split("\n");
       String[] tmpfolder=filepath.split("\\\\");
       String 	assistant=tmpfolder[tmpfolder.length-1];
       String[] folder=assistant.split("\\.");
       projectDataFolder=folder[0];

       String sStandardString= "datasets" + File.separator +projectDataFolder+File.separator;

       for(int i=0; i<sAllSchemas.length; i++){
    	   
    	   System.out.println(sAllSchemas[i]);
    	   
       }
       
       Transitions trs = new Transitions();
       
       for(int i=0; i<sAllSchemas.length; i++){
        	
        	if(i==sAllSchemas.length-1){
        		String sFinalString2=sStandardString+sAllSchemas[i];
            	Schema schema=HecateParser.parse(sFinalString2);
            	AllSchemas.add(schema);
        		break;
        	}
        	String sFinalString=sStandardString+sAllSchemas[i].trim();
        	
        	AllSchemas.add(HecateParser.parse(sFinalString));
        	
        	String sFinalString2=sStandardString+sAllSchemas[i+1].trim();
        	
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
