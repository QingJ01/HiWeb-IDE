package com.hiweb.ide;

import android.content.Context;

import com.hiweb.ide.edit.Do;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AnnouncementManager {
    public static AnnouncementInf getServerInf(){
        try
        {
            String url = Vers.i.serverHost + "server/easyweb/announcement/";
            int id = Integer.parseInt(Objects.requireNonNull(Do.testGetHtml(url+"id.txt", 6)));
            String title = Objects.requireNonNull(Do.testGetHtml(url+"title.txt", 6));
            String description = Objects.requireNonNull(Do.testGetHtml(url+"description.txt", 6));
            String text = Objects.requireNonNull(Do.testGetHtml(url+"text.txt", 6));
            String link = Objects.requireNonNull(Do.testGetHtml(url+"link.txt", 6));

            return new AnnouncementInf(id,title,description,text,link);
        }
        catch (NullPointerException e)
        {
            return null;
        }

    }

    public static boolean isNeedAnnouncing(Context context){
        File localIDFile = new File(context.getFilesDir(),"announcement_id");
        int localID = -1;
        if (!localIDFile.exists()) {
            try {
                localIDFile.createNewFile();
                Do.write("-1",localIDFile);
            } catch (IOException e) {

            }
        }
        else
        {
            try
            {
                localID = Integer.parseInt(Do.getText(localIDFile));
            }
            catch (Exception e)
            {
                localIDFile.delete();
            }
        }

        return Vers.i.newestAnnouncementInf != null && Vers.i.newestAnnouncementInf.id>localID;
    }

    public static void updateLocalID(Context context) throws Exception {
        File localIDFile = new File(context.getFilesDir(),"announcement_id");
        if (!localIDFile.exists()) localIDFile.createNewFile();
        Do.write(String.valueOf(Vers.i.newestAnnouncementInf.id),localIDFile);
    }

    public static class AnnouncementInf{
        public int id=-1;
        public String title;
        public String description;
        public String text;
        public String link;

        public AnnouncementInf(int id,String title,String description,String text,String link)
        {
            this.id = id;
            this.title = title;
            this.description = description;
            this.text = text;
            this.link = link;

        }
    }
}