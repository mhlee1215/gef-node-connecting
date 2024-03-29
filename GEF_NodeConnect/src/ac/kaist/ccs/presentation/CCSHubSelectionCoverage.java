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
import java.util.List;

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
import org.jfree.chart.axis.NumberTickUnit;
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
public class CCSHubSelectionCoverage extends JFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
	String title;
    public CCSHubSelectionCoverage(final String title, List<Double> coverSourceNum) {

        super(title);
        this.title = title;
        final XYDataset dataset = createDataset(coverSourceNum);
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
    private XYDataset createDataset(List<Double> coverSourceNum) {
        
    	List<Integer> costTypeList = CCSStatics.getCostTypeList();
    	final XYSeriesCollection dataset = new XYSeriesCollection();
    	for(Integer costType : costTypeList){
    		final XYSeries series = new XYSeries(CCSStatics.getCostTypeString(costType));
    		List<Double> dataPerEachCostType = coverSourceNum;
    		
    		double x = 1.0;
    		for(Double dataSingle : dataPerEachCostType){
    			series.add(x, dataSingle);
    			x+=1.0;
    		}
    		
    		dataset.addSeries(series);
    		
    	}
        
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
            "Percent of CO2 emission source node coverage (%)",               // y axis label
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
        //final NumberAxis xAxis = (NumberAxis) plot.getRangeAxis();
        //xAxis.setTickUnit(new NumberTickUnit(10));
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
        rangeAxis.setTickUnit(new NumberTickUnit(10));
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

//        final CCSHubSelectionCoverage demo = new CCSHubSelectionCoverage("Line Chart Demo 6");
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);

    }

}