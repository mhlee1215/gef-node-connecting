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
		
	List<CCSSourceData> childSources;
	
	public CCSHubData(int x, int y, int type) {
		super(x, y, type);		
		int size = 11;
		node = new FigHubNode(x, y, size, size);
		childSources = new ArrayList<CCSSourceData>();
	}
	
	public List<CCSSourceData> getChildSources() {
		return childSources;
	}

	public void setChildSources(List<CCSSourceData> childSources) {
		this.childSources = childSources;
	}

	@Override
	public String toString() {
		return "CCSHubData [childSources=" + childSources + "]";
	}
	
	
}
