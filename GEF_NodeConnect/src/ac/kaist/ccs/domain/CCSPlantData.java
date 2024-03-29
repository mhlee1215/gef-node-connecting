package ac.kaist.ccs.domain;

import java.util.ArrayList;
import java.util.List;

import ac.kaist.ccs.fig.FigPlantNode;

public class CCSPlantData extends CCSSourceData {
		
	int nameKey;
	int capacity;
	int geo_info;
	List<Integer> childHub;
	
//	public CCSPlantData(int x, int y) {
//		super(x, y, 0, 0);
//		this.type = CCSNodeData.TYPE_PLANT;
//		int size = 7;
//		node = new FigPlantNode(x, y, size, size);
//		node.setOwner(this);
//		childHub = new ArrayList<Integer>();
//	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getGeo_info() {
		return geo_info;
	}
	
	public String getGeo_infoString() {
		return CCSStatics.geoInfoStringMap.get(this.geo_info);
	}

	public void setGeo_info(int geo_info) {
		this.geo_info = geo_info;
	}

	public CCSPlantData(int x, int y, int key, int capacity, int geo_info) {
		super(x, y, 0, 0);
		this.nameKey = key;
		this.type = CCSNodeData.TYPE_PLANT;
		this.capacity = capacity;
		this.geo_info = geo_info;
		int size = 7;
		node = new FigPlantNode(x, y, size, size);
		node.setOwner(this);
		childHub = new ArrayList<Integer>();
	}
	
	@Override
	public CCSPlantData clone(){
		CCSPlantData clone = new CCSPlantData(x, y, nameKey, capacity, geo_info);
		clone.setIndex(index);
		clone.setChildSources(new ArrayList<Integer>(childSources));
		return clone;
	}
	
	
	public List<Integer> getChildHub() {
		return childHub;
	}


	public void setChildHub(List<Integer> childHub) {
		this.childHub = childHub;
	}


	@Override
	public String toString() {
		return "CCSPlantData [capacity=" + capacity + ", geo_info=" + geo_info
				+ ", childHub=" + childHub + "]";
	}
	
	@Override
	public String getNodeType(){
		return "PLANT";
	}
	
	public String getNameString(){
		return CCSStatics.storageNameStringMap.get(this.nameKey);
	}
	

	
}
