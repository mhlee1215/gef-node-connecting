package ac.kaist.ccs.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.jfree.ui.RefineryUtilities;
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
import org.tigris.gef.base.CmdZoomIn;
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

import ac.kaist.ccs.base.CmdZoom;
import ac.kaist.ccs.base.CustomCellRenderer;
import ac.kaist.ccs.base.MyTableModel;
import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSEdgeData;
import ac.kaist.ccs.domain.CCSExpData;
import ac.kaist.ccs.domain.CCSHubData;
import ac.kaist.ccs.domain.CCSJointData;
import ac.kaist.ccs.domain.CCSNodeData;
import ac.kaist.ccs.domain.CCSPlantData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSStatics;
import ac.kaist.ccs.domain.CCSUtils;
import ac.kaist.ccs.domain.CCSStatics.PlantData;
import ac.kaist.ccs.domain.CCSStatics.StorageData;
import ac.kaist.ccs.domain.CCSUtils.SortTuple;
import ac.kaist.ccs.presentation.CCSHubSelectionCo2Coverage;
import ac.kaist.ccs.presentation.CCSHubSelectionCost;
import ac.kaist.ccs.presentation.CCSHubSelectionCoverage;
import ac.kaist.ccs.ui.NodePaletteFig;
import ac.kaist.ccs.ui.NodeRenderManager;
import ac.kaist.ccs.ui.ResizerPaletteFig;
import ac.kaist.ccs.ui.WestToolBar;
import ac.kaist.ccs.domain.CCSUtils.*;
import ac.kaist.ccs.fig.FigCCSNode;

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
	
	JMenu showHub = null;
	JMenuItem showStorage = null;
	JMenu showConnection = null;
	

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

		
		UiGlobals.saveNodeSnapshot(CCSStatics.STATE_INITIAL);
		doMainProcess(1, 0, null);

		// Insert Loading data
//		UiGlobals.resetNodes();
//		//UiGlobals.ccsData  = makeRandomData(300, 500, 500);
//		makeRefRandomData(refImage, refTerrainImage, UiGlobals.ccsData);
		
		
		
//		UiGlobals.saveNodeSnapshot();
//		//List<Integer> hubIndexList = new ArrayList<Integer>();
//		List<Integer> hubIndexList = selectHubIndexList();
//		
//		
//		System.out.println("Selected hubIndexList: "+hubIndexList);
//		
//		showSourceData();
//		showCombinedData(hubIndexList);
//		List<Double> coverageList = getCoverage(hubIndexList);
//		List<Double> coverageAmountList = getCoverageAmount(hubIndexList);
//		
//		UiGlobals.loadNodeSnapshot();
//		
//		//selectHubNodes(hubIndexList);
//		List<Integer> costTypeList = CCSStatics.getCostTypeList();
//		Map<Integer, List<Double> > totalCostList = new HashMap<Integer, List<Double>>();
//		for(Integer hub_cnt : hubIndexList){
//			//Actual hub selection for cost simulation.
//			selectHubNode(hub_cnt);
//			for(int costType : costTypeList){
//				if(totalCostList.get(costType) == null)
//					totalCostList.put(costType, new ArrayList<Double>());
//				
//				double totalCost = computeHubCostAll(costType, CCSStatics.CO2_STATE_EXTREME);
//				totalCostList.get(costType).add(totalCost);
//			}
//		}
//		
//		
//		//SHOW HUB SELECTION COST
//		CCSHubSelectionCost costFrame = new CCSHubSelectionCost("Hub Selection - COST", totalCostList);
//        costFrame.pack();
//        RefineryUtilities.centerFrameOnScreen(costFrame);
//        costFrame.setVisible(true);
//
//        //SHOW HUB SELECTION COVERAGE
//        CCSHubSelectionCoverage coverageFrame = new CCSHubSelectionCoverage("Hub Selection - COVERAGE", coverageList);
//        coverageFrame.pack();
//        RefineryUtilities.centerFrameOnScreen(coverageFrame);
//        coverageFrame.setVisible(true);
//        
//        //SHOW HUB SELECTION COVERAGE AMOUNT
//        CCSHubSelectionCo2Coverage coverageAmountFrame = new CCSHubSelectionCo2Coverage("Hub Selection - COVERAGE AMOUNT", coverageAmountList);
//        coverageAmountFrame.pack();
//        RefineryUtilities.centerFrameOnScreen(coverageAmountFrame);
//        coverageAmountFrame.setVisible(true);
//		
//		
//        makeRefStorageData(refImage, refTerrainImage, UiGlobals.ccsData);
//		UiGlobals.saveNodeSnapshot();
	
		//List<Integer> hubList = UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB);
		//UiGlobals.loadNodeSnapshot();
		
		
//		System.out.println("Randering!");
//		NodeRenderManager nodeRenderManager = new NodeRenderManager(UiGlobals.ccsData,
//				null, _graph);
//		nodeRenderManager.init(_width, _height);
//		nodeRenderManager.drawNodes(true);
//		UiGlobals.setNodeRenderManager(nodeRenderManager);

	}
	
	public void doMainProcess(int stageUntil, int stageSpecific, Map<String, String> params){
		
		//Stage for source allocation
		if(stageUntil >= 1 || stageSpecific == 1){
			if(params == null || "true".equals(params.get("isResetNodes"))){
				UiGlobals.resetNodes();
				//UiGlobals.ccsData  = makeRandomData(300, 500, 500);
				makeRefRandomData(refImage, refTerrainImage, UiGlobals.ccsData);
				UiGlobals.saveNodeSnapshot(CCSStatics.STATE_CO2_ALLOCATED);
			}
			
		}
		
		//Stage for storage
		if(stageUntil >= 2 || stageSpecific == 2){
			UiGlobals.loadNodeSnapshot(CCSStatics.STATE_CO2_ALLOCATED);
			
			double isolatedSourceCost = 1000000000000.0;
			boolean isShowReports = false;
			
			if(params != null && params.get("isolatedSourceCost") != null){
				isolatedSourceCost = Double.parseDouble(params.get("isolatedSourceCost"));
			}
			
			if(params != null && params.get("isShowReports") != null){
				if("Y".equals(params.get("isShowReports").toString())) isShowReports = true;
				else if("N".equals(params.get("isShowReports").toString())) isShowReports = false;
			}
			
			
			
			//UiGlobals.saveNodeSnapshot();
			//List<Integer> hubIndexList = new ArrayList<Integer>();
			List<Integer> hubIndexList = selectHubIndexList(isShowReports);
			
			
			//System.out.println("Selected hubIndexList: "+hubIndexList);
			if(isShowReports){
				showSourceData();
				showCombinedData(hubIndexList);	
			}
			
			List<Double> coverageList = getCoverage(hubIndexList);
			List<Double> coverageAmountList = getCoverageAmount(hubIndexList);
			
			//UiGlobals.loadNodeSnapshot(CCSStatics.STATE_CO2_ALLOCATED);
			
			int hubSize = 0;
			while(hubSize == 0){
				try{
					String response = (String) JOptionPane.showInputDialog(null,
			                "The number of hub size ..",
			                "Enter hub property",
			                JOptionPane.QUESTION_MESSAGE, null, null, "10");
					hubSize = Integer.parseInt(response);
				}catch(Exception e){
					
				}
			}
			
	       
			
			//selectHubNodes(hubIndexList);
			List<Integer> costTypeList = CCSStatics.getCostTypeList();
			Map<Integer, List<Double> > totalCostList = new HashMap<Integer, List<Double>>();
			int hubCnt = 1;
			for(Integer hub_cnt : hubIndexList){
				System.out.println("++++getHubSelectionSources: "+UiGlobals.getNode(hub_cnt).getHubSelectionSources());
				if(hubCnt > hubSize) break;
				//Actual hub selection for cost simulation.
				selectHubNode(hub_cnt);
				for(int costType : costTypeList){
					if(totalCostList.get(costType) == null)
						totalCostList.put(costType, new ArrayList<Double>());
					
					double totalCost = computeHubCostAll(costType, CCSStatics.CO2_STATE_EXTREME, isolatedSourceCost);
					totalCostList.get(costType).add(totalCost);
				}
				hubCnt++;
			}
			
			if(isShowReports){
				//SHOW HUB SELECTION COST
				CCSHubSelectionCost costFrame = new CCSHubSelectionCost("Hub Selection - COST (isolatedSourceCost="+isolatedSourceCost+")", totalCostList);
		        costFrame.pack();
		        RefineryUtilities.centerFrameOnScreen(costFrame);
		        costFrame.setVisible(true);
	
		        //SHOW HUB SELECTION COVERAGE
		        CCSHubSelectionCoverage coverageFrame = new CCSHubSelectionCoverage("Hub Selection - COVERAGE", coverageList);
		        coverageFrame.pack();
		        RefineryUtilities.centerFrameOnScreen(coverageFrame);
		        coverageFrame.setVisible(true);
		        
		        //SHOW HUB SELECTION COVERAGE AMOUNT
		        CCSHubSelectionCo2Coverage coverageAmountFrame = new CCSHubSelectionCo2Coverage("Hub Selection - COVERAGE AMOUNT", coverageAmountList);
		        coverageAmountFrame.pack();
		        RefineryUtilities.centerFrameOnScreen(coverageAmountFrame);
		        coverageAmountFrame.setVisible(true);
			}
	        
	        UiGlobals.saveNodeSnapshot(CCSStatics.STATE_HUB_ALLOCATED);
			
	        
		}
		
		if(stageUntil >= 3 || stageSpecific == 3){
			UiGlobals.loadNodeSnapshot(CCSStatics.STATE_HUB_ALLOCATED);
			makeRefStorageData(refImage, refTerrainImage, UiGlobals.ccsData);
			UiGlobals.saveNodeSnapshot(CCSStatics.STATE_STORAGE_ALLOCATED);
		}
		
		if(stageUntil >= 4 || stageSpecific == 4){
			UiGlobals.saveNodeSnapshot(CCSStatics.STATE_BEFORE_CONNECT);
			
			int connectionType = CCSStatics.CONNECT_TYPE_STAR;
			if("star".equalsIgnoreCase(params.get("connectionType"))) connectionType = CCSStatics.CONNECT_TYPE_STAR;
			else if("tree".equalsIgnoreCase(params.get("connectionType"))) connectionType = CCSStatics.CONNECT_TYPE_TREE;
			else if("backbone".equalsIgnoreCase(params.get("connectionType"))) connectionType = CCSStatics.CONNECT_TYPE_BACKBONE;
			else if("hybrid".equalsIgnoreCase(params.get("connectionType"))) connectionType = CCSStatics.CONNECT_TYPE_HYBRID;
			
			System.out.println("Randering!");
			NodeRenderManager nodeRenderManager = new NodeRenderManager(UiGlobals.ccsData,
					null, _graph);
			nodeRenderManager.init(_width, _height);
			nodeRenderManager.drawNodes(true, connectionType, CCSStatics.COST_TYPE_THE_OGDEN_MODELS);
			UiGlobals.setNodeRenderManager(nodeRenderManager);
		}else{
			UiGlobals.saveNodeSnapshot(CCSStatics.STATE_BEFORE_CONNECT);
			
			System.out.println("Randering without connection!");
			NodeRenderManager nodeRenderManager = new NodeRenderManager(UiGlobals.ccsData,
					null, _graph);
			nodeRenderManager.init(_width, _height);
			nodeRenderManager.drawNodes(true, 0, 0);
			UiGlobals.setNodeRenderManager(nodeRenderManager);
		}
	}
	
	public List<Integer> selectHubIndexList(boolean isShowReports){
		List<Integer> hubIndexList = new ArrayList<Integer>();
		
		//selectRandomHubNodes();
		Vector<Vector> tableContentAll = new Vector<Vector>();
		
		for(int i = 0 ; i < 10 ; i++){
			hubClustering();
			List<SortTuple> s = computeS_selAll();
			Vector<Vector> tableContent = convertSortTupleToTable(s);
			
			Vector separator = new Vector();
			for(int j = 0 ; j < tableContent.get(0).size(); j++){
				separator.add("");
			}
			tableContent.add(separator);
			
			for(Vector row : tableContent){
				tableContentAll.add(row);	
			}
			
			
			hubIndexList.add((int) s.get(0).firstId);
			//hubIndexList.add(10);
			System.out.println("sort tuple list :"+s.size());
			createHubNode((int) s.get(0).firstId);
		}
		
		if(isShowReports){
			Vector<String> columnNames = new Vector<String>();
	 	    columnNames.addElement("ID");
	 	    columnNames.addElement("S_sel");
	 	    columnNames.addElement("Connected Node #");
	 	    columnNames.addElement("Acc CO2 amount");
	 	    
	 	    MyTableModel mm = new MyTableModel();
		    mm.setData(tableContentAll);
		    mm.setColumnNames(columnNames);

		    JTable table = new JTable(mm)
		    {
				 @Override
				   public TableCellRenderer getCellRenderer(int row, int column) {
				    // TODO Auto-generated method stub
				    return new CustomCellRenderer();
				   }
			};

		    
		    table.setAutoCreateRowSorter(true);
		    table.setRowSelectionAllowed(true);
		    table.setColumnSelectionAllowed(false);
		    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    table.getSelectionModel().addListSelectionListener(new RowListener().init(table));
		    
		    TableColumnModel cm = table.getColumnModel();
		    
		    cm.getColumn(0).setPreferredWidth(100);


		    
		    table.getTableHeader().setForeground(Color.white);
		    table.getTableHeader().setBackground(CustomCellRenderer.headerBG);
		    
			
			JScrollPane scroll = new JScrollPane( table );
			
			JPanel m = new JPanel();
			m.setLayout(new BorderLayout());
			m.add("Center", scroll);
			
			JPanel buttonPanel = new JPanel();
			JButton exportButton = new JButton("Export");
			exportButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	JFileChooser fileChooser = new JFileChooser();
	            	
	            	 /* Enabling Multiple Selection */
	                //fileChooser.setMultiSelectionEnabled(true);

	                /* Setting Current Directory */
	                fileChooser.setCurrentDirectory(new File("E:/ext_work/respace/workspace/CTS_analysis/input"));
	                
	                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel 97 File","xls"));
	                
	                //JFrame parent = new MenuMain();
	                
	                
	                int returnVal = fileChooser.showDialog(new JFrame(), "Open File Path");
	            }
			});
			buttonPanel.add(exportButton);
			
			m.add("South", buttonPanel);

			
			JFrame frame = new JFrame("Cost Result Table");
			frame.getContentPane().add( m );
			frame.setVisible(true);
			frame.setSize( 1024, 500 ); 
		}
		
		
		
		return hubIndexList;
	}
	
	public List<Double> getCoverage(List<Integer> hubIndexList){
		int total = UiGlobals.ccsData.get(CCSSourceData.TYPE_SOURCE).size()+UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB).size();
		List<Double> coverage = new ArrayList<Double>();
		
		int curentCoverNodeNum = 0;
		for(int index : hubIndexList){
			CCSSourceData sData = UiGlobals.getNode(index);
			curentCoverNodeNum += sData.getHubSelectionSources().size();
			coverage.add(100*curentCoverNodeNum / (double) total );
		}
		
		return coverage;
	}
	
	public List<Double> getCoverageAmount(List<Integer> hubIndexList){
		List<Integer> sourceData = UiGlobals.ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<Integer> hubData = UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB);
		
		double totalCo2Amount = 0.0;
		
		System.out.println("sourceData.size:"+sourceData.size());
		for(int index : sourceData){
			CCSSourceData sData = UiGlobals.getNode(index);
			totalCo2Amount += sData.getCo2_amount();
		}
		
		System.out.println("hubData.size:"+hubData.size());
		for(int index : hubData){
			CCSSourceData sData = UiGlobals.getNode(index);
			totalCo2Amount += sData.getCo2_amount();
		}
		
		List<Double> coverageCo2Amount = new ArrayList<Double>();
		
		double curentCoverCo2Amount = 0;
		System.out.println("hubIndexList: "+hubIndexList);
		for(int index : hubIndexList){
			CCSSourceData sData = UiGlobals.getNode(index);
			System.out.println("call computes2 :"+index);
			sData.computeCo2();
			curentCoverCo2Amount += sData.getAcc_co2_amount();
			coverageCo2Amount.add((curentCoverCo2Amount*100)/(double) totalCo2Amount );
		}
		
		System.out.println(">>>>>>>>>"+coverageCo2Amount);
		return coverageCo2Amount;
	}
	
	public void hubClustering(){
		List<Integer> hubCandidateData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB_CANDIDATE);
		List<Integer> sourceData = UiGlobals.ccsData.get(CCSNodeData.TYPE_SOURCE);
		
		//Clear Connection
		for(int sourceIndex : sourceData){
			CCSSourceData sData = UiGlobals.getNode(sourceIndex);
			sData.getOutgoingHub().clear();
		}
		
		for(int hubCandidateIndex : hubCandidateData){
			CCSSourceData hCandidateData = UiGlobals.getNode(hubCandidateIndex);
			hCandidateData.getHubSelectionSources().clear();
		}
		
		System.out.println("hubCandidateData: "+hubCandidateData);
		for(int hubCandidateIndex : hubCandidateData){
			CCSSourceData hCandidateData = UiGlobals.getNode(hubCandidateIndex);
			
			for(int sourceIndex : sourceData){
				if(hubCandidateData.contains((Integer)sourceIndex)){
					//System.out.println("sourceIndex("+sourceIndex+") canceled, "+"hubCandidateData: "+hubCandidateData);
					continue;
				}
				//System.out.println("sourceIndex("+sourceIndex+") okay, "+"hubCandidateData: "+hubCandidateData);
				CCSSourceData sData = UiGlobals.getNode(sourceIndex);
				
				if(sData.isValid() && !sData.isHubCandidate() && CCSUtils.dist(sData, hCandidateData) < (CCSStatics.HUB_SELECTION_RANGE / CCSStatics.pixelToDistance)){
					System.out.println("hCandidate("+hCandidateData.getIndex()+") added children ("+sData.getIndex()+")");
					//hCandidateData.addChildSource(sData.getIndex());
					hCandidateData.getHubSelectionSources().add(sData.getIndex());
			
					sData.addOutgoingHub(hCandidateData.getIndex());
				}
			}
		}
	}
	
	public List<SortTuple> computeS_selAll(){
		List<Integer> hubCandidateData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB_CANDIDATE);
		List<SortTuple> nodesSortByS_sel = new ArrayList<SortTuple>();
		for(int hubCandidateIndex : hubCandidateData){
			CCSSourceData hCandidateData = UiGlobals.getNode(hubCandidateIndex);
			double s_sel = hCandidateData.computeHubSelectionCost();
			Vector row = new Vector();
			row.add(hubCandidateIndex);
			row.add(s_sel);
			row.add(hCandidateData.getHubSelectionSources().size());
			row.add(hCandidateData.getAcc_co2_amount());
			
			nodesSortByS_sel.add(new SortTuple(s_sel, hCandidateData.getIndex(), row));
		}
		
		Collections.sort(nodesSortByS_sel, new NoDecCompare());
		
//		Vector<Vector> tableContent = new Vector<Vector>();
//		for(SortTuple tuple : nodesSortByS_sel){
//			tableContent.add((Vector) tuple.secondId);
//		}
		return nodesSortByS_sel;
	}
	
	public Vector<Vector>convertSortTupleToTable(List<SortTuple> nodesSortByS_sel){
		Vector<Vector> tableContent = new Vector<Vector>();
		for(SortTuple tuple : nodesSortByS_sel){
			tableContent.add((Vector) tuple.secondId);
		}
		return tableContent;
	}
	
	//허브 선택을 위한 분석과정의 하나로 선택함. 분석용이며, 실제로 만들어 지는 단계와는 다름
	public void createHubNode(int hub_cnt){
		List<Integer> hubCandidateData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB_CANDIDATE);
		
		CCSSourceData curHubCandidate = UiGlobals.getNode(hub_cnt);
		//curHubCandidate.setValid(false);
		hubCandidateData.remove((Integer)hub_cnt);
		
		for(int childIndex : curHubCandidate.getHubSelectionSources()){
			CCSSourceData sData = UiGlobals.getNode(childIndex);
			//sData.setDistanceToDst((float) CCSUtils.dist(sData, curHubCandidate));
			System.out.println("set seelction hub :"+hub_cnt);
			sData.setSelectionHub(hub_cnt);
			sData.setValid(false);
		}
	}
	
	public void showCombinedData(List<Integer> hubIndexList){
		//List<Integer> ccsHubData = UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB);
		
		Vector<Vector> tableContentAll = new Vector<Vector>();
		for(Integer index : hubIndexList){
			CCSSourceData toBeHub = UiGlobals.getNode(index);
			toBeHub.computeHubCost(CCSStatics.COST_TYPE_THE_OGDEN_MODELS, CCSStatics.CO2_STATE_EXTREME);
			System.out.println(">>>>>>"+toBeHub.getHubSelectionSources());
			
			for(int i = 0 ; i < toBeHub.getHubSelectionSources().size() ; i++){
				Vector row = new Vector();
				int sIndex = toBeHub.getHubSelectionSources().get(i);
				CCSSourceData sData = UiGlobals.getNode(sIndex);
				if(i == 0){
					row.add(index);
					row.add(toBeHub.getTerrain_typeStringShort());
					row.add(toBeHub.getCo2_type());
					row.add(toBeHub.getExpData().getS_selection());
					row.add(toBeHub.getExpData().getS_cost());
					row.add("");
				}else{
					row.add("");
					row.add("");
					row.add("");
					row.add("");
					row.add("");
					row.add("");
				}
				
				row.add("Node "+sData.getIndex());
				row.add(sData.getX()+", "+sData.getY());
				row.add(sData.getIndustry_typeString());
				row.add(sData.getDistanceToSelectionHubKM());
				row.add(sData.getCo2_amount());
				row.add(sData.getExpData().getPipe_diameter());
				tableContentAll.add(row);	
			}
			
		}
		
		Vector<String> columnNames = new Vector<String>();

		columnNames.addElement("Hub ID");
		columnNames.addElement("terrain");
		columnNames.addElement("co2 status");
		columnNames.addElement("S_sel");
		columnNames.addElement("S_cost");
		columnNames.addElement("");
		columnNames.addElement("Node");
		columnNames.addElement("Location");
		columnNames.addElement("Industry (Plant)");
		columnNames.addElement("Distance (soure to hub)");
		columnNames.addElement("Co2 amount");
		columnNames.addElement("Diameter");
 	    
 	    MyTableModel mm = new MyTableModel();
	    mm.setData(tableContentAll);
	    mm.setColumnNames(columnNames);

	    JTable table = new JTable(mm)
	    {
			 @Override
			   public TableCellRenderer getCellRenderer(int row, int column) {
			    // TODO Auto-generated method stub
			    return new CustomCellRenderer();
			   }
		};

	    
	    table.setAutoCreateRowSorter(true);
	    table.setRowSelectionAllowed(true);
	    table.setColumnSelectionAllowed(false);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table.getSelectionModel().addListSelectionListener(new RowListener().init(table));
	    
	    TableColumnModel cm = table.getColumnModel();
	    
	    cm.getColumn(0).setPreferredWidth(100);


	    
	    table.getTableHeader().setForeground(Color.white);
	    table.getTableHeader().setBackground(CustomCellRenderer.headerBG);
	    
		
		JScrollPane scroll = new JScrollPane( table );
		
		JPanel m = new JPanel();
		m.setLayout(new BorderLayout());
		m.add("Center", scroll);
		
		JFrame frame = new JFrame("Hub&Source Info Table");
			    
		frame.getContentPane().add( m );
		frame.setVisible(true);
		frame.setSize( 1024, 500 ); 
		
	}
	
	public double computeHubCostAll(int costType, int co2Type, double isolatedSourceCost){
		List<Integer> hubNodes = UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB);
		
		double totalCost = 0.0;
		List<Integer> sourceComputed = new ArrayList<Integer>();
		
		for(Integer index : hubNodes){
			//List<CCSSourceData> hubfNodes = UiGlobals.getHubNodes();
			
			CCSHubData hubNode = (CCSHubData) UiGlobals.getNode(index);
			
			if(co2Type == 0){
				totalCost += hubNode.computeCost(costType);
			}else{
				totalCost += hubNode.computeCost(costType, co2Type);
			}
			
			sourceComputed.addAll(hubNode.getHubSelectionSources());
			
			//System.out.println("sourceComputed: "+sourceComputed);
			//System.out.println("hubNode.getHubSelectionSources(): "+hubNode.getHubSelectionSources());
		}
		
		System.out.println("sourceComputed("+sourceComputed.size()+"): "+sourceComputed);
		
		List<Integer> sourceNodes = UiGlobals.ccsData.get(CCSSourceData.TYPE_SOURCE);
		int isolatedCnt = 0;
		for(Integer index : sourceNodes){
			//어떠한 허브에도 속하지 않은 경우..
			if(!sourceComputed.contains(index)){
				CCSSourceData src = UiGlobals.getNode(index);
			//if(!(UiGlobals.getNode(src.getSelectionHub()) instanceof CCSHubData)){
			//if(src.isValid()){	
				totalCost += isolatedSourceCost * src.getCo2_amount() + src.getOpenCost();
				isolatedCnt++;
			}
		}
		System.out.println("isolatedCnt: "+isolatedCnt);
		
		return totalCost;
	}
	
	public void showSourceData(){
		List<Integer> ccsSourceData = UiGlobals.ccsData.get(CCSSourceData.TYPE_SOURCE);
		Vector<Vector> rowData = new Vector<Vector>(ccsSourceData.size());
		
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = new Vector();
			row.add(data.getIndex());
			
			row.add(data.getCo2_amount());
			row.add(data.getIndustry_typeString());
			
			if(data.getSelectionHub() != null)
				row.add(data.getDistanceToSelectionHubKM());
			else
				row.add("Not Connected");
			
			rowData.add(row);
		}
		
		
		Vector<String> columnNames = new Vector<String>();
 	    columnNames.addElement("Source ID");
 	    columnNames.addElement("Co2 Amount (tone/day)");
 	    columnNames.addElement("Industry Type");
 	    columnNames.addElement("Distant to Hub (km)");
 	    
 	    MyTableModel mm = new MyTableModel();
	    mm.setData(rowData);
	    mm.setColumnNames(columnNames);
	    
	    JTable table = new JTable(mm)
	    {
			 @Override
			   public TableCellRenderer getCellRenderer(int row, int column) {
			    // TODO Auto-generated method stub
			    return new CustomCellRenderer();
			   }
		};

	    
	    table.setAutoCreateRowSorter(true);
	    table.setRowSelectionAllowed(true);
	    table.setColumnSelectionAllowed(false);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table.getSelectionModel().addListSelectionListener(new RowListener().init(table));
	    
	    TableColumnModel cm = table.getColumnModel();
	    
	    cm.getColumn(0).setPreferredWidth(100);


	    
	    table.getTableHeader().setForeground(Color.white);
	    table.getTableHeader().setBackground(CustomCellRenderer.headerBG);
	    
		
		JScrollPane scroll = new JScrollPane( table );
		
		JPanel m = new JPanel();
		m.setLayout(new BorderLayout());
		m.add("Center", scroll);
		
		JFrame frame = new JFrame("Source Info Table");
			    
		frame.getContentPane().add( m );
		frame.setVisible(true);
		frame.setSize( 1024, 500 ); 
	}
	
	public static class RowListener implements ListSelectionListener {
		JTable table;
		
		public RowListener init(JTable table){
			this.table = table;
			return this;
		}
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            //System.out.println("select:"+table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0));
            try{
            	Integer selectedNodeIndex = (Integer) table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0);
            	if(selectedNodeIndex == null) return;
            	FigCCSNode fig = UiGlobals.getNode(selectedNodeIndex).getNode();
            	UiGlobals.graph.getEditor().getSelectionManager().select(fig);
            }catch(Exception e){
            	//e.printStackTrace();
            }
            
        }
    }
	
	public void showHubCandidateData(List<Integer> ccsHubCandidateData){
		//List<Integer> ccsHubData = UiGlobals.ccsData.get(CCSSourceData.TYPE_HUB);
		Vector<Vector> rowData = new Vector<Vector>(ccsHubCandidateData.size());
		
		for(Integer index : ccsHubCandidateData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = new Vector();
			row.add("Source "+data.getIndex());
			
			row.add(data.getCo2_amount());
			row.add(data.getIndustry_typeString());
			row.add(data.getDistanceToDst());
			
			rowData.add(row);
		}
		
		
		Vector<String> columnNames = new Vector<String>();
 	    columnNames.addElement("Hub Candidate");
 	    columnNames.addElement("S_sel");
 	    columnNames.addElement("# of connected Source node");
 	    columnNames.addElement("accumulate Co2 amount");
 	    
 	    MyTableModel mm = new MyTableModel();
	    mm.setData(rowData);
	    mm.setColumnNames(columnNames);

	    JTable table = new JTable(mm)
	    {
			 @Override
			   public TableCellRenderer getCellRenderer(int row, int column) {
			    // TODO Auto-generated method stub
			    return new CustomCellRenderer();
			   }
		};

	    
	    table.setAutoCreateRowSorter(true);
	    table.setRowSelectionAllowed(true);
	    table.setColumnSelectionAllowed(false);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //table.getSelectionModel().addListSelectionListener(new RowListener());
	    
	    TableColumnModel cm = table.getColumnModel();
	    
	    cm.getColumn(0).setPreferredWidth(100);


	    
	    table.getTableHeader().setForeground(Color.white);
	    table.getTableHeader().setBackground(CustomCellRenderer.headerBG);
	    
		
		JScrollPane scroll = new JScrollPane( table );
		
		JPanel m = new JPanel();
		m.setLayout(new BorderLayout());
		m.add("Center", scroll);
		
		JFrame frame = new JFrame("Source Info Table");
			    
		frame.getContentPane().add( m );
		frame.setVisible(true);
		frame.setSize( 1024, 500 ); 
	}
	
	public void selectRandomHubNodes(){
		Random random = new Random();
		List<Integer> hubCandidateData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB_CANDIDATE);
		List<Integer> hubData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
		List<Integer> sourceData = UiGlobals.ccsData.get(CCSNodeData.TYPE_SOURCE);
		
		for(int i = 0 ; i < 10 ; i++){
			Integer hub_cnt = random.nextInt(hubCandidateData.size());
			CCSSourceData curSrc = UiGlobals.getNode(hubCandidateData.get(hub_cnt));
			int range = (int) (60 / CCSStatics.kilometerToMile / CCSStatics.pixelToDistance);
			System.out.println("range : "+(60 / CCSStatics.kilometerToMile));
			curSrc = new CCSHubData(curSrc, range);
			UiGlobals.setNode(curSrc);
			hubData.add(curSrc.getIndex());
			sourceData.remove(curSrc.getIndex());
			hubCandidateData.remove((int)hub_cnt);
			System.out.println("hubCandidateData :"+hubCandidateData);
		}
	}
	
	public void selectHubNodes(List<Integer> hubIndexList){
		for(int hub_cnt : hubIndexList){
			selectHubNode(hub_cnt);
		}
	}
	
	//실제로 허브를 만드는 단계. 
	public void selectHubNode(Integer hubIndex){
		List<Integer> hubCandidateData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB_CANDIDATE);
		List<Integer> hubData = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
		List<Integer> sourceData = UiGlobals.ccsData.get(CCSNodeData.TYPE_SOURCE);
		
		CCSSourceData curSrc = UiGlobals.getNode(hubIndex);
		int range = (int) (60 / CCSStatics.kilometerToMile / CCSStatics.pixelToDistance);
		//System.out.println("range : "+(60 / CCSStatics.kilometerToMile));
		//System.out.println("children at hubselection : "+curSrc.getHubSelectionSources());
		
		for(Integer hubSelectionSource : curSrc.getHubSelectionSources()){
			CCSSourceData s = UiGlobals.getNode(hubSelectionSource);
			s.setSelectionHub(curSrc.getIndex());
		}
		
		curSrc = new CCSHubData(curSrc, range);
		UiGlobals.setNode(curSrc);
		hubData.add(curSrc.getIndex());
		sourceData.remove(curSrc.getIndex());
		
		
		hubCandidateData.remove((Integer)hubIndex);
		//System.out.println("hubCandidateData :"+hubCandidateData);
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

	public void makeRefRandomData(
			BufferedImage refImage, BufferedImage refTerrainImage, Map<Integer, List<Integer>> ccsData) {

		//Map<Integer, List<Integer>> ccsData = new HashMap<Integer, List<Integer>>();
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


		List<Integer> sourceData = new ArrayList<Integer>();
		List<Integer> hubData = new ArrayList<Integer>();
		List<Integer> hubCandidateData = new ArrayList<Integer>();
		List<Integer> storageData = new ArrayList<Integer>();
		//List<Integer> hubCandidateList = new ArrayList<Integer>();

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
					UiGlobals.addNode(node);
					sourceData.add(node.getIndex());
					sourceInThisRegion.add(node.getIndex());
					
					
				}
			}
			
			System.out.println("hub size:"+hub_size+", sourceInThisRegion:"+sourceInThisRegion);
			for(int i = 0 ; i < hub_size ; i++){
				Integer hub_cnt = random.nextInt(sourceInThisRegion.size());
				CCSSourceData curSrc = UiGlobals.getNode(sourceInThisRegion.get(hub_cnt));
				//hubCandidateList.add(sourceInThisRegion.get(hub_cnt));
				hubCandidateData.add(curSrc.getIndex());
				curSrc.setHubCandidate(true);
				
				sourceInThisRegion.remove((int)hub_cnt);
			}
		}
		
		
//		for(Integer key : CCSStatics.storageMap.keySet()){
//			StorageData sData = CCSStatics.storageMap.get(key);
//			CCSPlantData storage = new CCSPlantData(sData.x, sData.y, key, sData.storagecapacity, sData.geological_information);
//			UiGlobals.addNode(storage);
//			storageData.add(storage.getIndex());
//		}
	
		ccsData.put(CCSSourceData.TYPE_SOURCE, sourceData);
		ccsData.put(CCSSourceData.TYPE_HUB, hubData);
		//ccsData.put(CCSSourceData.TYPE_PLANT, storageData);
		ccsData.put(CCSSourceData.TYPE_HUB_CANDIDATE, hubCandidateData);

		//return ccsData;
	}
	
	public void makeRefStorageData(
			BufferedImage refImage, BufferedImage refTerrainImage, Map<Integer, List<Integer>> ccsData) {
		List<Integer> storageData = new ArrayList<Integer>();
		for(Integer key : CCSStatics.storageMap.keySet()){
			StorageData sData = CCSStatics.storageMap.get(key);
			CCSPlantData storage = new CCSPlantData(sData.x, sData.y, key, sData.storagecapacity, sData.geological_information);
			UiGlobals.addNode(storage);
			storageData.add(storage.getIndex());
		}
		ccsData.put(CCSSourceData.TYPE_PLANT, storageData);
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
			CCSSourceData node = new CCSPlantData(x, y, 1, 0, 1);
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

		JMenu view = new JMenu(Localizer.localize("GefBase", "View"));
		_menubar.add(view);
		view.add(new CmdZoom(1.2));
		view.add(new CmdZoom(1/1.2));
		
		JMenu experiment = new JMenu(Localizer.localize("GefBase", "Experiment"));
		_menubar.add(experiment);
		
		JMenuItem costExp = new JMenuItem("Cost Experimnet");
		experiment.add(costExp);
		costExp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(jMenuItem, "Successfully pressed a menu item");
            	UiGlobals.getNodeRenderManager().computeCostExperiment();
            }
        });
		
		JMenuItem diaExp = new JMenuItem("Diameter Experimnet");
		experiment.add(diaExp);
		diaExp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(jMenuItem, "Successfully pressed a menu item");
            	UiGlobals.getNodeRenderManager().computeDiameterExperiment();
            }
        });
		
		JMenuItem hubCostExp = new JMenuItem("HubCost Experimnet");
		experiment.add(hubCostExp);
		hubCostExp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(jMenuItem, "Successfully pressed a menu item");
            	UiGlobals.getNodeRenderManager().computeHubCostExpExperiment();
            }
        });
		
		JMenuItem plantCostExp = new JMenuItem("PlantCost Experimnet");
		experiment.add(plantCostExp);
		plantCostExp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(jMenuItem, "Successfully pressed a menu item");
            	UiGlobals.getNodeRenderManager().computePlantCostExpExperiment();
            }
        });
		
		
		JMenu stepByStep = new JMenu(Localizer.localize("GefBase", "Step-By-Step"));
		_menubar.add(stepByStep);
		JMenu showCo2Source = new JMenu("1. Show co2 source");
		stepByStep.add(showCo2Source);
		
		ButtonGroup co2SourceGroup = new ButtonGroup();
		
		JRadioButtonMenuItem randomRadio = new JRadioButtonMenuItem("Random Data");
		randomRadio.setSelected(true);
		co2SourceGroup.add(randomRadio);
		showCo2Source.add(randomRadio);
		randomRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(null,
				"Would you reset currently rendered sources?",
				"Reset notification",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
            	
            	if(n == 0){
            		//Go reset
            		doMainProcess(0, 1, null);
            	}
            	else if(n == 1){
            		//Remain
            		HashMap<String, String> params = new HashMap<String, String>();
            		params.put("isResetNode", "false");
            		doMainProcess(0, 1, params);
            	}
            }
		});
		
		
		JRadioButtonMenuItem manualRadio = new JRadioButtonMenuItem("Manual Data");
		co2SourceGroup.add(manualRadio);
		showCo2Source.add(manualRadio);
		manualRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	JOptionPane.showMessageDialog(null,
            		    "Manual Source rendering is not implemented yet.",
            		    "Implementation Error",
            		    JOptionPane.ERROR_MESSAGE);
            	
//            	Object[] options = {"Yes", "No"};
//				int n = JOptionPane.showOptionDialog(null,
//				"Would you reset currently rendered sources?",
//				"Reset notification",
//				JOptionPane.YES_NO_OPTION,
//				JOptionPane.QUESTION_MESSAGE,
//				null,     //do not use a custom Icon
//				options,  //the titles of buttons
//				options[0]); //default button title
//            	
//            	if(n == 0){
//            		//Go reset
//            		doMainProcess(0, 1, null);
//            	}
//            	else if(n == 1){
//            		//Remain
//            		HashMap<String, String> params = new HashMap<String, String>();
//            		params.put("isResetNode", "false");
//            		doMainProcess(0, 1, params);
//            	}
            }
		});
		
//		showCo2Source.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //JOptionPane.showMessageDialog(jMenuItem, "Successfully pressed a menu item");
//            	//UiGlobals.getNodeRenderManager().computeDiameterExperiment();
//            	int dialogResult = JOptionPane.showConfirmDialog (null, "Currently rendered data will be lost. Would you want to proceed?","Warning",JOptionPane.YES_NO_OPTION);
//            	if(dialogResult == JOptionPane.YES_OPTION){
//            		System.out.println("YES!");
//            	}
//            }
//        });
		
		showHub = new JMenu("2. Show Hub");
		//showHub.setEnabled(false);
		stepByStep.add(showHub);
		
		ButtonGroup hubGroup = new ButtonGroup();
		
		JRadioButtonMenuItem cost25Radio = new JRadioButtonMenuItem("cost 25$");
		cost25Radio.setName("25");
		hubGroup.add(cost25Radio);
		showHub.add(cost25Radio);
		cost25Radio.addActionListener(new HubRadioAction());
		
		JRadioButtonMenuItem cost50Radio = new JRadioButtonMenuItem("cost 50$");
		cost50Radio.setName("50");
		hubGroup.add(cost50Radio);
		showHub.add(cost50Radio);
		cost50Radio.addActionListener(new HubRadioAction());
		
		JRadioButtonMenuItem cost75Radio = new JRadioButtonMenuItem("cost 75$");
		cost75Radio.setName("75");
		hubGroup.add(cost75Radio);
		showHub.add(cost75Radio);
		cost75Radio.addActionListener(new HubRadioAction());
		
		JRadioButtonMenuItem cost100Radio = new JRadioButtonMenuItem("cost 100$");
		cost100Radio.setName("100");
		hubGroup.add(cost100Radio);
		showHub.add(cost100Radio);
		cost100Radio.addActionListener(new HubRadioAction());
		
		JRadioButtonMenuItem cost125Radio = new JRadioButtonMenuItem("cost 125$");
		cost125Radio.setName("125");
		hubGroup.add(cost125Radio);
		showHub.add(cost125Radio);
		cost125Radio.addActionListener(new HubRadioAction());
		
		
		
		showStorage = new JMenuItem("3. Show Storage");
		//showStorage.setEnabled(false);
		stepByStep.add(showStorage);
		showStorage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doMainProcess(0, 3, null);
			}
		});
		
		showConnection = new JMenu("4. Show Connection");
		//showConnection.setEnabled(false);
		stepByStep.add(showConnection);
		
		JMenuItem connectionStar = new JMenuItem("Star connection");
		connectionStar.setName("star");
		showConnection.add(connectionStar);
		connectionStar.addActionListener(new ConnectionRadioAction());
		
		JMenuItem connectionTree = new JMenuItem("Tree connection");
		connectionTree.setName("tree");
		showConnection.add(connectionTree);
		connectionTree.addActionListener(new ConnectionRadioAction());
		
		JMenuItem connectionBackbone = new JMenuItem("Backbone connection");
		connectionBackbone.setName("backbone");
		showConnection.add(connectionBackbone);
		connectionBackbone.addActionListener(new ConnectionRadioAction());
		
		JMenuItem connectionHybrid = new JMenuItem("Hybrid connection");
		connectionHybrid.setName("hybrid");
		showConnection.add(connectionHybrid);
		connectionHybrid.addActionListener(new ConnectionRadioAction());
		

	}
	
	public class HubRadioAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			Object[] options = {"Yes", "No"};
			int n = JOptionPane.showOptionDialog(null,
			"Would you see analysis reports?",
			"Report Notification",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,     //do not use a custom Icon
			options,  //the titles of buttons
			options[0]); //default button title
			
			String isolatedSourceCost = ((JRadioButtonMenuItem)e.getSource()).getName();
			Map<String, String> params = new HashMap<String, String>();
			params.put("isolatedSourceCost", isolatedSourceCost);
			
        	if(n == 0){
        		params.put("isShowReports", "Y");
        	}
        	else if(n == 1){
        		params.put("isShowReports", "N");
        	}
			
			doMainProcess(0, 2, params);
		}
	}
	
	public class ConnectionRadioAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String connectionType = ((JMenuItem)e.getSource()).getName();
			Map<String, String> params = new HashMap<String, String>();
			params.put("connectionType", connectionType);
			doMainProcess(0, 4, params);
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
	
	public static void main(String[] artv){
		Class<CCSSourceData> a = CCSSourceData.class;
		for(Method b : a.getMethods()){
			System.out.println(b.getName());
		}
		
		CCSSourceData b = new CCSSourceData();
		try {
			int aa = 10;
			try {
				Method bbb = CCSSourceData.class.getMethod("setIndex", int.class);
				bbb.setAccessible(true);
				try {
					bbb.invoke(b, aa);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("b.index :"+b.getIndex());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
