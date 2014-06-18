package org.kaist.ie.ui;

import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.presentation.JGraph;

import ac.kaist.ccs.base.UiGlobals;

public class NodeRenderManager {

	private JGraph graph = null;
	private int width = 0;
	private int height = 0;
	public static final int _PADDING = 50;
	
	public NodeRenderManager(JGraph graph)
	{
		this.graph = graph;
	}
	
	public void init(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void drawNodes(boolean removeExistedNodes){
		drawNodes(removeExistedNodes, true);
	}
	
	public void drawNodes(boolean removeExistedNodes, boolean readAnnotation)
	{
		Editor editor = graph.getEditor();
		
		if(removeExistedNodes)
			editor.getLayerManager().getActiveLayer().removeAll();
		
		//Node load
		//스케일이 바뀔때마다 로드해야 함.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// createAndShowGUI();
				new LoadingProgressBarNode(graph);
			}
		});
		
		
	}
}
