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

package org.ssu.ml.presentation;

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
import org.ssu.ml.base.CmdGetNodes;
import org.ssu.ml.base.NodeDescriptor;
import org.ssu.ml.base.UiGlobals;
import org.ssu.ml.ui.CNodeData;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.Editor;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.util.Localizer;

/**
 * Primitive Fig for displaying circles and ovals.
 * @author ics125
 */
public class FigCustomNode extends FigRect implements MouseListener {
	
	Color borderColor = new Color(160, 196, 255);//Color.black;
	
	int selectedCountBySearch = 0;
	float borderSize = 1.5f;
	Stroke borderStroke = new BasicStroke(borderSize);
	boolean increased = false;
	
	/**
	 * @return the selectedCountBySearch
	 */
	public int getSelectedCountBySearch() {
		return selectedCountBySearch;
	}

	/**
	 * @param selectedCountBySearch the selectedCountBySearch to set
	 */
	public void setSelectedCountBySearch(int selectedCountBySearch) {
		this.selectedCountBySearch = selectedCountBySearch;
	}

	public void markByInfoPanel(){
		borderStroke = new BasicStroke(50);
	}
	public void resetbyInfoPanel(){
		borderStroke = new BasicStroke(borderSize);
	}
	public void increaseBorderWidth(){
		borderSize++;
		selectedCountBySearch++;
		//if(increased)
		//	System.out.println(borderSize+", "+this.getOwner());

		borderStroke = new BasicStroke(borderSize);
		increased = true;
	}
	
	public void resetBorderWidth(){
		borderSize = 1;
		selectedCountBySearch = 0;
		borderStroke = new BasicStroke(borderSize);
	}
	
	public void setFoundMark(Color markColor){
		setLineColor(markColor);
		increaseBorderWidth();
		setBorderColor(markColor);
	}
	public void resetFoundMark(){
		setBorderColor(Color.black);	
		resetBorderWidth();
		setLineColor(CNodeData.getDefaultColor());
		
	}
	
    /**
	 * @return the borderStroke
	 */
	public Stroke getBorderStroke() {
		return borderStroke;
	}

	/**
	 * @param borderStroke the borderStroke to set
	 */
	public void setBorderStroke(Stroke borderStroke) {
		this.borderStroke = borderStroke;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public FigCustomNode(int x, int y, int w, int h) {
		super(x, y, w, h);
		// TODO Auto-generated constructor stub
	}
    
    public FigCustomNode(int x, int y, int w, int h, Object owner) {
		super(x, y, w, h);
		setOwner(owner);
		// TODO Auto-generated constructor stub
	}
	/**
     * 
     */
    private static final long serialVersionUID = 7376986113799307733L;
    /**
     * Used as a percentage tolerance for making it easier for the user to
     * select a hollow circle with the mouse. Needs-More-Work: This is bad
     * design that needs to be changed. Should use just GRIP_FACTOR.
     */
    
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = new Vector();
        
        Editor editor = UiGlobals.curEditor();
        List<Fig> list = editor.getSelectionManager().getSelectedFigs();
        String nodeStr = "";
        String prefix = "<html><body style=\"background-color: #ffffdd\"><h3><font color=#000000><span >";
        String postfix = "</span></font></h3></body></html>";
     
        int nodeCount = 0;
        
        for(int count = 0 ; count < list.size() ; count++)
        {
        	Fig node = list.get(count);

        	Object desc = node.getOwner();
        	if(desc instanceof NodeDescriptor)
        	{
        		
        		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
        		System.out.println("name : "+nodeDesc.getName()+", "+node.getLocation());
        		if(count == 0)
        			nodeStr = nodeDesc.getName();
        		else if(count < 6)
        			nodeStr += "<br>&nbsp;"+nodeDesc.getName();
        		else {
        			nodeStr += "<br>&nbsp;...<br>&nbsp;...<br>&nbsp;Total "+list.size()+" nodes";
        			break;
        		}
        		nodeCount++;
        	}
        }
        
        
        
        NodeDescriptor desc = (NodeDescriptor)this.getOwner();
        JLabel name = new JLabel(prefix+nodeStr+postfix);
        if(nodeCount > 5)
        	name.setPreferredSize(new Dimension(120, (nodeCount+2)*24));
        else
        	name.setPreferredSize(new Dimension(120, (nodeCount)*24));
        name.setToolTipText(desc.getName());
        name.setOpaque(true);
        name.setBackground(new Color(255, 255, 221));
        name.setFocusable(false);
        
        //name.set
        
        JMenu getMenu = new JMenu(Localizer.localize("PresentationGef",
        "Get selected Node"));
        
        getMenu.add(new CmdGetNodes());

        
        
        
        //popUpActions.addElement(orderMenu);
        popUpActions.addElement(name);
        popUpActions.addElement(new JSeparator());
        popUpActions.addElement(getMenu);
        //popUpActions.addElement(getClustering);

        return popUpActions;
    }
    
    /** Paint this FigRect */
    public void paint(Graphics g) {
    	if(visible){
    	//g.draw
       // drawRect(g, isFilled(), getFillColor(), getLineWidth(), getLineColor(), getX(), getY(), getWidth(),
        //        getHeight(), getDashed(), _dashes, _dashPeriod);
    	
    	Graphics2D g2 = (Graphics2D)g.create();
		g2.setRenderingHint(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
//		int w = getWidth();
//		int h = getHeight();
//		int arc = 4;
//		int padding = 2;
//		
//		
//		BufferedImage shadow = GraphicsUtilities.createCompatibleTranslucentImage(w+padding, h+padding);
//		
//		Graphics2D g3 = shadow.createGraphics();
//	    g3.setColor(Color.white);
//	    g3.fillRoundRect(0, 0, w+padding, h+padding, arc, arc);
//	    g3.dispose();
//	    
//		int shadowSize = 1;
//		Color shadowColor = Color.black;
//		ShadowRenderer renderer = new ShadowRenderer(shadowSize, 0.5f, shadowColor);
//	    shadow = renderer.createShadow(shadow);
//	    
//		//if(this.drawShadow){
//		    g2.drawImage(shadow, getX()-padding, getY()-padding, getWidth()+padding, getHeight()+padding, null);
//		//}
		
		
    	Color old = g2.getColor();
    	g2.setColor(getLineColor());
    	g2.fillOval(getX(), getY(), getWidth(), getHeight());
    	
    	g2.setColor(borderColor);
    	g2.setStroke(borderStroke);
    	g2.drawOval(getX(), getY(), getWidth(), getHeight());
    	
    	g2.setColor(old);
    	}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
    
} /* end class FigCircle */