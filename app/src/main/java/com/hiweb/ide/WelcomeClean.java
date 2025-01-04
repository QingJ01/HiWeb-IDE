package com.hiweb.ide;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.core.content.ContextCompat;

import java.io.File;

import com.hiweb.ide.add.addViewWidget.FeedButton;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.server.LocalServersManager;

public class WelcomeClean
{
	public static void update()
	{
		LinearLayout lyG=MainActivity.main.binding.lyWelcomeClean;
		lyG.removeAllViews();
		lyG.setGravity(Gravity.CENTER);
		lyG.setVisibility(View.VISIBLE);
		lyG.setOrientation(LinearLayout.VERTICAL);
		lyG.setPadding(0,0,0,Do.dp2px(MainActivity.main,40));
		if(SettingsClass.ITheme==1)
		{
			lyG.setBackgroundColor(Color.BLACK);
		}
		else
		{
			lyG.setBackgroundColor(Color.WHITE);
		}

		ScrollView sv=new ScrollView(MainActivity.main);
		sv.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		sv.setFillViewport(true);
		lyG.addView(sv);

		RelativeLayout rl=new RelativeLayout(MainActivity.main);
		rl.setLayoutParams(new ScrollView.LayoutParams(-1,-1));
		sv.addView(rl);

		LinearLayout ly=new LinearLayout(MainActivity.main);
		ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-1));
		ly.setPadding(Do.dp2px(MainActivity.main,30),Do.dp2px(MainActivity.main,30),Do.dp2px(MainActivity.main,30),0);
		ly.setGravity(Gravity.CENTER);
		ly.setOrientation(LinearLayout.VERTICAL);
		rl.addView(ly);

		//本地用户存档
		FeedButton userBtn;
		Drawable venterDraw = MainActivity.main.getDrawable(R.drawable.venter);
		if(Vers.i.userName!=null)
		{
			userBtn=new FeedButton(MainActivity.main,-1,R.string.manage_my_user,venterDraw);
			userBtn.tvTitle.setText(Vers.i.userName);
			userBtn.setOnClickListener(view -> {
				UserLoginer.showUserInf();
			});
		}
		else
		{
			userBtn=new FeedButton(MainActivity.main,R.string.user_create,R.string.venter_service,venterDraw);
			userBtn.setOnClickListener(view -> {
				UserLoginer.showLogin("");
			});
		}
		userBtn.setColor(Color.parseColor("#00B0F0"));
		userBtn.diminish();
		ly.addView(userBtn);

		//恢复
		if(Vers.i.vOaBackup!=null){
			FeedButton recoverBtn = new FeedButton(MainActivity.main,R.string.recover_msg,-1,MainActivity.main.getDrawable(R.drawable.recover));
			recoverBtn.setColor(Color.parseColor("#FF8100"));
			recoverBtn.diminish();
			recoverBtn.setOnClickListener(view -> {
				Dl dl=new Dl(MainActivity.main);
				dl.builder.setTitle(R.string.recover);
				dl.builder.setMessage(MainActivity.main.getString(R.string.recover_1)+" "+ Vers.i.vOaBackup[0]+" "+MainActivity.main.getString(R.string.recover_2));
				dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						ly.removeView(recoverBtn);
						MainActivity.main.binding.wwvWelcome.startRecover();
					}
				});
				dl.builder.setNeutralButton(R.string.main_explorer_delete, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						new File(MainActivity.main.getFilesDir(),"recover.json").delete();
						Vers.i.vOaBackup=null;
						ly.removeView(recoverBtn);
					}
				});
				dl.show();
			});
			ly.addView(recoverBtn);
		}

		//PHP Recovery
		if(Vers.i.isAutoStartPHPServer){
			FeedButton phpBtn = new FeedButton(MainActivity.main,R.string.php_web_server_has_recovered,-1,MainActivity.main.getDrawable(R.drawable.link));
			phpBtn.setColor(Color.parseColor("#64BF64"));
			phpBtn.diminish();
			phpBtn.setOnClickListener(view -> {
				new LocalServersManager().showDialog(MainActivity.main);
				phpBtn.setVisibility(View.GONE);
				Vers.i.isAutoStartPHPServer=false;
			});
			ly.addView(phpBtn);
		}

		//HopWeb Editor Ad
		if(!Vers.i.isInstalledHopWeb && !new TipsManager(MainActivity.main).isExists("learn_hopweb")){
			FeedButton hwadBtn = new FeedButton(MainActivity.main,R.string.get_hopweb_ad,-1,MainActivity.main.getDrawable(R.drawable.hopweb_line));
			hwadBtn.setColor(Color.parseColor("#0488FF"));
			hwadBtn.diminish();
			hwadBtn.setOnClickListener(view -> {
				Do.showHopWebDialog();
				new TipsManager(MainActivity.main).create("learn_hopweb");
				hwadBtn.setVisibility(View.GONE);
			});
			ly.addView(hwadBtn);
		}

		//Announcement
		if(AnnouncementManager.isNeedAnnouncing(MainActivity.main)){
			FeedButton announcementBtn = new FeedButton(MainActivity.main,-1,-1, ContextCompat.getDrawable(MainActivity.main,R.drawable.notice));
			announcementBtn.tvTitle.setText(Vers.i.newestAnnouncementInf.title);
			announcementBtn.tvDescription.setText(Vers.i.newestAnnouncementInf.description);
			announcementBtn.setColor(Color.parseColor("#FF6666"));
			announcementBtn.diminish();
			announcementBtn.setOnClickListener(view -> {
				Dl announcementDl = new Dl(MainActivity.main);
				announcementDl.builder
						.setTitle(Vers.i.newestAnnouncementInf.title)
						.setMessage(Vers.i.newestAnnouncementInf.text)
						.setPositiveButton(R.string.check,(d,w)->{
							Do.openUrl(Vers.i.newestAnnouncementInf.link,MainActivity.main);
						})
						.setNeutralButton(R.string.del_announcement,(d,w)->{
							try {
								AnnouncementManager.updateLocalID(MainActivity.main);
								announcementBtn.setVisibility(View.GONE);
							} catch (Exception e) {
								Do.showErrDialog(MainActivity.main,e);
							}
						});
				announcementDl.show();
			});
			ly.addView(announcementBtn);
		}

		View viewSplit = new View(MainActivity.main);
		viewSplit.setLayoutParams(new LinearLayout.LayoutParams(-1,Do.dp2px(MainActivity.main,20)));
		ly.addView(viewSplit);

		HomeButton hbCreateProject=new HomeButton(MainActivity.main, R.drawable.create_proj, R.string.create_proj, new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					MainActivity.main.createProject();
				}
			});
		ly.addView(hbCreateProject);
		
		HomeButton hbCreateTempProject=new HomeButton(MainActivity.main, R.drawable.create_temp_proj, R.string.create_temp_proj, new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					MainActivity.main.CreateTempProject();
				}
			});
		ly.addView(hbCreateTempProject);
		
		HomeButton hbCreateFile=new HomeButton(MainActivity.main, R.drawable.create_file, R.string.create_file, new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					MainActivity.main.createNewFile();
				}
			});
		ly.addView(hbCreateFile);
		
		HomeButton hbExamples=new HomeButton(MainActivity.main, R.drawable.examples, R.string.example_proj, new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ExamplesClass.showMainDialog();
				}
			});
		ly.addView(hbExamples);
		
		HomeButton hbRecent=new HomeButton(MainActivity.main, R.drawable.recent, R.string.recent_projs, new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					MainActivity.main.showRecentProjects();
				}
			});
		ly.addView(hbRecent);
	}
	
	public static void hide()
	{
		MainActivity.main.binding.lyWelcomeClean.setVisibility(View.GONE);
	}

	public static boolean isHide()
	{
		return MainActivity.main.binding.lyWelcomeClean.getVisibility()==View.GONE;
	}
	
	public static class HomeButton extends LinearLayout
	{
		public HomeButton(Context c,int drawable,int title,View.OnClickListener onclick)
		{
			super(c);
			setLayoutParams(new LinearLayout.LayoutParams(-1,-2));

			Drawable draw = c.getDrawable(drawable);
			draw.setTint(Do.getColor(c,R.color.display_color));
			FeedButton feedButton=new FeedButton(c,title,-1,draw);
			feedButton.setOnClickListener(onclick);
			addView(feedButton);
		}
	}
}
