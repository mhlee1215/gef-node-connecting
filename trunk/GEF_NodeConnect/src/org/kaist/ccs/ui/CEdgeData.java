package org.kaist.ccs.ui;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class CEdgeData {
	public static int DEFAULT_SIZE = 5000;
	Vector<Float> weight = null;
	Vector<String> srcName = null;
	Vector<String> destName = null;
	Vector<Integer> groups = null;
	
	
	int groupNum = 0;
	double pre_scale = 1;
	int padding = 10;
	
	public CEdgeData(){
		weight = new Vector<Float>(DEFAULT_SIZE);
		srcName = new Vector<String>(DEFAULT_SIZE);
		destName = new Vector<String>(DEFAULT_SIZE);
		groups = new Vector<Integer>(DEFAULT_SIZE);
	}
	
	public String toString(int index)
	{
		String result = "";
		
		if(index >= size())
			result = "Index over the last index.";
		else
			result = srcName.get(index)+", "+destName.get(index)+" ["+weight.get(index)+"]";
		return result;
	}
	

	public double getPre_scale() {
		return pre_scale;
	}

	public void setPre_scale(double preScale) {
		pre_scale = preScale;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	
	public void insertItem(String srcName, String destName, float weight)
	{
		this.srcName.add(srcName);
		this.destName.add(destName);
		this.weight.add(weight);
		
		Random random = new Random();		//need more work.
		groups.add(random.nextInt(5));

	}
	
	
	

	public static int getDEFAULT_SIZE() {
		return DEFAULT_SIZE;
	}

	public static void setDEFAULT_SIZE(int dEFAULTSIZE) {
		DEFAULT_SIZE = dEFAULTSIZE;
	}

	public Vector<Float> getWeight() {
		return weight;
	}

	public void setWeight(Vector<Float> weight) {
		this.weight = weight;
	}

	public Vector<String> getSrcName() {
		return srcName;
	}

	public void setSrcName(Vector<String> srcName) {
		this.srcName = srcName;
	}

	public Vector<String> getDestName() {
		return destName;
	}

	public void setDestName(Vector<String> destName) {
		this.destName = destName;
	}

	public Vector<Integer> getGroups() {
		return groups;
	}

	public void setGroups(Vector<Integer> groups) {
		this.groups = groups;
	}


	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public Color getColor(int index)
	{
		int group = groups.get(index);
		if(group == 0) return Color.yellow;
		else if(group == 1) return Color.green;
		else if(group == 2) return Color.red;
		else if(group == 3) return Color.blue;
		else if(group == 3) return Color.magenta;
		return Color.black;
	} 
	
	public String[] getSrcNameArry(){
		String[] result = new String[srcName.size()];
		for(int count = 0 ; count < srcName.size() ; count++)
			result[count] = srcName.get(count);
		return result;
	}
	
	public String[] getDestNameArry(){
		String[] result = new String[destName.size()];
		for(int count = 0 ; count < destName.size() ; count++)
			result[count] = destName.get(count);
		return result;
	}
	
	public int size(){
		return srcName.size();
	}

}
