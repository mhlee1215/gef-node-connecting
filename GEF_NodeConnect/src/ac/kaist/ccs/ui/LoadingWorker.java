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

package ac.kaist.ccs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.base.ReorderAction;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.base.NodeDescriptor;
import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSEdgeData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.fig.FigCCSLine;
import ac.kaist.ccs.fig.FigCCSNode;


public class LoadingWorker extends JPanel
                              implements ActionListener, 
                                         PropertyChangeListener {
	Logger logger = Logger.getLogger(LoadingWorker.class);
	
	private final static int STATUS_STARTED = 0;
	private final static int STATUS_STOPPED = 1;
	private final static int STATUS_CANCELED = 2;
	
	private int status;
	//private int pre_scaled;
	
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
    
    private Map<Integer, List<CCSSourceData> > ccsData;
    private List<CCSEdgeData> ccsConData;
    
    private HashMap<String, FigCCSNode> nodeHash = new HashMap<String, FigCCSNode>();
    
    JXPanel loadingPanel = null;
    String loadingText = "Now loading node info...";
  
    int scale;
    
    
    public LoadingWorker(Map<Integer, List<CCSSourceData> > ccsData, List<CCSEdgeData> ccsConData, JGraph graph, int scale) {
        super();
        
        this.ccsData = ccsData;
        this.ccsConData = ccsConData;
        
        this.graph = graph;
        //this.pre_scaled = UiGlobals.getPre_scaled();
      
        this.scale = scale;
        
        
        
        
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
        task = new NodeTask(ccsData, ccsConData, this, this.graph, scale);
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
    		task = new NodeTask(ccsData, ccsConData, this, this.graph, scale);
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
    
    
    public void init()
    {
    	logger.debug("public void init");
    	logger.debug("====[S]======================");
    	Editor editor = graph.getEditor();
//    	
//    	float minLocx = Utils.minValue(nodeData.getLocxArry());
//		float minLocy = Utils.minValue(nodeData.getLocyArry());
//		float maxLocx = Utils.maxValue(nodeData.getLocxArry());
//		float maxLocy = Utils.maxValue(nodeData.getLocyArry());

		int width, height;
		
		width = CCSStatics.refImg.getWidth();//graph.getWidth();//(int) maxLocx - (int) minLocx + NodeRenderManager._PADDING;
		height = CCSStatics.refImg.getHeight();//graph.getHeight();// (int) maxLocy - (int) minLocy + NodeRenderManager._PADDING;
		System.out.println("width: "+width);
		System.out.println("height: "+height);

		double scale = UiGlobals.getGrid_scale();//Math.pow(2, pre_scaled - 1);

		
		//logger.debug("pre_scaled : " + pre_scaled + ", real scale : "+ scale);
		

		logger.debug("edtor.setScale("+1.0 / scale+")");
		editor.setScale(1.0 / scale);

		//nodeData.setPre_scale(scale);

		
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
		map.put("Color", Color.LIGHT_GRAY);
		map.put("bgColor", Color.white);
		map.put("spacing_include_stamp", 5000);
		map.put("paintLines", true);
		map.put("paintDots", false);
		map.put("stamp", (Image) CCSStatics.refImg);
		grid.adjust(map);
		
		UiGlobals.setStatusbarText(" resolution : x "+scale);
		logger.debug("====[E]======================");
    }
    
    
    class NodeTask extends SwingWorker<Void, Void> {
    	boolean progressFlag = true;
    	Map<Integer, List<CCSSourceData> > ccsData = null;
    	List<CCSEdgeData> ccsConData = null;
    	float minLocx = 0;
    	float minLocy = 0;
    	float maxLocx = 0;
    	float maxLocy = 0;
    	
    	JGraph _graph = null;
    	JPanel panel = null; 
    	int scale;
    	int padding = 50;
    	

    	public NodeTask(Map<Integer, List<CCSSourceData> > ccsData, List<CCSEdgeData> ccsConData, JPanel panel, JGraph graph, int scale)
    	{
    		this.ccsData = ccsData;
    		this.ccsConData = ccsConData;
    		this.panel = panel;
    		this._graph = graph;
    		this.scale = scale;
    	}
        /*
         * Main task. Executed in background thread.
         */
    	
    	public void addNode(List<CCSSourceData> nodeData){
    		//System.out.println("nodeData : "+nodeData);
    		Editor editor = _graph.getEditor();
    		for(int i = 0 ; i < nodeData.size() ; i++){
    			
    			CCSSourceData node = nodeData.get(i);
    			Fig fig = node.getNode();
    			Point loc = fig.getLocation();
    			loc.x = cvtLoc(loc.x);
    			loc.y = cvtLoc(loc.y);
    			fig.setLocation(loc);
    			
    			
    			//System.out.println(fig);
    			
    			if(cur_work%1000 == 0){
            		UiGlobals.setStatusbarText(" Node Rendering... "+cur_work+"/"+max_work);//System.out.println("cur_work! : "+cur_work);
            	}
            	if(cur_work%100 == 0 ){ 
            		try{
            			Thread.sleep(5);
            		}catch(Exception e){}
            		changeProgress();
            	}
                if(!progressFlag) break;
                editor.add(fig);
                
                //fig.reorder(func, lay);
                if(node.getEdge() != null){
                	//System.out.println("draw edge!"+node.getEdge().getEdgeFig());
                	Fig edgeFig = node.getEdge().getEdgeFig();
                	editor.add(edgeFig);
                	edgeFig.reorder(ReorderAction.SEND_TO_BACK, editor.getLayerManager().getActiveLayer());
                }
                
    			cur_work++;
    		}
    	}
    	
    	public void addEdge(List<CCSEdgeData> ccsConData){
    		//System.out.println("ccsConData : "+ccsConData);
    		Editor editor = _graph.getEditor();
    		for(int i = 0 ; i < ccsConData.size() ; i++){
    			
    			CCSEdgeData node = ccsConData.get(i);
    			FigCCSLine fig = node.getEdgeFig();
    			
    			
    			
    			
    			//System.out.println(fig);
    			
    			if(cur_work%1000 == 0){
            		UiGlobals.setStatusbarText(" Node Rendering... "+cur_work+"/"+max_work);//System.out.println("cur_work! : "+cur_work);
            	}
            	if(cur_work%100 == 0 ){ 
            		try{
            			Thread.sleep(5);
            		}catch(Exception e){}
            		changeProgress();
            	}
                if(!progressFlag) break;
                editor.add(fig);
    			cur_work++;
    		}
    	}
    	
        @Override
        public Void doInBackground() {
        	
        	 JPanel mainPanel = UiGlobals.getMainPane();
             mainPanel.remove(UiGlobals.getGraphPane());
             mainPanel.add(loadingPanel, BorderLayout.CENTER);
            //Initialize progress property.
            setProgress(0);
           
            Editor editor = _graph.getEditor();
            
//            float[] locxArry = nodeData.getLocxArry();
//            float[] locyArry = nodeData.getLocyArry();
//            
//            
//            HashMap<String, DoublePair> nodeLocMap = nodeData.getHashMap();
//            String[] srcNames = edgeData.getSrcNameArry();
//        	String[] destNames = edgeData.getDestNameArry();
        	
            
            //max_work = nodeData.size();
            
           
                      
            
            Layer cmp = editor.getLayerManager().getActiveLayer();
            
            editor.getLayerManager().setPaintActiveOnly(true);
            
            int inserted = 0;
//            double preScale = scale;// nodeData.getPre_scale();
//            int padding = 50;//nodeData.getPadding();
            
            cur_work = 0;
            max_work = 0;
//            if(ccsConData != null){
//            	max_work += ccsConData.size();
//            	//addEdge(ccsConData);
//            }
            
            
            Map<Integer, CCSSourceData> nodesAll = UiGlobals.getNodes();
            List<CCSSourceData> nodeList = new ArrayList<CCSSourceData>();
            for(Integer key : nodesAll.keySet()){
            	if(nodesAll.get(key) != null)
            		nodeList.add(nodesAll.get(key));
            }
            
            max_work += nodeList.size();
            //System.out.println("nodeList: "+nodeList);
            addNode(nodeList);
            
//            List<CCSSourceData> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
//            if(sourceData != null){
//            	max_work += sourceData.size();
//            	addNode(sourceData);
//            }
//            List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);
//            if(hubData != null){
//            	max_work += hubData.size();
//            	addNode(hubData);
//            }
//            List<CCSSourceData> plantData = ccsData.get(CCSSourceData.TYPE_PLANT);
//            if(plantData != null){
//            	max_work += plantData.size();
//            	addNode(plantData);
//            }
//            List<CCSSourceData> jointData = ccsData.get(CCSSourceData.TYPE_JOINT);
//            if(jointData != null){
//            	max_work += jointData.size();
//            	addNode(jointData);
//            }
            
            
            	
            
//        	for(int count = cur_work ; count < max_work ; count++){
//        		
//        		
//        		
//        		
//        		try{
//	        		inserted++;
//	        		
//	            	int locx = (int)((locxArry[count]+Math.abs(minLocx))*preScale) + padding/2;
//	            	int locy = (int)((locyArry[count]+Math.abs(minLocy))*preScale) + padding/2;
//	            	
//	            	String nodeName = nodeData.getPointerName(count);
//	            	NodeDescriptor desc = new NodeDescriptor();
//	            	desc.setName(nodeName);
//	            	desc.setGroup(nodeData.getGroup(count));
//	            	FigCCSNode rect = new FigCCSNode(locx, locy, 7, 7, desc);
//	            	
//	            	rect.setLineColor(nodeData.getColor(count));
//	
//	            	rect.setLocked(true);
//	
////	            	if(cmp == null)
////	            		cmp = editor.getLayerManager().getActiveLayer();
////	            	else
////	            	{
////	            		if(cmp != editor.getLayerManager().getActiveLayer()){
////	            			System.out.println(cmp);
////	            			System.out.println(editor.getLayerManager().getActiveLayer());
////	            			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
////	            			try{ Thread.sleep(3000); }catch(Exception e){}
////	            			//break;
////	            		}
////	            	}
//	            	editor.add(rect);
//	            	nodeHash.put(nodeName, rect);
//	            	cur_work++;
//	            	if(cur_work%1000 == 0){
//	            		UiGlobals.setStatusbarText(" Node Rendering... "+cur_work+"/"+max_work);//System.out.println("cur_work! : "+cur_work);
//	            		
//	            		editor.getLayerManager().setPaintActiveOnly(false);
//	            		
//	            		editor.getLayerManager().setPaintActiveOnly(true);
//	            		
//	            	}
//	            	if(cur_work%100 == 0 ){ 
//	            		
//	            		try{
//	            			Thread.sleep(5);
//	            		}catch(Exception e){}
//	            		changeProgress();
//	            	}
//	                if(!progressFlag) break;
//        		}catch(OutOfMemoryError e){
//        			e.printStackTrace();
//        			JOptionPane.showMessageDialog(UiGlobals.getApplet(),
//        				    "Node loading is fail because your java heap space too small to handle CCS. If you want to increase java heap space, please reference guide in CCS website.",
//        				    "Node loading error",
//        				    JOptionPane.ERROR_MESSAGE);
//        		}
//            }
        	editor.getLayerManager().setPaintActiveOnly(false);
        	//editor.damageAll();
        	
        	
        	java.util.List<Fig> nodes = editor.getLayerManager().getContents();
        	System.out.println("inserted : "+inserted+", "+nodes.size());
        	//for(int i = 0 ;i < nodes.size(); i++){
        	//	System.out.println((i+1)+", "+nodes.get(i).getOwner());
        	//}
        	
            return null;
        }
        
        public int cvtLoc(int loc){
        	return (int)((loc)*scale);// + padding/2;
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
            
            
            
            
            UiGlobals.setNodeHash(nodeHash);
            
            JPanel mainPanel = UiGlobals.getMainPane();
            mainPanel.remove(loadingPanel);
            mainPanel.add(UiGlobals.getGraphPane(), BorderLayout.CENTER);
            
        }
    }
}