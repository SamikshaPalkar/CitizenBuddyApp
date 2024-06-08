package com.project.peoplescorner;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import com.project.connection.conn_Object;
import com.project.data.Login_Request;
import com.project.peoplescorner.data.CurrLocation;
import com.project.peoplescorner.data.complaint_RequestObject;
import com.project.peoplescorner.utils.new_location;
import com.project.peoplescorner.utils.progressdialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewComplaint extends Activity {
	Context context;
	public static ImageButton imgButton;
	public static double currlat, currlng;
	private int respval = 0;
	private String resp = "";
	Dialog dg;

	LocationManager lm;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	double x, y;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		context = this;
		setContentView(R.layout.newcomplaint_screen);
		initScreenComponents();

		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		try {
			if (network_enabled)
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
						locationListenerNetwork);
		} catch (SecurityException e) {
			//TODO Auto-generated method stub
		}
	}

	LocationListener locationListenerNetwork = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			x = location.getLatitude();
			y = location.getLongitude();
		}
	};


	private void initScreenComponents() {
		//INitialize Mob No Field
		try {
			imgButton = (ImageButton) findViewById(R.id.btnImage);
			//set background color
			imgButton.setBackgroundColor(Color.TRANSPARENT);
			imgButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					Intent i = new Intent(NewComplaint.this, CameraPreview.class);
					startActivity(i);

//					Toast tst=Toast.makeText(context, "Button Clicked", Toast.LENGTH_LONG);
//					tst.show();
					/*if (checkCameraHardware(context)) {
						Intent i = new Intent(NewComplaint.this, CameraPreview.class);
						startActivity(i);
					}*/

				}
			});

			//set button
			Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
			btnSubmit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Check if all mandatory fields are entered

					//if yes then check coverage and call web service
					conn_Object conn = new conn_Object();
					if (!conn.checkNetwork(context)) {
						Toast.makeText(context, "Network coverage unavailable.", Toast.LENGTH_LONG).show();
						conn = null;
						//exit method
						return;
					}
					//Create new instance of progressDialog
					progressdialog dialog = new progressdialog();
					dg = dialog.createDialog(context);
					dg.show();
					Thread thlocation = new Thread() {
						@Override
						public void run() {
							//Get location
							try {
								getCellLocation();
								hd.sendEmptyMessage(0);
							} catch (Exception e) {

							}
						}

					};
					//start download thread
					thlocation.start();
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("ERROR:", e.getLocalizedMessage());
		}

	}

	public Handler hd = new Handler() {
		public void handleMessage(Message msg) {
			//cancel dialog
			dg.cancel();
			//check download response
			switch (respval) {

				//Download successful
				case 1:
					AlertDialog alert1 = new AlertDialog.Builder(context).create();
					alert1.setTitle("Complaint submission failed");
					alert1.setMessage("Sorry, complaint could not be submitted successfully. Please try again later.");
					alert1.show();


					break;

				case 0:
					AlertDialog alert = new AlertDialog.Builder(context).create();
					alert.setTitle("Complaint submitted successfully");
					alert.setMessage("Your complaint has been submitted successfully. Your complaint ID is " + resp);
					alert.show();

					break;
			}
		}
	};


	private complaint_RequestObject getValues() {
		//Create instance of complaint Object
		complaint_RequestObject objRequest;
		try {
			//Create new instance of complaint_RequestObject
			complaint_RequestObject objComplaint = new complaint_RequestObject();
			//Get spinner value
			Spinner spDeptID = (Spinner) findViewById(R.id.spdeptid);

			String dept = spDeptID.getSelectedItem().toString();

			int deptid;

			if(dept.trim().equalsIgnoreCase("ELECTRICITY"))
				deptid=1000;
			else
			 deptid = spDeptID.getSelectedItemPosition();

			Toast.makeText(getApplicationContext(),String.valueOf(deptid),Toast.LENGTH_LONG).show();

			//get mobile no
			//EditText edtMobNo=(EditText)findViewById(R.id.edtmobNo);
			//String mobNo=edtMobNo.getText().toString();
			String mobNo = Login_Request.GetPhoneNumber();
			//get complaint desc
			EditText edtDesc = (EditText) findViewById(R.id.edtcompDesc);
			String Desc = edtDesc.getText().toString();
			//Street Address
			String loc = get_StreetLocation();
			//image
			//create file instance
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			//byte[] readbytes=null;
//			if(bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes))
//			{
//				readbytes=new byte[bytes.size()];
//				readbytes=bytes.toByteArray();
//			}
			File fin = new File(CameraPreview._path);
			//InputStream is=new FileInputStream(fin);
			FileInputStream is = new FileInputStream(fin);
			BufferedInputStream bis = new BufferedInputStream(is);
			//Open file to read
			//FileInputStream fin=openFileInput(Camerapreview._path);
			final byte[] readbytes = new byte[bis.available()];

			bis.read(readbytes);

			//Set request image
			objRequest = new complaint_RequestObject();
			objRequest.setCompID(String.valueOf(deptid + 1));
			objRequest.setMobNo(mobNo);
			objRequest.setCompDesc(Desc);
			objRequest.setLoc_Address(loc);
			objRequest.setLat(String.valueOf(currlat));
			objRequest.setLng(String.valueOf(currlng));
			objRequest.setImage(readbytes);
			return objRequest;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void getCellLocation() {
		try {
			//Create instance of location class
			CurrLocation loc = new CurrLocation();
			loc.setCurrentLocation(this);
			//check if currlat and currlng are greater than 0
			//currlat>0 || currlng>0
			if (currlat > 0 || currlng > 0) {

                 new_location.setLatitude(currlat);
                 new_location.setLongitude(currlng);


				String text = "Latitude=" + currlat + " Longitude=" + currlng;
				//send lat long details to server.
				//Call method
				conn_Object conn = new conn_Object();
				resp = conn.sendData(getValues(), context);

				//Display message to user that request has been submitted successfully
				if (resp.length() > 0 && !resp.equals("0")) {
					respval = 0;
				} else {
					respval = 1;
				}
			} else {
				//should be used to re-call the function?
			}
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();

		}
	}


	private String get_StreetLocation() {
		try {
			//reverse geo-coding to get current address name
			Geocoder geo = new Geocoder(getBaseContext(), Locale.getDefault());

			try {
				List<Address> addresses = geo.getFromLocation(currlat, currlng, 5);
				if (addresses.size() > 0) {
					Address ad = addresses.get(0);
					String locl = ad.getLocality();
					String locl2 = ad.getFeatureName();

					return locl2;
				}
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();
			return null;
		}
	}


	public void setButtonImage() {

	}

	//method to get phone no.
	/*private String getDevicePhoneNo() {
		try {
			TelephonyManager tMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return TODO;
			}
			String mPhoneNumber = tMgr.getLine1Number();
			 Log.d("MOB NO: ", mPhoneNumber);
			 return mPhoneNumber;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}*/


	 /** Check if this device has a camera */
	 private boolean checkCameraHardware(Context context) {
		 if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		 {
		 // this device has a camera
		 return true;
		 }
		 else {
		 // no camera on this device
		 return false;
		 }
	 }


}
