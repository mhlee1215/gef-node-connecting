// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package ac.kaist.ccs.base;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jfree.chart.JFreeChart;
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
import ac.kaist.ccs.ui.Utils;


public class CmdSaveChart extends Cmd {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;
    JGridPanel gridPanel;
    String filename;

    // //////////////////////////////////////////////////////////////
    // constructor

    /** Default behaviour is to restore scaling to 1.0 (1 to 1) */
    public CmdSaveChart(JGridPanel gridPanel, String filename) {
        super("Save chart");
        this.gridPanel = gridPanel;
        this.filename = filename;
    }

    public void doIt() {
    	
        

    	try {
    		//URL path = UiGlobals.getApplet().getCodeBase();
    		//JPanel selectedPanel = gridPanel.getChart();
    		//Component[] c = selectedPanel.getComponents();
    		//System.out.println(c[0]);
    		if(gridPanel.getChart() == null){
    		    JOptionPane.showMessageDialog(UiGlobals.getDistFrame(), "Please select a graph you want.");
    			System.out.println("You must select one Graph to save.");
    		}
    		else{
	    		//JPanel titledPanel = (JPanel)c[0];
	    		//File outputfile = new File("c:/aa.png");
	    		//ImageIO.write(createImage(selectedPanel), "png", outputfile);
				//if(c.length > 0){
					
					//JGridChartPanel gcp  = (JGridChartPanel) c[0];
					
				//}
				saveToFile(gridPanel.getChart(),"/home/mhlee/public/data/"+filename+".jpg");
    		}
    	} catch (Exception e) {
    		System.out.println("Download method can be execute only on the web!");
    		e.printStackTrace();
		} 
        
    }

    public void undoIt() {
       
    }
    
    public void saveToFile(JPanel chart, String aFileName)
			throws Exception {
		saveToFile(chart, aFileName, chart.getWidth(), chart.getHeight());
	}
    
    public void saveToFile(JPanel chart, String aFileName,
			int width, int height)
			throws Exception {
		BufferedImage img = createImage(chart, width, height);
		
		SendImageToJsp(img, aFileName, width, height);
	}
	
    public BufferedImage createImage(JPanel panel) {

        int w = panel.getWidth();
        int h = panel.getHeight();
        
        return createImage(panel, w, h);
    }
    
    public BufferedImage createImage(JPanel panel, int width, int height) {

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }
    
//	protected BufferedImage draw(JPanel chart, int width, int height)
//    {
//        BufferedImage img =
//        new BufferedImage(width , height,
//        BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = img.createGraphics();
//              
//        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
// 
//        g2.dispose();
//        return img;
//    }
	
	public void SendImageToJsp(BufferedImage img, String filename, int width, int height) throws Exception
	{
		
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		
		ImageIO.write(img,"jpeg", bas);
		byte[] data = bas.toByteArray();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		System.out.println(data);
		
		
		
		String url = UiGlobals.getApplet().getCodeBase().toString() + "coordinator/writeImage.jsp";
		//String url = "http://localhost:8080/coordinator/writeImage.jsp";
		HttpClient httpClient = new HttpClient();
		System.out.println("code base to Write : "+url);
		PostMethod postMethod = new PostMethod(url);
		
		System.out.println("send filename : "+filename);
		postMethod.setRequestEntity(new InputStreamRequestEntity(bis));
		
		try{
			//Execute
			httpClient.executeMethod(postMethod);
			
			System.out.println(postMethod.getResponseBody());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String[] params = {}; 
        CallJSObject jsObject = new CallJSObject("callImageDownloader", params, UiGlobals.getApplet());
        Thread thread = new Thread(jsObject);
        thread.run();
		
	}
} /* end class CmdZoom */