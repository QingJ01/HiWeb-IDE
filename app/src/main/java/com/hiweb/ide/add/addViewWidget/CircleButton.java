package com.venter.easyweb.add.addViewWidget;
import android.content.*;
import android.graphics.drawable.*;
import android.widget.*;
import com.venter.easyweb.*;
import com.venter.easyweb.edit.*;
import android.view.*;
import android.graphics.*;

public class CircleButton extends RelativeLayout
{
	Context ctx;
	ImageView imageView;
	TextView textView;
	Drawable drawable;
	String text;
	boolean isImportantBtn;
	public CircleButton(Context ctx,Drawable drawable,String text,boolean isImportantBtn)
	{
		super(ctx);
		this.ctx=ctx;
		this.drawable=drawable;
		this.text=text;
		this.isImportantBtn=isImportantBtn;
		barMode();
	}
	
	public void barMode()
	{
		removeAllViews();
		RelativeLayout.LayoutParams params;

		imageView=new ImageView(ctx);
		drawable.setTint(isImportantBtn ? Do.getColor(getContext(),R.color.pure) :  Do.getColor(getContext(),R.color.opposition));
		imageView.setImageDrawable(drawable);
		params=new RelativeLayout.LayoutParams(Do.dp2px(ctx,30),Do.dp2px(ctx,30));
		params.addRule(RelativeLayout.ALIGN_LEFT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		Do.setMargin(imageView,0,0,Do.dp2px(ctx,15),0);

		textView=new TextView(ctx);
		params=new RelativeLayout.LayoutParams(-1,Do.dp2px(ctx,30));
		textView.setLayoutParams(params);
		textView.setTextColor(isImportantBtn ? Do.getColor(getContext(),R.color.pure) :  Do.getColor(getContext(),R.color.opposition));
		textView.setText(text);
		textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(18);
		textView.setPadding(Do.dp2px(ctx,35),0,Do.dp2px(ctx,35),0);

		setBackgroundDrawable(isImportantBtn ? ctx.getDrawable(R.drawable.circle_btn) : ctx.getDrawable(R.drawable.circle_ash_btn));
		setGravity(Gravity.CENTER);
		setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		setPadding(Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15));
		Do.setMargin(this,Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15));

		addView(imageView);
		addView(textView);
	}
	public void circleMode()
	{
		removeAllViews();
		RelativeLayout.LayoutParams params;

		imageView=new ImageView(ctx);
		drawable.setTint(isImportantBtn ? Do.getColor(getContext(),R.color.pure) :  Do.getColor(getContext(),R.color.opposition));
		imageView.setImageDrawable(drawable);
		params=new RelativeLayout.LayoutParams(Do.dp2px(ctx,30),Do.dp2px(ctx,30));
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		
		setBackgroundDrawable(isImportantBtn ? ctx.getDrawable(R.drawable.circle_btn) : ctx.getDrawable(R.drawable.circle_ash_btn));
		setGravity(Gravity.CENTER);
		setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
		setPadding(Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15));
		Do.setMargin(this,Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15),Do.dp2px(ctx,15));

		addView(imageView);
	}
}
