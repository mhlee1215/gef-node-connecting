package org.ssu.ml.base;

import java.awt.Color;
import java.util.Vector;

public class ColorPool {
	public Vector<Color> colorVector = new Vector<Color>();
	
	public ColorPool(){
		colorVector.add(new Color(255, 153, 153));
		colorVector.add(new Color(255, 255, 102));
		colorVector.add(new Color(204, 255, 153));
	}
	
	public Color getColor(int index){
		if(index >= colorVector.size()) index = colorVector.size()-1;
		return colorVector.get(index);
	}
}
