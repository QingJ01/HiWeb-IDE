package com.venter.easyweb.add;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.venter.easyweb.Editor;
import com.venter.easyweb.R;
import com.venter.easyweb.Vers;
import com.venter.easyweb.add.addViewWidget.ChoiceLayout;
import com.venter.easyweb.add.addViewWidget.NumLayout;
import com.venter.easyweb.add.addViewWidget.StyleLayout;
import com.venter.easyweb.add.addViewWidget.TextLayout;
import com.venter.easyweb.Dl;
import com.venter.easyweb.edit.*;

public class AddClass
{
    private Context ctx;
    private Editor Eo;
    private String[] SaEmsName;
    public AddClass(Context ctx,Editor Eo)
    {
        this.ctx=ctx;
        this.Eo=Eo;
        //获取要插入元素的名称以显示在插入列表里
        SaEmsName=new String[Vers.i.OaAdd.length];
        for(int i = 0; i< Vers.i.OaAdd.length; i++)
        {
            SaEmsName[i]=getContext().getString((Integer) Vers.i.OaAdd[i][0])+"("+ Vers.i.OaAdd[i][1]+")";
        }
    }
    
    public void builder()
    {
        Dl AbList=new Dl(getContext());
        AbList.builder.setTitle(R.string.main_menu_add_title);
        AbList.builder.setItems(SaEmsName, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    switch(p2)
                    {
                        case 6:
                            new AddTable(getContext(),Eo);
                            break;
                        case 10:
                            new AddCSS(getContext(),Eo);
                            break;
                        default:
                            AddEms(p2);
                            break;
                    }
                }
            });
        AbList.show();
    }
    Object[][] SaAllAdd;
    LinearLayout Ly;
    public void AddEms(final int index)
    {
        ScrollView Sv=new ScrollView(getContext());
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(getContext());
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        Ly=new LinearLayout(getContext());
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(Do.dp2px(getContext(),10),Do.dp2px(getContext(),10),Do.dp2px(getContext(),10),Do.dp2px(getContext(),10));
        Sv.addView(Rl);
        Rl.addView(Ly);
        
        String STitle=new String(getContext().getString(R.string.insert)+getContext().getString((Integer) Vers.i.OaAdd[index][0]));
        
        if(Vers.i.OaAdd[index][2]!=null)
        {
            SaAllAdd=new Object[((Object[][])(Vers.i.OaAdd[index][2])).length+ Vers.i.OaAddPub.length][3];
            int i2=0;
            for(; i2<((Object[][])(Vers.i.OaAdd[index][2])).length; i2++)
            {
                SaAllAdd[i2]=((Object[][])(Vers.i.OaAdd[index][2]))[i2];
            }
            for(int i = 0; i< Vers.i.OaAddPub.length; i++,i2++)
            {
                SaAllAdd[i2]= Vers.i.OaAddPub[i];
            }
        }
        else
        {
            SaAllAdd= Vers.i.OaAddPub;
        }
        
        for(int i=0;i<SaAllAdd.length;i++)
        {
            boolean IsAddCJ=true;
            switch((int)SaAllAdd[i][2])
            {
                case Vers.ADD_TYPE_TEXT:
                    TextLayout Tl=new TextLayout(getContext());
                    Tl.build(i,(String)SaAllAdd[i][1],(int)SaAllAdd[i][0],SaAllAdd[i][3]);
                    Ly.addView(Tl);
                    break;
                case Vers.ADD_TYPE_STYLE:
                    StyleLayout Sl=new StyleLayout(getContext());
                    Sl.build(i,(String)SaAllAdd[i][1],(int)SaAllAdd[i][0],SaAllAdd[i][3]);
                    Ly.addView(Sl);
                    break;
                case Vers.ADD_TYPE_NUM:
                    NumLayout Nl=new NumLayout(getContext());
                    Nl.build(true,i,(String)SaAllAdd[i][1],(int)SaAllAdd[i][0],SaAllAdd[i][3]);
                    Ly.addView(Nl);
                    break;
                case Vers.ADD_TYPE_CHOICE:
                    ChoiceLayout Cl=new ChoiceLayout(getContext());
                    Cl.build(true,i,(String)SaAllAdd[i][1],(int)SaAllAdd[i][0],SaAllAdd[i][3]);
                    Ly.addView(Cl);
                    break;
                case Vers.ADD_TYPE_CHOICE_WITHOUT_DEFAULT:
                    ChoiceLayout Cl2=new ChoiceLayout(getContext());
                    Cl2.build(false,i,(String)SaAllAdd[i][1],(int)SaAllAdd[i][0],SaAllAdd[i][3]);
                    Ly.addView(Cl2);
                    break;
                default:
                    IsAddCJ=false;
                    break;
            }
            
        }
        
        Dl Ab=new Dl(getContext());
        Ab.builder.setTitle(STitle);
        Ab.builder.setView(Sv);
        Ab.builder.setPositiveButton(R.string.insert, (p1, p2) -> {
            String SAdd=new String("");
            for(int i=0;i<SaAllAdd.length;i++)
            {
                try
                {
                    String STemp;
                    switch((int)SaAllAdd[i][2])
                    {
                        case Vers.ADD_TYPE_TEXT:
                            TextLayout Tl=Ly.findViewById(i);
                            STemp=(Tl.getText((String)SaAllAdd[i][4]));
                            if(Tl.getText((String)SaAllAdd[i][4])!=null)
                            {
                                SAdd+=(STemp+" ");
                            }

                            break;
                        case Vers.ADD_TYPE_STYLE:
                            StyleLayout Sl=Ly.findViewById(i);
                            STemp=(Sl.getText((String)SaAllAdd[i][4]));
                            if(Sl.getText((String)SaAllAdd[i][4])!=null)
                            {
                                SAdd+=(STemp+" ");
                            }

                            break;
                        case Vers.ADD_TYPE_NUM:
                            NumLayout Nl=Ly.findViewById(i);
                            STemp=(Nl.getText((String)SaAllAdd[i][4]));
                            if(Nl.getText((String)SaAllAdd[i][4])!=null)
                            {
                                SAdd+=(STemp+" ");
                            }

                            break;

                        case Vers.ADD_TYPE_CHOICE:
                        case Vers.ADD_TYPE_CHOICE_WITHOUT_DEFAULT:
                            ChoiceLayout Cl=Ly.findViewById(i);
                            STemp=(Cl.getText((String)SaAllAdd[i][4]));
                            if(Cl.getText((String)SaAllAdd[i][4])!=null)
                            {
                                SAdd+=(STemp+" ");
                            }

                            break;
                    }
                }
                catch(Exception e)
                {

                }

            }
            String SOutput=(String) Vers.i.OaAdd[index][3];
            SOutput=SOutput.replace("$",SAdd);
            Eo.insert(SOutput);
        });
        Ab.show();
    }
    public Context getContext()
    {
        return ctx;
    }
}
