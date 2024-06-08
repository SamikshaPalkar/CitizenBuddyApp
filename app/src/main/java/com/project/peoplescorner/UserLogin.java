package com.project.peoplescorner;


//import com.project.data.NewRegistration_Request;

import com.project.connection.connectionManager;
import com.project.data.Login_Request;
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

public class UserLogin extends Activity {

	
	Dialog dg;
	int resp;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		
		
		
		Button btnLogin=(Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				Login();
			}
		});
		
		
		Button btnNewRegistration=(Button)findViewById(R.id.btnRegistration);
		btnNewRegistration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(UserLogin.this,User_Registration.class);
				startActivity(intent);
			}
		});
		
		Button btnForgetPassword=(Button)findViewById(R.id.btnForgetPassword);
		btnForgetPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				ForgetPassword();
			}
		});
		
	}
	
	public void ForgetPassword()
	{
		EditText txtPhoneNumber=(EditText)findViewById(R.id.txtPhoneNumber);
		String PhoneNumber=txtPhoneNumber.getText().toString().trim();
		
		if(PhoneNumber.equals(""))
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter Phone Number");
			alert.setMessage("Phone Number is Mandatory");
			alert.show();
		}
		else
		{
			Login_Request.SetPhoneNumber(PhoneNumber);
			
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
						conn.ForgetPassword();
						hd2.sendEmptyMessage(0);
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
	
	public void Login()
	{
		EditText txtPhoneNumber=(EditText)findViewById(R.id.txtPhoneNumber);
		EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
		String PhoneNumber=txtPhoneNumber.getText().toString().trim();
		String Password=txtPassword.getText().toString().trim();
		
		if(PhoneNumber.equals("")||Password.equals(""))
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter All Details");
			alert.setMessage("All Fields Are Mandatory");
			alert.show();
		}
		else
		{
			Login_Request.SetPhoneNumber(PhoneNumber);
			Login_Request.SetPassword(Password);
			
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
						if(conn.Login())
						{
							resp=0;
						}
						else
						{
							resp=1;
						}
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
			
			switch (resp) {
			case 0:
				
				Intent intent=new Intent(UserLogin.this,TabScreen.class);
				startActivity(intent);
				
				break;

			case 1:
				
				Toast.makeText(getApplicationContext(), "Invalid Phone Number Or Password", Toast.LENGTH_LONG).show();
				break;
			}
			
			
			
			
		}
	};
	
	Handler hd2=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			
			if(Login_Request.GetStatus().equals("Invalid"))
			{
				Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
			}
			else if(Login_Request.GetStatus().equals("Done"))
			{
				Toast.makeText(getApplicationContext(), "Password Sent on Email Id", Toast.LENGTH_LONG).show();
			}
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_login, menu);
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
