package com.venter.easyweb;
import android.content.*;
import android.view.*;
import com.venter.easyweb.edit.*;
import com.venter.easyweb.server.*;
import java.io.*;
import java.util.Objects;

public class Menus
{
	/*
		{
			R.drawable.xxx,R.string.xxx,new View.onClickListener(){},(Menus.SAVE)
		}
	*/
	public static int SAVE=0x1A;
	public static int CLOSE_OTHER=0x2A;
	public static int CLOSE_ALL=0x3A;
	public static int ADD_JS=0x4A;
	public static int STOP_JS=0x5A;
	public static int CONTINUE_HW=0x6A;
	public static int FULL_PREVIEW=0x7A;
	public static int HALF_PREVIEW =0x8A;
	public static int PAUSE_JS=0x9A;
	public static int PREVIEW=0x10A;
	public static int ZOOM_MODE_PC=0x11A;
	public static int ZOOM_MODE_MP=0x12A;
	
	public static Object[] saveMenu={R.drawable.save,R.string.main_menu_save, (View.OnClickListener) p1 -> MainActivity.main.save(),SAVE};
	public static Object[] addMenu={R.drawable.add,R.string.insert, (View.OnClickListener) p1 -> MainActivity.main.add()};
	public static Object[] reviewElementsMenu={R.drawable.review_element,R.string.main_menu_review_element, (View.OnClickListener) p1 -> MainActivity.main.reviewElement()};
	public static Object[] formatMenu={R.drawable.format_code,R.string.main_menu_format, (View.OnClickListener) p1 -> MainActivity.main.getNowEditor().format()};
	public static Object[] findAndReplaceMenu={R.drawable.find_replace,R.string.find_and_replace, (View.OnClickListener) p1 -> MainActivity.main.getNowEditor().findText()};
	public static Object[] goToLineMenu={R.drawable.skip_line,R.string.go_to_line, (View.OnClickListener) p1 -> MainActivity.main.getNowEditor().gotoline()};
	public static Object[] closeMenu={R.drawable.close_file,R.string.close_now_file, (View.OnClickListener) p1 -> MainActivity.main.CloseFile()};
		
	public static Object[] closeOtherMenu={R.drawable.close_other,R.string.close_other_file, (View.OnClickListener) p1 -> MainActivity.main.closeOther(),CLOSE_OTHER};
		
	public static Object[] closeAllMenu={R.drawable.close_all,R.string.close_all_file, (View.OnClickListener) p1 -> MainActivity.main.closeAll(),CLOSE_ALL};
	public static Object[] runJsMenu={R.drawable.preview,R.string.main_menu_run, (View.OnClickListener) p1 -> {
		if(SettingsClass.isAllowAbsolute)
		{
			MainActivity.main.save(() -> MainActivity.main.runJs());
		}
		else
		{
			MainActivity.main.runJs();
		}
	}};
	public static Object[] previewMenu={R.drawable.preview,R.string.main_menu_review, (View.OnClickListener) p1 -> PagePreviewer.previewNowFile(false),PREVIEW};
	public static Object[] previewSettingMenu={R.drawable.preview_set,R.string.review_with_settings, (View.OnClickListener) p1 -> PagePreviewer.previewWithSettings()};
	public static Object[] editFullMenu={R.drawable.full_edit,R.string.fullscreen_edit, (View.OnClickListener) p1 -> MainActivity.main.fullScreenEdit()};
	public static Object[][] mainMenuHTML={
		saveMenu,
		previewMenu,
		previewSettingMenu,
		addMenu,
		reviewElementsMenu,
		formatMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuCSS={
		saveMenu,
		addMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuJS={
		saveMenu,
		runJsMenu,
		addMenu,
		formatMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuJSS={
		saveMenu,
		runJsMenu,
		addMenu,
		formatMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuPHP={
		saveMenu,
		previewMenu,
		addMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuText={
		saveMenu,
		previewMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
	public static Object[][] mainMenuXml={
		saveMenu,
		findAndReplaceMenu,
		goToLineMenu,
		editFullMenu,
		closeMenu,
		closeOtherMenu,
		closeAllMenu
	};
		
	public static Object[] runProjMenu={R.drawable.ep_preview,R.string.main_menu_run, (View.OnClickListener) p1 -> MainActivity.main.runProject()};
	public static Object[] importMenu={R.drawable.import_file,R.string.main_create_add, (View.OnClickListener) p1 -> MainActivity.main.epImportFile()};
	public static Object[] packZipMenu={R.drawable.zip,R.string.main_project_pack, (View.OnClickListener) p1 -> MainActivity.main.epPackZip()};
	public static Object[] manifestMenu={R.drawable.manifest,R.string.main_project_manifest, (View.OnClickListener) p1 -> MainActivity.main.epManifest()};
	public static Object[] serverMenu={R.drawable.server,R.string.main_project_service, (View.OnClickListener) p1 -> {
		try {
			new ServerMain().main();
		} catch (Exception e) 
		{
			Do.showErrDialog(MainActivity.main,e);
		}
	}};
	public static Object[] regFrameworkMenu={R.drawable.reg_framework,R.string.main_project_reg_jsres, (View.OnClickListener) p1 -> SettingDoor.regJsresProject()};
	public static Object[] packApkMenu={R.drawable.android_apk,R.string.pack_apk, (View.OnClickListener) p1 -> Vers.i.nowProjectPackMachine.show(false)};
	public static Object[] gohomeMenu={R.drawable.home,R.string.home, (View.OnClickListener) p1 -> MainActivity.main.binding.elvMainExplorerFile.toExplorer(Vers.i.ProjectDir)};
	public static Object[] refreshMenu={R.drawable.refresh,R.string.main_menu_refresh, (View.OnClickListener) p1 -> MainActivity.main.binding.elvMainExplorerFile.toExplorer(new File(Vers.i.ProjectDir+ Vers.i.ExplorerPath))};
	public static Object[] epCloseMenu={R.drawable.close,R.string.main_explorer_close, (View.OnClickListener) p1 -> MainActivity.main.CloseWeb()};
	public static Object[] mkdirMenu={R.drawable.mkdir,R.string.main_create_folder, (View.OnClickListener) p1 -> MainActivity.main.epMkdir()};
	public static Object[] websiteInfMenu={R.drawable.information,R.string.main_menu_review_msg, (View.OnClickListener) p1 -> MainActivity.main.epWebsiteInf()};
	public static Object[] previewRefreshMenu={R.drawable.refresh,R.string.main_menu_refresh, (View.OnClickListener) p1 -> MainActivity.main.PreviewHwwv.reload()};
	public static Object[] previewGoBackMenu={R.drawable.goback,R.string.main_More_preview_goback, (View.OnClickListener) p1 -> {
		if(MainActivity.main.PreviewHwwv.canGoBack())
		{
			MainActivity.main.PreviewHwwv.goBack();
		}
	}};
	public static Object[] previewGoMenu={R.drawable.goforward,R.string.main_More_preview_go, (View.OnClickListener) p1 -> {
		if(MainActivity.main.PreviewHwwv.canGoForward())
		{
			MainActivity.main.PreviewHwwv.goForward();
		}
	}};
	public static Object[] previewFullMenu={R.drawable.full_preview,R.string.full_screen_preview, (View.OnClickListener) p1 -> MainActivity.main.fullScreenPreview(),FULL_PREVIEW};
	public static Object[] previewUnFullMenu={R.drawable.unfull_preview,R.string.recover, (View.OnClickListener) p1 -> MainActivity.main.unFullScreenPreview(), HALF_PREVIEW};
	public static Object[] previewPCMode={R.drawable.pc,R.string.zoom_as_pc, (View.OnClickListener) p1 -> MainActivity.main.setPreviewerZoomMode(true),ZOOM_MODE_PC};
	public static Object[] previewMPMode={R.drawable.mp,R.string.zoom_as_mp, (View.OnClickListener) p1 -> MainActivity.main.setPreviewerZoomMode(false),ZOOM_MODE_MP};
	public static Object[] addJsMenu={R.drawable.add_js,R.string.main_menu_js_add, (View.OnClickListener) p1 -> MainActivity.main.epAddJs(),ADD_JS};
	public static Object[] clearJsMenu={R.drawable.clear_all,R.string.main_menu_clear, (View.OnClickListener) p1 -> MainActivity.main.binding.termux.reset()};
	public static Object[] stopJsMenu={R.drawable.stop,R.string.main_menu_stop, (View.OnClickListener) p1 -> MainActivity.main.stopJsRun(),STOP_JS};
	public static Object[] continueMenu={R.drawable.hopweb,R.string.continue_hw_1,new View.OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				if(!new TipsManager(MainActivity.main).isExists("continue_on_hopweb_2"))
				{
					Dl dl=new Dl(MainActivity.main);
					dl.builder.setIcon(R.drawable.hopweb);
					dl.builder.setTitle(R.string.continue_hw_1);
					dl.builder.setMessage(R.string.continue_hw_2);
					dl.builder.setCancelable(false);
					dl.builder.setPositiveButton(R.string.ok, (p11, p2) -> toContinue());
					dl.builder.setNegativeButton(R.string.cancel,null);
					dl.show();

					new TipsManager(MainActivity.main).create("continue_on_hopweb_2");
				}
				else
				{
					toContinue();
				}
			}
			
			private void toContinue()
			{
				if(!Vers.i.isInstalledHopWeb)
				{
					Do.showHopWebDialog();
				}
				else
				{
					final Runnable runnable= () -> {
						Intent intent=new Intent();
						intent.setClassName("com.venter.hopweb","com.venter.hopweb.EditorActivity");
						intent.putExtra("projectPath", Vers.i.ProjectDir.getPath());
						String openFilePath = null;
						if(Vers.i.OpenFile !=null && Vers.i.OpenFile.getPath().startsWith(Vers.i.ProjectDir.getPath()))
						{
							openFilePath = Vers.i.OpenFile.getPath();
							MainActivity.main.closeVoid(Vers.i.INowEpSID);
						}
						intent.putExtra("openFile", openFilePath);
						MainActivity.main.startActivity(intent);
						Vers.i.isUpdateProjectWhenResume=true;
					};
					
					Do.showWaitAndRunInThread(false, () -> {
						try
						{
							for(final Object IKey:MainActivity.main.binding.Ep.MSID.keySet())
							{
								if(!MainActivity.main.getEpi((Integer) IKey).BIsSave)
								{
									Do.write(((Editor) (((Object[]) (Objects.requireNonNull(MainActivity.main.binding.Ep.MSID.get(IKey))))[1])).getString(), Vers.i.OpenFile);
									MainActivity.main.runOnUiThread(() -> MainActivity.main.getEpi((Integer) IKey).save());
								}
							}

							MainActivity.main.runOnUiThread(() -> {
								Do.finishWaiting();
								runnable.run();
							});
						}
						catch(final Exception e)
						{
							MainActivity.main.runOnUiThread(() -> {
								Do.finishWaiting();
								Do.showErrDialog(MainActivity.main,e);
							});
						}
					});
				}
			}
		},CONTINUE_HW};
	
	public static Object[][] explorerMenuNoProj={
		
	};
	public static Object[][] explorerMenu={
		continueMenu,
		runProjMenu,
		importMenu,
		mkdirMenu,
		packZipMenu,
		manifestMenu,
		serverMenu,
		regFrameworkMenu,
		packApkMenu,
		gohomeMenu,
		refreshMenu,
		epCloseMenu
	};
	public static Object[][] explorerMenuPreview={
		websiteInfMenu,
		previewRefreshMenu,
		previewGoBackMenu,
		previewGoMenu,
		previewFullMenu,
		previewUnFullMenu,
		previewPCMode,
		previewMPMode
	};
	public static Object[][] explorerMenuTermux={
		addJsMenu,
		clearJsMenu,
		stopJsMenu
	};
}
