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

package org.kaist.ie.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;
import org.kaist.ie.fig.FigCCSNode;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.demo.SampleNode;
import org.tigris.gef.graph.presentation.DefaultGraphModel;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;

import ac.kaist.ccs.base.CmdGridChart;
import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.base.NodeDescriptor;
import ac.kaist.ccs.base.UiGlobals;

import java.beans.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Random;


public class LoadingWorker extends JPanel
                              implements ActionListener, 
                                         PropertyChangeListener {
	Logger logger = Logger.getLogger(LoadingWorker.class);
	
	private final static int STATUS_STARTED = 0;
	private final static int STATUS_STOPPED = 1;
	private final static int STATUS_CANCELED = 2;
	
	private int status;
	private int pre_scaled;
	
	private CNodeData nodeData = null;
	private CEdgeData edgeData = null;
	
    private JProgressBar progressBar;
    private JButton startButton;
    private JButton stopButton;
    private JButton cancelButton;
    private JTextArea taskOutput;
    private NodeTask task;
    
    private int max_work = 0;
    private int cur_work = 0;
    
    private JFrame frame = null;
    private JGraph graph = null;
    
    private HashMap<String, FigCCSNode> nodeHash = new HashMap<String, FigCCSNode>();
    
    JXPanel loadingPanel = null;
    String loadingText = "Now loading node info...";
  

    class NodeTask extends SwingWorker<Void, Void> {
    	boolean progressFlag = true;
    	CNodeData nodeData = null;
    	float minLocx = 0;
    	float minLocy = 0;
    	float maxLocx = 0;
    	float maxLocy = 0;
    	
    	JGraph _graph = null;
    	JPanel panel = null; 
    	

    	public NodeTask(CNodeData nodeData, JPanel panel, JGraph graph)
    	{
    		this.nodeData = nodeData;
    		this.panel = panel;
    		this._graph = graph;
    		
    		minLocx = nodeData.getMinXValue();
        	minLocy = nodeData.getMinYValue();
        	maxLocx = nodeData.getMaxXValue();
        	maxLocy = nodeData.getMaxYValue();
    	}
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
        	 JPanel mainPanel = UiGlobals.getMainPane();
             mainPanel.remove(UiGlobals.getGraphPane());
             mainPanel.add(loadingPanel, BorderLayout.CENTER);
            //Initialize progress property.
            setProgress(0);
           
            Editor editor = _graph.getEditor();
            
            float[] locxArry = nodeData.getLocxArry();
            float[] locyArry = nodeData.getLocyArry();
            
            
            HashMap<String, DoublePair> nodeLocMap = nodeData.getHashMap();
            String[] srcNames = edgeData.getSrcNameArry();
        	String[] destNames = edgeData.getDestNameArry();
        	
            
            max_work = nodeData.size();
            Layer cmp = editor.getLayerManager().getActiveLayer();
            
            editor.getLayerManager().setPaintActiveOnly(true);
            
            int inserted = 0;
            double preScale = nodeData.getPre_scale();
            int padding = nodeData.getPadding();
            
            /*for(int count = 0 ; count < srcNames.length ; count++){
            	DoublePair srcLoc = nodeLocMap.get(srcNames[count]);
        		DoublePair destLoc = nodeLocMap.get(destNames[count]);
        		int srcLocx = (int)((srcLoc.x+Math.abs(minLocx))*preScale) + padding/2;
            	int srcLocy = (int)((srcLoc.y+Math.abs(minLocy))*preScale) + padding/2;
            	int destLocx = (int)((destLoc.x+Math.abs(minLocx))*preScale) + padding/2;
            	int destLocy = (int)((destLoc.y+Math.abs(minLocy))*preScale) + padding/2;
            	
            	FigLine line = new FigLine(srcLocx, srcLocy, destLocx, destLocy, Color.blue);
            	line.setLineColor(Color.blue);
            	editor.add(line);
            	
            	if(count % 100 == 0) System.out.println(count+"/"+srcNames.length);
            }*/
            
        	for(int count = cur_work ; count < max_work ; count++){
        		try{
	        		inserted++;
	        		
	            	int locx = (int)((locxArry[count]+Math.abs(minLocx))*preScale) + padding/2;
	            	int locy = (int)((locyArry[count]+Math.abs(minLocy))*preScale) + padding/2;
	            	
	            	String nodeName = nodeData.getPointerName(count);
	            	NodeDescriptor desc = new NodeDescriptor();
	            	desc.setName(nodeName);
	            	desc.setGroup(nodeData.getGroup(count));
	            	FigCCSNode rect = new FigCCSNode(locx, locy, 7, 7, desc);
	            	
	            	rect.setLineColor(nodeData.getColor(count));
	
	            	rect.setLocked(true);
	
//	            	if(cmp == null)
//	            		cmp = editor.getLayerManager().getActiveLayer();
//	            	else
//	            	{
//	            		if(cmp != editor.getLayerManager().getActiveLayer()){
//	            			System.out.println(cmp);
//	            			System.out.println(editor.getLayerManager().getActiveLayer());
//	            			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
//	            			try{ Thread.sleep(3000); }catch(Exception e){}
//	            			//break;
//	            		}
//	            	}
	            	editor.add(rect);
	            	nodeHash.put(nodeName, rect);
	            	cur_work++;
	            	if(cur_work%1000 == 0){
	            		UiGlobals.setStatusbarText(" Node Rendering... "+cur_work+"/"+max_work);//System.out.println("cur_work! : "+cur_work);
	            		
	            		editor.getLayerManager().setPaintActiveOnly(false);
	            		
	            		editor.getLayerManager().setPaintActiveOnly(true);
	            		
	            	}
	            	if(cur_work%100 == 0 ){ 
	            		
	            		try{
	            			Thread.sleep(5);
	            		}catch(Exception e){}
	            		changeProgress();
	            	}
	                if(!progressFlag) break;
        		}catch(OutOfMemoryError e){
        			e.printStackTrace();
        			JOptionPane.showMessageDialog(UiGlobals.getApplet(),
        				    "Node loading is fail because your java heap space too small to handle COEX. If you want to increase java heap space, please reference guide in coex website.",
        				    "Node loading error",
        				    JOptionPane.ERROR_MESSAGE);
        		}
            }
        	editor.getLayerManager().setPaintActiveOnly(false);
        	//editor.damageAll();
        	
        	
        	java.util.List<Fig> nodes = editor.getLayerManager().getContents();
        	System.out.println("inserted : "+inserted+", "+nodes.size());
        	//for(int i = 0 ;i < nodes.size(); i++){
        	//	System.out.println((i+1)+", "+nodes.get(i).getOwner());
        	//}
        	
            return null;
        }
        public void stop(){
        	progressFlag = false;
        }
        /*
         * Executed in event dispatch thread
         */
        public void done() {
        	if(UiGlobals.get_scaleSlider()!= null)
        		UiGlobals.get_scaleSlider().setEnabled(true);
        	
            //Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            //taskOutput.append("Node Rendering Finish!\n");
            
            System.out.println("status : "+status);
            //if(status == STATUS_CANCELED || status == STATUS_STARTED)
            //	frame.setVisible(false);
            
            UiGlobals.getCoordBottomPanel().remove(panel);
            UiGlobals.setStatusbarText(" Node rendering is completed.");
            
            
            if(UiGlobals.getIsExample().equals("Y")){
            	if(UiGlobals.getExampleType().equals("2")){
            		CmdGridChart cmdGridChart = new CmdGridChart();
            		cmdGridChart.doIt();
            	}
            }
            
            UiGlobals.setNodeHash(nodeHash);
            
            JPanel mainPanel = UiGlobals.getMainPane();
            mainPanel.remove(loadingPanel);
            mainPanel.add(UiGlobals.getGraphPane(), BorderLayout.CENTER);
            
        }
    }
    
    public LoadingWorker(JGraph graph) {
        super();
        nodeData = new CNodeData();
        edgeData = new CEdgeData();
        
        this.graph = graph;
        this.pre_scaled = UiGlobals.getPre_scaled();
      
       
        
        if(UiGlobals.getcNodeData() == null || (!UiGlobals.getFileName().equals(UiGlobals.getLoadedFileName()))){
        	int readCount = -1;
        	readCount = readCoordData();
        	UiGlobals.setNodeCount(readCount);
        	int readEdgeCount = -1;
        	readEdgeCount = readEdgeData();
        }else{
        	nodeData = UiGlobals.getcNodeData();
        }
        	
        
        
        init();

        
        
                
        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        
        stopButton = new JButton("Stop");
        stopButton.setActionCommand("Stop");
        stopButton.addActionListener(this);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        progressBar.setStringPainted(true); 

//        taskOutput = new JTextArea(5, 20);
//        taskOutput.setMargin(new Insets(5,5,5,5));
//        taskOutput.setEditable(false);

        //JPanel panel = new JPanel();
        //panel.add(startButton);
        //panel.add(stopButton);
        //panel.add(cancelButton);
        //panel.add(progressBar);
        

        //add(panel, BorderLayout.PAGE_START);
        this.setLayout(new MigLayout("insets -2 -2 0 0"));
        int width = UiGlobals.getCoordBottomPanel().getSize().width;
        add(progressBar, new CC().wrap().width(""+width));
        setOpaque(true); //content panes must be opaque
        UiGlobals.getCoordBottomPanel().add(this);

        loadingPanel = new JXPanel();
		loadingPanel.setBackgroundPainter(new MattePainter(PaintUtils.AERITH, true));
		JXBusyLabel label = new JXBusyLabel(new Dimension(25, 25));
		label.getBusyPainter().setPoints(25);
		label.getBusyPainter().setTrailLength(12);
		label.setName("busyLabel");
        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
        label.setBusy(true);
        label.setText(loadingText);
        label.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        loadingPanel.setLayout(new BorderLayout(UiGlobals.getMainPane().getWidth()/2-180, 0));
        loadingPanel.add(BorderLayout.WEST, new JLabel(""));
		loadingPanel.add(BorderLayout.CENTER, label);
		
        startButton.setEnabled(false);
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new NodeTask(nodeData, this, this.graph);
        //task.addPropertyChangeListener(this);
        status = STATUS_STARTED;
        task.execute();
    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
    	Object s = evt.getSource();
    	
    	if(s == startButton){
    		task = new NodeTask(nodeData, this, this.graph);
    		status = STATUS_STARTED;
    		task.execute();
    		System.out.println("execute?");
    	}
    	else if(s == stopButton){
    		//progressBar.setIndeterminate(false);
    		status = STATUS_STOPPED;
    		task.stop();

    	}
    	else if(s == cancelButton){
    		//progressBar.setIndeterminate(false);
    		status = STATUS_CANCELED;
    		task.stop();
    		frame.setVisible(false);

    	}
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }
    
    public void changeProgress(){
    	//progressBar.setIndeterminate(false);
    	//System.out.println("progress : "+(float)cur_work*100/max_work);
    	progressBar.setValue((int)((float)cur_work*100/max_work));
        progressBar.setString(cur_work+"/"+max_work);
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
				UiGlobals.setLoadedFileName(filename);
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

				nodeData.insertItem(subStrs[0], Float.parseFloat(subStrs[1]), Float
						.parseFloat(subStrs[2]));

				lineCount++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		UiGlobals.setcNodeData(nodeData);
		return lineCount;
	}
    
    public int readEdgeData() {
    	String filename = UiGlobals.getFileName()+".edges";
		//int totalCount = getFileLineCount(filename);
		//if(totalCount <= 0) return -1;
		
		//edgeData.setPointCount(totalCount);
		//edgeData.init();

		int lineCount = 0;
		try {
			BufferedReader br = Utils.getInputReader(filename);
			
			if(br == null) return 0;
			
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
				
				if(Float.parseFloat(subStrs[2]) < 0.999) continue;
				edgeData.insertItem(subStrs[0], subStrs[1], Float.parseFloat(subStrs[2]));

				//if(lineCount%100 == 0) System.out.println("read edge.. : "+lineCount);
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
			nodeData.insertItem("random_" + count, random.nextInt(maxWidth), random
					.nextInt(maxHeight));
		}
		
		for (int count = 0; count < size; count++) {
			edgeData.insertItem("random_" + random.nextInt(10), "random_" + random.nextInt(10), random.nextFloat());
		}
		
		return size;
	}
    
    public void init()
    {
    	logger.debug("public void init");
    	logger.debug("====[S]======================");
    	Editor editor = graph.getEditor();
    	
    	float minLocx = Utils.minValue(nodeData.getLocxArry());
		float minLocy = Utils.minValue(nodeData.getLocyArry());
		float maxLocx = Utils.maxValue(nodeData.getLocxArry());
		float maxLocy = Utils.maxValue(nodeData.getLocyArry());

		int width, height;
		
		width = (int) maxLocx - (int) minLocx + NodeRenderManager._PADDING;
		height = (int) maxLocy - (int) minLocy + NodeRenderManager._PADDING;
		

		double scale = UiGlobals.getGrid_scale();//Math.pow(2, pre_scaled - 1);

		
		logger.debug("pre_scaled : " + pre_scaled + ", real scale : "+ scale);
		

		logger.debug("edtor.setScale("+1.0 / scale+")");
		editor.setScale(1.0 / scale);

		nodeData.setPre_scale(scale);
		
		
		int drawingSizeX = (int)(width*scale);
		int drawingSizeY = (int)(height*scale);
		logger.debug("drawingSizeX : "+drawingSizeX);
		logger.debug("drawingSizeY : "+drawingSizeY);
		graph.setDrawingSize(drawingSizeX, drawingSizeY);
		UiGlobals.setDrawingSizeX(drawingSizeX);
		UiGlobals.setDrawingSizeY(drawingSizeY);

		LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed(
				"Grid");
		HashMap<String, Object> map = new HashMap<String, Object>();
		logger.debug("spacing_include_stamp : "+(UiGlobals.getGrid_spacing()*scale));
		map.put("spacing_include_stamp", (int)(UiGlobals.getGrid_spacing()*scale));
		logger.debug("thick : "+scale);
		map.put("thick", (int) scale);
		
		grid.adjust(map);
		
		UiGlobals.setStatusbarText(" resolution : x "+scale);
		logger.debug("====[E]======================");
    }
}