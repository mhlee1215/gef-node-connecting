package org.kaist.ccs.ui;

import java.util.List;
import java.util.Map;

import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.presentation.JGraph;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSEdgeData;
import ac.kaist.ccs.domain.CCSNodeData;

public class NodeRenderManager {

	private JGraph graph = null;
	private int width = 0;
	private int height = 0;
	public static final int _PADDING = 50;
	Map<Integer, List<CCSNodeData> > ccsData;
	List<CCSEdgeData> ccsConData;
	
	public NodeRenderManager(Map<Integer, List<CCSNodeData> > ccsData, List<CCSEdgeData> ccsConData, JGraph graph)
	{
		this.ccsData = ccsData;
		this.ccsConData = ccsConData;
		this.graph = graph;
	}
	
	public void init(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
		
	public void drawNodes(boolean removeExistedNodes)
	{
		Editor editor = graph.getEditor();
		
		if(removeExistedNodes)
			editor.getLayerManager().getActiveLayer().removeAll();
		
		//Node load
		//스케일이 바뀔때마다 로드해야 함.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			Map<Integer, List<CCSNodeData> > ccsData;
			List<CCSEdgeData> ccsConData;
			JGraph graph;
			
			public void run() {
				// createAndShowGUI();
				//new LoadingProgressBarNode(graph);
				new LoadingWorker(ccsData, ccsConData, graph, 1);
			}
			
			public Runnable init(Map<Integer, List<CCSNodeData> > ccsData, List<CCSEdgeData> ccsConData, JGraph graph){
				this.ccsData = ccsData;
				this.ccsConData = ccsConData;
				this.graph = graph;
				return this;
			}
		}.init(ccsData,  ccsConData, graph));
		
		
	}
}
