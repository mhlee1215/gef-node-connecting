package org.kaist.ie.base;

import java.applet.Applet;
import netscape.javascript.JSObject;


public class CallJSObject implements Runnable{
	
	Applet applet = null;
	String functionName;
	String[] param;
	public CallJSObject(String functionName, String[] param, Applet applet){
		this.applet = applet;
		this.functionName = functionName;
		this.param = param;
		
	}
	public void run()
	{
		System.out.println("START CALL JSP");
		if(applet == null)
		{
			System.err.println("Applet is null");
			return;
		}
		//Call javascript function from JSP.
		JSObject win = JSObject.getWindow(applet);
        if(win == null)
        	System.err.println("Applet is not existed.");
        
        System.out.println("CALL "+functionName);
        win.call(functionName, param); 		//
		
	}
}
