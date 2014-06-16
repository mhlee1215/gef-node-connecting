package org.ssu.ml.ui;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.ssu.ml.base.DoublePair;

public class CNodeData {
	public static int DEFAULT_SIZE = 5000;
	Vector<Float> locxVector = null;
	Vector<Float> locyVector = null;
	Vector<String> pointerName = null;
	Vector<Integer> groups = null;
	
	float minXValue = 100000000;
	float maxXValue = -10000000;
	float minYValue = 100000000;
	float maxYValue = -10000000;
	
	
	int groupNum = 0;
	double pre_scale = 1;
	int padding = 10;
	
	static final Color defaultColor = new Color(132, 179, 255);
	
	
	public static Color getDefaultColor() {
		return defaultColor;
	}

	public CNodeData(){
		locxVector = new Vector<Float>(DEFAULT_SIZE);
    	locyVector = new Vector<Float>(DEFAULT_SIZE);
    	pointerName = new Vector<String>(DEFAULT_SIZE);
    	groups = new Vector<Integer>(DEFAULT_SIZE);
	}
	
	public String toString(int index)
	{
		String result = "";
		
		if(index >= size())
			result = "Index over the last index.";
		else
			result = pointerName.get(index)+" ["+locxVector.get(index)+", "+locyVector.get(index)+"]";
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

	
	public void insertItem(String name, float locx, float locy)
	{
		locxVector.add(locx);
		locyVector.add(locy);
		pointerName.add(name);
		
		if(locx < minXValue) minXValue = locx;
		else if(locx > maxXValue) maxXValue = locx;
		if(locy < minYValue) minYValue = locy;
		else if(locy > maxYValue) maxYValue = locy;
		
		Random random = new Random();		//need more work.
		groups.add(random.nextInt(5));	
	}
	
//	public static Color getColor(int group)
//	{
//		if(group == 0) return Color.yellow;
//		else if(group == 1) return Color.green;
//		else if(group == 2) return Color.red;
//		else if(group == 3) return Color.blue;
//		else if(group == 3) return Color.magenta;
//		return Color.black;
//	}
	
	public float getMinXValue() {
		return minXValue;
	}

	public void setMinXValue(float minXValue) {
		this.minXValue = minXValue;
	}

	public float getMaxXValue() {
		return maxXValue;
	}

	public void setMaxXValue(float maxXValue) {
		this.maxXValue = maxXValue;
	}

	public float getMinYValue() {
		return minYValue;
	}

	public void setMinYValue(float minYValue) {
		this.minYValue = minYValue;
	}

	public float getMaxYValue() {
		return maxYValue;
	}

	public void setMaxYValue(float maxYValue) {
		this.maxYValue = maxYValue;
	}

	public Color getColor(int index)
	{
		return defaultColor;
//		int group = groups.get(index);
//		if(group == 0) return Color.yellow;
//		else if(group == 1) return Color.green;
//		else if(group == 2) return Color.red;
//		else if(group == 3) return Color.blue;
//		else if(group == 3) return Color.magenta;
//		return Color.black;
	} 
	
	public HashMap<String, DoublePair> getHashMap()
	{
		HashMap<String, DoublePair> result = new HashMap<String, DoublePair>();
		for(int count = 0 ; count < size() ; count++)
		{
			DoublePair point = new DoublePair(locxVector.get(count), locyVector.get(count));
			
			result.put(pointerName.get(count), point);
		}
		return result;
	}
	
	public int size(){
		return pointerName.size();
	}
	
	public float[] getLocxArry(){
		float[] result = new float[locxVector.size()];
		for(int count = 0 ; count < locxVector.size() ; count++)
			result[count] = locxVector.get(count);
		return result;
	}
	public float[] getLocyArry(){
		float[] result = new float[locyVector.size()];
		for(int count = 0 ; count < locyVector.size() ; count++)
			result[count] = locyVector.get(count);
		return result;
	}
	
	public String getPointerName(int count)
	{
		return pointerName.get(count);
	}
	
	public int getGroup(int count)
	{
		return groups.get(count);
	}
	
	public int getSize(){
		return pointerName.size();
	}
}
