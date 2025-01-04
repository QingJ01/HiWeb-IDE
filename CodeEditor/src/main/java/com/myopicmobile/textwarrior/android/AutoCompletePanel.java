package com.myopicmobile.textwarrior.android;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.myopicmobile.textwarrior.R;
import com.myopicmobile.textwarrior.common.Flag;
import com.myopicmobile.textwarrior.common.Language;
import com.myopicmobile.textwarrior.common.LanguageNonProg;
import com.myopicmobile.textwarrior.common.Lexer;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.AdapterView;
import android.os.Build;
import android.view.ViewDebug;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.ListView;
import java.util.List;

public class AutoCompletePanel {

	private FreeScrollingTextField _textField;
	private Context _context;
	private static Language _globalLanguage = LanguageNonProg.getInstance();
	private PopupWindow _autoCompletePanel;
	private AutoCompletePanel.MyAdapter _adapter;
	private ListView vLv;
	private Filter _filter;
	private List<String> oneTags;

	private int _verticalOffset;

	private int _height;

	private int _horizontal;

	private CharSequence _constraint;

	private int _backgroundColor;

	private GradientDrawable gd;

	private int _textColor;

	private String nowPackName="";

	private int LjkhIndex=-100;

	int y=0;

	public AutoCompletePanel(FreeScrollingTextField textField) {
		_textField = textField;
		_context = textField.getContext();
		initAutoCompletePanel();

	}

	public void setTextColor(int color){
		_textColor=color;
		gd.setStroke(1, color);
		_autoCompletePanel.setBackgroundDrawable(gd);
	}


	public void setBackgroundColor(int color){
		_backgroundColor=color;
		gd.setColor(color);
		_autoCompletePanel.setBackgroundDrawable(gd);
	}

	public void setBackground(Drawable color){
		_autoCompletePanel.setBackgroundDrawable(color);
	}

	private void initAutoCompletePanel() {
		oneTags=new ArrayList<String>();
		oneTags.add("br");
		oneTags.add("hr");
		oneTags.add("img");
		oneTags.add("input");
		oneTags.add("param");
		oneTags.add("meta");
		oneTags.add("link");
		oneTags.add("embed");

		final Object[][] jsQuickInputArray=new Object[][]{
				{"function","function (){\n    \n}",-2},
				{"if","if (){\n    \n}",-9},
				{"else","else{\n    \n}",-2},
				{"while","while(){\n    \n}",-9},
				{"for","for (){\n    \n}",-9},
				{"switch","switch (){\n    \n}",-9},
				{"case","case :",-1},
				{"default","default:",0},
				{"do","do{\n    \n}\nwhile();",-2},
				{"break","break;",0},
				{"continue","continue;",0}
		};
		final Object[][] phpQuickInputArray=new Object[][]{
				{"echo","echo \"\";",-2},
				{"print","print \"\";",-2},
				{"function","function (){\n    \n}",-2},
				{"if","if (){\n    \n}",-9},
				{"elseif","elseif (){\n    \n}",-9},
				{"else","else{\n    \n}",-2},
				{"while","while (){\n    \n}",-9},
				{"for","for (){\n    \n}",-9},
				{"foreach","foreach (){\n    \n}",-9},
				{"switch","switch (){\n    \n}",-9},
				{"case","case :",-1},
				{"default","default:",0},
				{"do","do{\n    \n}\nwhile ();",-2},
				{"break","break;",0},
				{"continue","continue;",0}
		};

		vLv=new ListView(_context);
		vLv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		_autoCompletePanel = new PopupWindow(vLv);
		_autoCompletePanel.setOutsideTouchable(true);
		_autoCompletePanel.setAnimationStyle(R.style.AnimBottom);
		_adapter = new MyAdapter(_context, android.R.layout.simple_list_item_1);

		//_autoCompletePanel.setDropDownGravity(Gravity.BOTTOM | Gravity.LEFT);
		_filter = _adapter.getFilter();

		int backgroundColor = _textField.isDark ? Color.BLACK : Color.WHITE;
		int textColor =  _textField.isDark ? Color.WHITE : Color.BLACK;
		gd=new GradientDrawable();
		gd.setColor(backgroundColor);
		gd.setCornerRadius(4);
		gd.setStroke(1, textColor);
		setTextColor(textColor);

		vLv.setAdapter(_adapter);
		_autoCompletePanel.setBackgroundDrawable(gd);
		vLv.setOnItemClickListener(new OnItemClickListener(){

			public boolean isReplaced=false;
			public void replaceText(String text)
			{
				_textField.replaceText(_textField.getCaretPosition() - _constraint.length(), _constraint.length(), text);
				isReplaced=true;
			}

			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
				// TODO: Implement this method
				isReplaced=false;
				String origin=((AutoItem)p2).origin;
				String type=((AutoItem)p2).vSType;
				switch(_textField.nowLang)
				{
					case 0:
					case 3:
						//HTML PHP
						if(type.equals("element"))
						{
							//element
							if(oneTags.contains(origin))
							{
								if(origin.equals("br"))
								{
									replaceText((LjkhIndex==-100 ? "<" :"")+origin+">");
								}
								else
								{
									replaceText((LjkhIndex==-100 ? "<" :"")+origin+" >");
									_textField.moveCaret(_textField._caretPosition-1);
								}

							}
							else
							{
								replaceText((LjkhIndex==-100 ? "<" :"")+origin+"></"+origin+">");
								_textField.moveCaret(_textField._caretPosition-("</"+origin+">").length());

							}
						}
						if(type.equals("attribute"))
						{
							//attrs
							if(origin.equals("onclick"))
								replaceText(origin+"=\"javascript:\"");
							else
								replaceText(origin+"=\"\"");
							_textField.moveCaret(_textField._caretPosition-1);
						}

					case 1:
					case 5:
						//JS JSS

						boolean hasQuickInput=false;
						if(type.equals("keyword"))
						{

							Object[][] quickInputArray=null;
							if(_textField.nowLang==3)
							{
								//PHP
								quickInputArray=phpQuickInputArray;
							}
							else
							{
								quickInputArray=jsQuickInputArray;
							}

							for(int i = 0; i<quickInputArray.length; i++)
							{
								if(origin.equals(quickInputArray[i][0]))
								{
									String complete= (String) quickInputArray[i][1];
									replaceText(complete);
									_textField.moveCaret(_textField._caretPosition + (Integer) quickInputArray[i][2]);
									hasQuickInput=true;

									break;
								}
							}
						}

						if(!hasQuickInput)
						{
							if(type.equals("keyword")||type.equals("function"))
								replaceText(origin);
							if(type.equals("function")&&(origin.endsWith("()")||origin.endsWith("[]")))
							{
								_textField.moveCaret(_textField._caretPosition-1);
							}
						}

						if(_textField.nowLang==1||_textField.nowLang==5)
							break;

					case 2:
						if(type.equals("css"))
						{
							//css
							replaceText(origin+": ;");
							_textField.moveCaret(_textField._caretPosition-1);
						}
						break;
				}
				if(!isReplaced)
				{
					replaceText(origin);
				}
				_adapter.abort();
				dismiss();
			}
		});

	}

	public void setWidth(int width) {
		// TODO: Implement this method
		_autoCompletePanel.setWidth(width);
	}

	private void setHeight(int height) {
		// TODO: Implement this method

		if (_height != height) {
			_height = height;
			_autoCompletePanel.setHeight(height);
		}
	}

	private void setVerticalOffset(int verticalOffset) {
		// TODO: Implement this method
		//verticalOffset=Math.min(verticalOffset,_textField.getWidth()/2);
		int max=0 - _autoCompletePanel.getHeight();
		if (verticalOffset > max) {
			_textField.scrollBy(0, verticalOffset - max);
			verticalOffset = max;
		}
		if (_verticalOffset != verticalOffset) {
			_verticalOffset = verticalOffset;
			y=verticalOffset;
		}
	}

	public void update(CharSequence constraint,int ljkh) {
		nowPackName="";
		LjkhIndex=ljkh;
		_adapter.restart();
		_filter.filter(constraint);
	}

	public void show() {
		if (!_autoCompletePanel.isShowing())
		{
			_autoCompletePanel.showAtLocation(_textField,Gravity.TOP,0,0);

			_autoCompletePanel.update(_textField,0,y,_autoCompletePanel.getWidth(),_autoCompletePanel.getHeight());
		}

		vLv.setFadingEdgeLength(0);
	}

	public void dismiss() {
		if (_autoCompletePanel.isShowing()) {
			_autoCompletePanel.dismiss();
		}
	}
	synchronized public void setLanguage(Language lang) {
		_globalLanguage = lang;
	}

	synchronized public static Language getLanguage() {
		return _globalLanguage;
	}

	/**
	 * Adapter定义
	 */
	class MyAdapter extends ArrayAdapter<String> implements Filterable {

		private int _h;
		private Flag _abort;

		private DisplayMetrics dm;

		public MyAdapter(android.content.Context context, int resource) {
			super(context, resource);
			_abort = new Flag();
			setNotifyOnChange(false);
			dm=context.getResources().getDisplayMetrics();

		}

		public void abort() {
			_abort.set();
		}


		private int dp(float n) {
			// TODO: Implement this method
			return (int)TypedValue.applyDimension(1,n,dm);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO: Implement this method
			TextView view=(TextView) super.getView(position, convertView, parent);
			view.setTextColor(_textColor);
			AutoItem item=new AutoItem(view);
			item.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
			return item;
            /*TextView view=null;
             if(convertView==null){
             view=new TextView(_context);
             view.setTextSize(16);
             view.setPadding(dp(8),dp(3),dp(8),dp(3));
             }
             else{
             view=(TextView) convertView;
             }
             view.setText(getItem(position));*/
		}



		public void restart() {
			// TODO: Implement this method
			_abort.clear();
		}
		public int getItemWidth() {
			_textField.measure(0,0);
			return _textField.getMeasuredWidth();
		}
		public int getItemHeight() {
			if (_h != 0)
				return _h;

			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TextView item = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
			item.measure(0, 0);
			_h = item.getMeasuredHeight();
			return _h;
		}/**
		 * 实现自动完成的过滤算法
		 */
		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {

				/**
				 * 本方法在后台线程执行，定义过滤算法
				 */
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
                    /*int l=constraint.length();
                     int i=l;
                     for(;i>0;i--){
                     if(constraint.charAt(l-1)=='.')
                     break;
                     }
                     if(i>0){
                     constraint=constraint.subSequence(i,l);
                     }*/

					// 此处实现过滤
					// 过滤后利用FilterResults将过滤结果返回
					ArrayList <String>buf = new ArrayList<String>();
					String keyword = String.valueOf(constraint);
					String[] ss=keyword.split("\\.");
					if (ss.length > 1) {
						String pkg=ss[ss.length-2];
						keyword = ss[ss.length-1];
						nowPackName=(String.valueOf(constraint).split("\\."))[ss.length-2];
						if (_globalLanguage.isBasePackage(pkg)) {
							String[] keywords=_globalLanguage.getBasePackage(pkg);
							for (String k:keywords) {
								if (k.startsWith(keyword))
									buf.add(k+":function");
							}
						}
					}
					else if (ss.length == 1) {
						if (keyword.charAt(keyword.length() - 1) == '.') {
							String pkg=keyword.substring(0, keyword.length() - 1);
							keyword = "";
							if (_globalLanguage.isBasePackage(pkg)) {
								String[] keywords=_globalLanguage.getBasePackage(pkg);
								for (String k:keywords) {
									buf.add(k+":function");
								}
							}
						}
						else {
							String[] keywords;
							ArrayList normalList=new ArrayList();
							ArrayList keywordsList=new ArrayList();

							keywords = _globalLanguage.getKeywords();
							for (String k:keywords) {
								if (k.startsWith(keyword))
								{
									boolean isNormal=true;
									if(_globalLanguage.isHtmlElement(k))
									{
										if(!buf.contains(k+":element"))
										{
											buf.add(k+":element");
										}
										isNormal=false;
									}
									if(_globalLanguage.isJsKeyword(k)||_globalLanguage.isPHPKeywords(k))
									{
										if(!keywordsList.contains(k+":keyword"))
										{
											keywordsList.add(k + ":keyword");
										}
										isNormal = false;
									}
									if(isNormal)
									{
										if(!normalList.contains(k+":normal"))
											normalList.add(k+":normal");
									}
								}
							}

							ArrayList attrsList=new ArrayList();
							ArrayList funsList=new ArrayList();

							keywords = _globalLanguage.getNames();
							for (String k:keywords) {
								if (k.startsWith(keyword))
								{
									boolean isNormal=true;
									if(_globalLanguage.isAttr(k))
									{
										if(!attrsList.contains(k+":attribute"))
										{
											attrsList.add(k + ":attribute");
										}
										isNormal = false;
									}
									if(_globalLanguage.isCss(k))
									{
										if(!buf.contains(k+":css"))
										{
											buf.add(k + ":css");
										}
										isNormal = false;
									}
									if(_globalLanguage.isPackName(k))
									{
										if(!funsList.contains(k+":function"))
										{
											funsList.add(k + ":function");
										}
										isNormal = false;
									}
									if(isNormal)
									{
										if(!normalList.contains(k+":normal"))
											normalList.add(k+":normal");
									}
								}
							}

							for (Object name:attrsList) {
								buf.add((String) name);
							}
							for (Object name:keywordsList) {
								buf.add((String) name);
							}
							for (Object name:funsList) {
								buf.add((String) name);
							}
							for (Object name:normalList) {
								buf.add((String) name);
							}
						}
					}
					_constraint = keyword;
					FilterResults filterResults = new FilterResults();
					filterResults.values = buf;   // results是上面的过滤结果
					filterResults.count = buf.size();  // 结果数量
					return filterResults;
				}
				/**
				 * 本方法在UI线程执行，用于更新自动完成列表
				 */
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0 && !_abort.isSet()) {
						// 有过滤结果，显示自动完成列表
						MyAdapter.this.clear();   // 清空旧列表
						MyAdapter.this.addAll((ArrayList<String>)results.values);
						//int y = _textField.getPaintBaseline(_textField.getCaretRow()) - _textField.getScrollY();
						int y = _textField.getCaretY() + _textField.rowHeight() / 2 - _textField.getScrollY();

						int Height=Math.min(_textField.getHeight()/3, getItemHeight() * results.count);

						setHeight(Height);
						setWidth(_textField.getWidth());
						//setHeight((int)(Math.min(_textField.getContentHeight()*0.4,getItemHeight() * Math.min(6, results.count))));

						setVerticalOffset(y-_textField.getHeight());//_textField.getCaretY()-_textField.getScrollY()-_textField.getHeight());
						notifyDataSetChanged();
						show();
					}
					else {
						// 无过滤结果，关闭列表
						notifyDataSetInvalidated();
					}
				}

			};
			return filter;
		}
	}
	public static int dp2px(Context context, float dpValue) {
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dpValue * scale + 0.5f);
	}

	public class AutoItem extends TextView
	{
		public String vSType;
		public String origin;
		public AutoItem(TextView view)
		{
			super(view.getContext());
			setTextColor(_textColor);
			setTextSize(18);
			setPadding(dp2px(view.getContext(),10),dp2px(view.getContext(),10),dp2px(view.getContext(),10),dp2px(view.getContext(),10));
			String name=view.getText().toString();

			if(name.lastIndexOf(":")==-1)
			{
				origin=view.getText().toString();
				normalMode(view);
				return;
			}
			origin=name.split(":")[0];
			vSType=name.split(":")[1];

			if(vSType.equals("function"))
			{
				Drawable drawable=view.getContext().getResources().getDrawable(R.drawable.ed_function);
				drawable.setBounds(0,0,dp2px(view.getContext(),20),dp2px(view.getContext(),20));
				setCompoundDrawables(drawable,null,null,null);
			}
			else if(vSType.equals("element"))
			{
				Drawable drawable=view.getContext().getResources().getDrawable(R.drawable.ed_element);
				drawable.setBounds(0,0,dp2px(view.getContext(),20),dp2px(view.getContext(),20));
				setCompoundDrawables(drawable,null,null,null);
			}
			else if(vSType.equals("attribute"))
			{
				Drawable drawable=view.getContext().getResources().getDrawable(R.drawable.ed_attr);
				drawable.setBounds(0,0,dp2px(view.getContext(),20),dp2px(view.getContext(),20));
				setCompoundDrawables(drawable,null,null,null);
			}
			else if(vSType.equals("css"))
			{
				Drawable drawable=view.getContext().getResources().getDrawable(R.drawable.ed_css);
				drawable.setBounds(0,0,dp2px(view.getContext(),20),dp2px(view.getContext(),20));
				setCompoundDrawables(drawable,null,null,null);
			}
			else if(vSType.equals("keyword"))
			{
				setTypeface(null,Typeface.BOLD);
			}
			else
			{
				normalMode(view);
			}
			setText(origin);
		}
		public void normalMode(TextView view)
		{
			vSType="";
			setCompoundDrawables(null,null,null,null);
			setText(origin);
		}

	}
}
