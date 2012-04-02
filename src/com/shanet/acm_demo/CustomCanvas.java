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
	private ArrayList<Path> paths = new ArrayList<Path>();  
	private Thread thread = null;
	private SurfaceHolder surfaceHolder;
	private volatile boolean running = false;

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
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setSize(int size) {
		paint.setStrokeWidth(size);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Clear the previous paths since they should have already been drawn
		paths.clear();

		synchronized (getHolder()) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				// Start a new path
				path = new Path();
				path.moveTo(event.getX(), event.getY());  
				path.lineTo(event.getX(), event.getY());
			} else if(event.getAction() == MotionEvent.ACTION_MOVE) {  
				// Get a line to the current position
				path.lineTo(event.getX(), event.getY());
				// Add the path to the paths array
				paths.add(path);
			} else if(event.getAction() == MotionEvent.ACTION_UP) {
				// Get the final position and add it to the paths array
				path.lineTo(event.getX(), event.getY());
				paths.add(path);
			}
		}
		return true;
	}

	public void onResume() {
		// Init and start the thread
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void onPause() {
		boolean retry = true;
		running = false;
		while(retry) {
			try {
				// Wait until the current thread ends
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while(running){
			if(surfaceHolder.getSurface().isValid()){
				// Unlock the canvas to allow for drawing
				Canvas canvas = surfaceHolder.lockCanvas();

				// Draw the current path to the canvas
				synchronized (canvas) {
					for(Path path : paths) {
						canvas.drawPath(path, paint);
					}	
				}

				// Lock it again
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}		
	}
}