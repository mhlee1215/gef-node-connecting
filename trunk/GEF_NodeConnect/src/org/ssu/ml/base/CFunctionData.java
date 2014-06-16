package org.ssu.ml.base;

public class CFunctionData {
	
	private String name = "";
	private Double pvalue = 0.0;
	public CFunctionData(String name, Double pvalue){
		this.name = name;
		this.pvalue = pvalue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPvalue() {
		return pvalue;
	}
	public void setPvalue(Double pvalue) {
		this.pvalue = pvalue;
	}
	@Override
	public String toString() {
		return name+" : "+pvalue;//String.format("%f", pvalue);
	}
	
	
}
