package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
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

public class CCSJointData extends CCSSourceData {
		
	float co2_amount;
	CCSSourceData hub;
	float cost;
	double storageCapitalCost;

	public CCSJointData(int x, int y) {
		super(x, y, 0, 0);
		this.type = CCSNodeData.TYPE_JOINT;
		int size = 5;
		this.storageCapitalCost = CCSStatics.STORAGE_CAPITAL_COST;
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
	public double computeCost(int costType, int co2Type){
		double childCost = 0;
		computeCo2();
		
		System.out.println("compute cost "+this.index+", childSources: "+childSources);
		for(Integer childIdx : childSources){
			CCSSourceData childNode = UiGlobals.getNode(childIdx);
			if(childNode.getIndex() == this.index){
				System.err.println("ERROR!! cycle!, childNode.getIndex():"+childNode.getIndex()+", this.index:"+this.index);
				//break;
				System.exit(0);
			}
			childCost += childNode.computeCost(costType, co2Type);
		}
		
		//System.out.println("this.industry_type: "+this.industry_type+", this.type :"+this.type);
		if(this.industry_type > 0){
			childCost += CCSStatics.captureCapitalCostMap.get(this.industry_type);	
		}
		
		
		double m = (this.acc_co2_amount*1000)/365; // kCo2 -> tone/day
		double L = this.getDistanceToDst();
		double f = 1.0;
		//System.out.println("Distance : "+L);
		CO2StateData co2Data = CCSStatics.co2StateMap.get(co2Type);
		
		//Method 1
		if(costType == CCSStatics.COST_TYPE_THE_OGDEN_MODELS){
			double D = Math.pow((5084.5*L *f*m*m) / (co2Data.p_outlet*co2Data.p_outlet-1), 0.2);
			D *= CCSStatics.COST_TYPE_1_TEMP_SCALE;
			this.pipelineCost = childCost;// + Math.pow((m / 1600), 0.48) * Math.pow((L / 100), 0.24) * 0.15 * L;
			this.pipe_diameter = D;
		}
		//Method 2
		else if(costType == CCSStatics.COST_TYPE_MIT_MODEL){
			double D = getConvergedDiameter(m, L, co2Data.lou, co2Data.mu, co2Data.p_outlet, co2Data.p_inlet);
			D *= CCSStatics.COST_TYPE_2_TEMP_SCALE;
			this.pipelineCost = childCost;// + (20989 * D * L * 0.15) + 3100*L;
			this.pipe_diameter = D;
		}
		//Method 3
		else if(costType == CCSStatics.COST_TYPE_ECOFYS_MODEL){
			double D = Math.pow((1.155*m*m*L)/((co2Data.p_outlet - 1)*co2Data.lou), 0.2);
			D *= CCSStatics.COST_TYPE_3_TEMP_SCALE;
			this.pipelineCost = childCost;// + 154.7 * D * L;
			this.pipe_diameter = D;
		}
		//Method 4
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_PH4_6){
			double D = Math.pow((L*co2Data.lou*m*m) / (25041*(co2Data.p_outlet - 1)), 0.2);
			D *= CCSStatics.COST_TYPE_4_TEMP_SCALE;
			double capitalCost = Math.pow(10, 6) * ((0.057 * L + 1.8663) + (0.00129 * L)*D + (0.000486 * L + 0.000007) * D*D );
			double annualPipelineOMCost = 144000 + 0.61*(23213 * D + 899 * L - 259269) + 0.7*(39305 * D + 1694*L - 351355);
			this.pipelineCost = childCost;// + capitalCost + annualPipelineOMCost;
			this.pipe_diameter = D;
		}
		//Method 5
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_2){
			double D = Math.pow(0.073*m / co2Data.lou, 0.5)/0.0254;
			D *= CCSStatics.COST_TYPE_5_TEMP_SCALE;
			this.pipelineCost = childCost;// + (10.3 * Math.pow(10, 6) *((0.057*L+1.8663)+(0.00129*L)*D+(0.000486*L+0.000007)*D*D)+10.5*350000*L) / 
						//((1.1*1.1 - 1)/Math.pow(1.1, 20));
			this.pipe_diameter = D;
		}
		//Method 6
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_3){
			double D = Math.pow((4*m)/(18.41*Math.PI*co2Data.lou), 0.5);
			D *= CCSStatics.COST_TYPE_6_TEMP_SCALE;
			this.pipelineCost = childCost;// + 4335 * Math.pow(m/24, 0.5) * 1.02;
			this.pipe_diameter = D;
		}
		
		expData.setPipe_diameter(pipe_diameter);
		expData.setPipelineCost(pipelineCost);
		expData.setPipelineCostMe(pipelineCost - childCost);
		
		return pipelineCost;
	}


	@Override
	public String toString() {
		return "CCSSourceData [co2_amount=" + co2_amount + ", hub=" + hub
				+ ", cost=" + cost + "]";
	}
	
	
}
