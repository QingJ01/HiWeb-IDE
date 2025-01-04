package com.venter.easyweb.add;

import android.content.Context;
import android.content.DialogInterface;
import com.venter.easyweb.MainActivity;
import com.venter.easyweb.R;
import com.venter.easyweb.Dl;

public class AddScript 
{
    public AddScript(Context ctx,final Object[][] Oa,boolean isID)
    {
        String[] SaName=new String[Oa.length];
        for(int i=0;i<Oa.length;i++)
        {
            SaName[i]=isID ? ctx.getString((int)Oa[i][0]) : (String) Oa[i][0];
        }
        Dl AbAddList=new Dl(ctx);
        AbAddList.builder.setTitle(R.string.main_menu_add_title);
        AbAddList.builder.setItems(SaName, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    MainActivity.main.getNowEditor().insert((String)Oa[p2][1]);
                }
            });
        AbAddList.show();
    }
}
