package com.hiweb.ide;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.hiweb.ide.add.addViewWidget.StyleLayout;
import com.hiweb.ide.edit.Do;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;

import androidx.core.content.ContextCompat;

public class CharsAdder extends LinearLayout
{
    public LinearLayout Ly;

    private HorizontalScrollView Hsv;
    public CharsAdder(android.content.Context context) {
        super(context);
        init();
    }

    public CharsAdder(android.content.Context context, android.util.AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public CharsAdder(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init();
    }

    public CharsAdder(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
        init();
    }
    public CharButton Cb;
    
    public int getSizeHeight()
    {
        return Do.dp2px(getContext(),40);
    }
	private CharButton getCharButton(String text,View.OnClickListener click)
	{
		CharButton vCbNew=new CharButton(getContext());
		vCbNew.setText(text);
		vCbNew.setOnClickListener(click);
		return vCbNew;
	}
    private CharButton getCharButton(int text,View.OnClickListener click)
	{
		return getCharButton(getContext().getResources().getString(text),click);
	}
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.alpha_theme_color));
        setLayoutParams(new RelativeLayout.LayoutParams(-1,getSizeHeight()));
        
        Ly=new LinearLayout(getContext());
        Hsv=new HorizontalScrollView(getContext());
        
        Ly.setLayoutParams(new HorizontalScrollView.LayoutParams(-1,-2));
        Ly.setGravity(Gravity.CENTER|Gravity.LEFT);
        
        Hsv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        Hsv.setFillViewport(true);
        
        Hsv.addView(Ly);
        
        addView(Hsv);
        {
			List<CharButton> vLFuns=new ArrayList();
            final String[] SaCharsList=SettingsClass.SaChars;
			vLFuns.add(getCharButton("←", new View.OnClickListener(){

							   @Override
							   public void onClick(View p1)
							   {
								   MainActivity.main.getNowEditor().moveCaret(MainActivity.main.getNowEditor()._caretPosition-1);
							   }
						   }));
			vLFuns.add(getCharButton("→", new View.OnClickListener(){

							   @Override
							   public void onClick(View p1)
							   {
								   MainActivity.main.getNowEditor().moveCaret(MainActivity.main.getNowEditor()._caretPosition+(1));
							   }
						   }));
            vLFuns.add(getCharButton(R.string.charbutton_undo, new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						MainActivity.main.getNowEditor().undo();
					}
				}));
            vLFuns.add(getCharButton(R.string.charbutton_redo, new View.OnClickListener(){

							   @Override
							   public void onClick(View p1)
							   {
								   MainActivity.main.getNowEditor().redo();
							   }
						   }));
            vLFuns.add(getCharButton(R.string.color, new View.OnClickListener(){

							   @Override
							   public void onClick(View p1)
							   {
								   ColorPickerDialogBuilder
									   .with(MainActivity.main)
									   .setTitle(com.hiweb.ide.R.string.main_menu_add_choose_color_title)
									   .initialColor(Color.parseColor(Vers.i.defaultChooseColor))
									   .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
									   .setCancelable(false)
									   .density(12)
									   .lightnessSliderOnly()
									   .setOnColorSelectedListener(new OnColorSelectedListener() {
										   @Override
										   public void onColorSelected(int selectedColor) {
											   String HEXText=MainActivity.main.convertToRGBA(selectedColor);
										   }
									   })
									   .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
										   @Override
										   public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
											   String HEXText=MainActivity.main.convertToRGBA(selectedColor);
											   if(Vers.i.IsChangeColor)
												   HEXText.substring(1,HEXText.length());
											   MainActivity.main.getNowEditor().insert(HEXText);
										   }
									   })
									   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
										   @Override
										   public void onClick(DialogInterface dialog, int which) {
										   }
									   })
									   .build()
									   .show();
							   }
						   }));
            if(Vers.i.FileType==0|| Vers.i.FileType==2|| Vers.i.FileType==3)
            {
                vLFuns.add(getCharButton(R.string.style, new View.OnClickListener(){

								   @Override
								   public void onClick(View p1)
								   {
									   Dl AdbStyle=new Dl(MainActivity.main);
									   AdbStyle.builder.setTitle(R.string.add_style_title);
									   LinearLayout Ly=new LinearLayout(MainActivity.main);
									   Ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
									   final StyleLayout Sl=new StyleLayout(MainActivity.main);
									   Sl.build(0,null,-1,null);
									   Ly.addView(Sl);
									   AdbStyle.builder.setView(Ly);
									   AdbStyle.builder.setPositiveButton(R.string.insert, new DialogInterface.OnClickListener(){

											   @Override
											   public void onClick(DialogInterface p1, int p2) {
												   MainActivity.main.getNowEditor().insert(Sl.Acet.getText().toString());
											   }
										   });
									   AdbStyle.show();
								   }
							   }));
				vLFuns.add(getCharButton(R.string.main_File, new View.OnClickListener(){

								   @Override
								   public void onClick(View p1)
								   {
									   final FileChooseClass fileChooseClass=new FileChooseClass();
									   fileChooseClass.Type(true);
									   if(Vers.i.IsOpenProject)
									   {
										   fileChooseClass.setOpenPath(Vers.i.ProjectDir);
									   }
									   else
									   {
										   fileChooseClass.setOpenPath(Environment.getExternalStorageDirectory());
									   }
									   fileChooseClass.setOnFileClickListener(new OnFileClickListener(){
											   @Override
											   public void onClick(File ChooseFile,AlertDialog dialog)
											   {
												   dialog.dismiss();
												   String path=Do.getRelativePath(Vers.i.OpenFile.getPath(),ChooseFile.getPath());
												   MainActivity.main.getNowEditor().insert(path);
											   }
										   });
									   fileChooseClass.Show(getContext());
								   }
							   }));
            }
            
            vLFuns.add(getCharButton(R.string.charbutton_table, new View.OnClickListener(){

							   @Override
							   public void onClick(View p1)
							   {
								   MainActivity.main.getNowEditor().insert("\t");
							   }
						   }));
			for(CharButton nowCb:vLFuns)
			{
				Ly.addView(nowCb);
			}
            for(int i=0;i<SaCharsList.length;i++)
            {
				final int i2=i;
				Ly.addView(getCharButton(SaCharsList[i], new View.OnClickListener(){

								   @Override
								   public void onClick(View p1)
								   {
									   MainActivity.main.getNowEditor().insert(SaCharsList[i2]);
								   }
							   }));
                
            }
            
        }
    }
    private class CharButton extends androidx.appcompat.widget.AppCompatTextView
    {
        public CharButton(Context ctx)
        {
            super(ctx);
            setBackground(getContext().getResources().getDrawable(R.drawable.shape_charbutton));
            setLayoutParams(new LinearLayout.LayoutParams(-2,Do.dp2px(getContext(),40f)));
            setPadding(35,0,35,0);
            setTextSize(16);
            setGravity(Gravity.CENTER);
            setTextColor(ContextCompat.getColor(ctx,R.color.title));
        }
    }
    
}
