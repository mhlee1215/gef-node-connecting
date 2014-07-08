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

package ac.kaist.ccs.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.ModeCreateFigCircle;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.base.ModeCreateFigPoly;
import org.tigris.gef.base.ModeCreateFigRRect;
import org.tigris.gef.base.ModeCreateFigRect;
import org.tigris.gef.base.ModeCreateFigText;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.ui.ToolBar;

import ac.kaist.ccs.base.CmdZoom;
import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.ui.NodePaletteFig.ComboItem;

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
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        
        //add(new CmdSetMode(ModeCreateFigLine.class, "Line"));
        //add(new CmdSetMode(ModeCreateFigText.class, "Text"));
        
        //add(image1, "Image1", "Image1");
        add(new CmdZoom(1.2), "", "zoomIn", ToolBar.BUTTON_TYPE_TEXT);
        add(new CmdZoom(1/1.2), "", "zoomOut", ToolBar.BUTTON_TYPE_TEXT);
        //this.addSeparator();
        //add(new CmdGridChart(), "Geneset size distribution", "siGraph", ToolBar.BUTTON_TYPE_TEXT);
        
        //if(!UiGlobals.isUseTargetConversion())
        //	add(new CmdShowFuncAssociate(), "FuncAssociatie", "funcAssociate", ToolBar.BUTTON_TYPE_TEXT);
        //add(new CmdShowAbout(), "Show About", "about1", ToolBar.BUTTON_TYPE_NO_TEXT);
        
        JLabel viewTypeLabel = new JLabel("View Type :");
        add(viewTypeLabel);
        
        String[] viewTypeStrings = {"CO2 Amount", "Cost"}; 
        JComboBox viewTypeCombo = new JComboBox(viewTypeStrings);
        //viewTypeCombo.setPreferredSize(new Dimension(200, 30));
        viewTypeCombo.setName("ViewTypeCombo");
        add(viewTypeCombo);
        viewTypeCombo.addActionListener(this);
        
        JLabel connectTypeLabel = new JLabel("Connect Type :");
        add(connectTypeLabel);
        
        Vector<ComboItem> connectComboItemList = new Vector<ComboItem>();
        connectComboItemList.add(new ComboItem("1. Star", CCSStatics.CONNECT_TYPE_STAR));
        connectComboItemList.add(new ComboItem("2. Tree", CCSStatics.CONNECT_TYPE_TREE));
        connectComboItemList.add(new ComboItem("3. Backbone", CCSStatics.CONNECT_TYPE_BACKBONE));
        connectComboItemList.add(new ComboItem("4. Hybrid", CCSStatics.CONNECT_TYPE_HYBRID));
        
        JComboBox<ComboItem> connectTypeCombo = new JComboBox<ComboItem>(connectComboItemList);
     
        connectTypeCombo.setName("ConnectTypeCombo");
        add(connectTypeCombo);
        connectTypeCombo.addActionListener(this);
        
        JLabel costTypeLabel = new JLabel("Cost Type :");
        add(costTypeLabel);
        
        Vector<ComboItem> costComboItemList = new Vector<ComboItem>();
        costComboItemList.add(new ComboItem("1. The Ogden Models", CCSStatics.COST_TYPE_1));
        costComboItemList.add(new ComboItem("2. MIT model", CCSStatics.COST_TYPE_2));
        costComboItemList.add(new ComboItem("3. Ecofys Model", CCSStatics.COST_TYPE_3));
        costComboItemList.add(new ComboItem("4. IEA GHG PH4/6", CCSStatics.COST_TYPE_4));
        costComboItemList.add(new ComboItem("5. IEA GHG 2005/2", CCSStatics.COST_TYPE_5));
        costComboItemList.add(new ComboItem("6. IEA GHG 2005/2", CCSStatics.COST_TYPE_6));
        costComboItemList.add(new ComboItem("7. Parker model", CCSStatics.COST_TYPE_7));
         
        JComboBox<ComboItem> costTypeCombo = new JComboBox<ComboItem>(costComboItemList);
     
        costTypeCombo.setName("CostTypeCombo");
        add(costTypeCombo);
        costTypeCombo.addActionListener(this);
		//add(resetButton);
		
		
        //add(searchField);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object s = e.getSource();
		if(s instanceof JButton){
			JButton button = (JButton)s;
			Editor editor = UiGlobals.curEditor();
			
			
		}
		else if(s instanceof JComboBox){
			JComboBox cb = (JComboBox)s;
			if("ViewTypeCombo".equals(cb.getName())){
				System.out.println("Selected view type : "+cb.getSelectedIndex());	
				
				Map<Integer, CCSSourceData> nodesAll = UiGlobals.getNodes();
	            List<CCSSourceData> nodeList = new ArrayList<CCSSourceData>();
	            for(Integer key : nodesAll.keySet()){
	            	if(nodesAll.get(key) != null){
	            		if(cb.getSelectedIndex() == CCSSourceData.VIEW_TYPE_CO2){
	            			nodesAll.get(key).viewType = CCSSourceData.VIEW_TYPE_CO2;
	    				}else if(cb.getSelectedIndex() == CCSSourceData.VIEW_TYPE_COST){
	    					nodesAll.get(key).viewType = CCSSourceData.VIEW_TYPE_COST;
	    				}
	            		 
	            	}
	            }
				
	            UiGlobals.graph.getEditor().damageAll();
			}else if("ConnectTypeCombo".equals(cb.getName())){
				ComboItem selectedItem = (ComboItem) cb.getSelectedItem();
				System.out.println("selectedItem: "+selectedItem);
			}else if("CostTypeCombo".equals(cb.getName())){
				ComboItem selectedItem = (ComboItem) cb.getSelectedItem();
				System.out.println("selectedItem: "+selectedItem);
			}
			
			
		    
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertychangeevent) {
		// TODO Auto-generated method stub
		
	}
	
	public class ComboItem {
	    private int value;
	    private String label;

	    public ComboItem(String label, int value) {
	        this.value = value;
	        this.label = label;
	    }

	    public int getValue() {
	        return this.value;
	    }

	    public String getLabel() {
	        return this.label;
	    }

	    @Override
	    public String toString() {
	        return label;
	    }
	}
} /* end class PaletteFig */
