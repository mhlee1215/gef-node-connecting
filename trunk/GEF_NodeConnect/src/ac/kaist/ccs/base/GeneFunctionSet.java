package ac.kaist.ccs.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdesktop.swingx.JXPanel;
import org.kaist.ccs.fig.FigCCSNode;
import org.kaist.ie.presentation.CustomCellRenderer1;
import org.kaist.ie.presentation.JGridChartPanel;
import org.kaist.ie.presentation.JNodeInfoTableModel;
import org.kaist.ie.presentation.MultiLineCellRenderer;
import org.kaist.ie.presentation.MultiLineHeaderRenderer;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.presentation.Fig;

//import org.ssu.ml.presentation.

public class GeneFunctionSet extends JFrame implements Runnable, ActionListener, MouseListener {
	
	static final Color COLOR_STANDBY = new Color(209, 209, 209);
	static final Color COLOR_EMPTY = new Color(247, 247, 247);
	static final Color COLOR_FINISH = new Color(196, 222, 255);
	static final Color COLOR_FINISH_MAX = new Color(7, 118, 255);
	static final Color COLOR_PROCESSING = new Color(255, 96, 43);
	static final Color COLOR_SELECTED = new Color(220, 96, 43);
	
	static final int VALUE_STANDBY = 1;
	static final int VALUE_EMPTY = 2;
	static final int VALUE_FINISH = 3;
	static final int VALUE_PROCESSING = 4;
	
	BigInteger[] binomA = null;
	BigInteger[] binomB = null;
	BigInteger[] binomC = null;
	
	Double[] logMap = null;
	
	
	
	List<String> geneIds = new Vector<String>();
	HashMap<String, HashMap<Integer, List<String>>> functionUniverse = null;
	
	int maxGridX = 0;
	int maxGridY = 0;
	HashMap<Point, HashMap<Integer, List<String>>> gridFuncData = null;
	HashMap<Point, List<String>> gridGeneSetData = null;
	
	List<Fig> nodes = null;
	int interval_space = 0;
    int xOffset = 0;
    int yOffset = 0;

    JFrame main = this;
	JXPanel mainPanel = null;
	JXPanel resultPanel = null;
	JScrollPane scrollPane = null;
	JTextField filterText = null;
	JTextField thresholdText = null;
	double thresholdValue = 0.05;
	
	JComboBox heightCombo = null;
	int heightValue = 20;
	
	private JComboBox filterColumn;
	private TableRowSorter<JNodeInfoTableModel> sorter;
	SortedListForFunctionData[][][] resultMap = null;
	JTable nodeTable = null;
	
	
	int[][] process = null;
	JXPanel[][] processPanel = null;
	Color[][] processPanelColor = null;
	
	int maxColumnCnt = 0;//UiGlobals.getAnnotationHeader().size();
	
	Vector<String> columnData = null;
	Object[][] dataFilled = null;
	
	int largestFuncListSize = 0;
	
	JFrame tableFrame = null;
	
	
	int testCount = 0;
	
	Vector<HashMap<String, Integer>> nSearchHash = new Vector<HashMap<String, Integer>>();
	
	HashMap<String, HashMap<String, Integer>> preCalFunctionData = null;
	HashMap<String, Integer> tableIndexMap = null;
	
	public GeneFunctionSet(HashMap<String, HashMap<Integer, List<String>>> functionUniverse){
		this.functionUniverse = functionUniverse;
		Set<String> keys = functionUniverse.keySet();
		for(String id : keys){
			geneIds.add(id);
		}
		
		int N = functionUniverse.keySet().size();
		preLogCal(N);
		
		
		setSize(new Dimension(400, 400));
	}
	
	public void paint(Graphics g){
		//System.out.println("paint, size: "+this.getSize());
		super.paint(g);
		mainPanel.removeAll();
		//System.out.println("P: "+process.length+", "+process[0].length);
		for(int i = 0 ; i <= maxGridY ; i++){
			for(int j = 0 ; j <= maxGridX ; j++){
				//JXPanel panel = processPanel[i][j];
				
				if(process[i][j] == VALUE_FINISH){
					processPanel[i][j].setBackground(processPanelColor[i][j]);
				}
				else if(process[i][j] == VALUE_STANDBY){
					processPanel[i][j].setBackground(COLOR_STANDBY);
					processPanelColor[i][j] = COLOR_STANDBY;
				}
				else if(process[i][j] == VALUE_EMPTY){
					processPanel[i][j].setBackground(COLOR_EMPTY);
					processPanelColor[i][j] = COLOR_EMPTY;
				}
				else if(process[i][j] == VALUE_PROCESSING){
					processPanel[i][j].setBackground(COLOR_PROCESSING);
					processPanelColor[i][j] = COLOR_PROCESSING;
				}
				
				processPanel[i][j].setPreferredSize(new Dimension(this.getSize().width/maxGridX, this.getSize().height/maxGridY));
    			//System.out.println("add.. i: "+i+", j: "+j);
    			if(j+1 > maxGridX )
    				mainPanel.add(processPanel[i][j], "wrap, width 1::, height 1::");
    			else
    				mainPanel.add(processPanel[i][j], "width 1::, height 1::");
			}
		}
	}
	
	public void run(){
		init();
		setVisible(false);
		setVisible(true);
		invalidate();
		mainPanel.revalidate();
		
		long start = System.currentTimeMillis();
		int n = this.getContainAttributeGeneSize(1, "transport");
		//int qSize = data.size();
		/*for(int i = 0 ; i < 50 ; i++){
			System.out.println("itor["+i+"]");
			getAdjPValue(data, qSize, 1, "transport", n);
		}*/
		
		
		for(int i = 1 ; i < maxColumnCnt ; i++)
			genGeneFunctionByGrid(i);
		
		getGeneSetByGrid();
		int columnIndex = 1;
		int itorCnt = 0;
		
		System.out.println("maxGridX: "+maxGridX);
		System.out.println("maxGridY: "+maxGridY);
		
		int filledGridCnt = 0;
		for(int i = 0 ; i <= maxGridY ; i++){
         	for(int j = 0 ; j <= maxGridX ; j++){
         		//ID 모음
         		List<String> curGeneSet = gridGeneSetData.get(new Point(j, i));
         		//Ontology 모음
         		HashMap<Integer, List<String>> curFuncSet = gridFuncData.get(new Point(j, i));
         		if(curGeneSet != null && curFuncSet != null)
         		{
         			List<String> funcList = curFuncSet.get(columnIndex);
         			if(funcList != null){
         				filledGridCnt++;
         				
         			}else{
         			}
         		}else{
         		}
         	}
         }
		
		int calculatedGridCnt = 0;
		
		int maxGeneSetSize = 0;
		int minGeneSetSize = 10000000;
		
		for(int i = 0 ; i <= maxGridY ; i++){
         	for(int j = 0 ; j <= maxGridX ; j++){
         		List<String> curGeneSet = gridGeneSetData.get(new Point(j, i));
         		if(curGeneSet != null){
         		maxGeneSetSize = Math.max(maxGeneSetSize, curGeneSet.size());
         		if(curGeneSet.size() > 0)
         			minGeneSetSize = Math.min(minGeneSetSize, curGeneSet.size());
         		}
         	}
		}
		
		largestFuncListSize = 0;
		for(int i = 0 ; i <= maxGridY ; i++){
         	for(int j = 0 ; j <= maxGridX ; j++){
         		//System.out.println("GRID: "+i+", "+j);
         		List<String> curGeneSet = gridGeneSetData.get(new Point(j, i));
         		
         		HashMap<Integer, List<String>> curFuncSet = gridFuncData.get(new Point(j, i));
         		if(curGeneSet != null && curFuncSet != null)
         		{
         			calculatedGridCnt++;
         			for(int l = 1 ; l < maxColumnCnt ; l++){
	         			List<String> funcList = curFuncSet.get(l);
	         			
	         			if(funcList != null){
	         				largestFuncListSize = Math.max(largestFuncListSize, funcList.size());
	         				
	         				process[i][j] = VALUE_PROCESSING;
	         				processPanel[i][j].setBackground(COLOR_PROCESSING);
	         				processPanelColor[i][j] = COLOR_PROCESSING;
	         				
	         				//this.repaint();
	         				//mainPanel.getComponent(i*(maxGridX+1)+j).setBackground(Color.blue);
	         				if(j == 13 && i == 0)
	         					System.out.println("funcList["+l+"]: "+funcList.size());
	         				for(int k = 0 ; k < funcList.size() ; k++){
	         					n = this.getContainAttributeGeneSize(l, funcList.get(k));
	         					double pvalue = getAdjPValue(curGeneSet, curGeneSet.size(), l, funcList.get(k), n);
	         					
	         					resultMap[i][j][l-1].add(new CFunctionData(funcList.get(k), pvalue));
	         					itorCnt++;
	         					
	         				}
	         				process[i][j] = VALUE_FINISH;
	         				
	         				
	         				int curGeneSize = curGeneSet.size();
	         				//System.out.println("maxGeneSetSize-minGeneSetSize: "+maxGeneSetSize+", "+minGeneSetSize);
	         				//System.out.println("curGeneSize:"+curGeneSize+", ratio: "+(((double)(curGeneSize-minGeneSetSize)) / (maxGeneSetSize-minGeneSetSize)));
	         				int colorR = COLOR_FINISH.getRed();
	         				//System.out.println("R VAR: "+(COLOR_FINISH_MAX.getRed() - COLOR_FINISH.getRed())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize)));
	         				colorR += (COLOR_FINISH_MAX.getRed() - COLOR_FINISH.getRed())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize)); 
	         				int colorG = COLOR_FINISH.getGreen();
	         				//System.out.println("G VAR: "+(COLOR_FINISH_MAX.getGreen() - COLOR_FINISH.getGreen())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize)));
	         				colorG += (COLOR_FINISH_MAX.getGreen() - COLOR_FINISH.getGreen())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize));
	         				int colorB = COLOR_FINISH.getBlue();
	         				//System.out.println("B VAR: "+(COLOR_FINISH_MAX.getBlue() - COLOR_FINISH.getBlue())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize)));
	         				colorB += (COLOR_FINISH_MAX.getBlue() - COLOR_FINISH.getBlue())*((curGeneSize-minGeneSetSize)*0.1*10/(maxGeneSetSize-minGeneSetSize));
	         				
	         				//System.out.println(colorR+" "+colorG+" "+colorB);
	         				Color finishColor = new Color(colorR, colorG, colorB);
	         				
	         				processPanel[i][j].setBackground(finishColor);
	         				processPanelColor[i][j] = finishColor;
	         			}else{
	         				//System.out.print("X");
	         				process[i][j] = VALUE_EMPTY;
	         				//mainPanel.getComponent(i*maxGridX+1+j).setBackground(Color.white);
	         				processPanel[i][j].setBackground(COLOR_EMPTY);
	         				processPanelColor[i][j] = COLOR_EMPTY;
	         			}
         			}
         		}else{
         			
         			//System.out.print("X");
         			process[i][j] = VALUE_EMPTY;
         			//mainPanel.getComponent(i*maxGridX+1+j).setBackground(Color.white);
         			processPanel[i][j].setBackground(COLOR_EMPTY);
         			processPanelColor[i][j] = COLOR_EMPTY;
         		}
         		
         		String dots = "";
             	if(calculatedGridCnt%3 == 0) dots = ".  ";
             	else if(calculatedGridCnt%3 == 1) dots = ".. ";
             	else if(calculatedGridCnt%3 == 2) dots = "...";
             	this.setTitle("Processing"+dots+" "+String.format("%.2f", (calculatedGridCnt*0.1*1000/filledGridCnt))+"%, Grid cnt: "+calculatedGridCnt+"/"+filledGridCnt);
         	}
         	
         	
         	//System.out.println(" validCnt: "+calculatedGridCnt+"/"+filledGridCnt+", itorCnt: "+itorCnt);
         }
		
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		start = System.currentTimeMillis();
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("time: "+elapsedTimeSec+", itorCnt: "+itorCnt);
		showList();
	}
	
	public void init(){
		System.out.println("init");
		preCalFunctionData = UiGlobals.getPreCalFunctionData();
		System.out.println("preCalFunctionData: "+preCalFunctionData);
		
		maxGridX = 0;
		maxGridY = 0;
		maxColumnCnt = UiGlobals.getAnnotationHeader().size();
		
		mainPanel = new JXPanel();
		resultPanel = new JXPanel();
		this.setContentPane(mainPanel);
		mainPanel.setBackground(Color.white);
		this.setContentPane(mainPanel);
		mainPanel.setPreferredSize(getSize());
		mainPanel.setSize(getSize());
		mainPanel.invalidate();
		System.out.println("size: "+mainPanel.getSize());
		
		MigLayout layout = new MigLayout(new LC().insets("15").gridGapX("1").gridGapY("1"));
		mainPanel.setLayout(layout);
		
		Editor editor = UiGlobals.curEditor();
		
		//그리드 정보 가져옴
		LayerGrid grid = (LayerGrid) editor.getLayerManager().findLayerNamed("Grid");
        HashMap map = grid.getParameters();
        this.interval_space = (Integer)map.get("spacing");
        this.xOffset = (Integer)map.get("xOffset");
        this.yOffset = (Integer)map.get("yOffset");
        System.out.println("xOffset:"+xOffset+", yOffset: "+yOffset);
        //노드 정보 가져옴
    	this.nodes = editor.getLayerManager().getActiveLayer().getContents();
    	//노드 정보를 통해 그리드 정보 갱신
    	for(int count = 0 ; count < nodes.size() ; count++)
        {
        	Fig node = nodes.get(count);
        	int gridX = (node.getLocation().x+Math.abs(xOffset))/interval_space;
        	int gridY = (node.getLocation().y+Math.abs(yOffset))/interval_space;
        	
        	maxGridX = Math.max(gridX, maxGridX);
        	maxGridY = Math.max(gridY, maxGridY);
        }
    	//그리드별 작업 진행여부 저장 빈
    	System.out.println("maxGridX: "+maxGridX);
		System.out.println("maxGridY: "+maxGridY);
		System.out.println("maxColumnCnt: "+maxColumnCnt);
    	process = new int[maxGridY+1][maxGridX+1];
    	processPanel = new JXPanel[maxGridY+1][maxGridX+1];
    	processPanelColor = new Color[maxGridY+1][maxGridX+1];
    	
    	resultMap = new SortedListForFunctionData[maxGridY+1][maxGridX+1][maxColumnCnt+1];
    	
    	for(int i = 0 ; i <= maxGridY ; i++)
    		for(int j = 0 ; j <= maxGridX ; j++){
    			process[i][j] = VALUE_STANDBY;
    			processPanel[i][j] = new JXPanel();
    			processPanel[i][j].setBackground(COLOR_STANDBY);
    			processPanelColor[i][j] = COLOR_STANDBY;
    			
    			processPanel[i][j].addMouseListener(this);
    			processPanel[i][j].setName(j+","+i);
    			for(int l = 0 ; l < maxColumnCnt ; l++)
    				resultMap[i][j][l] = new SortedListForFunctionData();
    		}
    	
    	invalidate();
    	mainPanel.revalidate();
	}
	
	/**
	 * Attribute가 속한 유전자 수
	 * @param columnIndex
	 * @param functionName
	 * @return
	 */
	public int getContainAttributeGeneSize(int columnIndex, String functionName){
		if(preCalFunctionData != null){
			String header = UiGlobals.getAnnotationHeader().get(columnIndex);
			HashMap<String, Integer> columnFuncData = preCalFunctionData.get(header);
			//System.out.println("header:: "+header);
			if(columnFuncData != null){
				//System.out.println("");
				//System.out.println("columnFuncData: "+columnFuncData);
				//System.out.println("functionName: "+functionName);
				int result = columnFuncData.get(functionName);
				if(result > 0){
					//System.out.println("found in pre-calculated data.");
					return result; 
				}else{
					//System.out.println("result is less than or equal to zero");
				}
			}else{
				//System.out.println("columnFuncData is null");
			}
		}else{
			//System.out.println("preCalFunctionData is null");
		}
		
		Set<String> keys = functionUniverse.keySet();
		int resultCnt = 0;
		
		for(String key : keys){
			HashMap<Integer, List<String>> functionById = functionUniverse.get(key);
			
			if(functionById != null){
				
				List<String> functionByColumn = functionById.get(columnIndex);
				if(functionByColumn!= null)
					if(functionByColumn.contains(functionName)) 
						resultCnt++;	
			}
		}
		//System.out.println("getContainAttributeGeneSize resultCnt: "+resultCnt);
		return resultCnt;
	}
	
	/**
	 * 쿼리 셋 내에 Attribute 를 포함한 유전자셋 수
	 * @param querySet
	 * @param columnIndex
	 * @param functionName
	 * @return
	 */
	public int getContainAttributeGeneSizeInQuery(List<String> querySet, int columnIndex, String functionName){
		int resultCnt = 0;
		
		for(String key : querySet){
			//System.out.println("keySize: "+functionUniverse.keySet().size());
			HashMap<Integer, List<String>> functionById = functionUniverse.get(key);
			//System.out.println("re: "+functionById+", key: "+key);
			if(functionById != null){
				List<String> functionByColumn = functionById.get(columnIndex);
				//System.out.println("B...");
				//for(Integer key11 : functionById.keySet())
				//	System.out.println("11: "+key);
				if(functionByColumn!= null)
					if(functionByColumn.contains(functionName)) 
						resultCnt++;	
			}
		}
		//System.out.println("getContainAttributeGeneSizeInQuery resultCnt: "+resultCnt);
		return resultCnt;
	}
	
	/**
	 * @param querySet
	 * @param columnIndex
	 * @param functionName
	 * @return
	 */
	
	public double getPValue(List<String> querySet, int q, int columnIndex, String functionName, int n){
		/**
		 * The number of genes in the Query Set
		 */
		//int q = querySet.size();
		/**
		 * The total number of genes having attribute A
		 */
		//int n = this.getContainAttributeGeneSize(columnIndex, functionName);
		/**
		 * The number of genes in Q having attribute A
		 */
		int m = this.getContainAttributeGeneSizeInQuery(querySet, columnIndex, functionName);
		/**
		 * The total number of genes in the gene universe.
		 */
		int N = functionUniverse.keySet().size();
		
		
		double overPresentPValue = 0.0;
		double devider = (binom(N, n));
		//System.out.println("q: "+q+", n: "+n+", m: "+m+", N: "+N);
		for(int a = m ; a <= Math.min(q, n) ; a++){
			//System.out.println("overPresentPValue: "+overPresentPValue+", (binom(1, q, a): "+binom(1, q, a)+", binom(2, N-q, n-a): "+binom(2, N-q, n-a)+", binom(3, N, n): "+binom(3, N, n));
			//overPresentPValue += (binom(1, q, a).multiply(binom(2, N-q, n-a))).divide(binom(3, N, n)).doubleValue();
			overPresentPValue += Math.exp((binom(q, a)+binom(N-q, n-a))-devider);
		}
		//System.out.println("overPresentPValue: "+overPresentPValue);
		double underPresentPValue = 0.0;
		for(int a = 0 ; a <= m ; a++){
			//underPresentPValue += (binom(q, a)*binom(N-a, n-a))/devider;
			underPresentPValue += Math.exp((binom(q, a)+binom(N-q, n-a))-devider);
		}
		
//		if(testCount < 100){
//			System.out.println("function: "+functionName+", N: "+N+", n: "+n+", q: "+q+", Math.min(q, n): "+Math.min(q, n)+", result: "+Math.min(overPresentPValue, underPresentPValue));
//			testCount++;
//		}
		
		return Math.min(overPresentPValue, underPresentPValue);
	}
	
	public double getAdjPValue(List<String> querySet, int querySetSize, int columnIndex, String functionName, int n){
		
		//double pValue = getPValue(querySet, columnIndex, functionName);
		//int querySetSize = querySet.size();
		int iterSize = 50;
		
		/**
		 * The number of genes in the Query Set
		 */
		//int q = querySetSize;
		/**
		 * The total number of genes having attribute A
		 */
		//int n = this.getContainAttributeGeneSize(columnIndex, functionName);
		/**
		 * The number of genes in Q having attribute A
		 */
		//int m = this.getContainAttributeGeneSizeInQuery(querySet, columnIndex, functionName);
		/**
		 * The total number of genes in the gene universe.
		 */
		//int N = functionUniverse.keySet().size();
		
		//binomA = new BigInteger [ q + 1];
		//binomB = new BigInteger [ N - q + 1];
		//binomC = new BigInteger [ N + 1];
		
		
		//preCal(binomA, q);
		//System.out.println("A: "+binomA);
		//preCal(binomB, N-q);
		//System.out.println("B: "+binomB);
		//preCal(binomC, N);		
		//System.out.println("C: "+binomC);
		double oriPValue = getPValue(querySet, querySetSize, columnIndex, functionName, n);
		//System.out.println("oriPValue: "+oriPValue);
		/*for(int i = 0 ; i < iterSize ; i++){
			//System.out.println(i+" .. try.."+querySetSize+", "+functionUniverse.keySet().size());
			List<String> randSelection = makeRandGeneSelection(querySetSize, functionUniverse.keySet().size());
			//System.out.println(randSelection);
			double itorPValue = getPValue(randSelection, querySetSize, columnIndex, functionName, n);
			//System.out.println("itorPValue: "+itorPValue);
		}*/
		
		return oriPValue;
	}
	
	public void preLogCal(int size){
		logMap = new Double[size+1];
		for(int i = 0 ; i <= size ; i++){
			logMap[i] = Math.log(i);
		}
	}
	
	public double getLog(int val){
		//if(logMap.get(val) == null)
		//	return Math.log(val);
		return logMap[val] == null ? Math.log(val) : logMap[val];//.get(val);
	}
	
	public void preCal(BigInteger[] data, int n){
		//data = new double [ n + 1];
		data [0] = new BigInteger("1");
    	for(int i = 1 ; i <= n ; i++)
    	{
    		//System.out.println("i: "+i);
    		data[i] = new BigInteger("1");
    		for(int j = i - 1 ; j > 0 ; --j){
    			data[j] = data[j].add( data[j - 1] );
    		}
    	}
    	
	}
	
    public BigInteger binom (int type, int n, int m)
    {
    	
    	if(type == 1)
    		return binomA[m];
    	if(type == 2)
    		return binomB[m];
    	if(type == 3)
    		return binomC[m];
    	return new BigInteger("0");
    	
    	//double result = 0;
    	//return factorial(n)/(factorial(m)*factorial(n-m));
    }
    
    public double binom (int N, int n)
    {
    	
    	double result = 0.0;
    	for(int i = N ; i >= N - n + 1 && i >= 1; i--){
    		result += getLog(i);//Math.log(i);
    	}
    	for(int i = n-1 ; i >= 1 ; i--){
    		result -= getLog(i);//Math.log(i);
    	}
    	
    	return result;
    	
    	//double result = 0;
    	//return factorial(n)/(factorial(m)*factorial(n-m));
    }
    
    public static double binomTest (int N, int n)
    {
    	
    	double result = 0.0;
    	for(int i = N ; i >= N - n + 1 && i >= 1; i--){
    		result += Math.log(i);
    	}
    	for(int i = n-1 ; i >= 1 ; i--){
    		result -= Math.log(i);
    	}
    	
    	return result;
    	
    	//double result = 0;
    	//return factorial(n)/(factorial(m)*factorial(n-m));
    }
    
    public static int factorial(int n){
    	if(n == 1) return 1;
    	else return n * factorial(n-1);
    }
	
    public List<String> makeRandGeneSelection(int size, int maxSize){
		if(size > maxSize) return null;
		int[] sequence = new int[maxSize];
		
		//랜덤 큐 채우기 
		Random oRandom = new Random();
		for(int i = 0 ; i < size ; i++){
			int selection = oRandom.nextInt(maxSize);//(int)Math.random()/(maxSize-i);
			int swapA = sequence[selection];
			if(swapA == 0) swapA = selection;
			int swapB = sequence[i];
			if(swapB == 0) swapB = i+1;
			sequence[i] = swapA;
			sequence[selection] = swapB;
			
		}
		
		for(int i = 0 ; i < size ; i++)
			if(sequence[i] == 0) sequence[i] = i;
		
		List<String> resultList = new Vector<String>();
		
		//System.out.println("size: "+geneIds.size());
		
		for(int i = 0 ; i < size ; i++){
			//System.out.println("seq: "+sequence[i]);
			resultList.add(geneIds.get(sequence[i]));
		}
		
		return resultList;
	}
    
    public static void main(String[] argv){
//    	System.out.println(Math.exp(binomTest(155, 154)+binomTest(9180, 793)-binomTest(9335, 947)));
//    	System.out.println(Math.exp(binomTest(9180, 793)));
//    	System.out.println(Math.exp(binomTest(9335, 947)));
    	JFrame main = new JFrame();
    	main.setSize(100, 100);
    	main.setVisible(true);
    	
    	JPanel optionPane = new JPanel();
    	optionPane.setLayout(new MigLayout(new LC().insets("5").gridGapX("1").gridGapY("1")));
		
    	JLabel xGridText = new JLabel("X: ");
    	optionPane.add(xGridText, "width 15::15");
		JTextField xGrid = new JTextField();
		optionPane.add(xGrid, "width 30::30");
		
		JButton submit = new JButton("Download");
		optionPane.add(submit, "span 1 2, wrap, height 38::38");
		
		JLabel yGridText = new JLabel("Y: ");
    	optionPane.add(yGridText, "width 15::15");
		JTextField yGrid = new JTextField();
		optionPane.add(yGrid, "width 30::30");
		
		final JDialog dialog = new JDialog(main, 
                "Insert grid location.",
                true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(
			    JDialog.HIDE_ON_CLOSE);
				
		dialog.pack();
		dialog.setVisible(true);
    }
    
    /**
     * 그리드별 속한 진셋을 구함
     */
    public void getGeneSetByGrid(){
    	gridGeneSetData = new HashMap<Point, List<String>>();
    	 for(int count = 0 ; count < nodes.size() ; count++)
         {
         	Fig node = nodes.get(count);
         	String name = "";
         	Object desc = node.getOwner();
         	if(desc instanceof NodeDescriptor)
         	{
         		
         		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
        		name = nodeDesc.getName();
         	}
         	
         	int gridX = (node.getLocation().x+xOffset)/interval_space;
         	int gridY = (node.getLocation().y+yOffset)/interval_space;
         	
         	//maxGridX = Math.max(gridX, maxGridX);
         	//maxGridY = Math.max(gridY, maxGridY);
         	
         	List<String> curGeneSet = gridGeneSetData.get(new Point(gridX, gridY));
         	if(curGeneSet == null){
         		curGeneSet = new Vector<String>();
         		gridGeneSetData.put(new Point(gridX, gridY), curGeneSet);
         	}
         	curGeneSet.add(name);
         	
         }
    	 
    	 /*int filledGrid = 0;
         for(int i = 0 ; i <= maxGridX ; i++){
         	for(int j = 0 ; j <= maxGridY ; j++){
         		List<String> curGeneSet = gridGeneSetData.get(new Point(i, j));
         		if(curGeneSet != null)
         		{
         			System.out.println(i+", "+j+": "+curGeneSet);
         			filledGrid++;
         		}
         	}
         }
         System.out.println("filled: "+filledGrid);*/
    }
    
    /**
     * 그리드별 펑션 리스트 구함
     * @param columnIndex : annotation 파일의 컬럼 번호를 의미함 , 각각 다른 속성을 가지고 있으므로..
     */
    public void genGeneFunctionByGrid(int columnIndex){
    	
    	if(gridFuncData == null)
    		gridFuncData = new HashMap<Point, HashMap<Integer, List<String>>>();
        
        for(int count = 0 ; count < nodes.size() ; count++)
        {
        	Fig node = nodes.get(count);
        	String name = "";
        	Object desc = node.getOwner();
        	if(desc instanceof NodeDescriptor)
        	{
        		
        		NodeDescriptor nodeDesc = (NodeDescriptor)desc;
       			name = nodeDesc.getName();
        	}
        	
        	int gridX = (node.getLocation().x+xOffset)/interval_space;
        	int gridY = (node.getLocation().y+yOffset)/interval_space;
        	
        	maxGridX = Math.max(gridX, maxGridX);
        	maxGridY = Math.max(gridY, maxGridY);
        	
        	HashMap<Integer, List<String>> curGridFunc = gridFuncData.get(new Point(gridX, gridY));
        	if(curGridFunc == null){
        		curGridFunc = new HashMap<Integer, List<String>>();
        		gridFuncData.put(new Point(gridX, gridY), curGridFunc);
        	}
        	
        	List<String> funcList = curGridFunc.get(columnIndex);
        	if(funcList == null){
        		funcList = new Vector<String>();
        		curGridFunc.put(columnIndex, funcList);
        	}
        	
        	if(functionUniverse.get(name) != null){
        		if(functionUniverse.get(name).get(columnIndex) != null)
		        	for(String funcName : functionUniverse.get(name).get(columnIndex)){
		        		if(!funcList.contains(funcName))
		        			funcList.add(funcName);
		        	}
        	}
        }
    	
    }
    
    public void showList(){
		scrollPane = null;
		
		if(resultPanel != null){
			resultPanel.removeAll();
			this.remove(resultPanel);
		}
		
		String[] column = null;
		
		columnData = new Vector<String>();
		columnData.add("X");
		columnData.add("Y");
		
		for(int i = 1 ; 1==1 && i < UiGlobals.getAnnotationHeader().size() ; i++)
			columnData.add(UiGlobals.getAnnotationHeader().get(i));
		//columnData.add(UiGlobals.getAnnotationHeader().get(1));
		//columnData.add(UiGlobals.getAnnotationHeader().get(2));
		
		Object[][] data = null;
		Integer[][] dataHeight = null;
		
		try{
			if(column == null && (columnData == null || columnData.size() == 0)){
				//JOptionPane.showMessageDialog(UiGlobals.getNodeInfoFrame(),
				//	    "Eggs are not supposed to be green.", 
				//	    "Message",
				//	    JOptionPane.WARNING_MESSAGE);
			}else{
				int total = (maxGridX+1)*(maxGridY+1);
				this.setTitle("Total "+total+" Grids.");
				
				if(column == null){
					column = new String[columnData.size()];
					columnData.toArray(column);
				}
				
				
				data = new Object[total][columnData.size()+1];
				dataHeight = new Integer[total][columnData.size()+1];
				
				HashMap<String, HashMap<Integer, String>> annotationContent = UiGlobals.getAnnotationContent();
				
				int filledCount = 0;
				
//				for(int i = 0 ; i <= maxGridY ; i++){
//		    		for(int j = 0 ; j <= maxGridX ; j++){
//		    			System.out.println("i: "+i+", j: "+j+".."+resultMap[i][j].size());
//		    		}
//				}
				
				boolean isHaveRowData = false;
				
				tableIndexMap = new HashMap<String, Integer>();
				
				for(int i = 0 ; i <= maxGridY ; i++){
		    		for(int j = 0 ; j <= maxGridX ; j++){
		    			isHaveRowData = false;
		    			
		    			data[filledCount][0] = j;
		    			data[filledCount][1] = i;
		    			tableIndexMap.put(Integer.toString(j)+","+Integer.toString(i), filledCount);
		    			
		    			for(int l = 2 ; l < columnData.size() ; l++){
		    				dataHeight[filledCount][l] = 0;
		    				//System.out.println("i: "+i+", j: "+j+"l: "+l);
		    				SortedListForFunctionData function = resultMap[i][j][l-2];
			    			if(function.size() == 0) continue;
			    			
			    			//Set<String> keys = function.keySet();
			    			//System.out.println("i: "+i+", j: "+j+", "+function.size());
			    			
			    			int keyCnt = 0;
			    			int addedCnt = 0;
			    			data[filledCount][l] = "";
			    			
			    			for(CFunctionData functionData : function){
			    				isHaveRowData = true;
			    				//System.out.println("dataHeight["+filledCount+"]["+l+"]: "+dataHeight[filledCount][l]);
			    				dataHeight[filledCount][l]++;
			    				if(functionData.getPvalue() < thresholdValue){
			    					
				    				if(addedCnt == 0)
				    					data[filledCount][l] = functionData.toString();
				    				else
				    					data[filledCount][l] = data[filledCount][l].toString()+"\n"+functionData.toString();
				    				
				    				addedCnt++;
				    			}
			    				keyCnt++;
			    			}
			    			//System.out.println("l: "+l+", keys: "+keys.size());
			    			
			    			
		    			}
		    			
		    			if(isHaveRowData) filledCount++;
		    			
		    			
						
		    		}
				}
				
				
//				for(Fig fig : selectedFig){
//					if( fig.getOwner() instanceof NodeDescriptor){
//						NodeDescriptor des = (NodeDescriptor)fig.getOwner();
//						HashMap<Integer, String> property = annotationContent.get(des.getName());
//						
//						Set<Integer> keys = property.keySet();
//						for(int key = 0 ; key < columnData.size() ; key++){
//							
//							data[count][key] = property.get(key);
//						}
//					}
//					count++;
//				}
				
				
				if(scrollPane != null)
					this.remove(scrollPane);
				//revalidate();
				
				System.out.println("tableCreated");
				
//				for(String col : column)
//					System.out.println("column: "+col);
//				for(Object d : data)
//					System.out.println("data: "+d.toString());
				
				dataFilled = new Object[filledCount][columnData.size()];
				//System.out.println("column.length: "+column.length);
				
				for(int i = 0 ; i < filledCount ; i++){
					dataFilled[i][0] = data[i][0];
					dataFilled[i][1] = data[i][1];
					for(int j = 2 ; j < columnData.size() ; j++){
						dataFilled[i][j] = data[i][j];
						if(dataFilled[i][j] == null) dataFilled[i][j] = "";
					}
				}
				
				
				//for(int i = 0 ; i < dataFilled[0].length ; i++){
				//	System.out.println("dataFilled[[0]["+i+"].length: "+dataFilled[0][i]);
				//}
				
				//for(int i = 0 ; i < dataFilled.length ; i++){
				//	System.out.println("dataFilled[i].length: "+dataFilled[i].length+", ");
				//}
				
				JNodeInfoTableModel model = new JNodeInfoTableModel(column, dataFilled);
				sorter = new TableRowSorter<JNodeInfoTableModel>(model);
				nodeTable = new JTable(model);
				//nodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				//nodeTable.setDefaultRenderer(String.class, new CustomCellRenderer1());
				
				MultiLineHeaderRenderer headerRenderer = new MultiLineHeaderRenderer(
						SwingConstants.CENTER, SwingConstants.BOTTOM);
				headerRenderer.setBackground(Color.gray);
				headerRenderer.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
				headerRenderer.setForeground(Color.white);
				headerRenderer.setMinimumSize(new Dimension(10, 100));
				headerRenderer.setFont(new Font("Lucida Grande", Font.BOLD, 16));

				TableColumnModel tcm = nodeTable.getColumnModel();
				for (int i = 0; i < tcm.getColumnCount(); i++){
					tcm.getColumn(i).setHeaderRenderer(headerRenderer);
					String header = tcm.getColumn(i).getHeaderValue().toString();
					String[] headParts = header.split(" ");//{"11", "22"};
					String[] newHeader = new String[(headParts.length / 2) + 1];
					for(int j = 0 ; j < headParts.length ; j++){
						if(newHeader[j/2] == null) newHeader[j/2] = "";
						newHeader[j/2] = newHeader[j/2]+" "+headParts[j];
					}
					tcm.getColumn(i).setHeaderValue(newHeader);
				}
				
				nodeTable.getColumnModel().getColumn(0).setMaxWidth(30);
				nodeTable.getColumnModel().getColumn(1).setMaxWidth(30);
				
				MultiLineCellRenderer cellRenderer = new MultiLineCellRenderer();
			    //cellRenderer.setLineWrap(true);
			    //cellRenderer.setWrapStyleWord(true);
			      
				for(int i = 2 ; i < columnData.size(); i++)
					nodeTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
				
				//System.out.println("cs: "+nodeTable.getColumnModel().getColumnCount());
				//nodeTable.setRowMargin(10);
				//nodeTable.setRowHeight(100);
//				nodeTable.setRowHeight(1, 100);
//				nodeTable.setRowHeight(2, 100);
//				nodeTable.setRowHeight(3, 100);
				//for(int i = 0 ; i < filledCount ; i++)
				//	if(dataHeight[i] > 0)
				//		nodeTable.setRowHeight(i, 30 * dataHeight[i]);
				nodeTable.setRowHeight(nodeTable.getRowHeight() * Math.min(largestFuncListSize, heightValue));
				nodeTable.setRowSorter(sorter);
				nodeTable.setEnabled(true);
				
				//nodeTable.setAutoCreateRowSorter(true);
				nodeTable.setPreferredScrollableViewportSize(new Dimension(5000, 5000));
				//nodeTable.setFillsViewportHeight(true);
				nodeTable.getSelectionModel().addListSelectionListener(new RowListener());
				scrollPane = new JScrollPane(nodeTable);
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		
		
		MigLayout layout = new MigLayout(new LC().insets("5").gridGapX("1").gridGapY("1"),
				 new AC().align("left").gap("rel").grow(1f).fill(),
				 new AC().gap("0"));
		resultPanel = new JXPanel();
		resultPanel.setLayout(layout);
		
		if(scrollPane == null){ 
			MigLayout layout1 = new MigLayout(new LC().fillX().fillY().insets("0 0 0 0"),
					 new AC().align("center").gap("rel").grow(1f).fill(),
					 new AC().gap("0"));
			JPanel empty = new JPanel();
			empty.setLayout(layout1);
			empty.add(new JLabel("After annotation file is loaded, then this windows will be activated."));
			scrollPane = new JScrollPane(empty);
		}else{
			JLabel filterLabel = new JLabel("Filter column: ");
			resultPanel.add(filterLabel);
			filterColumn = new JComboBox();
			for(String columSr : column)
				filterColumn.addItem(columSr);
			resultPanel.add(filterColumn);
			JLabel filterTextLabel = new JLabel("Filter value: ");
			resultPanel.add(filterTextLabel, "align right, width 70::70");
			filterText = new JTextField();
			filterText.getDocument().addDocumentListener(
	                new DocumentListener() {
	                    public void changedUpdate(DocumentEvent e) {
	                        newFilter();
	                    }
	                    public void insertUpdate(DocumentEvent e) {
	                        newFilter();
	                    }
	                    public void removeUpdate(DocumentEvent e) {
	                        newFilter();
	                    }
	                });
			resultPanel.add(filterText, "align left, width 100::");
			
			JLabel thresholdLabel = new JLabel("Threshold: ");
			resultPanel.add(thresholdLabel, "align right, width 70::70");
			thresholdText = new JTextField();
			thresholdText.setName("thresholdText");
			thresholdText.setText(Double.toString(thresholdValue));
			thresholdText.addActionListener(this);
			resultPanel.add(thresholdText, "align left, width 50::");
			
			JLabel heightLabel = new JLabel("Height: ");
			resultPanel.add(heightLabel, "align right, width 60::60");
			String[] strHeightItems = {"5", "10", "20", "30", "40", "50", "60"};
			if(heightCombo == null){
				heightCombo = new JComboBox(strHeightItems);
				heightCombo.setSelectedIndex(2);
			}
			heightCombo.setName("heightCombo");
			
			heightCombo.addActionListener(this);
			resultPanel.add(heightCombo, "align left, width 40::");
			
			JButton exportBtn = new JButton("Export As CSV");
			exportBtn.setName("exportAsCsv");
			exportBtn.addActionListener(this);
			resultPanel.add(exportBtn, "wrap");
			
		}
		
		CC cc = new CC();
		resultPanel.add(scrollPane, cc.wrap().spanX(9));
		
		//this.add(main);
		if(tableFrame == null){ 
			tableFrame = new JFrame();
			tableFrame.setSize(900, 700);
		}
		tableFrame.setVisible(true);
		tableFrame.setContentPane(resultPanel);
		
	}
    
    private void newFilter() {
        RowFilter<JNodeInfoTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterText.getText(), filterColumn.getSelectedIndex());
        } catch (java.util.regex.PatternSyntaxException e) {
        	e.printStackTrace();
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            if(!main.isVisible())
            	main.setVisible(true);
            outputSelection();
        }
    }
    
    private void outputSelection() {
    	int[] selectionIndex = nodeTable.getSelectedRows();
    	
    	for(int i = 0 ; i <= maxGridY ; i++){
    		for(int j = 0 ; j <= maxGridX ; j++)
    			processPanel[i][j].setBackground(processPanelColor[i][j]);
    	}
    	for(int i = 0 ; i < selectionIndex.length ; i++){
    		int x = Integer.parseInt(nodeTable.getModel().getValueAt(selectionIndex[i], 0).toString());
    		int y = Integer.parseInt(nodeTable.getModel().getValueAt(selectionIndex[i], 1).toString());
    		processPanel[y][x].setBackground(COLOR_SELECTED);
    	}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source instanceof JButton){
			JButton btnSrc = (JButton)source;
			if("exportAsCsv".equals(btnSrc.getName())){
				
				
				for (int c : nodeTable.getSelectedRows()) {
					
				}
				
				Calendar cal = Calendar.getInstance();
		        String filename = "funcAssociation_"+String.format("%04d%02d%02d%02d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		        filename += ".csv";
		        
		        //String result = gridData.generateData();
		        String result = "";//gridData.generateDataSquare();
		        
		        for(int i = 0 ; i < columnData.size() ; i++){
		        	if(i == 0) result += "\""+columnData.get(i)+"\"";
        			else result += ",\""+columnData.get(i)+"\"";
		        }
		        
		        result += "\n";
		        
		        if(dataFilled != null && nodeTable.getSelectedRows().length > 0){
		        	for (int i : nodeTable.getSelectedRows()) {
		        		for(int j = 0 ; j < dataFilled[i].length ; j++){
		        			if(j == 0) result += "\""+dataFilled[i][j].toString()+"\"";
		        			else result += ",\""+dataFilled[i][j].toString()+"\"";
		        		}
		        		result += "\n";
					}
//		        	for(int i = 0 ; i < dataFilled.length ; i++){
//		        		
//		        	}
		        }else{
		        	JOptionPane.showMessageDialog(this,
		        		    "Please select more than a row to download.", 
		        		    "Info", JOptionPane.INFORMATION_MESSAGE);
		        	return;
		        }
		        
		        System.out.println("filename : "+filename);
		        System.out.println(result);
		        
		        //Attach Filename on top of reuslt file.
		        result = filename+"\n"+result;
		        
		        byte[] data = result.getBytes();
				
				ByteArrayInputStream bis = new ByteArrayInputStream(data);
				//System.out.println(data);
				
				
				
				String url = UiGlobals.getApplet().getCodeBase().toString() + "coordinator/writeGridData.jsp";
				//String url = "http://localhost:8080/coordinator/writeImage.jsp";
				HttpClient httpClient = new HttpClient();
				System.out.println("code base to Write : "+url);
				PostMethod postMethod = new PostMethod(url);
				
				//System.out.println("send filename : "+filename);
				postMethod.setRequestEntity(new InputStreamRequestEntity(bis));
				
				try{
					//Execute
					httpClient.executeMethod(postMethod);
					
					System.out.println(postMethod.getResponseBody());
				}catch(Exception ee){
					ee.printStackTrace();
				}
				
				String[] params = {filename}; 
		        CallJSObject jsObject = new CallJSObject("callGridDownloader", params, UiGlobals.getApplet());
		        Thread thread = new Thread(jsObject);
		        thread.run();
			}
		}else if(source instanceof JTextField){
			JTextField textSrc = (JTextField)source;
			if("thresholdText".equals(textSrc.getName())){
				try{
					thresholdValue = Double.parseDouble(textSrc.getText());
					showList();
				}catch(Exception ee){
					System.out.println("it is not a number.");
				}
				
				
			}
		}else if(source instanceof JComboBox){
			JComboBox comboSrc = (JComboBox)source;
			if("heightCombo".equals(comboSrc.getName())){
				try{
					heightValue = Integer.parseInt(comboSrc.getSelectedItem().toString());
					showList();
				}catch(Exception ee){
					System.out.println("it is not a number.");
				}
				
				
			}
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("mouseClicked!");
		//System.out.println(e.getSource());
		if(e.getSource() instanceof JPanel){
			JPanel src = (JPanel)e.getSource();
			
			if(!tableFrame.isVisible())
				tableFrame.setVisible(true);
			if(nodeTable != null)
				nodeTable.changeSelection(tableIndexMap.get(src.getName()), 0, false, false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("mouseEntered!");
		//System.out.println(e.getSource());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("mouseExited!");
		//System.out.println(e.getSource());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("mousePressed!");
		//System.out.println(e.getSource());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("mouseReleased!");
		//System.out.println(e.getSource());
	}
    

}
