package ac.kaist.ccs.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.kaist.ccs.base.UiGlobals;

public class CCSStatics {
	static boolean isInitialized = false;
	
	public static BufferedImage refImg;
	public static BufferedImage refTerrainImg;
	
	public static float pixelToDistance = (float) (51.2 / 71);
	public static float kilometerToMile = 0.621f;
	
	//초임
	public static int CO2_STATE_EXTREME = 1;
	//고밀도
	public static int CO2_STATE_HIGH = 2;
	//저온
	public static int CO2_STATE_LOW = 1;
	
	public static Map<Integer, CO2StateData> co2StateMap = null;
	
	public static int STORAGE_ONSHORE_CHUNGNAM = 1;
	public static int STORAGE_ONSHORE_TAEBACKSAN = 2;
	public static int STORAGE_ONSHORE_MYUNGYEONG = 3;
	public static int STORAGE_ONSHORE_GYEONGSANG = 4;
	public static int STORAGE_ONSHORE_BUKPYEONG = 5;
	
	public static int STORAGE_OFFSHORE_BOKPYEONG = 6;
	public static int STORAGE_OFFSHORE_ULLEUNG = 7;
	public static int STORAGE_OFFSHORE_JEJU = 8;
	public static int STORAGE_OFFSHORE_GUNSAN = 9;
	public static int STORAGE_OFFSHORE_HEUKSAN = 10;
	public static int STORAGE_OFFSHORE_POHANG = 11;
	
	public static int GEO_INFO_COALBED = 1;
	public static int GEO_INFO_SANDSTONE = 2;
	public static int GEO_INFO_SALINEAQUIFER = 3;
	public static int GEO_INFO_EOR = 4;
	
	
	public static Map<Integer, StorageData> storageMap = null;
	
	
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
	
	public static Map<Integer, Integer> hubNumberMap = null;
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
	
	public static int COST_TYPE_1 = 1;
	public static int COST_TYPE_2 = 2;
	public static int COST_TYPE_3 = 3;
	public static int COST_TYPE_4 = 4;
	public static int COST_TYPE_5 = 5;
	public static int COST_TYPE_6 = 6;
	
	public static int COST_TYPE_THE_OGDEN_MODELS = COST_TYPE_1;
	public static int COST_TYPE_MIT_MODEL = COST_TYPE_2;
	public static int COST_TYPE_ECOFYS_MODEL = COST_TYPE_3;
	public static int COST_TYPE_IEA_GHG_PH4_6 = COST_TYPE_4;
	public static int COST_TYPE_IEA_GHG_2005_2 = COST_TYPE_5;
	public static int COST_TYPE_IEA_GHG_2005_3 = COST_TYPE_6;
	
	public static int CONNECT_TYPE_STAR = 1;
	public static int CONNECT_TYPE_TREE = 2;
	public static int CONNECT_TYPE_BACKBONE = 3;
	public static int CONNECT_TYPE_HYBRID = 4;
	
	public static double UNIT_STORAGE_COST = 0.72;
	public static double STORAGE_CAPITAL_COST = 10228607.0;
	
	public static Map<Integer, Double> terrainAdditiveCost = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> terrainMultiplier = null;//new HashMap<Integer, Double>();
	
	public static Map<Integer, Double> unitTransportCostMap = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> transportCapitalCostMap = null;//new HashMap<Integer, Double>();
	
	public static Map<Integer, Double> unitCaptureCostMap = null;//new HashMap<Integer, Double>();
	public static Map<Integer, Double> captureCapitalCostMap = null;//new HashMap<Integer, Double>();
	
	public static Map<Integer, String> plantTypeStringMap = null;
	public static Map<Integer, String> plantTypeStringShortMap = null;
	public static Map<Integer, String> terrainTypeStringMap = null;
	public static Map<Integer, String> terrainTypeStringShortMap = null;

	public static void init(){
		isInitialized = true;
		
		co2StateMap = new HashMap<Integer, CO2StateData>();
		co2StateMap.put(CO2_STATE_EXTREME, new CO2StateData(6.16E-05, 745.5, 14f, 40, 4));
		co2StateMap.put(CO2_STATE_HIGH, new CO2StateData(9.93E-05, 904.9, 8.5f, 10, 2));
		co2StateMap.put(CO2_STATE_LOW, new CO2StateData(1.17E-04, 964.5, 6.5f, -20, 1));
	
		storageMap = new HashMap<Integer, StorageData>();
		storageMap.put(STORAGE_ONSHORE_CHUNGNAM, new StorageData(110, 330, 10, GEO_INFO_COALBED));
		storageMap.put(STORAGE_ONSHORE_TAEBACKSAN, new StorageData(400, 200, 6, GEO_INFO_COALBED));
		storageMap.put(STORAGE_ONSHORE_MYUNGYEONG, new StorageData(310, 350, 13, GEO_INFO_COALBED));
		storageMap.put(STORAGE_ONSHORE_GYEONGSANG, new StorageData(330, 415, 535, GEO_INFO_SANDSTONE));
		storageMap.put(STORAGE_ONSHORE_BUKPYEONG, new StorageData(420, 140, 141, GEO_INFO_SANDSTONE));
		storageMap.put(STORAGE_OFFSHORE_BOKPYEONG, new StorageData(480, 135, 877, GEO_INFO_SALINEAQUIFER));
		storageMap.put(STORAGE_OFFSHORE_ULLEUNG, new StorageData(490, 160, 3018, GEO_INFO_EOR));
		storageMap.put(STORAGE_OFFSHORE_JEJU, new StorageData(100, 695, 95101, GEO_INFO_EOR));
		storageMap.put(STORAGE_OFFSHORE_GUNSAN, new StorageData(40, 420, 254, GEO_INFO_EOR));
		storageMap.put(STORAGE_OFFSHORE_HEUKSAN, new StorageData(5, 680, 0, GEO_INFO_EOR));
		storageMap.put(STORAGE_OFFSHORE_POHANG, new StorageData(480, 440, 38, GEO_INFO_SALINEAQUIFER));
		
		
		
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
		
		hubNumberMap = new HashMap<Integer, Integer>();
		hubNumberMap.put(REGION_SEOUL, 0);
		hubNumberMap.put(REGION_INCHEON, 3);
		hubNumberMap.put(REGION_KYUNGKI, 2);
		hubNumberMap.put(REGION_CHUNGNAM, 3);
		hubNumberMap.put(REGION_CHUNGBUK, 1);
		hubNumberMap.put(REGION_KANGWON, 2);
		hubNumberMap.put(REGION_KYUNGNAM, 0);
		hubNumberMap.put(REGION_KYUNGBUK, 1);
		hubNumberMap.put(REGION_DAEGU, 0);
		hubNumberMap.put(REGION_PUSAN, 1);
		hubNumberMap.put(REGION_ULSAN, 3);
		hubNumberMap.put(REGION_JEONNAM, 3);
		hubNumberMap.put(REGION_JEONBUK, 1);
		
		
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
		
		plantTypeStringMap = new HashMap<Integer, String>();
		plantTypeStringMap.put(PLANT_TYPE_A,  "Power Plant");
		plantTypeStringMap.put(PLANT_TYPE_B,  "Iron and Steel");
		plantTypeStringMap.put(PLANT_TYPE_C,  "Oil Refinery");
		plantTypeStringMap.put(PLANT_TYPE_D,  "Petrochemical");
		
		plantTypeStringShortMap = new HashMap<Integer, String>();
		plantTypeStringShortMap.put(PLANT_TYPE_A,  "A");
		plantTypeStringShortMap.put(PLANT_TYPE_B,  "B");
		plantTypeStringShortMap.put(PLANT_TYPE_C,  "C");
		plantTypeStringShortMap.put(PLANT_TYPE_D,  "D");
		
		terrainTypeStringShortMap = new HashMap<Integer, String>();
		terrainTypeStringShortMap.put(TERRAIN_TYPE_FLAT_DRY,  "DRY");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_MOUNTAINOUS,  "MTN");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_MARCH_WETLAND,  "WET");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_RIVER,  "RIVER");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_HIGHPOPULATION,  "HPP");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_DESERT,  "DST");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_FOREST,  "FST");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_OFFSHORE_LESS_500,  "O<500");
		terrainTypeStringShortMap.put(TERRAIN_TYPE_OFFSHORE_OVER_500,  "O>500");
		
		terrainTypeStringMap = new HashMap<Integer, String>();
		terrainTypeStringMap.put(TERRAIN_TYPE_FLAT_DRY,  "Flat and Dry");
		terrainTypeStringMap.put(TERRAIN_TYPE_MOUNTAINOUS,  "Mountainous");
		terrainTypeStringMap.put(TERRAIN_TYPE_MARCH_WETLAND,  "March Wetland");
		terrainTypeStringMap.put(TERRAIN_TYPE_RIVER,  "River");
		terrainTypeStringMap.put(TERRAIN_TYPE_HIGHPOPULATION,  "High Population");
		terrainTypeStringMap.put(TERRAIN_TYPE_DESERT,  "Desert");
		terrainTypeStringMap.put(TERRAIN_TYPE_FOREST,  "Forest");
		terrainTypeStringMap.put(TERRAIN_TYPE_OFFSHORE_LESS_500,  "Offshore < 500");
		terrainTypeStringMap.put(TERRAIN_TYPE_OFFSHORE_OVER_500,  "Offshore > 500");
		
		
		
	}
	
	public static void updateScalingFactor(int viewType){
		double maxCost = 0;
		for(Integer key : UiGlobals.getNodes().keySet()){
			CCSSourceData data = UiGlobals.getNodes().get(key);
			
			double elementValue = 0;//data.pipelineCost;
			
			if(viewType == CCSSourceData.VIEW_TYPE_CO2)
				elementValue = data.co2_amount;
			else if(viewType == CCSSourceData.VIEW_TYPE_ACC_CO2)
				elementValue = data.acc_co2_amount;
			else if(viewType == CCSSourceData.VIEW_TYPE_COST)
				elementValue = Math.pow(data.pipelineCost, 1);	
			
			if(maxCost < elementValue){
				maxCost = elementValue;
			}
		}
		int maxSize = 21;
		scalingFactor = (float) (maxSize / maxCost);
		System.out.println("scalingFactor: "+scalingFactor+", maxCost: "+maxCost);
	}
	
	public static float scalingFactor = 1.0f;
	
	public static int getScaledSize(double magnitude, int viewType){
//		int scaledSize = (int)(magnitude * scalingFactor);
//		if(scaledSize < 5) scaledSize = 5;
//		return scaledSize;
		
		if(viewType == CCSSourceData.VIEW_TYPE_CO2)
			return (int) Math.max(5, Math.log10(magnitude)*3);
		else if(viewType == CCSSourceData.VIEW_TYPE_ACC_CO2)
			return (int) Math.max(5, Math.log10(magnitude)*3);
		else if(viewType == CCSSourceData.VIEW_TYPE_COST)
			return 5+(int)(magnitude * scalingFactor);
		else return 0;
		
		
//		if(magnitude < 100){
//			return 5;
//		}else if(magnitude < 500){
//			return 9;
//		}else if(magnitude < 1000){
//			return 13;
//		}else if(magnitude < 3000){
//			return 15;
//		}else if(magnitude < 5000){
//			return 17;
//		}else if(magnitude < 7000){
//			return 19;
//		}else{
//			return 21;
//		}
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
	
	public static class StorageData {
		public int x;
		public int y;
		public int storagecapacity;
		public int geological_information;
		
		public StorageData(int x, int y, int storagecapacity, int geological_information){		
			this.x = x;
			this.y = y;
			this.storagecapacity = storagecapacity*1000;
			this.geological_information = geological_information;
		}
	}
	
	public static class CO2StateData{
		public double mu;
		public double lou;
		public double p_outlet;
		public int num_pumpstation;
		public double temperature;
		
		public CO2StateData(double mu, double lou, double p_outlet, int num_pumpstation, double temperature){
			this.mu = mu;
			this.lou = lou;
			this.p_outlet = p_outlet;
			this.num_pumpstation = num_pumpstation;
			this.temperature = temperature;
		}
	}
	
	
}
