package org.ssu.ml.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jfree.chart.JFreeChart;
import org.ssu.ml.base.UiGlobals;




public class Utils {
	
	public static int requestIntParameter(HttpServletRequest request, String name, int initVal)
	{
		if(request.getParameter(name) == null)
			return initVal;
		else
			return Integer.parseInt(request.getParameter(name));
	}
	
	public static String requestStringParameter(HttpServletRequest request, String name, String initVal)
	{
		if(request.getParameter(name) == null)
			return initVal;
		else
			return (String)request.getParameter(name);
	}
	
	public static int maxIndex(float[] args)
	{
		float maxValue = -1000000;
		int maxCount = -1;
		Class type;
		
		for(int count = 0 ; count < args.length ; count++)
		{
			if(maxValue < args[count])
			{
				maxValue = args[count];
				maxCount = count;
			}
		}
		return maxCount;
	}
	
	public static int minIndex(float[] args)
	{
		float minValue = 1000000;
		int minCount = -1;
		Class type;
		
		for(int count = 0 ; count < args.length ; count++)
		{
			if(minValue > args[count])
			{
				minValue = args[count];
				minCount = count;
			}
		}
		return minCount;
	}
	
	public static float minValue(float[] args)
	{
		return args[minIndex(args)];
	}
	
	public static float maxValue(float[] args)
	{
		return args[maxIndex(args)];
	}
	
	public static void main(String[] argv)
	{
		Object a = (float)0.01;
		if(a instanceof Float)
		{
			System.out.println("??");
		}
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
	
//	public static void saveToFile(JFreeChart chart, String aFileName,
//			int width, int height, double quality)
//			throws Exception {
//		BufferedImage img = draw(chart, width, height);
//		
//		SendImageToJsp(img, aFileName, width, height);
//	}
//	
//	protected static BufferedImage draw(JFreeChart chart, int width, int height)
//    {
//        BufferedImage img =
//        new BufferedImage(width , height,
//        BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = img.createGraphics();
//                       
//        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
// 
//        g2.dispose();
//        return img;
//    }
//	
//	public static void SendImageToJsp(BufferedImage img, String filename, int width, int height) throws Exception
//	{
//		
//		ByteArrayOutputStream bas = new ByteArrayOutputStream();
//		
//		ImageIO.write(img,"jsp", bas);
//		
//		byte[] data = bas.toByteArray();
//		
//		ByteArrayInputStream bis = new ByteArrayInputStream(data);
//		System.out.println(data);
//		
//		
//		
//		String url = UiGlobals.getApplet().getCodeBase().toString() + "writeImage.jsp";
//		HttpClient httpClient = new HttpClient();
//		System.out.println("code base to Write : "+url);
//		PostMethod postMethod = new PostMethod(url);
//		
//		//Set Inputstream as entity
//		NameValuePair[] requestBody = {
//				new NameValuePair("filename", filename)
//		};
//		postMethod.setRequestBody(requestBody);
//		postMethod.setRequestEntity(new InputStreamRequestEntity(bis));
//		
//		try{
//			//Execute
//			httpClient.executeMethod(postMethod);
//			
//			System.out.println(postMethod.getResponseBody());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//	}

}
