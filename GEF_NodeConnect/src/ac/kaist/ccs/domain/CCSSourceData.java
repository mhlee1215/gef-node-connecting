package ac.kaist.ccs.domain;

import java.util.ArrayList;
import java.util.List;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.domain.CCSStatics.CO2StateData;
import ac.kaist.ccs.fig.FigSourceNode;

public class CCSSourceData extends CCSNodeData {
	

	public static void main(String[] argv){
		System.out.println(Math.pow(10, 0.2));
		return;
	}


	Integer index;
	float co2_amount;
	float acc_co2_amount;
	int co2_type;
	double compPumpCost;
	double compPumpCostMe;
	double pipelineCost;
	double pipe_diameter;
	double pipe_diameterMe;
	double openCost;
	float rank;
	int terrain_type;
	int industry_type;

	Integer clusterHub = null;
	//허브 선택 과정에서 할당된 허브 (실제 연결이랑 무관, 허브 선택시 분석 자료로 사용)
	Integer selectionHub = null;
	Integer dst = null;
	int edge;
	boolean isHubCandidate = false;
	
	private double distanceToDst;
	private double distanceToSelectionHub;
	//연결되 자식 소스
	protected List<Integer> childSources;
	//클러스터링 했을때 허브가 포함하는 소스
	protected List<Integer> clusterSources;
	//허브 선택시 소속되는 소
	protected List<Integer> hubSelectionSources;
	//소스에서 연결가능한 허브들의 허브 
	protected List<Integer> outgoingHub;
	
	private List<Integer> visitedNodeForCost;
	
	
	public static int VIEW_TYPE_CO2 = 0;
	public static int VIEW_TYPE_COST = 1;
	public static int VIEW_TYPE_ACC_CO2 = 2;
	public int viewType = VIEW_TYPE_CO2;
	
	CCSExpData expData = null;
	
	boolean isValid = true;

	
	
	public double getDistanceToSelectionHub() {
		if(selectionHub == null) return 0;
		else return CCSUtils.dist(this.getIndex(), selectionHub);
	}
	
	public double getDistanceToSelectionHubKM() {
		return getDistanceToSelectionHub() * CCSStatics.pixelToDistance;
	}

//	public void setDistanceToSelectionHub(float distanceToSelectionHub) {
//		this.distanceToSelectionHub = distanceToSelectionHub;
//	}

	public Integer getSelectionHub() {
		return selectionHub;
	}

	public void setSelectionHub(Integer selectionHub) {
		this.selectionHub = selectionHub;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public double getUnitCaptureCost(){
		return CCSStatics.unitCaptureCostMap.get(this.industry_type);
	}
	
	public double getOpenCost() {
		return openCost;
	}

	public void setOpenCost(double openCost) {
		this.openCost = openCost;
	}
	
	public boolean isHubCandidate() {
		return isHubCandidate;
	}

	public void setHubCandidate(boolean isHubCandidate) {
		this.isHubCandidate = isHubCandidate;
	}

	public CCSExpData getExpData() {
		return expData;
	}

	public void setExpData(CCSExpData expData) {
		this.expData = expData;
	}

	public CCSEdgeData connectTo(CCSSourceData dstNode){
		//this.edge = new CCSEdgeData(this, dstNode);
		dstNode.addChildSource(this.getIndex());
		CCSEdgeData e = new CCSEdgeData(this, dstNode);
		//distanceToDst = (float) Math.sqrt((x-dstNode.getX())*(x-dstNode.getX())+(y-dstNode.getY())*(y-dstNode.getY())) * CCSStatics.pixelToDistance;
		this.dst = dstNode.getIndex();
		e = UiGlobals.addEdge(e);
		this.edge = e.getIndex();
		
		UiGlobals.setNode(this);
		
		return e;
	}
	
	public float getAcc_co2_amount() {
		return acc_co2_amount;
	}

	public void setAcc_co2_amount(float acc_co2_amount) {
		this.acc_co2_amount = acc_co2_amount;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
		this.node.setOwner(this.index);
	}

	public CCSSourceData(){
		super(0, 0, TYPE_SOURCE);
	}
	
	public CCSSourceData(int x, int y, float co2_amount, int terrain_type) {
		super(x, y, CCSNodeData.TYPE_SOURCE);		
		int size = 7;
		node = new FigSourceNode(x, y, size, size);
		node.setOwner(this.index);
		this.co2_amount = co2_amount;
		this.terrain_type = terrain_type;
		childSources = new ArrayList<Integer>();
		clusterSources = new ArrayList<Integer>();
		hubSelectionSources = new ArrayList<Integer>();
		outgoingHub = new ArrayList<Integer>();
		expData = new CCSExpData();
		co2_type = CCSStatics.CO2_STATE_EXTREME;
	}
	
	@Override
	public CCSSourceData clone(){
		//System.out.println("CLONE!!!");
		CCSSourceData clone = new CCSSourceData(x, y, co2_amount, industry_type, terrain_type);
		clone.setIndex(index);
		clone.setHubCandidate(this.isHubCandidate);
		clone.setChildSources(new ArrayList<Integer>(childSources));
		clone.setClusterSources(new ArrayList<Integer>(clusterSources));
		clone.setHubSelectionSources(new ArrayList<Integer>(hubSelectionSources));
		clone.setOutgoingHub(new ArrayList<Integer>(outgoingHub));
		clone.setSelectionHub(selectionHub);
		return clone;
	}
	
	public List<Integer> getHubSelectionSources() {
		return hubSelectionSources;
	}

	public void setHubSelectionSources(List<Integer> hubSelectionSources) {
		this.hubSelectionSources = hubSelectionSources;
	}

	public void addOutgoingHub(int hubIndex){
		outgoingHub.add(hubIndex);
	}
	
	public List<Integer> getOutgoingHub() {
		return outgoingHub;
	}

	public void setOutgoingHub(List<Integer> outgoingHub) {
		this.outgoingHub = outgoingHub;
	}

	public CCSSourceData(int x, int y, float co2_amount, int industry_type, int terrain_type) {
		//super(x, y, CCSNodeData.TYPE_SOURCE);
		this(x, y, co2_amount, terrain_type);
		this.industry_type = industry_type;
		this.openCost = CCSStatics.captureCapitalCostMap.get(industry_type);
	}
	
//	public CCSSourceData(CCSSourceData data, boolean isHub){
//		this(data.x, data.y, data.co2_amount, data.industry_type, data.terrain_type);
//		this.index = data.index;
//		this.isHubCandidate = isHub;
//		this.testVal = 10;
//	}

	public List<Integer> getChildSources() {
		return childSources;
	}

	public void setChildSources(List<Integer> childSources) {
		this.childSources = childSources;
	}
	
	public void addChildSource(int childIndex){
		this.childSources.add(childIndex);
	}
	
	public List<Integer> getClusterSources() {
		return clusterSources;
	}

	public void setClusterSources(List<Integer> clusterSources) {
		this.clusterSources = clusterSources;
	}
	
	public void addClusterSource(int sourceIndex){
		this.clusterSources.add(sourceIndex);
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
		if(clusterHub == null) return null;
		else return UiGlobals.getNode(clusterHub);
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
		if(dst == null) return null;
		else return UiGlobals.getNode(dst);
	}

	public void setDst(CCSSourceData dst) {
		this.dst = dst.getIndex();
	}

	public double getCost() {
		return pipelineCost;
	}
	
	
	public double computeCompressorPumtCost(){
		return computeCompressorPumtCost(this.co2_type);
	}
	public double computeCompressorPumtCost(int co2Type){
		double childCompCost = 0;
		
		//System.out.println("childSources: "+childSources);
		for(Integer childIdx : childSources){
			CCSSourceData childNode = UiGlobals.getNode(childIdx);
			if(childNode.getIndex() == this.index){
				System.err.println("ERROR!! cycle!, childNode.getIndex():"+childNode.getIndex()+", this.index:"+this.index);
				break;
				//System.exit(0);
			}
			childCompCost += childNode.computeCompressorPumtCost(co2Type);
		}
		
		double m = (this.co2_amount*1000)/365; // kCo2 -> tone/day
		CO2StateData co2Data = CCSStatics.co2StateMap.get(co2Type);
		
		double w_stotal = 0.0;
		double n_train = 0.0;
		double w_p = 0.0;
		double m_train = 0.0;
		double c_comp = 0.0;
		double c_pump = 0.0;
		double c_total = 0.0;
		int n_pump = 0;
		
		//초임계
		if(co2Type == CCSStatics.CO2_STATE_EXTREME){
			w_stotal = 4.166 * m;
			n_pump = (int) Math.ceil(this.getDistanceToDst() / 50);
		}
		//고밀도
		else if(co2Type == CCSStatics.CO2_STATE_HIGH){
			w_stotal = 4.166 * m;
			n_pump = (int) Math.ceil(this.getDistanceToDst() / 100);
		}
		//저온
		else if(co2Type == CCSStatics.CO2_STATE_LOW){
			w_stotal = 4.046 * m;
			n_pump = (int) Math.ceil(this.getDistanceToDst() / 200);
		}
		
		n_train = Math.ceil(w_stotal / 40000);
		
		double p_final = co2Data.p_outlet;
		double p_cut_off = 7.38;
		//double p_initial = 0.1;
		
		//초임계 또는 고온
		if(co2Type == CCSStatics.CO2_STATE_EXTREME || co2Type == CCSStatics.CO2_STATE_HIGH){
			w_p = ((1000*10)/((double)24*36))*(m*(p_final - p_cut_off)/(0.75 * co2Data.lou));
		}
		//저온
		else if(co2Type == CCSStatics.CO2_STATE_LOW){
			w_p = 0;
		}
		
		m_train = (1000*m) / (24*3600*n_train);
		
		//수식 이함. 대괄호 닫기가 없음.
		c_comp = m_train * n_train * ((0.13*Math.pow(10, 6))*Math.pow(m_train,-0.71)+(1.40*Math.pow(10, 6))*Math.pow(m_train,-0.6)) * Math.log((p_cut_off)/(CCSStatics.p_initial));
		c_pump = ((1.11 * Math.pow(10, 6))*(w_p / 1000)) + 70000;
		c_total = 0.19 * (c_comp + c_pump * n_pump);
		
		this.compPumpCost = childCompCost + c_total;
		
		
		expData.setW_stotal(w_stotal);
		expData.setN_train(n_train);
		expData.setW_p(w_p);
		expData.setM_train(m_train);
		expData.setC_comp(c_comp);
		expData.setC_pump(c_pump);
		expData.setC_total(c_total);
		expData.setN_pump(n_pump);
		expData.setCompPumpCost(compPumpCost);
		expData.setCompPumpCostMe(c_total);
//		double W_stotal = 0.0;
//		double N_train = 0.0;
//		double W_p = 0.0;
//		double m_train = 0.0;
//		double C_comp = 0.0;
//		double C_pump = 0.0;
//		double C_total = 0.0;
//		int N_pump = 0;
		
		return this.compPumpCost;
	}
	
	public float computeCo2(){
		float childCo2 = 0;
		
		if(childSources.size() > 0)
			System.out.println("compute co2 "+this.index+", childSources("+childSources.size()+"): "+childSources);
		for(Integer childIdx : childSources){
			CCSSourceData childNode = UiGlobals.getNode(childIdx);
			if(childNode.getIndex() == this.index){
				System.err.println("ERROR!! cycle!, childNode.getIndex():"+childNode.getIndex()+", this.index:"+this.index);
				break;
				//System.exit(0);
			}
			childCo2 += childNode.computeCo2();
		}
		
		this.acc_co2_amount = childCo2 + this.co2_amount;
		return this.acc_co2_amount;
	}
	
	public double computeCost(int costType){
		return computeCost(costType, this.co2_type);
	}
	
	public double computeCost(int costType, int co2Type){
		return computeCost(costType, co2Type, childSources, this.getDistanceToDst());
	}
	
	public double computeCost(int costType, int co2Type, List<Integer> sources, double distanceToTarget){
		double childCost = 0;
		computeCo2();
		
		System.out.println("compute cost "+this.index+", sources: "+sources);
		for(Integer childIdx : sources){
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
		double L = distanceToTarget;//this.getDistanceToDst();
		double f = 1.0;
		System.out.println("Distance : "+L);
		CO2StateData co2Data = CCSStatics.co2StateMap.get(co2Type);
		
		//Method 1
		if(costType == CCSStatics.COST_TYPE_THE_OGDEN_MODELS){
			//double D = Math.pow((5084.5*L *f*m*m) / (co2Data.p_outlet*co2Data.p_outlet-1), 0.2);
			//System.out.println("denominator : "+(CCSStatics.p_initial*CCSStatics.p_initial-co2Data.p_outlet*co2Data.p_outlet));
			//System.out.println("all : "+(55661.6*L*m*m) / (CCSStatics.p_initial*CCSStatics.p_initial-co2Data.p_outlet*co2Data.p_outlet));
			double denom = (co2Data.p_inlet*co2Data.p_inlet-co2Data.p_outlet*co2Data.p_outlet);
			double D = Math.pow((55661.6*L*m*m) / denom, 0.2);
			D *= CCSStatics.COST_TYPE_1_TEMP_SCALE;
			//this.pipelineCost = childCost + Math.pow((m / 1600), 0.48) * Math.pow((L / 100), 0.24) * 0.15 * L;
			this.pipelineCost = childCost + 700*Math.pow((m / 1600), 0.48) * Math.pow((L / 100), 0.24) * 0.19 * 1000 * L;
			this.pipe_diameter = D;
		}
		//Method 2
		else if(costType == CCSStatics.COST_TYPE_MIT_MODEL){
			double D = getConvergedDiameter(m, L, co2Data.lou, co2Data.mu, co2Data.p_outlet, co2Data.p_inlet);
			D *= CCSStatics.COST_TYPE_2_TEMP_SCALE;
			this.pipelineCost = childCost + (20989 * D * L * 0.15) + 3100*L;
			this.pipe_diameter = D;
		}
		//Method 3
		else if(costType == CCSStatics.COST_TYPE_ECOFYS_MODEL){
			//double D = Math.pow((1.155*m*m*L)/((co2Data.p_outlet - 1)*co2Data.lou), 0.2);
			double D = Math.pow((1245*m*m*L)/((co2Data.p_inlet-co2Data.p_outlet)*co2Data.lou), 0.2);
			D *= CCSStatics.COST_TYPE_3_TEMP_SCALE;
			this.pipelineCost = childCost + 3294.0 * D * L;
			this.pipe_diameter = D;
		}
		//Method 4
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_PH4_6){
			//double D = Math.pow((L*co2Data.lou*m*m) / (25041*(co2Data.p_outlet - 1)), 0.2);
			double D = Math.pow((L*co2Data.lou*m*m) / (23506*(co2Data.p_inlet - co2Data.p_outlet)), 0.2);
			D *= CCSStatics.COST_TYPE_4_TEMP_SCALE;
			double capitalCost = Math.pow(10, 6) * ((0.057 * L + 1.8663) + (0.00129 * L)*D + (0.000486 * L + 0.000007) * D*D );
			double annualPipelineOMCost = 144000 + 0.61*(23213 * D + 899 * L - 259269) + 0.7*(39305 * D + 1694*L - 351355);
			this.pipelineCost = childCost + capitalCost + annualPipelineOMCost;
			this.pipe_diameter = D;
		}
		//Method 5
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_2){
			double D = Math.pow(m / co2Data.lou, 0.5)*3.38;
			D *= CCSStatics.COST_TYPE_5_TEMP_SCALE;
			//this.pipelineCost = childCost + (10.3 * Math.pow(10, 6) *((0.057*L+1.8663)+(0.00129*L)*D+(0.000486*L+0.000007)*D*D)+10.5*350000*L) / 
			//			((1.1*1.1 - 1)/Math.pow(1.1, 20));
			this.pipelineCost = childCost + 0.12 * 1.3 * Math.pow(10, 6) * ((0.057*L+1.8663)+(0.00129*L)*D+(0.000486*L+0.000007)*D*D) + 0.14 * 1.3 * 35000 * L;
			this.pipe_diameter = D;
		}
		//Method 6
		else if(costType == CCSStatics.COST_TYPE_IEA_GHG_2005_3){
			//double D = Math.pow((4*m)/(18.41*Math.PI*co2Data.lou), 0.5);
			double D = Math.pow(0.0352*m, 0.5);
			D *= CCSStatics.COST_TYPE_6_TEMP_SCALE;
			this.pipelineCost = childCost + 4335 * Math.pow(m/24, 0.5) * 1.02;
			this.pipe_diameter = D;
		}
		
		expData.setPipe_diameter(pipe_diameter);
		expData.setPipelineCost(pipelineCost);
		expData.setPipelineCostMe(pipelineCost - childCost);
		
		return pipelineCost;
	}
	
	public double getConvergedDiameter(double m, double L, double lou, double mu, double p_outlet, double p_inlet){
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
									 (Math.PI*Math.PI*lou*((p_inlet - p_outlet) / L)*(Math.pow(10, 6)/1000)))
					 , 0.2);
			 //System.out.println("D:"+D+", RE:"+Re+", F_f:"+F_f);
			 //System.out.println((Math.pow((1000 / (double)(24*3600)), 2)));
		 }
		 Dc = D;
		 return Dc;
	}

	public void setCost(float cost) {
		this.pipelineCost = cost;
	}
	
	public void addCost(float cost) {
		this.pipelineCost += cost;
	}

	public int getCo2_type() {
		return co2_type;
	}

	public void setCo2_type(int co2_type) {
		this.co2_type = co2_type;
	}

	public double getCompPumpCost() {
		return compPumpCost;
	}

	public void setCompPumpCost(double compPumpCost) {
		this.compPumpCost = compPumpCost;
	}

	public double getCompPumpCostMe() {
		return compPumpCostMe;
	}

	public void setCompPumpCostMe(double compPumpCostMe) {
		this.compPumpCostMe = compPumpCostMe;
	}

	public double getPipelineCost() {
		return pipelineCost;
	}

	public void setPipelineCost(double pipelineCost) {
		this.pipelineCost = pipelineCost;
	}

	public double getPipe_diameter() {
		return pipe_diameter;
	}

	public void setPipe_diameter(double pipe_diameter) {
		this.pipe_diameter = pipe_diameter;
	}

	public double getPipe_diameterMe() {
		return pipe_diameterMe;
	}

	public void setPipe_diameterMe(double pipe_diameterMe) {
		this.pipe_diameterMe = pipe_diameterMe;
	}

	public double getDistanceToDst() {
		if(dst == null) return 0.0;
		return CCSUtils.dist(this.getIndex(), dst);
	}
	
	public double getDistanceToDstKM(){
		return this.getDistanceToDst() * CCSStatics.pixelToDistance;
	}

//	public void setDistanceToDst(float distanceToDst) {
//		this.distanceToDst = distanceToDst;
//	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public void setClusterHub(int clusterHub) {
		this.clusterHub = clusterHub;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	@Override
	public String toString() {
		return "{\"index\":\"" + index + "\",\"co2_amount\":\"" + co2_amount
				+ "\",\"acc_co2_amount\":\"" + acc_co2_amount
				+ "\",\"co2_type\":\"" + co2_type + "\",\"compPumpCost\":\""
				+ compPumpCost + "\",\"compPumpCostMe\":\"" + compPumpCostMe
				+ "\",\"pipelineCost\":\"" + pipelineCost
				+ "\",\"pipe_diameter\":\"" + pipe_diameter
				+ "\",\"pipe_diameterMe\":\"" + pipe_diameterMe
				+ "\",\"rank\":\"" + rank + "\",\"terrain_type\":\""
				+ terrain_type + "\",\"industry_type\":\"" + industry_type
				+ "\",\"clusterHub\":\"" + clusterHub + "\",\"dst\":\"" + dst
				+ "\",\"edge\":\"" + edge + "\",\"isHubCandidate\":\""
				+ isHubCandidate + "\",\"distanceToDst\":\"" + this.getDistanceToDst()
				+ "\",\"childSources\":\"" + childSources
				+ "\",\"viewType\":\"" + viewType + "\",\"expData\":\""
				+ expData + "\"}";
	}
	
	
	public String getNodeType(){
		return "SOURCE";
	}
	
	public double getUnitStorageCost(){
		return CCSStatics.UNIT_STORAGE_COST;
	}
	
	public double getHubOpenCost(){
		return this.openCost + CCSStatics.STORAGE_CAPITAL_COST;
	}
	
	public double computeHubSelectionCost(){
		double s_selection = 0.0;
		double acc_co2 = 0.0;
		
		for(int childIndex : this.getHubSelectionSources()){
			CCSSourceData sData = UiGlobals.getNode(childIndex);
			//System.out.println("outGoing : "+sData.getOutgoingHub());
			s_selection += (sData.getCo2_amount() / CCSUtils.dist(this, sData)) * (1 / (double)sData.getOutgoingHub().size());
			acc_co2 += sData.getCo2_amount();
		}
		
		this.expData.setS_selection(s_selection);
		this.acc_co2_amount = (float) (acc_co2 + this.getCo2_amount());
		return s_selection * this.getHubSelectionSources().size();
	}
	
	public double computeHubCost(int costType, int co2Type){
		double s_cost = 0.0;
		double co2_capture_facility_open_cost = 0.0;
		double co2_capture_cost = 0.0;
		double pipeline_cost = 0.0;
		double hub_open_cost = getHubOpenCost();
		double hub_storage_cost = 0.0;
		double co2_acc_amount = this.getCo2_amount();
		
		for(int childIndex : this.getHubSelectionSources()){
			CCSSourceData childNode = UiGlobals.getNode(childIndex);
			System.out.println("childNode.getDistanceToSelectionHub():"+childNode.getDistanceToSelectionHub());
			childNode.computeCost(costType, co2Type, childNode.getHubSelectionSources(), childNode.getDistanceToSelectionHub());
			co2_capture_facility_open_cost+= childNode.getOpenCost();
			co2_capture_cost += childNode.getUnitCaptureCost()*childNode.getCo2_amount();
			pipeline_cost += childNode.getPipelineCost();
			co2_acc_amount += childNode.getCo2_amount();
		}
		
		hub_storage_cost = co2_acc_amount * getUnitStorageCost();
		
		s_cost = co2_capture_facility_open_cost + co2_capture_cost + pipeline_cost + hub_open_cost + hub_storage_cost;
		
		this.expData.setS_cost(s_cost);
		this.expData.setCo2_capture_facility_open_cost(co2_capture_facility_open_cost);
		this.expData.setCo2_capture_cost(co2_capture_cost);
		this.expData.setPipeline_cost(pipeline_cost);
		this.expData.setHub_open_cost(hub_open_cost);
		this.expData.setHub_storage_cost(hub_storage_cost);
		
		return s_cost;
	}
	
}
