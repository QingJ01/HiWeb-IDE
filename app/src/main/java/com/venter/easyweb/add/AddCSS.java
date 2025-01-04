package com.venter.easyweb.add;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.venter.easyweb.Editor;
import com.venter.easyweb.MainActivity;
import com.venter.easyweb.R;
import com.venter.easyweb.add.addViewWidget.StyleLayout;
import com.venter.easyweb.add.addViewWidget.TextLayout;
import com.venter.easyweb.Dl;
import com.venter.easyweb.edit.*;

public class AddCSS 
{
    Context ctx;
    Editor Eo;
    LinearLayout Ly;
    public AddCSS(final Context ctx,final Editor Eo)
    {
        this.ctx=ctx;
        this.Eo=Eo;
        
        String STitle=ctx.getString(R.string.insert)+"CSS";
        
        ScrollView Sv=new ScrollView(ctx);
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(ctx);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        Ly=new LinearLayout(ctx);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(Do.dp2px(ctx,10),Do.dp2px(ctx,10),Do.dp2px(ctx,10),Do.dp2px(ctx,10));
        Sv.addView(Rl);
        Rl.addView(Ly);
        
        final TextLayout Tl=new TextLayout(ctx);
        Tl.build(0,"Selector",R.string.add_css_name,null);
        Ly.addView(Tl);
        final StyleLayout Sl=new StyleLayout(ctx);
        Sl.build(1,"Style",R.string.add_style,null);
        Ly.addView(Sl);
        
        Dl Ab=new Dl(ctx);
        Ab.builder.setTitle(STitle);
        Ab.builder.setView(Sv);
        Ab.builder.setPositiveButton(R.string.insert, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if(Tl.getText("$")==null||Sl.getText("$")==null)
                    {
                        MainActivity.main.toast(R.string.add_table_err);
                    }
                    else
                    {
                        StringBuilder Sb=new StringBuilder();
                        Sb.append(Tl.getText("$\n"));
                        Sb.append("{\n");
                        Sb.append(Sl.getText("    $\n"));
                        Sb.append("}\n");

                        Eo.insert(Sb.toString());
                    }
                }
            });
        Ab.show();
    }
}
