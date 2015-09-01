package gui.tableElements;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import data.dataKeeper.GlobalDataKeeper;

public class JvTable extends JTable {

    //private int originalRowHeight=1;
   // private float zoomFactor = 1.0f;
	private Dimension initialIntercellSpacing=new Dimension();

    public JvTable(TableModel dataModel)
    {
        super(dataModel);
        initialIntercellSpacing=super.getIntercellSpacing();
    }

  

    public void setZoom(int rowHeight, int columnWidth)
    {
    	
		
		for(int i=0; i<super.getRowCount(); i++){
			super.setRowHeight(i, rowHeight);
				
		}

		//generalTable.setGridColor(new Color(0,0,0,100));
		//super.setIntercellSpacing(new Dimension(0, 0));

		
		
		for(int i=0; i<super.getColumnCount(); i++){
			if(i==0){
				super.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
				//super.getColumnModel().getColumn(0).setMaxWidth(columnWidth);
				//super.getColumnModel().getColumn(0).setMinWidth(columnWidth);
			}
			else{
				super.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
				//super.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
				//super.getColumnModel().getColumn(i).setMinWidth(columnWidth);
			}
		}
        firePropertyChange("zoom", 1500, 5000);
    }
    
    public void showGrid(boolean showGridBoolean){
    	System.out.println("??"+showGridBoolean);
    	super.setShowGrid(showGridBoolean);
    	firePropertyChange("grid", !showGridBoolean, showGridBoolean);

    }

    public void uniformlyDistributed(int columnWidth){
    	for(int i=0; i<super.getColumnCount(); i++){
			if(i==0){
				super.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
				//super.getColumnModel().getColumn(0).setMaxWidth(columnWidth);
				//super.getColumnModel().getColumn(0).setMinWidth(columnWidth);
			}
			else{
				super.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
				//super.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
				//super.getColumnModel().getColumn(i).setMinWidth(columnWidth);
			}
		}
        firePropertyChange("uniformly", 1500, 5000);
    }
   
    
    public void notUniformlyDistributed(GlobalDataKeeper globalDataKeeper){
    	for(int i=0; i<super.getColumnCount(); i++){
    		if(i==0){
    			super.getColumnModel().getColumn(0).setPreferredWidth(150);
    			//generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
    			//generalTable.getColumnModel().getColumn(0).setMinWidth(150);
    		}
    		else{
    			int tot=800/globalDataKeeper.getAllPPLTransitions().size();
    			int sizeOfColumn=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(i-1).getSize()*tot;
    			super.getColumnModel().getColumn(i).setPreferredWidth(sizeOfColumn);
    			//generalTable.getColumnModel().getColumn(i).setMaxWidth(sizeOfColumn);
    			//generalTable.getColumnModel().getColumn(i).setMinWidth(70);
    		}
		}
        firePropertyChange("uniformly", 1500, 5000);
    }
    
}
