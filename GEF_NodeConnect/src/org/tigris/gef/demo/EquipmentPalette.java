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




// File: EquipmentPalette.java
// Classes: EquipmentPalette
// Original Author: jrobbins@ics.uci.edu
// $Id: EquipmentPalette.java,v 1.1.1.1 2000/09/04 12:38:52 1sturm Exp $

package org.tigris.gef.demo;

import java.awt.*;
import java.util.*;

import org.tigris.gef.base.*;

/** A class to define the left hand column of buttons in the Example
 *  application. Right now it just has one kind of node.
 *
 * @see uci.gef.demo.FlexibleApplet */

public class EquipmentPalette extends org.tigris.gef.ui.ToolBar {

  /** Construct a new palette of example nodes for the Example application */
  public EquipmentPalette() { defineButtons(); }


  /** Define a button to make for the Example application */
  public void  defineButtons() {
    Vector v = new Vector();
    add(new CmdCreateNode(NodeCPU.class, "CPU"), "CPU", "NodeOne");
    add(new CmdCreateNode(NodePrinter.class, "Printer"), "Printer", "NodeOne");
    add(new CmdCreateNode(NodeWall.class, "Wall"), "Wall", "NodeOne");
  }
} /* end class EquipmentPalette */
