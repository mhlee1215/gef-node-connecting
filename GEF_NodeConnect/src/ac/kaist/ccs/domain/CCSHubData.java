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
import ac.kaist.ccs.fig.FigSourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSHubData extends CCSSourceData {
		
	int range;
	
	
	public CCSHubData(int x, int y, int co2_amount, int terrain_type, int range) {
		super(x, y, co2_amount, terrain_type);
		this.type = CCSNodeData.TYPE_HUB;
		this.range = range;
		int size = 7;
		node = new FigHubNode(x, y, size, size, range);
		node.setOwner(this);
		
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	

	@Override
	public String toString() {
		return super.toString()+"CCSHubData [range=" + range + ", childSources=" + childSources
				+ "]";
	}
	
	
}
