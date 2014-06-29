package ac.kaist.ccs.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CCSStatics {
	static boolean isInitialized = false;
	
	public static int REGION_SEOUL = 1;
	public static int REGION_INCHEON = 2;
	public static int REGION_KYUNGKI = 3;
	public static int REGION_CHUNGNAM = 4;
	public static int REGION_CHUNGBUK = 5;
	public static int REGION_KANGWON = 6;
	public static int REGION_KYUNGNAM = 7;
	public static int REGION_KYUNGBUK = 8;
	public static int REGION_DAEGU = 9;
	public static int REGION_PUSAN = 10;
	public static int REGION_ULSAN = 11;
	public static int REGION_JEONNAM = 12;
	public static int REGION_JEONBUK = 13;
	
	public static Map<Color, Integer> regionColorMap = null;
	
	public static Map<Integer, List<PlantData> > plantInfoMap = null;
	
	public static int TERRAIN_TYPE_FLAT_DRY = 1;
	public static int TERRAIN_TYPE_MOUNTAINOUS = 2;
	public static int TERRAIN_TYPE_MARCH_WETLAND = 3;
	public static int TERRAIN_TYPE_RIVER = 4;
	public static int TERRAIN_TYPE_HIGHPOPULATION = 5;
	public static int TERRAIN_TYPE_DESERT = 6;
	public static int TERRAIN_TYPE_FOREST = 7;
	public static int TERRAIN_TYPE_OFFSHORE_LESS_500 = 8;
	public static int TERRAIN_TYPE_OFFSHORE_OVER_500 = 9;
	
	public static Map<Color, Integer> terrainColorMap = null;
	
	public static int DIAMETER_688 = 1;
	public static int DIAMETER_927 = 2;
	
	public static int PLANT_TYPE_POWER = 1;
	public static int PLANT_TYPE_IRON_AND_STEEL = 2;
	public static int PLANT_TYPE_OIL_REFINERY = 3;
	public static int PLANT_TYPE_PETROCHEMICAL = 4;
	
	public static int PLANT_TYPE_A = PLANT_TYPE_POWER;
	public static int PLANT_TYPE_B = PLANT_TYPE_IRON_AND_STEEL;
	public static int PLANT_TYPE_C = PLANT_TYPE_OIL_REFINERY;
	public static int PLANT_TYPE_D = PLANT_TYPE_PETROCHEMICAL;
	
	public static double UNIT_STORAGE_COST = 0.72;
	public static double STORAGE_CAPITAL_COST = 10228607.0;
	
	public static Map<Integer, Double> terrainAdditiveCost = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> terrainMultiplier = null;//new HashMap<Integer, Double>();
	
	public static Map<Integer, Double> unitTransportCostMap = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> transportCapitalCostMap = null;//new HashMap<Integer, Double>();
	
	public static Map<Integer, Double> unitCaptureCostMap = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> captureCapitalCostMap = null;//new HashMap<Integer, Double>();

	public static void init(){
		isInitialized = true;
		
		terrainAdditiveCost = new HashMap<Integer, Double>();
		terrainMultiplier = new HashMap<Integer, Double>();
		
		terrainAdditiveCost.put(TERRAIN_TYPE_FLAT_DRY, 50000.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_MOUNTAINOUS, 85000.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_MARCH_WETLAND, 100000.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_RIVER, 300000.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_HIGHPOPULATION, 100000.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_DESERT, 0.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_FOREST, 0.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_OFFSHORE_LESS_500, 0.0);
		terrainAdditiveCost.put(TERRAIN_TYPE_OFFSHORE_OVER_500, 0.0);
		
		terrainMultiplier.put(TERRAIN_TYPE_FLAT_DRY, 1.0);
		terrainMultiplier.put(TERRAIN_TYPE_MOUNTAINOUS, 2.5);
		terrainMultiplier.put(TERRAIN_TYPE_MARCH_WETLAND, 1.0);
		terrainMultiplier.put(TERRAIN_TYPE_RIVER, 1.0);
		terrainMultiplier.put(TERRAIN_TYPE_HIGHPOPULATION, 1.0);
		terrainMultiplier.put(TERRAIN_TYPE_DESERT, 1.3);
		terrainMultiplier.put(TERRAIN_TYPE_FOREST, 3.0);
		terrainMultiplier.put(TERRAIN_TYPE_OFFSHORE_LESS_500, 1.6);
		terrainMultiplier.put(TERRAIN_TYPE_OFFSHORE_OVER_500, 2.7);
		
		unitTransportCostMap = new HashMap<Integer, Double>();
		transportCapitalCostMap = new HashMap<Integer, Double>();
		
		unitTransportCostMap.put(DIAMETER_688, 5.15);
		unitTransportCostMap.put(DIAMETER_927, 3.34);
		transportCapitalCostMap.put(DIAMETER_688, 216900.0);
		transportCapitalCostMap.put(DIAMETER_927, 292247.0);
		
		unitCaptureCostMap = new HashMap<Integer, Double>();
		captureCapitalCostMap = new HashMap<Integer, Double>();
	
		unitCaptureCostMap.put(PLANT_TYPE_POWER, 49.76);
		unitCaptureCostMap.put(PLANT_TYPE_IRON_AND_STEEL, 38.29);
		unitCaptureCostMap.put(PLANT_TYPE_OIL_REFINERY, 80.26);
		unitCaptureCostMap.put(PLANT_TYPE_PETROCHEMICAL, 58.85);
		
		captureCapitalCostMap.put(PLANT_TYPE_POWER, 333.0);
		captureCapitalCostMap.put(PLANT_TYPE_IRON_AND_STEEL, 639.0);
		captureCapitalCostMap.put(PLANT_TYPE_OIL_REFINERY, 283.0);
		captureCapitalCostMap.put(PLANT_TYPE_PETROCHEMICAL, 558.0);
		
		plantInfoMap = new HashMap<Integer, List<PlantData> >();
		
		List<PlantData> plnatList = null;
		
		
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 1, 620));
		plantInfoMap.put(REGION_SEOUL,  plnatList);
		
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 7, 23481));
		plnatList.add(new PlantData(PLANT_TYPE_B, 2, 616));
		plnatList.add(new PlantData(PLANT_TYPE_C, 1, 7870));
		plantInfoMap.put(REGION_INCHEON,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 7, 5744));
		plantInfoMap.put(REGION_KYUNGKI,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 11, 119622));
		plnatList.add(new PlantData(PLANT_TYPE_C, 1, 2986));
		plnatList.add(new PlantData(PLANT_TYPE_D, 3, 2760));
		plantInfoMap.put(REGION_CHUNGNAM,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_D, 5, 16008));
		plantInfoMap.put(REGION_CHUNGBUK,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 5, 8405));
		plnatList.add(new PlantData(PLANT_TYPE_D, 6, 27719));
		plantInfoMap.put(REGION_KANGWON,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 1, 29539));
		plantInfoMap.put(REGION_KYUNGNAM,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 2, 1863));
		plnatList.add(new PlantData(PLANT_TYPE_B, 4, 12261));
		plantInfoMap.put(REGION_KYUNGBUK,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 2, 2179));
		plantInfoMap.put(REGION_DAEGU,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 4, 3537));
		plnatList.add(new PlantData(PLANT_TYPE_B, 1, 112));
		plantInfoMap.put(REGION_PUSAN,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 2, 4257));
		plnatList.add(new PlantData(PLANT_TYPE_C, 1, 4817));
		plnatList.add(new PlantData(PLANT_TYPE_D, 8, 5441));
		plantInfoMap.put(REGION_ULSAN,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 7, 21506));
		plnatList.add(new PlantData(PLANT_TYPE_C, 1, 6103));
		plnatList.add(new PlantData(PLANT_TYPE_D, 3, 2601));
		plantInfoMap.put(REGION_JEONNAM,  plnatList);
		
		plnatList = new ArrayList<PlantData>();
		plnatList.add(new PlantData(PLANT_TYPE_A, 3, 2576));
		plantInfoMap.put(REGION_JEONBUK,  plnatList);
		
		regionColorMap = new HashMap<Color, Integer>();
		
		regionColorMap.put(new Color(231, 0, 18), REGION_SEOUL);
		regionColorMap.put(new Color(248, 181, 48), REGION_INCHEON);
		regionColorMap.put(new Color(255, 244, 92), REGION_KYUNGKI);
		regionColorMap.put(new Color(209, 192, 166), REGION_CHUNGNAM);
		regionColorMap.put(new Color(144, 130, 189), REGION_CHUNGBUK);
		regionColorMap.put(new Color(242, 156, 159), REGION_KANGWON);
		regionColorMap.put(new Color(97, 25, 135), REGION_KYUNGNAM);
		regionColorMap.put(new Color(205, 225, 153), REGION_KYUNGBUK);
		regionColorMap.put(new Color(138, 197, 105), REGION_DAEGU);
		regionColorMap.put(new Color(16, 32, 120), REGION_PUSAN);
		regionColorMap.put(new Color(36, 0, 74), REGION_ULSAN);
		regionColorMap.put(new Color(195, 223, 245), REGION_JEONNAM);
		regionColorMap.put(new Color(18, 181, 176), REGION_JEONBUK);
		
		terrainColorMap = new HashMap<Color, Integer>();
		
		terrainColorMap.put(new Color(190, 234, 116), TERRAIN_TYPE_FLAT_DRY);
		terrainColorMap.put(new Color(210, 165, 146), TERRAIN_TYPE_MOUNTAINOUS);
		//terrainColorMap.put(TERRAIN_TYPE_MARCH_WETLAND, new Color(255));
		terrainColorMap.put(new Color(0, 3, 126), TERRAIN_TYPE_RIVER);
		terrainColorMap.put(new Color(216, 20, 4), TERRAIN_TYPE_HIGHPOPULATION);
		//terrainColorMap.put(TERRAIN_TYPE_DESERT, new Color(255, 255, 255, 255));
		//terrainColorMap.put(TERRAIN_TYPE_FOREST, new Color(255, 255, 255, 255));
		//terrainColorMap.put(TERRAIN_TYPE_OFFSHORE_LESS_500, new Color(255, 255, 255, 255));
		//terrainColorMap.put(TERRAIN_TYPE_OFFSHORE_OVER_500, new Color(255, 255, 255, 255));
		
	}
	
	public static double convertCostViaTerrain(double cost, int type){
		if(!isInitialized) init();
		return terrainMultiplier.get(type)*cost + terrainAdditiveCost.get(type);
	}
	
	public static class PlantData {
		public int industry_type;
		public int plant_num;
		public int co2_amount;
		
		public PlantData(int industry_type, int plant_num, int co2_amount){
			this.industry_type = industry_type;
			this.plant_num = plant_num;
			this.co2_amount = co2_amount;
		}
		
		
	}
	
	
}
