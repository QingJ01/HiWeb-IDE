package com.venter.easyweb.add.addViewWidget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.venter.easyweb.R;

public class ObjectLayout extends LinearLayout 
{
    public LinearLayout Ly;
    public ObjectLayout(Context ctx)
    {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void build(int id,String name,int description,Object other)
    {
        TextView Tv=new TextView(getContext());
        Tv.setTextSize(12);
        Tv.setTextColor(ContextCompat.getColor(getContext(),R.color.display_color));
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        Ly=new LinearLayout(getContext());
        Ly.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
		
        String title="";
        if(description==-1)
        {
            title=null;
            Tv.setVisibility(View.GONE);
        }
        else
        {
            if(name==null)
            {
                title=getContext().getString(description);
            }
            else
            {
                title=name+":"+getContext().getString(description);
            }
            Tv.setText(title);
        }
        if(other!=null)
        {
            Ly.removeAllViews();
            Ly.addView((View)other);
        }

        addView(Tv);
        addView(Ly);
        if(id>=0)
            setId(id);
    }
    public LinearLayout getCup()
    {
        return Ly;
    }
}
