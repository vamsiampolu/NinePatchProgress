package com.example.ninepatchprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {

	public CustomProgressBar(Context context)
	{
		super(context);
	}
	
	public CustomProgressBar(Context context,AttributeSet attrs)
	{
		super(context,attrs);
	}
	
	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public synchronized void setProgress(int progress)
	{
		super.setProgress(progress);
		//forcing the progress bar to redraw because the superclass method does not handle that
		invalidate();
	}
	
	@Override
	public synchronized void onDraw(Canvas canvas)
	{
		updateProgress();
		super.onDraw(canvas);
	}
	
	private float getScale(int progress)
	{
		float scale=getMax()>0?(float)progress/(float)getMax():0;
		return scale;
	}
	
	//Using nine-patch drawables instead of clip drawables:
	public void updateProgress()
	{
		Drawable progressDrawable=getProgressDrawable();
		if(progressDrawable!=null && progressDrawable instanceof LayerDrawable)
		{
			LayerDrawable d=(LayerDrawable)progressDrawable;
			final float scale=getScale(getProgress());
			Drawable progressBar=d.findDrawableByLayerId(R.id.progress);
			int width=d.getBounds().right-d.getBounds().left;
			if(progressBar!=null)
			{
				Rect progressBarBounds=progressBar.getBounds();
				progressBarBounds.right=progressBarBounds.left+(int)(width*scale+0.5f);
				progressBar.setBounds(progressBarBounds);
			}
			Drawable patternOverlay=d.findDrawableByLayerId(R.id.pattern);
			if(patternOverlay!=null)
			{
				if(progressBar!=null)
				{
					Rect patternOverlayBounds=progressBar.copyBounds();
					final int left=patternOverlayBounds.left;
					final int right=patternOverlayBounds.right;
					patternOverlayBounds.left=(left+1>right)?left:left+1;
					patternOverlayBounds.right=(right>0)?right-1:right;
					patternOverlay.setBounds(patternOverlayBounds);
				}
				else
				{
					//There is no progress bar so treat this as a progressBar...
					Rect patternOverlayBounds=patternOverlay.getBounds();
					patternOverlayBounds.left=patternOverlayBounds.left+(int)(width*scale+0.5f);
					patternOverlay.setBounds(patternOverlayBounds);
				}
			}	
		}
	}

}
