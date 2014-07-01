package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigSourceNode;
import ac.kaist.ccs.fig.FigPlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSNodeData {
	int x;
	int y;
	int type;
	
	FigCCSNode node;
	
	public final static int TYPE_SOURCE = 1;
	public final static int TYPE_HUB = 2;
	public final static int TYPE_JOINT = 3;
	public final static int TYPE_PLANT = 4;


	

//	public static CCSNodeData createNode(int x, int y, int type){
//		CCSNodeData a = null; //= new CCSNodeData(x, y, type);
//		switch (type) {
//		case TYPE_SOURCE:
//			a = new CCSSourceData(x, y);
//			break;
//		case TYPE_HUB:
//			a = new CCSNodeData(x, y, type);
//			break;
//		case TYPE_JOINT:
//			a = new CCSNodeData(x, y, type);
//			break;
//		case TYPE_PLANT:
//			a = new CCSNodeData(x, y, type);
//			break;
//		default:
//			a = new CCSNodeData(x, y, type);
//			break;
//
//		}
//		return a;
//	}
	
	public CCSNodeData(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		
		int size = 7;
		node = new FigCCSNode(x, y, size, size);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public FigCCSNode getNode() {
		return node;
	}

	public void setNode(FigCCSNode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "CCSNodeData [x=" + x + ", y=" + y + ", type=" + type
				+ ", node=" + node + "]";
	}
	
	
}
