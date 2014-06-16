package org.kaist.ie.ui;

import org.kaist.ie.base.UiGlobals;
import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.presentation.JGraph;

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
		
		//annotation load
		//이미 로드한 경우는 로드하지 않는다.
		if(UiGlobals.getAnnotationHeader().size() == 0){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					// createAndShowGUI();
					new LoadingProgressBarAnnotation(UiGlobals.getAnnotationFileName());
				}
			});
		}
		
	}
}
