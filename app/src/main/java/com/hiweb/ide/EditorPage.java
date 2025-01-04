package com.hiweb.ide;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.myopicmobile.textwarrior.common.LanguageNonProg;
import com.myopicmobile.textwarrior.common.LanguageCpp;
import com.hiweb.ide.edit.Do;

import androidx.core.content.ContextCompat;

public class EditorPage extends LinearLayout
{
    public EditorPage(Context context)
    {
        super(context);
        init();
    }
    public EditorPage(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
        init();
    }
    public EditorPage(Context context,AttributeSet attrs) 
    {
        super(context,attrs);
        init();
    }

    public EditorPage(Context context,AttributeSet attrs, int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        init();
	}
    public LinearLayout LyItemCup;
    public LinearLayout LyEditorCup;
    public HorizontalScrollView Hsv;
    
    public Map MSID;
	/*
		Integer:SID
		Object[]:{
			EPI,
			Editor
		}
	*/
	
    public int INowAllCount=0;
    
    public Map MAllFiles;
    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.editor_page, this);
        
        LyItemCup=findViewById(R.id.ly_ep_items_cup);
        Hsv=findViewById(R.id.hsv_ep);
        
        MSID=new HashMap<Integer,Object[]>();
        MAllFiles=new HashMap<Integer,File>();
    }
    public void setEditorCup(LinearLayout LyEditorCup)
    {
        this.LyEditorCup=LyEditorCup;
    }
    public void add(File F,String text,boolean isSetEven)
    {
        EPItem EPI=new EPItem(getContext());
        int INum=LyItemCup.getChildCount();
        INowAllCount++;
        
        EPI.setBox(F,INowAllCount,text);
        LyItemCup.addView(EPI);
        
        Editor Eo=new Editor(getContext(),INowAllCount,isSetEven);
        Eo.setLayoutParams(new LayoutParams(-1,-1));
        Eo.setVisibility(View.GONE);
        Eo.setId(INowAllCount*1024);
        if (F.getName().toLowerCase().endsWith(".js"))
        {
            Eo.build(1,text);
        }
        else if (F.getName().toLowerCase().endsWith(".html")||F.getName().toLowerCase().endsWith(".htm"))
        {
            Eo.build(0,text);
        }
        else if (F.getName().toLowerCase().endsWith(".css"))
        {

            Eo.build(2,text);
        }
        else if (F.getName().toLowerCase().endsWith(".php"))
        {

            Eo.build(3,text);
        }
        else if (F.getName().toLowerCase().endsWith(".xml"))
        {

            Eo.build(4,text);
        }
        else if (F.getName().toLowerCase().endsWith(".jss"))
        {

            Eo.build(5,text);
        }
        else
        {
            Eo.build(6,text);
        }

        LyEditorCup.addView(Eo);
        
        MAllFiles.put(INowAllCount,F);
        MSID.put(INowAllCount,new Object[]{EPI,Eo});
        
        open(INowAllCount);
    }
    
	public void checkBtns()
	{
		if(MSID==null||MSID.size()==0)
			return;
		if((((EPItem) ((Object[]) (MSID.get(Vers.i.INowEpSID)))[0])).BIsSave)
		{
			MainActivity.main.binding.editorMenuBar.hide(Menus.SAVE);
		}
		else
		{
			MainActivity.main.binding.editorMenuBar.visible(Menus.SAVE);
		}

		if(MSID.size()>1)
		{
			MainActivity.main.binding.editorMenuBar.visible(Menus.CLOSE_OTHER);
			MainActivity.main.binding.editorMenuBar.visible(Menus.CLOSE_ALL);
		}
		else
		{
			MainActivity.main.binding.editorMenuBar.hide(Menus.CLOSE_OTHER);
			MainActivity.main.binding.editorMenuBar.hide(Menus.CLOSE_ALL);
		}
	}

    public void open(int sid)
    {
        Do.hideIME();
		try
		{
			MainActivity.main.getNowEditor()._clipboardPanel.hide();
		}
		catch(NullPointerException e)
		{

		}
		MainActivity.main.binding.pastebar.setVisibility(View.GONE);

		if(MainActivity.main.findViewById(R.id.ly_editor_findandreplace).getVisibility()==View.VISIBLE)
		{
			MainActivity.main.getNowEditor().findText();
		}

        File F=((EPItem) (((Object[]) MSID.get(sid))[0])).FNow;
        Editor Eo=(Editor) (((Object[]) MSID.get(sid))[1]);
        final EPItem EPI=(EPItem) (((Object[]) MSID.get(sid))[0]);

        Vers.i.OpenFile = F;

        MainActivity.main.reGetHwwvPreview();

        MainActivity.main.binding.lyPreviewAlert.setVisibility(View.GONE);

        MainActivity.main.stopJsRun();

        {
            boolean BTempIsVisibleMore=false;
            if(MainActivity.main.MoreIsVisible)
            {
                BTempIsVisibleMore=true;
            }
            if (Vers.i.IsOpenProject)
            {
                MainActivity.main.ViewExplorer();
            }
            else
            {
                MainActivity.main.NoViewExplorer();
            }

            if(!BTempIsVisibleMore)
                MainActivity.main.GoneMore();
       	}

        if (F.getName().toLowerCase().endsWith(".js"))
        {

            Vers.i.FileType = 1;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuJS, ContextCompat.getColor(getContext(),R.color.title));
			MainActivity.main.binding.editorMenuBar.hide(Menus.PAUSE_JS);
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);

            
        }
        else if (F.getName().toLowerCase().endsWith(".html")||F.getName().toLowerCase().endsWith(".htm"))
        {

            Vers.i.FileType = 0;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuHTML,ContextCompat.getColor(getContext(),R.color.title));
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);

            
        }
        else if (F.getName().toLowerCase().endsWith(".css"))
        {

            Vers.i.FileType = 2;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuCSS,ContextCompat.getColor(getContext(),R.color.title));
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.GONE);

            
        }
        else if (F.getName().toLowerCase().endsWith(".php"))
        {

            Vers.i.FileType = 3;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuPHP,ContextCompat.getColor(getContext(),R.color.title));
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);

            
        }
        else if (F.getName().toLowerCase().endsWith(".xml"))
        {

            Vers.i.FileType = 4;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuXml,ContextCompat.getColor(getContext(),R.color.title));
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.GONE);
        }
        else if (F.getName().toLowerCase().endsWith(".jss"))
        {

            Vers.i.FileType = 5;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuJSS,ContextCompat.getColor(getContext(),R.color.title));
			MainActivity.main.binding.editorMenuBar.hide(Menus.PAUSE_JS);
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);
        }
        else
        {
            Vers.i.FileType = 6;
            MainActivity.main.binding.editorMenuBar.load(Menus.mainMenuText,ContextCompat.getColor(getContext(),R.color.title));
            MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.GONE);

			if(F.getName().toLowerCase().endsWith(".jsp")||
			   F.getName().toLowerCase().endsWith(".asp")||
			   F.getName().toLowerCase().endsWith(".aspx"))
			{
				Vers.i.FileType = 7;
				MainActivity.main.binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);
			}
			else
			{
				MainActivity.main.binding.editorMenuBar.hide(Menus.PREVIEW);
			}
        }
		MainActivity.main.binding.splitLineView.setVisibility(View.VISIBLE);

        Vers.i.OpenFile = F;
        for(Object IKey:MSID.keySet())
        {
            EPItem EpiNow=(EPItem) ((Object[]) (MSID.get(IKey)))[0];
            EpiNow.unchoose();
        }
        EPI.choose();

        for(int i=0;i<LyEditorCup.getChildCount();i++)
        {
            LyEditorCup.getChildAt(i).setVisibility(View.GONE);
        }
        MainActivity.main.reGetCharsAdder();
        Vers.i.INowEpSID=sid;

        Eo.build(Vers.i.FileType,null);
        Eo.inNames();

        Eo.setVisibility(View.VISIBLE);

        Eo.setFocusable(true);

        Hsv.post(new Runnable(){

                @Override
                public void run() {
                    Hsv.scrollTo(EPI.getLeft(),0);
                }
            });
		Vers.i.vOaBackup=null;
		
		checkBtns();
    }
	public static void reSetCopyBoredClick(final Editor ed,int width)
	{
		boolean isDark=ed.PasteBar==null;
		if(!isDark)
		{
			if(SettingsClass.ITheme==4)
				isDark=true;
		}
		ed.vCopyBored.click(width,width,isDark,
			new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ed.selectAll();
				}
			},
			new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ed.cut();
				}
			},
			new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ed.copy();
				}
			},
			new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ed.paste();
				}
			});
	}
	
    public void remove(int sid)
    {
		MainActivity.main.getNowEditor()._clipboardPanel.hide();
		MainActivity.main.binding.pastebar.setVisibility(View.GONE);
		if(MainActivity.main.findViewById(R.id.ly_editor_findandreplace).getVisibility()==View.VISIBLE)
		{
			MainActivity.main.getNowEditor().findText();
		}
		
        Editor Eo=(Editor) (((Object[]) MSID.get(sid))[1]);
        EPItem EPI=(EPItem) (((Object[]) MSID.get(sid))[0]);
        
        MSID.remove(sid);
        LyItemCup.removeView(EPI);
        LyEditorCup.removeView(Eo);
		
        if(MSID.size()>0)
        {
            Object[] OaSID;
            OaSID=MSID.keySet().toArray();
            open((Integer) OaSID[0]);
        }
		
		checkBtns();

        checkIsSavedAllAndStopBackup();
    }
    
    public class EPItem extends LinearLayout
    {
        public EPItem(Context context)
        {
            super(context);
            init();
        }
        public EPItem(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context,attrs,defStyleAttr,defStyleRes);
            init();
        }
        public EPItem(Context context,AttributeSet attrs) 
        {
            super(context,attrs);
            init();
        }

        public EPItem(Context context,AttributeSet attrs, int defStyleAttr)
        {
            super(context,attrs,defStyleAttr);
            init();
        }
        public TextView TvName;
        public LinearLayout LyBottom;
        public boolean BIsChosen=false;
        public boolean BIsSave=true;
        public String SName="";
        public File FNow;

        @SuppressLint("ResourceType")
        private void init()
        {
            LayoutInflater.from(getContext()).inflate(R.layout.ep_item, this);

            TvName=findViewById(R.id.tv_ep_item_name);
            LyBottom=findViewById(R.id.ly_ep_item_bottom);
            
            setOnClickListener(p1 -> {
                if(!BIsChosen)
                    EditorPage.this.open(getId()/2048);
            });
        }
        
        public void setBox(File F,int sid,String SText)
        {
            FNow=F;
            SName=F.getName();
            TvName.setText(SName);
            setId(sid*2048);
        }
        
        public void star()
        {
            if(!BIsSave)
                return;
            TvName.setText(SName+"*");
			MainActivity.main.binding.editorMenuBar.visible(Menus.SAVE);
            MainActivity.main.recoverController(false);
            BIsSave=false;
        }
        public void save()
        {
            unstar();
        }
        public void unstar()
        {
            TvName.setText(SName);
			MainActivity.main.binding.editorMenuBar.hide(Menus.SAVE);
            BIsSave=true;

            checkIsSavedAllAndStopBackup();
        }
        
        public void choose()
        {
            BIsChosen=true;
            LyBottom.setVisibility(View.VISIBLE);
        }
        public void unchoose()
        {
            BIsChosen=false;
            LyBottom.setVisibility(View.GONE);
        }
        public File getFile()
        {
            return FNow;
        }
    }
    private void checkIsSavedAllAndStopBackup(){
        boolean isSavedAll = true;
        for (Object IKey : MainActivity.main.binding.Ep.MSID.keySet().toArray()) {
            if (!MainActivity.main.getEpi((Integer) IKey).BIsSave) {
                isSavedAll = false;
                break;
            }
        }
        if(isSavedAll){
            MainActivity.main.recoverController(true);
        }
    }
}
