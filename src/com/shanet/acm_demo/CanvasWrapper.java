package com.shanet.acm_demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class CanvasWrapper extends Activity {
	
	private static final int RED   = 0;
	private static final int GREEN = 1;
	private static final int BLUE  = 2;
	
	private CustomCanvas cc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int color = ((Integer)getIntent().getExtras().get("color")).intValue();
        int size  = ((Integer)getIntent().getExtras().get("size")).intValue();
        
        cc = new CustomCanvas(this);
        
        cc.setColor(getColor(color));
        cc.setSize(size);
        
        setContentView(cc);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	cc.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	cc.onResume();
    }
    
    public int getColor(int spinnerValue) {
	    switch(spinnerValue) {
	    	case RED:
	    		return Color.RED;
	    	case GREEN:
	    		return Color.GREEN;
	    	case BLUE:
	    		return Color.BLUE;
	    	default:
    	}
		return -1;
    }
}