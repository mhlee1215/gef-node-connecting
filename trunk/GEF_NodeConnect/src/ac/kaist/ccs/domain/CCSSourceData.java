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
		
	int index;
	float co2_amount;
	CCSSourceData clusterHub;
	CCSSourceData dst;
	private CCSEdgeData edge;
	float cost;
	float rank;

	public CCSEdgeData connectTo(CCSSourceData dstNode){
		//this.edge = new CCSEdgeData(this, dstNode);
		CCSEdgeData e = new CCSEdgeData(this, dstNode);
		this.dst = dstNode;
		this.edge = e;
		return e;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public CCSSourceData(int x, int y) {
		super(x, y, CCSNodeData.TYPE_SOURCE);		
		int size = 7;
		node = new FigCO2SourceNode(x, y, size, size);
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public float getCo2_amount() {
		return co2_amount;
	}



	public void setCo2_amount(float co2_amount) {
		this.co2_amount = co2_amount;
	}



	public CCSEdgeData getEdge() {
		return edge;
	}

	public void setEdge(CCSEdgeData edge) {
		this.edge = edge;
	}

	public CCSSourceData getClusterHub() {
		return clusterHub;
	}

	public void setClusterHub(CCSSourceData clusterHub) {
		this.clusterHub = clusterHub;
	}

	public CCSSourceData getDst() {
		return dst;
	}

	public void setDst(CCSSourceData dst) {
		this.dst = dst;
	}

	public float getCost() {
		return cost;
	}



	public void setCost(float cost) {
		this.cost = cost;
	}



	@Override
	public String toString() {
		return "CCSSourceData [index=" + index + ", co2_amount=" + co2_amount
				+ ", clusterHub=" + clusterHub + ", dst=" + dst + ", edge="
				+ edge + ", cost=" + cost + ", rank=" + rank + "]";
	}
	
	
}