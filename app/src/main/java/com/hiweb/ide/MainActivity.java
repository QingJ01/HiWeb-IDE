package com.hiweb.ide;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.view.inputmethod.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.google.gson.*;
import com.hiweb.ide.add.*;
import com.hiweb.ide.add.addViewWidget.*;
import com.hiweb.ide.databinding.MainBinding;
import com.hiweb.ide.edit.*;
import com.hiweb.ide.server.*;
import com.hiweb.ide.server.php_server.PHPServerReceiver;
import com.venter.jssrunner.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.apache.commons.text.StringEscapeUtils;

import java.io.File;

public class MainActivity extends BaseActivity {
    JssRunner Jr;

    boolean MoreIsVisible = false;
    boolean MoreIsFull = false;
    int MoreMode = 0;
    int MoreMenu = R.menu.main_explorer_no_project;
    /*
	 0:站点管理器
	 1:网页预览
     2:输出
	 */

    public static MainActivity main;

    public int ICaHeight;

    public boolean isAutoHideExplorer = true;

    private int statusBarHeight = 0;

    public CharsAdder Ca;
    public HopWebWebView PreviewHwwv;
    JSRunWebView JsWv;

    public MainBinding binding;

    public MainActivity() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Vers.i.skipTpOpenFile != null) {
            OpenFile(Vers.i.skipTpOpenFile);
        }
        Vers.i.skipTpOpenFile = null;

        if (intent.getBooleanExtra("showServerDialog", false)) {
            try {
                new LocalServersManager().showDialog(MainActivity.this);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 检测屏幕的方向：纵向或横向
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
        //检测实体键盘的状态：推出或者合上
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //实体键盘处于推出状态，在此处添加额外的处理代码
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //实体键盘处于合上状态，在此处添加额外的处理代码
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Vers.i.isEW) {
            Do.restartApplication(this);
            return;
        }

        SignCheck signCheck = new SignCheck(MainActivity.this, "41:AC:53:CE:33:2C:C8:1F:6D:38:D2:12:51:AF:BB:24:CE:8B:EE:0D");
        if (!signCheck.check()) {
            String msg = "EasyWeb IDE \u7684\u5B89\u88C5\u5305\u4F3C\u4E4E\u53D7\u5230\u4E86\u635F\u574F\u3002\n\n\u8BF7\u68C0\u67E5\u60A8\u83B7\u53D6 EasyWeb IDE \u7684\u6765\u6E90\u3002\u82E5\u975E\u5B98\u65B9\u6E20\u9053\uFF0C\u8BF7\u901A\u8FC7\u4E0B\u65B9\u94FE\u63A5\u83B7\u53D6\u5B98\u65B9\u7248\u672C\u91CD\u65B0\u5B89\u88C5\u3002\n\u8BF7\u6CE8\u610F\uFF0C\u82E5\u5B89\u88C5\u5931\u8D25\uFF0C\u8BF7\u5378\u8F7D\u5F53\u524D\u7248\u672C\u91CD\u8BD5\u3002\n\nhttps://www.coolapk.com/apk/241241";
            Intent i = new Intent();
            i.setClass(this, ErrorActivity.class);
            i.putExtra("ErrorMsg", msg);

            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(i);
            finish();
            return;
        }

            binding = MainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        coordinatorLayout=binding.coordinator;

        Vers.i.hasRunned = true;

        Spannable text = new SpannableString(getTitle());
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.title)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(text);

        reGetCharsAdder();

        binding.btnMainMoreSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                MoreOnClick(p1);
            }
        });
        binding.btnMainMoreFull.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                MoreOnClick(p1);
            }
        });

        {
            binding.Ep.setEditorCup(binding.lyEditorCup);
        }

        {
            binding.lyPreviewAlert.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                }
            });
            binding.ibPreviewAlert.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    binding.lyPreviewAlert.setVisibility(View.GONE);
                }
            });
        }
        binding.btnMainMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                BottomOnClick(p1);
            }
        });
        binding.rlRoot.setPadding(0, statusBarHeight, 0, 0);
        binding.pastebar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                // TODO: Implement this method
            }
        });

        showUser();

        setActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                UserLoginer.showUserInf();
            }
        });

        binding.lyEditorButtons.setVisibility(View.GONE);
        ICaHeight = Ca.getSizeHeight();

        SoftKeyBoardListener.OnSoftKeyBoardChangeListener softListener = new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

            public void showInTop() {
                if (findViewById(R.id.ly_editor_findandreplace).getVisibility() == View.VISIBLE || Vers.i.isFullEdit) {
                    GoneMore();
                    binding.lyMainMenu.setVisibility(View.GONE);
                    binding.btnMainMore.setVisibility(View.GONE);
                } else {
                    binding.lyMainMenu.setVisibility(View.VISIBLE);
                    binding.btnMainMore.setVisibility(View.VISIBLE);
                }

                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) binding.lyEditorButtons.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                binding.lyEditorButtons.setLayoutParams(params);

                binding.lyMainEditor.setPadding(0, Ca.getSizeHeight(), 0, 0);

                binding.actionbar.post(new Runnable() {

                    @Override
                    public void run() {
                        if (binding.pastebar.getVisibility() == View.VISIBLE) {
                            getNowEditor()._clipboardPanel.show();
                        }
                    }
                });
            }

            @Override
            public void keyBoardShow(int height) {
                if (Vers.i.isFullPreview)
                    return;
                if (binding.lyMainEditor.getVisibility() == View.GONE)
                    return;

                if (findViewById(R.id.ly_editor_findandreplace).getVisibility() == View.VISIBLE) {
                    showInTop();
                    return;
                }
                binding.toolbar.setVisibility(View.GONE);
                if ((!MoreIsFull || (MoreIsFull && !MoreIsVisible)) && isAutoHideExplorer) {
                    GoneMore();
                    binding.lyMainMenu.setVisibility(View.GONE);
                    binding.btnMainMore.setVisibility(View.GONE);
                }

                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) binding.lyEditorButtons.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                binding.lyEditorButtons.setLayoutParams(params);

                binding.lyMainEditor.setPadding(0, 0, 0, 0);

                if (binding.lyMainEditor.getVisibility() == View.VISIBLE) {
                    binding.lyEditorButtons.setVisibility(View.VISIBLE);
                } else {
                    binding.lyEditorButtons.setVisibility(View.GONE);
                }

                binding.actionbar.post(new Runnable() {

                    @Override
                    public void run() {
                        if (binding.pastebar.getVisibility() == View.VISIBLE) {
                            getNowEditor()._clipboardPanel.show();
                        }
                    }
                });
            }

            @Override
            public void keyBoardHide(int height) {
                if (Vers.i.isFullPreview)
                    return;
                binding.toolbar.setVisibility(View.VISIBLE);
                showInTop();

                binding.actionbar.post(new Runnable() {

                    @Override
                    public void run() {
                        if (binding.pastebar.getVisibility() == View.VISIBLE) {
                            getNowEditor()._clipboardPanel.show();
                        }
                    }
                });
            }
        };

        SoftKeyBoardListener.setListener(this, softListener);

        binding.btnMainFile.setText(getString(R.string.main_File));
        Drawable fileDeawable = getResources().getDrawable(R.drawable.main_file);
        fileDeawable.setTint(ContextCompat.getColor(this, R.color.opposition));
        binding.btnMainFile.setIcon(fileDeawable);
        binding.btnMainTools.setText(getString(R.string.main_Tools));
        Drawable toolsDeawable = getResources().getDrawable(R.drawable.main_tools);
        toolsDeawable.setTint(ContextCompat.getColor(this, R.color.opposition));
        binding.btnMainTools.setIcon(toolsDeawable);
        binding.btnMainMore.setImageDrawable(getResources().getDrawable(R.drawable.main_more));

        Do.showPermissions(this);

        MoreIsFull = false;

        checkRecover();

        NoViewExplorer();

        softListener.keyBoardHide(0);

        if (Vers.i.skipTpOpenFile != null) {
            OpenFile(Vers.i.skipTpOpenFile);
        }
        Vers.i.skipTpOpenFile = null;

        main = this;

        {
            //当应用意外关闭后，检测 PHP Web Server 是否开启。
            File profitFile = new File(getFilesDir(), "php_profit");
            if (profitFile.exists()) {
                try {
                    String profitText = Do.getText(profitFile);
                    int port = Integer.parseInt(profitText.split("//////////")[0]);
                    String path = profitText.split("//////////")[1];

                    if (Vers.phpServerWebsitesMap == null)
                        Vers.phpServerWebsitesMap = new HashMap<Integer, Object[]>();
                    Vers.phpServerWebsitesMap.put(port, new Object[]{new File(path), LocalServersManager.getServerItem(1, port, new File(path))});

                    PHPServerReceiver.startServer(this, port, false);
                } catch (Exception e) {

                }
                Vers.i.isAutoStartPHPServer=true;
            }
        }

        binding.wwvWelcome.load();

        new Thread(() -> {
            EasyAppInformation.loadFunctions(getFilesDir());
        }).start();

        findUpdate(false);

        try {
            if(SettingsClass.bgFile!=null)
            {
                binding.ivEditorBackground.setVisibility(View.VISIBLE);
                binding.ivEditorBackground.setImageBitmap(Do.getLoacalBitmap(SettingsClass.bgFile));
                binding.ivEditorBackground.setAlpha(SettingsClass.bgAlpha);
                if(SettingsClass.bgScale!=null)
                    binding.ivEditorBackground.setScaleType(ImageView.ScaleType.valueOf(SettingsClass.bgScale));
            }
        }
        catch (Exception e)
        {

        }

        new Thread(() -> {
            while(true)
            {
                Vers.i.newestAnnouncementInf=AnnouncementManager.getServerInf();
                if(Vers.i.newestAnnouncementInf!=null)
                    break;
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {

                }
            }

            Vers.i.newestAnnouncementInf=AnnouncementManager.getServerInf();

            MainActivity.this.runOnUiThread(()->{
                binding.wwvWelcome.loadWhenShowWelcome();
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Vers.i.skipTpProjectFile != null) {
            OpenWeb(Vers.i.skipTpProjectFile, true);
            if(Vers.i.skipTpOpenFile!=null)
                OpenFile(Vers.i.skipTpOpenFile);
        }

        Vers.i.skipTpProjectFile = null;
        Vers.i.skipTpOpenFile=null;
    }

    public void recoverController(boolean isExit) {
        //在EasyWeb中打开一个文件时在私有目录创建一个文件，正常关闭打开的文件时删除私有目录的文件
        //下一次启动时，若文件存在则进入Recover模式
        File file = new File(getFilesDir(), "recover.sign");
        if (isExit) {
            BackupManager.stop();
            file.delete();
            new File(getFilesDir(), "recover.json").delete();
        } else {
            BackupManager.start();
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public void checkRecover() {
        File file = new File(getFilesDir(), "recover.sign");
        if (file.exists()) {
            BackupManager.recoverMode();
        }
    }

    public void showUser() {
        {
            //显示用户名
            binding.toolbar.setNavigationIcon(null);
            Spannable text = new SpannableString(getTitle());
            text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.title)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            binding.toolbar.setTitle(text);

            if (Vers.i.userName != null) {
                Drawable iconDrawable = getDrawable(R.drawable.user);
                iconDrawable.setTint(ContextCompat.getColor(this, R.color.title));
                binding.toolbar.setNavigationIcon(iconDrawable);
                if (!Vers.i.userName.equals("admin")) {
                    text = new SpannableString(Vers.i.userName);
                    text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.title)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    binding.toolbar.setSubtitle(text);
                } else {
                    binding.toolbar.setSubtitle(null);
                }
            } else {
                binding.toolbar.setNavigationIcon(null);
                binding.toolbar.setSubtitle(null);
            }

        }
    }

    public void reGetCharsAdder() {
        try {
            binding.lyEditorButtons.removeAllViews();
            Ca.destroyDrawingCache();
        } catch (Exception e) {

        }
        Ca = null;
        Ca = new CharsAdder(this);
        Ca.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        binding.lyEditorButtons.addView(Ca);

        main = this;
    }

    public void reGetHwwvPreview() {
        try {
            binding.lyPreviewBowl.removeAllViews();
            PreviewHwwv.clearData();
        } catch (Exception e) {

        }
        Vers.i.AlertList = new ArrayList();
        PreviewHwwv = null;
        PreviewHwwv = new HopWebWebView(this);
        PreviewHwwv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        binding.lyPreviewBowl.addView(PreviewHwwv);

        main = this;
    }

    public void CopyExplorerPath(final File file) {
        String[] Sa;

        String SFull = getString(R.string.main_explorer_copy_full_path);
        String SWebsite = getString(R.string.main_explorer_copy_website_path);
        String SRelatedFile = getString(R.string.main_explorer_copy_related_file_path);

        String relatedPath = "";
        try {
            relatedPath = Do.getRelativePath(Vers.i.OpenFile.getPath(), file.getPath());
        } catch (Exception e) {

        }

        if (Vers.i.OpenFile == null || relatedPath.trim().equals("")) {
            Sa = new String[]{SFull, SWebsite};
        } else {
            Sa = new String[]{SFull, SWebsite, SRelatedFile};
        }

        LinearLayout Ly = new LinearLayout(this);
        ScrollView Sv = new ScrollView(this);
        Sv.setFillViewport(true);
        RelativeLayout Rl = new RelativeLayout(this);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
        Ly = new LinearLayout(this);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
        Sv.addView(Rl);
        Rl.addView(Ly);

        Dl Ab = new Dl(MainActivity.this);
        Ab.builder.setTitle(R.string.main_explorer_copy_type);
        Ab.builder.setView(Sv);
        final AlertDialog Ad = Ab.show();

        for (int i = 0; i < Sa.length; i++) {
            final ButtonLayout Bl = new ButtonLayout(this);
            Bl.leftImg.setVisibility(View.GONE);
            Bl.titleTv.setText(Sa[i]);
            switch (i) {
                case 0:
                    Bl.descriptionTv.setText(file.getPath());
                    break;
                case 1:
                    Bl.descriptionTv.setText(file.getPath().substring(Vers.i.ProjectDir.getPath().length() + 1, file.getPath().length()));
                    break;
                case 2:
                    Bl.descriptionTv.setText(relatedPath);
                    break;
            }
            Bl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    Ad.dismiss();
                    Do.copyText(MainActivity.this, Bl.descriptionTv.getText().toString());
                    toast(R.string.main_explorer_copy_done);
                }
            });
            Ly.addView(Bl);
        }


    }

    public boolean IsNowFileWebsiteFile() {
        if (Vers.i.OpenFile != null && Vers.i.OpenFile.getPath().length() > Vers.i.ProjectDir.getPath().length() && Vers.i.OpenFile.getPath().substring(0, Vers.i.ProjectDir.getPath().length()).equals(Vers.i.ProjectDir.getPath())) {
            //↑判断当前文件是否是站点中的文件（如果是False，则当前文件是站点外的文件）
            return true;
        }
        return false;
    }

    public void showPreview(String SMsg, String SUrl) {
        Vers.i.AlertList.add(new String[]{Do.getTime("HH:mm:ss"), SMsg, SUrl});
        binding.tvPreviewAlert.setText(SMsg);
        binding.lyPreviewAlert.setVisibility(View.VISIBLE);
    }

    public void fullMore() {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) binding.lyMainMore.getLayoutParams();
        params.height = LayoutParams.MATCH_PARENT;
        binding.lyMainMore.setLayoutParams(params);
        binding.lyMainMore.setBackgroundColor(ContextCompat.getColor(this, R.color.explorer_color));
        binding.btnMainMoreFull.setImageDrawable(getResources().getDrawable(R.drawable.full_exit));
        MoreIsFull = true;
    }

    public void unFullMore() {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) binding.lyMainMore.getLayoutParams();
        params.height = Do.dp2px(this, 250);
        binding.lyMainMore.setLayoutParams(params);
        binding.lyMainMore.setBackgroundDrawable(getDrawable(R.drawable.explorer_shape));
        binding.btnMainMoreFull.setImageDrawable(getResources().getDrawable(R.drawable.full));
        MoreIsFull = false;
    }

    public void ShowIME(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void findUpdate(final boolean IsShowToast) {
        UpdateManager.check(IsShowToast, this);
    }

    public String convertToRGBA(int color) {
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return "#" + red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
    }

    String WriteFile = "";
    String CreateMode = "";

    public void createNewFile() {
        WriteFile = "";
        CreateMode = "";

        Dl dl = new Dl(MainActivity.this);
        dl.builder.setTitle(R.string.main_File_create);
        dl.builder.setItems(new String[]{getString(R.string.empty_file), getString(R.string.main_create_html), getString(R.string.main_create_css), getString(R.string.main_create_js), getString(R.string.main_create_php), getString(R.string.main_create_php_web), getString(R.string.main_create_xml), getString(R.string.main_create_jss)}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                try {
                    switch (p2) {
                        case 0:
                            WriteFile = "";
                            CreateMode = "";
                            break;
                        case 1:
                            WriteFile = "create_html.html";
                            CreateMode = ".html";
                            break;
                        case 2:
                            WriteFile = "create_css.css";
                            CreateMode = ".css";
                            break;
                        case 3:
                            WriteFile = "create_js.js";
                            CreateMode = ".js";
                            break;
                        case 4:
                            WriteFile = "create_php.php";
                            CreateMode = ".php";
                            break;
                        case 5:
                            WriteFile = "create_php_web.php";
                            CreateMode = ".php";
                            break;
                        case 6:
                            WriteFile = "create_xml.xml";
                            CreateMode = ".xml";
                            break;
                        case 7:
                            WriteFile = "create_jss.jss";
                            CreateMode = ".jss";
                            break;
                    }
                    final EditText Edittext = new EditText(MainActivity.this);
                    Edittext.setText(getString(R.string.main_new_file_json_name) + CreateMode);

                    Dl dl = new Dl(MainActivity.this);
                    dl.builder.setTitle(getString(R.string.main_new_file_name));
                    dl.builder.setCancelable(false);
                    dl.builder.setView(Edittext);
                    dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            final String FileName = Edittext.getText().toString();
                            FileChoose = new FileChooseClass();
                            FileChoose.Type(false);
                            if (Vers.i.IsOpenProject) {
                                FileChoose.setOpenPath(Vers.i.ProjectDir);
                            } else {
                                FileChoose.setOpenPath(Environment.getExternalStorageDirectory());
                            }
                            FileChoose.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View p1) {
                                    try {
                                        File NewFile = new File(FileChoose.nowPath.getPath(), FileName);
                                        if (!NewFile.exists()) {
                                            FileChoose.dialog.dismiss();
                                            if (WriteFile.equals("")) {
                                                NewFile.createNewFile();
                                            } else {
                                                AssetsClass.copyBigDataToSD(MainActivity.this, WriteFile, NewFile.getPath());
                                            }
                                            if (Vers.i.IsOpenProject && (Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath).equals(NewFile.getParent())) {
                                                binding.elvMainExplorerFile.toExplorer(NewFile.getParentFile());
                                            }
                                            OpenFile(NewFile);
                                        } else {
                                            toast(R.string.main_new_file_exists);
                                        }

                                    } catch (Exception e) {
                                        toast(R.string.main_new_file_cannot_create);
                                    }
                                }
                            });
                            FileChoose.Show(MainActivity.this);
                        }
                    });

                    dl.builder.setNegativeButton(R.string.cancel, null);
                    dl.show();
                } catch (Exception e) {
                    toast(R.string.main_new_file_cannot_create);
                }
            }
        });
        dl.show();
    }


    public void BottomOnClick(View v) {
        PopupMenu popupMenu;
        switch (v.getId()) {
            case R.id.btn_main_File:

                popupMenu = new PopupMenu(this, binding.btnMainFile);
                // menu布局
                popupMenu.getMenuInflater().inflate(R.menu.main_file_menu, popupMenu.getMenu());
                // menu的item点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    FileChooseClass FileChoose;

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_main_File_create:
                                createNewFile();
                                break;
                            case R.id.menu_main_File_open:
                                FileChoose = new FileChooseClass();
                                FileChoose.canOpenWebsite();
                                FileChoose.Type(true);
                                FileChoose.setOpenPath(Environment.getExternalStorageDirectory());
                                FileChoose.setOnFileClickListener(new OnFileClickListener() {
                                    @Override
                                    public void onClick(final File ChooseFile, final AlertDialog dialog) {

                                        if (ChooseFile.getName().lastIndexOf(".") != -1) {
                                            String fileType = ChooseFile.getName().substring(ChooseFile.getName().lastIndexOf(".") + 1).toLowerCase();
                                            try {
                                                boolean isTextFile = Do.isTextFile(ChooseFile);
                                                if (isTextFile) {
                                                    if (Vers.i.IsOpeningFile && Vers.i.OpenFile.getPath().equals(ChooseFile.getPath())) {
                                                        toast(R.string.main_open_opened);
                                                    } else {
                                                        File OpenFile = ChooseFile;
                                                        dialog.dismiss();
                                                        OpenFile(OpenFile);
                                                    }

                                                } else {
                                                    Dl dl = new Dl(MainActivity.this);
                                                    dl.builder.setTitle(R.string.open_binary_file);
                                                    dl.builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface p1, int p2) {
                                                            if (Vers.i.IsOpeningFile && Vers.i.OpenFile.getPath().equals(ChooseFile.getPath())) {
                                                                toast(R.string.main_open_opened);
                                                            } else {
                                                                File OpenFile = ChooseFile;
                                                                dialog.dismiss();
                                                                OpenFile(OpenFile);
                                                            }
                                                        }
                                                    });
                                                    dl.show();
                                                }
                                            } catch (Exception e) {
                                                Do.showErrDialog(MainActivity.this, e);
                                            }

                                        } else {
                                            if (Vers.i.IsOpeningFile && Vers.i.OpenFile.getPath().equals(ChooseFile.getPath())) {
                                                toast(R.string.main_open_opened);
                                            } else {
                                                File OpenFile = ChooseFile;
                                                dialog.dismiss();
                                                OpenFile(OpenFile);
                                            }
                                        }
                                    }


                                });
                                FileChoose.Show(MainActivity.this, MainActivity.this.getString(R.string.main_File_open));

                                break;
                            case R.id.menu_main_File_copy:
                                Dl WebInputAb = new Dl(MainActivity.this);
                                WebInputAb.builder.setTitle(R.string.main_File_copy);
                                final TextLayout TlCopy = new TextLayout(MainActivity.this);
                                TlCopy.build(0, null, R.string.main_File_copy_msg, R.string.main_File_copy_example);
                                TlCopy.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
                                WebInputAb.builder.setView(TlCopy);
                                WebInputAb.builder.setPositiveButton(R.string.ok, null);
                                final AlertDialog WebInputAd = WebInputAb.create();
                                WebInputAd.show();

                                WebInputAd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View p1) {
                                        try {
                                            {
                                                WebInputAd.dismiss();

                                                final EditText Acet = new EditText(MainActivity.this);
                                                Acet.setText(TlCopy.getText("$").split("/")[TlCopy.getText("$").split("/").length - 1].trim());
                                                final Dl AdbName = new Dl(MainActivity.this);
                                                AdbName.builder.setView(Acet);
                                                AdbName.builder.setTitle(R.string.edit_save_name);
                                                AdbName.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface p1, int p2) {
                                                        try {
                                                            if (Acet.getText().toString().trim().equals("")) {
                                                                throw new Exception();
                                                            }
                                                            final FileChooseClass FileChoose = new FileChooseClass();
                                                            FileChoose.Type(false);
                                                            if (Vers.i.IsOpenProject) {
                                                                FileChoose.setOpenPath(Vers.i.ProjectDir);
                                                            } else {
                                                                FileChoose.setOpenPath(Environment.getExternalStorageDirectory());
                                                            }
                                                            FileChoose.setOnClickListener(new View.OnClickListener() {

                                                                @Override
                                                                public void onClick(View p1) {
                                                                    Do.showWaitAndRunInThread(false, new Runnable() {

                                                                        @Override
                                                                        public void run() {
                                                                            try {
                                                                                final File NewFile = new File(FileChoose.nowPath.getPath(), Acet.getText().toString().trim());
                                                                                if (!NewFile.exists()) {
                                                                                    FileChoose.dialog.dismiss();
                                                                                    if (!NewFile.createNewFile())
                                                                                        throw new IOException();

                                                                                    try {
                                                                                        Do.downloadNet(TlCopy.getText("$"), NewFile.getPath());
                                                                                    } catch (final Exception e) {

                                                                                        MainActivity.this.runOnUiThread(new Runnable() {

                                                                                            @Override
                                                                                            public void run() {
                                                                                                Do.finishWaiting();
                                                                                                Do.showErrDialog(MainActivity.this, e);
                                                                                            }
                                                                                        });
                                                                                        return;
                                                                                    }

                                                                                    MainActivity.this.runOnUiThread(new Runnable() {

                                                                                        @Override
                                                                                        public void run() {
                                                                                            Do.finishWaiting();
                                                                                            if (Vers.i.IsOpenProject && (Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath).equals(NewFile.getParent())) {
                                                                                                binding.elvMainExplorerFile.toExplorer(NewFile.getParentFile());
                                                                                            }
                                                                                            toast(R.string.done);
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    MainActivity.this.runOnUiThread(new Runnable() {

                                                                                        @Override
                                                                                        public void run() {
                                                                                            Do.finishWaiting();
                                                                                            toast(R.string.main_new_file_exists);
                                                                                        }
                                                                                    });
                                                                                }

                                                                            } catch (IOException e) {
                                                                                MainActivity.this.runOnUiThread(new Runnable() {

                                                                                    @Override
                                                                                    public void run() {
                                                                                        Do.finishWaiting();
                                                                                        toast( R.string.main_new_file_cannot_create);
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                            MainActivity.this.runOnUiThread(new Runnable() {

                                                                @Override
                                                                public void run() {
                                                                    FileChoose.Show(MainActivity.this);
                                                                }
                                                            });
                                                        } catch (Exception e) {
                                                            MainActivity.this.runOnUiThread(new Runnable() {

                                                                @Override
                                                                public void run() {
                                                                    toast( R.string.main_new_file_cannot_create);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                MainActivity.this.runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        AdbName.show();
                                                    }
                                                });
                                            }
                                        } catch (Exception e) {
                                            MainActivity.this.runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    Do.finishWaiting();
                                                    toast(R.string.main_File_copy_cantconnect);
                                                }
                                            });
                                        }
                                    }
                                });
                                break;
                            case R.id.menu_main_File_example:
                                ExamplesClass.showMainDialog();
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();
                break;
            case R.id.btn_main_Tools:
                popupMenu = new PopupMenu(this, binding.btnMainTools);
                // menu布局
                popupMenu.getMenuInflater().inflate(R.menu.main_tools_menu, popupMenu.getMenu());
                // menu的item点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_main_Tools_base64:
                                new Base64Class(MainActivity.this);
                                break;
                            case R.id.menu_main_Tools_encoder:
                                Encoder.show(MainActivity.this);
                                break;
                            case R.id.menu_main_Tools_easyapp:
                                EasyAppInformation.showInfDialog();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            case R.id.btn_main_More:
                ToMore();
                break;
        }
    }

    public void stopJsRun() {
        binding.termux.reset();
        try {
            JsWv.stopLoading();
            Jr.stopLoading();
            JsWv.destroy();
            Jr.destroy();
        } catch (Exception e) {
        }
        JsWv = null;
        Jr = null;

        binding.editorMenuBar.hide(Menus.PAUSE_JS);
        binding.exploreMenuBar.hide(Menus.ADD_JS);
        binding.exploreMenuBar.hide(Menus.STOP_JS);
    }

    public void CloseWeb() {
        Vers.i.IsOpenProject = false;
        Vers.i.ExplorerPath = "";
        Vers.i.ProjectName = "";
        Vers.i.ProjectDir = null;
        Vers.i.nowHWPPath = null;
        Vers.i.nowWebsitePort = -1;
        Vers.i.nowProjectServerType = -1;
        Vers.i.nowProjectPackMachine=null;

        NoViewExplorer();
    }

    public void OpenWeb(File openFileDir, boolean isShowInf) {
        try {
            CloseWeb();
        } catch (Exception e) {

        }
        try {
            Vers.i.ProjectDir = openFileDir;
            Vers.i.ExplorerPath = "";
            Vers.i.nowProjectServerType = -1;
            Vers.i.nowWebsitePort = -1;

            JsonParser parser = new JsonParser();
            JsonObject object = null;
            File manifestFile = new File(openFileDir.getPath() + "/manifest.json");
            try {
                object = (JsonObject) parser.parse(new FileReader(manifestFile));
            } catch (Exception e) {

            }

            if (object != null) {
                if (object.has("name"))
                    Vers.i.ProjectName = object.get("name").getAsString();
                else
                    Vers.i.ProjectName = null;
                if (object.has("author"))
                    Vers.i.ProjectAuthor = object.get("author").getAsString();
                else
                    Vers.i.ProjectAuthor = null;
                if (object.has("image")) {
                    Vers.i.SProjectImg = object.get("image").getAsString();
                    Vers.i.ProjectImg = new File(openFileDir.getPath() + object.get("image").getAsString());
                } else {
                    Vers.i.SProjectImg = null;
                    Vers.i.ProjectImg = null;
                }
                if (object.has("javascript")) {
                    Vers.i.SProjectJs = object.get("javascript").getAsString();
                    Vers.i.ProjectJs = new File(openFileDir.getPath() + object.get("javascript").getAsString());
                } else {
                    Vers.i.SProjectJs = null;
                    Vers.i.ProjectJs = null;
                }
                if (object.has("download")) {
                    Vers.i.SProjectDownload = object.get("download").getAsString();
                    Vers.i.ProjectDownload = new File(openFileDir.getPath() + object.get("download").getAsString());
                } else {
                    Vers.i.SProjectDownload = null;
                    Vers.i.ProjectDownload = null;
                }
                if (object.has("video")) {
                    Vers.i.SProjectVideo = object.get("video").getAsString();
                    Vers.i.ProjectVideo = new File(openFileDir.getPath() + object.get("video").getAsString());
                } else {
                    Vers.i.SProjectVideo = null;
                    Vers.i.ProjectVideo = null;
                }

                Vers.i.nowHWPPath = new File(openFileDir.getPath() + "/manifest.json");
            } else {
                Vers.i.ProjectName = null;
                Vers.i.ProjectAuthor = null;
                Vers.i.ProjectImg = null;
                Vers.i.ProjectJs = null;
                Vers.i.ProjectDownload = null;
                Vers.i.SProjectVideo = null;
                Vers.i.SProjectImg = null;
                Vers.i.SProjectJs = null;
                Vers.i.SProjectDownload = null;
                Vers.i.SProjectVideo = null;
                Vers.i.nowHWPPath = null;

                manifestFile.renameTo(new File(manifestFile.getParentFile(), "manifest.json.bak"));
                toast(R.string.read_manifest_cant);
            }
            Vers.i.nowProjectPackMachine=new PackMachine();
            Vers.i.IsOpenProject = true;
            ViewExplorer();

            boolean isServer = Do.isServerContainWebsite(Vers.i.ProjectDir);
            if (isServer) {
                ServerMain.readManifest();
            }
            if (isShowInf)
                showInf();
            {
                String name;
                if (Vers.i.ProjectName != null)
                    name = Vers.i.ProjectName;
                else
                    name = Vers.i.ProjectDir.getName();
                Do.saveHistory(Vers.i.ProjectDir, name);

            }

            File makeAPKEasyweb = new File(Vers.i.ProjectDir,"MakeAPK.easyweb");
            if(makeAPKEasyweb.exists()){
                Vers.i.nowProjectPackMachine.runMakeAPK(makeAPKEasyweb,true);
            }

        } catch (Exception e) {
            Do.showErrDialog(MainActivity.this, e);
        }
    }

    public void showInf() throws IOException {
        File F = new File(Vers.i.ProjectDir.getPath() + "/$INF$.easyweb");
        if (F.exists()) {
            Dl Adb = new Dl(this);
            Adb.builder.setTitle(R.string.website_inf);
            Adb.builder.setMessage(Do.getText(F));
            Adb.builder.setPositiveButton(R.string.ok, null);
            Adb.show();
        }
    }

    private void OpenVoid(final File file) {
        Do.showWaitAndRunInThread(false, this, new Runnable() {

            @Override
            public void run() {
                try {
                    final String text = Do.getText(file);
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            prepareOpenFile(file, text, true);
                        }
                    });
                } catch (IOException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Do.finishWaiting();
                            toast( R.string.main_open_error);
                        }
                    });
                }
            }
        });
    }

    public void prepareOpenFile(File file, String text, boolean isSetEvn) {
        Do.finishWaiting();
        binding.lyMainEditor.setVisibility(View.VISIBLE);
        binding.Ep.setVisibility(View.VISIBLE);
        Vers.i.IsOpeningFile = true;

        if (Vers.i.IsOpenProject) {
            ViewExplorer();
        } else {
            NoViewExplorer();
        }
        GoneMore();
        binding.Ep.add(file, text, isSetEvn);
        Vers.i.IsOpeningFile = false;
        binding.wwvWelcome.hide();

        binding.lyEditorButtons.setVisibility(View.VISIBLE);

        showUser();
    }


    public Editor getNowEditor() {
        return ((Editor) (((Object[]) (binding.Ep.MSID.get(Vers.i.INowEpSID)))[1]));
    }

    public Editor getHwewv(int sid) {
        return ((Editor) (((Object[]) (binding.Ep.MSID.get(sid)))[1]));
    }

    public EditorPage.EPItem getNowEpi() {
        return ((EditorPage.EPItem) (((Object[]) (binding.Ep.MSID.get(Vers.i.INowEpSID)))[0]));
    }

    public EditorPage.EPItem getEpi(int sid) {
        return ((EditorPage.EPItem) (((Object[]) (binding.Ep.MSID.get(sid)))[0]));
    }

    public void OpenFile(final File file) {
        if (!binding.Ep.MSID.isEmpty()) {
            int IOpenSID = Do.getMFileKey((HashMap<Integer, File>) binding.Ep.MAllFiles, file);
            if (IOpenSID != -1 && binding.Ep.MSID.containsKey(IOpenSID)) {
                binding.Ep.open(IOpenSID);
            } else {
                OpenVoid(file);
            }
        } else {
            OpenVoid(file);
        }
    }

    public void ViewOutput() {
        binding.lyMainMoreExplorerNoProject.setVisibility(View.GONE);
        binding.lyMainMoreExplorer.setVisibility(View.GONE);
        binding.lyMainMorePreview.setVisibility(View.GONE);
        binding.termux.setVisibility(View.VISIBLE);
        binding.tvMainMoreTitle.setText(R.string.main_output);
        binding.tvMainMoreSubtitle.setText("");
        VisibleMore();
        MoreMenu = R.menu.main_output_menu;
        MoreMode = 2;

        checkExplorerMenuBar();
    }

    String url;

    public void runProject() {
        if (!Vers.i.IsOpenProject)
            return;

        Do.showWaitAndRunInThread(false, new Runnable() {

            class AError extends Exception {
            }

            class BError extends Exception {
            }

            @Override
            public void run() {
                try {
                    switch (Vers.i.nowProjectServerType) {
                        case -1:
                            throw new BError();
                        case 0:
                        case 1:
                            if (Do.isServerContainWebsite(Vers.i.ProjectDir)) {
                                if (Vers.i.nowProjectServerType == 0 && !Vers.i.isServerOn) {
                                    MainActivity.this.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Do.finishWaiting();
                                            new LocalServersManager().showDialog(MainActivity.this);
                                            toast(R.string.run_but_on);
                                        }
                                    });
                                    return;
                                }
                                try {
                                    int portNow = Vers.i.nowWebsitePort;
                                    String ip = Do.getIpAddressString();
                                    url = "http://" + ip + ":" + portNow;
                                    throw new AError();
                                } catch (final SocketException e) {
                                    MainActivity.this.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Do.showErrDialog(MainActivity.this, e);
                                        }
                                    });
                                }
                            } else {
                                throw new BError();
                            }
                            break;
                    }
                    throw new BError();

                } catch (BError e) {
                    //未部署

                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Do.finishWaiting();
                            Dl Adb = new Dl(MainActivity.this);
                            Adb.builder.setTitle(R.string.inf);
                            Adb.builder.setMessage(R.string.run_but_copy);
                            Adb.builder.setPositiveButton(R.string.main_project_service, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    try {
                                        new ServerMain().main();
                                    } catch (Exception e) {
                                        Do.showErrDialog(MainActivity.this, e);
                                    }
                                }
                            });
                            Adb.show();
                        }
                    });
                    return;
                } catch (AError e) {
                    //已部署

                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Do.finishWaiting();
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            MainActivity.this.startActivity(intent);
                        }
                    });

                }
            }
        });
    }

    JsonObject JoAll;
    int vIServerType = -1;

    public void epImportFile() {
        boolean isNeedTip = false;
        if (!new TipsManager(MainActivity.this).isExists("ep_add")) {
            isNeedTip = true;
            new TipsManager(MainActivity.this).create("ep_add");
        }

        FileChoose = new FileChooseClass();
        FileChoose.Type(true);
        FileChoose.setOpenPath(Environment.getExternalStorageDirectory());
        FileChoose.setOnFileClickListener(new OnFileClickListener() {
            String SToPath = "";

            @Override
            public void onClick(final File ChooseFile, AlertDialog dialog) {
                dialog.dismiss();
                String SAfter = "";
                Dl Ab = new Dl(MainActivity.this);
                Ab.builder.setTitle(ChooseFile.getName());
                Ab.builder.setPositiveButton(R.string.add_file_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        boolean IsMkDone = false;
                        if (!new File(Vers.i.ProjectDir + SToPath).exists()) {
                            if (!new File(Vers.i.ProjectDir + SToPath).mkdirs())
                                Do.showErrDialog(MainActivity.this, new IOException("Create files failed."));
                            else
                                IsMkDone = true;
                        } else {
                            IsMkDone = true;
                        }
                        if (IsMkDone)
                            StartCopy(ChooseFile);
                    }
                });
                Ab.builder.setNegativeButton(R.string.add_file_no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        SToPath = Vers.i.ExplorerPath;
                        StartCopy(ChooseFile);
                    }
                });
                SToPath = Vers.i.ExplorerPath;

                if (ChooseFile.getName().lastIndexOf(".") != -1) {
                    SAfter = ChooseFile.getName().substring(ChooseFile.getName().lastIndexOf(".") + 1).toLowerCase();
                }
                if (FileTypeClass.FileTypeMap().containsKey(SAfter.trim().toLowerCase())) {
                    int I = (int) FileTypeClass.FileTypeMap().get(SAfter.trim().toLowerCase());
                    if (Vers.i.ProjectImg != null && I == FileTypeClass.TYPE_IMG && !Vers.i.ExplorerPath.equals(Vers.i.SProjectImg)) {
                        Ab.builder.setMessage(R.string.add_file_img);
                        SToPath = Vers.i.SProjectImg;
                        Ab.show();
                    } else if (Vers.i.ProjectJs != null && I == FileTypeClass.TYPE_JS && !Vers.i.ExplorerPath.equals(Vers.i.SProjectJs)) {
                        Ab.builder.setMessage(R.string.add_file_js);
                        SToPath = Vers.i.SProjectJs;
                        Ab.show();
                    } else if (Vers.i.ProjectDownload != null && I == FileTypeClass.TYPE_DOWN && !Vers.i.ExplorerPath.equals(Vers.i.SProjectDownload)) {
                        Ab.builder.setMessage(R.string.add_file_down);
                        SToPath = Vers.i.SProjectDownload;
                        Ab.show();
                    } else if (Vers.i.ProjectVideo != null && I == FileTypeClass.TYPE_VIDEO && !Vers.i.ExplorerPath.equals(Vers.i.SProjectVideo)) {
                        Ab.builder.setMessage(R.string.add_file_video);
                        SToPath = Vers.i.SProjectVideo;
                        Ab.show();
                    } else if (I == FileTypeClass.TYPE_WEB) {
                        SToPath = Vers.i.ExplorerPath;
                        StartCopy(ChooseFile);
                    } else if (Vers.i.ProjectDownload != null && !FileTypeClass.FileTypeMap().containsKey(SAfter.trim().toLowerCase()) && !Vers.i.ExplorerPath.equals(Vers.i.SProjectDownload)) {
                        Ab.builder.setMessage(R.string.add_file_down);
                        SToPath = Vers.i.SProjectDownload;
                        Ab.show();
                    } else {
                        SToPath = Vers.i.ExplorerPath;
                        StartCopy(ChooseFile);
                    }
                } else if (Vers.i.ProjectDownload != null && !Vers.i.ExplorerPath.equals(Vers.i.SProjectDownload)) {
                    Ab.builder.setMessage(R.string.add_file_down);
                    SToPath = Vers.i.SProjectDownload;
                    Ab.show();
                } else {
                    SToPath = Vers.i.ExplorerPath;
                    StartCopy(ChooseFile);
                }
            }

            private void StartCopy(final File ChooseFile) {
                Do.showWaitAndRunInThread(false, new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Do.copySdcardFile(ChooseFile.getPath(), new File(Vers.i.ProjectDir + SToPath + "/" + ChooseFile.getName()).getPath());
                            MainActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    binding.elvMainExplorerFile.toExplorer(new File(Vers.i.ProjectDir + SToPath));
                                    Do.finishWaiting();
                                    toast( R.string.done);
                                }
                            });

                        } catch (Exception e) {
                            Do.finishWaiting();
                            Do.showErrDialog(MainActivity.this, e);
                        }
                    }
                });
            }
        });
        if (isNeedTip) {
            Dl Adb = new Dl(MainActivity.this);
            Adb.builder.setTitle(R.string.tip_ep_add_tt);
            Adb.builder.setMessage(R.string.tip_ep_add_msg);
            Adb.builder.setCancelable(false);
            Adb.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    FileChoose.Show(MainActivity.this);
                }
            });
            Adb.show();
        } else {
            FileChoose.Show(MainActivity.this);
        }
    }

    public void epMkdir() {
        try {
            final EditText Edittext = new EditText(MainActivity.this);
            Edittext.setText(getString(R.string.main_create_folder));

            Dl dl = new Dl(MainActivity.this);
            dl.builder.setTitle(getString(R.string.main_new_file_name));
            dl.builder.setCancelable(false);
            dl.builder.setView(Edittext);
            dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    final String FolderName = Edittext.getText().toString();
                    try {
                        File FolderPathFile = new File(Vers.i.ProjectDir + Vers.i.ExplorerPath + "/" + FolderName);
                        if (!FolderPathFile.exists()) {
                            if (!FolderPathFile.mkdirs()) {
                                throw new Exception();
                            }
                            binding.elvMainExplorerFile.toExplorer(FolderPathFile.getParentFile());
                            toast(R.string.done);
                        } else {
                            toast(R.string.main_new_file_exists);
                        }
                    } catch (Exception e) {
                        toast(R.string.main_new_file_cannot_create);
                    }
                }
            });
            dl.builder.setNegativeButton(R.string.cancel, null);
            dl.show();
        } catch (Exception e) {
            toast(R.string.main_new_file_cannot_create);
        }
    }

    public void epPackZip() {
        FileChoose = new FileChooseClass();
        FileChoose.Type(false);
        FileChoose.setOpenPath(Environment.getExternalStorageDirectory());
        FileChoose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                final File toFile = new File(FileChoose.nowPath, (Vers.i.ProjectName == null ? Vers.i.ProjectDir.getName() : Vers.i.ProjectName) + ".zip");
                if (FileChoose.nowPath.getPath().startsWith(Vers.i.ProjectDir.getPath())) {
                    toast(R.string.copy_much_err);
                } else if (toFile.exists()) {
                    toast(R.string.main_new_file_exists);
                } else {
                    FileChoose.dialog.dismiss();
                    Do.showWaitAndRunInThread(false, new Runnable() {

                        @Override
                        public void run() {
                            try {
                                FileOutputStream fos1 = new FileOutputStream(toFile);
                                ZipUtil.toZip(Vers.i.ProjectDir.getPath(), fos1, true);
                                MainActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                    toast(R.string.done);
                                    }
                                });
                            } catch (final Exception e) {
                                MainActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Do.showErrDialog(MainActivity.this, e);
                                    }
                                });

                            } finally {
                                Do.finishWaiting();

                            }
                        }
                    });
                }
            }
        });
        FileChoose.Show(MainActivity.this);
    }

    public void epManifest() {
        try {
            ScrollView Sv = new ScrollView(MainActivity.this);
            Sv.setFillViewport(true);
            final LinearLayout LyManifest = new LinearLayout(MainActivity.this);
            LyManifest.setOrientation(LinearLayout.VERTICAL);
            LyManifest.setGravity(Gravity.CENTER);
            LyManifest.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
            LyManifest.setLayoutParams(new ScrollView.LayoutParams(-1, -1));
            Sv.addView(LyManifest);
            {
                TextLayout TlName = new TextLayout(MainActivity.this);
                TlName.build(0, null, R.string.manifest_name, null);
                if (Vers.i.ProjectName != null)
                    TlName.Acet.setText(Vers.i.ProjectName);
                LyManifest.addView(TlName);

                TextLayout TlAuthor = new TextLayout(MainActivity.this);
                TlAuthor.build(1, null, R.string.manifest_author, null);
                if (Vers.i.ProjectAuthor != null)
                    TlAuthor.Acet.setText(Vers.i.ProjectAuthor);
                LyManifest.addView(TlAuthor);

                TextLayout TlImg = new TextLayout(MainActivity.this);
                TlImg.build(2, null, R.string.manifest_img, R.string.manifest_hint_img);
                if (Vers.i.ProjectImg != null)
                    TlImg.Acet.setText(Vers.i.SProjectImg);
                LyManifest.addView(TlImg);

                TextLayout TlJs = new TextLayout(MainActivity.this);
                TlJs.build(3, null, R.string.manifest_js, R.string.manifest_hint_js);
                if (Vers.i.ProjectJs != null)
                    TlJs.Acet.setText(Vers.i.SProjectJs);
                LyManifest.addView(TlJs);

                TextLayout TlDownload = new TextLayout(MainActivity.this);
                TlDownload.build(4, null, R.string.manifest_download, R.string.manifest_hint_download);
                if (Vers.i.ProjectDownload != null)
                    TlDownload.Acet.setText(Vers.i.SProjectDownload);
                LyManifest.addView(TlDownload);

                TextLayout TlVideo = new TextLayout(MainActivity.this);
                TlVideo.build(5, null, R.string.manifest_video, R.string.manifest_hint_video);
                if (Vers.i.ProjectVideo != null)
                    TlVideo.Acet.setText(Vers.i.SProjectVideo);
                LyManifest.addView(TlVideo);

                if (new File(Vers.i.ProjectDir, "manifest.json").exists())
                    JoAll = (JsonObject) JsonParser.parseString(Do.getText(new File(Vers.i.ProjectDir, "manifest.json")));
                else {
                    JoAll = new JsonObject();
                }

                vIServerType = -1;
                if (JoAll.has("server") && ((JsonObject) JoAll.get("server")).has("server_type")) {
                    vIServerType = ((JsonObject) JoAll.get("server")).get("server_type").getAsInt();
                }
                ObjectLayout vOlServer = new ObjectLayout(MainActivity.this);
                LinearLayout vLyServer = new LinearLayout(MainActivity.this);
                vLyServer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                vLyServer.setGravity(Gravity.CENTER | Gravity.LEFT);
                vLyServer.setOrientation(LinearLayout.VERTICAL);
                vLyServer.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
                final TextView vActvServer = new TextView(MainActivity.this);
                final Button vAcbtnClearServer = new Button(MainActivity.this, null, R.attr.buttonBarButtonStyle);
                vAcbtnClearServer.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                vAcbtnClearServer.setTextColor(Color.parseColor("#1593E5"));
                vAcbtnClearServer.setText(R.string.manifest_clear_server);
                vAcbtnClearServer.setGravity(Gravity.CENTER);
                vAcbtnClearServer.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        try {
                            switch (vIServerType) {
                                case 0:
                                case 1:
                                    if (Do.isServerContainWebsite(Vers.i.ProjectDir)) {
                                        toast(R.string.manifest_clear_server_err);
                                        return;
                                    }
                                    break;
                            }

                            JoAll.remove("server");
                            Do.write(JoAll.toString(), new File(Vers.i.ProjectDir, "manifest.json"));
                            Vers.i.nowProjectServerType = -1;
                            Vers.i.nowWebsitePort = -1;
                            vActvServer.setText(R.string.none);
                            vAcbtnClearServer.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Do.showErrDialog(MainActivity.this, e);
                        }
                    }
                });

                vLyServer.addView(vActvServer);
                vLyServer.addView(vAcbtnClearServer);
                switch (vIServerType) {
                    case 0:
                        vActvServer.setText(R.string.server_type_wifi);
                        break;
                    case 1:
                        vActvServer.setText(R.string.server_type_php);
                        break;
                    case 2:
                        vActvServer.setText(R.string.server_type_other);
                        break;
                    default:
                        vActvServer.setText(R.string.none);
                        vAcbtnClearServer.setVisibility(View.GONE);
                        break;
                }
                vOlServer.build(-1, null, R.string.manifest_now_server, vLyServer);
                LyManifest.addView(vOlServer);

                Dl Ab = new Dl(MainActivity.this);
                Ab.builder.setTitle(R.string.main_project_manifest);
                Ab.builder.setView(Sv);
                Ab.builder.setPositiveButton(R.string.ok, null);

                final AlertDialog Ad = Ab.show();

                Ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        try {
                            String[] SaVars = new String[6];
                            for (int i = 0; i < SaVars.length; i++) {
                                SaVars[i] = ((TextLayout) LyManifest.findViewById(i)).getText("$");
                                if (i > 2 && SaVars[i] != null && !SaVars[i].substring(0, 1).equals("/")) {
                                    toast(R.string.manifest_path_err);
                                    return;
                                }
                            }
                            Ad.dismiss();
                            if (Vers.i.nowHWPPath == null || !Vers.i.nowHWPPath.exists()) {
                                Vers.i.nowHWPPath = new File(Vers.i.ProjectDir.getPath() + "/manifest.json");
                                Vers.i.nowHWPPath.createNewFile();
                            }
                            JsonParser Jp = new JsonParser();
                            String SJson;
                            SJson = Do.getText(Vers.i.nowHWPPath);
                            JsonObject object;
                            try {
                                object = (JsonObject) Jp.parse(SJson);
                            } catch (Exception e) {
                                object = new JsonObject();
                            }

                            if (SaVars[0] != null) {
                                Vers.i.ProjectName = SaVars[0];
                                object.addProperty("name", SaVars[0]);
                            } else {
                                Vers.i.ProjectName = null;
                                if (object.has("name")) object.remove("name");
                            }

                            if (SaVars[1] != null) {
                                Vers.i.ProjectAuthor = SaVars[1];
                                object.addProperty("author", SaVars[1]);
                            } else {
                                Vers.i.ProjectAuthor = null;
                                if (object.has("auther")) object.remove("auther");
                            }
                            if (SaVars[2] != null) {
                                Vers.i.ProjectImg = new File(Vers.i.ProjectDir.getPath() + SaVars[2]);
                                Vers.i.SProjectImg = SaVars[2];
                                object.addProperty("image", SaVars[2]);
                            } else {
                                Vers.i.ProjectImg = null;
                                if (object.has("image")) object.remove("image");
                            }
                            if (SaVars[3] != null) {
                                Vers.i.ProjectJs = new File(Vers.i.ProjectDir.getPath() + SaVars[3]);
                                Vers.i.SProjectJs = SaVars[3];
                                object.addProperty("javascript", SaVars[3]);
                            } else {
                                Vers.i.ProjectJs = null;
                                if (object.has("javascript")) object.remove("javascript");
                            }
                            if (SaVars[4] != null) {
                                Vers.i.ProjectDownload = new File(Vers.i.ProjectDir.getPath() + SaVars[4]);
                                Vers.i.SProjectDownload = SaVars[4];
                                object.addProperty("download", SaVars[4]);
                            } else {
                                Vers.i.ProjectDownload = null;
                                if (object.has("download")) object.remove("download");
                            }
                            if (SaVars[5] != null) {
                                Vers.i.ProjectVideo = new File(Vers.i.ProjectDir.getPath() + SaVars[5]);
                                Vers.i.SProjectVideo = SaVars[5];
                                object.addProperty("video", SaVars[5]);
                            } else {
                                Vers.i.ProjectVideo = null;
                                if (object.has("video")) object.remove("video");
                            }

                            Do.write(object.toString(), Vers.i.nowHWPPath);

                            OpenWeb(Vers.i.ProjectDir, false);
                        } catch (Exception e) {
                            Do.showErrDialog(MainActivity.this, e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Do.showErrDialog(MainActivity.this, e);
        }
    }

    public void epWebsiteInf() {
        {
            LinearLayout Ly;

            ScrollView Sv = new ScrollView(MainActivity.this);
            Sv.setFillViewport(true);
            RelativeLayout Rl = new RelativeLayout(MainActivity.this);
            Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
            Ly = new LinearLayout(MainActivity.this);
            Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
            Ly.setOrientation(LinearLayout.VERTICAL);
            Ly.setPadding(Do.dp2px(this, 10), Do.dp2px(this, 10), Do.dp2px(this, 10), Do.dp2px(this, 10));
            Sv.addView(Rl);
            Rl.addView(Ly);

            TextView ActvTitle = new TextView(MainActivity.this);
            if (binding.lyPreviewErrorBowl.getChildCount() > 0) {
                ActvTitle.setText(R.string.cant_open_page);
            } else {
                if (PreviewHwwv.STitle != null) {
                    ActvTitle.setText(PreviewHwwv.STitle);
                    Do.CanSelect(ActvTitle);
                } else {
                    ActvTitle.setText(R.string.no_title);
                }
            }

            ObjectLayout OlTitle = new ObjectLayout(MainActivity.this);
            OlTitle.build(0, null, R.string.website_title, ActvTitle);
            Ly.addView(OlTitle);

            ImageView Aciv = new ImageView(MainActivity.this);
            TextView ActvNoIcon = new TextView(MainActivity.this);
            ActvNoIcon.setText(R.string.no_icon);
            if (PreviewHwwv.getFavicon() != null) {
                Aciv.setImageBitmap(PreviewHwwv.getFavicon());
                ObjectLayout OlIcon = new ObjectLayout(MainActivity.this);
                OlIcon.build(1, null, R.string.website_icon, Aciv);
                Ly.addView(OlIcon);
            } else {
                ObjectLayout OlIcon = new ObjectLayout(MainActivity.this);
                OlIcon.build(1, null, R.string.website_icon, ActvNoIcon);
                Ly.addView(OlIcon);
            }

            TextView ActvURL = new TextView(MainActivity.this);
            Do.CanSelect(ActvURL);
            String SUrl = PreviewHwwv.getUrl() != null ? PreviewHwwv.getUrl() : PreviewHwwv.SUrl;
            if (SUrl == null || (SUrl.equals("about:blank"))) {
                ActvURL.setText("file://" + Do.URLEncode(Vers.i.OpenFile.getPath()));
            } else {
                ActvURL.setText(SUrl);
            }

            ObjectLayout OlURL = new ObjectLayout(MainActivity.this);
            OlURL.build(2, null, R.string.website_url, ActvURL);
            Ly.addView(OlURL);

            Dl AdbMsg = new Dl(MainActivity.this);
            AdbMsg.builder.setTitle(R.string.main_menu_review_msg);
            AdbMsg.builder.setView(Sv);
            AdbMsg.builder.setPositiveButton(R.string.ok, null);
            AdbMsg.show();
        }
    }

    public void epAddJs() {
        Dl AdbAdd = new Dl(MainActivity.this);
        AdbAdd.builder.setTitle(R.string.main_menu_js_add);
        final EditText AcetAdd = new EditText(MainActivity.this);
        AcetAdd.setHint(R.string.only_one_line);
        AdbAdd.builder.setView(AcetAdd);
        AdbAdd.builder.setPositiveButton(R.string.main_menu_run, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                ViewOutput();

                String SCode = StringEscapeUtils.unescapeEcmaScript(AcetAdd.getText().toString());
                if (Vers.i.FileType == 0 || Vers.i.FileType == 3) {
                    PreviewHwwv.evaluateJavascript("javascript:" + SCode, null);
                } else if (Vers.i.FileType == 1 || Vers.i.FileType == 5) {
                    if (JsWv != null) {
                        JsWv.evaluateJavascript("javascript:" + SCode, null);
                    } else if (Jr != null) {
                        Jr.evaluateJavascript("javascript:" + SCode, null);
                    }
                }
            }
        });
        AdbAdd.show();
    }

    public void MoreOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_more_full:
                if (!MoreIsFull) {
                    fullMore();
                } else {
                    unFullMore();
                }
                break;
            case R.id.btn_main_more_switch:
                if (Vers.i.FileType == 0 || Vers.i.FileType == 3 || Vers.i.FileType == 7) {
                    PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
                    //获取菜单填充器
                    MenuInflater inflater = popup.getMenuInflater();
                    //填充菜单
                    inflater.inflate(R.menu.switch_menu, popup.getMenu());

                    popup.getMenu().getItem(MoreMode).setVisible(false);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem p1) {
                            switch (p1.getItemId()) {
                                case R.id.menu_explorer:
                                    if (Vers.i.IsOpenProject) {
                                        ViewExplorer();
                                    } else {
                                        NoViewExplorer();

                                    }
                                    break;
                                case R.id.menu_preview:
                                    Topreview();
                                    break;
                                case R.id.menu_termux:
                                    ViewOutput();
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();

                } else {
                    switch (MoreMode) {
                        case 0:
                            if (Vers.i.FileType == 1 || Vers.i.FileType == 5) {
                                ViewOutput();
                            }

                            break;
                        case 1:
                        case 2:
                            if (Vers.i.IsOpenProject) {
                                ViewExplorer();
                            } else {
                                NoViewExplorer();

                            }
                            break;
                    }
                }
                break;
        }
    }

    public void showRecentProjects() {


        final JsonArray ja = binding.wwvWelcome.getRecentArray();
        if (ja == null || ja.size() == 0) {
            toast(R.string.no_recent);
            return;
        }

        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new ScrollView.LayoutParams(-1, -1));
        sv.addView(rl);
        LinearLayout ly = new LinearLayout(this);
        ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        ly.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
        ly.setOrientation(LinearLayout.VERTICAL);
        rl.addView(ly);

        Dl adb = new Dl(this);
        adb.builder.setTitle(R.string.open_recent);
        adb.builder.setView(sv);
        final AlertDialog ad = adb.show();

        for (int i = 0; i < ja.size(); i++) {
            final int time = i;
            ButtonLayout btn = new ButtonLayout(this);
            btn.leftImg.setVisibility(View.GONE);
            btn.titleTv.setText(((JsonObject) (ja.get(i))).get("name").getAsString());
            btn.descriptionTv.setText(((JsonObject) (ja.get(i))).get("path").getAsString());
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    binding.wwvWelcome.openRecent(time);
                    ad.dismiss();
                }
            });
            btn.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View p1) {
                    Dl dl = new Dl(MainActivity.this);
                    dl.builder.setTitle(MainActivity.this.getString(R.string.do_you_want_to_delete) + ((JsonObject) (ja.get(time))).get("name").getAsString() + MainActivity.this.getString(R.string.review_element_del_qu2));
                    dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            binding.wwvWelcome.delRecent(time);
                            ad.dismiss();
                        }
                    });
                    dl.show();
                    return true;
                }
            });
            ly.addView(btn);
        }
    }

    public void CreateTempProject() {
        if (!new TipsManager(this).isExists("ep_temp")) {
            Dl Adb = new Dl(this);
            Adb.builder.setTitle(R.string.temp_tip);
            Adb.builder.setMessage(R.string.temp_inf);
            Adb.builder.setCancelable(false);
            Adb.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    final File FTempWebsite = new File(Vers.i.FVenter, "Temporary Website");
                    if (FTempWebsite.exists()) {
                        Do.delete(FTempWebsite.getPath());
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            createWebsite(FTempWebsite.getPath(), getString(R.string.temp_website), false, true, Vers.i.USUAL_HTML_PROJ);
                        }
                    });
                }
            });
            Adb.show();
            new TipsManager(this).create("ep_temp");
        } else {
            final File FTempWebsite = new File(Vers.i.FVenter, "Temporary Website");
            if (FTempWebsite.exists()) {
                Do.delete(FTempWebsite.getPath());
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    createWebsite(FTempWebsite.getPath(), getString(R.string.temp_website), false, true, Vers.i.USUAL_HTML_PROJ);
                }
            });
        }
    }

    FileChooseClass FileChoose;

    public void createProject() {
        LinearLayout ly = new LinearLayout(this);
        ly.setOrientation(LinearLayout.VERTICAL);
        ly.setPadding(Do.dp2px(this, 10), Do.dp2px(this, 10), Do.dp2px(this, 10), Do.dp2px(this, 10));
        final TextLayout tlProjName = new TextLayout(this);
        tlProjName.build(-1, null, R.string.main_new_file_name, null);
        if (Vers.i.userName == null) {
            tlProjName.Acet.setText(getString(R.string.main_create_website));
        } else {
            tlProjName.Acet.setText(Vers.i.userName + getString(R.string.main_create_website_user));
        }
        ly.addView(tlProjName);
        final FileLayout fileLayout = new FileLayout(this);
        fileLayout.build(false, -1, null, R.string.save_dir, null);
        fileLayout.Acet.setText(Vers.i.FProjects.getPath());
        ly.addView(fileLayout);
        final ChoiceLayout choiceLayout = new ChoiceLayout(this);
        choiceLayout.build(false, -1, null, R.string.website_type, getString(R.string.new_proj_usual) + "|" + getString(R.string.new_proj_php) + "|" + getString(R.string.new_proj_easyapp));
        ly.addView(choiceLayout);
        Dl dl = new Dl(this);
        dl.builder
                .setTitle(R.string.new_project_type)
                .setView(ly)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        if (tlProjName.getText("$") == null || fileLayout.getText("$") == null || new File(fileLayout.getText("$") + "/" + tlProjName.getText("$")).exists()) {
                            toast(R.string.create_website_err);
                            return;
                        }

                        try {
                            if(choiceLayout.Acs.getSelectedItemPosition() == 2 && !new PackMachine().checkAndDownload())
                            {
                                return;
                            }
                        } catch (Exception e) {
                            Do.showErrDialog(MainActivity.this,e);
                            return;
                        }

                        createWebsite(fileLayout.getText("$") + "/" + tlProjName.getText("$"), tlProjName.getText("$"), false, true, choiceLayout.Acs.getSelectedItemPosition());
                    }
                });
        dl.show();
    }

    public void createWebsite(final String path, final String websiteName, boolean isSelected, final boolean isOpenWeb, final int type) {
        try {
            final File WebPathHWPFile = new File(path + "/manifest.json");
            if (!WebPathHWPFile.getParentFile().exists()) {
                if (isSelected)
                    FileChoose.dialog.dismiss();
                WebPathHWPFile.getParentFile().mkdirs();
                WebPathHWPFile.createNewFile();
                final JsonObject object = new JsonObject();
                object.addProperty("name", websiteName);
                object.addProperty("image", "/images");
                object.addProperty("video", "/mediae");
                if (Vers.i.userName != null)
                    object.addProperty("author", Vers.i.userName);

                try {
                    Do.write(object.toString(), WebPathHWPFile);

                    String SPath = (type == Vers.i.PHP_PROJ ? WebPathHWPFile.getParent() + "/index.php" : WebPathHWPFile.getParent() + "/index.html");

                    String SHTMLText = "";
                    if (type == Vers.i.USUAL_HTML_PROJ || type == Vers.i.EASYAPP_PROJ) {
                        if (Vers.i.userName != null) {
                            AssetsClass.copyBigDataToSD(MainActivity.this, "create_html_project.html", getFilesDir().getPath() + "/temp_webpage");

                            SHTMLText = Do.getText(new File(getFilesDir().getPath() + "/temp_webpage"));

                            SHTMLText = SHTMLText.replace("<website_name>", StringEscapeUtils.escapeHtml4(websiteName));

                            SimpleDateFormat df = new SimpleDateFormat("yyyy");
                            SHTMLText = SHTMLText.replace("<user_name>", "Copyright &copy; " + df.format(new Date()) + " " + StringEscapeUtils.escapeHtml4(Vers.i.userName) + ".");
                        } else {
                            AssetsClass.copyBigDataToSD(MainActivity.this, "create_html.html", getFilesDir().getPath() + "/temp_webpage");

                            SHTMLText = Do.getText(new File(getFilesDir().getPath() + "/temp_webpage"));
                        }
                    } else if(type == Vers.i.PHP_PROJ) {
                        AssetsClass.copyBigDataToSD(MainActivity.this, "create_php_web.php", getFilesDir().getPath() + "/temp_webpage");

                        SHTMLText = Do.getText(new File(getFilesDir().getPath() + "/temp_webpage"));
                    }
                    try {
                        new File(SPath).delete();
                    } catch (Exception e) {

                    }
                    new File(SPath).createNewFile();

                    Do.write(SHTMLText, new File(SPath));

                    if (isOpenWeb)
                        OpenWeb(WebPathHWPFile.getParentFile(), true);
                    OpenFile(new File(WebPathHWPFile.getParent() + (type == Vers.i.PHP_PROJ ? "/index.php" : "/index.html")));
                } catch (Exception e) {
                    toast(R.string.main_new_file_exists);
                }
            } else {
                toast(R.string.main_new_file_exists);
            }

            if(type == Vers.i.EASYAPP_PROJ) {
                Vers.i.nowProjectPackMachine.show(true);
                File easyAppEvents = new File(Vers.i.nowProjectPackMachine.packLibDir,"easyapp-events.js");
                File easyAppEventsSite = new File(Vers.i.ProjectDir,"easyapp-events.js");
                Do.copySdcardFile(easyAppEvents.getPath(),easyAppEventsSite.getPath());
            }

        } catch (Exception e) {
            toast(R.string.main_new_file_cannot_create);
        }
    }

    public void checkExplorerMenuBar() {
        if (MoreMode == 0) {
            if (binding.lyMainMoreExplorerNoProject.getVisibility() == View.VISIBLE) {
                binding.exploreMenuBar.load(Menus.explorerMenuNoProj, ContextCompat.getColor(this, R.color.opposition));
            } else {
                binding.exploreMenuBar.load(Menus.explorerMenu, ContextCompat.getColor(this, R.color.opposition));
                if (!Vers.i.isInstalledHopWeb || SettingsClass.isHideHW) {
                    binding.exploreMenuBar.hide(Menus.CONTINUE_HW);
                }
            }
        } else if (MoreMode == 1) {
            binding.exploreMenuBar.load(Menus.explorerMenuPreview, ContextCompat.getColor(this, R.color.opposition));
            if (Vers.i.isFullPreview) {
                binding.exploreMenuBar.visible(Menus.HALF_PREVIEW);
                binding.exploreMenuBar.hide(Menus.FULL_PREVIEW);
            } else {
                binding.exploreMenuBar.visible(Menus.FULL_PREVIEW);
                binding.exploreMenuBar.hide(Menus.HALF_PREVIEW);
            }

            if (!Vers.i.isPreviewerZoomPC) {
                binding.exploreMenuBar.hide(Menus.ZOOM_MODE_MP);
                binding.exploreMenuBar.visible(Menus.ZOOM_MODE_PC);
            } else {
                binding.exploreMenuBar.hide(Menus.ZOOM_MODE_PC);
                binding.exploreMenuBar.visible(Menus.ZOOM_MODE_MP);
            }
        } else if (MoreMode == 2) {
            binding.exploreMenuBar.load(Menus.explorerMenuTermux, ContextCompat.getColor(this, R.color.opposition));
            if (Vers.i.FileType == 0 || Vers.i.FileType == 3 || Vers.i.FileType == 7) {
                binding.exploreMenuBar.hide(Menus.ADD_JS);
                binding.exploreMenuBar.hide(Menus.STOP_JS);
            } else if (Vers.i.FileType == 1 || Vers.i.FileType == 5) {
                binding.exploreMenuBar.visible(Menus.ADD_JS);
                binding.exploreMenuBar.visible(Menus.STOP_JS);

                try {
                    if (binding.editorMenuBar.findViewById(Menus.PAUSE_JS).getVisibility() == View.GONE) {
                        binding.exploreMenuBar.hide(Menus.ADD_JS);
                    }
                } catch (Exception e) {
                    binding.exploreMenuBar.hide(Menus.ADD_JS);
                }
            }
        }

    }

    public void NoViewExplorer() {
        binding.tvMainMoreTitle.setText(R.string.main_explorer);
        binding.tvMainMoreSubtitle.setText("");
        MoreMode = 0;
        binding.lyMainMoreExplorer.setVisibility(View.GONE);
        binding.lyMainMorePreview.setVisibility(View.GONE);
        binding.termux.setVisibility(View.GONE);
        binding.lyMainMoreExplorerNoProject.setVisibility(View.VISIBLE);
        MoreMenu = R.menu.main_explorer_no_project;

        checkExplorerMenuBar();
    }

    public void ViewExplorer() {
        if (Vers.i.ProjectName == null) {
            binding.tvMainMoreTitle.setText(getString(R.string.unkown_website));
        } else {
            binding.tvMainMoreTitle.setText(Vers.i.ProjectName);
        }

        MoreMode = 0;
        binding.lyMainMoreExplorerNoProject.setVisibility(View.GONE);
        binding.lyMainMoreExplorer.setVisibility(View.VISIBLE);
        binding.lyMainMorePreview.setVisibility(View.GONE);
        binding.termux.setVisibility(View.GONE);

        MoreMenu = R.menu.main_explorer_menu;
        binding.elvMainExplorerFile.toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
        VisibleMore();

        checkExplorerMenuBar();
    }

    public void VisibleMore() {
        if (MainActivity.this.findViewById(R.id.ly_editor_findandreplace).getVisibility() == View.VISIBLE) {
            MainActivity.this.getNowEditor().findText();
        }
        binding.lyMainMore.setVisibility(View.VISIBLE);
        Drawable LeftDrawable = getResources().getDrawable(R.drawable.main_more_close);
        binding.btnMainMore.setImageDrawable(LeftDrawable);
        MoreIsVisible = true;
    }

    public void GoneMore() {
        binding.lyMainMore.setVisibility(View.GONE);
        Drawable LeftDrawable = getResources().getDrawable(R.drawable.main_more);
        binding.btnMainMore.setImageDrawable(LeftDrawable);
        MoreIsVisible = false;
    }

    public void ToMore() {
        if (MoreIsVisible) {
            GoneMore();
        } else {
            VisibleMore();
        }
    }

    private void CloseApp() {
        Dl CloseBuilder = new Dl(MainActivity.this);
        CloseBuilder.builder.setTitle(R.string.main_close_app);
        CloseBuilder.builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                try {
                    stopService(Do.getEasyWebServerServiceIntent());
                } catch (Exception e) {

                }
                Do.exit(MainActivity.this);
            }
        });
        CloseBuilder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (Vers.i.isFullPreview) {
                unFullScreenPreview();
            } else if (binding.pastebar.getVisibility() == View.VISIBLE) {
                getNowEditor()._clipboardPanel.hide();
            } else if (findViewById(R.id.ly_editor_findandreplace).getVisibility() == View.VISIBLE) {
                getNowEditor().findText();
            } else if (MoreIsVisible) {
                GoneMore();
            } else if (Vers.i.isFullEdit) {
                unFullScreenEdit();
            } else if (binding.lyMainEditor.getVisibility() != View.GONE) {
                CloseFile();
            } else {
                CloseApp();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        super.onCreateOptionsMenu(menu);
        Vers.i.MainMenu = menu;

        new MenuInflater(this).inflate(R.menu.main_menu_button, menu);
        menu.findItem(R.id.menu_MainServer).setVisible(true);
        menu.findItem(R.id.menu_MainAbout).setVisible(true);
        menu.findItem(R.id.menu_MainServer).getIcon().setTint(ContextCompat.getColor(this, R.color.title));
        menu.findItem(R.id.menu_MainAbout).getIcon().setTint(ContextCompat.getColor(this, R.color.title));
        //通过代码的方式来添加Menu
        //添加菜单项（组ID，菜单项ID，排序，标题）
//        menu.add(0, 2, 200, "Over");
        //添加子菜单
//        SubMenu sub1 = menu.addSubMenu("setting");
//        sub1.add(1, SET_ITEM1, 300, "声音设置");
//        sub1.add(1, SET_ITEM2, 400, "背景设置");

        return true;
    }

    String saveText = "";

    public void save() {
        save(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
            }
        });
    }

    public void save(final Runnable run) {
        String S = MainActivity.this.getNowEditor().getString();
        saveText = S;
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Do.write(saveText, Vers.i.OpenFile);
                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                getNowEpi().save();
                                run.run();
                            }
                        });
                    } catch (Exception e) {
                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                toast(R.string.main_save_error);
                            }
                        });

                    }


                }
            }).start();

        } catch (Exception e) {
            toast(R.string.main_open_error);
        }
    }

    public void add() {
        switch (Vers.i.FileType) {
            case 0:
            case 3:
                //HTML
                {
                    String[] typesArray={
                            getString(R.string.html_elements),
                            getString(R.string.js_codes),
                            getString(R.string.easyapps_funs)
                    };
                    Dl dl=new Dl(this);
                    dl.builder
                            .setTitle(R.string.insert_type)
                            .setItems(typesArray, (dialog, which) -> {
                                switch (which)
                                {
                                    case 0:
                                        new AddClass(MainActivity.this, MainActivity.this.getNowEditor()).builder();
                                        break;
                                    case 1:
                                        new AddScript(MainActivity.this, Vers.i.OaAddJS,true);
                                        break;
                                    case 2:
                                        new AddEasyApp();
                                        break;
                                }
                            });
                    dl.show();
                }
                break;
            case 1:
                //JS
                {
                    String[] typesArray={
                            getString(R.string.js_codes),
                            getString(R.string.easyapps_funs)
                    };
                    Dl dl=new Dl(this);
                    dl.builder
                            .setTitle(R.string.insert_type)
                            .setItems(typesArray, (dialog, which) -> {
                                switch (which)
                                {
                                    case 0:
                                        new AddScript(MainActivity.this, Vers.i.OaAddJS,true);
                                        break;
                                    case 1:
                                        new AddEasyApp();
                                        break;
                                }
                            });
                    dl.show();
                }
                break;
            case 2:
                //CSS
                Dl AbList = new Dl(MainActivity.this);
                AbList.builder.setTitle(R.string.main_menu_add_title);
                AbList.builder.setItems(new String[]{getString((Integer) Vers.i.OaAdd[10][0]) + "(" + Vers.i.OaAdd[10][1] + ")"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        new AddCSS(MainActivity.this, MainActivity.this.getNowEditor());
                    }
                });
                AbList.show();
                break;
            case 5:
                //JSS
                Dl AdbAddTypes = new Dl(MainActivity.this);
                AdbAddTypes.builder
                        .setTitle(R.string.main_menu_add_title)
                        .setItems(new String[]{MainActivity.this.getString(R.string.add_jss_file), MainActivity.this.getString(R.string.add_jss_web), MainActivity.this.getString(R.string.add_jss_debug)}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        switch (p2) {
                            case 0:
                                new AddScript(MainActivity.this, Vers.i.OaAddJSSFile,true);
                                break;
                            case 1:
                                new AddScript(MainActivity.this, Vers.i.OaAddJSSWeb,true);
                                break;
                            case 2:
                                new AddScript(MainActivity.this, Vers.i.OaAddJSSDebug,true);
                                break;
                        }
                    }
                });
                AdbAddTypes.show();

                break;
        }
    }

    public void reviewElement() {
        if (!new TipsManager(MainActivity.this).isExists("review_element")) {
            Dl dl = new Dl(MainActivity.this);
            dl.builder.setTitle(R.string.try_new_fun);
            dl.builder.setMessage(R.string.review_element_help);
            dl.builder.setCancelable(false);
            dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    new ReviewElement().show();
                    new TipsManager(MainActivity.this).create("review_element");
                }
            });
            dl.show();
        } else {
            new ReviewElement().show();
        }
    }

    public void runJs() {
        switch (Vers.i.FileType) {
            case 1:
            case 5:
                //JS JSS
                Vers.i.RunIsViewDone = true;
                binding.editorMenuBar.visible(Menus.PAUSE_JS);
                binding.termux.reset();

                String SText = getNowEditor().getString();

                editorText = SText;

                try {
                    JsWv.destroy();
                    Jr.destroy();
                } catch (Exception e) {
                }
                JsWv = null;
                Jr = null;
                if (Vers.i.FileType == 1) {
                    try {
                        ViewOutput();
                        JsWv = new JSRunWebView(getApplicationContext());
                        File tempJSFile = new File(getFilesDir(), "temp_javascript");
                        tempJSFile.createNewFile();
                        Do.write(editorText, tempJSFile);
                        JsWv.loadDataWithBaseURL("file://" + getNowEpi().getFile().getParent() + "/", "<script src='" + tempJSFile.getPath() + "'></script>", "text/html", "utf-8", null);
                    } catch (Exception e) {
                        toast(R.string.error_private_dirs);
                    }
                } else if (Vers.i.FileType == 5) {

                    Dl Adb = new Dl(MainActivity.this);
                    Adb.builder.setTitle(R.string.main_menu_debug_setting);
                    final TextLayout TlUrldata = new TextLayout(MainActivity.this);
                    TlUrldata.build(0, null, R.string.debug_setting_urldata, R.string.debug_setting_urldata_none);
                    TlUrldata.Acet.setText(Vers.i.urlSetting);
                    TlUrldata.setPadding(Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10), Do.dp2px(MainActivity.this, 10));
                    Adb.builder.setView(TlUrldata);
                    Adb.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            Map urlData = new HashMap();
                            try {
                                if (TlUrldata.getText("$") != null) {
                                    Vers.i.urlSetting = TlUrldata.getText("$").trim();
                                    String data = "{" + TlUrldata.getText("$").trim() + "}";
                                    JsonObject Jo = (JsonObject) JsonParser.parseString(data);

                                    for (Map.Entry<String, JsonElement> key : Jo.entrySet()) {
                                        urlData.put(key.getKey(), key.getValue().toString().substring(1, key.getValue().toString().length() - 1));
                                    }
                                }
                            } catch (Exception e) {
                                toast( R.string.debug_setting_urldata_err);
                            }
                            ViewOutput();
                            Jr = new JssRunner(getApplicationContext());
                            Jr.run(binding.termux.wcc, MainActivity.this, editorText, getNowEpi().getFile().getParentFile(), urlData, null, -1, null, null);
                        }
                    });
                    Adb.show();
                }
                break;
        }
    }

    boolean BUnsave = false;
    Object[] OaKeys;

    public void closeOther() {
        BUnsave = false;
        OaKeys = binding.Ep.MSID.keySet().toArray();
        int nowSID = Vers.i.INowEpSID;
        for (Object IKey : OaKeys) {
            if ((Integer) IKey == nowSID) {
                continue;
            } else {
                if (!getEpi((Integer) IKey).BIsSave) {
                    BUnsave = true;
                } else {
                    closeVoid((Integer) IKey);
                }
            }
        }
        if (BUnsave) {
            Dl dl = new Dl(MainActivity.this);
            dl.builder.setTitle(R.string.close_but_unsave_title);
            dl.builder.setMessage(R.string.close_but_unsave);
            dl.builder.setPositiveButton(R.string.ok, null);
            dl.show();
        }
    }

    public void closeAll() {
        BUnsave = false;
        OaKeys = binding.Ep.MSID.keySet().toArray();
        for (Object IKey : OaKeys) {
            if (!getEpi((Integer) IKey).BIsSave) {
                BUnsave = true;
            } else {
                closeVoid((Integer) IKey);
            }
        }
        if (BUnsave) {
            Dl dl = new Dl(MainActivity.this);
            dl.builder.setTitle(R.string.close_but_unsave_title);
            dl.builder.setMessage(R.string.close_but_unsave);
            dl.builder.setPositiveButton(R.string.ok, null);
            dl.builder.show();
        }
    }

    public void clearJs() {
        try {
            JsWv.destroy();
            Jr.destroy();
        } catch (Exception e) {
        }
        JsWv = null;
        Jr = null;

        binding.editorMenuBar.hide(Menus.PAUSE_JS);
        binding.exploreMenuBar.hide(Menus.ADD_JS);
    }

    String editorText = "";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case R.id.menu_MainServer:
                new LocalServersManager().showDialog(MainActivity.this);
                break;
            case R.id.menu_MainAbout:
                SettingDoor.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO Auto-generated method stub
        if (!Vers.i.isFullPreview) {
            if (!(binding.toolbar.getVisibility() == View.GONE)) {
                ToMore();
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void closeVoid(int sid) {
        binding.Ep.remove(sid);
        if (binding.Ep.MSID.isEmpty()) {
            binding.editorMenuBar.setVisibility(View.GONE);
            binding.splitLineView.setVisibility(View.GONE);
            binding.lyMainEditor.setVisibility(View.GONE);
            binding.Ep.setVisibility(View.GONE);
            binding.lyPreviewAlert.setVisibility(View.GONE);
            Vers.i.INowEpSID = -1;
            binding.Ep.MSID = new HashMap<Integer, Object[]>();
            binding.Ep.MAllFiles = new HashMap<Integer, File>();
            Vers.i.OpenFile = null;
            Vers.i.IsOpeningFile = false;
            binding.lyEditorButtons.setVisibility(View.GONE);
            binding.wwvWelcome.load();

            PreviewHwwv.loadUrl("");
            binding.termux.reset();
            if (Vers.i.IsOpenProject) {
                ViewExplorer();
            } else {
                NoViewExplorer();
            }
            GoneMore();
            binding.btnMainMoreSwitch.setVisibility(View.GONE);
        }
    }

    public void CloseFile() {
        if (!getNowEpi().BIsSave) {
            Dl dl = new Dl(MainActivity.this);
            dl.builder.setTitle(R.string.main_save_isClose);
            dl.builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    closeVoid(Vers.i.INowEpSID);
                }
            });
            dl.show();
        } else {
            closeVoid(Vers.i.INowEpSID);
        }
    }

    public void Topreview() {
        isAutoHideExplorer = false;
        Do.hideIME();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                MainActivity.this.isAutoHideExplorer = true;
            }
        }).start();
        binding.lyMainMoreExplorerNoProject.setVisibility(View.GONE);
        binding.lyMainMoreExplorer.setVisibility(View.GONE);
        binding.lyMainMorePreview.setVisibility(View.VISIBLE);
        binding.termux.setVisibility(View.GONE);
        binding.lyPreviewAlert.setVisibility(View.GONE);
        binding.tvMainMoreTitle.setText(R.string.main_review);
        if (!Vers.i.MainPreviewIsDone)
            binding.tvMainMoreSubtitle.setText(R.string.main_More_loading);
        else
            binding.tvMainMoreSubtitle.setText(R.string.main_More_done);

        VisibleMore();
        MoreMenu = R.menu.main_preview_menu;
        MoreMode = 1;

        checkExplorerMenuBar();
    }

    public void setPreviewerZoomMode(boolean isPC) {
        Vers.i.isPreviewerZoomPC = isPC;
        if (PreviewHwwv != null) {
            PreviewHwwv.getSettings().setUseWideViewPort(isPC);
            PreviewHwwv.reload();
        }
        if (!isPC) {
            binding.exploreMenuBar.hide(Menus.ZOOM_MODE_MP);
            binding.exploreMenuBar.visible(Menus.ZOOM_MODE_PC);
        } else {
            binding.exploreMenuBar.hide(Menus.ZOOM_MODE_PC);
            binding.exploreMenuBar.visible(Menus.ZOOM_MODE_MP);
        }
    }

    public void fullScreenPreview() {
        fullMore();
        binding.actionbar.setVisibility(View.GONE);
        try {
            getNowEditor()._clipboardPanel.hide();
        } catch (Exception e) {

        }

        binding.lyEditorButtons.setVisibility(View.GONE);
        binding.lyMainMenu.setVisibility(View.GONE);
        binding.btnMainMore.setVisibility(View.GONE);
        binding.exploreMenuBar.hide(Menus.FULL_PREVIEW);
        binding.exploreMenuBar.visible(Menus.HALF_PREVIEW);
        binding.btnMainMoreSwitch.setVisibility(View.GONE);
        binding.btnMainMoreFull.setVisibility(View.GONE);
        Do.setMargin(binding.lyMainMore, 0, 0, 0, 0);
        Vers.i.isFullPreview = true;
    }

    public void unFullScreenPreview() {
        binding.actionbar.setVisibility(View.VISIBLE);
        try {
            getNowEditor()._clipboardPanel.hide();
        } catch (Exception e) {

        }
        binding.lyEditorButtons.setVisibility(View.VISIBLE);
        binding.lyMainMenu.setVisibility(View.VISIBLE);
        binding.btnMainMore.setVisibility(View.VISIBLE);
        binding.exploreMenuBar.hide(Menus.HALF_PREVIEW);
        binding.exploreMenuBar.visible(Menus.FULL_PREVIEW);
        binding.btnMainMoreSwitch.setVisibility(View.VISIBLE);
        binding.btnMainMoreFull.setVisibility(View.VISIBLE);
        Do.setMargin(binding.lyMainMore, 0, 0, 0, Do.dp2px(this, 40));
        unFullMore();
        Vers.i.isFullPreview = false;
    }

    public void fullScreenEdit() {
        GoneMore();
        binding.actionbar.setVisibility(View.GONE);
        try {
            getNowEditor()._clipboardPanel.hide();
        } catch (Exception e) {

        }
        if (findViewById(R.id.ly_editor_findandreplace).getVisibility() == View.VISIBLE) {
            getNowEditor().findText();
        }
        binding.lyMainMenu.setVisibility(View.GONE);
        binding.btnMainMore.setVisibility(View.GONE);
        Vers.i.isFullEdit = true;
    }

    public void unFullScreenEdit() {
        binding.actionbar.setVisibility(View.VISIBLE);
        try {
            getNowEditor()._clipboardPanel.hide();
        } catch (Exception e) {

        }
        binding.lyMainMenu.setVisibility(View.VISIBLE);
        binding.btnMainMore.setVisibility(View.VISIBLE);
        Vers.i.isFullEdit = false;
    }

    @Override
    protected void onDestroy() {
        //释放资源
        try {
            stopService(Do.getEasyWebServerServiceIntent());
        } catch (Exception e) {

        }

        try {
            PreviewHwwv.destroy();
        } catch (Exception e) {

        }

        PreviewHwwv = null;

        super.onDestroy();
    }
}
