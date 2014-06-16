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

// File: PaletteFig.java
// Classes: PaletteFig
// Original Author: ics125 spring 1996
// $Id: PaletteFig.java 1153 2008-11-30 16:14:45Z bobtarling $

package org.kaist.ie.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.kaist.ie.base.CGridHistogramData;
import org.kaist.ie.base.CmdGridChart;
import org.kaist.ie.base.CmdShowAbout;
import org.kaist.ie.base.CmdShowFuncAssociate;
import org.kaist.ie.base.CmdZoom;
import org.kaist.ie.base.UiGlobals;
import org.kaist.ie.presentation.FigCustomNode;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.ModeCreateFigCircle;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.base.ModeCreateFigPoly;
import org.tigris.gef.base.ModeCreateFigRRect;
import org.tigris.gef.base.ModeCreateFigRect;
import org.tigris.gef.base.ModeCreateFigText;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ToolBar;

/**
 * A Palette that defines buttons to create lines, rectangles, rounded
 * rectangles, circles, and text. Also a select button is provided to switch
 * back to ModeSelect.
 * 
 * Needs-more-work: sticky mode buttons are not supported right now. They should
 * be in the next release.
 * 
 * @see ModeSelect
 * @see ModeCreateFigLine
 * @see ModeCreateFigRect
 * @see ModeCreateFigRRect
 * @see ModeCreateFigCircle
 * @see ModeCreateFigText
 * @see ModeCreateFigPoly
 */

public class NodePaletteFig extends WestToolBar implements ActionListener, PropertyChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 304194274216578087L;
    
    String keyword = "";
	String propertyName = "";

    public NodePaletteFig() {
        defineButtons();
    }

    /**
     * Defined the buttons in this palette. Each of these buttons is associated
     * with an CmdSetMode, and that Cmd sets the next global Mode to somethign
     * appropriate. All the buttons can stick except 'select'. If the user
     * unclicks the sticky checkbox, the 'select' button is automatically
     * pressed.
     */
    public void defineButtons() {
        this.setBackground(Color.black);
        this.setForeground(Color.black);
        
        //add(new CmdSetMode(ModeCreateFigLine.class, "Line"));
        //add(new CmdSetMode(ModeCreateFigText.class, "Text"));
        
        //add(image1, "Image1", "Image1");
        add(new CmdZoom(2), "", "zoomIn", ToolBar.BUTTON_TYPE_TEXT);
        add(new CmdZoom(0.5), "", "zoomOut", ToolBar.BUTTON_TYPE_TEXT);
        //this.addSeparator();
        add(new CmdGridChart(), "Geneset size distribution", "siGraph", ToolBar.BUTTON_TYPE_TEXT);
        
        if(!UiGlobals.isUseTargetConversion())
        	add(new CmdShowFuncAssociate(), "FuncAssociatie", "funcAssociate", ToolBar.BUTTON_TYPE_TEXT);
        //add(new CmdShowAbout(), "Show About", "about1", ToolBar.BUTTON_TYPE_NO_TEXT);
        
        
        
        Vector<String> annotHeader = UiGlobals.getAnnotationHeader();
        
        
        String[] strScaleItems = new String[annotHeader.size()];
        annotHeader.toArray(strScaleItems);
		JComboBox searchCombo = new JComboBox(strScaleItems);
		searchCombo.setPreferredSize(new Dimension(50, 30));
		searchCombo.setEnabled(false);
		searchCombo.addActionListener(this);
		if(UiGlobals.isUseTargetConversion())
			searchCombo.setVisible(false);
		UiGlobals.setPropertySearchCombo(searchCombo);
		add(searchCombo);
		
		JTextField searchField = new JTextField();
        searchField.setEnabled(false);
        searchField.setPreferredSize(new Dimension(300, 30));
        UiGlobals.setPropertySearchField(searchField);
        searchField.addActionListener(this);
        if(UiGlobals.isUseTargetConversion())
        	searchField.setVisible(false);
		add(searchField);
		
		JButton searchButton = new JButton("Search");
		searchButton.setName("Search");
		searchButton.setEnabled(false);
		UiGlobals.setPropertySearchButton(searchButton);
		searchButton.addActionListener(this);
		if(UiGlobals.isUseTargetConversion())
			searchButton.setVisible(false);
		add(searchButton);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setName("Reset");
		resetButton.setEnabled(false);
		UiGlobals.setPropertyResetButton(resetButton);
		resetButton.addActionListener(this);
		if(UiGlobals.isUseTargetConversion())
			resetButton.setVisible(false);
		add(resetButton);
		
		
        //add(searchField);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object s = e.getSource();
		if(s instanceof JButton){
			JButton button = (JButton)s;
			Editor editor = UiGlobals.curEditor();
			if("Search".equals(button.getName())){
				String selectedProperty = (String)UiGlobals.getPropertySearchCombo().getSelectedItem();
				System.out.println("searc start: "+selectedProperty);
				
				
				
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// createAndShowGUI();
						new LoadingProgressBarSearchAndMark(keyword, propertyName);
					}
				});
			}else if("Reset".equals(button.getName())){
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// createAndShowGUI();
						new LoadingProgressBarSearchAndMark(keyword, propertyName, true);
					}
				});
			}
			
		}
		else if(s instanceof JComboBox){
		    JComboBox cb = (JComboBox)s;
		    String scaleName = (String)cb.getSelectedItem();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertychangeevent) {
		// TODO Auto-generated method stub
		
	}
} /* end class PaletteFig */
