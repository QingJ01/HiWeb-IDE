package com.venter.easyweb;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.venter.easyweb.R;
import com.venter.easyweb.edit.Do;
import android.app.AlertDialog;

import org.apache.commons.text.StringEscapeUtils;


public class Encoder 
{
    public static void show(final Context c)
    {
        final String[] Sa=new String[]{"HTML","Java","JavaScript","XML"};
        
        Dl AdbList=new Dl(c);
        AdbList.builder.setTitle(R.string.encoder_title);
        AdbList.builder.setItems(Sa, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, final int index) {
                    Dl AdbEncoder=new Dl(c);
                    AdbEncoder.builder.setTitle(Sa[index]+" "+c.getString(R.string.encoder_main_title));
                    final EditText Acet=new EditText(c);
                    AdbEncoder.builder.setView(Acet);
                    AdbEncoder.builder.setPositiveButton(R.string.encoder,null);
                    AdbEncoder.builder.setNegativeButton(R.string.unencoder,null);
                    AlertDialog AdEncoder=AdbEncoder.show();
                    AdEncoder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                            String SEncode = null;
                            @Override
                            public void onClick(View p1) {      
                                String SText=Acet.getText().toString();
                                switch(index)
                                {
                                    case 0:
                                        SEncode= StringEscapeUtils.escapeHtml4(SText);
                                        break;
                                    case 1:
                                        SEncode=StringEscapeUtils.escapeJava(SText);
                                        break;
                                    case 2:
                                        SEncode=StringEscapeUtils.escapeEcmaScript(SText);
                                        break;
                                    case 3:
                                        SEncode=StringEscapeUtils.escapeXml11(SText);
                                        break;
                                }
                                Dl dl=new Dl(c);
                                dl.builder.setMessage(SEncode)
                                    .setPositiveButton(R.string.copy_to_driver, new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface p1, int p2) {
                                            Do.copyText(c,SEncode);
                                        }
                                    });
                                dl.show();
                            }
                        });
                    AdEncoder.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){

                            String SUnencode = null;
                            @Override
                            public void onClick(View p1) {   
                                String SText=Acet.getText().toString();
                                switch(index)
                                {
                                    case 0:
                                        SUnencode=StringEscapeUtils.unescapeHtml4(SText);
                                        break;
                                    case 1:
                                        SUnencode=StringEscapeUtils.unescapeJava(SText);
                                        break;
                                    case 2:
                                        SUnencode=StringEscapeUtils.unescapeEcmaScript(SText);
                                        break;
                                    case 3:
                                        SUnencode=StringEscapeUtils.unescapeXml(SText);
                                        break;
                                }
                                Dl dl=new Dl(c);
                                dl.builder.setMessage(SUnencode)
                                    .setPositiveButton(R.string.copy_to_driver, new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface p1, int p2) {
                                            Do.copyText(c,SUnencode);
                                        }
                                    });
                                dl.show();
                            }
                        });                    
                }
            });
        AdbList.show();
    }
}
