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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.ui.CNodeData;

/**
 * Primitive Fig for displaying circles and ovals.
 * @author ics125
 */
public class FigSourceNode extends FigCCSNode {
	
	//Color borderColor = new Color(84, 255, 61);//Color.black;
	
	public FigSourceNode(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		borderColor = new Color(84, 255, 61);
		coreColor = new Color(Math.max(borderColor.getRed()-borderColorDiff, 0), Math.max(borderColor.getGreen()-borderColorDiff, 0), Math.max(borderColor.getBlue()-borderColorDiff, 0));
	}

	@Override
	public String toString() {
		return "FigCO2SourceNode [borderColor=" + borderColor + "]";
	}
	
	public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = new Vector();
        
        Editor editor = UiGlobals.curEditor();
        //List<Fig> list = editor.getSelectionManager().getSelectedFigs();
        String nodeStr = "";
        String prefix = "<html><body style=\"background-color: #ffffdd\"><h3><font color=#000000><span >";
        String postfix = "</span></font></h3></body></html>";
        
        CCSSourceData sourceData = (CCSSourceData) this.getOwner();
     
        int nodeCount = 3;
        
        nodeStr = "CO2: "+Float.toString(sourceData.getCo2_amount());
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
        	name.setPreferredSize(new Dimension(120, (nodeCount+2)*24));
        else
        	name.setPreferredSize(new Dimension(120, (nodeCount)*24));
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
	
	@Override
	public void paint(Graphics g) {
    	if(visible){
	    	//g.draw
	       // drawRect(g, isFilled(), getFillColor(), getLineWidth(), getLineColor(), getX(), getY(), getWidth(),
	        //        getHeight(), getDashed(), _dashes, _dashPeriod);
	    	
	    	Graphics2D g2 = (Graphics2D)g.create();
			g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
					
	    	Color old = g2.getColor();
	    	g2.setColor(coreColor);
	    	
	    	
	    	CCSSourceData sData = (CCSSourceData) this.getOwner();
	    	float magnitude = 0.0f;
	    	if(sData.viewType == CCSSourceData.VIEW_TYPE_CO2)
	    		magnitude = sData.getCo2_amount();
	    	else if(sData.viewType == CCSSourceData.VIEW_TYPE_COST)
	    		magnitude = sData.getCost();
	    	
			if(magnitude < 100){
				_w = 5;
				_h = 5;
			}else if(magnitude < 500){
				_w = 9;
				_h = 9;
			}else if(magnitude < 1000){
				_w = 13;
				_h = 13;
			}else if(magnitude < 3000){
				_w = 15;
				_h = 15;
			}else if(magnitude < 5000){
				_w = 17;
				_h = 17;
			}else if(magnitude < 7000){
				_w = 19;
				_h = 19;
			}else{
				_w = 21;
				_h = 21;
			}
	    	
	    	
	    	g2.fillOval(getX(), getY(), getWidth(), getHeight());
	    	
	    	g2.setColor(borderColor);
	    	g2.setStroke(borderStroke);
	    	g2.drawOval(getX(), getY(), getWidth(), getHeight());
	    	
	    	g2.setColor(old);
	    	
	    	if(this.getOwner() instanceof CCSSourceData){
	    		Font oldFont = g2.getFont();
	    		
	    		g2.setFont(new Font("TimesRoman", Font.PLAIN, 5)); 
	    		CCSSourceData owner = (CCSSourceData) getOwner();
	    		Color fontColor = new Color(0, 0, 0);
	    		g2.setColor(fontColor);
	    		
	    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);

	    		String propertyStr = owner.getIndustry_typeString()+","+owner.getTerrain_typeStringShort();
	    		//String propertyStr = Integer.toString(owner.getIndustry_type())+","+CCSStatics.terrainTypeStringMap.get(owner.getTerrain_type());
	    		
		        g2.drawString(propertyStr,getX()+4,getY()-4);
		        
		        g2.setColor(old);
		        g2.setFont(oldFont);
	    	}
    	}
    }

	
} /* end class FigCircle */