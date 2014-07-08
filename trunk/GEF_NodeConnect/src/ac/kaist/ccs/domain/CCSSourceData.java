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

public class CCSSourceData extends CCSNodeData {
	

	int index;
	float co2_amount;
	float cost;
	float rank;
	int terrain_type;
	int industry_type;

	int clusterHub;
	int dst;
	int edge;
	
	float distanceToDst;
	protected List<Integer> childSources;
	
	
	public static int VIEW_TYPE_CO2 = 0;
	public static int VIEW_TYPE_COST = 1;
	public int viewType = VIEW_TYPE_CO2;
	
	

	public CCSEdgeData connectTo(CCSSourceData dstNode){
		//this.edge = new CCSEdgeData(this, dstNode);
		dstNode.addChildSource(this.getIndex());
		CCSEdgeData e = new CCSEdgeData(this, dstNode);
		distanceToDst = (float) Math.sqrt((x-dstNode.getX())*(x-dstNode.getX())+(y-dstNode.getY())*(y-dstNode.getY())) * CCSStatics.pixelToDistance;
		
		
		this.dst = dstNode.getIndex();
		dstNode.childSources.add(this.index);
		e = UiGlobals.addEdge(e);
		this.edge = e.getIndex();
		
		UiGlobals.setNode(this);
		
		return e;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public CCSSourceData(int x, int y, float co2_amount, int terrain_type) {
		super(x, y, CCSNodeData.TYPE_SOURCE);		
		int size = 7;
		node = new FigSourceNode(x, y, size, size);
		node.setOwner(this);
		this.co2_amount = co2_amount;
		this.terrain_type = terrain_type;
		childSources = new ArrayList<Integer>();
	}
	
	@Override
	public CCSSourceData clone(){
		//System.out.println("CLONE!!!");
		CCSSourceData clone = new CCSSourceData(x, y, co2_amount, industry_type, terrain_type);
		clone.setIndex(index);
		return clone;
	}
	
	public CCSSourceData(int x, int y, float co2_amount, int industry_type, int terrain_type) {
		//super(x, y, CCSNodeData.TYPE_SOURCE);
		this(x, y, co2_amount, terrain_type);
		this.industry_type = industry_type;
		
		
	}

	public List<Integer> getChildSources() {
		return childSources;
	}

	public void setChildSources(List<Integer> childSources) {
		this.childSources = childSources;
	}
	
	public void addChildSource(int childIndex){
		this.childSources.add(childIndex);
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
		return UiGlobals.getEdge(edge);
	}

	public void setEdge(CCSEdgeData edge) {
		this.edge = edge.getIndex();
	}

	public CCSSourceData getClusterHub() {
		return UiGlobals.getNode(clusterHub);
	}

	public void setClusterHub(CCSSourceData clusterHub) {
		this.clusterHub = clusterHub.getIndex();
	}

	public int getTerrain_type() {
		return terrain_type;
	}
	
	public String getTerrain_typeStringShort() {
		return CCSStatics.terrainTypeStringShortMap.get(this.terrain_type);
	}
	
	public String getTerrain_typeString() {
		return CCSStatics.terrainTypeStringMap.get(this.terrain_type);
	}

	public void setTerrain_type(int terrain_type) {
		this.terrain_type = terrain_type;
	}

	public int getIndustry_type() {
		return industry_type;
	}
	
	public String getIndustry_typeString() {
		return CCSStatics.plantTypeStringMap.get(industry_type);
	}
	
	public String getIndustry_typeStringShort() {
		return CCSStatics.plantTypeStringShortMap.get(industry_type);
	}

	public void setIndustry_type(int industry_type) {
		this.industry_type = industry_type;
	}

	public CCSSourceData getDst() {
		return UiGlobals.getNode(dst);
	}

	public void setDst(CCSSourceData dst) {
		this.dst = dst.getIndex();
	}

	public float getCost() {
		return cost;
	}
	
	public float computeCost(int pipeDiameterType){
		double childCost = 0;
		
		for(Integer childIdx : childSources){
			CCSSourceData childNode = UiGlobals.getNode(childIdx);
			childCost += childNode.computeCost(pipeDiameterType);
		}
		
		Double unitTransportCost = CCSStatics.unitTransportCostMap.get(pipeDiameterType);
		Double transportCapitalCost = CCSStatics.transportCapitalCostMap.get(pipeDiameterType);
		this.cost = (float) (childCost + (float) (this.co2_amount * unitTransportCost + transportCapitalCost * distanceToDst));
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public void addCost(float cost) {
		this.cost += cost;
	}



	@Override
	public String toString() {
		return "CCSSourceData [index=" + index + ", co2_amount=" + co2_amount
				+ ", cost=" + cost + ", rank=" + rank + ", terrain_type="
				+ terrain_type + ", industry_type=" + industry_type
				+ ", clusterHub=" + clusterHub + ", dst=" + dst + ", edge="
				+ edge + ", distanceToDst=" + distanceToDst + ", childSources="
				+ childSources + ", viewType=" + viewType + "]";
	}
	
	
	
	
}
