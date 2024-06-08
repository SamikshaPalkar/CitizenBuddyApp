package com.project.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.project.data.Login_Request;
import com.project.data.NewRegistration_Request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class connectionManager 
{
	public static boolean checkNetworkAvailable(Context context)
	{
		try {
			ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo(); 
			return activeNetworkInfo != null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	
	public void NewRegistration()
	{
		String responseString;
		try
		{
			final String TAG_Result="Result";
			////old///http://my-demo.in/Peoples_User_Service/Service1.svc/
			String url=String.format("http://my-demo.in/Peoples_User_Service/Service1.svc/NewRegistration/"+URLEncoder.encode(NewRegistration_Request.GetUsername(),"utf-8")+"/"+URLEncoder.encode(NewRegistration_Request.GetAddress(),"utf-8")+"/"+NewRegistration_Request.GetPhoneNo()+"/"+NewRegistration_Request.GetEmailId()+"/"+NewRegistration_Request.GetAdharCarNo());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Result=jsonObj.getString(TAG_Result);
		        
		        NewRegistration_Request.SetResult(Result);
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean Login()
	{
		String responseString;
		try
		{
			final String TAG_User_Id="User_Id";
			final String TAG_Username="Username";
			
			String url=String.format("http://my-demo.in/Peoples_User_Service/Service1.svc/Login/"+Login_Request.GetPhoneNumber()+"/"+Login_Request.GetPassword());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String User_Id=jsonObj.getString(TAG_User_Id);
		        String Username=jsonObj.getString(TAG_Username);
		        
		        
		        if(!User_Id.equals("null"))
		        {
		        	Login_Request.SetUser_Id(User_Id);
		        	Login_Request.SetUsername(Username);   	
				     
		        	return true;
		        }
		        else
		        {
		        	return false;
		        }
		        
		    }
		    else
		    {
		    	return false;
		    }
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void CheckStatus()
	{
		String responseString;
		try
		{
			final String TAG_Status="Status";
			final String TAG_complaint_reply="complaint_reply";
			
			String url=String.format("http://my-demo.in/Peoples_User_Service/Service1.svc/CheckStatus/"+Login_Request.GetPhoneNumber()+"/"+Login_Request.GetComplaint_Id());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Status=jsonObj.getString(TAG_Status);
		        String Reply=jsonObj.getString(TAG_complaint_reply);
		        
		        Login_Request.SetStatus(Status);
		        Login_Request.SetReply(Reply);
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void ForgetPassword()
	{
		String responseString;
		try
		{
			final String TAG_Result="Result";
			
			
			String url=String.format("http://my-demo.in/Peoples_User_Service/Service1.svc/ForgetPassword/"+Login_Request.GetPhoneNumber());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Status=jsonObj.getString(TAG_Result);
		    
		        
		        Login_Request.SetStatus(Status);
		      
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
