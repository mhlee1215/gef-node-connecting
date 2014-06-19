// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package ac.kaist.ccs.base;

import java.util.HashMap;
import java.util.List;

import org.jfree.ui.RefineryUtilities;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Localizer;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.presentation.JGridTabbedFrame;


public class CmdGetNodes extends Cmd {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;

    // //////////////////////////////////////////////////////////////
    // constructor

    /** Default behaviour is to restore scaling to 1.0 (1 to 1) */
    public CmdGetNodes() {
        super("Download nodes");
    }

    /**
     * Each time <code>doIt()</code> is invoked, adjust scaling by a factor of
     * <code>magnitude</code>.
     * 
     * @param magnitude
     *                the factor by which to adjust the Editor's scaling. Must
     *                be greater than or equal to zero. If zero, resets the
     *                Editor's scale factor to 1.
     */


    /** Convert the zoom magnitude to an English description. */
   

    /** Adjust the scale factor of the current editor. */
    public void doIt() {
    	
        
        Editor editor = UiGlobals.curEditor();
        List<Fig> list = editor.getSelectionManager().getSelectedFigs();
        String nodeStr = "";
        for(int count = 0 ; count < list.size() ; count++)
        {
        	Fig node = list.get(count);
        	//System.out.println(node.getLocation().x+", "+node.getLocation().y+", "+node.getId());

        	Object desc = node.getOwner();
        	if(desc instanceof NodeDescriptor)
        	{
        		
        		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
        		System.out.println("name : "+nodeDesc.getName()+", "+node.getLocation());
        		if(count == 0)
        			nodeStr = nodeDesc.getName();
        		else if(count < 100)
        			nodeStr += ","+nodeDesc.getName();
        	}
        }
        String[] params = {nodeStr}; 
        CallJSObject jsObject = new CallJSObject("listProcessing", params, UiGlobals.getApplet());
        Thread thread = new Thread(jsObject);
        thread.run();
    }

    /**
     * Undo the zoom. Does not yet work for magnitude of 0 (a reset), and is
     * subject to skew due to precision errors since for floats we cannot assume
     * <code>(x * f / f) == x</code>
     */
    public void undoIt() {
       
    }
} /* end class CmdZoom */