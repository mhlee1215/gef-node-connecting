// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package org.ssu.ml.base;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;
import org.ssu.ml.base.UiGlobals;
import org.ssu.ml.presentation.JGridChartPanel;
import org.ssu.ml.presentation.JGridTabbedFrame;
import org.ssu.ml.presentation.JGridHistogramPanel;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Localizer;


public class CmdGridChart extends Cmd implements ComponentListener {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;
    JFrame histoFrame;
    
    // //////////////////////////////////////////////////////////////
    // constructor

    /** Default behaviour is to restore scaling to 1.0 (1 to 1) */
    public CmdGridChart() {
        super("");
    }

    /**
     * Each time <code>doIt()</code> is invoked, adjust scaling by a factor of
     * <code>magnitude</code>.
     * 
     * @param magnitude
     *                the factor by which to adjust the Editor's scaling. Must
     *                be greater than or equal to zero. If zero, resets the
     *                Editor's scale factor to 1.
     */


    /** Convert the zoom magnitude to an English description. */
   

    /** Adjust the scale factor of the current editor. */
    public void doIt() {
    	
        
        Editor editor = UiGlobals.curEditor();
        LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed("Grid");
        HashMap map = grid.getParameters();
        int interval_space = (Integer)map.get("spacing");
        int xOffset = (Integer)map.get("xOffset");
        int yOffset = (Integer)map.get("yOffset");
        System.out.println("Hi! I'm a GridHistogram Swich!, space : "+interval_space);
        
        List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
        int drawingSizeX = UiGlobals.getDrawingSizeX();
        int drawingSizeY = UiGlobals.getDrawingSizeY();
        
        System.out.println("interval_space : "+interval_space);
        System.out.println("drawingSizeX : "+drawingSizeX);
        System.out.println("drawingSizeY : "+drawingSizeY);
        CGridHistogramData histoData = new CGridHistogramData(drawingSizeX, drawingSizeY, interval_space);
        
        System.out.println("1xOffset : "+xOffset);
        xOffset = Math.abs(xOffset);
        yOffset = Math.abs(yOffset);
        System.out.println("2xOffset : "+xOffset);
        for(int count = 0 ; count < nodes.size() ; count++)
        {
        	Fig node = nodes.get(count);
        	histoData.addData(node.getLocation().x+xOffset, node.getLocation().y+yOffset);
        }
        Double[] result = histoData.generateHistoData();
        double[] result_1 = new double[result.length];
        for(int count = 0 ; count < result.length ; count++)
        	result_1[count] = (double)result[count];
        
        String category = "Cell size: "+interval_space+", x-axis offset: "+xOffset+", y-axis offset: "+yOffset;
        //category = "d"+interval_space+"x"+xOffset+"y"+yOffset;
        if(UiGlobals.getGridCategories().size() == 0 || !UiGlobals.getGridCategories().get(UiGlobals.getGridCategories().size()-1).equals(category)){
	        UiGlobals.getGridStes().add(new CGridState(interval_space, xOffset, yOffset));
	        UiGlobals.getGridDatas().add(result_1);
	        UiGlobals.getGridCategories().add(category);
        }
        
        if(result_1.length > 0){
        	//histoFrame = new JGridTabbedFrame("title");
        	if(UiGlobals.getDistFrame()!=null) UiGlobals.getDistFrame().setVisible(false);
        	histoFrame = new JFrame("Geneset size distribution"); 
        	UiGlobals.setDistFrame(histoFrame);
        	histoFrame.addComponentListener(this);
        	
        	JGridChartPanel total = new JGridChartPanel("Total", 800, 300);
        	for(int count = 0 ; count < UiGlobals.getGridDatas().size() ; count++)
        	{
        		total.addData(UiGlobals.getGridDatas().get(count), UiGlobals.getGridCategories().get(count));
        		
            	//panel.setPrecise(10);
    	        
        	}
        	total.drawChart();
        	histoFrame.add(total);
        	//histoFrame = total;
        	
//        	for(int count = 0 ; count < UiGlobals.getGridDatas().size() ; count++)
//        	{
//        		JGridHistogramPanel panel = new JGridHistogramPanel("Grid Density - "+count, UiGlobals.getGridDatas().get(count));
//            	panel.drawHistogram();
//            	//panel.setPrecise(10);
//    	        histoFrame.addPanel(panel, UiGlobals.getGridCategories().get(count));
//        	}
        	
	        
	        histoFrame.setSize(700, 460);
	        //histoFrame.pack();
	        
	        //RefineryUtilities.centerFrameOnScreen(histoFrame);
	        histoFrame.setVisible(true);
	        //histoFrame.setUndecorated(true);
	        //histoFrame.setResizable(false);
        }
        else
        	System.out.println("No Data Error!");
        
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