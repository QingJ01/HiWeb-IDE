package com.hiweb.ide.add.addViewWidget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hiweb.ide.R;
import com.hiweb.ide.add.AddStyle;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.Vers;

public class StyleLayout extends LinearLayout 
{
    public EditText Acet;
    public StyleLayout(Context ctx)
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
        Acet=new EditText(getContext());
        Acet.setLayoutParams(new LinearLayout.LayoutParams(-2,-2,1f));
        LinearLayout Ly=new LinearLayout(getContext());
        Ly.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        ImageButton Acib=new ImageButton(getContext(),null,android.R.attr.buttonBarButtonStyle);
        Acib.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(),44),Do.dp2px(getContext(),44)));
        Acib.setImageDrawable(Vers.i.getAddIconDrawable());

        Acib.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    new AddStyle().add(getContext(),Acet);
                }
            });

        Ly.addView(Acet);
        Ly.addView(Acib);
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
            Acet.setHint(getContext().getString((Integer) other));
        }

        addView(Tv);
        addView(Ly);
        if(id>=0)
            setId(id);
    }

    public String getText(String code)
    {
        if(Acet.getText().toString().equals(""))
        {
            return null;
        }
        else
        {
            return code.replace("$",Acet.getText().toString());
        }

    }

}
