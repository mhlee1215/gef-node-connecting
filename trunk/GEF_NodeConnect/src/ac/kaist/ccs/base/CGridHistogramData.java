package ac.kaist.ccs.base;

import java.awt.Color;
import java.util.Vector;

public class CGridHistogramData {
	
	int gSpacing;
	int gWidth, gHeight;
	int[][] gBean;
	
	public CGridHistogramData(int width, int height, int spacing)
	{
		gSpacing = spacing;
		gWidth = (int)Math.ceil((double)width/spacing)+1;
		gHeight = (int)Math.ceil((double)height/spacing)+1;
		gBean = new int[gWidth][gHeight];
		for(int count = 0 ; count < gWidth ; count++)
			for(int count1 = 0 ; count1 < gHeight ; count1++)
				gBean[count][count1] = 0;
		
		
	}
	
	public void addData(int width, int height)
	{
		int w = (int)(width/gSpacing);
		int h = (int)(height/gSpacing);		
		gBean[w][h]++;
	}
	
	public Double[] generateHistoData()
	{
		Double[] result;
		Vector<Double> vector = new Vector<Double>();
		for(int count = 0 ; count < gWidth ; count++)
		{
			for(int count1 = 0 ; count1 < gHeight ; count1++)
			{
				//result[count*gWidth+count1] = gBean[count][count1];
				if(gBean[count][count1] > 0)
					vector.add((double)gBean[count][count1]);
			}
		}
		
		result = new Double[vector.size()];
		vector.toArray(result);
		
		return result;
	}
	
}
