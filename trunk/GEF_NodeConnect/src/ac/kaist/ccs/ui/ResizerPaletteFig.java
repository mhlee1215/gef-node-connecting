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

// File: PaletteFig.java
// Classes: PaletteFig
// Original Author: ics125 spring 1996
// $Id: PaletteFig.java 1153 2008-11-30 16:14:45Z bobtarling $

package ac.kaist.ccs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.java.dev.colorchooser.ColorChooser;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTaskPane;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerGrid;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.ResourceLoader;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.fig.FigCCSNode;


public class ResizerPaletteFig extends WestToolBar implements ChangeListener, ActionListener, ItemListener {

	/**
     * 
     */
	private static final long serialVersionUID = 304194274216578087L;
	
	private int gridCurValue = 0;
	private int scaleCurValue = 0;
	
	JComboBox scaleCombo = null;
	JSpinner gridSpinner = null;
	JSlider gridResizer = null;
	JSlider scaleResizer = null;
	
	
	JMenu scaleMenu;
	String scaleNamePrefix = "SCALE_PREFIX";
	String scalePrefix = "x";
	int scaleMin = 1;
	int initScale = UiGlobals.getPre_scaled();
	int scaleMax = 9;
	
	private final String LAYER_ORIGINAL = "Original";
	private final String LAYER_ALL = "All";
	
	int taskWidth = 125;
	
	public int getTaskWidth() {
		return taskWidth;
	}

	public void setTaskWidth(int taskWidth) {
		this.taskWidth = taskWidth;
	}

	public ResizerPaletteFig() {
		defineButtons();
	}

	/**
	 * Defined the buttons in this palette. Each of these buttons is associated
	 * with an CmdSetMode, and that Cmd sets the next global Mode to somethign
	 * appropriate. All the buttons can stick except 'select'. If the user
	 * unclicks the sticky checkbox, the 'select' button is automatically
	 * pressed.
	 */
	public void defineButtons() {
		//this.setLayout(new GridLayout(4, 1));
		//this.setLayout(new FlowLayout());
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// add(new CmdSetMode(ModeCreateFigLine.class, "Line"));
		// add(new CmdSetMode(ModeCreateFigText.class, "Text")	);

		// add(image1, "Image1", "Image1");
		//add(new CmdZoom(2), "Zoom in", "zoom_in");
		//add(new CmdZoom(0.5), "Zoom out", "zoom_out");
		scaleMenu = new JMenu(scalePrefix+initScale);
		scaleMenu.setToolTipText("<html><h3>Manupulation of scale</h3><br>The Scale means that total resolution of white plane. <br>If scale value become higher, then total size of white <br>plane become larger. However, when you chanege scale, <br>the plane you will see is same as before. <br>This is because the white plane is fit to your monitor.</html>");
		ButtonGroup group = new ButtonGroup();
		for(int count = scaleMin ; count < scaleMax ; count++){
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(scalePrefix+count);
			item.setName(scaleNamePrefix+count);
			item.addActionListener(this);
			group.add(item);
			scaleMenu.add(item);
			if(count == initScale)
				item.setSelected(true);
		}
		JMenuBar scaleMenuBar = new JMenuBar();
		//scaleMenuBar.setComponentOrientation()
		scaleMenuBar.add(scaleMenu);
		//scaleMenuBar.setPreferredSize(new Dimension(50, 50));
		
		String[] strScaleItems = new String[scaleMax - scaleMin];
		for(int count = scaleMin ; count < scaleMax ; count++){
		    strScaleItems[count-scaleMin] = scalePrefix+count;
		}
		scaleCombo = new JComboBox(strScaleItems);
		scaleCombo.setName("scale");
		scaleCombo.setSelectedIndex(initScale-1);
		scaleCombo.addActionListener(this);
		
		
		
		int scaleMin = UiGlobals.getPre_scaled();
		int scaleMax = 12;
		scaleCurValue = scaleMin;
		scaleResizer = new JSlider(JSlider.VERTICAL,
				scaleMin, scaleMax, scaleCurValue);
		scaleResizer.setName("scaleResizer");
		scaleResizer.setBackground(Color.white);
		Hashtable<Integer, JLabel> scaleLableTable = 
            new Hashtable<Integer, JLabel>();
//		scaleLableTable.put(new Integer( scaleMin ),
//				minLabel );
//		scaleLableTable.put(new Integer( scaleMax ),
//				maxLabel );
		scaleResizer.setLabelTable(scaleLableTable);
        //scaleResizer.setPaintLabels(true);
        scaleResizer.addChangeListener(this);
        //scaleResizer.setPreferredSize(new Dimension(50, 500));
        scaleResizer.setBorder(
                //BorderFactory.createEmptyBorder(0,0,0,0)
                //BorderFactory.createLineBorder(Color.black, 1)
                BorderFactory.createTitledBorder("Scale")
                );
		//add(scaleResizer);
		scaleResizer.setEnabled(false);
		
		
		
		setLayout(new GridLayout(1, 1));
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		add(mainPanel);
		mainPanel.setLayout(new MigLayout("insets 1 1 1 1"));
		//GridBagConstraints c = new GridBagConstraints();
		
		//int leftToolbarWidth = 30;
		
		
		
		
		JXTaskPane scaleTask = new JXTaskPane();
		scaleTask.setLayout(new GridBagLayout());
		GridBagConstraints taskConstraints = new GridBagConstraints();
		taskConstraints.fill = GridBagConstraints.HORIZONTAL;
		taskConstraints.anchor = GridBagConstraints.PAGE_START;
		taskConstraints.weightx = 1;
		taskConstraints.insets = new Insets(-6,-8,-6,-8);  //top padding
		taskConstraints.gridx = 0;
		taskConstraints.gridy = 1;
        
		Icon scaleTaskIcon = ResourceLoader.lookupIconResource("scaleTask1", "scaleTask1");
		scaleTask.setTitle("Scale");
		scaleTask.setFocusable(false);
		scaleTask.setCollapsed(false);
		scaleTask.setIcon(scaleTaskIcon);
		
		scaleTask.add(scaleCombo, taskConstraints);
		mainPanel.add(scaleTask, "wrap, width "+taskWidth+"::"+taskWidth+"");
		
		JXTaskPane experimentTask = new JXTaskPane();
		experimentTask.setLayout(new GridBagLayout());
		Icon experimnetTaskIcon = ResourceLoader.lookupIconResource("siGraph", "siGraph");
		experimentTask.setTitle("Experimnet");
		experimentTask.setFocusable(false);
		experimentTask.setCollapsed(false);
		experimentTask.setIcon(experimnetTaskIcon);
		
		JButton btnShowPipelineDiameter = new JButton("Do experimnet");
		btnShowPipelineDiameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Do experiment");
				UiGlobals.getNodeRenderManager().computeCostExperiment();
				
			}});
		
//		JButton btnShowCost = new JButton("Show Cost");
//		btnShowPipelineDiameter.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("hi");
//			}});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());
		btnPanel.add(btnShowPipelineDiameter);
		//btnPanel.add(btnShowCost);
		
		experimentTask.add(btnPanel, taskConstraints);
		mainPanel.add(experimentTask, "wrap, width "+taskWidth+"::"+taskWidth+"");
		//UiGlobals.set_scaleSlider(scaleResizer);
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		Object source = e.getSource();
		if(source instanceof JSlider)
		{
			JSlider slider = (JSlider)source;
			String sliderName = slider.getName();
			System.out.println(sliderName);
			if(sliderName != null){
				if (sliderName.equals("gridResizer")) {

					if (gridCurValue != slider.getValue()) {
						gridCurValue = slider.getValue();
						int space = slider.getValue();
						gridSpinner.setValue(space);
						gridResize(space);
						
					}
				}
				else if(sliderName.equals("scaleResizer"))
				{
					if(scaleCurValue != slider.getValue()){
						System.out.println("scale Value : "+slider.getValue());
						scaleCurValue = slider.getValue();
						
						//JPanel mainPanel = UiGlobals.getMainPane();
			            //mainPanel.remove(UiGlobals.getGraphPane());
			            
						UiGlobals.get_scaleSlider().setEnabled(false);
						NodeRenderManager manager = UiGlobals.getNodeRenderManager();
						UiGlobals.setPre_scaled(scaleCurValue);
						manager.drawNodes(true);
					}
				}
			}
		}
		else if(source instanceof JSpinner)
		{
			JSpinner spinner = (JSpinner)source;
			String spinnerName = spinner.getName();
			System.out.println("gridSpinner");
			if(spinnerName.equals("gridSpinner")){
				if (gridCurValue != (Integer)spinner.getValue()) {
					gridCurValue = (Integer)spinner.getValue();
					int scale = (Integer)spinner.getValue();
					gridResizer.setValue(scale);
					gridResize(scale);
					
				}
			}
			
		}
		
		// TODO Auto-generated method stub
		
	}
	
	public void gridResize(int space)
	{
		Editor editor = UiGlobals.curEditor();
		LayerGrid grid = (LayerGrid) editor.getLayerManager()
				.findLayerNamed("Grid");
		HashMap map = new HashMap();
		UiGlobals.setGrid_spacing(space);
		map.put("spacing_include_stamp", (int) (space)*UiGlobals.getGrid_scale());
		
		//Offset init.
		map.put("yOffset", 0);
		map.put("xOffset", 0);
		UiGlobals.setStatusbarText("x-offset: "+0+", y-offset: "+0);
		
		grid.adjust(map);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if(s instanceof JButton){
			JButton button = (JButton)s;
			
			if("ShowSelectedNodeInfo".equals(button.getName())){
				String selectedLayer = "SelectedLayer";//UiGlobals.getShowLayerCombo().getSelectedItem().toString();
				List<Fig> layerNodes  = UiGlobals.getLayerData().get(selectedLayer);
				UiGlobals.showNodeInfoList(layerNodes);
			}else{
				Editor editor = UiGlobals.curEditor();
				LayerGrid grid = (LayerGrid) editor.getLayerManager()
						.findLayerNamed("Grid");
				HashMap params = grid.getParameters();
				int spacing = (Integer)params.get("spacing");
				
				double moveSize = .1; 
				int distence = (int)(spacing*moveSize);
				if(distence == 0) distence = 1;
		
				
				HashMap map = new HashMap();	
				if(button.getActionCommand().equals("gridUp")){
					map.put("yOffset", -distence);
				}else if(button.getActionCommand().equals("gridDown")){
					map.put("yOffset", distence);
				}else if(button.getActionCommand().equals("gridLeft")){
					map.put("xOffset", -distence);
				}else if(button.getActionCommand().equals("gridRight")){
					map.put("xOffset", +distence);
				}
				grid.adjust(map);
				
				HashMap paramsAfter = grid.getParameters();
				int yOffset = (Integer)paramsAfter.get("yOffset");
				int xOffset = (Integer)paramsAfter.get("xOffset");
				UiGlobals.setStatusbarText("x-offset: "+xOffset+", y-offset: "+yOffset);
				
				
				
				
				
				
			}
		}
		else if(s instanceof JRadioButtonMenuItem){
			JRadioButtonMenuItem item = (JRadioButtonMenuItem)s;
			if(item.getName().contains(scaleNamePrefix)){
				for(int count = scaleMin ; count < scaleMax ; count++){
					if(item.getName().equals(scaleNamePrefix+count)){
						//scaleMenu.setText(scalePrefix+count);
						item.setSelected(true);
						
						//UiGlobals.get_scaleSlider().setEnabled(false);
						NodeRenderManager manager = UiGlobals.getNodeRenderManager();
						UiGlobals.setGrid_scale(count);
						manager.drawNodes(true);
						
					}
				}
			}/*else if(item.getName().contains(layerPrefix)){
				JMenuItem cb = (JMenuItem)s;
				System.out.println(cb.isSelected());
			}*/
		}
		else if(s instanceof JComboBox){
		    JComboBox cb = (JComboBox)s;
		    if("scale".equals(cb.getName())){
			    String scaleName = (String)cb.getSelectedItem();
			    String[] scaleNamePart = scaleName.split(scalePrefix);
			    System.out.println("Scale changed to : "+scaleNamePart[1]);
			    NodeRenderManager manager = UiGlobals.getNodeRenderManager();
	            UiGlobals.setGrid_scale(Integer.parseInt(scaleNamePart[1]));
	            manager.drawNodes(true);
		    }
		    else if("searchType".equals(cb.getName())){
		    	String searchType = (String)cb.getSelectedItem();
		    	UiGlobals.setSearchType(searchType);
		    }else if("layer".equals(cb.getName())){
		    	String layer = (String)cb.getSelectedItem();
		    }
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		//System.out.println(e);
		Object s = e.getItemSelectable();
		if(s instanceof JCheckBox){
			JCheckBox cb = (JCheckBox)s;
			if("onlyShowSearchedCheck".equals(cb.getName())){
				UiGlobals.setShowOnlyFound(cb.isSelected());
				showLayer("SelectedLayer");
			}
		}else if(s instanceof JComboBox){
			JComboBox cb = (JComboBox)s;
			if("layer".equals(cb.getName())){
				if(e.getStateChange() == ItemEvent.SELECTED){
					//System.out.println(cb.getSelectedItem());
					showLayer(cb.getSelectedItem().toString());
				}
			}
			
		}
	}
	
	public void showLayer(String layerName){
		System.out.println("showLayer: "+layerName+", showOnly: "+UiGlobals.isShowOnlyFound());
		Editor editor = UiGlobals.curEditor();
		if(layerName.contains(LAYER_ORIGINAL)){
			List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
			for(int count = 0 ; count < nodes.size() ; count++)
	        {
	        	Fig node = nodes.get(count);
	        	FigCCSNode nodeCustom = (FigCCSNode)node;
	        	nodeCustom.resetFoundMark();
	        	if(UiGlobals.isShowOnlyFound()){
	        		nodeCustom.setVisible(false);
		        	nodeCustom.setSelectable(false);
	        	}else{
	        		nodeCustom.setVisible(true);
		        	nodeCustom.setSelectable(true);
	        	}
	        	
	        }
		}
		else if(layerName.contains(LAYER_ALL)){
			List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
			for(int count = 0 ; count < nodes.size() ; count++)
	        {
	        	Fig node = nodes.get(count);
	        	FigCCSNode nodeCustom = (FigCCSNode)node;
	        	nodeCustom.resetFoundMark();
	        	if(UiGlobals.isShowOnlyFound()){
	        		nodeCustom.setVisible(false);
		        	nodeCustom.setSelectable(false);
	        	}else{
	        		nodeCustom.setVisible(true);
		        	nodeCustom.setSelectable(true);
	        	}
	        }
			
			Set<String> layerNameSet = UiGlobals.getLayerData().keySet();
			Iterator<String> keyIter = layerNameSet.iterator();
			
			while(keyIter.hasNext()){
				String layerNameTmp = keyIter.next();
				Color layerColor = UiGlobals.getLayerColor().get(layerNameTmp);
				List<Fig> layerNodes  = UiGlobals.getLayerData().get(layerNameTmp);
				if(nodes != null){
					for(int count = 0 ; count < layerNodes.size() ; count++)
			        {
			        	Fig node = layerNodes.get(count);
			        	FigCCSNode nodeCustom = (FigCCSNode)node;
			        	nodeCustom.setFoundMark(layerColor);
			        	nodeCustom.setVisible(true);
			        	nodeCustom.setSelectable(true);
			        }
				}
			}
		}else{
			
			List<Fig> nodes = editor.getLayerManager().getActiveLayer().getContents();
			for(int count = 0 ; count < nodes.size() ; count++)
	        {
	        	Fig node = nodes.get(count);
	        	FigCCSNode nodeCustom = (FigCCSNode)node;
	        	nodeCustom.resetFoundMark();
	        	if(UiGlobals.isShowOnlyFound()){
	        		nodeCustom.setVisible(false);
		        	nodeCustom.setSelectable(false);
	        	}else{
	        		nodeCustom.setVisible(true);
		        	nodeCustom.setSelectable(true);
	        	}
	        }
			
			List<Fig> layerNodes  = UiGlobals.getLayerData().get(layerName);
			Color layerColor = UiGlobals.getLayerColor().get(layerName);
			if(nodes != null){
				for(int count = 0 ; count < layerNodes.size() ; count++)
		        {
		        	Fig node = layerNodes.get(count);
		        	FigCCSNode nodeCustom = (FigCCSNode)node;
		        	nodeCustom.setFoundMark(layerColor);
		        	nodeCustom.setVisible(true);
		        	nodeCustom.setSelectable(true);
		        }
			}
		}
		
		editor.damageAll();
		
	}
} /* end class PaletteFig */
