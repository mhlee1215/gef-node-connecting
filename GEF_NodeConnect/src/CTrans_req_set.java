import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;

public class CTrans_req_set {
	int att_num;
	int label_num;
	float[][] attDatas = null;//new float[45200][4500];
	
	Vector<Float> meanDatas = new Vector<Float>();//null;//new float[45200];
	Vector<Float> sdDatas = new Vector<Float>();//null;//new float[45200];
	Vector<String> nameDatas = new Vector<String>();//null;//new String[45200];
	
	float[][] transDatas = null;//new float[27000][4500];
	String[] transName = null;//new String[27000];
	int[] transSumCount = null;//new int[27000];
	//int transCount = 0;
	int gplColumnNumber = 0;
	
	LinkedHashMap<String, Vector<String>> transIdHash = new LinkedHashMap<String, Vector<String>>();
	Hashtable<String, Vector<Integer>> probeId2TargetIdIndexHash = new Hashtable<String, Vector<Integer>>();
	//Hashtable<String, Integer> gplHash = new Hashtable<String, Integer>();
	Vector<Vector<Float>> gplVector = new Vector<Vector<Float>>();
	//Hashtable<String, float> gplMeanHash = new Hashtable<String, float>();
	//Hashtable<String, float> gplSDHash = new Hashtable<String, float>();
	Logger logger = Logger.getLogger(CTrans_req_set.class);
	BufferedWriter brTrans;
	String GplHeader = "";
	String delimeter = "\t";
	String outputDelimeter = "\t";
	public CTrans_req_set(String[] argv){
		
		String AnnotationFileName = argv[0];
		String ProbeColumnHeaderName = argv[1];
		String KeyColumnHeaderName = argv[2];
		String GplDataFileName = argv[3];
		String TransGplDataFileName = argv[4];
		int probeIndex = 0;
		int keyIndex = 0;
		
	
		for(int argc = 0 ; argc < argv.length ; argc++)
			 logger.debug("argv["+argc+"] : "+argv[argc]);
		try {
			
			
			
			File file = new File(AnnotationFileName);
			FileInputStream fis = new FileInputStream(file);
			
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			
			String strTmp = "";
			
			int count = 0;
			
			logger.debug("start Read Trans Set..");
			//brlog.write("start Read Trans Set..");
			while((strTmp=br.readLine()) != null)
			{
				
				if(!strTmp.startsWith("#"))
				{
					
					if(count == 0){
						//Read head.
						String[] strs = strTmp.split(",");
						for(int headCnt = 0 ; headCnt < strs.length ; headCnt++)
						{
							logger.debug(strs[headCnt]);
							if(strs[headCnt].replace("\"", "").trim().equals(ProbeColumnHeaderName.trim())){
								
								probeIndex = headCnt;
								logger.debug("Find! Probe Index : "+probeIndex);
								break;
							}
						}
						for(int headCnt = 0 ; headCnt < strs.length ; headCnt++)
						{
							if(strs[headCnt].replace("\"", "").trim().equals(KeyColumnHeaderName.trim())){
								
								keyIndex = headCnt;
								logger.debug("Find! keyIndex : "+keyIndex);
								break;
							}
						}
					}else{
					
					
						String[] strs = strTmp.split("\",\"");
						String proveId = strs[probeIndex].replace("\"", "").trim();
						String transIds = strs[keyIndex];
						//Target ID
						String[] transStrs = transIds.split("///");
						
						if(count%500 == 0){
							logger.debug("read line : "+count);
						//	//for(int lineCnt = 0 ; lineCnt < strs.length ; lineCnt++)
							//	System.out.println("["+lineCnt+"] : "+strs[lineCnt]);
						}
						
						for(int transCnt = 0 ; transCnt < transStrs.length ; transCnt++)
						{
							String transStrData = transStrs[transCnt].replace("\"", "").trim();
							Vector<String> tmpVector = null;
							if((tmpVector = transIdHash.get(transStrData))!= null)
							{
								if(!tmpVector.contains(proveId.trim()))
								{
									tmpVector.add(proveId.trim());
								}
							}
							else if(!transStrData.equals("---")){
								tmpVector = new Vector<String>();
								tmpVector.add(proveId.trim());
								transIdHash.put(transStrData, tmpVector);
							}
						}
					}
					count++;
				}
			}		
			
			br.close();
			isr.close();
			fis.close();
			
			
			logger.debug("size of hash : "+transIdHash.size());
			initProbeId2TargetIdIndexHash();
			transName = initTargetId(transIdHash);
			transSumCount = new int[transIdHash.size()];
			transIdHash.clear();
			
			
			//if(1==1) return;
			
			File fpGpl = new File(GplDataFileName);
			FileInputStream fisGpl = new FileInputStream(fpGpl);
			
			InputStreamReader isrGpl = new InputStreamReader(fisGpl);
			BufferedReader brGpl = new BufferedReader(isrGpl);
			String strTmpGpl = "";
			
			count = 0;
			logger.debug("start Analyzing..\n");
			
			
	
			
			//Vector<float> attVector = new Vector<float>();
			//float sum = (float)0.0;
			//float mean = (float)0.0;
			//float square_sum = (float)0.0;
			//float square_mean = (float)0.0;
			//float mean_square = (float)0.0;
			//float sd = (float)0.0;
			
			
			float value = (float)0.0;
			
			gplColumnNumber = 0;
			String[] strs = null;
			while((strTmpGpl = brGpl.readLine()) != null)
			{
				if(count == 0)
				{
					GplHeader = strTmpGpl;
					strs = strTmpGpl.split(delimeter);
					gplColumnNumber = strs.length;
					transDatas = new float[transName.length][gplColumnNumber];
					logger.debug("golColumnNumber : "+gplColumnNumber);
					
				}
				else{
					strs = strTmpGpl.split(delimeter);
					
					String probeId = strs[0];
					Vector<Integer> probeId2TargetIdMapping = probeId2TargetIdIndexHash.get(probeId);
					
					//att_num = strs.length-1;
					//sum=(float)0.0;
					//square_sum = (float)0.0;

					if(probeId2TargetIdMapping != null)
					{
						for(int probeMappingCnt = 0 ; probeMappingCnt < probeId2TargetIdMapping.size(); probeMappingCnt++){
							
							int targetIdIndex = probeId2TargetIdMapping.get(probeMappingCnt);
							transSumCount[targetIdIndex]++;
							for(int strCnt = 1 ; strCnt < gplColumnNumber ; strCnt++)
							{
								value = Float.parseFloat(strs[strCnt]);	
								//sum+=value;
								//square_sum += Math.pow(value, 2);
								transDatas[targetIdIndex][strCnt-1] += value;
							}
						}
						
						//mean = sum/gplColumnNumber;
						//square_mean = square_sum / gplColumnNumber;
						//mean_square = (float)Math.pow(mean, 2);
						//sd = (float)Math.sqrt(Math.abs(square_mean-mean_square));
						//sdDatas.add(sd);
						//meanDatas.add(mean);
						//nameDatas.add(strs[0]);
					}
					if(count%500 == 0){
						if(probeId2TargetIdMapping == null){
							logger.error("["+count+"] NO MAPPING TARGET INDEX , PROBEID : "+probeId);
						}else{
							logger.debug("["+count+"] READ, PROBEID : "+probeId);
						}
					}
				}
				count++;
			}
			
			brGpl.close();
			isrGpl.close();
			fisGpl.close();
			
			File fpTrans = new File(TransGplDataFileName);
			FileOutputStream fisTrans = new FileOutputStream(fpTrans);
			OutputStreamWriter isrTrans = new OutputStreamWriter(fisTrans);
			brTrans = new BufferedWriter(isrTrans);
			
			brTrans.write(GplHeader+"\n");
			for(int targetCnt = 0 ; targetCnt < transName.length ; targetCnt++){
				brTrans.write(transName[targetCnt]+outputDelimeter);
				
				for(int gplColumnCnt = 0 ; gplColumnCnt < gplColumnNumber ; gplColumnCnt++)
				{
					//logger.debug("transDatas["+targetCnt+"]["+gplColumnCnt+"] : "+transDatas[targetCnt][gplColumnCnt]);
					//logger.debug("transSumCount["+targetCnt+"] : "+transSumCount[targetCnt]);
					float targetValue = transDatas[targetCnt][gplColumnCnt]/transSumCount[targetCnt];
					brTrans.write(targetValue+outputDelimeter);
				}
				brTrans.write("\n");
				brTrans.flush();
				if(targetCnt % 500 == 0)
					logger.debug("file writingin : "+targetCnt+"/"+transName.length);
			}
			
			for(int targetSumCnt = 0 ; targetSumCnt < transSumCount.length ; targetSumCnt++)
				logger.debug("transSumCount["+targetSumCnt+"] : "+transSumCount[targetSumCnt]);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("Finish!");
	}
	
	public String[] initTargetId(LinkedHashMap<String, Vector<String>> hash){
		Iterator<String> iter = hash.keySet().iterator();
		String targetNameArry[] = new String[hash.size()];
		int iterCnt = 0;
		while(iter.hasNext())
		{
			String key = iter.next();
			targetNameArry[iterCnt] = key;
			iterCnt++;
		}
		return targetNameArry;
	}
	public void initProbeId2TargetIdIndexHash(){
		Iterator<String> iter = transIdHash.keySet().iterator();
		logger.debug("public void initProbeId2TargetIdIndexHash");
		logger.debug("==[S]====================================");
		int iterCnt = 0;
		int iterMax = transIdHash.size();
		while(iter.hasNext())
		{
			String key = iter.next();
			Vector<String> gpls = transIdHash.get(key);
			
			for(int count1 = 0 ; count1 < gpls.size(); count1++)
			{
				String probeId = gpls.get(count1);
				Vector<Integer> probeId2TargetIndex = probeId2TargetIdIndexHash.get(probeId);
				if(probeId2TargetIndex == null){
					probeId2TargetIndex = new Vector<Integer>();
					probeId2TargetIdIndexHash.put(probeId, probeId2TargetIndex);
				}
				probeId2TargetIndex.add(iterCnt);
				
			}
			
			if(iterCnt % 1000 == 0){
				logger.debug("ProbeId to TargetId index Hash init.. "+iterCnt+"/"+iterMax);
			}
			iterCnt++;
		}
		logger.debug("==[E]====================================");
	}
	
	public static void main(String[] argv)
	{
		String[] conv_argv = null;
		
		if(argv.length < 4)
		{
			System.out.println("Usabe : <annotation file path> <probeSet Name> <Target Column Name> <gplSet path> [<output min Pair in transSet>]");
			return;
		}else{
			if(argv.length < 5){
				conv_argv = new String[5];
				conv_argv[0] = argv[0];
				conv_argv[1] = argv[1].replace("_/", " ");
				conv_argv[2] = argv[2].replace("_/", " ");
				conv_argv[3] = argv[3];
				conv_argv[4] = argv[3]+".trans";
			}else{
				conv_argv = argv;
			}
		}
		new CTrans_req_set(conv_argv);
		
	}
}
