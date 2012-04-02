package com.shanet.acm_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get handles to views
        final Spinner colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        final EditText sizeText = (EditText) findViewById(R.id.sizeText);
        Button startCanvasButton = (Button) findViewById(R.id.startCanvasButton);
        
        // Start the canvas activity with the selected shape type
        startCanvasButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create an intent to start our  
		        Intent canvasIntent = new Intent(main.this, CanvasWrapper.class);
		        canvasIntent.putExtra("color", colorSpinner.getSelectedItemPosition());
		        canvasIntent.putExtra("size", Integer.parseInt(sizeText.getText().toString()));
		        startActivity(canvasIntent);
			}
		});
    }
}