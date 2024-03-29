package ac.kaist.ccs.presentation;
/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------
 * LineChartDemo6.java
 * -------------------
 * (C) Copyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LineChartDemo6.java,v 1.5 2004/04/26 19:11:55 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2004 : Version 1 (DG);
 * 
 */



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;




import ac.kaist.ccs.domain.CCSStatics;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class CCSHubSelectionCost extends JFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
	String title;
	double minVal, maxVal;
    public CCSHubSelectionCost(final String title, Map<Integer, List<Double>> data) {

        super(title);
        this.title = title;
        final XYDataset dataset = createDataset(data);
        final JFreeChart chart = createChart(dataset);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
		JButton buttonExport = new JButton("Export");
		buttonPanel.add("East", buttonExport);
		buttonExport.addActionListener(new ActionListener(){
			ChartPanel chartPanel;
			
			public ActionListener init(ChartPanel chartPanel){
				this.chartPanel = chartPanel;
				return this;
			}
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Dimension size = chartPanel.getSize();
				
				try {
					//String outPath = textFieldSelectPath.getText();
					//String filename = "chromatography.png";
					//String path = outPath+"/"+filename;
					JFileChooser fileChooser = new JFileChooser();
	            	
	                fileChooser.setCurrentDirectory(new File("/Users/mac/Desktop"));
	                
	                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG","jpeg"));
	                
	                
	                int returnVal = fileChooser.showDialog(new JFrame(), "Open File Path");
	                
	                if(returnVal == JFileChooser.APPROVE_OPTION) {
	                    String inputPath = fileChooser.getSelectedFile().getAbsolutePath();
	                    if(!inputPath.endsWith(".jpeg"))
	                    	inputPath = inputPath + ".jpeg";

	                    OutputStream os = new FileOutputStream(inputPath);
						System.out.println(inputPath+"///"+size.width + " " + size.height);
						BufferedImage chartImage = chartPanel.getChart().createBufferedImage( size.width, size.height, null);
						ImageIO.write( chartImage, "png", os );
						os.close();
						JOptionPane.showMessageDialog(null, "Chart image was saved in "+inputPath);
	                   
	                }
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
		}.init(chartPanel));
        
        
		panel.add("Center", chartPanel);
        panel.add("South", buttonPanel);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 500));
        setContentPane(panel);

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(Map<Integer, List<Double>> dataAll) {
        minVal = 99999999999.0;
        maxVal = 0;
        
    	List<Integer> costTypeList = CCSStatics.getCostTypeList();
    	final XYSeriesCollection dataset = new XYSeriesCollection();
    	for(Integer costType : costTypeList){
    		final XYSeries series = new XYSeries(CCSStatics.getCostTypeString(costType));
    		List<Double> dataPerEachCostType = dataAll.get(costType);
    		
    		double x = 1.0;
    		for(Double dataSingle : dataPerEachCostType){
    			series.add(x, dataSingle);
    			x+=1.0;
    			
    			if(dataSingle < minVal) minVal = dataSingle;
    			if(dataSingle > maxVal) maxVal = dataSingle;
    		}
    		
    		dataset.addSeries(series);
    		
    	}
    	
//        final XYSeries series1 = new XYSeries("First");
//        series1.add(1.0, 1.0);
//        series1.add(2.0, 4.0);
//        series1.add(3.0, 3.0);
//        series1.add(4.0, 5.0);
//        series1.add(5.0, 5.0);
//        series1.add(6.0, 7.0);
//        series1.add(7.0, 7.0);
//        series1.add(8.0, 8.0);
//
//        final XYSeries series2 = new XYSeries("Second");
//        series2.add(1.0, 5.0);
//        series2.add(2.0, 7.0);
//        series2.add(3.0, 6.0);
//        series2.add(4.0, 8.0);
//        series2.add(5.0, 4.0);
//        series2.add(6.0, 4.0);
//        series2.add(7.0, 2.0);
//        series2.add(8.0, 1.0);
//
//        final XYSeries series3 = new XYSeries("Third");
//        series3.add(3.0, 4.0);
//        series3.add(4.0, 3.0);
//        series3.add(5.0, 2.0);
//        series3.add(6.0, 3.0);
//        series3.add(7.0, 6.0);
//        series3.add(8.0, 3.0);
//        series3.add(9.0, 4.0);
//        series3.add(10.0, 3.0);

       
//        dataset.addSeries(series1);
//        dataset.addSeries(series2);
//        dataset.addSeries(series3);
        
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            "Number of Hubs",                      // x axis label
            "Cost for pipeline transportation \nfrom CO2 emission source node to hubs",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setRange(minVal-(maxVal-minVal)*0.05, maxVal+(maxVal-minVal)*0.05);
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

//        final CCSHubSelectionCost demo = new CCSHubSelectionCost("Line Chart Demo 6");
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);

    }

}