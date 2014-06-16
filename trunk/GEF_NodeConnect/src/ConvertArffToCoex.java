import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;



/**
 * 웨카포멧 에서 Coex의 상관계수 구하기 형식으로 변환
 * 
 * @author sutting
 *
 */
public class ConvertArffToCoex {
	
	LinkedHashMap<Integer, String> readLabel = new LinkedHashMap<Integer, String>();
	LinkedHashMap<Integer, Vector<Float>> readRows = new LinkedHashMap<Integer, Vector<Float>>();
	
	private final String ARFF_RELATION_STR = "RELATION";
	private final String ARFF_ATTRIBUTE_STR = "ATTRIBUTE";
	private final String ARFF_DATA_STR = "DATA";
	
	String stage = "";
	
	String relationName; 
	
	public ConvertArffToCoex(String fileName) throws IOException{
		//String fileName = "prostate_tumorVSNormal_train";
		//String fileName = "leukemia_train";
		File file = new File("d://"+fileName+".arff");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		File fpTrans = new File("d://"+fileName+".coex");
		FileOutputStream fisTrans = new FileOutputStream(fpTrans);
		OutputStreamWriter isrTrans = new OutputStreamWriter(fisTrans);
		BufferedWriter brCoex = new BufferedWriter(isrTrans);
		
		String strTmp = "";
		
		int count = 0;
		int attributeCount = 0;
		System.out.println("start Read arff Set..");
		//brlog.write("start Read Trans Set..");
		while((strTmp=br.readLine()) != null){
			if(strTmp.startsWith("@")){
				String[] strPart = strTmp.split(" ");
				if(!stage.equals(ARFF_DATA_STR)){
					
					String attributeName = strPart[0].substring(1);
					
					if(attributeName.equalsIgnoreCase(ARFF_RELATION_STR)){
						stage = ARFF_RELATION_STR;
						
						if(strPart.length > 1)
							relationName = strPart[1];
					}
					else if(attributeName.equalsIgnoreCase(ARFF_ATTRIBUTE_STR)){
						stage = ARFF_ATTRIBUTE_STR;
						if(strPart.length > 1){
							readLabel.put(attributeCount, strPart[1]);
							readRows.put(attributeCount, new Vector<Float>());
							attributeCount++;
						}
					}
					else if(attributeName.equalsIgnoreCase(ARFF_DATA_STR)){
						stage = ARFF_DATA_STR;
					}
				}
			}
			else{
				String[] strPart = strTmp.split(",");
				for(int i = 0 ; i < strPart.length ; i++){
					try{
						Float readVal = Float.parseFloat(strPart[i].trim());
						readRows.get(i).add(readVal);
					}catch(Exception e){}
				}
			}
			
			count++;
		}
		
		System.out.println("read fin.");
		
		Iterator<Integer> iter = readRows.keySet().iterator();
		int iterCnt = 0;
		int prevLength = -1;
		int length = -1;
		while(iter.hasNext())
		{
			int key = iter.next();
			
			Vector<Float> value = readRows.get(key);
			length = value.size();
			if(value != null && value.size() > 0){
				brCoex.write(Integer.toString(iterCnt));
				for(Float val : value){
					brCoex.write("\t"+val);
				}
				brCoex.write("\n");
				
				if(prevLength != length && prevLength > 0){
					System.out.println("ERROR!!!!!!!!!!!! prev: "+prevLength+", cur: "+length);
				}
			}
			iterCnt++;
			
			prevLength = length;
		}
		brCoex.flush();
	}
	
	public static void main(String[] argv) throws IOException{
		new ConvertArffToCoex(argv[0]);
	}
}
