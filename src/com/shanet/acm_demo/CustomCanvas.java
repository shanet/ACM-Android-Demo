package com.shanet.acm_demo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomCanvas extends SurfaceView implements Runnable {

	private Path path;
	private Paint paint;
	private ArrayList<Path> paths;
	private SurfaceHolder surfaceHolder;
	private boolean drawing;
	private Thread drawingThread;

	public CustomCanvas(Context context) {
		this(context, null);
	}

	public CustomCanvas(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Init paint
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		paint.setDither(true);  
		paint.setColor(Color.RED);  
		paint.setStyle(Paint.Style.STROKE);  
		paint.setStrokeJoin(Paint.Join.ROUND);  
		paint.setStrokeCap(Paint.Cap.ROUND);  
		paint.setStrokeWidth(30);

		surfaceHolder = getHolder();
		paths = new ArrayList<Path>();
	}
	

	public void setColor(int color) {
		paint.setColor(color);
	}
	

	public void setSize(int size) {
		paint.setStrokeWidth(size);
	}
	
	public void onResume() {
		// Init and start the thread
		drawing = true;
		drawingThread = new Thread(this);
		drawingThread.start();
	}

	public void onPause() {
		drawing = false;
		try {
			// Wait until the drawing thread ends
			drawingThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized(paths) {
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// Start a new path
					path = new Path();
					path.moveTo(event.getX(), event.getY());  
					path.lineTo(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					// Get a line to the current position
					path.lineTo(event.getX(), event.getY());
					
					// Add the path to the paths array
					paths.add(path);
					break;
				case MotionEvent.ACTION_UP:
					// Get the final position and add it to the paths array
					path.lineTo(event.getX(), event.getY());
					paths.add(path);
			}
		}
		
		return true;
	}
	

	@Override public void run() {
		while(drawing) {
			if(surfaceHolder.getSurface().isValid()){
				// Lock the canvas to allow for drawing
				Canvas canvas = surfaceHolder.lockCanvas();

				// Draw the current path to the canvas
				synchronized(paths) {
					for(Path path : paths) {
						canvas.drawPath(path, paint);
					}
					paths.clear();
				}

				// Unlock it since we're finished
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}		
	}
}
