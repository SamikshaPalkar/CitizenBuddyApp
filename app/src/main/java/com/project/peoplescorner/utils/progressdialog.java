package com.project.peoplescorner.utils;



import com.project.peoplescorner.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Window;

public class progressdialog {
	
	public Dialog createDialog(Context context)
	{
		try {
			Dialog dialog=new Dialog(context,android.R.style.Theme_Translucent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.loading);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new OnCancelListener() {             
			    @Override
			    public void onCancel(DialogInterface dialog) {
			        //onBackPressed();
			    	dialog.dismiss();
			    }
			});
   return dialog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}

}
