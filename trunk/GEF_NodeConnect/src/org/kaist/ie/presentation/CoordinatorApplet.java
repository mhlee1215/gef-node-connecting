package org.kaist.ie.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.kaist.ie.ui.NodePaletteFig;
import org.kaist.ie.ui.NodeRenderManager;
import org.kaist.ie.ui.ResizerPaletteFig;
import org.kaist.ie.ui.WestToolBar;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.event.ModeChangeEvent;
import org.tigris.gef.event.ModeChangeListener;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.util.Localizer;
import org.tigris.gef.util.ResourceLoader;

import ac.kaist.ccs.base.UiGlobals;


public class CoordinatorApplet extends JApplet implements ModeChangeListener {

	public static final int _PADDING = 100;

	int _width = 2000;
	int _height = 2000;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id = null;
	String coordFileName = "";
	String edgeFileName = "";
	int pre_scaled = 1;

	
	
	
	Editor editor = null;

	private WestToolBar _toolbar = null;
	private WestToolBar _westToolbar = null;
	/** The graph pane (shown in middle of window). */
	private JGraph _graph;
	/** A statusbar (shown at bottom ow window). */
	private JLabel _statusbar = new JLabel(" ");
	//private JXPanel jxpanel;
	private JPanel _mainPanel = new JPanel(new BorderLayout());
	private JPanel _graphPanel = new JPanel(new BorderLayout());
	private JMenuBar _menubar = new JMenuBar();

	public CoordinatorApplet() throws Exception {
		
		Localizer.addResource("GefBase",
				"org.tigris.gef.base.BaseResourceBundle");
		Localizer.addResource("GefPres",
				"org.tigris.gef.presentation.PresentationResourceBundle");
		Localizer.addLocale(Locale.getDefault());
		Localizer.switchCurrentLocale(Locale.getDefault());
		ResourceLoader.addResourceExtension("gif");
		ResourceLoader.addResourceExtension("png");
		ResourceLoader.addResourceExtension("jpg");
		ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
		
		UiGlobals.init();

		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
        	e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
			  UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		//UiGlobals.set_curApplet(this);
		UiGlobals.setApplet(this);
		
	}
	
	private void initParam() {
		if (getParameter("prescaled") == null)
			pre_scaled = 1;
		else
			pre_scaled = Integer.parseInt(getParameter("prescaled"));
		
		if (getParameter("isusetargetconversion") == null)
			UiGlobals.setUseTargetConversion(false);
		else{
			if("Y".equals(getParameter("isusetargetconversion")) || "1".equals(getParameter("isusetargetconversion")))
				UiGlobals.setUseTargetConversion(true);
			else
				UiGlobals.setUseTargetConversion(false);
		}
		
		if(this.getParameter("tocolumn") != null)
			UiGlobals.setTargetColumnName(this.getParameter("tocolumn"));
		else
			UiGlobals.setTargetColumnName("");
		
		//UiGlobals.setPre_scaled(pre_scaled);
		UiGlobals.setFileName(this.getParameter("fileName"));
		UiGlobals.setAnnotationFileName(this.getParameter("annotationFileName"));
		
		String isExample = this.getParameter("isExample");
		if(isExample == null) isExample = "N";
		UiGlobals.setIsExample(isExample);
		UiGlobals.setExampleType(this.getParameter("type"));
		
		System.out.println("===PARAMETER INFO===");
		System.out.println("isUseConversion: "+UiGlobals.isUseTargetConversion());
		System.out.println("fileName: "+UiGlobals.getFileName());
		System.out.println("annotationFileName: "+UiGlobals.getAnnotationFileName());
		System.out.println("tocolumn: "+this.getParameter("tocolumn"));
		System.out.println("===PARAMETER INFO END===");
	}

	private void jbInit() throws Exception {
		
		long mega = (long) Math.pow(2, 20);
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();
		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.
		// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		// Get amount of free memory within the heap in bytes. This size will increase
		// after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		System.out.println("===VM INFO=START===");
		System.out.println("heapSize: "+heapSize/mega+"MB");
		System.out.println("heapMaxSize: "+heapMaxSize/mega+"MB");
		System.out.println("heapFreeSize: "+heapFreeSize/mega+"MB");
		System.out.println("===VM INFO=END===");
		
		try{
			System.out.println("this.getCodeBase() : "+this.getCodeBase());
		}catch(Exception e){
			System.out.println("Executed in local!");
		}
		// paths.setCodebase(this.getCodeBase().toString());
		// this.

		
			
		
		
		
		
		
		
		//System.out.println("isExample : "+UiGlobals.getIsExample());
		//System.out.println("exampleType : "+UiGlobals.getExampleType());
		
		//Start to read annotation file
		//initAnnotation(UiGlobals.getAnnotationFileName());
		
		//Start to node rendering
		NodeRenderManager nodeRenderManager = new NodeRenderManager(_graph);
		nodeRenderManager.init(_width, _height);
		nodeRenderManager.drawNodes(true);
		UiGlobals.setNodeRenderManager(nodeRenderManager);
		
		
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	public void init() {
		init(new JGraph());
	}

	public void init(JGraph jg) {
		initParam();
		setDefaultFont(UiGlobals.getNormalFont());
		this.setToolBar(new NodePaletteFig()); // needs-more-work
		this.setWestToolBar(new ResizerPaletteFig());
		
		_graph = jg;

		editor = _graph.getEditor();
		// _graph.setBackground(Color.white);
		// _graph.setBounds(0, 0, _width, _height);

		LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed(
				"Grid");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Color", Color.LIGHT_GRAY);
		map.put("bgColor", Color.white);
		map.put("spacing_include_stamp", (int)UiGlobals.getDefault_grid_spacing()+50);
		map.put("paintLines", true);
		map.put("paintDots", false);

		grid.adjust(map);

		Container content = getContentPane();
		//setUpMenus();
		content.setLayout(new BorderLayout());
		//content.add(_menubar, BorderLayout.NORTH);
		_graphPanel.add(_graph, BorderLayout.CENTER);
		_graphPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		UiGlobals.setGraphPane(_graphPanel);
		//_mainPanel.add(_graphPanel, BorderLayout.CENTER);
		content.add(_mainPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("insets 0 0 0 0"));
		bottomPanel.add(_statusbar, "wrap");
		
		content.add(bottomPanel, BorderLayout.SOUTH);
		UiGlobals.set_statusBar(_statusbar);
		UiGlobals.setCoordBottomPanel(bottomPanel);
		setSize(870, 600);
		setVisible(true);

		_graph.addModeChangeListener(this);
		
		UiGlobals.setMainPane(_mainPanel);

		try {
			jbInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void modeChange(ModeChangeEvent mce) {
		// TODO Auto-generated method stub

	}

	public void setToolBar(WestToolBar tb) {
		_toolbar = tb;
		_mainPanel.add(_toolbar, BorderLayout.NORTH);
	}

	public void setWestToolBar(WestToolBar tb) {
		_westToolbar = tb;
		_mainPanel.add(_westToolbar, BorderLayout.EAST);
	}

	public void start() {
		// TODO Auto-generated method stub
		super.start();
	}

	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
	/**
	 * Set default font
	 * @param font
	 */
	public void setDefaultFont(Font font)
	{
		
			String[] applyList = {
				"RadioButtonMenuItem.font",
				"CheckBoxMenuItem.font",
				"RadioButton.font",
				"ToolBar.font",
				"ProgressBar.font",
				"Menu.font",
				"Button.font",
				"TitledBorder.font",
				"ComboBox.font",
				"ToggleButton.font",
				"TabbedPane.font",
				"List.font",
				"MenuBar.font",
				"MenuItem.font",
				"CheckBox.font",
				"Label.font",
			};
			
			int nSize = applyList.length;
			for( int i=0; i < nSize; i++ ){
				UIManager.put(applyList[i], font);
			}
			
	}

	
	
	

	
	
	

}
