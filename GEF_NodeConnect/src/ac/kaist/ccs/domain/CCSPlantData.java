package ac.kaist.ccs.domain;

import java.util.ArrayList;
import java.util.List;
import ac.kaist.ccs.fig.FigPlantNode;

public class CCSPlantData extends CCSSourceData {
		
	List<Integer> childHub;
	
	public CCSPlantData(int x, int y) {
		super(x, y, 0, 0);
		this.type = CCSNodeData.TYPE_PLANT;
		int size = 7;
		node = new FigPlantNode(x, y, size, size);
		node.setOwner(this);
		childHub = new ArrayList<Integer>();
	}
	
	
	public List<Integer> getChildHub() {
		return childHub;
	}


	public void setChildHub(List<Integer> childHub) {
		this.childHub = childHub;
	}


	@Override
	public String toString() {
		return "CCSPlantData [childHub=" + childHub + "]";
	}
	
	
}
