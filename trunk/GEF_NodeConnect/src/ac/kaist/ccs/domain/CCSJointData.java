package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigSourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSJointData extends CCSSourceData {
		
	float co2_amount;
	CCSSourceData hub;
	float cost;

	public CCSJointData(int x, int y) {
		super(x, y, 0, 0);
		this.type = CCSNodeData.TYPE_JOINT;
		int size = 5;
		node = new FigJointNode(x, y, size, size);
		node.setOwner(this);
	}
	
	@Override
	public CCSJointData clone(){
		CCSJointData clone = new CCSJointData(x, y);
		clone.setIndex(index);
		return clone;
	}
	

	public float getCo2_amount() {
		return co2_amount;
	}



	public void setCo2_amount(float co2_amount) {
		this.co2_amount = co2_amount;
	}



	public CCSSourceData getHub() {
		return hub;
	}



	public void setHub(CCSJointData hub) {
		this.hub = hub;
	}



	public double getCost() {
		return cost;
	}
	
	@Override
	public String getNodeType(){
		return "JOINT";
	}



	public void setCost(float cost) {
		this.cost = cost;
	}



	@Override
	public String toString() {
		return "CCSSourceData [co2_amount=" + co2_amount + ", hub=" + hub
				+ ", cost=" + cost + "]";
	}
	
	
}
