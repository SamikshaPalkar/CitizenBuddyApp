package com.project.peoplescorner;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabScreen extends TabActivity{
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_screen);
        Resources res = getResources(); 
        // Resource object to get Drawables    
        TabHost tabHost = getTabHost();  
        // The activity TabHost    
        TabHost.TabSpec spec;  
        // Resusable TabSpec for each tab    
        Intent intent;  
        // Reusable Intent for each tab    
        // Create an Intent to launch an Activity for the tab (to be reused)    
        intent = new Intent().setClass(this, NewComplaint.class);    
        // Initialize a TabSpec for each tab and add it to the TabHost    
        spec = tabHost.newTabSpec("New Complaint").setIndicator("New Complaint",                      
        res.getDrawable(R.drawable.ic_newcomplaint))                  
        .setContent(intent);    
        tabHost.addTab(spec);    
        // Do the same for the other tabs    
        intent = new Intent().setClass(this, ExistingComplaints.class);    
        spec = tabHost.newTabSpec("Check Status").setIndicator("Complaint Status",                      
        res.getDrawable(R.drawable.ic_prevcomplaints))                  
        .setContent(intent);    
        tabHost.addTab(spec);    
        //set current tab
        tabHost.setCurrentTab(2);                
    }
}
