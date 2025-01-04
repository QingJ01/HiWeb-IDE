package com.myopicmobile.textwarrior.android;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ClipboardPanel {
	protected FreeScrollingTextField _textField;
	private Context _context;
	private LinearLayout EWActionBar;
	
	public ClipboardPanel(FreeScrollingTextField textField,LinearLayout EWActionBar) {
		_textField = textField;
		_context = textField.getContext();
		this.EWActionBar=EWActionBar;
	}

	public Context getContext() {
		return _context;
	}

	public void show() {
		if(EWActionBar.getVisibility()==View.GONE)
			return;
		int height=EWActionBar.getHeight();
		
		_textField.vCopyBored.setVisibility(View.VISIBLE);
        if(_textField.PasteBar!=null)
		{
			_textField.PasteBar.setLayoutParams(new RelativeLayout.LayoutParams(-1,height));
			
			boolean isDark=_textField.PasteBar==null;
			if(!isDark)
			{
				if(_textField.themeCode==4)
					isDark=true;
			}
			
			_textField.vCopyBored.click(-2,-1,isDark,
				new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						_textField.selectAll();
					}
				},
				new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						_textField.cut();
					}
				},
				new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						_textField.copy();
					}
				},
				new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						_textField.paste();
					}
				});
			
			_textField.PasteBar.setVisibility(View.VISIBLE);
			if(_textField.T.getVisibility()==View.GONE)
			{
				_textField.TitleTextView.setVisibility(View.GONE);
				_textField.vCopyBored.setPadding(dp2px(getContext(),0),dp2px(getContext(),0),dp2px(getContext(),0),dp2px(getContext(),0));
			}
			else
			{
				_textField.TitleTextView.setVisibility(View.VISIBLE);
				_textField.vCopyBored.setPadding(dp2px(getContext(),10),dp2px(getContext(),10),dp2px(getContext(),10),dp2px(getContext(),10));
			}
		}
	}
	public static int dp2px(Context context, float dpValue) {
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
	public void hide() {
		_textField.vCopyBored.setVisibility(View.GONE);
        if(_textField.PasteBar!=null)
		{
			_textField.PasteBar.setVisibility(View.GONE);
		}
	}
}
