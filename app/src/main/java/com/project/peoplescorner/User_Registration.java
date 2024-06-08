package com.project.peoplescorner;


import com.project.connection.connectionManager;
import com.project.data.NewRegistration_Request;
import com.project.peoplescorner.utils.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class User_Registration extends Activity {

	
	Dialog dg;
	int resp;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__registration);
		
		
		
		Button btnRegister=(Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				NewRegistration();
			}
		});
	}
	
	public void NewRegistration()
	{
		EditText txtUsername=(EditText)findViewById(R.id.txtUsername);
		EditText txtAddress=(EditText)findViewById(R.id.txtAddress);
		EditText txtPhoneNo=(EditText)findViewById(R.id.txtPhoneNo);
		EditText txtEmailId=(EditText)findViewById(R.id.txtEmailId);
		EditText txtAdharCarNo=(EditText)findViewById(R.id.txtAdharNo);
		
		String Username=txtUsername.getText().toString().trim();
		String Address=txtAddress.getText().toString().trim();
		String PhoneNo=txtPhoneNo.getText().toString().trim();
		String EmailId=txtEmailId.getText().toString().trim();
		String AdharNo=txtAdharCarNo.getText().toString().trim();
		
		if(Username.equals("")||Address.equals("")||PhoneNo.equals("")||EmailId.equals("")||AdharNo.equals(""))
		{
			
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter All Details");
			alert.setMessage("All Fields Are Mandatory");
			alert.show();
			
		}
		else
		{
			String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
			
			String PhonePattern = "[0-9]{10}";
			
			if(PhoneNo.matches(PhonePattern)&&EmailId.matches(emailPattern))
			{
				
				NewRegistration_Request.SetUsername(Username);
				NewRegistration_Request.SetPhoneNo(PhoneNo);
				NewRegistration_Request.SetEmailId(EmailId);
				NewRegistration_Request.SetAddress(Address);
				NewRegistration_Request.SetAdharCarNo(AdharNo);
				
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
							conn.NewRegistration();
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
			else if(!PhoneNo.matches(PhonePattern) && !EmailId.matches(emailPattern))
			{
				txtPhoneNo.setError("Invalid Phone Number");
				txtEmailId.setError("Invalid Email Id");
				//Toast.makeText(getApplicationContext(), "Invalid Phone Number & Email Id", Toast.LENGTH_LONG).show();
			}
			else if(!PhoneNo.matches(PhonePattern))
			{
				txtPhoneNo.setError("Invalid Phone Number");
				//Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
			}
			else if(!EmailId.matches(emailPattern))
			{
				txtEmailId.setError("Invalid Email Id");
				//Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			
			if(NewRegistration_Request.GetResult().equals("Exists"))
			{
				Toast.makeText(getApplicationContext(), "Already Exists", Toast.LENGTH_LONG).show();
			}
			else if(NewRegistration_Request.GetResult().equals("Registered"))
			{
				Toast.makeText(getApplicationContext(), "Registered Seccuessfully", Toast.LENGTH_LONG).show();
				
				Intent intent=new Intent(User_Registration.this,UserLogin.class);
				startActivity(intent);
				finish();
				
			}
				
		}
	};
	
	public void onBackPressed() 
	{
		Intent intent=new Intent(User_Registration.this,UserLogin.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user__registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
