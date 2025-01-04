package com.hiweb.ide;
import com.hiweb.ide.edit.Do;
import android.content.DialogInterface;

public class UpdateManager
{
	private static String vers;
	public static void check(final boolean isShowToast,final BaseActivity activity)
	{
		vers=null;
		Runnable runnable=new Runnable(){

			@Override
			public void run()
			{
				vers=Do.testGetHtml(Vers.i.serverHost+"server/easyweb/update/lastest_version.txt",6);

				Vers.i.easyAppNewestVersion =Do.testGetHtml(Vers.i.serverHost+"server/easyweb/easyapp/version.txt",6);

				activity.runOnUiThread(() -> {
					try
					{
						if(vers!=null&&vers.indexOf("<andnext>")!=-1&&vers.split("<andnext>").length==3)
						{

							String[] versArr=vers.split("<andnext>");
							int newVersionCode=Integer.parseInt(versArr[0]);
							String newVersionName=versArr[1];
							String newLog=versArr[2];
							int nowVersionCode=Do.getVersionCode();

							if(newVersionCode>nowVersionCode)
							{
								Dl dl=new Dl(activity);
								dl.builder.setTitle(activity.getString(R.string.main_update_title)+newVersionName);
								dl.builder.setMessage(newLog);
								dl.builder.setPositiveButton(R.string.main_update_btn, new DialogInterface.OnClickListener(){

										@Override
										public void onClick(DialogInterface p1, int p2)
										{
											Do.openUrl("https://www.coolapk.com/apk/com.hiweb.ide",activity);
										}
									});
								dl.builder.setNegativeButton(android.R.string.cancel,null);
								dl.builder.setCancelable(false);
								dl.show();
							}
							else
							{
								if(isShowToast)
								{
									activity.toast(R.string.main_about_newver);
								}
							}
						}
						else
						{
							throw new Exception();
						}
					}
					catch(Exception e)
					{
						if(isShowToast)
						{
							activity.toast(R.string.cant_check_update);
						}
					}
					finally
					{
						if(isShowToast)
						{
							Do.finishWaiting();
						}
					}

				});
			}
		};

		if(isShowToast)
		{
			Do.showWaitAndRunInThread(true,activity,runnable);
		}
		else
		{
			new Thread(runnable).start();
		}
	}
}
