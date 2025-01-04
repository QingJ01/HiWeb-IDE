package com.hiweb.ide.add;

import com.hiweb.ide.Dl;
import com.hiweb.ide.EasyAppInformation;
import com.hiweb.ide.MainActivity;
import com.hiweb.ide.R;
import com.hiweb.ide.Vers;

public class AddEasyApp {
    public AddEasyApp() {
        if (Vers.i.easyAppFunsMap == null) {
            try {
                Vers.i.nowProjectPackMachine.checkAndDownload();
            } catch (Exception e) {
                MainActivity.main.toast(R.string.error);
            }
            return;
        }

        String[] typesArray = Vers.i.easyAppFunsMap.keySet().toArray(new String[] {});

        Dl AdbAddTypes = new Dl(MainActivity.main);
        AdbAddTypes.builder
                .setTitle(R.string.main_menu_add_title)
                .setItems(typesArray, (p1, p2) -> {
                    new AddScript(MainActivity.main, Vers.i.easyAppFunsMap.get(typesArray[p2]), false);
                })
                .setPositiveButton(R.string.easyapps_inf, (p1, p2) -> {
                    EasyAppInformation.showInfDialog();
                });
        AdbAddTypes.show();
    }
}
