package com.hiweb.ide;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import com.hiweb.ide.edit.Do;
import java.io.File;
import java.io.IOException;
import android.app.AlertDialog;

public class Base64Class {
    Context c;
    public Base64Class(Context c) {
        this.c = c;

        Dl Adb=new Dl(c);
        Adb.builder.setTitle(R.string.base64)
            .setItems(new String[]{c.getString(R.string.enbase64),c.getString(R.string.debase64)}, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    switch (p2) {
                        case 0:
                            en();
                            break;
                        case 1:
                            de();
                            break;
                    }
                }
            });
            Adb.show();
    }

    private void en() {
        final EditText Acet=new EditText(getContext());
        Acet.setHint(R.string.en_text);
        Dl Adb=new Dl(getContext());
        Adb.builder.setTitle(R.string.enbase64)
            .setView(Acet)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if (!Acet.getText().toString().trim().equals("")) {
                        final String SText=Do.enbase64(Acet.getText().toString());
                        Dl AdbResult=new Dl(getContext());
                        AdbResult.builder.setMessage(SText);
                        AdbResult.builder.setPositiveButton(R.string.copy_to_driver, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    Do.copyText(getContext(), SText);
                                }
                            });
                        AdbResult.show();
                    }
                }
            })
            .setNeutralButton(R.string.choose_file, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    FileChooseClass Fcc=new FileChooseClass();
                    Fcc.Type(true);
                    Fcc.setOpenPath(Environment.getExternalStorageDirectory());
                    Fcc.setOnFileClickListener(new OnFileClickListener(){
                            @Override
                            public void onClick(File ChooseFile, AlertDialog dialog) {
                                try {
                                    dialog.dismiss();
                                    final String SText=Do.enbase64file(ChooseFile);
                                    Dl AdbResult=new Dl(getContext());
                                    AdbResult.builder.setMessage(SText);
                                    AdbResult.builder.setPositiveButton(R.string.copy_to_driver, new DialogInterface.OnClickListener(){

                                            @Override
                                            public void onClick(DialogInterface p1, int p2) {
                                                Do.copyText(getContext(), SText);
                                            }
                                        });
                                    AdbResult.show();
                                } catch (IOException e) {
                                    Do.showErrDialog(getContext(),e);
                                }
                            }
                        });
                    Fcc.Show(getContext());
                }
            });
            Adb.show();
    }

    private void de() {
        final EditText Acet=new EditText(getContext());
        Acet.setHint(R.string.de_text_hint);
        Dl Adb=new Dl(getContext());
        Adb.builder.setTitle(R.string.debase64)
            .setView(Acet)
            .setPositiveButton(R.string.de_text, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if (!Acet.getText().toString().trim().equals("")) {
                        final String SText=Do.debase64(Acet.getText().toString());
                        Dl AdbResult=new Dl(getContext());
                        AdbResult.builder.setMessage(SText);
                        AdbResult.builder.setPositiveButton(R.string.copy_to_driver, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    Do.copyText(getContext(), SText);
                                }
                            });
                        AdbResult.show();
                    }
                }
            })
            .setNeutralButton(R.string.de_file, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    Dl AdbName=new Dl(c);
                    AdbName.builder.setTitle(R.string.edit_save_name);
                    final EditText AcetName=new EditText(c);
                    AdbName.builder.setView(AcetName);
                    AdbName.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                if(AcetName.getText().toString().trim().equals(""))
                                {
                                    MainActivity.main.toast(R.string.name_cannot_ignore);
                                    return;
                                }
                                else
                                {
                                    final FileChooseClass Fcc=new FileChooseClass();
                                    Fcc.Type(false);
                                    Fcc.setOpenPath(Environment.getExternalStorageDirectory());
                                    Fcc.setOnClickListener(new View.OnClickListener(){

                                            @Override
                                            public void onClick(View p1) {
                                                File file=new File(Fcc.nowPath,AcetName.getText().toString());
                                                if(file.exists())
                                                {
                                                    MainActivity.main.toast(R.string.main_new_file_exists);
                                                    return;
                                                }
                                                
                                                Fcc.dialog.dismiss();
                                                try {
                                                    final String SText=Acet.getText().toString();
                                                    Do.debase64file(SText,file.getPath());
                                                    MainActivity.main.toast(R.string.done);
                                                } catch (IOException e) {
                                                    Do.showErrDialog(getContext(),e);
                                                }
                                            }
                                        });
                                    Fcc.Show(getContext());
                                }
                            }
                        });
                    AdbName.show();
                }
            });
            Adb.show();
    }

    public Context getContext() {
        return c;
    }
}
