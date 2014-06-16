/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package org.ssu.ml.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.ssu.ml.base.CmdGridChart;
import org.ssu.ml.base.DoublePair;
import org.ssu.ml.base.NodeDescriptor;
import org.ssu.ml.base.UiGlobals;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.demo.SampleNode;
import org.tigris.gef.graph.presentation.DefaultGraphModel;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;

import java.beans.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.ssu.ml.presentation.FigCustomNode;
import org.ssu.ml.ui.LoadingProgressBarNode.NodeTask;


public class LoadingProgressBarSearchAndMark extends JPanel
                              implements ActionListener, 
                                         PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -203850379942717505L;

	Logger logger = Logger.getLogger(LoadingProgressBarSearchAndMark.class);
	
	private SearchAndMark task;
	String filename;
	String propertyName;
	boolean isReset = false;

    class SearchAndMark extends SwingWorker<Void, Void> {
    	boolean progressFlag = true;
    	String keyword = "";
    	String propertyName = "";

    	public SearchAndMark(String keyword, String propertyName)
    	{
    		this.keyword = keyword;
    		this.propertyName = propertyName;
    	}
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            
         
            //Initialize progress property.
            setProgress(0);
            
            Editor editor = UiGlobals.curEditor();
            java.util.List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
            
            HashMap<String, HashMap<Integer, String>> annotationContent = UiGlobals.getAnnotationContent();
    		int searchIndex = UiGlobals.getPropertySearchCombo().getSelectedIndex();
    		String searchKeyword = UiGlobals.getPropertySearchField().getText();
    		
    		int findCount = 0;
    		String currentLayer = "SelectedLayer";//UiGlobals.getShowLayerCombo().getSelectedItem().toString();
    		
    		Vector<Fig> selectedFig = null;
    		if(!"New".equalsIgnoreCase(UiGlobals.getSearchType())){
				selectedFig = (Vector<Fig>) UiGlobals.getLayerData().get(currentLayer);
			}else
				selectedFig = new Vector<Fig>();
    		
    		Vector<Fig> intersectSelectFig = new Vector<Fig>();
    		//Vector<Fig> newSelectedFig = new Vector<Fig>();
    		System.out.println("UiGlobals.getSearchType(): "+UiGlobals.getSearchType());
	        for(int count = 0 ; count < nodes.size() ; count++)
	        {
	        	Fig node = nodes.get(count);
	        	FigCustomNode nodeCustom = (FigCustomNode)node;
	        	
	        	if(isReset){
	        		nodeCustom.resetFoundMark();	
	        		nodeCustom.setVisible(true);
    	        	nodeCustom.setSelectable(true);
	        		//editor.damageAll();
	        	}else{
	        		NodeDescriptor desc = (NodeDescriptor)nodeCustom.getOwner();
	        		HashMap<Integer, String> propertyMap = annotationContent.get(desc.getName());
	        		if(propertyMap != null){
	        			String property = propertyMap.get(searchIndex);
	        			
	        			
	        			
	        			
	        			if("New".equalsIgnoreCase(UiGlobals.getSearchType())){
	        				
		        			if(property.contains(searchKeyword)){
		        				findCount++;
		        				Color markColor = UiGlobals.getSearchMarkColor();
		        				nodeCustom.setFoundMark(markColor);
	    	        			nodeCustom.setVisible(true);
	    	    	        	nodeCustom.setSelectable(true);
	    	    	        	
	    	    	        	selectedFig.add(nodeCustom);
		        			}else{
		        				nodeCustom.setBorderColor(Color.black);	
		        				nodeCustom.resetBorderWidth();
		    	        		nodeCustom.setLineColor(CNodeData.getDefaultColor());
		    	        		
		    	        		if(UiGlobals.isShowOnlyFound()){
		    	        			nodeCustom.setVisible(false);
		    	    	        	nodeCustom.setSelectable(false);
		    	        		}
		        			}
	        			}else if("Union".equalsIgnoreCase(UiGlobals.getSearchType())){
		        			if(property.contains(searchKeyword)){
		        				findCount++;
		        				Color markColor = UiGlobals.getSearchMarkColor();
		        				nodeCustom.setFoundMark(markColor);
		        				
		        				if(!selectedFig.contains(nodeCustom))
		        					selectedFig.add(nodeCustom);
		        				
		        				if(UiGlobals.isShowOnlyFound()){
		    	        			nodeCustom.setVisible(true);
		    	        			nodeCustom.setSelectable(true);
		        				}
		        				//
		        			}
	        			}else if("Intersection".equalsIgnoreCase(UiGlobals.getSearchType())){
	        				nodeCustom.resetFoundMark();
	        				
		        			if(property.contains(searchKeyword)){
		        				if(selectedFig.contains(nodeCustom))
		        				{
		        					findCount++;
		        					Color markColor = UiGlobals.getSearchMarkColor();
			        				nodeCustom.setFoundMark(markColor);
			        				intersectSelectFig.add(nodeCustom);
		        				}else{
		        					if(UiGlobals.isShowOnlyFound()){
			    	        			nodeCustom.setVisible(false);
			    	        			nodeCustom.setSelectable(false);
			    	        		}
		        				}
		        			}else{
	        					if(UiGlobals.isShowOnlyFound()){
		    	        			nodeCustom.setVisible(false);
		    	        			nodeCustom.setSelectable(false);
		    	        		}
	        				}
	        			}else if("Minus".equalsIgnoreCase(UiGlobals.getSearchType())){
		        			if(property.contains(searchKeyword)){
		        				findCount++;
		        				selectedFig.remove(nodeCustom);
		        				nodeCustom.resetFoundMark();
		        				//
		    	        		if(UiGlobals.isShowOnlyFound()){
		    	        			nodeCustom.setVisible(false);
		    	        			nodeCustom.setSelectable(false);
		    	        		}
		        			}
	        			}
	        		}
	        	}
	        	
	        	if(count % 100 == 0)
	        		editor.damageAll();
	        	
	        }
	        if("New".equalsIgnoreCase(UiGlobals.getSearchType())){
		        String newLayerName = "SelectedLayer";
				//newLayerName+=UiGlobals.getShowLayerCombo().getItemCount();
				UiGlobals.getLayerColor().put(newLayerName, UiGlobals.getSearchMarkColor());
				UiGlobals.getLayerData().put(newLayerName, selectedFig);
				//UiGlobals.getShowLayerCombo().addItem(newLayerName);
				//UiGlobals.getShowLayerCombo().setSelectedIndex(UiGlobals.getShowLayerCombo().getItemCount()-1);
	        }else if("Intersection".equalsIgnoreCase(UiGlobals.getSearchType())){
	        	selectedFig = intersectSelectFig;
	        }
	        
	        for(Fig fig : selectedFig){
	        	editor.getLayerManager().getActiveLayer().reorder(fig, CmdReorder.BRING_TO_FRONT);
	        }
        	
	        String output = findCount+" gene(s) is found.";
	        if(isReset)
	        	output = "The color of genes are reseted.";
	        UiGlobals.setStatusbarText(output);
            return null;
        }
        public void stop(){
        	progressFlag = false;
        }
        /*
         * Executed in event dispatch thread
         */
        public void done() {
        	UiGlobals.getPropertySearchButton().setEnabled(true);
        	UiGlobals.getPropertySearchCombo().setEnabled(true);
        	UiGlobals.getPropertySearchField().setEnabled(true);
        	UiGlobals.getPropertyResetButton().setEnabled(true);
        }
    }
    
    
    public LoadingProgressBarSearchAndMark(String keyword, String propertyName) {
        this(keyword, propertyName, false);
    }
    
    public LoadingProgressBarSearchAndMark(String keyword, String propertyName, boolean isReset) {
        super(new BorderLayout());
        this.isReset = isReset;
        init();
                
        task = new SearchAndMark(keyword, propertyName);
        task.execute();
    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
    	Object s = evt.getSource();
    	
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }
    
    public void changeProgress(){
    	//progressBar.setIndeterminate(false);
    	//System.out.println("progress : "+(float)cur_work*100/max_work);
    	//progressBar.setValue((int)((float)cur_work*100/max_work));
        //progressBar.setString(cur_work+"/"+max_work);
        //taskOutput.append(String.format(
        //            "%s\n", data.toString(cur_work)));
    }
    
    
    
    public int readCoordData() {
		//int totalCount = getFileLineCount(filename);
		//if(totalCount <= 0) return -1;
		
		String filename = UiGlobals.getFileName()+".coord";

		int lineCount = 0;
		try {
			BufferedReader br = null;
			if(UiGlobals.getFileName()!=null){
				br = Utils.getInputReader(filename);	
			}
			
			if(br == null){
				makeRandomData(50, 300, 300);
				return 0;
			}
			
			String str = null;
			String sep = "\0";
			String[] seps = { "\t", ",", ".", " " };
			while ((str = br.readLine()) != null) {
				if (lineCount == 0) {
					// Find Separator
					for (int count = 0; count < seps.length; count++) {
						if (str.contains(seps[count])) {
							sep = seps[count];
							break;
						}
					}
				}

				String[] subStrs = str.split(sep);

				lineCount++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineCount;
	}
    
    public int makeRandomData(int size, int maxWidth, int maxHeight) {
		// data.
		Random random = new Random();

		for (int count = 0; count < size; count++) {
		}
		return size;
	}
    
    public void init()
    {
    	UiGlobals.getPropertySearchButton().setEnabled(false);
    	UiGlobals.getPropertySearchCombo().setEnabled(false);
    	UiGlobals.getPropertySearchField().setEnabled(false);
    	UiGlobals.getPropertyResetButton().setEnabled(false);
    	logger.debug("public void init");
    	logger.debug("====[S]======================");
    }
}