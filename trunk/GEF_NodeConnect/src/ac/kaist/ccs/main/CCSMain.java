package ac.kaist.ccs.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.tigris.gef.base.AlignAction;
import org.tigris.gef.base.CmdAdjustGrid;
import org.tigris.gef.base.CmdAdjustGuide;
import org.tigris.gef.base.CmdAdjustPageBreaks;
import org.tigris.gef.base.CmdCopy;
import org.tigris.gef.base.CmdExit;
import org.tigris.gef.base.CmdGroup;
import org.tigris.gef.base.CmdOpen;
import org.tigris.gef.base.CmdOpenWindow;
import org.tigris.gef.base.CmdPaste;
import org.tigris.gef.base.CmdPrint;
import org.tigris.gef.base.CmdPrintPageSetup;
import org.tigris.gef.base.CmdRemoveFromGraph;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.CmdSave;
import org.tigris.gef.base.CmdSavePGML;
import org.tigris.gef.base.CmdSaveSVG;
import org.tigris.gef.base.CmdSelectAll;
import org.tigris.gef.base.CmdSelectInvert;
import org.tigris.gef.base.CmdSelectNext;
import org.tigris.gef.base.CmdShowProperties;
import org.tigris.gef.base.CmdSpawn;
import org.tigris.gef.base.CmdUngroup;
import org.tigris.gef.base.CmdUseReshape;
import org.tigris.gef.base.CmdUseResize;
import org.tigris.gef.base.CmdUseRotate;
import org.tigris.gef.base.DistributeAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.base.NudgeAction;
import org.tigris.gef.event.ModeChangeEvent;
import org.tigris.gef.event.ModeChangeListener;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.undo.RedoAction;
import org.tigris.gef.undo.UndoAction;
import org.tigris.gef.util.Localizer;
import org.tigris.gef.util.ResourceLoader;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSEdgeData;
import ac.kaist.ccs.domain.CCSHubData;
import ac.kaist.ccs.domain.CCSJointData;
import ac.kaist.ccs.domain.CCSNodeData;
import ac.kaist.ccs.domain.CCSPlantData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.domain.CCSStatics.PlantData;
import ac.kaist.ccs.domain.CCSStatics.StorageData;
import ac.kaist.ccs.ui.NodePaletteFig;
import ac.kaist.ccs.ui.NodeRenderManager;
import ac.kaist.ccs.ui.ResizerPaletteFig;
import ac.kaist.ccs.ui.WestToolBar;

public class CCSMain extends JApplet implements ModeChangeListener {

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

	int padding = 0;
	int scale = pre_scaled;

	Editor editor = null;

	private WestToolBar _toolbar = null;
	private WestToolBar _westToolbar = null;
	/** The graph pane (shown in middle of window). */
	private JGraph _graph;
	/** A statusbar (shown at bottom ow window). */
	private JLabel _statusbar = new JLabel(" ");
	// private JXPanel jxpanel;
	private JPanel _mainPanel = new JPanel(new BorderLayout());
	private JPanel _graphPanel = new JPanel(new BorderLayout());
	private JMenuBar _menubar = new JMenuBar();

	BufferedImage refImage = null;
	BufferedImage refTerrainImage = null;

	public CCSMain() throws Exception {

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
		CCSStatics.init();

		
		try {
			String osName = System.getProperty("os.name").toLowerCase();
			if (!osName.contains("mac")) // if not on mac
			{
				UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			}
			
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// UiGlobals.set_curApplet(this);
		UiGlobals.setApplet(this);

	}

	private void initParam() {
		if (getParameter("prescaled") == null)
			pre_scaled = 1;
		else
			pre_scaled = Integer.parseInt(getParameter("prescaled"));

		if (getParameter("isusetargetconversion") == null)
			UiGlobals.setUseTargetConversion(false);
		else {
			if ("Y".equals(getParameter("isusetargetconversion"))
					|| "1".equals(getParameter("isusetargetconversion")))
				UiGlobals.setUseTargetConversion(true);
			else
				UiGlobals.setUseTargetConversion(false);
		}

		if (this.getParameter("tocolumn") != null)
			UiGlobals.setTargetColumnName(this.getParameter("tocolumn"));
		else
			UiGlobals.setTargetColumnName("");

		// UiGlobals.setPre_scaled(pre_scaled);
		UiGlobals.setFileName(this.getParameter("fileName"));
		UiGlobals
				.setAnnotationFileName(this.getParameter("annotationFileName"));

		String isExample = this.getParameter("isExample");
		if (isExample == null)
			isExample = "N";
		UiGlobals.setIsExample(isExample);
		UiGlobals.setExampleType(this.getParameter("type"));

		System.out.println("===PARAMETER INFO===");
		System.out.println("isUseConversion: "
				+ UiGlobals.isUseTargetConversion());
		System.out.println("fileName: " + UiGlobals.getFileName());
		System.out.println("annotationFileName: "
				+ UiGlobals.getAnnotationFileName());
		System.out.println("tocolumn: " + this.getParameter("tocolumn"));
		System.out.println("===PARAMETER INFO END===");
	}

	private void jbInit() throws Exception {

		UiGlobals.clearAllNode();
		UiGlobals.clearAllEdge();
		
		long mega = (long) Math.pow(2, 20);
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();
		// Get maximum size of heap in bytes. The heap cannot grow beyond this
		// size.
		// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		// Get amount of free memory within the heap in bytes. This size will
		// increase
		// after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		System.out.println("===VM INFO=START===");
		System.out.println("heapSize: " + heapSize / mega + "MB");
		System.out.println("heapMaxSize: " + heapMaxSize / mega + "MB");
		System.out.println("heapFreeSize: " + heapFreeSize / mega + "MB");
		System.out.println("===VM INFO=END===");

		try {
			System.out.println("this.getCodeBase() : " + this.getCodeBase());
		} catch (Exception e) {
			System.out.println("Executed in local!");
		}


		// Insert Loading data
		//UiGlobals.ccsData  = makeRandomData(300, 500, 500);
		UiGlobals.ccsData = makeRefRandomData(refImage, refTerrainImage);
		
		UiGlobals.saveNodeSnapshot();
	
		
		System.out.println("Randering!");
		NodeRenderManager nodeRenderManager = new NodeRenderManager(UiGlobals.ccsData,
				null, _graph);
		nodeRenderManager.init(_width, _height);
		nodeRenderManager.drawNodes(true);
		UiGlobals.setNodeRenderManager(nodeRenderManager);

	}

	public int cvtLoc(int loc) {
		return (int) ((loc) * scale) + padding / 2;
	}

	public double getColorDist(Color c1, Color c2) {
		double dA = c1.getAlpha() - c2.getAlpha();
		double dR = c1.getRed() - c2.getRed();
		double dG = c1.getGreen() - c2.getGreen();
		double dB = c1.getBlue() - c2.getBlue();

		return Math.sqrt(dA * dA + dR * dR + dG * dG + dB * dB);
	}

	public Color getPixelARGB(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		// System.out.println("argb: " + alpha + ", " + red + ", " + green +
		// ", " + blue);\
		return new Color(red, green, blue);
	}

	public Color getColorBin(Color c, List<Color> colorBinList, double threshold) {
		Color resultBin = null;
		double minDist = 9999999;

		for (Color cc : colorBinList) {
			double curDist = getColorDist(c, cc);
			if (curDist < threshold && curDist < minDist) {
				resultBin = cc;
				minDist = curDist;
			}
		}

		return resultBin;
	}

	public Map<Integer, List<CCSSourceData>> makeRefRandomData(
			BufferedImage refImage, BufferedImage refTerrainImage) {

		Map<Integer, List<CCSSourceData>> ccsData = new HashMap<Integer, List<CCSSourceData>>();
		Random random = new Random();

		int h = refImage.getHeight();
		int w = refImage.getWidth();

		System.out.println("height : " + h + ", width:" + w);

		Map<Dimension, Integer> terrianTypexMap = new HashMap<Dimension, Integer>();
		Map<Integer, List<Dimension>> regionIdxMap = new HashMap<Integer, List<Dimension>>();
		
		List<Color> terrainColorBinList = new ArrayList<Color>();
		for(Color key : CCSStatics.terrainColorMap.keySet()){
			terrainColorBinList.add(key);
		}
		
		
		double threshold = 10;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				// int loc = i * w + j;
				Dimension loc = new Dimension(j, i);
				int pixel = refImage.getRGB(j, i);
				Color c = getPixelARGB(pixel);
				Integer region_idx = CCSStatics.regionColorMap.get(c);
				//Skip unrecognized region.
				if(region_idx == null) continue;
				
				int pixelTerrain = refTerrainImage.getRGB(j,  i);
				Color tColor = getPixelARGB(pixelTerrain);
				
				Color tBin = getColorBin(tColor, terrainColorBinList, threshold);
				
				terrianTypexMap.put(loc, CCSStatics.terrainColorMap.get(tBin));

				if(region_idx != null){
					List<Dimension> locList = regionIdxMap.get(region_idx);
					if (locList == null) {
						locList = new ArrayList<Dimension>();
						regionIdxMap.put(region_idx, locList);
					}
					locList.add(loc);
				}
			}
		}

		System.out.println("colorIdxMap key Size:"
				+ regionIdxMap.keySet().size());
		// System.out.println("colorIdxMap:"+colorIdxMap.get(colorBinList.get(0)));

		// int pixel = image.getRGB(j, i);
		// printPixelARGB(pixel);

		int hubPerEachRegion = 1;

		List<CCSSourceData> sourceData = new ArrayList<CCSSourceData>();
		List<CCSSourceData> hubData = new ArrayList<CCSSourceData>();
		List<CCSSourceData> storageData = new ArrayList<CCSSourceData>();

		//System.out.println(CCSStatics.terrainColorMap);
		
		
		//System.out.println(terrianTypexMap);
		
		for(Integer region_idx : regionIdxMap.keySet()){
			
			List<PlantData> plantList = CCSStatics.plantInfoMap.get(region_idx);
			List<Dimension> locList = regionIdxMap.get(region_idx);
			
			int hub_size = CCSStatics.hubNumberMap.get(region_idx);
			
			List<Integer> sourceInThisRegion = new ArrayList<Integer>();
			
			for(PlantData plantData : plantList){
				
				List<Float> portions = getRandomPortions(plantData.plant_num);
				
				
				
				for (int plant_count = 0; plant_count < plantData.plant_num; plant_count++) {
					float cur_co2 = portions.get(plant_count) * plantData.co2_amount;
				
					Dimension curLoc = locList.get(random.nextInt(locList.size()));
					locList.remove(curLoc);
					
					int x = cvtLoc((int) curLoc.getWidth());
					int y = cvtLoc((int) curLoc.getHeight());
					
					
					Integer loc_terrain_type = terrianTypexMap.get(curLoc);
					if(loc_terrain_type == null){
						plant_count--;
						continue;
					}

					
					CCSSourceData node = new CCSSourceData(x, y, cur_co2, plantData.industry_type, loc_terrain_type);
					sourceData.add(node);
					UiGlobals.addNode(node);
					sourceInThisRegion.add(node.getIndex());
					
					
				}
				
				System.out.println("hub size:"+hub_size);
				for(int i = 0 ; i < hub_size ; i++){
					Integer hub_cnt = random.nextInt(sourceInThisRegion.size());
					CCSSourceData curSrc = UiGlobals.getNode(sourceInThisRegion.get(hub_cnt));
					sourceData.remove(curSrc);
					int range = (int) (60 / CCSStatics.kilometerToMile / CCSStatics.pixelToDistance);
					curSrc = new CCSHubData(curSrc, range);
					hubData.add(curSrc);
					//UiGlobals.addNode(node);
					
					
					sourceInThisRegion.remove(hub_cnt);
				}
			}
			
			
			
			
			
		}
		
		
		for(Integer key : CCSStatics.storageMap.keySet()){
			System.out.println("ADD STORAGE");
			StorageData sData = CCSStatics.storageMap.get(key);
			CCSPlantData storage = new CCSPlantData(sData.x, sData.y, sData.storagecapacity, sData.geological_information);
			System.out.println(storage);
			storageData.add(storage);
			UiGlobals.addNode(storage);
		}
	
		//System.out.println("COLOR END!");
		ccsData.put(CCSSourceData.TYPE_SOURCE, sourceData);
		ccsData.put(CCSSourceData.TYPE_HUB, hubData);
		ccsData.put(CCSSourceData.TYPE_PLANT, storageData);

		return ccsData;
	}
	
	
	
	public List<Float> getRandomPortions(int pie_num){
		List<Integer> portions_int = new ArrayList<Integer>();
		List<Float> portions = new ArrayList<Float>();
		
		Random rand = new Random();
		
		int sum = 0;
		for(int i = 0 ; i < pie_num ; i ++){
			int cur_val = rand.nextInt(100);
			sum+=cur_val;
			portions_int.add(cur_val);
		}
		
		for(int i = 0 ; i < pie_num ; i ++){
			portions.add(portions_int.get(i) / (float)sum);
		}

		return portions;
	}

	public Map<Integer, List<CCSSourceData>> makeRandomData(int size,
			int maxWidth, int maxHeight) {
		// IloCplex cplex = new IloCplex();
		Map<Integer, List<CCSSourceData>> ccsData = new HashMap<Integer, List<CCSSourceData>>();
		Random random = new Random();

		List<CCSSourceData> sourceData = new ArrayList<CCSSourceData>();

		for (int count = 0; count < size; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			int co2 = random.nextInt(100);
			int terrain_type = random.nextInt(10);
			
			CCSSourceData node = new CCSSourceData(x, y, co2, terrain_type);
			
			sourceData.add(node);
			UiGlobals.addNode(node);
			//node.setIndex(index);
		}

		List<CCSSourceData> hubData = new ArrayList<CCSSourceData>();
		for (int count = 0; count < 10; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			int range = 100;
			int co2 = random.nextInt(100);
			int terrain_type = random.nextInt(10);
			CCSSourceData node = new CCSHubData(x, y, co2, 1, terrain_type, range);
			
			hubData.add(node);
			UiGlobals.addNode(node);
			//node.setIndex(index);
		}

		List<CCSSourceData> plantData = new ArrayList<CCSSourceData>();
		for (int count = 0; count < size / 10; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			CCSSourceData node = new CCSPlantData(x, y, 0, 1);
			plantData.add(node);
			UiGlobals.addNode(node);
			//node.setIndex(index);
		}

		// List<CCSSourceData> jointData = new ArrayList<CCSSourceData>();
		// for (int count = 0; count < size/10; count++) {
		// int x = cvtLoc(random.nextInt(maxWidth));
		// int y = cvtLoc(random.nextInt(maxHeight));
		// CCSSourceData node = new CCSJointData(x, y);
		// jointData.add(node);
		// int index = UiGlobals.addNode(node);
		// node.setIndex(index);
		// }

		ccsData.put(CCSSourceData.TYPE_SOURCE, sourceData);
		ccsData.put(CCSSourceData.TYPE_HUB, hubData);
		ccsData.put(CCSSourceData.TYPE_PLANT, plantData);
		// ccsData.put(CCSSourceData.TYPE_JOINT, jointData);

		return ccsData;
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
		NodePaletteFig topBar = new NodePaletteFig();
		this.setToolBar(topBar); // needs-more-work
		ResizerPaletteFig westBar = new ResizerPaletteFig();
		this.setWestToolBar(westBar);

		_graph = jg;
		UiGlobals.graph = _graph;

		editor = _graph.getEditor();
		// _graph.setBackground(Color.white);
		// _graph.setBounds(0, 0, _width, _height);

		LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed(
				"Grid");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Color", Color.LIGHT_GRAY);
		map.put("bgColor", Color.white);
		map.put("spacing_include_stamp", 5000);
		map.put("paintLines", true);
		map.put("paintDots", false);

		try {
			refImage = ImageIO.read(this.getClass().getResource(
					"/ac/kaist/ccs/images/korea_02.jpg"));
			refTerrainImage = ImageIO.read(this.getClass().getResource(
					"/ac/kaist/ccs/images/Terrain_1.png"));
			
			CCSStatics.refImg = refImage;
			CCSStatics.refTerrainImg = refTerrainImage;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int bgPadding = 20;

		int bgWidth = refImage.getWidth() + bgPadding;
		int bgHeight = refImage.getHeight() + bgPadding;

		map.put("stamp", (Image) refImage);

		System.out.println("ADJUST!: " + refImage);
		grid.adjust(map);

		Container content = getContentPane();
		setUpMenus();
		content.setLayout(new BorderLayout());
		content.add(_menubar, BorderLayout.NORTH);
		_graphPanel.add(_graph, BorderLayout.CENTER);
		_graphPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		UiGlobals.setGraphPane(_graphPanel);
		// _mainPanel.add(_graphPanel, BorderLayout.CENTER);
		content.add(_mainPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("insets 0 0 0 0"));
		bottomPanel.add(_statusbar, "wrap");

		content.add(bottomPanel, BorderLayout.SOUTH);
		UiGlobals.set_statusBar(_statusbar);
		UiGlobals.setCoordBottomPanel(bottomPanel);
		setSize(bgWidth + westBar.getTaskWidth(), bgHeight + topBar.getHeight());

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

	protected void setUpMenus() {
		// JMenuBar menuBar = new JMenuBar();

		JMenuItem openItem, saveItem, printItem, exitItem;
		JMenuItem deleteItem, copyItem, pasteItem;
		JMenuItem groupItem, ungroupItem;
		JMenuItem toBackItem, backwardItem, toFrontItem, forwardItem;

		JMenu file = new JMenu(Localizer.localize("GefBase", "File"));
		file.setMnemonic('F');
		_menubar.add(file);
		// file.add(new CmdNew());
		openItem = file.add(new CmdOpen());
		saveItem = file.add(new CmdSave());
		file.add(new CmdSavePGML());
		file.add(new CmdSaveSVG());
		CmdPrint cmdPrint = new CmdPrint();
		printItem = file.add(cmdPrint);
		file.add(new CmdPrintPageSetup(cmdPrint));
		file.add(new CmdOpenWindow("org.tigris.gef.base.PrefsEditor",
				"Preferences..."));
		// file.add(new CmdClose());
		exitItem = file.add(new CmdExit());

		JMenu edit = new JMenu(Localizer.localize("GefBase", "Edit"));
		edit.setMnemonic('E');
		// _menubar.add(edit);

		JMenuItem undoItem = edit.add(new UndoAction(Localizer.localize(
				"GefBase", "Undo")));
		undoItem.setMnemonic(Localizer.localize("GefBase", "UndoMnemonic")
				.charAt(0));
		JMenuItem redoItem = edit.add(new RedoAction(Localizer.localize(
				"GefBase", "Redo")));
		redoItem.setMnemonic(Localizer.localize("GefBase", "RedoMnemonic")
				.charAt(0));

		JMenu select = new JMenu(Localizer.localize("GefBase", "Select"));
		edit.add(select);
		select.add(new CmdSelectAll());
		select.add(new CmdSelectNext(false));
		select.add(new CmdSelectNext(true));
		select.add(new CmdSelectInvert());

		edit.addSeparator();

		copyItem = edit.add(new CmdCopy());
		copyItem.setMnemonic('C');
		pasteItem = edit.add(new CmdPaste());
		pasteItem.setMnemonic('P');

		deleteItem = edit.add(new CmdRemoveFromGraph());
		edit.addSeparator();
		edit.add(new CmdUseReshape());
		edit.add(new CmdUseResize());
		edit.add(new CmdUseRotate());

		JMenu view = new JMenu(Localizer.localize("GefBase", "View"));
		// _menubar.add(view);
		view.setMnemonic('V');
		view.add(new CmdSpawn());
		view.add(new CmdShowProperties());
		// view.addSeparator();
		// view.add(new CmdZoomIn());
		// view.add(new CmdZoomOut());
		// view.add(new CmdZoomNormal());
		view.addSeparator();
		view.add(new CmdAdjustGrid());
		view.add(new CmdAdjustGuide());
		view.add(new CmdAdjustPageBreaks());

		JMenu arrange = new JMenu(Localizer.localize("GefBase", "Arrange"));
		// _menubar.add(arrange);
		arrange.setMnemonic('A');
		groupItem = arrange.add(new CmdGroup());
		groupItem.setMnemonic('G');
		ungroupItem = arrange.add(new CmdUngroup());
		ungroupItem.setMnemonic('U');

		JMenu align = new JMenu(Localizer.localize("GefBase", "Align"));
		arrange.add(align);
		align.add(new AlignAction(AlignAction.ALIGN_TOPS));
		align.add(new AlignAction(AlignAction.ALIGN_BOTTOMS));
		align.add(new AlignAction(AlignAction.ALIGN_LEFTS));
		align.add(new AlignAction(AlignAction.ALIGN_RIGHTS));
		align.add(new AlignAction(AlignAction.ALIGN_H_CENTERS));
		align.add(new AlignAction(AlignAction.ALIGN_V_CENTERS));
		align.add(new AlignAction(AlignAction.ALIGN_TO_GRID));

		JMenu distribute = new JMenu(
				Localizer.localize("GefBase", "Distribute"));
		arrange.add(distribute);
		distribute.add(new DistributeAction(DistributeAction.H_SPACING));
		distribute.add(new DistributeAction(DistributeAction.H_CENTERS));
		distribute.add(new DistributeAction(DistributeAction.V_SPACING));
		distribute.add(new DistributeAction(DistributeAction.V_CENTERS));

		JMenu reorder = new JMenu(Localizer.localize("GefBase", "Reorder"));
		arrange.add(reorder);
		toBackItem = reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
		toFrontItem = reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
		backwardItem = reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
		forwardItem = reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));

		JMenu nudge = new JMenu(Localizer.localize("GefBase", "Nudge"));
		arrange.add(nudge);
		nudge.add(new NudgeAction(NudgeAction.LEFT));
		nudge.add(new NudgeAction(NudgeAction.RIGHT));
		nudge.add(new NudgeAction(NudgeAction.UP));
		nudge.add(new NudgeAction(NudgeAction.DOWN));

		KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK);
		KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_MASK);

		KeyStroke delKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlU = KeyStroke.getKeyStroke(KeyEvent.VK_U,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B,
				KeyEvent.CTRL_MASK);
		KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK);
		KeyStroke sCtrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		KeyStroke sCtrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);

		openItem.setAccelerator(ctrlO);
		saveItem.setAccelerator(ctrlS);
		printItem.setAccelerator(ctrlP);
		exitItem.setAccelerator(altF4);

		deleteItem.setAccelerator(delKey);
		undoItem.setAccelerator(ctrlZ);
		redoItem.setAccelerator(ctrlY);
		copyItem.setAccelerator(ctrlC);
		pasteItem.setAccelerator(ctrlV);

		groupItem.setAccelerator(ctrlG);
		ungroupItem.setAccelerator(ctrlU);

		toBackItem.setAccelerator(sCtrlB);
		toFrontItem.setAccelerator(sCtrlF);
		backwardItem.setAccelerator(ctrlB);
		forwardItem.setAccelerator(ctrlF);

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
	 * 
	 * @param font
	 */
	public void setDefaultFont(Font font) {

		String[] applyList = { "RadioButtonMenuItem.font",
				"CheckBoxMenuItem.font", "RadioButton.font", "ToolBar.font",
				"ProgressBar.font", "Menu.font", "Button.font",
				"TitledBorder.font", "ComboBox.font", "ToggleButton.font",
				"TabbedPane.font", "List.font", "MenuBar.font",
				"MenuItem.font", "CheckBox.font", "Label.font", };

		int nSize = applyList.length;
		for (int i = 0; i < nSize; i++) {
			UIManager.put(applyList[i], font);
		}

	}

}
