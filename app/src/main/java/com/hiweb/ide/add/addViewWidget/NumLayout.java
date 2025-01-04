package com.venter.easyweb.add.addViewWidget;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.venter.easyweb.R;

public class NumLayout extends LinearLayout 
{
    public EditText Acet;
    public NumLayout(Context ctx)
    {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void build(boolean IsFloat,int id,String name,int description,Object other)
    {
        TextView Tv=new TextView(getContext());
        Tv.setTextSize(12);
        Tv.setTextColor(ContextCompat.getColor(getContext(),R.color.display_color));
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        Acet=new EditText(getContext());
        Acet.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        if(IsFloat)
            Acet.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        else
            Acet.setInputType(InputType.TYPE_CLASS_NUMBER);
            
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
        addView(Acet);
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
