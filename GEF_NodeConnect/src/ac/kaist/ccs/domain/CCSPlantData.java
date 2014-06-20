package ac.kaist.ccs.domain;

import java.util.ArrayList;
import java.util.List;
import ac.kaist.ccs.fig.FigPlantNode;

public class CCSPlantData extends CCSSourceData {
		
	List<CCSSourceData> childHub;
	
	public CCSPlantData(int x, int y) {
		super(x, y);
		this.type = CCSNodeData.TYPE_PLANT;
		int size = 7;
		node = new FigPlantNode(x, y, size, size);
		childHub = new ArrayList<CCSSourceData>();
	}
	
	
	public List<CCSSourceData> getChildHub() {
		return childHub;
	}


	public void setChildHub(List<CCSSourceData> childHub) {
		this.childHub = childHub;
	}


	@Override
	public String toString() {
		return "CCSPlantData [childHub=" + childHub + "]";
	}
	
	
}
