package com.hiweb.ide;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.hiweb.ide.edit.Do;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EasyAppInformation {
    static MainActivity main;
    static String version;

    public static void showInfDialog() {
        main = MainActivity.main;
        try {
            if (!new PackMachine().checkAndDownload())
                return;
        } catch (Exception e) {
            Do.showErrDialog(main, e);
            return;
        }

        File libVersion = new File(main.getFilesDir(), "packingLibrary/EasyApp_version.txt");
        version = "Unknown";
        try {
            version = Do.getText(libVersion);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dl dl = new Dl(main);
        ListView listView = new ListView(main);
        String[] strings = new String[] {
                main.getString(R.string.version_info),
                main.getString(R.string.video_demo),
                main.getString(R.string.API_table)
        };
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.main, android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Toast.makeText(main, version, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Do.openUrl("https://space.bilibili.com/1680300503", MainActivity.main);
                    break;
                case 2:
                    Do.openUrl(Vers.i.supportHost + "easywebide/article/#9", MainActivity.main);
                    break;
            }
        });
        dl.builder
                .setTitle(R.string.easyapps_inf)
                .setMessage(R.string.easyapp_introduction)
                .setView(listView);
        dl.show();

    }

    public static void loadFunctions(File appFileDir) {
        try {
            File funsFile = new File(appFileDir, "packingLibrary/functions.json");

            JsonArray allArray = (JsonArray) JsonParser.parseReader(new FileReader(funsFile));
            Vers.i.easyAppFunsMap = new HashMap<String, Object[][]>();
            for (int i = 0; i < allArray.size(); i++) {
                JsonArray singleTypeFunsArray = (JsonArray) allArray.get(i);
                String typeName = singleTypeFunsArray.get(0).getAsString();
                Object[][] singleTypeFunsObjects = new Object[singleTypeFunsArray.size() - 1][2];
                for (int n = 0; n < singleTypeFunsArray.size() - 1; n++) {
                    singleTypeFunsObjects[n][0] = ((JsonArray) singleTypeFunsArray.get(n + 1)).get(0).getAsString();
                    singleTypeFunsObjects[n][1] = ((JsonArray) singleTypeFunsArray.get(n + 1)).get(1).getAsString();
                }
                Vers.i.easyAppFunsMap.put(typeName, singleTypeFunsObjects);
            }
        } catch (Exception e) {

        }
    }
}
