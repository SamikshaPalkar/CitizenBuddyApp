package com.project.peoplescorner;

import com.project.connection.conn_Object;
import com.project.connection.connectionManager;
import com.project.data.Login_Request;
import com.project.peoplescorner.data.complaint_RequestObject;
import com.project.peoplescorner.data.statusReply;
import com.project.peoplescorner.utils.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExistingComplaints extends Activity{
	private Context context;
	Dialog dg;
	private int respval=0;
	statusReply resp;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkstatus_screen);
		context=this;
		//init screen components
		initScreen();
	}
	
	private void initScreen()
	{
		/*try {
			Button btnStatus=(Button)findViewById(R.id.btnStatus);
			btnStatus.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Check if fields are entered
					EditText txtID=(EditText)findViewById(R.id.edtcheckID);
					
					//Check if ID is entered
					if(txtID.getText().toString().equalsIgnoreCase(""))
					{
						AlertDialog alert1=new AlertDialog.Builder(context).create();
						alert1.setTitle("Complaint ID is mandatory");
						alert1.setMessage("Please provide your complaint ID.");
						alert1.show();										
						return;
					}
					
					//if yes then check coverage and call web service					
					conn_Object conn=new conn_Object();
					if(!conn.checkNetwork(context))
					{
						Toast.makeText(context, "Network coverage unavailable.", Toast.LENGTH_LONG).show();
						conn=null;
						//exit method
						return;
					}

					//Create new instance of progressDialog
					progressdialog dialog=new progressdialog();
					dg=dialog.createDialog(context);
					dg.show();
					Thread thlocation=new Thread()
					{
						@Override
						public void run()
						{
							//Get Status
							//Check if fields are entered
							EditText txtID=(EditText)findViewById(R.id.edtcheckID);
							//if fields are entered then call service
							conn_Object conn=new conn_Object();
							resp=conn.getStatus(txtID.getText().toString(),context);

							//Display message to user that request has been submitted successfully
							if(resp!=null)
							{
								respval=0;
							}
							else
							{
								respval=1;
							}
							hd.sendEmptyMessage(0);
						}
				
					};
					//start download thread
					thlocation.start();			
				}
			});

				}
		catch(Exception ex)
		{
			String msg=ex.getLocalizedMessage();
		}*/
		
		
		Button btnStatus=(Button)findViewById(R.id.btnStatus);
		btnStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				GetStatus();
			}
		});
		
				
	}
	
	
	public void GetStatus()
	{
		
		EditText txtId=(EditText)findViewById(R.id.edtcheckID);
		String Id=txtId.getText().toString().trim();
		if(Id.equals(""))
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter Complain Id");
			alert.setMessage("Complaint Id Mandatory");
			alert.show();
		}
		else
		{
			Login_Request.SetComplaint_Id(Id);
			
			final connectionManager conn=new connectionManager();
			if(connectionManager.checkNetworkAvailable(this))
			{
				progressdialog dialog=new progressdialog();
				dg=dialog.createDialog(this);
				dg.show();
				
				Thread tthread=new Thread()
				{
					public void run()
					{
						conn.CheckStatus();
						hd.sendEmptyMessage(0);
					}
				};
				tthread.start();
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Internet Not Working", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
	
	Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			
			TextView txtStatus=(TextView)findViewById(R.id.lbStatusVal);

			TextView txtReply=(TextView)findViewById(R.id.lbReplyVal);
			
			txtStatus.setText(Login_Request.GetStatus());
			txtReply.setText(Login_Request.GetReply());
		}
	};
	
/*private void setValues(statusReply resp)
{
	try
	{
		//set values on screen
		TextView txtStatus=(TextView)findViewById(R.id.lbStatusVal);

		TextView txtReply=(TextView)findViewById(R.id.lbReplyVal);
		
		txtStatus.setText(resp.getStatus());
		
		txtReply.setText(resp.getReply());
		
//		if(txtStatus.getText().equals("0"))
//		{
//			txtStatus.append("		"+resp.getStatus());
//			
//		}
//		else
//		{
//			txtStatus.append(resp.getStatus());
//		}
//		if(txtReply.getText().equals("0"))
//		{
//			txtReply.append("		"+resp.getReply());	
//		}
//		else
//		{
//			txtReply.append(resp.getReply());
//		}

	}
	catch(Exception ex)
	{
		String msg=ex.getLocalizedMessage();
	}
}*/
	

	/*public Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			//cancel dialog
			dg.cancel();
			//check download response
			switch (respval)
			{
			
			//Download successful
			case 0:
				//setValues(resp);				
				AlertDialog alert=new AlertDialog.Builder(context).create();
				alert.setTitle("Complaint details Fetched successfully");
				alert.setMessage("Your Complaint details has been downloaded successfully.");
				alert.show();
				setValues(resp);

				break;
			case 1:
				AlertDialog alert1=new AlertDialog.Builder(context).create();
				alert1.setTitle("Failed to get Complaint Details");
				alert1.setMessage("Failed to get Complaint Details. Please try again later.");
				alert1.show();					

				break;
			}
		}
	};*/


}