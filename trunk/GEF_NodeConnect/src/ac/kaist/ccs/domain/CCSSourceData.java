package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigCO2SourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSSourceData extends CCSNodeData {
		
	float co2_amount;
	CCSSourceData hub;
	float cost;

	public CCSSourceData(int x, int y, int type) {
		super(x, y, type);		
		int size = 11;
		node = new FigCO2SourceNode(x, y, size, size);
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



	public void setHub(CCSSourceData hub) {
		this.hub = hub;
	}



	public float getCost() {
		return cost;
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
