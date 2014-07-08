package ac.kaist.ccs.domain;

import java.awt.Color;

import ac.kaist.ccs.base.UiGlobals;
import ac.kaist.ccs.fig.FigCCSLine;

public class CCSEdgeData {
	int index;
	
	
	float cost;
	
	int src;
	int dst;
	
	FigCCSLine edgeFig;
	
	public CCSEdgeData(CCSSourceData src, CCSSourceData dst){
		if(src == null || dst == null) return;
		this.src = src.getIndex();
		this.dst = dst.getIndex();
		
		Color lineColor = new Color(125, 125, 125);
		edgeFig = new FigCCSLine(src.x+src.getNode().getWidth()/2, src.y+src.getNode().getHeight()/2, dst.x+dst.getNode().getWidth()/2, dst.y+dst.getNode().getHeight()/2, lineColor);
		edgeFig.setOwner(this);
	}
	
	@Override
	public CCSEdgeData clone(){
		CCSEdgeData clone = new CCSEdgeData(UiGlobals.getNode(src), UiGlobals.getNode(dst));
		clone.setIndex(index);
		return clone;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDst() {
		return dst;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

	@Override
	public String toString() {
		return "CCSEdgeData [index=" + index + ", cost=" + cost + ", src="
				+ src + ", dst=" + dst + ", edgeFig=" + edgeFig + "]";
	}

	public FigCCSLine getEdgeFig() {
		return edgeFig;
	}

	public void setEdgeFig(FigCCSLine edge) {
		this.edgeFig = edge;
	}
	
	

}
