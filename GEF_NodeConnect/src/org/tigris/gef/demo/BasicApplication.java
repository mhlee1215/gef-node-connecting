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

// File: BasicApplication.java
// Class: BasicApplication
// original author: jrobbins@ics.uci.edu
// $Id: BasicApplication.java,v 1.4 2004/06/07 14:13:07 bobtarling Exp $

package org.tigris.gef.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

import org.tigris.gef.base.*;
import org.tigris.gef.util.*;
import org.tigris.gef.graph.presentation.*;

/** A simple example of the minimum code needed to build an
 *  application using GEF. */

public class BasicApplication {

    protected JGraphFrame _jgf;

    public BasicApplication() {
        // init localizer and resourceloader
        Localizer.addResource("GefBase","org.tigris.gef.base.BaseResourceBundle");
        Localizer.addResource("GefPres","org.tigris.gef.presentation.PresentationResourceBundle");
        Localizer.addLocale(Locale.getDefault());
        Localizer.switchCurrentLocale(Locale.getDefault());
        ResourceLoader.addResourceExtension("gif");
        ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
        _jgf = new JGraphFrame();
        _jgf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
              _jgf.dispose();
            }
            public void windowClosed(WindowEvent event) {
              System.exit(0);
            }
      });

      _jgf.setToolBar(new SamplePalette()); //needs-more-work

      // make the delete key remove elements from the underlying GraphModel
      //_jgf.getGraph().bindKey(new CmdDispose(), KeyEvent.VK_DELETE, 0);
    
      _jgf.setBounds(10, 10, 300, 200);
      _jgf.setVisible(true);
        //LayerManager lm =  _jgf.getGraph().getEditor().getLayerManager();
        //LayerPerspective lay = (LayerPerspective) lm.getActiveLayer();
        //lay.addNodeTypeRegion(SampleNode.class, new Rectangle(10, 10, 200, 200));
        //lay.addNodeTypeRegion(SampleNode2.class, new Rectangle(250, 10, 200, 200));
        DefaultGraphModel dgm = (DefaultGraphModel) _jgf.getGraphModel();
        for (int i = 0; i < 1; i++) {
            SampleNode sn = new SampleNode();
            sn.initialize(null);
            dgm.addNode(sn);
        }
        
    }
    
    public static void main(String args[]) {
        BasicApplication demo = new BasicApplication();
    }
    
} /* end class BasicApplication */

