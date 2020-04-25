package gui.mainEngine;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.ParametersJDialog;
import gui.dialogs.ProjectInfoDialog;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class BusinessLogic {
	
	public Gui gui;
	
	public BusinessLogic(Gui gui) {
		this.gui = gui;
	}
	
	protected void createProjectAction(ActionEvent e1) {
		CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

		createProjectDialog.setModal(true);
		
		
		createProjectDialog.setVisible(true);
		
		if(createProjectDialog.getConfirmation()){
			
			createProjectDialog.setVisible(false);
			
			File file = createProjectDialog.getFile();
            System.out.println(file.toString());
            this.gui.project=file.getName();
            String fileName=file.toString();
            System.out.println("!!"+this.gui.project);
          
			try {
				this.gui.importData(fileName);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
				return;
			} catch (RecognitionException e) {
				
				JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
				return;
			}
			
			
		}

	}
	
	protected void infoAction(ActionEvent e1) {
		if(!(this.gui.currentProject==null)){
			
			
			System.out.println("Project Name:"+this.gui.projectName);
			System.out.println("Dataset txt:"+this.gui.datasetTxt);
			System.out.println("Input Csv:"+this.gui.inputCsv);
			System.out.println("Output Assessment1:"+this.gui.outputAssessment1);
			System.out.println("Output Assessment2:"+this.gui.outputAssessment2);
			System.out.println("Transitions File:"+this.gui.transitionsFile);
			
			System.out.println("Schemas:"+this.gui.globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("Transitions:"+this.gui.globalDataKeeper.getAllPPLTransitions().size());
			System.out.println("Tables:"+this.gui.globalDataKeeper.getAllPPLTables().size());
			
			
			ProjectInfoDialog infoDialog = new ProjectInfoDialog(this.gui.projectName,this.gui.datasetTxt,this.gui.inputCsv,this.gui.transitionsFile,this.gui.globalDataKeeper.getAllPPLSchemas().size(),
					this.gui.globalDataKeeper.getAllPPLTransitions().size(), this.gui.globalDataKeeper.getAllPPLTables().size());
			
			infoDialog.setVisible(true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}
	
	protected void showGeneralLifetimePhasesWithClustersPLDAction(ActionEvent e1) {
		this.gui.wholeCol=-1;
		if(!(this.gui.project==null)){
			
			ParametersJDialog jD=new ParametersJDialog(true);
			
			jD.setModal(true);
			
			jD.setVisible(true);
			
			if(jD.getConfirmation()){
			
				this.gui.timeWeight = jD.getTimeWeight();
				this.gui.changeWeight = jD.getChangeWeight();
				this.gui.preProcessingTime = jD.getPreProcessingTime();
				this.gui.preProcessingChange = jD.getPreProcessingChange();
				this.gui.numberOfPhases = jD.getNumberOfPhases();
				this.gui.numberOfClusters = jD.getNumberOfClusters();
				this.gui.birthWeight=jD.geBirthWeight();
				this.gui.deathWeight=jD.getDeathWeight();
				this.gui.changeWeightCl=jD.getChangeWeightCluster();
	            
	            System.out.println(this.gui.timeWeight+" "+this.gui.changeWeight);
	            
				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,this.gui.outputAssessment1,this.gui.outputAssessment2,this.gui.timeWeight,this.gui.changeWeight,this.gui.preProcessingTime,this.gui.preProcessingChange);
				
				mainEngine.parseInput();		
				System.out.println("\n\n\n");
				mainEngine.extractPhases(this.gui.numberOfPhases);
				mainEngine.connectTransitionsWithPhases(this.gui.globalDataKeeper);
				this.gui.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
				TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.gui.globalDataKeeper,this.gui.birthWeight,this.gui.deathWeight,this.gui.changeWeightCl);
				mainEngine2.extractClusters(this.gui.numberOfClusters);
				this.gui.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
				mainEngine2.print();
				
				if(this.gui.globalDataKeeper.getPhaseCollectors().size()!=0){
					TableConstructionWithClusters table=new TableConstructionWithClusters(this.gui.globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					this.gui.segmentSize=table.getSegmentSize();
					System.out.println("Schemas: "+this.gui.globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					this.gui.finalColumns=columns;
					this.gui.finalRows=rows;
					this.gui.tabbedPane.setSelectedIndex(0);
					this.gui.makeGeneralTablePhases();
					this.gui.fillClustersTree();
				}
				else{
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		}
		else{
			
			JOptionPane.showMessageDialog(null, "Please select a project first!");
			
		}
	
	}
	
	protected void showGeneralLifetimePhasesPLDAction(ActionEvent e1) {
		if(!(this.gui.project==null)){
			this.gui.wholeCol=-1;
			ParametersJDialog jD=new ParametersJDialog(false);
			
			jD.setModal(true);
			
			
			jD.setVisible(true);
			
			if(jD.getConfirmation()){
			
				this.gui.timeWeight = jD.getTimeWeight();
				this.gui.changeWeight = jD.getChangeWeight();
				this.gui.preProcessingTime = jD.getPreProcessingTime();
				this.gui.preProcessingChange = jD.getPreProcessingChange();
				this.gui.numberOfPhases = jD.getNumberOfPhases();
	            
	            System.out.println(this.gui.timeWeight+" "+this.gui.changeWeight);
	            
				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.gui.inputCsv,this.gui.outputAssessment1,this.gui.outputAssessment2,this.gui.timeWeight,this.gui.changeWeight,this.gui.preProcessingTime,this.gui.preProcessingChange);

				mainEngine.parseInput();		
				System.out.println("\n\n\n");
				mainEngine.extractPhases(this.gui.numberOfPhases);
				mainEngine.connectTransitionsWithPhases(this.gui.globalDataKeeper);
				this.gui.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
				
				
				if(this.gui.globalDataKeeper.getPhaseCollectors().size()!=0){
					TableConstructionPhases table=new TableConstructionPhases(this.gui.globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					this.gui.segmentSize=table.getSegmentSize();
					System.out.println("Schemas: "+this.gui.globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					this.gui.finalColumns=columns;
					this.gui.finalRows=rows;
					this.gui.tabbedPane.setSelectedIndex(0);
					this.gui.makeGeneralTablePhases();
					this.gui.fillPhasesTree();
				}
				else{
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		}
		else{
			
			JOptionPane.showMessageDialog(null, "Please select a project first!");
			
		}
	}
	
	protected void showGeneralLifetimeIDUAction(ActionEvent e1) {
		if(!(this.gui.currentProject==null)){
			this.gui.zoomInButton.setVisible(true);
			this.gui.zoomOutButton.setVisible(true);
			TableConstructionIDU table=new TableConstructionIDU(this.gui.globalDataKeeper);
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			this.gui.segmentSizeZoomArea = table.getSegmentSize();
			System.out.println("Schemas: "+this.gui.globalDataKeeper.getAllPPLSchemas().size());
			System.out.println("C: "+columns.length+" R: "+rows.length);

			this.gui.finalColumnsZoomArea=columns;
			this.gui.finalRowsZoomArea=rows;
			this.gui.tabbedPane.setSelectedIndex(0);
			this.gui.makeGeneralTableIDU();
			this.gui.fillTree();
			
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}
	
	protected void showLifetimeTableAction(ActionEvent e2) {
		if(!(this.gui.currentProject==null)){
			TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(this.gui.globalDataKeeper);
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			this.gui.segmentSizeDetailedTable=table.getSegmentSize();
			this.gui.tabbedPane.setSelectedIndex(0);
			this.gui.makeDetailedTable(columns,rows,true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}
	
	protected void editProjectAction(ActionEvent e2) {
		String fileName=null;
		File dir=new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(this.gui, "Open");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
            File file = fcOpen1.getSelectedFile();
            System.out.println(file.toString());
            this.gui.project=file.getName();
            fileName=file.toString();
            System.out.println("!!"+this.gui.project);
          
            BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(fileName));
				String line;
				
				while(true) {
					line = br.readLine();
					if (line == null) 
						break;
					if(line.contains("Project-name")){
						String[] projectNameTable=line.split(":");
						this.gui.projectName=projectNameTable[1];
					}
					else if(line.contains("Dataset-txt")){
						String[] datasetTxtTable=line.split(":");
						this.gui.datasetTxt=datasetTxtTable[1];
					}
					else if(line.contains("Input-csv")){
						String[] inputCsvTable=line.split(":");
						this.gui.inputCsv=inputCsvTable[1];
					}
					else if(line.contains("Assessement1-output")){
						String[] outputAss1=line.split(":");
						this.gui.outputAssessment1=outputAss1[1];
					}
					else if(line.contains("Assessement2-output")){
						String[] outputAss2=line.split(":");
						this.gui.outputAssessment2=outputAss2[1];
					}
					else if(line.contains("Transition-xml")){
						String[] transitionXmlTable=line.split(":");
						this.gui.transitionsFile=transitionXmlTable[1];
					}
					
					
				};	
				
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			
			}
			
			System.out.println(this.gui.projectName);
			
			CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(this.gui.projectName,this.gui.datasetTxt,this.gui.inputCsv,this.gui.outputAssessment1,this.gui.outputAssessment2,this.gui.transitionsFile);
		
			createProjectDialog.setModal(true);
			
			createProjectDialog.setVisible(true);
			
			if(createProjectDialog.getConfirmation()){
				
				createProjectDialog.setVisible(false);
				
				file = createProjectDialog.getFile();
	            System.out.println(file.toString());
	            this.gui.project=file.getName();
	            fileName=file.toString();
	            System.out.println("!!"+this.gui.project);
			
				try {
					this.gui.importData(fileName);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {
					
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}
				
			}
			
		}
		else{
			return;
		}
		
	}

	protected void loadProjectAction(ActionEvent e1) {
		
		String fileName=null;
		File dir=new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(this.gui, "Open");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
            File file = fcOpen1.getSelectedFile();
            System.out.println(file.toString());
            this.gui.project=file.getName();
            fileName=file.toString();
            System.out.println("!!"+this.gui.project);
            System.out.println(fileName);
          

		}
		else{
			return;
		}
		try {
			this.gui.importData(fileName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		} catch (RecognitionException e) {
			
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
		
	}

}
