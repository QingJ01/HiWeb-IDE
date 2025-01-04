package com.venter.easyweb;
import java.io.*;

import android.widget.*;
import com.venter.easyweb.edit.*;
import java.net.*;
import android.content.*;
import android.app.*;
import com.venter.easyweb.add.addViewWidget.*;
import android.view.*;

public class PagePreviewer
{
	private static String HTMLText="";
	private static String baseURL=null;
	private static File nowFile=null;
	private static MainActivity main;
	private static String ip="0.0.0.0";
	private static File phpFile;
	private static File phpVersionFile;
	private static File libDir;
	public static boolean isDownloadingPhp=false;
	private static String host;

	private static void init()
	{
		main=MainActivity.main;
		phpFile=new File(main.getFilesDir(),"php");
		phpVersionFile=new File(main.getFilesDir(),"php_version.txt");
		libDir=new File(main.getFilesDir(),"lib");
	}

	public static void previewNowFile(boolean mustLocal)
	{
		init();
		try
		{
			ip = Do.getIpAddressString();
		}
		catch (SocketException e)
		{}
		nowFile = main.getNowEpi().getFile();
		baseURL="file://"+nowFile.getParent()+"/";
        String S=main.getNowEditor().getString();
		HTMLText=S;
      	
		if(Vers.i.FileType==7)
		{
			if(Vers.i.nowProjectServerType !=2|| Vers.i.ProjectDir==null||!nowFile.getPath().startsWith(Vers.i.ProjectDir.getPath()+"/"))
			{
				main.toast(R.string.preview_other_webpage_err);
				return;
			}
		}
		
		if(!mustLocal&& Vers.i.ProjectDir!=null&&nowFile.getPath().startsWith(Vers.i.ProjectDir.getPath()+"/")&& Vers.i.nowProjectServerType !=-1)
		{
			host=ip;
			if(Vers.i.nowProjectServerType ==2)
				host="localhost";

			main.save(new Runnable(){

				@Override
				public void run()
				{
					main.PreviewHwwv.loadUrl("http://"+host+":"+ Vers.i.nowWebsitePort +nowFile.getPath().replace(Vers.i.ProjectDir.getPath(),""));
				}
			});
			
			main.Topreview();
			main.binding.termux.reset();
			main.PreviewHwwv.clearCache(true);

			if(SettingsClass.isAutoPreviewFull)
			{
				main.fullScreenPreview();
			}
			return;
		}	
        if(Vers.i.FileType==0)
		{
			if(SettingsClass.isAllowAbsolute)
			{
				main.save(new Runnable(){

						@Override
						public void run()
						{
							main.PreviewHwwv.loadDataWithBaseURL(baseURL, HTMLText, "text/html", "utf-8", null);
						}
					});
			}
			else
			{
				main.PreviewHwwv.loadDataWithBaseURL(baseURL, HTMLText, "text/html", "utf-8", null);
			}		
		} 
		else if(Vers.i.FileType==3)
		{
			if(isDownloadingPhp)
			{
				main.toast(R.string.wait);
				return;
			}
			if(!isNewestPHP())
			{
				downloadPhp(MainActivity.main);
				return;
			}
			main.PreviewHwwv.wvc.onPageStarted(main.PreviewHwwv,"file://"+Do.URLEncode(Vers.i.OpenFile.getPath()),null);
			new Thread(new Runnable(){

					@Override
					public void run()
					{
						try
						{
							phpFile.setExecutable(true);
							libDir.setExecutable(true);
							for(File i:libDir.listFiles())
							{
								i.setExecutable(true);
							}
							final String output=Do.runCommand(new String[]{"/system/bin/sh","-c","export LD_LIBRARY_PATH=\""+libDir.getPath()+"\" && \""+phpFile.getPath()+"\" -f \""+nowFile.getPath()+"\""});
							main.runOnUiThread(new Runnable(){

									@Override
									public void run()
									{
										main.PreviewHwwv.wvc.onPageFinished(main.PreviewHwwv,"file://"+Do.URLEncode(Vers.i.OpenFile.getPath()));
										if(SettingsClass.isAllowAbsolute)
										{
											main.save(new Runnable(){

													@Override
													public void run()
													{
														main.PreviewHwwv.loadDataWithBaseURL(baseURL, output, "text/html", "utf-8", null);
													}
												});
										}
										else
										{
											main.PreviewHwwv.loadDataWithBaseURL(baseURL, output, "text/html", "utf-8", null);
										}					
									}
								});
						}
						catch(final IOException e)
						{
							main.runOnUiThread(new Runnable(){

									@Override
									public void run()
									{
										main.PreviewHwwv.wvc.onPageFinished(main.PreviewHwwv,"file://"+Do.URLEncode(Vers.i.OpenFile.getPath()));
										main.toast(R.string.php_is_preparing);
									}
								});
						}
						catch (final Exception e)
						{
							main.runOnUiThread(new Runnable(){

									@Override
									public void run()
									{
										main.PreviewHwwv.wvc.onPageFinished(main.PreviewHwwv,"file://"+Do.URLEncode(Vers.i.OpenFile.getPath()));
										Do.showErrDialog(MainActivity.main,e);
									}
								});
						}
					}
				}).start();
		}

		main.Topreview();
		main.binding.termux.reset();
		main.PreviewHwwv.clearCache(true);
		
		if(SettingsClass.isAutoPreviewFull)
		{
			main.fullScreenPreview();
		}
	}

	public static boolean isNewestPHP()
	{
		init();
		if(!phpVersionFile.exists())
			return false;
		try {
			String nowVersion=Do.getText(phpVersionFile);
			return Do.compareVersion(nowVersion,Vers.phpVersion) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static void downloadPhp(final Activity main)
	{
		init();
		Dl dl=new Dl(main);
		dl.builder.setTitle(R.string.lose_php)
		.setMessage(R.string.get_php_msg)
			.setPositiveButton(R.string.download, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface param1DialogInterface, int param1Int)
				{
					Do.showWaitAndRunInThread(true, new Runnable(){

							@Override
							public void run()
							{
								File compressedPHPFile=new File(Vers.i.FVenter,"php.zip");
								try
								{
									isDownloadingPhp=true;
									Do.downloadNet(Vers.i.serverHost+"/server/easyweb/php/php_"+Vers.phpVersion+".zip", compressedPHPFile.getPath());
									Do.upZipFileDir(compressedPHPFile, phpFile.getParentFile());
									phpVersionFile.createNewFile();
									Do.write(Vers.phpVersion,phpVersionFile);
									main.runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												Do.finishWaiting();
												new Dl(main,main.getString(R.string.done),main.getString(R.string.php_download_done),main.getString(R.string.ok));
												isDownloadingPhp=false;
											}
										});
								}
								catch (final Exception e)
								{
									phpFile.delete();
									compressedPHPFile.delete();
									main.runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												Do.finishWaiting();
												isDownloadingPhp=false;
												Dl dlDone=new Dl(MainActivity.main);
												dlDone.builder.setTitle(R.string.error);
												dlDone.builder.setMessage(Do.getErrorMsg(e));
												//dlDone.builder.setMessage(MainActivity.main.getString(R.string.php_download_error)+"\n"+MainActivity.main.getString(R.string.download_error));
												dlDone.builder.setPositiveButton(R.string.ok, null);
												dlDone.builder.setNeutralButton(R.string.solve_proj, new DialogInterface.OnClickListener(){

														@Override
														public void onClick(DialogInterface p1, int p2)
														{
															Do.openUrl(Vers.i.supportHost+"easywebide/article/#6",MainActivity.main);
														}
													});
												dlDone.show();
											}
										});
								}
							}
						});
				}
			});
		dl.show();
	}
	private static int reviewOrigin=0;
	private static String[] cusStringArr=null;
	public static void previewWithSettings()
	{
		main=MainActivity.main;
		
		LinearLayout Ly=new LinearLayout(main);
		ScrollView Sv=new ScrollView(main);
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(main);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        Ly=new LinearLayout(main);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
        Sv.addView(Rl);
        Rl.addView(Ly);
		
		final LinearLayout cusLy=new LinearLayout(main);
		cusLy.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        cusLy.setOrientation(LinearLayout.VERTICAL);
		
		final TextLayout baseUrlTl=new TextLayout(main);
		baseUrlTl.build(-1,"BaseURL",R.string.dec_baseurl,null);
		final TextLayout mimeTypeTl=new TextLayout(main);
		mimeTypeTl.build(-1,"MimeType",R.string.dec_mimeurl,null);
		final TextLayout encodingTl=new TextLayout(main);
		encodingTl.build(-1,"Encoding",R.string.dec_encoding,null);
		final TextLayout historyTl=new TextLayout(main);
		historyTl.build(-1,"HistoryURL",R.string.dec_history,null);
		
		cusLy.addView(baseUrlTl);
		cusLy.addView(mimeTypeTl);
		cusLy.addView(encodingTl);
		cusLy.addView(historyTl);
		
		if(cusStringArr!=null)
		{
			baseUrlTl.Acet.setText(cusStringArr[0]==null ? "" : cusStringArr[0]);
			mimeTypeTl.Acet.setText(cusStringArr[1]==null ? "" : cusStringArr[1]);
			encodingTl.Acet.setText(cusStringArr[2]==null ? "" : cusStringArr[2]);
			historyTl.Acet.setText(cusStringArr[3]==null ? "" : cusStringArr[3]);
		}
		
		final ChoiceLayout cl=new ChoiceLayout(main);
		cl.build(false,-1,null,R.string.review_origin,
				 main.getString(R.string.review_from_locale)+"|"+main.getString(R.string.review_from_website_server)+"|"+main.getString(R.string.review_from_custom));
		cl.Acs.setSelection(reviewOrigin,false);
		cl.Acs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if(p3==2)
					{
						//显示自定义
						cusLy.setVisibility(View.VISIBLE);
					}
					else
					{
						//隐藏自定义
						cusLy.setVisibility(View.GONE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
		
		Ly.addView(cl);
		Ly.addView(cusLy);
		cusLy.setVisibility(View.GONE);
			
		Dl dl=new Dl(main);
		dl.builder.setTitle(R.string.review_with_settings);
		dl.builder.setView(Sv);
		dl.builder.setPositiveButton(R.string.main_menu_review, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					cusStringArr=null;
					switch(cl.Acs.getSelectedItemPosition())
					{
						case 0:
							previewNowFile(true);
							break;
						case 1:
							previewNowFile(false);
							break;
						case 2:
							cusStringArr=new String[]{baseUrlTl.getText("$"),mimeTypeTl.getText("$"),encodingTl.getText("$"),historyTl.getText("$")};
							
							nowFile = main.getNowEpi().getFile();
							String S=main.getNowEditor().getString();
							HTMLText=S;
							
							if(SettingsClass.isAllowAbsolute)
							{
								main.save(new Runnable(){

										@Override
										public void run()
										{
											main.PreviewHwwv.loadDataWithBaseURL(baseUrlTl.getText("$")==null ? "" : baseUrlTl.getText("$"), HTMLText, mimeTypeTl.getText("$")==null ? "" : mimeTypeTl.getText("$"), encodingTl.getText("$")==null ? "" : encodingTl.getText("$"), historyTl.getText("$")==null ? "" : historyTl.getText("$"));
										}
									});
							}
							else
							{
								main.PreviewHwwv.loadDataWithBaseURL(baseUrlTl.getText("$")==null ? "" : baseUrlTl.getText("$"), HTMLText, mimeTypeTl.getText("$")==null ? "" : mimeTypeTl.getText("$"), encodingTl.getText("$")==null ? "" : encodingTl.getText("$"), historyTl.getText("$")==null ? "" : historyTl.getText("$"));				
							}
							
							main.Topreview();
							main.binding.termux.reset();
							main.PreviewHwwv.clearCache(true);

							if(SettingsClass.isAutoPreviewFull)
							{
								main.fullScreenPreview();
							}
							break;
					}
					reviewOrigin=cl.Acs.getSelectedItemPosition();
				}
			});
		dl.show();
	}
}
