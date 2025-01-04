package com.venter.easyweb.add.addViewWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.venter.easyweb.R;
import com.venter.easyweb.add.ChooseColor;
import com.venter.easyweb.edit.Do;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class ColorLayout extends LinearLayout  
{
    public EditText Acet;
    public ColorLayout(Context ctx)
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
        TextView Actv2=new TextView(getContext());
        Actv2.setTextSize(TypedValue.COMPLEX_UNIT_SP,Do.px2sp(getContext(),Acet.getTextSize()));
        Actv2.setTextColor(ContextCompat.getColor(getContext(),R.color.opposition));
        Actv2.setText("#");
        LinearLayout Ly=new LinearLayout(getContext());
        Ly.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        ImageButton Acib=new ImageButton(getContext(),null,android.R.attr.buttonBarButtonStyle);
        Acib.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(),44),Do.dp2px(getContext(),44)));
		Drawable colorDrawable=getResources().getDrawable(R.drawable.color);
		colorDrawable.setTint(ContextCompat.getColor(getContext(),R.color.opposition));
        Acib.setImageDrawable(colorDrawable);

        Acib.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
					String color="#000000";
					try
					{
						color=ChooseColor.convertToRGB(Color.parseColor("#"+Acet.getText().toString()),true);
					}
					catch(Exception e)
					{
						
					}
                    new ChooseColor().noWell().choose(getContext(),color,Acet);
                }
            });
            
        Acet.setKeyListener(DigitsKeyListener.getInstance("1234567890ABCDEFabcdef"));
        Acet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        
        Ly.addView(Actv2);
        Ly.addView(Acet);
        Ly.addView(Acib);

        String title="";
        if(name==null)
        {
            title=getContext().getString(description);
        }
        else
        {
            title=name+":"+getContext().getString(description);
        }
        Tv.setText(title);
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
            return code.replace("$","#"+Acet.getText().toString());
        }

    }

}
