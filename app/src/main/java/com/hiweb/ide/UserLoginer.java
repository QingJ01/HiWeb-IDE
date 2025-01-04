package com.venter.easyweb;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.venter.easyweb.add.addViewWidget.TextLayout;
import com.venter.easyweb.edit.Do;
import java.io.File;
import java.io.IOException;
import android.app.AlertDialog;

public class UserLoginer
{
    public static void showLogin(String deUsername)
    {
        View dialogView = LayoutInflater.from(MainActivity.main)
            .inflate(R.layout.dialog_login,null);
        
        final TextLayout Tl=dialogView.findViewById(R.id.tl_user_login);
        Tl.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
        Tl.build(-1,null,R.string.make_your_username,null);
        Tl.Acet.setText(deUsername);
        
        Dl Adb=new Dl(MainActivity.main);
        Adb.builder.setView(dialogView);
        Adb.builder.setPositiveButton(R.string.apply,null);
        final AlertDialog Ad=Adb.show();
        Ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    if(saveInf(Tl.getText("$")))
                    {
                        Ad.dismiss();
						MainActivity.main.binding.wwvWelcome.loadWhenShowWelcome();
                    }
                }
            });
    }
    private static boolean saveInf(String user)
    {
        if(user!=null&&(!user.trim().equals("")&&(user.trim().indexOf("\\")==-1&&user.trim().indexOf("\"")==-1&&user.trim().indexOf("/")==-1&&user.trim().indexOf("&")==-1&&user.length()<=12)))
        {
            JsonObject object=new JsonObject();
            object.addProperty("name",user);

            try {
                if(!Vers.i.userFile.getParentFile().exists())
                    new File(Vers.i.userFile.getParent()).mkdirs();
                Vers.i.userFile.createNewFile();

                Do.write(object.toString(), Vers.i.userFile);
                Vers.i.userName=user;
                
                MainActivity.main.showUser();
                return true;
            } catch (IOException e)
            {
                Do.showErrDialog(MainActivity.main,e);
                return false;
            }
        }
        else
        {
            MainActivity.main.toast(R.string.user_err);
            return false;
        }
    }
	
	private static String[] SaItems;
    public static void showUserInf()
    {
        Dl AdbUser=new Dl(MainActivity.main);
        View dialogView = LayoutInflater.from(MainActivity.main)
            .inflate(R.layout.dialog_myuser,null);
        TextView ActvHello=dialogView.findViewById(R.id.tv_user_hello);
        ListView LvcControl=dialogView.findViewById(R.id.lv_user_control);
        ActvHello.setText(MainActivity.main.getString(R.string.hello)+ Vers.i.userName+MainActivity.main.getString(R.string.control_username));
        AdbUser.builder.setView(dialogView);
        final AlertDialog Ad=AdbUser.show();
        
        SaItems=new String[]
		{
			MainActivity.main.getString(R.string.user_item_rename),
			MainActivity.main.getString(R.string.user_item_del)
		};
		if(Vers.i.userName.equals("admin"))
		{
			String[] array=new String[SaItems.length+1];
			
			for(int i=0;i<SaItems.length;i++)
			{
				array[i]=SaItems[i];
			}
			
			array[array.length-1]="Debug＞";
			SaItems=array;
		}
        ArrayAdapter adapter=new ArrayAdapter(MainActivity.main,android.R.layout.simple_list_item_1,SaItems);
        LvcControl.setAdapter(adapter);
        LvcControl.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                    switch(p3)
                    {
                        case 0:
                            //重新拟定用户名
                            Ad.dismiss();
                            showLogin(Vers.i.userName);
                            break;
                        case 1:
                            //清除用户名
                            Dl AdbDel=new Dl(MainActivity.main);
                            AdbDel.builder.setTitle(R.string.del_user_title);
                            AdbDel.builder.setMessage(R.string.del_user_msg);
                            AdbDel.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface p1, int p2) {
                                        
                                        if(!Vers.i.userFile.delete())
                                        {
                                            Do.showErrDialog(MainActivity.main,new IOException("Can't delete the file."));
                                        }
                                        else
                                        {
                                            Vers.i.userName=null;
                                            MainActivity.main.showUser();
											MainActivity.main.binding.wwvWelcome.loadWhenShowWelcome();
                                            Ad.dismiss();
                                        }
                                        
                                    }
                                });
                            AdbDel.show();
                            break;
                    }
					if(SaItems[p3].equals("Debug＞"))
					{
						//DEBUG
						String[] items=new String[]{
							"Copy Private Files To EasyWeb Folder",
							"Clear History",
							"Clear Tips",
							"Clear All Data",
							"Throw An Exception",
							"Open Private Files As Website",
                            "Delete signs",
                            "Exec shell",
                            "Make file executable"
						};
						Dl dl=new Dl(MainActivity.main);
						dl.builder.setItems(items, new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									switch(p2)
									{
										case 0:
											try
											{
												Do.copyDir(MainActivity.main.getFilesDir().getPath(), Vers.i.FVenter.getPath() + "/files/");
											}
											catch (Exception e)
											{
												Do.showErrDialog(MainActivity.main,e);
											}
											break;
										case 1:
											new File(MainActivity.main.getFilesDir(),"htx.json").delete();
											break;
										case 2:
											Do.delete(new File(MainActivity.main.getFilesDir(),"signs").getPath());
											break;
										case 3:
											Do.delete(MainActivity.main.getFilesDir().getPath());
											break;
										case 4:
											int test=1/0;
											break;
										case 5:
											MainActivity.main.OpenWeb(MainActivity.main.getFilesDir(),false);
											break;
                                        case 6:
                                            File signDir = new File(MainActivity.main.getFilesDir(),"signs");
                                            signDir.mkdirs();
                                            File[] signFiles =  signDir.listFiles();
                                            String[] signs = new String[signFiles.length];
                                            for (int i =0;i<signFiles.length;i++)
                                            {
                                                signs[i]=signFiles[i].getName();
                                            }
                                            Dl dl = new Dl(MainActivity.main);
                                            dl.builder
                                                    .setTitle("Select the sign to delete")
                                                    .setItems(signs,(dialog,which)->{
                                                        signFiles[which].delete();
                                                    })
                                                    .setPositiveButton("Clear All",(dialog,which)->{
                                                        Do.delete(signDir.getPath());
                                                    });
                                            dl.show();
                                            break;
                                        case 7:
                                            Dl shellDl =new Dl(MainActivity.main);
                                            TextLayout textLayout=new TextLayout(MainActivity.main);
                                            textLayout.build(-1,null,-1,null);
                                            shellDl.builder
                                                    .setTitle("Exec Shell")
                                                    .setView(textLayout)
                                                    .setPositiveButton(R.string.ok,(d,w)->{
                                                        try {
                                                            String o= Do.runCommand(textLayout.getText("$").split(" "));
                                                            new Dl(MainActivity.main,"Output",o,"Close");
                                                        } catch (Exception e) {
                                                            Do.showErrDialog(MainActivity.main,e);
                                                        }
                                                    });
                                            shellDl.show();
                                            break;
                                        case 8:
                                            FileChooseClass fileChoose = new FileChooseClass();
                                            fileChoose.Type(true);
                                            fileChoose.setOpenPath(Environment.getExternalStorageDirectory());
                                            fileChoose.setOnFileClickListener(new OnFileClickListener() {
                                                @Override
                                                public void onClick(final File ChooseFile, final AlertDialog dialog) {
                                                    ChooseFile.setExecutable(true,false);
                                                    MainActivity.main.toast("Done");
                                            }});
                                            fileChoose.Show(MainActivity.main);
                                            break;
									}
								}
							});
						dl.show();
					}
                }
            });
    }
}
