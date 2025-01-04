package com.venter.easyweb.add.addViewWidget;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.venter.easyweb.*;
import com.venter.easyweb.add.*;
import com.venter.easyweb.edit.*;
import java.io.*;
import android.os.*;
import android.app.*;

import androidx.core.content.ContextCompat;

public class FileLayout extends LinearLayout  
{
    public EditText Acet;
	boolean isFile;
    public FileLayout(Context ctx)
    {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void build(final boolean isFile,int id,String name,int description,Object other)
    {
		this.isFile=isFile;
        TextView Tv=new TextView(getContext());
        Tv.setTextSize(12);
        Tv.setTextColor(ContextCompat.getColor(getContext(),R.color.display_color));
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));

        Acet=new EditText(getContext());
        Acet.setLayoutParams(new LinearLayout.LayoutParams(-2,-2,1f));
        LinearLayout Ly=new LinearLayout(getContext());
        Ly.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        ImageButton Acib=new ImageButton(getContext(),null,android.R.attr.buttonBarButtonStyle);
        Acib.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(),44),Do.dp2px(getContext(),44)));
		Drawable colorDrawable=getResources().getDrawable(isFile ? R.drawable.file_small : R.drawable.folder_small);
		colorDrawable.setTint(ContextCompat.getColor(getContext(),R.color.opposition));
        Acib.setImageDrawable(colorDrawable);

        Acib.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
					File originFile=new File(Acet.getText().toString());
					if(isFile)
						if(originFile.getParentFile().exists())
							originFile=originFile.getParentFile();
						else 
							originFile=Environment.getExternalStorageDirectory();
					else
						if(!originFile.exists())
							originFile=Environment.getExternalStorageDirectory();
					final FileChooseClass fileChooseClass=new FileChooseClass();
					fileChooseClass.Type(isFile);
					fileChooseClass.setOpenPath(originFile);
					fileChooseClass.setOnFileClickListener(new OnFileClickListener(){
						
							public void onClick(File ChooseFile,AlertDialog dialog){
								dialog.dismiss();
								Acet.setText(ChooseFile.getPath());
							}
					});
					fileChooseClass.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								fileChooseClass.dialog.dismiss();
								Acet.setText(fileChooseClass.nowPath.getPath());
							}
						});
					fileChooseClass.Show(getContext());
                }
            });
			
        Ly.addView(Acet);
        Ly.addView(Acib);

        String title="";
        if(name==null)
        {
            title=getContext().getString(description);
        }
        else
        {
            title=name+":"+getContext().getString(description);
        }
        Tv.setText(title);
        if(other!=null)
        {
            Acet.setHint(getContext().getString((Integer) other));
        }

        addView(Tv);
        addView(Ly);
        if(id>=0)
            setId(id);
    }

    public String getText(String code)
    {
        if(Acet.getText().toString().equals(""))
        {
            return null;
        }
        else
        {
			try
			{
				if(isFile)
				{
					if(!new File(Acet.getText().toString()).getParentFile().exists())
						return null;
				}
				else
				{
					if(!new File(Acet.getText().toString()).exists())
						return null;
				}
				return Acet.getText().toString();
			}
			catch(Exception e)
			{
				return null;
			}
        }
    }
}
