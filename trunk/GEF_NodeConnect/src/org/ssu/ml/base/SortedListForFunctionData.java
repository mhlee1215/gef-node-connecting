package org.ssu.ml.base;

import java.util.Vector;

public class SortedListForFunctionData extends Vector<CFunctionData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean add(CFunctionData functionData){
		if(this.size() == 0) super.add(functionData);
		else{
			Double compareValue = functionData.getPvalue();
			int pitIndex = 0;
			boolean findPit = false;
			for(pitIndex = 0 ; pitIndex < this.size() ; pitIndex++){
				if(this.get(pitIndex).getPvalue() > compareValue){
					this.add(pitIndex, functionData);
					findPit = true;
					break;
				}
			}
			if(!findPit) super.add(functionData);
		}
		
		return true;
	}
}
