package com.venter.easyweb;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.view.View;
import com.venter.easyweb.edit.*;

public class SupplementManager
{
	public static void show()
	{
		LinearLayout Ly;
		ScrollView Sv=new ScrollView(MainActivity.main);
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(MainActivity.main);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        Ly=new LinearLayout(MainActivity.main);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
        Sv.addView(Rl);
        Rl.addView(Ly);
		
		final LinearLayout LyPacked=new LinearLayout(MainActivity.main);
		final LinearLayout LyDIY=new LinearLayout(MainActivity.main);
		LinearLayout.LayoutParams Ll=new LinearLayout.LayoutParams(-1,-2);
		LyPacked.setLayoutParams(Ll);
		LyDIY.setLayoutParams(Ll);
		LyPacked.setPadding(30,0,0,0);
		LyDIY.setPadding(30,0,0,0);
		LyPacked.setVisibility(View.GONE);
		LyDIY.setVisibility(View.GONE);
		
		ListView LvPacked=new ListView(MainActivity.main);
		ListView LvDIY=new ListView(MainActivity.main);
		LvPacked.setAdapter(new ArrayAdapter(MainActivity.main,android.R.layout.simple_list_item_1,new String[]{MainActivity.main.getString(R.string.supplement_rules_packed)}));
		LvDIY.setAdapter(new ArrayAdapter(MainActivity.main,android.R.layout.simple_list_item_1,new String[]{MainActivity.main.getString(R.string.supplement_rules_diy)}));
		LvPacked.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if(LyPacked.getVisibility()==View.VISIBLE)
					{
						LyPacked.setVisibility(View.GONE);
					}
					else
					{
						LyPacked.setVisibility(View.VISIBLE);
					}
				}
			});
		LvPacked.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if(LyDIY.getVisibility()==View.VISIBLE)
					{
						LyDIY.setVisibility(View.GONE);
					}
					else
					{
						LyDIY.setVisibility(View.VISIBLE);
					}
				}
			});
		Ly.addView(LvPacked);
		Ly.addView(LyPacked);
		Ly.addView(LvDIY);
		Ly.addView(LyDIY);
	}
}
