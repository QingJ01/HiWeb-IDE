package com.venter.easyweb;
import android.content.*;
import java.io.*;

public class AssetsClass
{
	public static void copyBigDataToSD(Context c,String assetsPath, String strOutFileName) throws IOException 
    {  
        InputStream myInput;  
        OutputStream myOutput = new FileOutputStream(strOutFileName);  
        myInput = c.getAssets().open(assetsPath);  
        byte[] buffer = new byte[1024];  
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length); 
            length = myInput.read(buffer);
        }

        myOutput.flush();  
        myInput.close();  
        myOutput.close();        
    }
}
