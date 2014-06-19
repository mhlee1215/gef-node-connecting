// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.kaist.ie.presentation;

import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.RefineryUtilities;
import org.kaist.ccs.ui.GridPaletteFig;
import org.tigris.gef.ui.*;

/**
 * A window that displays a toolbar, a connected graph editing pane, and a
 * status bar.
 */

public class JGridTabbedFrame extends JFrame implements Cloneable{
	
	Vector<double[]> datas = new Vector<double[]>();
	        
	int precise = 10;
	int beanCurValue = 10;
	JPanel _histogram;

    private static final long serialVersionUID = -8167010467922210977L;


    //private JTabbedPaneWithCloseIcons  _mainPanel = new JTabbedPaneWithCloseIcons();
    private JTabbedPane  tabbedPane = new JTabbedPane();
    
	public JGridTabbedFrame(String title) {
		super(title);
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
    }

    public JGridTabbedFrame(String title, double[] data) {
        this(title);
        //this.data = data;
        datas.add(data);
    }
    
    public void addPanel(JGridPanel panel, String title)
    {
    	tabbedPane.addTab(title, panel);
    	
    }
    
    public int getPrecise() {
		return precise;
	}

	public void setPrecise(int precise) {
		this.precise = precise;
	}


    // //////////////////////////////////////////////////////////////
    // Cloneable implementation

    public Object clone() {
        return null; // needs-more-work
    }

    public static void main(String[] argv)
    {
    	double data[] = {1, 2, 3, 2, 1, 4, 100, 50, 1, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	double data1[] = {3, 3, 3, 1, 1, 2, 2, 3, 3, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	JGridChartPanel char1 = new JGridChartPanel("title", 500, 500);
    	
    	char1.addData(data, "data1");
    	char1.addData(data1, "data2");
    	JGridHistogramPanel panel1 = new JGridHistogramPanel("title", data);
    	JGridHistogramPanel panel2 = new JGridHistogramPanel("title", data1);
    	JGridTabbedFrame frame = new JGridTabbedFrame("frame");
    	char1.drawChart();
    	panel1.drawHistogram();
    	panel2.drawHistogram();
    	
    	frame.addPanel(char1, "chart1");
    	frame.addPanel(panel1, "panel1");
    	frame.addPanel(panel2, "panel2");
       // _jgf.drawHistogram();
    	frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        
    }


} /* end class JGraphFrame */
