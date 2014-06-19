/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package org.kaist.ccs.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.kaist.ccs.fig.FigCCSNode;
import org.tigris.gef.base.Editor;
import org.tigris.gef.demo.SampleNode;
import org.tigris.gef.graph.presentation.DefaultGraphModel;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.base.NodeDescriptor;
import ac.kaist.ccs.base.UiGlobals;

import java.beans.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Random;


public class LoadingProgressPValue extends JPanel {

    private NodeTask task;
    
    class NodeTask extends SwingWorker<Void, Void> {
    	

    	public NodeTask()
    	{
    		
    	}
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            return null;
        }
        public void stop(){
        }
        /*
         * Executed in event dispatch thread
         */
        public void done() {
            
        }
    }

    public LoadingProgressPValue(CNodeData nodeData, JGraph editor) {
    	this(nodeData, null, editor);
    }
    
    public LoadingProgressPValue(CNodeData nodeData, CEdgeData edgeData, JGraph editor) {
        task = new NodeTask();
        task.execute();
    }

    public void propertyChange(PropertyChangeEvent evt) {

    }
    
    public void changeProgress(){
    }
    
    public int readEdgeData(String filename) {
		return 0;
	}
}