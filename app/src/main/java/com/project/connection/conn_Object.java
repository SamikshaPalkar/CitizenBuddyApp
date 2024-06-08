package com.project.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.project.peoplescorner.NewComplaint;
import com.project.peoplescorner.data.complaint_RequestObject;
import com.project.peoplescorner.data.statusReply;
import com.project.peoplescorner.utils.location_request;
import com.project.peoplescorner.utils.new_location;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class conn_Object {

		
    //Method will call web service and send data to it
    //private boolean sendData(byte[] data, String filename, String fileextn, double lat, double lng)
	public String sendData(complaint_RequestObject obj,Context context)
    {
    	try
    	{
    		/////old
    		/////String url="http://my-demo.in/Peoples_Corner_VService/Service1.svc/Complaint_register";
			String url="http://my-demo.in/Peoples_Corner_VService/Service1.svc/Complaint_register";
			//String url="http://my-demo.in/people_corner_service1/Service1.svc/Complaint_register";
    		//String url="http://my-demo.in/Transport_Complaint_Service/Service1.svc/Complaint_register";
    		
    		String strData="";
    		byte[] data=new byte[obj.getImage().length];
    		data=obj.getImage();
    		int lent=data.length;
    		
    		String res=Base64.encodeToString(data, Base64.DEFAULT);
    		byte[] decodeByte=Base64.decode(res, Base64.DEFAULT);
    		String strBytes=new String(decodeByte);
    		//File fout=new File("/mnt/testFile.txt");
//    		/("/mnt/sdcard/testFile.txt")
//    		File fout=new File(context.getFilesDir().getPath().toString());
////    		if(fout.mkdir())
////    		{
//    			File fnew=new File(context.getFilesDir().getPath().toString()+"/Test2.txt");
//        		fnew.createNewFile();
//        		FileWriter fos=new FileWriter(fnew);
//        		fos.write(res);
//        		Log.d("Encoded String: ", res);
//    			
//    		}
    		
    		byte[] byteDecode=Base64.decode(res, Base64.DEFAULT);
    		//prepare strData
    		for(int i=0;i<lent;i++)
    		{
    			short read = (short) ((short) data[i] & 0xff);
    			//strData+= String.valueOf(data[i])+" ";//+String.valueOf(data[i+1])+String.valueOf(data[i+2])+" ";
    			strData+= String.valueOf(read)+" ";
    		}
			String abc=strData;
    		Log.d("STRDATA: ", strData);
    		//Call out service and send details
    		HttpClient client=new DefaultHttpClient();
    		//response
    		HttpPost postrequest=new HttpPost();
    		//service uri
    		URI myuri= new URI(url);
    		    		
    		postrequest.setURI(myuri);
    		String requestXML="";
    		// conn_Object.emp_id - using hard-coded EMp id for now
			//oldcode
    		/*requestXML="<Complaint_register><dept_id>"+ obj.getCompID() +"</dept_id><mobile_no>"+ obj.getMobNo() +"</mobile_no><complaint_desc>"+obj.getCompDesc()+"</complaint_desc><longitude>"+location_request.GetLatitude()+"</longitude><latitude>"+location_request.GetLongitude()+"</latitude><location_name>"+obj.getLoc_Address()+"</location_name><file>"+strData+"</file></Complaint_register>";*/
			//newcaode
			requestXML="<Complaint_register><dept_id>"+ obj.getCompID() +"</dept_id><mobile_no>"+ obj.getMobNo() +"</mobile_no><complaint_desc>"+obj.getCompDesc()+"</complaint_desc><longitude>"+new_location.getLatitude()+"</longitude><latitude>"+new_location.getLongitude()+"</latitude><location_name>"+obj.getLoc_Address()+"</location_name><file>"+res+"</file></Complaint_register>";
    		//<location_name>"+obj.getLoc_Address()+"</location_name>
    		//Write data to file
    		
    	
//    		Toast tst=Toast.makeText(context, requestXML, Toast.LENGTH_LONG);
//    		tst.show();
//    		
    		StringEntity se=new StringEntity(requestXML, HTTP.UTF_8);
    		se.setContentType("text/xml");
    		postrequest.setHeader("Content-type","application/xml;charset=UTF-8");
    		postrequest.setEntity(se);
   		    //get response		
    		BasicHttpResponse httpResponse =  
    			    (BasicHttpResponse) client.execute(postrequest);
    		//check response
    		if(httpResponse!=null)
    		{
        		String resp="HTTPStatus "+httpResponse.getStatusLine().toString();
        		HttpEntity entity=httpResponse.getEntity();
        		char[] respbuffer=new char[(int)entity.getContentLength()];
        		InputStream is=entity.getContent();
        		InputStreamReader isreader=new InputStreamReader(is);
        		isreader.read(respbuffer);
        		is.close();
        		String xmlResponse=new String(respbuffer);
//        		
//				AlertDialog alert=new AlertDialog.Builder(context).create();
//				alert.setTitle("Response");
//				alert.setMessage(xmlResponse);
//				alert.show();


        		Document respdoc=null;
        		DocumentBuilderFactory docbuilderfactory=DocumentBuilderFactory.newInstance();
        		DocumentBuilder docbuilder=docbuilderfactory.newDocumentBuilder();
        		InputSource isource=new InputSource();
        		isource.setCharacterStream(new StringReader(xmlResponse));

        		respdoc=docbuilder.parse(isource);
        		
        		//Get data for IMAGE Tag
        		NodeList list=respdoc.getElementsByTagName("complaint_id");
        		//Check if NodeList has any values
        		if(list.getLength()<1)
        		{
        			//if no values then return false
        			return "";
//            		Toast tsNoData=Toast.makeText(MainActivity.getAppContext(), "No data found", Toast.LENGTH_SHORT);
//            		tsNoData.show();

        		}
        		else
        		{
            		Toast tsSendData=Toast.makeText(context, "Data found", Toast.LENGTH_LONG);
            		tsSendData.show();

//        			//selecting ith image
        			Element e=(Element) list.item(0);
        			//Getting attributes array of ith image
        			NodeList childpropertylist=e.getChildNodes();
        			//
        			String val="";
					//resarray[i]=new result_object();
        			for(int j=0;j<childpropertylist.getLength();j++)
        			{
        				Node echild=(Node)childpropertylist.item(j);
        				//Node n=echild.getFirstChild();
        				String test=echild.getNodeName();
        				val=echild.getNodeValue();
        			}    		
    		return val;
        		}
    	}
    	}
    	catch(Exception ex)
    	{
    		String msg=ex.getLocalizedMessage();
    		System.out.println(msg);
    		return "";
    	}
		return null;
    }
	
	
	public statusReply getStatus(String compID,Context context)
	{
		
		//Call out service and send details
		HttpClient client;
		//response
		HttpPost postrequest;
		String requestXML;
		try {
			String url="http://my-demo.in/Peoples_Corner_VService/Service1.svc/Complaint_progress";
			//String url="http://my-demo.in/people_corner_service1/Service1.svc/Complaint_progress";
			//String url="http://my-demo.in/Transport_Complaint_Service/Service1.svc/Complaint_progress";
			client = new DefaultHttpClient();
			postrequest = new HttpPost();
			//service uri
			URI myuri= new URI(url);
			    		
			postrequest.setURI(myuri);
			requestXML = "";
			// conn_Object.emp_id - using hard-coded EMp id for now
			requestXML="<Complaint_progress><complaint_id>"+ compID +"</complaint_id></Complaint_progress>";
			StringEntity se=new StringEntity(requestXML, HTTP.UTF_8);
			se.setContentType("text/xml");
			postrequest.setHeader("Content-type","application/xml;charset=UTF-8");
			postrequest.setEntity(se);
			    //get response		
			BasicHttpResponse httpResponse =  
				    (BasicHttpResponse) client.execute(postrequest);
			
			//check response
			if(httpResponse!=null)
			{
	    		String resp="HTTPStatus "+httpResponse.getStatusLine().toString();
	    		HttpEntity entity=httpResponse.getEntity();
	    		char[] respbuffer=new char[(int)entity.getContentLength()];
	    		InputStream is=entity.getContent();
	    		InputStreamReader isreader=new InputStreamReader(is);
	    		isreader.read(respbuffer);
	    		is.close();
	    		String xmlResponse=new String(respbuffer);
	    		
	    		Document respdoc=null;
	    		DocumentBuilderFactory docbuilderfactory=DocumentBuilderFactory.newInstance();
	    		DocumentBuilder docbuilder=docbuilderfactory.newDocumentBuilder();
	    		InputSource isource=new InputSource();
	    		isource.setCharacterStream(new StringReader(xmlResponse));

	    		respdoc=docbuilder.parse(isource);
	    		
	    		//Get data for Status Tag
	    		NodeList list_Status=respdoc.getElementsByTagName("status");
	    		NodeList list_Reply=respdoc.getElementsByTagName("reply");
	    		NodeList list_ReplyDatetime=respdoc.getElementsByTagName("reply_datetime");
	    		//Check if NodeList has any values
	    		if(list_Status.getLength()<1 || list_Reply.getLength()<1 || list_ReplyDatetime.getLength()<1)
	    		{
	    			//if no values then return false
	    			return null;
	    		}
	    		else
	    		{
	    			//Creating statusReply object
	    			statusReply objReply=new statusReply();

	    			//selecting Status
	    			Element eStatus=(Element) list_Status.item(0);
	    			//Getting attributes array 
	    			NodeList childpropertyStatus=eStatus.getChildNodes();

	    			//selecting Reply
	    			Element eReply=(Element) list_Reply.item(0);
	    			//Getting attributes array 
	    			NodeList childpropertyReply=eReply.getChildNodes();

	    			//selecting Reply Date
	    			Element eReplyDatetime=(Element) list_ReplyDatetime.item(0);
	    			//Getting attributes array 
	    			NodeList childpropertyReplyDatetime=eReplyDatetime.getChildNodes();

	    			
	    			//
	    			String val="";
					//resarray[i]=new result_object();
	    			for(int j=0;j<childpropertyStatus.getLength();j++)
	    			{
	    				Node echild=(Node)childpropertyStatus.item(j);
	    				//Node n=echild.getFirstChild();
	    				String test=echild.getNodeName();
	    				val=echild.getNodeValue();
	    				objReply.setStatus(val);
	    			}  

	    			for(int j=0;j<childpropertyReply.getLength();j++)
	    			{
	    				Node echild=(Node)childpropertyReply.item(j);
	    				//Node n=echild.getFirstChild();
	    				String test=echild.getNodeName();
	    				val=echild.getNodeValue();
	    				objReply.setReply(val);
	    			}  

	    			for(int j=0;j<childpropertyReplyDatetime.getLength();j++)
	    			{
	    				Node echild=(Node)childpropertyReplyDatetime.item(j);
	    				//Node n=echild.getFirstChild();
	    				String test=echild.getNodeName();
	    				val=echild.getNodeValue();
	    				objReply.setDate(val);
	    			}  

	    			
			return objReply;
	    		}
		}

return null;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

	}

	//Method to check network state
	public boolean checkNetwork(Context context)
	{
		try
		{
			ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo(); 
		    return activeNetworkInfo != null; 
		}
		catch(Exception ex)
		{
			String msg=ex.getLocalizedMessage();
			System.out.println(msg);
			return false;
		}
	}

}