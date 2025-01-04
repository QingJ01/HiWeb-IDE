package com.venter.easyweb;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.venter.easyweb.add.addViewWidget.ButtonLayout;
import com.venter.easyweb.edit.Do;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class FileChooseClass
{
	String BuilderName;
	File OpenPath;
	File Path;
	boolean ChooseType=true;
    boolean CanOpenWebsite=false;

	public File nowPath;
	File[] nowAllFile = null;
	File[] nowFiles = null;
	File[] nowFolders = null;
	File[] AfterAllFiles;
	String[] AfterAllFilesName = null;

	View.OnClickListener FolderChooseOnClick=null;
	OnFileClickListener FileChooseOnClick=null;

    Button ButtonChooseWebsite;
    Button ButtonChooseFoler;
    Button ButtonBack;
    Button ButtonHome;
    ImageButton ButtonBookmark;
    
	public AlertDialog dialog;
	public void Type(boolean IsFile)
	{
		ChooseType=IsFile;
	}
    public void canOpenWebsite()
    {
        CanOpenWebsite=true;
    }
	public void setOpenPath(File openPath)
	{
		OpenPath=openPath;
		nowPath=openPath;
	}

	// 存储文件列表
	private ArrayList<String> fileList = new ArrayList<>();

	public void Show(Context c)
	{
		String title="";
		if(ChooseType)
		{
			title=c.getString(R.string.choose_file);
		}
		else
		{
			title=c.getString(R.string.choose_folder);
		}
		Show(c,title);
	}
	
	public void Show(final Context context,String title)
	{
        Dl chooseDialog =
            new Dl(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_file_choose,null);
        
        ButtonChooseWebsite=dialogView.findViewById(R.id.btn_fileChoose_choose_website);
        ButtonChooseFoler=dialogView.findViewById(R.id.btn_fileChoose_choose_folder);
        ButtonBack=dialogView.findViewById(R.id.btn_fileChoose_back);
        ButtonHome=dialogView.findViewById(R.id.btn_fileChoose_home);
        ButtonBookmark=dialogView.findViewById(R.id.acib_fileChoose_bookmark);
        
		GoToFolder(OpenPath,context);

		TextView titleTv=dialogView.findViewById(R.id.tv_fileChoose_title);
		if(title==null)
		{
			titleTv.setVisibility(View.GONE);
		}
		else
		{
			titleTv.setText(title);
		}

		final TextView TextViewPath=(TextView) dialogView.findViewById(R.id.tv_fileChoose_path);
		TextViewPath.setText(nowPath.getPath());
		chooseDialog.builder.setTitle(BuilderName);
		chooseDialog.builder.setView(dialogView);

		dialog=chooseDialog.show();

		final HorizontalScrollView HorizontalScrollViewPath=(HorizontalScrollView) dialogView.findViewById(R.id.hsv_chooseFile_filePath);
		final ListView ListViewFile=(ListView) dialogView.findViewById(R.id.lv_fileChoose_file);
		List<Map<String, Object>> list=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName);
		ListViewFile.setAdapter(new ListAdspter(context, list));

		TextViewPath.setOnClickListener((p1)->{
			EditText editText=new EditText(MainActivity.main);
			editText.setText(nowPath.getPath());

			Dl dl =new Dl(MainActivity.main);
			dl.builder
					.setTitle(R.string.edit_path)
					.setView(editText)
					.setPositiveButton(R.string.ok,(d,w)->{
						try {
							File f = new File(editText.getText().toString());
							try
							{
								getFile(f);
								GoToFolder(f,context);
								List<Map<String, Object>> listn=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName);
								ListViewFile.setAdapter(new ListAdspter(context, listn));
								TextViewPath.setText(nowPath.getPath());

								Handler handler = new Handler();
								handler.post(new Runnable() {
									@Override
									public void run()
									{
										HorizontalScrollViewPath.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
									}
								});
							}
							catch(Exception e)
							{
								Do.showErrDialog(context,e);
							}
						}
						catch (Exception e)
						{
							MainActivity.main.toast(R.string.cannot_go_path);
						}
					});
			dl.show();
		});

		ListViewFile.setOnItemClickListener ( new OnItemClickListener ( ){

				@Override
				public void onItemClick ( AdapterView<?> p1, View p2, int p3, long p4 )
				{
					if(AfterAllFiles[p3].isDirectory())
					{
						try
						{
							getFile(AfterAllFiles[p3]);
							GoToFolder(AfterAllFiles[p3],context);
							List<Map<String, Object>> list=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName); 
							ListViewFile.setAdapter(new ListAdspter(context, list));
							TextViewPath.setText(nowPath.getPath());
							Handler handler = new Handler();
							handler.post(new Runnable() {
									@Override
									public void run()
									{
										HorizontalScrollViewPath.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
									}
								});
						}
						catch(Exception e)
						{
							Do.showErrDialog(context,e);
						}
					}
					else if(AfterAllFiles[p3].isFile())
					{

						FileChooseOnClick.onClick(AfterAllFiles[p3],dialog);
					}
				}
			} );
		ListViewFile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				JsonArray ja;
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					try
                    {
						final File now=AfterAllFiles[p3];
						
						if(now.isFile())
							return false;
						
						final File bookmarkFile=new File(context.getFilesDir(),"bookmark.json");
						
						ja=(JsonArray) new JsonParser().parse(new FileReader(bookmarkFile));
						if(ja==null)
						{
							ja=new JsonArray();
						}

						for(int i=0;i<ja.size();i++)
						{
							if(ja.get(i).getAsString().equals(now.getPath()))
							{
								return false;
							}
						}
						
						Dl dl=new Dl(context);
						dl.builder.setTitle(R.string.add_to_bookmark_qu);
						dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									ja.add(now.getPath());
									try
									{
										Do.write(ja.toString(), bookmarkFile);
										Toast.makeText(context,R.string.done,Toast.LENGTH_SHORT).show();
									}
									catch (IOException e)
									{
										Do.showErrDialog(context,e);
									}
								}
							});
						dl.show();
                    }
                    catch(Exception e)
                    {
                        Do.showErrDialog(context,e);
                    }
					
					return true;
				}
			});
        
		if(!ChooseType)
		{
			ButtonChooseFoler.setVisibility(View.VISIBLE);
			ButtonChooseFoler.setOnClickListener ( FolderChooseOnClick );
		}

		ButtonBack.setOnClickListener ( new View.OnClickListener ( ){

				@Override
				public void onClick ( View p1 )
				{
					if(nowPath.getPath().equals("/"))
					{
						Toast.makeText(context,context.getString(R.string.cant_back),Toast.LENGTH_SHORT).show();

					}
					else
					{
						if(nowPath.getPath().split("/").length==2)
						{
							GoToFolder(new File("/"),context);
						}
						else
						{
							try
							{
								getFile(nowPath.getParentFile());
								GoToFolder(nowPath.getParentFile(),context);
								List<Map<String, Object>> list=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName); 
								ListViewFile.setAdapter(new ListAdspter(context, list));
								TextViewPath.setText(nowPath.getPath());

								Handler handler = new Handler();
								handler.post(new Runnable() {
										@Override
										public void run()
										{
											HorizontalScrollViewPath.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
										}
									});
							}
							catch(Exception e)
							{
								Do.showErrDialog(context,e);
							}

						}

					}

				}
			} );
		ButtonHome.setOnClickListener ( new View.OnClickListener ( ){

				@Override
				public void onClick ( View p1 )
				{
					try
					{
						getFile(OpenPath);
						GoToFolder(OpenPath,context);
						List<Map<String, Object>> list=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName); 
						ListViewFile.setAdapter(new ListAdspter(context, list));
						TextViewPath.setText(nowPath.getPath());

						Handler handler = new Handler();
						handler.post(new Runnable() {
								@Override
								public void run()
								{
									HorizontalScrollViewPath.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
								}
							});
					}
					catch(Exception e)
					{
						Do.showErrDialog(context,e);

					}
				}
			} );
        ButtonBookmark.setOnClickListener ( new View.OnClickListener ( ){
				JsonArray ja;
                @Override
                public void onClick ( View p1 )
                {
                    try
                    {
						final File bookmarkFile=new File(context.getFilesDir(),"bookmark.json");
						if(!bookmarkFile.exists())
						{
							bookmarkFile.createNewFile();
							Do.write("[]",bookmarkFile);
						}
						ja=(JsonArray) new JsonParser().parse(new FileReader(bookmarkFile));
						if(ja==null)
						{
							ja=new JsonArray();
						}
						
						if(Vers.i.userName!=null&& Vers.i.userName.equals("admin"))
						{
							ja.add(context.getFilesDir().getPath());
						}
						
						ScrollView sv=new ScrollView(context);
						sv.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
						RelativeLayout rl=new RelativeLayout(context);
						rl.setLayoutParams(new ScrollView.LayoutParams(-1,-1));
						sv.addView(rl);
						LinearLayout ly=new LinearLayout(context);
						ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-1));
						ly.setPadding(Do.dp2px(context,10),Do.dp2px(context,10),Do.dp2px(context,10),Do.dp2px(context,10));
						ly.setOrientation(LinearLayout.VERTICAL);
						rl.addView(ly);

						Dl adb=new Dl(context);
						adb.builder.setTitle(R.string.bookmark);
						adb.builder.setView(sv);
						final AlertDialog ad=adb.show();

						for(int i=0;i<ja.size();i++)
						{
							final int time=i;
							final File nowFile=new File(ja.get(i).getAsString());
							ButtonLayout btn=new ButtonLayout(context);
							btn.leftImg.setVisibility(View.GONE);
							if(nowFile.getPath().equals(Vers.i.FProjects.getPath()))
							{
								btn.titleTv.setText(R.string.projects);
							}
							else
							{
								btn.titleTv.setText(nowFile.getName());
							}
							btn.descriptionTv.setText(nowFile.getPath());
							btn.setOnClickListener(new View.OnClickListener(){

									@Override
									public void onClick(View p1)
									{
										if(!nowFile.exists())
										{
											Toast.makeText(context,R.string.open_folder_error,Toast.LENGTH_SHORT).show();
											return;
										}
										ad.dismiss();
										getFile(nowFile);
										GoToFolder(nowFile,context);
										List<Map<String, Object>> list=getData(nowAllFile.length, nowFolders.length, nowFiles.length, AfterAllFilesName); 
										ListViewFile.setAdapter(new ListAdspter(context, list));
										TextViewPath.setText(nowPath.getPath());

										Handler handler = new Handler();
										handler.post(new Runnable() {
												@Override
												public void run()
												{
													HorizontalScrollViewPath.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
												}
											});
									}
								});
							btn.setOnLongClickListener(new View.OnLongClickListener(){

									@Override
									public boolean onLongClick(View p1)
									{
										if(ja.get(time).getAsString().equals(Vers.i.FProjects.getPath()))
										{
											return false;
										}
										Dl dl=new Dl(context);
										dl.builder.setTitle(context.getString(R.string.do_you_want_to_delete)+new File(ja.get(time).getAsString()).getName()+context.getString(R.string.review_element_del_qu2));
										dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
												@Override
												public void onClick(DialogInterface p1, int p2)
												{
													
													if((Vers.i.userName!=null&& Vers.i.userName.equals("admin")))
													{
														if(time==ja.size()-1)
														{
															return;
														}
														else
														{
															ja.remove(ja.size()-1);
														}
													}
													
													ja.remove(time);
													
													try
													{
														Do.write(ja.toString(), bookmarkFile);
														ad.dismiss();
													}
													catch (IOException e)
													{
														Do.showErrDialog(context,e);
													}
												}
											});
										dl.show();
										return true;
									}
								});
							ly.addView(btn);
						}
                    }
                    catch(Exception e)
                    {
                        Do.showErrDialog(context,e);
                    }


                }
			} );
			
        ButtonChooseWebsite.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    dialog.dismiss();
                    MainActivity.main.OpenWeb(nowPath,true);
                }
            });
	}

	public void setOnClickListener(View.OnClickListener onClick){
		FolderChooseOnClick=onClick;
	}
	public void setOnFileClickListener(OnFileClickListener onClick)
	{
		FileChooseOnClick=onClick;
	}
	private void GoToFolder(File path,Context context)
	{
		try
		{
            boolean IsWebsite=false;
            
			ArrayList<String> arr = getFile(path);
			for (File a : path.listFiles()) {
				// 打印出所有的文件,包含完整路径
				if(a.isDirectory())
				{
					a.listFiles();
				}
				else if(a.isFile())
				{
					if(!a.canRead())
					{
						throw new Exception();
					}
				}
			}
			nowAllFile=new File[arr.size()];
			AfterAllFiles=new File[arr.size()];
			AfterAllFilesName=new String[arr.size()];
			int i=0;
			for (String a : arr) {
				// 打印出所有的文件,包含完整路径
				nowAllFile[i]=new File(a);
				i++;
			}
			int fileNum = 0;
			int folderNum = 0;
			for(int a=0;a<arr.size();a++)
			{
				if(nowAllFile[a].isFile())
				{
					fileNum++;
				}
				else if(nowAllFile[a].isDirectory())
				{
					folderNum++;
				}
			}

			nowFiles=new File[fileNum];
			nowFolders=new File[folderNum];
			for(int a=0,fileIndex=0,folderIndex=0;a<arr.size();a++)
			{
				if(nowAllFile[a].isFile())
				{
					nowFiles[fileIndex]=nowAllFile[a];
					fileIndex++;
				}
				else if(nowAllFile[a].isDirectory())
				{
					nowFolders[folderIndex]=nowAllFile[a];
					folderIndex++;
				}
			}
			Arrays.sort(nowFiles);
			Arrays.sort(nowFolders);

			for(int a=0;a<folderNum;a++)
			{
				AfterAllFiles[a]=nowFolders[a];
			}
			for(int a=folderNum,b=0;a<arr.size();a++,b++)
			{
				AfterAllFiles[a]=nowFiles[b];
			}

			for(int a=0;a<folderNum;a++)
			{
				AfterAllFilesName[a]=nowFolders[a].getName();
			}
			for(int a=folderNum,b=0;a<arr.size();a++,b++)
			{
				AfterAllFilesName[a]=nowFiles[b].getName();
                if(AfterAllFilesName[a].equals("manifest.json")||AfterAllFilesName[a].equals("index.html")||AfterAllFilesName[a].equals("index.php"))
                {
                    IsWebsite=true;
                }
			}
			nowPath=path;
            
            if((ChooseType&&CanOpenWebsite)&&IsWebsite)
            {
                ButtonChooseWebsite.setVisibility(View.VISIBLE);
            }
            else
            {
                ButtonChooseWebsite.setVisibility(View.GONE);
            }

		}
		catch(Exception e)
		{
			Do.showErrDialog(context,e);
		}
	}
	private List<Map<String, Object>> getData(int allNumber, int folderNumber, int fileNumber, String[] allName)
	{ 
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>(); 
		for (int i = 0; i < folderNumber; i++)
		{ 
			Map<String, Object> map=new HashMap<String, Object>(); 
			map.put("image", R.drawable.folder); 
			map.put("name", allName[i]);
			list.add(map); 
		} 
		for (int i=folderNumber;i < allNumber;i++)
		{
			Map<String, Object> map=new HashMap<String, Object>(); 
			map.put("image", R.drawable.list_file); 
			map.put("name", allName[i]);
			list.add(map); 
		}
		return list; 
	} 

	private ArrayList<String> getFile(File path)  {
		this.fileList=new ArrayList<>();
		File[] listFile = path.listFiles();
		for (File a : listFile) {
			if(!ChooseType)
			{
				if(a.isDirectory())
				{
					this.fileList.add(a.getAbsolutePath());
				}
			}
			else
			{
				this.fileList.add(a.getAbsolutePath());
			}
		}
		return fileList;

	}
}
class ListAdspter extends BaseAdapter
{ 

	private List<Map<String, Object>> data; 
	private LayoutInflater layoutInflater; 
	private Context context; 
	public ListAdspter(Context context, List<Map<String, Object>> data)
	{ 
		this.context = context; 
		this.data = data; 
		this.layoutInflater = LayoutInflater.from(context); 
	} 
	/** 
	 * 组件集合，对应list.xml中的控件 
	 * @author Administrator 
	 */ 
	public final class Zujian
	{ 
		public ImageView image; 
		public TextView name; 
	} 
	@Override 
	public int getCount()
	{ 
		return data.size(); 
	} 
	/** 
	 * 获得某一位置的数据 
	 */ 
	@Override 
	public Object getItem(int position)
	{ 
		return data.get(position); 
	} 
	/** 
	 * 获得唯一标识 
	 */ 
	@Override 
	public long getItemId(int position)
	{ 
		return position; 
	} 

	@Override 
	public View getView(int position, View convertView, ViewGroup parent)
	{ 
		Zujian zujian=null; 
		if (convertView == null)
		{ 
			zujian = new Zujian(); 
			//获得组件，实例化组件 
			convertView = layoutInflater.inflate(R.layout.list_items, null); 
			zujian.image = (ImageView)convertView.findViewById(R.id.iv_listItems_icon); 
			zujian.name = (TextView)convertView.findViewById(R.id.tv_listItems_file_name); 

			convertView.setTag(zujian); 
		}
		else
		{ 
			zujian = (Zujian)convertView.getTag(); 
		} 
		//绑定数据 



		zujian.image.setImageResource((Integer)data.get(position).get("image")); 
		zujian.name.setText((String)data.get(position).get("name")); 

		return convertView; 
	} 
} 

