// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package ac.kaist.ccs.base;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jfree.ui.RefineryUtilities;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Localizer;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.presentation.JGridChartPanel;
import ac.kaist.ccs.presentation.JGridHistogramPanel;
import ac.kaist.ccs.presentation.JGridPanel;
import ac.kaist.ccs.presentation.JGridTabbedFrame;


public class CmdSaveGridData extends Cmd implements ComponentListener {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;
    JGridTabbedFrame histoFrame;
    JGridChartPanel gridPanel;
    
    // //////////////////////////////////////////////////////////////
    // constructor

    public CmdSaveGridData(JGridChartPanel gridPanel) {
        super("Get Gene Sets");
        this.gridPanel = gridPanel;
    }

    public void doIt() {
    	
    	if(gridPanel.getChart() == null){
    		System.out.println("please select at least one Graph.");
    		JOptionPane.showMessageDialog(UiGlobals.getDistFrame(), "Please select a graph you want.");
    		return;
    	}
        
        Editor editor = UiGlobals.curEditor();
        LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed("Grid");
        //HashMap map = grid.getParameters();
        
        
        
        int panelIndex = Integer.parseInt(gridPanel.getChart().getName());
        
        CGridState gstate = UiGlobals.getGridStes().get(panelIndex);
        
        int interval_space = gstate.getSpace();
        int xOffset = gstate.getxOffset();
        int yOffset = gstate.getyOffset();
        
        List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
        int drawingSizeX = UiGlobals.getDrawingSizeX();
        int drawingSizeY = UiGlobals.getDrawingSizeY();
        
        System.out.println("panelIndex : "+panelIndex);
        System.out.println("interval_space : "+interval_space);
        System.out.println("xOffset : "+xOffset);
        System.out.println("yOffset : "+yOffset);
        System.out.println("drawingSizeX : "+drawingSizeX);
        System.out.println("drawingSizeY : "+drawingSizeY);
        
        
        xOffset = Math.abs(xOffset);
        yOffset = Math.abs(yOffset);
        
        CGridData gridData = new CGridData(drawingSizeX, drawingSizeY, interval_space);
        for(int count = 0 ; count < nodes.size() ; count++)
        {
        	Fig node = nodes.get(count);
        	String name = "";
        	Object desc = node.getOwner();
        	if(desc instanceof NodeDescriptor)
        	{
        		
        		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
       			name = nodeDesc.getName();
        	}
        	gridData.addData(node.getLocation().x+xOffset, node.getLocation().y+yOffset, name);
        }
        
        Calendar cal = Calendar.getInstance();
        String filename = "geneSet_"+String.format("%04d%02d%02d%02d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        filename += "."+gridPanel.getGeneDataType();
        
        //String result = gridData.generateData();
        String result = "";//gridData.generateDataSquare();
        
        if(gridPanel.getGeneDataType().equals(JGridChartPanel.TYPE_GMX))
            result = gridData.generateDataSquare();
        else if(gridPanel.getGeneDataType().equals(JGridChartPanel.TYPE_GMT))
            result = gridData.generateDataSquareTrans();
        System.out.println("filename : "+filename);
        System.out.println(result);
        
        //Attach Filename on top of reuslt file.
        result = filename+"\n"+result;
        
        byte[] data = result.getBytes();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		System.out.println(data);
		
		
		
		String url = UiGlobals.getApplet().getCodeBase().toString() + "coordinator/writeGridData.jsp";
		//String url = "http://localhost:8080/coordinator/writeImage.jsp";
		HttpClient httpClient = new HttpClient();
		System.out.println("code base to Write : "+url);
		PostMethod postMethod = new PostMethod(url);
		
		//System.out.println("send filename : "+filename);
		postMethod.setRequestEntity(new InputStreamRequestEntity(bis));
		
		try{
			//Execute
			httpClient.executeMethod(postMethod);
			
			System.out.println(postMethod.getResponseBody());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String[] params = {filename}; 
        CallJSObject jsObject = new CallJSObject("callGridDownloader", params, UiGlobals.getApplet());
        Thread thread = new Thread(jsObject);
        thread.run();

//        UiGlobals.getGridStes().add(new CGridState(interval_space, xOffset, yOffset));
//        UiGlobals.getGridDatas().add(result_1);
//        UiGlobals.getGridCategories().add("d"+interval_space+"x"+xOffset+"y"+yOffset);
//        
//        if(result_1.length > 0){
//        	histoFrame = new JGridTabbedFrame("title");
//        	histoFrame.addComponentListener(this);
//        	
//        	JGridChartPanel total = new JGridChartPanel("Total", 800, 600);
//        	for(int count = 0 ; count < UiGlobals.getGridDatas().size() ; count++)
//        	{
//        		total.addData(UiGlobals.getGridDatas().get(count), UiGlobals.getGridCategories().get(count));
//        		
//            	//panel.setPrecise(10);
//    	        
//        	}
//        	total.drawChart();
//        	histoFrame.addPanel(total, "Total Density");
//        	
//        	for(int count = 0 ; count < UiGlobals.getGridDatas().size() ; count++)
//        	{
//        		JGridHistogramPanel panel = new JGridHistogramPanel("Grid Density - "+count, UiGlobals.getGridDatas().get(count));
//            	panel.drawHistogram();
//            	//panel.setPrecise(10);
//    	        histoFrame.addPanel(panel, UiGlobals.getGridCategories().get(count));
//        	}
//        	
//	        
//	        
//	        histoFrame.pack();
//	        
//	        //RefineryUtilities.centerFrameOnScreen(histoFrame);
//	        histoFrame.setVisible(true);
//	        //histoFrame.setUndecorated(true);
//	        //histoFrame.setResizable(false);
//        }
//        else
//        	System.out.println("No Data Error!");
        
    }

    /**
     * Undo the zoom. Does not yet work for magnitude of 0 (a reset), and is
     * subject to skew due to precision errors since for floats we cannot assume
     * <code>(x * f / f) == x</code>
     */
    public void undoIt() {
       
    }

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
} /* end class CmdZoom */