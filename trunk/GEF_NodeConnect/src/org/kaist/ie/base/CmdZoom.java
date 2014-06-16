// File: CmdZoom.java
// Classes: CmdZoom
// Original Author: lawley@dstc.edu.au
// $Id: CmdZoom.java 1153 2008-11-30 16:14:45Z bobtarling $

package org.kaist.ie.base;

import java.util.Formatter;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.kaist.ie.base.UiGlobals;
import org.kaist.ie.ui.LoadingProgressBarNode;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.util.Localizer;

/**
 * Zoom the view. Needs-More-Work:
 * 
 * 
 */

public class CmdZoom extends Cmd {
    private static final long serialVersionUID = 8472508088519383941L;
    protected double _magnitude;
    Logger logger = Logger.getLogger(CmdZoom.class);
    // //////////////////////////////////////////////////////////////
    // constructor

    /** Default behaviour is to restore scaling to 1.0 (1 to 1) */
    public CmdZoom() {
        this(0);
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
    public CmdZoom(double magnitude) {
        super(wordFor(magnitude));
        _magnitude = magnitude;
    }

    /** Convert the zoom magnitude to an English description. */
    protected static String wordFor(double magnitude) {
        if (magnitude < 0) {
            throw new IllegalArgumentException(
                    "Zoom magnitude cannot be less than 0");
        } else if (magnitude == 0.0)
            return Localizer.localize("GefBase", "ZoomReset");
        else if (magnitude > 1.0)
            return Localizer.localize("GefBase", "ZoomIn");
        else if (magnitude < 1.0)
            return Localizer.localize("GefBase", "ZoomOut");
        else
            return Localizer.localize("GefBase", "DoNothing"); // Not a very
                                                                // useful option
    }

    /** Adjust the scale factor of the current editor. */
    public void doIt() {
    	
        Editor ed = (Editor) Globals.curEditor();
        if (ed == null)
            return;
        logger.debug("ed.getScale() : "+ed.getScale()+", mag : "+_magnitude);
        
        double nextScale = ed.getScale() * _magnitude;
        logger.debug("before format : "+nextScale);
        int precise = 1;
        if(nextScale < 0.2)
        	precise = 2;
        logger.debug("format : "+String.format("%."+precise+"f", nextScale));
        nextScale = Double.parseDouble(String.format("%."+precise+"f", nextScale));
        logger.debug("after format : "+nextScale);
        if (_magnitude > 0.0) {
            ed.setScale(nextScale);
        } else {
            ed.setScale(1.0);
        }
        
        double scale = 1.0;
        if((ed.getScale()) < 1.0)
        	scale = 1.0/(ed.getScale());
        	
        
        Editor editor = UiGlobals.curEditor();
        LayerGrid grid = (LayerGrid)editor.getLayerManager().findLayerNamed("Grid");
        HashMap map = new HashMap();
        
        map.put("thick", (int)scale);
        grid.adjust(map);
        
        UiGlobals.setStatusbarText(" scale : x "+ed.getScale());
        
        ed.damageAll();
    }

    /**
     * Undo the zoom. Does not yet work for magnitude of 0 (a reset), and is
     * subject to skew due to precision errors since for floats we cannot assume
     * <code>(x * f / f) == x</code>
     */
    public void undoIt() {
        Editor ed = (Editor) Globals.curEditor();
        if (ed == null)
            return;

        if (_magnitude > 0.0) {
            ed.setScale(ed.getScale() / _magnitude);
        } else {
            System.out.println("Cannot undo CmdZoom reset, yet.");
        }
    }
} /* end class CmdZoom */