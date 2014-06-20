package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigCO2SourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSHubData extends CCSSourceData {
		
	int range;
	List<CCSSourceData> childSources;
	
	public CCSHubData(int x, int y, int range) {
		super(x, y);
		this.type = CCSNodeData.TYPE_HUB;
		this.range = range;
		int size = 7;
		node = new FigHubNode(x, y, size, size, range);
		childSources = new ArrayList<CCSSourceData>();
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public List<CCSSourceData> getChildSources() {
		return childSources;
	}

	public void setChildSources(List<CCSSourceData> childSources) {
		this.childSources = childSources;
	}

	@Override
	public String toString() {
		return "CCSHubData [range=" + range + ", childSources=" + childSources
				+ "]";
	}
	
	
}
