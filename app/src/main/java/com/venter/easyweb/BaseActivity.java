package com.venter.easyweb;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;

import com.google.android.material.snackbar.Snackbar;
import com.venter.easyweb.edit.Do;
import com.venter.jssrunner.Utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BaseActivity extends Activity
{
    public CoordinatorLayout coordinatorLayout;
    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(Do.getThemeContext(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		CrashHandler crashHandler=CrashHandler.getInstance();
		crashHandler.init(this);

        if(Vers.i==null)
        {
            Vers.i=new Vers();
        }
    }
    
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {  
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {  
            Do.exit(this);
        }
        return false;  
	}
	
	public void toast(String text)
	{
		toast(text,Snackbar.LENGTH_SHORT,null);
	}
	public void toast(int text)
	{
		toast(getString(text));
	}
	public void toast(String text,int duration,View.OnClickListener action)
    {
        Toast toast = new Toast(this);
        toast.setDuration(duration);

        LinearLayout ly = new LinearLayout(this);

        float r = Do.dp2px( this,20);
        float[] outRadius = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape roundRectShape = new RoundRectShape(outRadius, null, null);
        ShapeDrawable contentDrawable = new ShapeDrawable();
        contentDrawable.setShape(roundRectShape);
        contentDrawable.getPaint().setColor(Do.getColor(this,R.color.ash));
        contentDrawable.getPaint().setAlpha(200);
        contentDrawable.getPaint().setStyle(Paint.Style.FILL);

        ly.setBackground(contentDrawable);
        ly.setPadding(Do.dp2px(this,20),Do.dp2px(this,10),Do.dp2px(this,20),Do.dp2px(this,10));
        ly.setGravity(Gravity.CENTER);

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(this,30),Do.dp2px(this,30)));
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Do.setMargin(iv,0,Do.dp2px(this,10),Do.dp2px(this,10),Do.dp2px(this,10));
        iv.setImageResource(R.drawable.icon_color);

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Do.getColor(this,R.color.opposition));

        ly.addView(iv);
        ly.addView(tv);

        toast.setView(ly);
        toast.show();
    }
}
