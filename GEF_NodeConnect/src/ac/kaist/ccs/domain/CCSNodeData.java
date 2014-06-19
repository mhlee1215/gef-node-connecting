package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import ac.kaist.ccs.base.DoublePair;
import ac.kaist.ccs.fig.FigCCSNode;
import ac.kaist.ccs.fig.FigCO2SourceNode;
import ac.kaist.ccs.fig.FigExcludePlantNode;
import ac.kaist.ccs.fig.FigHubNode;
import ac.kaist.ccs.fig.FigJointNode;

public class CCSNodeData {
	int x;
	int y;
	int type;

	public final static int TYPE_SOURCE = 1;
	public final static int TYPE_HUB = 2;
	public final static int TYPE_JOINT = 3;
	public final static int TYPE_PLANT = 4;

	float cost;

	FigCCSNode node;

	public CCSNodeData(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		
		int size = 11;
		
		switch (type) {
		case TYPE_SOURCE:
			node = new FigCO2SourceNode(x, y, size, size);
			break;
		case TYPE_HUB:
			node = new FigHubNode(x, y, size, size);
			break;
		case TYPE_JOINT:
			node = new FigJointNode(x, y, size/2, size/2);
			break;
		case TYPE_PLANT:
			node = new FigExcludePlantNode(x, y, size, size);
			break;
		default:
			node = new FigCCSNode(x, y, size, size);
			break;

		}
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

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public FigCCSNode getNode() {
		return node;
	}

	public void setNode(FigCCSNode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "{\"x\":\"" + x + "\", \"y\":\"" + y + "\", \"type\":\"" + type
				+ "\", \"cost\":\"" + cost + "\", \"node\":\"" + node + "\"}";
	}
	
	
}
