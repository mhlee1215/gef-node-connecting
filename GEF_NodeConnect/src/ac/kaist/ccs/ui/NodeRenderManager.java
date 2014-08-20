package ac.kaist.ccs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.presentation.JGraph;

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
import ac.kaist.ccs.domain.CCSUtils.*;
import ac.kaist.ccs.main.CCSMain.*;

public class NodeRenderManager {

	private JGraph graph = null;
	private int width = 0;
	private int height = 0;
	public static final int _PADDING = 50;
	Map<Integer, List<Integer> > ccsData;
	List<CCSEdgeData> ccsConData;
	int connectType;
	int costType;
	
	public NodeRenderManager(Map<Integer, List<Integer> > ccsData, List<CCSEdgeData> ccsConData, JGraph graph)
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
		drawNodes(removeExistedNodes, CCSStatics.CONNECT_TYPE_STAR, CCSStatics.COST_TYPE_THE_OGDEN_MODELS);
	}
		
	public void drawNodes(boolean removeExistedNodes, Integer connectType, Integer costType)
	{
		if(connectType != null)
			this.connectType = connectType;
		if(costType != null)
			this.costType = costType;
		
//		{
//			List<Integer> hubList = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
//			for(int i = 0 ; i < hubList.size() ; i++){
//				CCSHubData hubData1 = (CCSHubData) UiGlobals.getNode(hubList.get(i));
//				System.out.println("before laod kkk:"+hubData1.getIndex()+", children:"+hubData1.getChildSources());
//			}
//		}
		
		UiGlobals.loadNodeSnapshot(CCSStatics.STATE_BEFORE_CONNECT);
		
//		{
//			List<Integer> hubList = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
//			for(int i = 0 ; i < hubList.size() ; i++){
//				CCSHubData hubData1 = (CCSHubData) UiGlobals.getNode(hubList.get(i));
//				System.out.println("21kkk:"+hubData1.getIndex()+", children:"+hubData1.getChildSources());
//			}
//		}
		
		if(this.connectType == CCSStatics.CONNECT_TYPE_STAR)
			makeStarCon(UiGlobals.ccsData);
		else if(this.connectType == CCSStatics.CONNECT_TYPE_TREE)
			makeTreeCon(UiGlobals.ccsData);
		else if(this.connectType == CCSStatics.CONNECT_TYPE_BACKBONE)
			makeBackboneCon(UiGlobals.ccsData);
		else if(this.connectType == CCSStatics.CONNECT_TYPE_HYBRID)
			makeHybridCon(UiGlobals.ccsData);
		
		//System.out.println("UiGlobals.ccsData: "+UiGlobals.ccsData);
		computeCo2();
		
		//Connect to plant
		makePlantCon(UiGlobals.ccsData);
		
		computeCostAll(this.costType);
		
		Map<Integer, CCSSourceData> nodesAll = UiGlobals.getNodes();
        for(Integer key : nodesAll.keySet()){
        	if(nodesAll.get(key) != null){
        		nodesAll.get(key).viewType = UiGlobals.viewType;
        	}
        }
		
		
		Editor editor = graph.getEditor();
		
		if(removeExistedNodes)
			editor.getLayerManager().getActiveLayer().removeAll();
		
		//Node load
		//스케일이 바뀔때마다 로드해야 함.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			Map<Integer, List<Integer> > ccsData;
			List<CCSEdgeData> ccsConData;
			JGraph graph;
			
			public void run() {
				// createAndShowGUI();
				//new LoadingProgressBarNode(graph);
				new LoadingWorker(ccsData, ccsConData, graph, UiGlobals.getGrid_scale());
			}
			
			public Runnable init(Map<Integer, List<Integer> > ccsData, List<CCSEdgeData> ccsConData, JGraph graph){
				this.ccsData = ccsData;
				this.ccsConData = ccsConData;
				this.graph = graph;
				return this;
			}
		}.init(ccsData,  ccsConData, graph));
		
		
	}
	
	public void computeCostAll(int costType){
		computeCostAll(costType, 0);
	}
	
	public void computeCostAll(int costType, int co2Type){
		List<CCSSourceData> hubfNodes = UiGlobals.getPlantNodes();
		
		for(CCSSourceData hub : hubfNodes){
			if(co2Type == 0){
				hub.computeCost(costType);
				hub.computeCompressorPumtCost();
			}else{
				hub.computeCost(costType, co2Type);
				hub.computeCompressorPumtCost(co2Type);
			}
			hub.computeCo2();
		}
		
		CCSStatics.updateScalingFactor(CCSSourceData.VIEW_TYPE_CO2);
		
	}
	
	public void computeCo2(){
		List<CCSSourceData> hubfNodes = UiGlobals.getHubNodes();
		
		for(CCSSourceData hub : hubfNodes){
			hub.computeCo2();
		}
		
		CCSStatics.updateScalingFactor(CCSSourceData.VIEW_TYPE_CO2);
		
	}
	
	public void computeCostExperiment(){
		
		
		List<Integer> ccsSourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
		Vector<Vector> rowData = new Vector<Vector>(ccsSourceData.size());
		Map<Integer, Integer> rowIndexMap = new HashMap<Integer, Integer>();
		
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = new Vector();
			row.add(data.getIndex());
			for(int i = 0 ; i < 18 ; i++){
				row.add("N/A");
			}
			rowData.add(row);
			rowIndexMap.put(data.getIndex(), rowData.size()-1);
		}
		
		computeCostAll(CCSStatics.COST_TYPE_1, CCSStatics.CO2_STATE_EXTREME);
		int offset = 1;
		String formatStr = "%.3f";
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			
			//System.out.println(UiGlobals.getNode(data.getIndex()).getExpData());
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			row.set(offset + 3*0, String.format(formatStr, expData.getW_stotal()));
			row.set(offset + 3*1, String.format(formatStr, expData.getN_train()));
			row.set(offset + 3*2, String.format(formatStr, expData.getC_comp()));
			row.set(offset + 3*3, String.format(formatStr, expData.getM_train()));
			row.set(offset + 3*4, String.format(formatStr, expData.getW_p()));
			row.set(offset + 3*5, expData.getN_pump());
			//System.out.println("data.getIndex():"+data.getIndex());
			//rowData.add(row);
		}
		
		computeCostAll(CCSStatics.COST_TYPE_1, CCSStatics.CO2_STATE_HIGH);
		offset = 2;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			
			//System.out.println(UiGlobals.getNode(data.getIndex()).getExpData());
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			row.set(offset + 3*0, String.format(formatStr, expData.getW_stotal()));
			row.set(offset + 3*1, String.format(formatStr, expData.getN_train()));
			row.set(offset + 3*2, String.format(formatStr, expData.getC_comp()));
			row.set(offset + 3*3, String.format(formatStr, expData.getM_train()));
			row.set(offset + 3*4, String.format(formatStr, expData.getW_p()));
			row.set(offset + 3*5, expData.getN_pump());
			//System.out.println("data.getIndex():"+data.getIndex());
			//rowData.add(row);
		}
		computeCostAll(CCSStatics.COST_TYPE_1, CCSStatics.CO2_STATE_LOW);
		offset = 3;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			
			//System.out.println(UiGlobals.getNode(data.getIndex()).getExpData());
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			row.set(offset + 3*0, String.format(formatStr, expData.getW_stotal()));
			row.set(offset + 3*1, String.format(formatStr, expData.getN_train()));
			row.set(offset + 3*2, String.format(formatStr, expData.getC_comp()));
			row.set(offset + 3*3, String.format(formatStr, expData.getM_train()));
			row.set(offset + 3*4, String.format(formatStr, expData.getW_p()));
			row.set(offset + 3*5, expData.getN_pump());
			//System.out.println("data.getIndex():"+data.getIndex());
			//rowData.add(row);
		}
		
		
		
		Vector<String> columnNames = new Vector<String>();
 	    columnNames.addElement("ID");
 	    columnNames.addElement("W_total(EX)");
 	    columnNames.addElement("W_total(HI)");
 	    columnNames.addElement("W_total(LOW)");
 	    columnNames.addElement("N_train(EX)");
	    columnNames.addElement("N_train(HI)");
	    columnNames.addElement("N_train(LOW)");
	    columnNames.addElement("C_comp(EX)");
 	    columnNames.addElement("C_comp(HI)");
 	    columnNames.addElement("C_comp(LOW)");
 	    columnNames.addElement("m_train(EX)");
	    columnNames.addElement("m_train(HI)");
	    columnNames.addElement("m_train(LOW)");
	    columnNames.addElement("W_p(EX)");
 	    columnNames.addElement("W_p(HI)");
 	    columnNames.addElement("W_p(LOW)");
 	    columnNames.addElement("N_pump(EX)");
	    columnNames.addElement("N_pump(HI)");
	    columnNames.addElement("N_pump(LOW)");
 	    
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
	
	public void computeDiameterExperiment(){
		
		List<Integer> ccsSourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
		Vector<Vector> rowData = new Vector<Vector>(ccsSourceData.size());
		Map<Integer, Integer> rowIndexMap = new HashMap<Integer, Integer>();
		
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = new Vector();
			row.add(data.getIndex());
			for(int i = 0 ; i < 6 ; i++){
				row.add("N/A");
			}
			rowData.add(row);
			rowIndexMap.put(data.getIndex(), rowData.size()-1);
		}
		
		
		
		System.out.println("Compute Type 1");
		computeCostAll(CCSStatics.COST_TYPE_1);
		String formatStr = "%.3f";
		int offset = 1;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		
		System.out.println("Compute Type 2");
		computeCostAll(CCSStatics.COST_TYPE_2);
		offset = 2;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		System.out.println("Compute Type 3");
		computeCostAll(CCSStatics.COST_TYPE_3);
		offset = 3;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		System.out.println("Compute Type 4");
		computeCostAll(CCSStatics.COST_TYPE_4);
		offset = 4;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		System.out.println("Compute Type 5");
		computeCostAll(CCSStatics.COST_TYPE_5);
		offset = 5;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		System.out.println("Compute Type 6");
		computeCostAll(CCSStatics.COST_TYPE_6);
		offset = 6;
		for(Integer index : ccsSourceData){
			CCSSourceData data = UiGlobals.getNode(index);
			Vector row = rowData.get(rowIndexMap.get(data.getIndex()));
			CCSExpData expData = UiGlobals.getNode(data.getIndex()).getExpData();
			String diameterStr = String.format(formatStr, expData.getPipe_diameter()).toString();
			row.set(offset, diameterStr);
		}
		
		Vector<String> columnNames = new Vector<String>();
 	    columnNames.addElement("ID");
 	    columnNames.addElement("1. The Ogden Models");
 	    columnNames.addElement("2. MIT Model");
 	    columnNames.addElement("3. Ecofys Model");
 	    columnNames.addElement("4. IEA GHG PH4/6");
	    columnNames.addElement("5. IEA GHG 2005/2");
	    columnNames.addElement("6.IEA GHG 2005/3");
 	    
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
	    
	    cm.getColumn(0).setPreferredWidth(70);


	    
	    table.getTableHeader().setForeground(Color.white);
	    table.getTableHeader().setBackground(CustomCellRenderer.headerBG);
	    
		
		JScrollPane scroll = new JScrollPane( table );
		
		JPanel m = new JPanel();
		m.setLayout(new BorderLayout());
		m.add("Center", scroll);
		
		JFrame frame = new JFrame("Pipe Diameter Result Table");
			    
		frame.getContentPane().add( m );
		frame.setVisible(true);
		frame.setSize( 800, 500 ); 
	}
	
	public List<CCSEdgeData> makeNaiveCon(
			Map<Integer, List<CCSSourceData>> ccsData) {
		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		List<CCSSourceData> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int i = 0; i < sourceData.size(); i++) {

			CCSSourceData curSrc = sourceData.get(i);
			CCSSourceData minDistHub = null;
			double minDist = 999999999;

			for (int j = 0; j < hubData.size(); j++) {

				CCSSourceData curHub = hubData.get(j);

				double curDist = CCSUtils.dist(curSrc, curHub);
				if (minDist > curDist) {
					minDist = curDist;
					minDistHub = curHub;
				}
			}

			CCSEdgeData conData = curSrc.connectTo(minDistHub);
			ccsConData.add(conData);
		}

		return ccsConData;
	}

	public Map<Integer, List<Integer>> Clustering(
			Map<Integer, List<Integer>> ccsData) {
		// List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		List<Integer> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<Integer> hubData = ccsData.get(CCSSourceData.TYPE_HUB);
		
		System.out.println("SOURCE:"+sourceData);
		System.out.println("HUB:"+hubData);

		for (int i = 0; i < sourceData.size(); i++) {
			CCSSourceData curSrc = UiGlobals.getNode(sourceData.get(i));

			CCSHubData minRankHub = null;
			double minDist = 999999999;
			double minRank = 999999999;

			System.out.println("hubData.size:"+hubData.size());
			for (int j = 0; j < hubData.size(); j++) {
				//System.out.println("bbbb:"+UiGlobals.getNode(hubData.get(j)));
				CCSHubData curHub = (CCSHubData) UiGlobals.getNode(hubData.get(j));

				// System.out.println(curHub);
				double curDist = CCSUtils.dist(curSrc, curHub);
				float curRank = (float) (curSrc.getCo2_amount() / minDist);
				curSrc.setRank(curRank);
				if (minDist > curDist) {
					minDist = curDist;
					minRank = curRank;
					minRankHub = curHub;
				}
			}

			//System.out.println("minDist: "+minDist+",minRankHub.getRange(): "+minRankHub.getRange());
			if (minDist <= minRankHub.getRange()) {
				curSrc.setClusterHub(minRankHub);
			}
		}

		//System.out.println("sourceData.size(): "+sourceData.size());
		for (int i = 0; i < sourceData.size(); i++) {

			CCSSourceData curSrc = UiGlobals.getNode(sourceData.get(i));
			if (curSrc.getClusterHub() != null) {
				
				if (curSrc.getClusterHub().getType() == CCSNodeData.TYPE_HUB) {
					//System.out.println("??"+curSrc.getClusterHub().getType());
					((CCSHubData) curSrc.getClusterHub()).addClusterSource(curSrc
							.getIndex());
					// ((CCSHubData)curSrc.getHub()).getChildSources().add(curSrc);
				}
			}
		}

		//System.out.println("ccsData CLuster: "+ccsData.get(CCSNodeData.TYPE_HUB));
		int sum = 0;
		for(Integer hIndex : ccsData.get(CCSNodeData.TYPE_HUB)){
			sum += ((CCSHubData)UiGlobals.getNode(hIndex)).getChildSources().size();
		}
		System.out.println(sum);
		return ccsData;
	}

	public List<CCSEdgeData> makePlantCon(Map<Integer, List<Integer>> ccsData){
		
		List<Integer> hubData = ccsData.get(CCSSourceData.TYPE_HUB);
		for(int i = 0 ; i < hubData.size() ; i++)
			System.out.println("a::"+(CCSHubData)UiGlobals.getNode(hubData.get(i))+">>");
		List<Integer> plantData = ccsData.get(CCSSourceData.TYPE_PLANT);
		if(plantData == null) return null;
		
		List<SortTuple> hubSortList = new ArrayList<SortTuple>();
		for(Integer index : hubData){
			CCSSourceData data = UiGlobals.getNode(index);
			hubSortList.add(new SortTuple(data.getAcc_co2_amount(), data.getIndex(), 0));
		}
		
		for(Integer index : plantData){
			CCSSourceData data = UiGlobals.getNode(index);
			System.out.println(data);
		}
		
		Collections.sort(hubSortList, new NoDecCompare());
		int hubCnt = 0;
		List<Integer> connectedHub = new ArrayList<Integer>();
		
		for(SortTuple data : hubSortList){
			
			//Connect to plant directly
			if(hubCnt < 5){
				System.out.println("Add to plant!!!!.. index:"+data.firstId);
				CCSHubData curHub = (CCSHubData) UiGlobals.getNode((int) data.firstId);
				CCSPlantData closestPlant = (CCSPlantData) getClosestPoint(curHub.getIndex(), plantData);
				System.out.println(closestPlant.getNodeType());
				curHub.connectTo(closestPlant);	
				connectedHub.add(curHub.getIndex());
			}
			//Connect to other hub
			else{
				System.out.println("Add to another hub!!!!.. index:"+data.firstId);
				CCSHubData curHub = (CCSHubData) UiGlobals.getNode((int) data.firstId);
				CCSHubData closestPlant = (CCSHubData) getClosestPoint(curHub.getIndex(), connectedHub);
				curHub.connectTo(closestPlant);	
				connectedHub.add(curHub.getIndex());
			}
			hubCnt++;
		}
		
		
		return null;
	}
	
	public List<CCSEdgeData> makeStarCon(
			Map<Integer, List<Integer>> ccsData) {
		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();
		List<Integer> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);

		{
			List<Integer> hubList = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
			for(int i = 0 ; i < hubList.size() ; i++){
				CCSHubData hubData = (CCSHubData) UiGlobals.getNode(hubList.get(i));
				System.out.println("11kkk:"+hubData.getIndex()+", children:"+hubData.getChildSources());
			}
		}
		
		for (int i = 0; i < sourceData.size(); i++) {
			CCSSourceData curSrc = UiGlobals.getNode(sourceData.get(i));
			//System.out.println("curSrc.getClusterHub(): "+curSrc.getClusterHub());
			if (curSrc.getClusterHub() != null) {
				//System.out.println("Connect!"+curSrc.getIndex()+","+curSrc.getClusterHub().getIndex());
				curSrc.connectTo(curSrc.getClusterHub());
			}
		}
		
		{
			List<Integer> hubList = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
			for(int i = 0 ; i < hubList.size() ; i++){
				CCSHubData hubData = (CCSHubData) UiGlobals.getNode(hubList.get(i));
				System.out.println("22kkk:"+hubData.getIndex()+", children:"+hubData.getChildSources());
			}
		}

		int a = 1;
		//System.out.println("11ccsData: "+ccsData);
		
		return ccsConData;
	}

	public List<CCSEdgeData> makeTreeCon(
			Map<Integer, List<Integer>> ccsDataParam) {
		Map<Integer, List<Integer>> ccsData = Clustering(ccsDataParam);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<Integer> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		System.out.println("tree con hubData("+hubData.size()+"): "+hubData);
		
		{
			List<Integer> hubList = ccsData.get(CCSNodeData.TYPE_HUB);
			for(int i = 0 ; i < hubList.size() ; i++){
				CCSHubData hubData1 = (CCSHubData) UiGlobals.getNode(hubList.get(i));
				System.out.println("11kkk:"+hubData1.getIndex()+", children:"+hubData1.getClusterSources());
			}
		}
		
		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) UiGlobals.getNode(hubData.get(j));

			List<Integer> childIndexList = new ArrayList<Integer>(curHub.getClusterSources());
			List<Integer> connedtedList = new ArrayList<Integer>();
			connedtedList.add(curHub.getIndex());
			//System.out.println("connedtedList("+connedtedList.size()+"):"+connedtedList);
			System.out.println("childIndexList("+childIndexList.size()+"):"+childIndexList);
			// Add until remained node list is empty
			for (int ii = 0; ii < childIndexList.size();) {

				NodePair closestPair = getClosestPair(childIndexList,
						connedtedList);
				CCSSourceData minDistSrc = closestPair.first;
				CCSSourceData minDistConnectedSrc = closestPair.second;
				if(minDistSrc == null) System.out.println("NULL!!!");
				if (minDistSrc != null){
					minDistSrc.connectTo(minDistConnectedSrc);
					//System.out.println("Connect to :"+minDistConnectedSrc);
				}

				childIndexList.remove((Integer) minDistSrc.getIndex());
				connedtedList.add(minDistSrc.getIndex());
				
			}
		}
		
		{
			List<Integer> hubList = UiGlobals.ccsData.get(CCSNodeData.TYPE_HUB);
			for(int i = 0 ; i < hubList.size() ; i++){
				CCSHubData hubData1 = (CCSHubData) UiGlobals.getNode(hubList.get(i));
				System.out.println("21kkk:"+hubData1.getIndex()+", children:"+hubData1.getChildSources());
			}
		}

		return ccsConData;
	}

	public NodePair getClosestPair(List<Integer> firstIDList,
			List<Integer> secondIDList) {

		CCSSourceData minDistSrc = null;
		CCSSourceData minDistConnectedSrc = null;
		double minDist = 9999999;

		//System.out.println("firstIDList: "+firstIDList+", secondIDList:"+secondIDList);
		for (int i = 0; i < firstIDList.size(); i++) {
			int childIndex = firstIDList.get(i);
			CCSSourceData curSrc = UiGlobals.getNode(childIndex);
			//System.out.println("curSrc:"+curSrc.getIndex()+", childIndex:"+childIndex);

			for (int k = 0; k < secondIDList.size(); k++) {
				int connectedIndex = secondIDList.get(k);
				CCSSourceData curConnected = UiGlobals.getNode(connectedIndex);

				double curDist = CCSUtils.dist(curSrc, curConnected);
				if (minDist > curDist) {
					minDist = curDist;
					minDistSrc = curSrc;
					minDistConnectedSrc = curConnected;
				}
			}
		}

		return new NodePair(minDistSrc, minDistConnectedSrc);
	}
	
	public CCSSourceData getClosestPoint(Integer srcIdx,
			List<Integer> secondIDList) {

		CCSSourceData minDistSrc = null;
		// minDistConnectedSrc = null;
		double minDist = 9999999;

		//System.out.println("firstIDList: "+firstIDList+", secondIDList:"+secondIDList);
		CCSSourceData curSrc = UiGlobals.getNode(srcIdx);
		//System.out.println("curSrc:"+curSrc.getIndex()+", childIndex:"+childIndex);

		for (int k = 0; k < secondIDList.size(); k++) {
			int connectedIndex = secondIDList.get(k);
			CCSSourceData curConnected = UiGlobals.getNode(connectedIndex);

			double curDist = CCSUtils.dist(curSrc, curConnected);
			if (minDist > curDist) {
				minDist = curDist;
				minDistSrc = curConnected;
				//minDistConnectedSrc = curConnected;
			}
		}

		return minDistSrc;
	}
	
	public CCSSourceData getClosestPoint(CCSSourceData curSrc,
			List<CCSSourceData> secondIDList) {

		//CCSSourceData minDistSrc = null;
		CCSSourceData minDistConnectedSrc = null;
		double minDist = 9999999;

		for(CCSSourceData curConnected : secondIDList){

			double curDist = CCSUtils.dist(curSrc, curConnected);
			if (minDist > curDist) {
				minDist = curDist;
				//minDistSrc = curSrc;
				minDistConnectedSrc = curConnected;
			}
		}

		return minDistConnectedSrc;
	}

	 public static void main(String[] argv){
		 double D = 10;
		 float lou = 10;
		 float m = 1000;
		 float L = 20;
		 float mu = 10;
		 float p_outlet = 14;
		 
		 for (int i = 0 ; i < 10 ; i++){
			 double Re =  ((4* (1000 / 24*3600*0.0254)) * (m / Math.PI * mu * D)); 
			 double F_f = (1 / (4 * Math.pow(-1.8 * Math.log(6.91 / Re + (12*(0.00015 / D)/3.7)), 2)));
			 D = (1 / 0.0254) * 
					 Math.pow(
							 (32*F_f*m*m)*(
									 (Math.pow((1000 / (double)(24*3600)), 2))/
									 (Math.PI*Math.PI*lou*((p_outlet - 0.1) / L)*(Math.pow(10, 6)/1000)))
					 , 0.2);
			 System.out.println("D:"+D+", RE:"+Re+", F_f:"+F_f);
			 //System.out.println((Math.pow((1000 / (double)(24*3600)), 2)));
		 }
	 }

	public float getAngle(CCSSourceData hub, CCSSourceData ref,
			CCSSourceData newNode) {
		int x1 = ref.getX() - hub.getX();
		int y1 = ref.getY() - hub.getY();
		int x2 = newNode.getX() - hub.getX();
		int y2 = newNode.getY() - hub.getY();

		return getAngle(x1, y1, x2, y2);
	}

	public float getAngle(int x1, int y1, int x2, int y2) {

		int x_sign = x2 - x1;
		int y_sign = y2 - y1;

		int x_usign = Math.abs(x_sign);
		int y_usign = Math.abs(y_sign);

		float radian = (float) (Math.atan(y_usign / (float) x_usign) * 180 / Math.PI);

		if (x_sign >= 0 && y_sign >= 0) {
			// 1st quadrant
			// Do nothing
		} else if (x_sign < 0 && y_sign >= 0) {
			// 2nd quadrant
			radian = 180 - radian;

		} else if (x_sign < 0 && y_sign < 0) {
			// 3rd quadrant
			radian += 180;
		} else if (x_sign >= 0 && y_sign < 0) {
			// 4th quadrant
			radian = 360 - radian;
		}

		return radian;
	}

	public List<CCSEdgeData> makeBackboneCon(
			Map<Integer, List<Integer>> ccsDataParam) {
		Map<Integer, List<Integer> > ccsData = Clustering(ccsDataParam);

		//List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<Integer> hubData = ccsData.get(CCSSourceData.TYPE_HUB);
		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) UiGlobals.getNode(hubData.get(j));
			List<Integer> childIndexList = new ArrayList<Integer>(curHub.getClusterSources());

			// Grouping by angle = n pieces;
			float angleNum = 6;
			float anglePerPie = 360 / angleNum;

			int hx = curHub.getX();
			int hy = curHub.getY();
			Map<Integer, List<Integer>> pieBinMap = new HashMap<Integer, List<Integer>>();
			for (int i = 0; i < angleNum; i++) {
				pieBinMap.put(i, new ArrayList<Integer>());
			}

			for (int i = 0; i < childIndexList.size(); i++) {
				int childIndex = childIndexList.get(i);
				CCSSourceData curSrc = UiGlobals.getNode(childIndex);

				int cx = curSrc.getX();
				int cy = curSrc.getY();

				float angle = getAngle(hx, hy, cx, cy);
				int pieBin = (int) Math.floor(angle / anglePerPie);

				pieBinMap.get(pieBin).add(childIndex);
			}

			for (int i = 0; i < angleNum; i++) {
				List<Integer> binNodeList = pieBinMap.get(i);

				CCSSourceData maxDistSrc = null;
				double maxDist = 0;
				// Find node with maximum distance
				for (int k = 0; k < binNodeList.size(); k++) {
					int childIndex = binNodeList.get(k);
					CCSSourceData curSrc = UiGlobals.getNode(childIndex);

					double curDist = CCSUtils.dist(curSrc, curHub);
					if (maxDist < curDist) {
						maxDist = curDist;
						maxDistSrc = curSrc;
					}
				}

				// CCSEdgeData conData = new CCSEdgeData(maxDistSrc, curHub);
				// ccsConData.add(conData);
				// if(maxDistSrc != null)
				// maxDistSrc.connectTo(curHub);

				// Connect arthogonal to connection between hub to maximum
				// distance node.
				if (maxDistSrc != null) {
					maxDistSrc.connectTo(curHub);
					// System.out.println("maxDistSrc: " + maxDistSrc);
					binNodeList.remove((Integer) maxDistSrc.getIndex());

					// float distMax = maxDist;
					// double distMax = maxDist;
					// int mx = maxDistSrc.getX() - curHub.getX();
					// int my = maxDistSrc.getY() - curHub.getY();

					List<SortTuple> nodePairList = new ArrayList<SortTuple>();

					for (int k = 0; k < binNodeList.size(); k++) {
						int childIndex = binNodeList.get(k);
						CCSSourceData curSrc = UiGlobals.getNode(childIndex);

						// int cx = curSrc.getX() - curHub.getX();
						// int cy = curSrc.getY() - curHub.getY();
						//
						// double lengthToOrth = (mx*cx+my*cy) / distMax;
						//
						// int ox = (int)(lengthToOrth * mx / distMax);
						// int oy = (int)(lengthToOrth * my / distMax);

						// CCSSourceData node = new
						// CCSJointData(curHub.getX()+ox, curHub.getY()+oy);

						CCSSourceData node = getProjectionPoint(curHub,
								maxDistSrc, curSrc);

						UiGlobals.addNode(node);
						//node.setIndex(jointNodeIndex);

						// maxDistSrc : 가장 먼 노드 (이미 연결되있음)
						// node : joint 노드
						// curSrc : 연결할 새로운 노드
						// curHub : 현재 허브 노드
						jointConnection(maxDistSrc, node, curSrc, curHub);
					}
				}
			}
		}

		return null;
	}

	// Joint connection
	// joint node가 들어갈 자리의 양 옆 노드의 연결 사이에 joint를 집어넣는다.
	public void jointConnection(CCSSourceData ref, CCSSourceData joint,
			CCSSourceData newNode, CCSSourceData hub) {
		CCSSourceData justBeforeJoint = ref;

		while (justBeforeJoint.getDst() != null) {
			if(ref.getDst().getIndex() == justBeforeJoint.getDst().getIndex()) break;
			if (CCSUtils.dist(justBeforeJoint.getDst(), hub) < CCSUtils.dist(joint, hub))
				break;
			justBeforeJoint = justBeforeJoint.getDst();
			//System.out.println("HUL>..."+justBeforeJoint.getIndex()+", D1:"+dist(justBeforeJoint.getDst(), hub)+", D2"+dist(joint, hub));
		}

		justBeforeJoint.getDst().getChildSources().remove((Integer)justBeforeJoint.getIndex());
		joint.connectTo(justBeforeJoint.getDst());
		justBeforeJoint.connectTo(joint);
		newNode.connectTo(joint);
	}
	
	

	public CCSSourceData getProjectionPoint(CCSSourceData hub,
			CCSSourceData ref, CCSSourceData newNode) {

		double distToRef = CCSUtils.dist(hub, ref);
		double distToNew = CCSUtils.dist(hub, newNode);

		if (distToRef < distToNew)
			return null;

		int mx = ref.getX() - hub.getX();
		int my = ref.getY() - hub.getY();

		int cx = newNode.getX() - hub.getX();
		int cy = newNode.getY() - hub.getY();

		double distMax = CCSUtils.dist(ref, hub);
		double lengthToOrth = (mx * cx + my * cy) / distMax;
		
		if(lengthToOrth < 0) return null;

		int ox = (int) (lengthToOrth * mx / distMax);
		int oy = (int) (lengthToOrth * my / distMax);

		CCSSourceData node = new CCSJointData(hub.getX() + ox, hub.getY() + oy);
		
		double distToJoint = CCSUtils.dist(hub, node);
		if(distToJoint > distToRef)
			return null;
		else
			return node;
	}

	

	public List<CCSEdgeData> makeHybridCon(
			Map<Integer, List<Integer>> ccsData) {

		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<Integer> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) UiGlobals.getNode(hubData.get(j));
			List<CCSSourceData> connectedData = new ArrayList<CCSSourceData>();
			connectedData.add(curHub);

			List<Integer> childIndexList = new ArrayList<Integer>(curHub.getClusterSources());
			if(childIndexList.size() == 0) continue;
			
			List<SortTuple> nodesSortByRank = new ArrayList<SortTuple>();
			
			

			// Get Nodes with rank with descending order
			for (int i = 0; i < childIndexList.size(); i++) {
				int childIndex = childIndexList.get(i);
				CCSSourceData curSrc = UiGlobals.getNode(childIndex);
				nodesSortByRank.add(new SortTuple(getRank(curHub, curSrc),
						childIndex, childIndex));
			}

			Collections.sort(nodesSortByRank, new NoDecCompare());

			
			//Add Highest rank node
			CCSSourceData highestRankSrc = UiGlobals.getNode((int) nodesSortByRank.get(0).firstId);
			connectedData.add(highestRankSrc);
			highestRankSrc.connectTo(curHub);
			
			// Add from high rank node
			// Actual adding routine.
			for (int i = 1; i < nodesSortByRank.size(); i++) {
				int highRankNodeId = (int) nodesSortByRank.get(i).firstId;
				CCSSourceData curSrc = UiGlobals.getNode(highRankNodeId);

				//Get shortest length to connected node list
				CCSSourceData closestPoint = getClosestPoint(curSrc, connectedData);
				//Get shortest length to projected node list
				CCSSourceData possibleJoint = getProjectionPoint(curHub, closestPoint, curSrc);
				if(possibleJoint == null){
					curSrc.connectTo(closestPoint);
				}else{
					double distToClosest = CCSUtils.dist(closestPoint, curSrc);
					double distToJoint = CCSUtils.dist(possibleJoint, curSrc);
					if(distToClosest < distToJoint){
						//System.out.println("curSrc:"+curSrc+", closestPoint:"+closestPoint);
						curSrc.connectTo(closestPoint);
					}else{
						UiGlobals.addNode(possibleJoint);

						// maxDistSrc : 가장 먼 노드 (이미 연결되있음)
						// node : joint 노드
						// curSrc : 연결할 새로운 노드
						// curHub : 현재 허브 노드
						jointConnection(closestPoint, possibleJoint, curSrc, closestPoint.getDst());
					}
				}
				
				connectedData.add(curSrc);
			}

		}

		// Need to implement

		// Sort by Rank

		return ccsConData;
	}

	public double getRank(CCSSourceData hub, CCSSourceData ref) {
		return ref.getCo2_amount() / CCSUtils.dist(hub, ref);
	}

	
}
