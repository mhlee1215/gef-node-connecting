package org.kaist.ie.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;

import org.jdesktop.swingx.JXPanel;
import org.jfree.chart.JFreeChart;
import org.kaist.ccs.ui.GridPaletteFig;
import org.kaist.ccs.ui.TopToolBar;
import org.kaist.ccs.ui.WestToolBar;
import org.tigris.gef.ui.ToolBar;

import ac.kaist.ccs.base.CmdSaveChart;
import ac.kaist.ccs.base.CmdSaveGridData;



public class JGridPanel extends JXPanel{
	JPanel chart;
	WestToolBar toolbar = new WestToolBar();
	protected JLabel statusbar = new JLabel(" ");
	protected JPanel mainPanel = new JPanel(new BorderLayout());
    
    public JGridPanel(){
        setLayout(new BorderLayout());
    	add(mainPanel, BorderLayout.CENTER);
		setUpToolbar();
		setStatusBar();
    }
    public JPanel getChart() {
		return chart;
	}

	public void setUpToolbar()
    {
    	
    }
    
    public void setStatusBar(){
    	mainPanel.add(statusbar, BorderLayout.SOUTH);
    }
    
    public void showStatus(String msg) {
        if (statusbar != null)
            statusbar.setText(msg);
    }
 
}
