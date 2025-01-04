package com.hiweb.ide;

import java.io.File;
import com.hiweb.ide.edit.Do;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import android.content.*;

public class LibraryManager {
	public String url;
	public File saveDir;
	public File versionFile;
	public File zipFile;
	public String name;

	public LibraryManager(String name) {
		this.name = name;
		url = Vers.i.serverHost + "server/hiweb/jsres/" + name + "/" + name + ".zip";
		saveDir = new File(Vers.i.FVenter, ".jsres/" + name + "/prod");
		versionFile = new File(saveDir, ".version.txt");
		zipFile = new File(saveDir, ".main.zip");
	}

	public void download(final String ver) {
		Do.showWaitAndRunInThread(true, new Runnable() {

			@Override
			public void run() {
				try {
					saveDir.mkdirs();
					versionFile.createNewFile();
					zipFile.createNewFile();
					Do.downloadNet(url, zipFile.getPath());
					Do.write(ver, versionFile);
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							MainActivity.main.toast(R.string.jsres_download_done);
						}
					});
				} catch (final Exception e) {
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							Dl dlErr = new Dl(MainActivity.main);
							dlErr.builder
									.setTitle(R.string.error)
									.setMessage(MainActivity.main.getString(R.string.jsres_download_error) + "\n"
											+ MainActivity.main.getString(R.string.download_error))
									.setPositiveButton(R.string.ok, null)
									.setNeutralButton(R.string.solve_proj, new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface p1, int p2) {
											Do.openUrl(Vers.i.supportHost + "easywebide/article/#6", MainActivity.main);
										}
									});
							dlErr.show();
						}
					});
				}

			}
		});
	}

	public void reg(final Map vMOpenFile) {
		final File rootDir = Vers.i.ProjectDir;
		Do.showWaitAndRunInThread(false, new Runnable() {
			List allHTMLFileList;

			@Override
			public void run() {
				try {
					File libFolder = new File(rootDir, name);
					for (int i = 0;; i++) {
						if (libFolder.exists()) {
							libFolder = new File(libFolder.getPath() + "_" + i);
						} else {
							break;
						}
					}
					libFolder.mkdirs();

					if (!Do.upZipFileDir(zipFile, libFolder)) {
						throw new Exception("Can't unzip the resource.");
					}

					allHTMLFileList = new ArrayList();
					openDir(rootDir);
					for (Object now : allHTMLFileList) {
						String html;
						if (vMOpenFile.containsKey(now)) {
							html = ((Editor) (vMOpenFile.get(now))).getString();
						} else {
							html = Do.getText((File) now);
						}

						Document doc = Jsoup.parse(html);
						doc.outputSettings().indentAmount(4);

						String newHtml = regTheHtml((File) now, doc, libFolder);
						if (vMOpenFile.containsKey(now)) {
							((Editor) (vMOpenFile.get(now))).replaceAll(newHtml);
						} else {
							Do.write(newHtml, (File) now);
						}
					}
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							MainActivity.main.binding.elvMainExplorerFile
									.toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
							Dl dl = new Dl(MainActivity.main);
							dl.builder.setTitle(R.string.done);
							dl.builder.setMessage(R.string.reg_jsres_done);
							dl.builder.setPositiveButton(R.string.ok, null);
							dl.show();
						}
					});
				} catch (final Exception e) {
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							Do.showErrDialog(MainActivity.main, e);
						}
					});

				}
			}

			private void openDir(File dir) {
				File[] files = dir.listFiles();
				for (File now : files) {
					if (now.isDirectory()) {
						openDir(now);
						continue;
					}
					if (now.getName().toLowerCase().endsWith(".html")) {
						allHTMLFileList.add(now);
					}
				}
			}
		});
	}

	public String regTheHtml(File now, Document doc, File libFolder) {
		return null;
	}
}
