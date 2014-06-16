package org.kaist.ie.base;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Vector;

public class CGridData {
	
	int gSpacing;
	int gWidth, gHeight;
	HashMap<Point, Vector<String>> gNodeBean;
	String delimeter = "\t";
	
	public CGridData(int width, int height, int spacing)
	{
		gSpacing = spacing;
		gWidth = (int)Math.ceil((double)width/spacing)+1;
		gHeight = (int)Math.ceil((double)height/spacing)+1;
		gNodeBean = new HashMap<Point, Vector<String>>();
		for(int count = 0 ; count < gWidth ; count++)
			for(int count1 = 0 ; count1 < gHeight ; count1++)
				gNodeBean.put(new Point(count, count1), new Vector<String>());
	}
	
	public void addData(int width, int height, String name)
	{
		int w = (int)(width/gSpacing);
		int h = (int)(height/gSpacing);		
		gNodeBean.get(new Point(w, h)).add(name);
	}
	
	public String generateData()
	{
		StringBuffer result = new StringBuffer();
		Vector<Double> vector = new Vector<Double>();
		for(int count = 0 ; count < gWidth ; count++)
		{
			for(int count1 = 0 ; count1 < gHeight ; count1++)
			{
				Vector<String> curList = gNodeBean.get(new Point(count, count1));
				if(curList.size() > 0)
				{
					result.append("Grid "+count+","+count1+"\n");
					for(int nCount = 0 ; nCount < curList.size() ; nCount++)
						result.append(curList.get(nCount)+"\n");
				}
			}
		}
		return result.toString();
	}
	
	public String generateDataSquare()
	{
		StringBuffer result = new StringBuffer();
		boolean isHaveData = true;
		String cycleResult = "";
		for(int rowCnt = 0 ; isHaveData ; rowCnt++){
			isHaveData = false;
			cycleResult = "";//Integer.toString(rowCnt);
			for(int count = 0 ; count < gWidth ; count++)
			{
				for(int count1 = 0 ; count1 < gHeight ; count1++)
				{
					Vector<String> curList = gNodeBean.get(new Point(count, count1));
					if(curList.size() == 0) continue;
					if(rowCnt == 0){
						isHaveData = true;
						if(cycleResult.equals(""))
							cycleResult = "Grid_"+count+"_"+count1;
						else
							cycleResult += delimeter+"Grid_"+count+"_"+count1;
					}else if(rowCnt == 1){
                        isHaveData = true;
                        if(cycleResult.equals(""))
                            cycleResult = "na";
                        else
                            cycleResult += delimeter+"na";
                    }else{
						
						if(curList.size() > rowCnt-2)
						{
							isHaveData = true;
							if(cycleResult.equals(""))
							    cycleResult += curList.get(rowCnt-2);
							else
							    cycleResult += delimeter+curList.get(rowCnt-2);
						}else{
						    if(cycleResult.equals(""))
						        cycleResult += "na";
						    else
						        cycleResult += delimeter+"na";
						}
					}
				}
			}
			if(isHaveData)
				result.append(cycleResult+"\n");
		}
		return result.toString();
	}
	
	public String generateDataSquareTrans()
	{
	    StringBuffer result = new StringBuffer();
        boolean isHaveData = true;
        String cycleResult = "";
        
        for (int count = 0; count < gWidth; count++) {
            for (int count1 = 0; count1 < gHeight; count1++) {
                cycleResult = "";
                isHaveData = false;
                Vector<String> curList = gNodeBean.get(new Point(count, count1));
                cycleResult = "Grid_"+count+"_"+count1;
                cycleResult += delimeter+"na";
                for(int nodeNum = 0 ; nodeNum < curList.size() ; nodeNum++){
                    isHaveData = true;
                    cycleResult += delimeter + curList.get(nodeNum);
                }
                if(isHaveData)
                    result.append(cycleResult + "\n");
            }
        }
        return result.toString();
	}
	
}
