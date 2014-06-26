package ac.kaist.ccs.main;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

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
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.domain.CCSPlantData;
import ac.kaist.ccs.domain.CCSSourceData;
import ac.kaist.ccs.ui.LoadingProgressBarNode;
import ac.kaist.ccs.ui.LoadingWorker;
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

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
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

		Map<Integer, List<CCSSourceData>> ccsData = null;
		List<CCSEdgeData> ccsConData = null;

		// Insert Loading data
		if (ccsData == null) {
			// ccsData = makeRandomData(300, 500, 500);
			ccsData = makeRefRandomData(refImage);
			// return;
		}
		if (ccsConData == null) {
			// ccsConData = makeNaiveCon(ccsData);
			// ccsConData = makeStarCon(ccsData);
			// ccsConData = makeTreeCon(ccsData);
			ccsConData = makeBackboneCon(ccsData);
			// ccsConData = makeHybridCon(ccsData);
		}

		NodeRenderManager nodeRenderManager = new NodeRenderManager(ccsData,
				ccsConData, _graph);
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
		return new Color(alpha, red, green, blue);
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
			BufferedImage refImage) {

		Map<Integer, List<CCSSourceData>> ccsData = new HashMap<Integer, List<CCSSourceData>>();
		Random random = new Random();

		int h = refImage.getHeight();
		int w = refImage.getWidth();

		System.out.println("height : " + h + ", width:" + w);

		Map<Color, List<Dimension>> colorIdxMap = new HashMap<Color, List<Dimension>>();
		List<Color> colorBinList = new ArrayList<Color>();
		double threshold = 10;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				// int loc = i * w + j;
				Dimension loc = new Dimension(j, i);
				int pixel = refImage.getRGB(j, i);
				Color c = getPixelARGB(pixel);

				Color cBin = getColorBin(c, colorBinList, threshold);
				if (cBin == null) {
					cBin = c;
					colorBinList.add(c);
				}

				List<Dimension> locList = colorIdxMap.get(cBin);
				if (locList == null) {
					locList = new ArrayList<Dimension>();
					colorIdxMap.put(c, locList);
				}
				locList.add(loc);
			}
		}

		System.out.println("colorIdxMap key Size:"
				+ colorIdxMap.keySet().size());
		// System.out.println("colorIdxMap:"+colorIdxMap.get(colorBinList.get(0)));

		// int pixel = image.getRGB(j, i);
		// printPixelARGB(pixel);

		int sourcePerEachResgion = 10;
		int hubPerEachRegion = 1;

		List<CCSSourceData> sourceData = new ArrayList<CCSSourceData>();
		List<CCSSourceData> hubData = new ArrayList<CCSSourceData>();

		int cnt = 0;
		for (int i = 0; i < colorBinList.size(); i++) {

			Color ck = colorBinList.get(i);

			// Filter background color
			if (getColorDist(ck, new Color(255, 255, 255, 255)) < 10)
				continue;
			// Filter noise
			List<Dimension> locList = colorIdxMap.get(ck);
			if (locList.size() < 500)
				continue;
			cnt++;
			System.out.println("locList: " + locList.size());
			System.out.println("ck:" + ck);
			System.out.println("cnt:" + cnt);

			for (int count = 0; count < sourcePerEachResgion; count++) {
				Dimension curLoc = locList.get(random.nextInt(locList.size()));
				locList.remove(curLoc);

				int x = cvtLoc((int) curLoc.getWidth());
				int y = cvtLoc((int) curLoc.getHeight());
				CCSSourceData node = new CCSSourceData(x, y);
				float co2 = random.nextInt(100);
				node.setCo2_amount(co2);
				sourceData.add(node);
				int index = UiGlobals.addNode(node);
				node.setIndex(index);

			}

			for (int count = 0; count < hubPerEachRegion; count++) {
				Dimension curLoc = locList.get(random.nextInt(locList.size()));
				locList.remove(curLoc);

				int x = cvtLoc((int) curLoc.getWidth());
				int y = cvtLoc((int) curLoc.getHeight());
				int range = 100;
				CCSSourceData node = new CCSHubData(x, y, range);
				hubData.add(node);
				int index = UiGlobals.addNode(node);
				node.setIndex(index);
			}

			// break;
		}

		ccsData.put(CCSSourceData.TYPE_SOURCE, sourceData);
		ccsData.put(CCSSourceData.TYPE_HUB, hubData);

		return ccsData;
	}

	public Map<Integer, List<CCSSourceData>> makeRandomData(int size,
			int maxWidth, int maxHeight) throws IloException {
		// IloCplex cplex = new IloCplex();
		Map<Integer, List<CCSSourceData>> ccsData = new HashMap<Integer, List<CCSSourceData>>();
		Random random = new Random();

		List<CCSSourceData> sourceData = new ArrayList<CCSSourceData>();

		for (int count = 0; count < size; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			CCSSourceData node = new CCSSourceData(x, y);
			float co2 = random.nextInt(100);
			node.setCo2_amount(co2);
			sourceData.add(node);
			int index = UiGlobals.addNode(node);
			node.setIndex(index);
		}

		List<CCSSourceData> hubData = new ArrayList<CCSSourceData>();
		for (int count = 0; count < 10; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			int range = 100;
			CCSSourceData node = new CCSHubData(x, y, range);
			hubData.add(node);
			int index = UiGlobals.addNode(node);
			node.setIndex(index);
		}

		List<CCSSourceData> plantData = new ArrayList<CCSSourceData>();
		for (int count = 0; count < size / 10; count++) {
			int x = cvtLoc(random.nextInt(maxWidth));
			int y = cvtLoc(random.nextInt(maxHeight));
			CCSSourceData node = new CCSPlantData(x, y);
			plantData.add(node);
			int index = UiGlobals.addNode(node);
			node.setIndex(index);
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

				double curDist = dist(curSrc, curHub);
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

	public Map<Integer, List<CCSSourceData>> Clustering(
			Map<Integer, List<CCSSourceData>> ccsData) {
		// List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		List<CCSSourceData> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int i = 0; i < sourceData.size(); i++) {
			CCSSourceData curSrc = sourceData.get(i);

			CCSHubData minRankHub = null;
			double minDist = 999999999;
			double minRank = 999999999;

			for (int j = 0; j < hubData.size(); j++) {
				CCSHubData curHub = (CCSHubData) hubData.get(j);

				// System.out.println(curHub);
				double curDist = dist(curSrc, curHub);
				float curRank = (float) (curSrc.getCo2_amount() / minDist);
				curSrc.setRank(curRank);
				if (minDist > curDist) {
					minDist = curDist;
					minRank = curRank;
					minRankHub = curHub;
				}
			}

			if (minDist <= minRankHub.getRange()) {
				curSrc.setClusterHub(minRankHub);
			}
		}

		for (int i = 0; i < sourceData.size(); i++) {

			CCSSourceData curSrc = sourceData.get(i);
			if (curSrc.getClusterHub() != null) {
				if (curSrc.getClusterHub().getType() == CCSNodeData.TYPE_HUB) {
					((CCSHubData) curSrc.getClusterHub()).addChildSource(curSrc
							.getIndex());
					// ((CCSHubData)curSrc.getHub()).getChildSources().add(curSrc);
				}
			}
		}

		return ccsData;
	}

	public List<CCSEdgeData> makeStarCon(
			Map<Integer, List<CCSSourceData>> ccsData) {
		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();
		List<CCSSourceData> sourceData = ccsData.get(CCSSourceData.TYPE_SOURCE);

		for (int i = 0; i < sourceData.size(); i++) {
			CCSSourceData curSrc = sourceData.get(i);
			if (curSrc.getClusterHub() != null) {
				curSrc.connectTo(curSrc.getClusterHub());
			}
		}

		return ccsConData;
	}

	public List<CCSEdgeData> makeTreeCon(
			Map<Integer, List<CCSSourceData>> ccsData) {
		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) hubData.get(j);

			List<Integer> childIndexList = curHub.getChildSources();
			List<Integer> connedtedList = new ArrayList<Integer>();
			connedtedList.add(curHub.getIndex());

			// Add until remained node list is empty
			for (int ii = 0; ii < childIndexList.size();) {

				NodePair closestPair = getClosestPair(childIndexList,
						connedtedList);

				CCSSourceData minDistSrc = closestPair.first;
				CCSSourceData minDistConnectedSrc = closestPair.second;

				if (minDistSrc != null)
					minDistSrc.connectTo(minDistConnectedSrc);

				childIndexList.remove((Integer) minDistSrc.getIndex());
				connedtedList.add(minDistSrc.getIndex());
			}
		}

		return ccsConData;
	}

	public NodePair getClosestPair(List<Integer> firstIDList,
			List<Integer> secondIDList) {

		CCSSourceData minDistSrc = null;
		CCSSourceData minDistConnectedSrc = null;
		double minDist = 9999999;

		for (int i = 0; i < firstIDList.size(); i++) {
			int childIndex = firstIDList.get(i);
			CCSSourceData curSrc = UiGlobals.getNode(childIndex);
			// System.out.println("curSrc:"+curSrc.getIndex()+", childIndex:"+childIndex);

			for (int k = 0; k < secondIDList.size(); k++) {
				int connectedIndex = secondIDList.get(k);
				CCSSourceData curConnected = UiGlobals.getNode(connectedIndex);

				double curDist = dist(curSrc, curConnected);
				if (minDist > curDist) {
					minDist = curDist;
					minDistSrc = curSrc;
					minDistConnectedSrc = curConnected;
				}
			}
		}

		return new NodePair(minDistSrc, minDistConnectedSrc);
	}

	// public static void main(String[] argv){
	// int x1 = 50;
	// int y1 = 50;
	//
	// int x2 = 55;
	// int y2 = 50;
	//
	// System.out.println(getAngle(x1, y1, x2, y2));
	// }

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
			Map<Integer, List<CCSSourceData>> ccsData) {
		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) hubData.get(j);
			List<Integer> childIndexList = curHub.getChildSources();

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

					double curDist = dist(curSrc, curHub);
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

						int jointNodeIndex = UiGlobals.addNode(node);
						node.setIndex(jointNodeIndex);

						// maxDistSrc : 가장 먼 노드 (이미 연결되있음)
						// node : joint 노드
						// curSrc : 연결할 새로운 노드
						// curHub : 현재 허브 노드
						jointConnection(maxDistSrc, node, curSrc, curHub);
					}
				}
			}
		}

		return ccsConData;
	}

	// Joint connection
	// joint node가 들어갈 자리의 양 옆 노드의 연결 사이에 joint를 집어넣는다.
	public void jointConnection(CCSSourceData ref, CCSSourceData joint,
			CCSSourceData newNode, CCSSourceData hub) {
		CCSSourceData justBeforeJoint = ref;

		while (justBeforeJoint.getDst() != null) {
			if (dist(justBeforeJoint.getDst(), hub) < dist(joint, hub))
				break;
			justBeforeJoint = justBeforeJoint.getDst();
		}

		joint.connectTo(justBeforeJoint.getDst());
		justBeforeJoint.connectTo(joint);
		newNode.connectTo(joint);
	}

	public CCSSourceData getProjectionPoint(CCSSourceData hub,
			CCSSourceData ref, CCSSourceData newNode) {

		double distToRef = dist(hub, ref);
		double distToNew = dist(hub, newNode);

		if (distToRef < distToNew)
			return null;

		int mx = ref.getX() - hub.getX();
		int my = ref.getY() - hub.getY();

		int cx = newNode.getX() - hub.getX();
		int cy = newNode.getY() - hub.getY();

		double distMax = dist(ref, hub);
		double lengthToOrth = (mx * cx + my * cy) / distMax;

		int ox = (int) (lengthToOrth * mx / distMax);
		int oy = (int) (lengthToOrth * my / distMax);

		CCSSourceData node = new CCSJointData(hub.getX() + ox, hub.getY() + oy);
		return node;
	}

	public class NodePair {
		CCSSourceData first;
		CCSSourceData second;

		public NodePair(CCSSourceData first, CCSSourceData second) {
			this.first = first;
			this.second = second;
		}
	}

	public class SortTuple {
		double dist;
		int firstId;
		int secondId;

		public SortTuple(double dist, int firstId, int secondId) {
			this.dist = dist;
			this.firstId = firstId;
			this.secondId = secondId;
		}

	}

	static class NoAscCompare implements Comparator<SortTuple> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(SortTuple arg0, SortTuple arg1) {
			// TODO Auto-generated method stub
			return arg0.dist < arg1.dist ? -1 : arg0.dist > arg1.dist ? 1 : 0;
		}
	}

	static class NoDecCompare implements Comparator<SortTuple> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(SortTuple arg0, SortTuple arg1) {
			// TODO Auto-generated method stub
			return arg0.dist > arg1.dist ? -1 : arg0.dist < arg1.dist ? 1 : 0;
		}
	}

	public List<CCSEdgeData> makeHybridCon(
			Map<Integer, List<CCSSourceData>> ccsData) {

		ccsData = Clustering(ccsData);

		List<CCSEdgeData> ccsConData = new ArrayList<CCSEdgeData>();

		// List<CCSSourceData> sourceData =
		// ccsData.get(CCSSourceData.TYPE_SOURCE);
		List<CCSSourceData> hubData = ccsData.get(CCSSourceData.TYPE_HUB);

		for (int j = 0; j < hubData.size(); j++) {

			CCSHubData curHub = (CCSHubData) hubData.get(j);

			List<Integer> childIndexList = curHub.getChildSources();
			List<SortTuple> nodesSortByRank = new ArrayList<SortTuple>();

			// Get Nodes with rank with descending order
			for (int i = 0; i < childIndexList.size(); i++) {
				int childIndex = childIndexList.get(i);
				CCSSourceData curSrc = UiGlobals.getNode(childIndex);
				nodesSortByRank.add(new SortTuple(getRank(curHub, curSrc),
						childIndex, childIndex));
			}

			Collections.sort(nodesSortByRank, new NoDecCompare());

			// Add from high rank node
			// Actual adding routine.
			for (int i = 0; i < nodesSortByRank.size(); i++) {
				int highRankNodeId = nodesSortByRank.get(i).firstId;
				CCSSourceData curSrc = UiGlobals.getNode(highRankNodeId);

			}

		}

		// Need to implement

		// Sort by Rank

		return ccsConData;
	}

	public double getRank(CCSSourceData hub, CCSSourceData ref) {
		return ref.getCo2_amount() / dist(hub, ref);
	}

	public double dist(CCSSourceData src1, CCSSourceData src2) {
		double dist = 0;

		dist = Math.sqrt((src1.getX() - src2.getX())
				* (src1.getX() - src2.getX()) + (src1.getY() - src2.getY())
				* (src1.getY() - src2.getY()));

		return dist;
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
