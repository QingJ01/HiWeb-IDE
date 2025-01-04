package com.myopicmobile.textwarrior.android;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.widget.ImageButton;
import android.view.View;
import android.view.Gravity;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.myopicmobile.textwarrior.R;

public class CopyBored extends LinearLayout
{
	Context ctx;
	public CopyBored(Context ctx)
	{
		super(ctx);
		this.ctx=ctx;
		init();
	}
	public CopyBored(Context ctx,AttributeSet attrs)
	{
		super(ctx,attrs);
		this.ctx=ctx;
		init();
	}
	public CopyBored(Context ctx,AttributeSet attrs,int defStyleAttr)
	{
		super(ctx,attrs,defStyleAttr);
		this.ctx=ctx;
		init();
	}
	public CopyBored(Context ctx,AttributeSet attrs,int defStyleAttr,int defStyleRes)
	{
		super(ctx,attrs,defStyleAttr,defStyleRes);
		this.ctx=ctx;
		init();
	}
	
	private void init()
	{
		setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		setPadding(dp2px(getContext(),10),dp2px(getContext(),10),dp2px(getContext(),10),dp2px(getContext(),10));
	}
	public void click(int width,int height,boolean isDark,View.OnClickListener all,View.OnClickListener cut,View.OnClickListener copy,View.OnClickListener paste)
	{
		removeAllViews();
		TypedArray array = ctx.getTheme().obtainStyledAttributes(new int[] {  
																	 android.R.attr.actionModeSelectAllDrawable, 
																	 android.R.attr.actionModeCutDrawable, 
																	 android.R.attr.actionModeCopyDrawable, 
																	 android.R.attr.actionModePasteDrawable,
																 }); 
		Drawable draw;

		draw=array.getDrawable(0);
		draw.setTint(isDark ? Color.GRAY : Color.WHITE);
		ImageButton vIbSa=new ImageButton(ctx,null,android.R.attr.buttonBarButtonStyle);
		vIbSa.setLayoutParams(new LinearLayout.LayoutParams(width,height));
		vIbSa.setOnClickListener(all);
		vIbSa.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		vIbSa.setImageDrawable(draw);
		addView(vIbSa);
		
		draw=array.getDrawable(1);
		draw.setTint(isDark ? Color.GRAY : Color.WHITE);
		ImageButton vIbCut=new ImageButton(ctx,null,android.R.attr.buttonBarButtonStyle);
		vIbCut.setLayoutParams(new LinearLayout.LayoutParams(width,height));
		vIbCut.setOnClickListener(cut);
		vIbCut.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		vIbCut.setImageDrawable(draw);
		addView(vIbCut);

		draw=array.getDrawable(2);
		draw.setTint(isDark ? Color.GRAY : Color.WHITE);
		ImageButton vIbCp=new ImageButton(ctx,null,android.R.attr.buttonBarButtonStyle);
		vIbCp.setLayoutParams(new LinearLayout.LayoutParams(width,height));
		vIbCp.setOnClickListener(copy);
		vIbCp.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		vIbCp.setImageDrawable(draw);
		addView(vIbCp);

		draw=array.getDrawable(3);
		draw.setTint(isDark ? Color.GRAY : Color.WHITE);
		ImageButton vIbPs=new ImageButton(ctx,null,android.R.attr.buttonBarButtonStyle);
		vIbPs.setLayoutParams(new LinearLayout.LayoutParams(width,height));
		vIbPs.setOnClickListener(paste);
		vIbPs.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		vIbPs.setImageDrawable(draw);
		addView(vIbPs);

		array.recycle();
	}
	public static int dp2px(Context context, float dpValue) {
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
