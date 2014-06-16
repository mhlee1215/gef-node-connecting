package org.kaist.ie.base;

public class CGridState {
	private int space =0; 
    private int xOffset = 0;
    private int yOffset = 0;
    
    public CGridState(int space, int xOffset, int yOffset){
    	this.space = space;
    	this.xOffset = xOffset;
    	this.yOffset = yOffset;
    }
	public int getSpace() {
		return space;
	}
	public void setSpace(int space) {
		this.space = space;
	}
	public int getxOffset() {
		return xOffset;
	}
	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}
	public int getyOffset() {
		return yOffset;
	}
	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}
    
    
}
