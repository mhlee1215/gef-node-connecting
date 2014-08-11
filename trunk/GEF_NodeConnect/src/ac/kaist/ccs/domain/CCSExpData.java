package ac.kaist.ccs.domain;

public class CCSExpData {
	double w_stotal = 0.0;
	double n_train = 0.0;
	double w_p = 0.0;
	double m_train = 0.0;
	double c_comp = 0.0;
	double c_pump = 0.0;
	double c_total = 0.0;
	int n_pump = 0;
	double compPumpCost;
	double pipelineCost;
	double compPumpCostMe;
	
	double s_cost;
	double co2_capture_facility_open_cost;
	double co2_capture_cost;
	double pipeline_cost;
	double hub_open_cost;
	double hub_storage_cost;
	
	double s_selection;
	
	
	
	public double getS_selection() {
		return s_selection;
	}
	public void setS_selection(double s_selection) {
		this.s_selection = s_selection;
	}
	public double getS_cost() {
		return s_cost;
	}
	public void setS_cost(double s_cost) {
		this.s_cost = s_cost;
	}
	public double getCo2_capture_facility_open_cost() {
		return co2_capture_facility_open_cost;
	}
	public void setCo2_capture_facility_open_cost(
			double co2_capture_facility_open_cost) {
		this.co2_capture_facility_open_cost = co2_capture_facility_open_cost;
	}
	public double getCo2_capture_cost() {
		return co2_capture_cost;
	}
	public void setCo2_capture_cost(double co2_capture_cost) {
		this.co2_capture_cost = co2_capture_cost;
	}
	public double getPipeline_cost() {
		return pipeline_cost;
	}
	public void setPipeline_cost(double pipeline_cost) {
		this.pipeline_cost = pipeline_cost;
	}
	public double getHub_open_cost() {
		return hub_open_cost;
	}
	public void setHub_open_cost(double hub_open_cost) {
		this.hub_open_cost = hub_open_cost;
	}
	public double getHub_storage_cost() {
		return hub_storage_cost;
	}
	public void setHub_storage_cost(double hub_storage_cost) {
		this.hub_storage_cost = hub_storage_cost;
	}
	public double getCompPumpCostMe() {
		return compPumpCostMe;
	}
	public void setCompPumpCostMe(double compPumpCostMe) {
		this.compPumpCostMe = compPumpCostMe;
	}
	public double getPipelineCostMe() {
		return pipelineCostMe;
	}
	public void setPipelineCostMe(double pipelineCostMe) {
		this.pipelineCostMe = pipelineCostMe;
	}
	double pipelineCostMe;
	double pipe_diameter;
	public double getW_stotal() {
		return w_stotal;
	}
	public void setW_stotal(double w_stotal) {
		this.w_stotal = w_stotal;
	}
	public double getN_train() {
		return n_train;
	}
	public void setN_train(double n_train) {
		this.n_train = n_train;
	}
	public double getW_p() {
		return w_p;
	}
	public void setW_p(double w_p) {
		this.w_p = w_p;
	}
	public double getM_train() {
		return m_train;
	}
	public void setM_train(double m_train) {
		this.m_train = m_train;
	}
	public double getC_comp() {
		return c_comp;
	}
	public void setC_comp(double c_comp) {
		this.c_comp = c_comp;
	}
	public double getC_pump() {
		return c_pump;
	}
	public void setC_pump(double c_pump) {
		this.c_pump = c_pump;
	}
	public double getC_total() {
		return c_total;
	}
	public void setC_total(double c_total) {
		this.c_total = c_total;
	}
	public int getN_pump() {
		return n_pump;
	}
	public void setN_pump(int n_pump) {
		this.n_pump = n_pump;
	}
	public double getCompPumpCost() {
		return compPumpCost;
	}
	public void setCompPumpCost(double compPumpCost) {
		this.compPumpCost = compPumpCost;
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
	@Override
	public String toString() {
		return "{\"w_stotal\":\"" + w_stotal + "\",\"n_train\":\"" + n_train
				+ "\",\"w_p\":\"" + w_p + "\",\"m_train\":\"" + m_train
				+ "\",\"c_comp\":\"" + c_comp + "\",\"c_pump\":\"" + c_pump
				+ "\",\"c_total\":\"" + c_total + "\",\"n_pump\":\"" + n_pump
				+ "\",\"compPumpCost\":\"" + compPumpCost
				+ "\",\"pipelineCost\":\"" + pipelineCost
				+ "\",\"compPumpCostMe\":\"" + compPumpCostMe
				+ "\",\"s_cost\":\"" + s_cost
				+ "\",\"co2_capture_facility_open_cost\":\""
				+ co2_capture_facility_open_cost + "\",\"co2_capture_cost\":\""
				+ co2_capture_cost + "\",\"pipeline_cost\":\"" + pipeline_cost
				+ "\",\"hub_open_cost\":\"" + hub_open_cost
				+ "\",\"hub_storage_cost\":\"" + hub_storage_cost
				+ "\",\"pipelineCostMe\":\"" + pipelineCostMe
				+ "\",\"pipe_diameter\":\"" + pipe_diameter + "\"}";
	}
	
	
}
