package com.hiweb.ide;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.hiweb.ide.edit.Do;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class ExplorerListView extends ListView {
	private File[] NowFile;

	private ArrayList<String> fileList = new ArrayList<>();

	private ToFile toFile;

	public ExplorerListView(android.content.Context context) {
		super(context);
		init();
	}

	public ExplorerListView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ExplorerListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ExplorerListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		toFile = new ToFile();
		setOnItemClickListener(new AdapterView.OnItemClickListener() {

			int fileItemIndex = -1;

			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int itemIndex, long p4) {
				fileItemIndex = itemIndex;
				if (!Vers.i.ExplorerPath.equals("")) {
					fileItemIndex--;
					if (itemIndex == 0) {
						goback();
						return;
					}
				}
				if (NowFile[fileItemIndex].isFile()) {
					if (NowFile[fileItemIndex].getName().lastIndexOf(".") != -1) {
						String fileType = NowFile[fileItemIndex].getName()
								.substring(NowFile[fileItemIndex].getName().lastIndexOf(".") + 1).toLowerCase();
						try {
							boolean isTextFile = Do.isTextFile(NowFile[fileItemIndex]);
							if (fileType.equals("svg"))
								isTextFile = false;
							if (NowFile[fileItemIndex].getPath()
									.equals(Vers.i.ProjectDir.getPath() + "/MakeAPK.hiweb")) {
								Vers.i.nowProjectPackMachine.runMakeAPK(NowFile[fileItemIndex], false);
							} else if (isTextFile) {
								if (Vers.i.IsOpeningFile
										&& Vers.i.OpenFile.getPath().equals(NowFile[fileItemIndex].getPath())) {
									MainActivity.main.toast(R.string.main_open_opened);
								} else {
									File OpenFile = NowFile[fileItemIndex];
									MainActivity.main.GoneMore();
									MainActivity.main.OpenFile(OpenFile);
								}

							} else if (fileType.equals("png") || fileType.equals("jpg") || fileType.equals("jpeg")
									|| fileType.equals("svg") || fileType.equals("ico")) {
								new ImagePlayer().play(NowFile[fileItemIndex]);
							} else if (fileType.equals("zip")) {
								final File zipFile = NowFile[fileItemIndex];
								Dl dl = new Dl(getContext());
								dl.builder.setTitle(R.string.unzip_title);
								dl.builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface p1, int p2) {
										// TODO: Implement this method
										final File upPath = new File(zipFile.getParent() + "/"
												+ zipFile.getName().substring(0, zipFile.getName().length() - 4));
										if (upPath.exists()) {
											MainActivity.main.toast(R.string.main_new_file_exists);
											return;
										}
										upPath.mkdirs();

										Do.showWaitAndRunInThread(false, new Runnable() {

											@Override
											public void run() {
												if (Do.upZipFileDir(zipFile, upPath)) {
													MainActivity.main.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															Do.finishWaiting();
															MainActivity.main.toast(R.string.done);
															toExplorer(upPath);
														}
													});
												} else {
													MainActivity.main.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															Do.finishWaiting();
															Do.showErrDialog(MainActivity.main,
																	new Exception("Unzip failed."));
														}
													});
												}
											}
										});
									}
								});
								dl.show();
							} else if (fileType.equals("apk")) {
								Do.installApkFile(MainActivity.main, NowFile[fileItemIndex].getPath());
							} else {
								Do.openFile(getContext(), NowFile[fileItemIndex]);
							}
						} catch (Exception e) {
							Do.showErrDialog(getContext(), e);
						}
					} else {
						if (Vers.i.IsOpeningFile
								&& Vers.i.OpenFile.getPath().equals(NowFile[fileItemIndex].getPath())) {
							MainActivity.main.toast(R.string.main_open_opened);
						} else {
							File OpenFile = NowFile[fileItemIndex];
							MainActivity.main.GoneMore();
							MainActivity.main.OpenFile(OpenFile);
						}
					}
				} else {
					toExplorer(NowFile[fileItemIndex]);
				}
			}
		});
		setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			FileChooseClass Fcc;
			int fileItemIndex = -1;

			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int itemIndex, long p4) {
				fileItemIndex = itemIndex;
				if (!Vers.i.ExplorerPath.equals("")) {
					fileItemIndex--;
					if (itemIndex == 0) {
						return false;
					}
				}

				String[] listArr;
				listArr = new String[] { getContext().getString(R.string.main_explorer_open_with),
						getContext().getString(R.string.main_explorer_rename),
						getContext().getString(R.string.main_explorer_delete),
						getContext().getString(R.string.main_explorer_copy_path),
						getContext().getString(R.string.main_explorer_move),
						getContext().getString(R.string.main_explorer_copy) };

				Dl dl = new Dl(getContext());
				dl.builder.setTitle(NowFile[fileItemIndex].getName())
						.setItems(listArr, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface p1, int p2) {

								switch (p2) {
									case 0:
										if (NowFile[fileItemIndex].isFile()) {
											Do.openFile(getContext(), NowFile[fileItemIndex]);
										} else {
											MainActivity.main.toast(R.string.main_explorer_open_with_dir);
										}
										break;
									case 1:
										final EditText et = new EditText(getContext());
										et.setText(NowFile[fileItemIndex].getName());
										Dl dl = new Dl(getContext());
										dl.builder.setView(et);
										dl.builder.setPositiveButton(R.string.ok, (p11, p21) -> {
											if (NowFile[fileItemIndex]
													.renameTo(new File(NowFile[fileItemIndex].getParent() + "/"
															+ et.getText().toString()))) {
												toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
												MainActivity.main.toast(R.string.done);
											} else {
												Do.showErrDialog(getContext(), new IOException("Can't rename file."));
											}
										});
										dl.show();

										break;
									case 2:

										Dl dl2 = new Dl(getContext());
										dl2.builder.setTitle(R.string.main_explorer_delete_dialog);
										if (NowFile[fileItemIndex].getName().equals("manifest.json")
												&& NowFile[fileItemIndex].getParent()
														.equals(Vers.i.ProjectDir.getPath()))
											dl2.builder.setMessage(R.string.delete_manifest_alert);
										dl2.builder.setPositiveButton(R.string.main_explorer_delete, (p112, p212) -> {
											if (Do.delete(NowFile[fileItemIndex].getPath())) {
												toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
												MainActivity.main.toast(R.string.done);
											} else {
												Do.showErrDialog(getContext(),
														new IOException("Can't delete the file."));
											}
										});
										dl2.show();
										break;
									case 3:
										MainActivity.main.CopyExplorerPath(NowFile[fileItemIndex]);
										break;
									case 4:
										Fcc = new FileChooseClass();
										Fcc.Type(false);
										if (Vers.i.IsOpenProject) {
											Fcc.setOpenPath(Vers.i.ProjectDir);
										} else {
											Fcc.setOpenPath(Environment.getExternalStorageDirectory());
										}
										Fcc.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View p1) {
												try {
													if (new File(Fcc.nowPath.getPath(),
															NowFile[fileItemIndex].getName()).exists()) {
														MainActivity.main.toast(R.string.main_new_file_exists);
														return;
													} else if (new File(Fcc.nowPath.getPath(),
															NowFile[fileItemIndex].getName()).getPath()
															.startsWith(NowFile[fileItemIndex].getPath())) {
														MainActivity.main.toast(R.string.copy_much_err);
														return;
													}

													Fcc.dialog.dismiss();
													File FTo = Fcc.nowPath;
													if (NowFile[fileItemIndex].isFile()) {
														Do.copySdcardFile(NowFile[fileItemIndex].getPath(),
																new File(FTo.getPath(),
																		NowFile[fileItemIndex].getName()).getPath());
													} else {
														if (Do.copyDir(NowFile[fileItemIndex].getPath() + "/",
																new File(FTo.getPath(),
																		NowFile[fileItemIndex].getName()).getPath()
																		+ "/") == -1)
															throw new Exception();
													}
													if (!Do.delete(NowFile[fileItemIndex].getPath()))
														throw new Exception();
													if (FTo.getPath().length() >= Vers.i.ProjectDir.getPath().length()
															&& FTo.getPath()
																	.substring(0, Vers.i.ProjectDir.getPath().length())
																	.equals(Vers.i.ProjectDir.getPath())) {
														toExplorer(FTo);
													}
													MainActivity.main.toast(R.string.done);
												} catch (Exception e) {
													Do.showErrDialog(getContext(), e);
												}

											}
										});
										Fcc.Show(getContext());
										break;
									case 5:
										Fcc = new FileChooseClass();
										Fcc.Type(false);
										if (Vers.i.IsOpenProject) {
											Fcc.setOpenPath(Vers.i.ProjectDir);
										} else {
											Fcc.setOpenPath(Environment.getExternalStorageDirectory());
										}
										Fcc.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View p1) {
												try {
													if (new File(Fcc.nowPath.getPath(),
															NowFile[fileItemIndex].getName()).exists()) {
														MainActivity.main.toast(R.string.main_new_file_exists);
														return;
													} else if (new File(Fcc.nowPath.getPath(),
															NowFile[fileItemIndex].getName()).getPath()
															.startsWith(NowFile[fileItemIndex].getPath())) {
														MainActivity.main.toast(R.string.copy_much_err);
														return;
													}
													Fcc.dialog.dismiss();
													File FTo = Fcc.nowPath;
													if (NowFile[fileItemIndex].isFile()) {
														Do.copySdcardFile(NowFile[fileItemIndex].getPath(),
																new File(FTo.getPath(),
																		NowFile[fileItemIndex].getName()).getPath());
													} else {
														if (Do.copyDir(NowFile[fileItemIndex].getPath() + "/",
																new File(FTo.getPath(),
																		NowFile[fileItemIndex].getName()).getPath()
																		+ "/") == -1)
															throw new Exception();
													}
													if (FTo.getPath().length() >= Vers.i.ProjectDir.getPath().length()
															&& FTo.getPath()
																	.substring(0, Vers.i.ProjectDir.getPath().length())
																	.equals(Vers.i.ProjectDir.getPath())) {
														toExplorer(FTo);
													}
													MainActivity.main.toast(R.string.done);
												} catch (Exception e) {
													Do.showErrDialog(getContext(), e);
												}

											}
										});
										Fcc.Show(getContext());
										break;
								}
							}
						});
				dl.show();
				return true;
			}
		});
	}

	public void goback() {
		if (Vers.i.ExplorerPath.equals("")) {
			MainActivity.main.toast(R.string.main_explorer_can_not_goback);
		} else {
			String gobackPath = Vers.i.ExplorerPath.substring(0, Vers.i.ExplorerPath.lastIndexOf("/"));
			toExplorer(new File(Vers.i.ProjectDir.getPath() + gobackPath));
		}
	}

	public void toExplorer(File path) {
		putFile(path);
		NowFile = toFile.getFileList(path);
		Vers.i.ExplorerPath = path.getPath().substring(Vers.i.ProjectDir.getPath().length());
		List<Map<String, Object>> list = getData(NowFile.length, toFile.dirNum, toFile.fileNum, NowFile);
		setAdapter(new FileAdspter(getContext(), list));
		setSelection(0);
		MainActivity.main.binding.tvMainMoreSubtitle
				.setText(getContext().getString(R.string.main_explorer_website) + Vers.i.ExplorerPath);
		Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.main.binding.hsvMainExplorerSubtitle.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}
		});
	}

	private void putFile(File path) {
		this.fileList = new ArrayList<>();
		File[] listFile = path.listFiles();

		for (File a : listFile) {
			this.fileList.add(a.getAbsolutePath());
		}
	}

	private List<Map<String, Object>> getData(int allNumber, int folderNumber, int fileNumber, File[] allName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!Vers.i.ExplorerPath.equals("")) {
			// 判断加不加返回按钮
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", R.drawable.explorer_list_back);
			map.put("name", "..");
			list.add(map);
		}
		for (int i = 0; i < folderNumber; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", R.drawable.explorer_list_folder);
			map.put("name", allName[i].getName());
			list.add(map);
		}
		for (int i = folderNumber; i < allNumber; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("de", -1);
			if (allName[i].getName().equals("manifest.json")
					&& allName[i].getParent().equals(Vers.i.ProjectDir.getPath())) {
				map.put("image", R.drawable.explorer_list_manifest);
				map.put("de", R.string.manifest_json_de);
			} else if (allName[i].getName().equals("MakeAPK.hiweb")
					&& allName[i].getParent().equals(Vers.i.ProjectDir.getPath())) {
				map.put("image", R.drawable.explorer_list_exe);
				map.put("de", R.string.click_makeapk);
			} else if (allName[i].getName().equals("easyapp-events.js")
					&& allName[i].getParent().equals(Vers.i.ProjectDir.getPath())) {
				map.put("image", R.drawable.explorer_list_js);
				map.put("de", R.string.easyapp_events_js_de);
			} else if (allName[i].getName().toLowerCase().endsWith(".html") ||
					allName[i].getName().toLowerCase().endsWith(".htm") ||
					allName[i].getName().toLowerCase().endsWith(".asp") ||
					allName[i].getName().toLowerCase().endsWith(".aspx") ||
					allName[i].getName().toLowerCase().endsWith(".jsp")) {
				map.put("image", R.drawable.explorer_list_html);
			} else if (allName[i].getName().toLowerCase().endsWith(".css")) {
				map.put("image", R.drawable.explorer_list_css);
			} else if (allName[i].getName().toLowerCase().endsWith(".js")) {
				map.put("image", R.drawable.explorer_list_js);
			} else if (allName[i].getName().toLowerCase().endsWith(".png")
					|| allName[i].getName().toLowerCase().endsWith(".bmp")
					|| allName[i].getName().toLowerCase().endsWith(".jpg")
					|| allName[i].getName().toLowerCase().endsWith(".svg")
					|| allName[i].getName().toLowerCase().endsWith(".ico")) {
				map.put("image", R.drawable.explorer_list_image);
			} else if (allName[i].getName().toLowerCase().endsWith(".php")) {
				map.put("image", R.drawable.explorer_list_php);
			} else if (allName[i].getName().toLowerCase().endsWith(".xml")) {
				map.put("image", R.drawable.explorer_list_xml);
			} else if (allName[i].getName().toLowerCase().endsWith(".jss")) {
				map.put("image", R.drawable.explorer_list_jss);
			} else {
				map.put("image", R.drawable.explorer_list_file);
				if (allName[i].getName().equals("$INF$.hiweb")
						&& allName[i].getParent().equals(Vers.i.ProjectDir.getPath())) {
					map.put("de", R.string.inf_easyweb_de);
				}
			}
			map.put("name", allName[i].getName());
			list.add(map);
		}
		return list;
	}

}

class ToFile {
	public int fileNum = 0;
	public int dirNum = 0;

	public File[] getFileList(File dirPath) {
		File[] AllList = dirPath.listFiles();
		fileNum = 0;
		dirNum = 0;
		for (int i = 0; i < AllList.length; i++) {
			if (AllList[i].isFile()) {
				fileNum++;
			} else if (AllList[i].isDirectory()) {
				dirNum++;
			}
		}
		File[] fileList = new File[fileNum];
		File[] dirList = new File[dirNum];
		for (int i = 0, f = 0, d = 0; i < AllList.length; i++) {
			if (AllList[i].isFile()) {
				fileList[f] = AllList[i];
				f++;
			} else if (AllList[i].isDirectory()) {
				dirList[d] = AllList[i];
				d++;
			}
		}
		Arrays.sort(fileList);
		Arrays.sort(dirList);
		File[] sortAllList = new File[AllList.length];
		int a = 0;
		for (int i = 0; i < dirNum; i++) {
			sortAllList[a] = dirList[i];
			a++;
		}
		for (int i = 0; i < fileNum; i++) {
			sortAllList[a] = fileList[i];
			a++;
		}

		return sortAllList;
	}
}

class FileAdspter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public FileAdspter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	/**
	 * 组件集合，对应list.xml中的控件
	 * 
	 * @author Administrator
	 */
	public final class Zujian {
		public ImageView image;
		public TextView name;
		public TextView de;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Zujian zujian = null;
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(R.layout.list_explorer_item, null);
			zujian.image = (ImageView) convertView.findViewById(R.id.ib_main_explorer_list_item);
			zujian.name = (TextView) convertView.findViewById(R.id.tv_main_explorer_list_item);
			zujian.de = (TextView) convertView.findViewById(R.id.tv_main_explorer_list_item_de);
			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
		Drawable drawable = MainActivity.main.getDrawable((Integer) data.get(position).get("image"));
		drawable.setTint(ContextCompat.getColor(convertView.getContext(), R.color.opposition));
		zujian.image.setImageDrawable(drawable);
		zujian.name.setText((String) data.get(position).get("name"));
		if (((Map<String, Object>) data.get(position)).containsKey("de") && (int) data.get(position).get("de") != -1) {
			zujian.de.setVisibility(View.VISIBLE);
			zujian.de.setText((int) data.get(position).get("de"));
		} else {
			zujian.de.setVisibility(View.GONE);
		}

		return convertView;
	}
}
