package org.kaist.ie.base;

public class DoublePair {
	public double x;
	public double y;
	
	public DoublePair(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public String toString(){
		return "x : "+x+", y : "+y;
	}
	
}	
