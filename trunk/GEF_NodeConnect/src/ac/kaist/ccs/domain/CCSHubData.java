package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigSourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSHubData extends CCSSourceData {
		
	int range;
	double storageCapitalCost;
	double S_selection;
	double S_cost;
	

	public CCSHubData(CCSSourceData data, int range) {
		super(data.x, data.y, data.co2_amount, data.industry_type, data.terrain_type);
		this.type = CCSNodeData.TYPE_HUB;
		this.index = data.index;
		this.range = range;
		int size = 7;
		node = new FigHubNode(x, y, size, size, range);
		this.storageCapitalCost = CCSStatics.STORAGE_CAPITAL_COST;
		//node.setOwner(this);
		
	}
	
	public CCSHubData(int x, int y, float co2_amount, int industry_type, int terrain_type, int range) {
		super(x, y, co2_amount, industry_type, terrain_type);
		this.type = CCSNodeData.TYPE_HUB;
		this.range = range;
		int size = 7;
		node = new FigHubNode(x, y, size, size, range);
		this.storageCapitalCost = CCSStatics.STORAGE_CAPITAL_COST;
		//node.setOwner(this);
		
	}
	
	@Override
	public CCSHubData clone(){
		CCSHubData clone = new CCSHubData(x, y, co2_amount, industry_type, terrain_type, range);
		clone.setIndex(index);
		clone.setChildSources(new ArrayList<Integer>(childSources));
		return clone;
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
	
	@Override
	public String getNodeType(){
		return "HUB";
	}
	
	
}
