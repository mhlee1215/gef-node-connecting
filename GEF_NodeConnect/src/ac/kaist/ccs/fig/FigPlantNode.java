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
import ac.kaist.ccs.domain.CCSPlantData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.ui.CNodeData;

/**
 * Primitive Fig for displaying circles and ovals.
 * @author ics125
 */
public class FigPlantNode extends FigCCSNode {
	
	//Color borderColor = new Color(209, 41, 255);//Color.black;
	
	public FigPlantNode(int x, int y, int w, int h) {
		super(x, y, w, h);
		borderColor = new Color(209, 41, 255);
		coreColor = new Color(Math.max(borderColor.getRed()-borderColorDiff, 0), Math.max(borderColor.getGreen()-borderColorDiff, 0), Math.max(borderColor.getBlue()-borderColorDiff, 0));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
		if(visible){
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			
			Color old = g2.getColor();
			//g2.setComposite(makeComposite(0.3f));
	    	g2.setColor(borderColor);
	    	g2.setStroke(borderStroke);
	    	//g2.fillOval(getX()-hubRange+getWidth()/2, getY()-hubRange+getHeight()/2, hubRange*2, hubRange*2);
	    	int[] xPoints = {getX(), (int) (getX()+getWidth()*Math.sqrt(3)/2), (int) (getX()-getWidth()*Math.sqrt(3)/2)};//{getX()+getWidth()/2, getX(), getX() + getWidth()};
	    	int[] yPoints = {getY()-getWidth(), getY()+getWidth()/2, getY()+getWidth()/2};
	    	g2.fillPolygon(xPoints, yPoints, 3);
	    	g2.setColor(old);
	    	
	    	if((int)this.getOwner() > 0){
	    		Font oldFont = g2.getFont();
	    		
	    		g2.setFont(new Font("TimesRoman", Font.PLAIN, 5)); 
	    		CCSPlantData owner = (CCSPlantData) UiGlobals.getNode((int)getOwner());
	    		Color fontColor = new Color(0, 0, 0);
	    		g2.setColor(fontColor);
	    		
	    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);

	    		String propertyStr = owner.getNameString()+","+owner.getGeo_infoString();
	    		//String propertyStr = Integer.toString(owner.getIndustry_type())+","+CCSStatics.terrainTypeStringMap.get(owner.getTerrain_type());
	    		
		        g2.drawString(propertyStr,getX()+4,getY()-4);
		        
		        g2.setColor(old);
		        g2.setFont(oldFont);
	    	}
		}
		
		//super.paint(g);
		
		
	}
	
	@Override
	public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = new Vector();
        
        Editor editor = UiGlobals.curEditor();
        //List<Fig> list = editor.getSelectionManager().getSelectedFigs();
        String nodeStr = "";
        String prefix = "<html><body style=\"background-color: #ffffdd\"><h3><font color=#000000><span >";
        String postfix = "</span></font></h3></body></html>";
        
        CCSPlantData sourceData = (CCSPlantData) UiGlobals.getNode((int)this.getOwner());
     
        int nodeCount = 5;
        System.out.println("sourceData: "+sourceData);
        //nodeStr = "COST: "+Float.toString(sourceData.getCost());
        
        
        
        nodeStr = "COST: "+Double.toString(sourceData.getCost());
        nodeStr += "<br>StorageCapacity(Kco2): "+Integer.toString(sourceData.getCapacity());
        nodeStr += "<br>ACC CO2: "+Float.toString(sourceData.getAcc_co2_amount());
        nodeStr += "<br>Industry Type: "+sourceData.getIndustry_typeString();
        nodeStr += "<br>Terrain Type: "+sourceData.getTerrain_typeString();
        nodeStr += "<br>Geological Info.: "+sourceData.getGeo_infoString();
        // += "<br>Terrain Type: "+sourceData.getTerrain_typeString();
        
        JLabel name = new JLabel(prefix+nodeStr+postfix);
        if(nodeCount > 5)
        	name.setPreferredSize(new Dimension(300, (nodeCount+2)*24));
        else
        	name.setPreferredSize(new Dimension(300, (nodeCount)*24));
        name.setToolTipText("The source properties are displayed.");
        name.setOpaque(true);
        name.setBackground(new Color(255, 255, 221));
        name.setFocusable(false);
        
        popUpActions.addElement(name);
        
        return popUpActions;
    }

	
} /* end class FigCircle */