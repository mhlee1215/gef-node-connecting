import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.kaist.ie.base.GeneFunctionSet;
import org.kaist.ie.base.UiGlobals;
import org.kaist.ie.ui.Utils;


public class FunctionPreCalculator {
	String inStr = "";
	String outStr = "";
	int columnMax = 3;
	
	GeneFunctionSet geneFunctionSet = null;
	HashMap<String, HashMap<Integer, List<String>>> functionUniverse = null;
	Vector<HashMap<String, Integer>> functionPreCalculated = null;
	Vector<String> headerColumn = null;
	
	public FunctionPreCalculator(String input, String outputStr){
		inStr = input;
		outStr = outputStr;
		
		
		functionUniverse = new HashMap<String, HashMap<Integer, List<String>>>();
		System.out.println("Annotation file name: "+input);
		headerColumn = new Vector<String>();
		try {
			BufferedReader br = Utils.getInputReader(input);
			
			String strTmp = "";
			
			int count = 0;
			String delimiter = "\t";
			
			while((strTmp=br.readLine()) != null)
			{
				
				if(!strTmp.startsWith("#"))
				{
					
					if(count == 0){
						//Read head.
						String[] strs = strTmp.split(delimiter);
						//구분자가 탭이 아닌경우 콤마로 읽어들임
						if(strs.length <= 1) delimiter = ",";
						strs = strTmp.split(delimiter);
						
						for(int headCnt = 0 ; headCnt < strs.length ; headCnt++)
						{
							System.out.println("header:"+strs[headCnt]);
							headerColumn.add(strs[headCnt]);
						}
					}else{
						String[] strs = strTmp.split(delimiter);
						//Target ID
						
						
						
						HashMap<Integer, String> contentMap = new HashMap<Integer, String>();
						HashMap<Integer, List<String>> functionAttributes = new HashMap<Integer, List<String>>();
						String proveId = strs[0].replace("\"", "").trim();
						for(int strCnt = 0 ; strCnt < strs.length ; strCnt++)
						{
							
							String contentTmp = strs[strCnt].replace("\"", "").trim();
							contentMap.put(strCnt, contentTmp);
							
							List<String> funcList = new Vector<String>();
							String[] funcAttribute = contentTmp.split("///");
							for(int funcCount = 0 ; funcCount < funcAttribute.length ; funcCount++){
								String[] funcPart = funcAttribute[funcCount].split("//");
								//3개가 온전히 있을 경우 두번째만 취함
								if(funcPart.length == 3)
									funcList.add(funcPart[1].trim());
							}
							functionAttributes.put(strCnt, funcList);
							
						}
						
						functionUniverse.put(proveId, functionAttributes);
						
						if(count%100 == 0){
							//System.out.println(strTmp);
							String output = String.format("%.0f", ((double)count*100)/UiGlobals.getNodeCount())+"% An annotation file is being loaded..";
							//UiGlobals.getPropertySearchField().setText(output);
							//UiGlobals.setStatusbarText(output);
							//System.out.println("["+count+"] :: "+proveId);
						}
						
					}
					count++;
				}
			}		
			//UiGlobals.setAnnotationHeader(headerColumn);
			geneFunctionSet = new GeneFunctionSet(functionUniverse);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void startCalculate(){
		functionPreCalculated = new Vector<HashMap<String, Integer>>();
		for(int i = 1 ; i <= columnMax ; i++){
			Vector<String> functionList = getFunctionList(i);
			
			HashMap<String, Integer> semiResult = new HashMap<String, Integer>();
			
			int fncCnt = 0;
			for(String functionName : functionList){
				int containAttributeSize = getContainAttributeGeneSize(i, functionName);
				//System.out.println("functionName: "+functionName+", "+containAttributeSize);
				semiResult.put(functionName.trim(), containAttributeSize);
				if(fncCnt++%100 == 0) System.out.println(fncCnt+"/"+functionList.size());
			}
			
			System.out.println("finish column: "+i);
			functionPreCalculated.add(semiResult);
		}
	}
	
	public int writePreCalculated() throws IOException{
		BufferedWriter bw = getOutputWriter(outStr);
		
		//Set<String> keys = 
		
		for(int i = 1 ; i <= functionPreCalculated.size(); i++){//HashMap<String, Integer> funcResult : functionPreCalculated){
			HashMap<String, Integer> funcResult = functionPreCalculated.get(i-1);
			String header = headerColumn.get(i);
			System.out.println("@"+header);
			bw.write("@"+header+"\n");
			for(String key : getFunctionList(i)){
				bw.write(key+"\t"+funcResult.get(key.trim())+"\n");
			}	
		}
		
		bw.flush();
		return 0;
	}
	
	public Vector<String> getFunctionList(int columnIndex){
		Vector<String> result = new Vector<String>();
		
		Set<String> keys = functionUniverse.keySet();
		
		for(String key : keys){
			HashMap<Integer, List<String>> functionById = functionUniverse.get(key);
			
			if(functionById != null){
				
				List<String> functionByColumn = functionById.get(columnIndex);
				if(functionByColumn!= null){
					for(String functionName : functionByColumn)
						if(!result.contains(functionName)) result.add(functionName);
				}
			}
		}
		
		return result;
	}
	
	public int getContainAttributeGeneSize(int columnIndex, String functionName){
		Set<String> keys = functionUniverse.keySet();
		int resultCnt = 0;
		
		for(String key : keys){
			HashMap<Integer, List<String>> functionById = functionUniverse.get(key);
			
			if(functionById != null){
				
				List<String> functionByColumn = functionById.get(columnIndex);
				if(functionByColumn!= null)
					if(functionByColumn.contains(functionName)) 
						resultCnt++;	
			}
		}
		//System.out.println("getContainAttributeGeneSize resultCnt: "+resultCnt);
		return resultCnt;
	}
	
	public static BufferedReader getInputReader(String filename) {
		
		BufferedReader br = null;
		
		if(filename.contains("http://")){
			try {
				URL testServlet = new URL(filename);
				HttpURLConnection servletConnection = (HttpURLConnection) testServlet
						.openConnection();
	
				InputStream is = new BufferedInputStream(servletConnection
						.getInputStream());
				
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			try {
			File file = new File(filename);
			FileInputStream fis = new FileInputStream(file);
			
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		return br;
	}
	
	public static BufferedWriter getOutputWriter(String filename) {
		
		BufferedWriter bw = null;
		
		try {
		File file = new File(filename);
		FileOutputStream fis = new FileOutputStream(file);
		
		OutputStreamWriter isr = new OutputStreamWriter(fis);
		bw = new BufferedWriter(isr);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
		return bw;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Vector<String> nameVector = new Vector<String>();
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/YG_S98.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Rice.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Rat230_2.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Mouse430_2.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/MOE430A.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/HG-U133A_2.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/HG-U133A.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/HG_U95A.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Ecoli_ASv2.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Drosophila_2.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/DrosGenome1.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/Celegans.na30.annot.csv.trimmed");
//		nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/ATH1-121501.na30.annot.csv.trimmed");
		//nameVector.add("C:/Documents and Settings/hp/바탕 화면/annot/HG-U133_Plus_2.na30.annot.csv.trimmed");
		nameVector.add("C:/Documents and Settings/Administrator/My Documents/Downloads/HG_U95Av2.na31.annot.csv/HG_U95Av2.na31.annot.csv.trimmed");
		nameVector.add("C:/Documents and Settings/Administrator/My Documents/Downloads/HG_U95Av2.na31.annot.csv/MG_U74Av2.na31.annot.csv.trimmed");
		
		for(String filename : nameVector){
		
			String functionFileIn = filename;//"C:/Documents and Settings/hp/바탕 화면/annot/YG_S98.na30.annot.trimmed.csv";
			String functionFileOut = functionFileIn+".precal";
			FunctionPreCalculator pc = new FunctionPreCalculator(functionFileIn, functionFileOut);
			pc.startCalculate();
			pc.writePreCalculated();
			System.out.println("Program is finished : "+filename);
		}
		System.out.println("Program is totally finished.");
		// TODO Auto-generated method stub
		

	}

}
