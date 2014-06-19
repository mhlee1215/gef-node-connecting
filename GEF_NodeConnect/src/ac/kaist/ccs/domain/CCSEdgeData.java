package ac.kaist.ccs.domain;

import java.awt.Color;

import org.kaist.ccs.fig.FigCCSLine;

public class CCSEdgeData {
	float cost;
	
	CCSNodeData src;
	CCSNodeData dst;
	
	FigCCSLine edge;
	
	public CCSEdgeData(CCSNodeData src, CCSNodeData dst){
		this.src = src;
		this.dst = dst;
		
		Color lineColor = new Color(125, 125, 125);
		edge = new FigCCSLine(src.x+src.getNode().getWidth()/2, src.y+src.getNode().getHeight()/2, dst.x+dst.getNode().getWidth()/2, dst.y+dst.getNode().getHeight()/2, lineColor);
	}
	
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
		return "CCSEdgeData [cost=" + cost + ", src=" + src + ", dst=" + dst
				+ ", edge=" + edge + "]";
	}

	public FigCCSLine getEdge() {
		return edge;
	}

	public void setEdge(FigCCSLine edge) {
		this.edge = edge;
	}
	
	

}
