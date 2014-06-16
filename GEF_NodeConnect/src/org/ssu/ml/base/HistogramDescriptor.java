package org.ssu.ml.base;

import java.awt.Color;

public class HistogramDescriptor {
	int Xvalue;
	int Yvalue;
	Color border = Color.black;
	Color bar = Color.white;
	
	public HistogramDescriptor(int x, int y)
	{
		Xvalue = x;
		Yvalue = y;
	}
	
	public int getXvalue() {
		return Xvalue;
	}
	public void setXvalue(int xvalue) {
		Xvalue = xvalue;
	}
	public int getYvalue() {
		return Yvalue;
	}
	public void setYvalue(int yvalue) {
		Yvalue = yvalue;
	}
	public Color getBorder() {
		return border;
	}
	public void setBorder(Color border) {
		this.border = border;
	}
	public Color getBar() {
		return bar;
	}
	public void setBar(Color bar) {
		this.bar = bar;
	}
	
	
}
