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
import ac.kaist.ccs.domain.CCSStatics.CO2StateData;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigSourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSSourceData extends CCSNodeData {
	

	int index;
	float co2_amount;
	double cost;
	double pipe_diameter;
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

	public double getCost() {
		return cost;
	}
	
	public double computeCost(int costType, int co2Type){
		double childCost = 0;
		
		//System.out.println("childSources: "+childSources);
		for(Integer childIdx : childSources){
			CCSSourceData childNode = UiGlobals.getNode(childIdx);
			childCost += childNode.computeCost(costType, co2Type);
		}
		
		double m = (this.co2_amount*1000)/365; // kCo2 -> tone/day
		double L = distanceToDst;
		double f = 1.0;
		
		CO2StateData co2Data = CCSStatics.co2StateMap.get(co2Type);
		
		//Method 1
		if(costType == CCSStatics.COST_TYPE_THE_OGDEN_MODELS){
			double D = Math.pow((5084.5*L *f*m*m) / (co2Data.p_outlet*co2Data.p_outlet-0.01), 0.2);
			this.cost = childCost + Math.pow((m / 1600), 0.48) * Math.pow((L / 100), 0.24) * 0.15 * L;
			this.pipe_diameter = D;
		}
		//Method 2
		else if(costType == CCSStatics.COST_TYPE_MIT_MODEL){
			double D = getConvergedDiameter(m, L, co2Data.lou, co2Data.mu, co2Data.p_outlet);
			this.cost = childCost + (20989 * D * L * 0.15) + 3100*L;
			this.pipe_diameter = D;
		}else if(costType == CCSStatics.COST_TYPE_ECOFYS_MODEL){
			double D = Math.pow((1.155*m*m*L)/((co2Data.p_outlet - 0.1)*co2Data.lou), 0.2);
			this.cost = childCost + 154.7 * D * L;
			this.pipe_diameter = D;
		}else if(costType == CCSStatics.COST_TYPE_IEA_GHG_PH4_6){
			double D = Math.pow((L*co2Data.lou*m*m) / (25041*(co2Data.p_outlet - 0.1)), 0.2);
			double capitalCost = Math.pow(10, 6) * ((0.057 * L + 1.8663) + (0.00129 * L)*D + (0.000486 * L + 0.000007) * D*D );
			double annualPipelineOMCost = 144000 + 0.61*(23213 * D + 899 * L - 259269) + 0.7*(39305 * D + 1694*L - 351355);
			this.cost = childCost + capitalCost + annualPipelineOMCost;
			this.pipe_diameter = D;
		}else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_2){
			double D = Math.pow(0.073*m / co2Data.lou, 0.5)/0.0254;
			this.cost = childCost + (10.3 * Math.pow(10, 6) *((0.057*L+1.8663)+(0.00129*L)*D+(0.000486*L+0.000007)*D*D)+10.5*350000*L) / 
						((1.1*1.1 - 1)/Math.pow(1.1, 20));
			this.pipe_diameter = D;
		}else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_3){
			double D = Math.pow((4*m)/(18.41*Math.PI*co2Data.lou), 0.5);
			this.cost = childCost + 4335 * Math.pow(m/24, 0.5) * 1.02;
			this.pipe_diameter = D;
		}
//		else if(costType == CCSStatics.COST_TYPE_PARKER_MODEL){
//			//D 는 convergence model로 구하는게 맞는건가..
//			double D = getConvergedDiameter(m, L, co2Data.lou, co2Data.mu, co2Data.p_outlet);
//			this.cost = childCost + ((673.5*D*D + 11755*D + 234085)*L/1.61 + 355000)/(10*((1.1*1.1 - 1)/Math.pow(1.1, 25)));
//		} 
		
		//Double unitTransportCost = CCSStatics.unitTransportCostMap.get(pipeDiameterType);
		//Double transportCapitalCost = CCSStatics.transportCapitalCostMap.get(pipeDiameterType);
		//this.cost = (float) (childCost + (float) (this.co2_amount * unitTransportCost + transportCapitalCost * distanceToDst));
		return cost;
	}
	
	public double getConvergedDiameter(double m, double L, double lou, double mu, double p_outlet){
		double Dc = 0;
		double D = 10;
//		 float lou = 10;
//		 float m = 1000;
//		 float L = 20;
//		 float mu = 10;
//		 float p_outlet = 14;
		 
		 for (int i = 0 ; i < 5 ; i++){
			 double Re =  ((4* (1000 / 24*3600*0.0254)) * (m / Math.PI * mu * D)); 
			 double F_f = (1 / (4 * Math.pow(-1.8 * Math.log(6.91 / Re + (12*(0.00015 / D)/3.7)), 2)));
			 D = (1 / 0.0254) * 
					 Math.pow(
							 (32*F_f*m*m)*(
									 (Math.pow((1000 / (double)(24*3600)), 2))/
									 (Math.PI*Math.PI*lou*((p_outlet - 0.1) / L)*(Math.pow(10, 6)/1000)))
					 , 0.2);
			 //System.out.println("D:"+D+", RE:"+Re+", F_f:"+F_f);
			 //System.out.println((Math.pow((1000 / (double)(24*3600)), 2)));
		 }
		 Dc = D;
		 return Dc;
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
