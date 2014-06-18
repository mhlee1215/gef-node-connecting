package ac.kaist.ie.domain;

public class CCSEdgeData {
	float cost;
	
	CCSNodeData src;
	CCSNodeData dst;
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public CCSNodeData getSrc() {
		return src;
	}
	public void setSrc(CCSNodeData src) {
		this.src = src;
	}
	public CCSNodeData getDst() {
		return dst;
	}
	public void setDst(CCSNodeData dst) {
		this.dst = dst;
	}
	@Override
	public String toString() {
		return "{\"cost\":\"" + cost + "\", \"src\":\"" + src
				+ "\", \"dst\":\"" + dst + "\"}";
	}
	
	

}
