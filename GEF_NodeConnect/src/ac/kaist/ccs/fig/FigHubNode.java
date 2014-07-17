// $Id: FigCircle.java 1218 2009-01-12 22:16:24Z bobtarling $
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package ac.kaist.ccs.fig;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.Editor;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.util.Localizer;

import ac.kaist.ccs.base.CmdGetNodes;
import ac.kaist.ccs.base.NodeDescriptor;
import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSHubData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.ui.CNodeData;

/**
 * Primitive Fig for displaying circles and ovals.
 * @author ics125
 */
public class FigHubNode extends FigCCSNode {
	
	int hubRange;
	
	//Color borderColor = new Color(255, 176, 41);//Color.black;
	
	public FigHubNode(int x, int y, int w, int h, int range) {
		super(x, y, w, h);
		this.hubRange = range;
		this.borderColor = new Color(255, 176, 41);
		coreColor = new Color(Math.max(borderColor.getRed()-borderColorDiff, 0), Math.max(borderColor.getGreen()-borderColorDiff, 0), Math.max(borderColor.getBlue()-borderColorDiff, 0));
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FigHubNode [hubRange=" + hubRange + ", borderColor="
				+ borderColor + "]";
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
		
		if(visible){
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			
			CCSHubData sData = (CCSHubData) UiGlobals.getNode((int)this.getOwner());
	    	double magnitude = 0.0f;
	    	if(sData.viewType == CCSSourceData.VIEW_TYPE_CO2)
	    		magnitude = sData.getCo2_amount();
	    	else if(sData.viewType == CCSSourceData.VIEW_TYPE_COST)
	    		magnitude = sData.getCost();
	    	else if(sData.viewType == CCSSourceData.VIEW_TYPE_ACC_CO2)
	    		magnitude = sData.getAcc_co2_amount();
			
			_w = CCSStatics.getScaledSize(magnitude, sData.viewType);
	    	_h = CCSStatics.getScaledSize(magnitude, sData.viewType);
			
			Color old = g2.getColor();
			g2.setComposite(makeComposite(0.1f));
	    	g2.setColor(borderColor);
	    	g2.setStroke(borderStroke);
	    	g2.fillOval(getX()-hubRange, getY()-hubRange, hubRange*2, hubRange*2);
	    	
	    	g2.setColor(old);
		}
		
		super.paint(g);
	}
	
	private AlphaComposite makeComposite(float alpha) {
		  int type = AlphaComposite.SRC_OVER;
		  return(AlphaComposite.getInstance(type, alpha));
		 }

	public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = new Vector();
        
        Editor editor = UiGlobals.curEditor();
        //List<Fig> list = editor.getSelectionManager().getSelectedFigs();
        String nodeStr = "";
        String prefix = "<html><body style=\"background-color: #ffffdd\"><h3><font color=#000000><span >";
        String postfix = "</span></font></h3></body></html>";
        
        CCSSourceData sourceData = (CCSSourceData) UiGlobals.getNode((int)this.getOwner());
     
        int nodeCount = 5;
        
        nodeStr = "COST: "+Double.toString(sourceData.getCost());
        nodeStr += "<br>CO2: "+Float.toString(sourceData.getCo2_amount());
        nodeStr += "<br>ACC CO2: "+Float.toString(sourceData.getAcc_co2_amount());
        nodeStr += "<br>Industry Type: "+sourceData.getIndustry_typeString();
        nodeStr += "<br>Terrain Type: "+sourceData.getTerrain_typeString();
        
//        for(int count = 0 ; count < list.size() ; count++)
//        {
//        	Fig node = list.get(count);
//
//        	Object desc = node.getOwner();
//        	if(desc instanceof NodeDescriptor)
//        	{
//        		
//        		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
//        		System.out.println("name : "+nodeDesc.getName()+", "+node.getLocation());
//        		if(count == 0)
//        			nodeStr = nodeDesc.getName();
//        		else if(count < 6)
//        			nodeStr += "<br>&nbsp;"+nodeDesc.getName();
//        		else {
//        			nodeStr += "<br>&nbsp;...<br>&nbsp;...<br>&nbsp;Total "+list.size()+" nodes";
//        			break;
//        		}
//        		nodeCount++;
//        	}
//        }
        
        
        
        //NodeDescriptor desc = (NodeDescriptor)this.getOwner();
        JLabel name = new JLabel(prefix+nodeStr+postfix);
        if(nodeCount > 5)
        	name.setPreferredSize(new Dimension(200, (nodeCount+2)*24));
        else
        	name.setPreferredSize(new Dimension(200, (nodeCount)*24));
        name.setToolTipText("The source properties are displayed.");
        name.setOpaque(true);
        name.setBackground(new Color(255, 255, 221));
        name.setFocusable(false);
        
        //name.set
        
//        JMenu getMenu = new JMenu(Localizer.localize("PresentationGef",
//        "Get selected Node"));
//        
//        getMenu.add(new CmdGetNodes());

        
        
        
        //popUpActions.addElement(orderMenu);
        popUpActions.addElement(name);
        
        //popUpActions.addElement(new JSeparator());
        //popUpActions.addElement(getMenu);
        //popUpActions.addElement(getClustering);

        return popUpActions;
    }
	
	
	
	
    
    
    
} /* end class FigCircle */