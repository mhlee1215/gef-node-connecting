// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package org.kaist.ie.base;

import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;
import org.kaist.ie.base.UiGlobals;
import org.kaist.ie.presentation.JGridChartPanel;
import org.kaist.ie.presentation.JGridHistogramPanel;
import org.kaist.ie.presentation.JGridTabbedFrame;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Localizer;


public class CmdShowRenderType extends Cmd {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;

    // //////////////////////////////////////////////////////////////
    // constructor

    /** Default behaviour is to restore scaling to 1.0 (1 to 1) */
    public CmdShowRenderType() {
        super("Show Grid Histogram");
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
    	
        
        
    }

    /**
     * Undo the zoom. Does not yet work for magnitude of 0 (a reset), and is
     * subject to skew due to precision errors since for floats we cannot assume
     * <code>(x * f / f) == x</code>
     */
    public void undoIt() {
       
    }
} /* end class CmdZoom */