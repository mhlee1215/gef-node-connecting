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

package org.ssu.ml.presentation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.ssu.ml.base.CmdSaveChart;
import org.ssu.ml.base.CmdSaveGridData;
import org.ssu.ml.base.UiGlobals;
import org.ssu.ml.ui.WestToolBar;
import org.tigris.gef.ui.IStatusBar;
import org.tigris.gef.ui.ToolBar;

import etc.Colors;
import etc.RoundedBorder;

/**
 * A window that displays a toolbar, a connected graph editing pane, and a
 * status bar.
 */

public class JGridChartPanel extends JGridPanel  implements ActionListener, IStatusBar, Cloneable, ChangeListener, MouseListener{
	
	Border selectedBorder = new RoundedBorder(Color.LIGHT_GRAY, true, Color.black, true);//BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red);
	Border unselectedBorder = new RoundedBorder(Color.LIGHT_GRAY, true, Color.white);//BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white);
	Border overedBorder = new RoundedBorder(Color.LIGHT_GRAY, true, Color.LIGHT_GRAY);//BorderFactory.createMatteBorder(5, 5, 5, 5, Color.LIGHT_GRAY);
	JPanel overedPanel = null;
	JPanel selectedPanel = null;
	
	
	private static final String SERIES_GOOD = "Good";
	private static final String SERIES_BIG = "Big";
	private static final String SERIES_SMALL = "Small";
	public static final String TYPE_GMT = "GMT";
	public static final String TYPE_GMX = "GMX";
    
	Vector<double[]> datas = new Vector<double[]>();
	Vector<String> categories = new Vector<String>();
	double data[];            
	//int precise = 10;
	
	JSlider upperBoundRanger;
	JTextField upperBoundText;
	JSlider lowerBoundRanger;
	JTextField lowerBoundText;
	int upperRangeMax = 1000;
	int upperRangeMin = 100;
	int lowerRangeMax = 100;
	int lowerRangeMin = 1;
	int maxCurValue;
	int minCurValue;
	int minimum_interval = 5;
	private String geneDataType = TYPE_GMT;
	
	JScrollPane chartPanel;
	
	WestToolBar leftSideToolbar;
	
	int totalWidth = 0;
	int totalHeight = 0;
	int chartWidth = 280;
	int titleWidth = 80;
	int chartHeight = 300;
	int chartPadding = 20;
	
	int chartVerticalPadding = 0;
	int chartHorizenPadding = 0;
	
	int chartNum = 0;
	
	int drawHeight = 0;
	int drawWidth = 0;
	int maxWidthCnt = 5;
	int widthCnt = 0;
	int heightCnt = 0;

    public String getGeneDataType() {
        return geneDataType;
    }

    public void setGeneDataType(String geneDataType) {
        this.geneDataType = geneDataType;
    }

    private static final long serialVersionUID = -8167010467922210977L;
    
    

    DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
    Dimension drawingSize = null;
    /**
     * Contruct a new JGraphFrame with the title "untitled" and a new
     * DefaultGraphModel.
     */
	public JGridChartPanel(String title, int totalWidth, int toalHeight) {
		super();
		setUpSideToolbar();
		this.totalWidth = totalWidth;
		this.totalHeight = toalHeight;
    }

	public JPanel getChart(){
		return selectedPanel;
	}
	
    public void addData(double[] data, String category){
    	datas.add(data);
    	categories.add(category);
    	chartNum++;
    }
    
    public Object clone() {
        return null; // needs-more-work
    }
    
    @Override
    public void setUpToolbar()
    {
    	JComboBox typeCombo = null;
    	String[] strScaleItems = {TYPE_GMT, TYPE_GMX};
    	typeCombo = new JComboBox(strScaleItems);
    	typeCombo.setName("typeCombo");
		typeCombo.setSelectedIndex(0);
		typeCombo.addActionListener(this);
    	
    	
    	toolbar.add(new CmdSaveChart(this, "aa"), "Save chart image", "Save", ToolBar.BUTTON_TYPE_TEXT);
    	toolbar.addSeparator();
    	JLabel label = new JLabel("Geneset data format : ");
    	toolbar.add(label);
    	toolbar.add(typeCombo);
    	toolbar.add(new CmdSaveGridData(this), "Save geneset data", "Save", ToolBar.BUTTON_TYPE_TEXT);
    	mainPanel.add(toolbar, BorderLayout.NORTH);
    }
    
    /**
     * 
     */
    public void setUpSideToolbar()
    {
    	leftSideToolbar = new WestToolBar();
    	
		maxCurValue = 500;
		minCurValue = 25;
		leftSideToolbar.setLayout(new GridLayout(2, 1));
		
		{
			
			upperBoundRanger = new JSlider(JSlider.VERTICAL,
					upperRangeMin, upperRangeMax, maxCurValue);
			upperBoundRanger.setName("maxRanger");
			upperBoundRanger.setBackground(Color.white);
			JLabel minLabel = new JLabel(Integer.toString(upperRangeMin));
			JLabel maxLabel = new JLabel(Integer.toString(upperRangeMax));
			Hashtable<Integer, JLabel> labelTable = 
	            new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer( upperRangeMin ),
					minLabel );
			labelTable.put(new Integer( upperRangeMax ),
					maxLabel );
			upperBoundRanger.setLabelTable(labelTable);
			upperBoundRanger.setPaintLabels(false);
			//gridResizer.setFont(font);
	        upperBoundRanger.setPaintLabels(true);
	        upperBoundRanger.setMajorTickSpacing(100);
	        upperBoundRanger.setMinorTickSpacing(50);
	        upperBoundRanger.addChangeListener(this);
	        upperBoundRanger.setPaintTicks(true);
	        upperBoundRanger.setMinorTickSpacing(50);
	        upperBoundRanger.setPreferredSize(new Dimension(60, 130));
	        JPanel maxRangerPanel = new JPanel();
	        maxRangerPanel.setLayout(new MigLayout("insets 1 1 1 1"));
			GridBagConstraints cResizer = new GridBagConstraints();
			cResizer.insets = new Insets(0, -8, 0, 0);
			//BorderFactory.createt
			Font borderFont = new Font("Lucida Grande", Font.PLAIN, 9);
			maxRangerPanel.setBorder(BorderFactory.createTitledBorder(null,"Upper boundary", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, borderFont));
			maxRangerPanel.add(upperBoundRanger, "wrap");
			
			upperBoundText = new JTextField();
			upperBoundText.setName("upperBoundText");
			upperBoundText.addActionListener(this);
			upperBoundText.setText(Integer.toString(maxCurValue));
			upperBoundText.setPreferredSize(new Dimension(60, 3));
			maxRangerPanel.add(upperBoundText);
			
	        leftSideToolbar.add(maxRangerPanel);
		}
        
		{
	        lowerBoundRanger = new JSlider(JSlider.VERTICAL,
					lowerRangeMin, lowerRangeMax, minCurValue);
			lowerBoundRanger.setName("minRanger");
			lowerBoundRanger.setBackground(Color.white);
			
			JLabel minLabel = new JLabel(Integer.toString(lowerRangeMin));
			JLabel maxLabel = new JLabel(Integer.toString(lowerRangeMax));
			Hashtable<Integer, JLabel> labelTable = 
	            new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer( lowerRangeMin ),
					minLabel );
			labelTable.put(new Integer( lowerRangeMax ),
					maxLabel );
			lowerBoundRanger.setLabelTable(labelTable);
			lowerBoundRanger.setPaintLabels(false);
			//gridResizer.setFont(font);
	        lowerBoundRanger.setPaintLabels(true);
	        lowerBoundRanger.setMajorTickSpacing(10);
	        lowerBoundRanger.setMinorTickSpacing(5);
	        lowerBoundRanger.addChangeListener(this);
	        
	        lowerBoundRanger.setPaintTicks(true);
	        lowerBoundRanger.setMinorTickSpacing(5);
	        lowerBoundRanger.setPreferredSize(new Dimension(50, 130));
	        JPanel minRangerPanel = new JPanel();
	        minRangerPanel.setLayout(new MigLayout("insets 1 1 1 1"));
			
			Font borderFont = new Font("Lucida Grande", Font.PLAIN, 9);
			minRangerPanel.setBorder(BorderFactory.createTitledBorder(null,"Lower \nboundary", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, borderFont));
			
			minRangerPanel.add(lowerBoundRanger, "wrap");
			
			lowerBoundText = new JTextField();
			lowerBoundText.setName("lowerBoundText");
			lowerBoundText.setText(Integer.toString(minCurValue));
			lowerBoundText.addActionListener(this);
			lowerBoundText.setPreferredSize(new Dimension(60, 3));
			minRangerPanel.add(lowerBoundText);
			
			leftSideToolbar.add(minRangerPanel);
			
		}
    	
		mainPanel.add(leftSideToolbar, BorderLayout.WEST);
    }


    // //////////////////////////////////////////////////////////////
    // IStatusListener implementation

    public void init(){
        heightCnt = (int)(Math.ceil(((double)datas.size())/5));
        drawHeight = heightCnt*(chartHeight+chartVerticalPadding);
        if(datas.size() >= maxWidthCnt){
            widthCnt = maxWidthCnt;
        }
        else{ 
            widthCnt = (datas.size()%maxWidthCnt+1);
        }
        drawWidth = widthCnt*(chartWidth+chartHorizenPadding);
        
        System.out.println("widthCnt : "+widthCnt);
        System.out.println("drawWidth : "+drawWidth);
        System.out.println("heightCnt : "+heightCnt);
        System.out.println("drawHeight : "+drawHeight);
        
    }
    /**
     * 
     */
    public void drawChart()
    {
        init();
        
        chartPanel = createChart();
        
        chartPanel.setPreferredSize(new Dimension(700, chartHeight+chartPadding+10));
    	mainPanel.add(chartPanel, BorderLayout.CENTER);
        //this.remove
    }
    
    /**
     * @return
     */
    public JScrollPane createChart(){
    	
    	JPanel createdPanel = createPanel();
    	System.out.println("data.size : "+datas.size());
    	createdPanel.setPreferredSize(new Dimension(drawWidth, drawHeight));
    	
    	JPanel drawPanel = new JPanel();
    	drawPanel.setBackground(Color.white);
    	drawPanel.add(createdPanel);
    	drawPanel.addMouseListener(this);
    	JScrollPane jScrollPane = new JScrollPane(drawPanel);
    	
    	jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.addMouseListener(this);
    	return jScrollPane;
    }
    
	
	/**
	 * @param categoryDataset
	 * @param data
	 * @param categoryName
	 * @return
	 */
	private CategoryDataset addDataset(DefaultCategoryDataset categoryDataset, double data[], String categoryName){
		int overCount = 0;
		int goodCount = 0;
		int lowerCount = 0;
        
		for(int count = 0 ; count < data.length ; count++)
		{
			if(data[count] > maxCurValue)
				overCount++;
			else if(data[count] < minCurValue)
				lowerCount++;
			else
				goodCount++;
		}
		System.out.println("data.length : "+data.length);
		System.out.println("goodCount : "+goodCount+", overCount  :"+overCount+", : lowerCount"+lowerCount);
		
		
		categoryDataset.addValue(lowerCount, SERIES_SMALL, categoryName);
		categoryDataset.addValue(goodCount, SERIES_GOOD, categoryName);
		categoryDataset.addValue(overCount, SERIES_BIG, categoryName);
		return categoryDataset;
	}
	
	/**
	 * @param data
	 * @param categori
	 * @return
	 */
	public CategoryPlot createCategoryPlot(double[] data, String categori){
		NumberAxis rangeAxis1 = new NumberAxis(categori);
		rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//CategoryAxis categoryAxis = new CategoryAxis("a");
		
		BarRenderer renderer = new BarRenderer();
		renderer.setItemMargin(0.05);                 // 
        renderer.setDrawBarOutline(true);            // 
        //renderer.setOutlinePaint(Color.red);
        BarPainter painter = new StandardBarPainter();
        renderer.setBarPainter(painter);
        Stroke stroke = new BasicStroke(3.0f);
        //stroke.
        
        
        renderer.setSeriesOutlinePaint(0, Color.black);
        renderer.setSeriesOutlinePaint(1, Color.black);
        renderer.setSeriesOutlinePaint(2, Color.black);
        renderer.setSeriesOutlineStroke(0, stroke);
        renderer.setSeriesOutlineStroke(1, stroke);
        renderer.setSeriesOutlineStroke(2, stroke);
        renderer.setShadowXOffset(10.0);
        renderer.setShadowYOffset(10.0);
        
        StandardCategoryItemLabelGenerator labelGenerator = new StandardCategoryItemLabelGenerator();
        renderer.setItemLabelFont(UiGlobals.getNormalFont());
        renderer.setItemLabelGenerator(labelGenerator);
        renderer.setItemLabelPaint(Color.black);
        renderer.setItemLabelsVisible(true);
        
        NumberFormat format = NumberFormat.getInstance();
        String labelFormat = "<html><body style=\"background-color: #ffffdd\"><h2>{0} area</h2><br><strong>Location Info</strong> : {1}<br><strong>Count</strong> : {2}</body></html>";
        StandardCategoryToolTipGenerator generator = new StandardCategoryToolTipGenerator(labelFormat, format);
        //System.out.println(generator.getLabelFormat());
        
        renderer.setSeriesToolTipGenerator(0, generator);
        renderer.setSeriesToolTipGenerator(1, generator);
        renderer.setSeriesToolTipGenerator(2, generator);
        
        // 7.�;�� �ٲٱ�
        renderer.setSeriesPaint(0, UiGlobals.getConstantColor().getColor(0));
        renderer.setSeriesPaint(1, UiGlobals.getConstantColor().getColor(1));
        renderer.setSeriesPaint(2, UiGlobals.getConstantColor().getColor(2));
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int overCount = 0;
		int goodCount = 0;
		int lowerCount = 0;
        
		for(int count = 0 ; count < data.length ; count++)
		{
			if(data[count] > maxCurValue)
				overCount++;
			else if(data[count] < minCurValue)
				lowerCount++;
			else
				goodCount++;
		}
		System.out.println("data.length : "+data.length+", category : "+categori);
		System.out.println("goodCount : "+goodCount+", overCount  :"+overCount+", : lowerCount"+lowerCount);
		//dataset.addV
		dataset.addValue(lowerCount, SERIES_SMALL, categori);
		dataset.addValue(goodCount, SERIES_GOOD, categori);
		dataset.addValue(overCount, SERIES_BIG, categori);
		
        
        CategoryPlot categoryPlot = new CategoryPlot(dataset, null, rangeAxis1, renderer);
		//categoryPlot.setOrientation(PlotOrientation.VERTICAL);
		//categoryPlot.
        categoryPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        
        
		
		return categoryPlot;
		
	}
	
	public JPanel createPanel() {
		//System.out.println("create Panel, precise : " + precise);
		JXPanel mainPanel = new JXPanel();
		mainPanel.setName("mainPanel");
		mainPanel.addMouseListener(this);
		mainPanel.setBackground(Color.white);
		//GridBagConstraints gbcMain = new GridBagConstraints();  
		//mainPanel.setLayout( new GridBagLayout() );
		mainPanel.setLayout(new GridLayout(0, widthCnt));
		//mainPanel.setLayout(new FlowLayout());
		//mainPanel.setPreferredSize(new Dimension(700, 440));
		//gbcMain.gridy = 0;  
		
		//gbcMain.insets = new Insets( 0, 0, 0, 0 );
		
		NumberAxis valueAxis = new NumberAxis("Count");
		CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(valueAxis);
		//plot.setrange
		plot.setOrientation(PlotOrientation.VERTICAL);
		for(int count = 0 ; count < datas.size() ; count++)
		{
			categoryDataset = (DefaultCategoryDataset) addDataset(categoryDataset, datas.get(count), categories.get(count));
			CategoryPlot cplot = createCategoryPlot(datas.get(count), categories.get(count));
			JFreeChart plotChart = new JFreeChart(cplot);
			
			
			JXPanel subBorderPanel = new JXPanel();
			subBorderPanel.setName(Integer.toString(count));
			subBorderPanel.setBackground(Color.white);
			
			subBorderPanel.setBorder(this.unselectedBorder);
			if(UiGlobals.getIsExample().equals("Y")){
				if(UiGlobals.getExampleType().equals("2")){
					subBorderPanel.setBorder(this.selectedBorder);
					selectedPanel = subBorderPanel;
				}
			}
			
			
			subBorderPanel.setPreferredSize(new Dimension(chartWidth, chartHeight));
			String title = String.format("%-10s", categories.get(count));
			JXTitledPanel subTitlePanel = new JXTitledPanel(title);
			int titleWidth = chartWidth-40;
			int titleHeight = chartHeight-34;
			subTitlePanel.setPreferredSize(new Dimension(titleWidth, titleHeight));
			
			GridBagConstraints gbcBorder = new GridBagConstraints();  
			subBorderPanel.setLayout( new GridBagLayout() );   
		    gbcBorder.insets = new Insets(14, 15, 20, 15);
		    
		    
		    subBorderPanel.add( subTitlePanel, gbcBorder );
			
		    
			//int titleWidth = 300;
			
			
			//int width = 50;
			//int height = 50;
			Color color1 = Colors.Gray.color(0.02f);
			Color color2 = Colors.White.color(0.5f);
			 
			LinearGradientPaint gradientPaint =
			     new LinearGradientPaint(0, 0, chartWidth, 0,
			                             new float[]{0.1f, 0.3f, 0.9f},
			                             new Color[]{color1, color2, color1});
			MattePainter mattePainter = new MattePainter(gradientPaint);
			
//			LinearGradientPaint gradientPaint1 =
//                new LinearGradientPaint(1.0f, 0.0f, 1000, height,
//                                        new float[]{0.0f, 1.0f},
//                                        new Color[]{color1, color2});
//           MattePainter mattePainter1 = new MattePainter(gradientPaint1);
			
//			MattePainter mp = new MattePainter(Colors.LightBlue.alpha(0.8f));
//			GlossPainter gp = new GlossPainter(Colors.White.alpha(0.8f),
//			                                   GlossPainter.GlossPosition.TOP);
//			PinstripePainter pp = new PinstripePainter(Colors.Gray.alpha(0.8f),
//			                                           45d);
			//subChartPanel.set
//			RectanglePainter roundRect = new RectanglePainter(
//			        6, 6, 6, 6,   10,10,   true, 
//				Color.GRAY, 3, Color.DARK_GRAY); 
//			roundRect.setAntialiasing(true);
			
			//subTitlePanel.setTitlePainter(mattePainter);
			subTitlePanel.setTitlePainter(new CompoundPainter(mattePainter));
			subTitlePanel.setBorder(null);
			//subTitlePanel.setPreferredSize(new Dimension(chartWidth+58, chartHeight+30));
			RectanglePainter backgroundRoundRect = new RectanglePainter(
			        6, 6, 6, 6, 10,10,   true, 
				Color.GRAY, 3, Color.DARK_GRAY); 
			backgroundRoundRect.setAntialiasing(true);
			
			
			
			plotChart.setBackgroundPaint(Color.gray);
			
			JPanel innerChartPanel = new ChartPanel(plotChart);
			
			System.out.println("popup size : "+innerChartPanel.getComponentPopupMenu());
			
			System.out.println("createPanel size : "+getSize());
			innerChartPanel.setPreferredSize(new Dimension(titleWidth-115, titleHeight-116));
			
			JRadioButton radio = new JRadioButton();
			radio.setText("radio");
			
			
			GridBagConstraints gbc = new GridBagConstraints();  
			//subTitlePanel.setLayout( new GridBagLayout() );   
		    gbc.gridy = 1;  
		    gbc.insets = new Insets( 1, 5, 10, 5 );  
		    
		    //JPanel tmpPanel = new JPanel();
		    //tmpPanel.add(innerChartPanel);
		    //tmpPanel.setPreferredSize(new Dimension(titleWidth-50, titleHeight-50));
		    subTitlePanel.add( innerChartPanel);
			
		    subTitlePanel.addMouseListener(this);
		    innerChartPanel.addMouseListener(this);
		    
			mainPanel.add(subBorderPanel);
			plot.add(cplot);
		}
		
		plot.setDomainAxis(new CategoryAxis("111"));

		return mainPanel;
	}

	public void clean()
	{
		categoryDataset = new DefaultCategoryDataset();
	}
    public static void main(String[] argv)
    {
    	
    	double data[] = {1, 2, 3, 2, 1, 4, 5, 3, 1, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	double data1[] = {3, 3, 3, 1, 1, 2, 2, 3, 3, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	double data2[] = {3, 3, 3, 1, 1, 2, 2, 3, 3, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	double data3[] = {3, 3, 3, 1, 1, 2, 2, 3, 3, 2,3  ,1, 2,3, 1,2, 3, 12,3 };
    	JGridChartPanel char1 = new JGridChartPanel("title", 500, 500);
    	
    	JXFrame frame = new JXFrame();
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	RefineryUtilities.centerFrameOnScreen(frame);
    	frame.setVisible(true);
    	frame.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				JXFrame frame = (JXFrame)e.getComponent();
				
				Component[] c = frame.getContentPane().getComponents();
				if(c.length > 0){
					JGridChartPanel gcp  = (JGridChartPanel) c[0];
					gcp.reDraw();
				}
			}
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}});
    	//char1.setSize(700, 200);
    	//char1.setAlpha(.3f);
    	
    	
    	
    	char1.setBackground(Color.white);
    	char1.addData(data, "Interval : 100, X axis Offset : 100, Y axis Offset : 200");
    	char1.addData(data1, "2");
    	char1.addData(data2, "33");
    	//char1.addData(data3, "datar");
    	//char1.addData(data2, "33");
        //char1.addData(data3, "datar");
    	char1.drawChart();
    	frame.add(char1);
    	//frame.add(char1);
    	frame.pack();
    	
    	try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        	e.printStackTrace();
        } 
//    	
//    	final JDialog dialog = new JDialog(frame, "Child", true);
//        dialog.setBackground(Color.green);
//        dialog.setForeground(Color.green);
//        dialog.setSize(300, 200);
//        dialog.setLocationRelativeTo(frame);
//        JXTitledPanel panel = new JXTitledPanel("loading...");
//        JLabel label = new JLabel("hi");
//        panel.add(label);
//        dialog.add(panel);
//        dialog.setUndecorated(true);
//        dialog.setVisible(true);
        System.out.println("load fin!");
    }

	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source instanceof JSlider) {
			JSlider slider = (JSlider) source;
			String sliderName = slider.getName();
			if (sliderName != null) {
				if (sliderName.equals("maxRanger")) {

					if (maxCurValue != slider.getValue()) {
						maxCurValue = slider.getValue();

						upperBoundText.setText(Integer.toString(maxCurValue));
						
//						if (maxCurValue - minimum_interval < minCurValue) {
//							minCurValue = maxCurValue - minimum_interval;
//							minRanger.setValue(minCurValue);
//						}
					}
				} else if (sliderName.equals("minRanger")) {

					if (minCurValue != slider.getValue()) {
						minCurValue = slider.getValue();

						lowerBoundText.setText(Integer.toString(minCurValue));
						
//						if (maxCurValue < minCurValue + minimum_interval) {
//							maxCurValue = minCurValue + minimum_interval;
//							maxRanger.setValue(maxCurValue);
//						}
					}
				}
				clean();
				mainPanel.remove(3);
				//mainPanel.remove(chartPanel);
				chartPanel = createChart();
				
				mainPanel.add(chartPanel);
				mainPanel.revalidate();

				this.showStatus("Size of good genesets : [" + minCurValue + " to "
						+ maxCurValue + "]");

			}
		}
		
	}
	
	public void reDraw(){
		
//		clean();
//		mainPanel.remove(chartPanel);
//		chartPanel = createPanel();
//		mainPanel.add(chartPanel);
//		chartPanel.revalidate();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() instanceof JXTitledPanel){
			JXTitledPanel source = (JXTitledPanel)e.getSource();
			JXPanel shdowPanel = (JXPanel)source.getParent();
			//System.out.println("src : "+source +", selected : "+selectedPanel);
			if(shdowPanel != selectedPanel){
				shdowPanel.setBorder(this.overedBorder);
				//source.setAlpha(.5f);
			}
		}else if(e.getSource() instanceof JPanel){
			JPanel source = (JPanel)e.getSource();
			e.setSource(source.getParent());
			mouseEntered(e);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() instanceof JXTitledPanel){
			JXTitledPanel source = (JXTitledPanel)e.getSource();
			JXPanel shdowPanel = (JXPanel)source.getParent();
			if(shdowPanel != selectedPanel){
				shdowPanel.setBorder(this.unselectedBorder);
				//source.setAlpha(1.0f);
			}
		}else if(e.getSource() instanceof JPanel){
			JPanel source = (JPanel)e.getSource();
			e.setSource(source.getParent());
			mouseExited(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() instanceof JXTitledPanel){
			JPanel source = (JPanel)e.getSource();
			JXPanel shdowPanel = (JXPanel)source.getParent();
			if(selectedPanel != null){
				selectedPanel.setBorder(this.unselectedBorder);
			}
			shdowPanel.setBorder(this.selectedBorder);
			selectedPanel = shdowPanel;
			
		}else if(e.getSource() instanceof JPanel){
			JPanel source = (JPanel)e.getSource();
			if(source.getName() != null && source.getName().equals("mainPanel")){
				//unselect all panel.
			    if(selectedPanel == null){
			        // TODO Need to fix... It doesn't work but not a serious problem.
			        e.setSource(source.getParent());
	                mousePressed(e);
			    }
			    else{
			        selectedPanel.setBorder(this.unselectedBorder);
			        selectedPanel = null;
			    }
			}else{
				e.setSource(source.getParent());
				mousePressed(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object s = e.getSource();
		if(s instanceof JComboBox){
			JComboBox combo = (JComboBox)s;
			System.out.println(combo.getSelectedItem());
			if(combo.getName().equals("typeCombo")){
			    this.geneDataType = (String)combo.getSelectedItem();
			}
		}
		else if(s instanceof JTextField){
			JTextField text = (JTextField)s;
			if(text.getName().equals("upperBoundText")){
				System.out.println("upper text value: "+text.getText());
				maxCurValue = Integer.parseInt(text.getText());
				if(maxCurValue > upperRangeMax){
					maxCurValue = upperRangeMax;
					
				}else if(maxCurValue < upperRangeMin){
					maxCurValue = upperRangeMin;
				}
				
				text.setText(Integer.toString(maxCurValue));
				upperBoundRanger.setValue(maxCurValue);
			}else if(text.getName().equals("lowerBoundText")){
				System.out.println("lower text value: "+text.getText());
				minCurValue = Integer.parseInt(text.getText());
				if(minCurValue > lowerRangeMax){
					minCurValue = lowerRangeMax;
				}else if(minCurValue < lowerRangeMin){
					minCurValue = lowerRangeMin;
				}
				text.setText(Integer.toString(minCurValue));
				lowerBoundRanger.setValue(minCurValue);
			}
			
		}
	}

}
