package com.project.peoplescorner;



import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	private static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			context=this;
			
			//thread for splash screen
			Thread splashThread = new Thread() 
			{         
				@Override         
				public void run() {            
				try 
				{
					sleep(4000);
				} 
				catch (InterruptedException e) {               
				// do nothing            
				} 
				finally {               
				finish();               
				//Call TabScreen
				Intent i=new Intent(MainActivity.this,UserLogin.class);
				startActivity(i);
				}
			}      
			};

			splashThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
