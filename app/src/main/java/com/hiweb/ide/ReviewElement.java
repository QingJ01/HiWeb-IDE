package com.hiweb.ide;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.myopicmobile.textwarrior.android.CopyBored;
import com.hiweb.ide.add.addViewWidget.ButtonLayout;
import com.hiweb.ide.add.addViewWidget.ObjectLayout;
import com.hiweb.ide.add.addViewWidget.TextLayout;
import com.hiweb.ide.edit.Do;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class ReviewElement {
	String vSHTML;
	private Document vDocHtml;
	private Elements vEsHead;
	private Elements vEsBody;
	private List vLChildPath;

	private Drawable getAddDrawable() {
		Drawable drawable = MainActivity.main.getResources().getDrawable(R.drawable.create_proj);
		drawable.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
		return drawable;
	}

	private Drawable getTextDrawable() {
		Drawable drawable = MainActivity.main.getResources().getDrawable(R.drawable.text);
		drawable.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
		return drawable;
	}

	private Drawable getAttrDrawable() {
		Drawable drawable = MainActivity.main.getResources().getDrawable(R.drawable.menu);
		drawable.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
		return drawable;
	}

	private Drawable getElementDrawable() {
		Drawable drawable = MainActivity.main.getResources().getDrawable(R.drawable.html_element);
		drawable.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
		return drawable;
	}

	public void show() {
		readElements();
		Do.showWaitAndRunInThread(false, new Runnable() {

			@Override
			public void run() {
				vLChildPath = new ArrayList();

				MainActivity.main.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							Do.finishWaiting();

							ScrollView Sv = new ScrollView(MainActivity.main);
							Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							Sv.setFillViewport(true);
							RelativeLayout Rl = new RelativeLayout(MainActivity.main);
							Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							LinearLayout Ly = new LinearLayout(MainActivity.main);
							Ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							Ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
									Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
							Ly.setOrientation(LinearLayout.VERTICAL);
							Sv.addView(Rl);
							Rl.addView(Ly);

							ObjectLayout vOlHead = new ObjectLayout(MainActivity.main);
							vOlHead.build(-1, null, R.string.review_element_head, null);
							ObjectLayout vOlBody = new ObjectLayout(MainActivity.main);
							vOlBody.build(-1, null, R.string.review_element_body, null);

							Ly.addView(vOlHead);
							Ly.addView(vOlBody);

							Dl AdbReview = new Dl(MainActivity.main);
							AdbReview.builder.setTitle(R.string.main_menu_review_element);
							AdbReview.builder.setView(Sv);
							AdbReview.builder.setPositiveButton(R.string.close, null);
							final AlertDialog AdReview = AdbReview.show();

							{
								// Head
								ButtonLayout vBlAdd = new ButtonLayout(MainActivity.main);

								vBlAdd.leftImg.setImageDrawable(getAddDrawable());
								vBlAdd.titleTv.setText(R.string.review_element_add);
								vBlAdd.descriptionTv.setText(R.string.review_element_add_description);
								vBlAdd.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										AdReview.dismiss();
										addChild(vDocHtml.head());
									}
								});
								vOlHead.Ly.addView(vBlAdd);

								String vSText = vDocHtml.head().html();
								ButtonLayout vBlTxt = new ButtonLayout(MainActivity.main);
								vBlTxt.leftImg.setImageDrawable(getTextDrawable());
								vBlTxt.titleTv.setText(R.string.review_element_text);
								vBlTxt.descriptionTv.setText(vSText.length() > 20 ? vSText.substring(0, 21) + "..."
										: (vSText.length() == 0
												? MainActivity.main.getString(R.string.review_element_no_texts)
												: vSText));
								vBlTxt.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										AdReview.dismiss();
										try {
											editText(vDocHtml.head());
										} catch (Exception e) {
											Do.showErrDialog(MainActivity.main, e);
										}
									}
								});
								vOlHead.Ly.addView(vBlTxt);
								for (int i = 0; i < vEsHead.size(); i++) {
									final int index = i;

									boolean vBIsBox = false;

									final Element vENow = vEsHead.get(i);
									if (vENow.children().size() > 0) {
										vBIsBox = true;
									}

									String vSChild = "";
									if (vBIsBox) {
										vSChild = MainActivity.main.getString(R.string.review_element_child_elements);
										for (Element vENowChild : vENow.children()) {
											String name = vENowChild.tagName();
											vSChild += (name + ";");
										}
									} else {
										vSChild = MainActivity.main
												.getString(R.string.review_element_no_child_elements);
									}

									ButtonLayout vBlChild = new ButtonLayout(MainActivity.main);
									vBlChild.leftImg.setImageDrawable(getElementDrawable());
									vBlChild.titleTv.setText(vENow.tagName());
									vBlChild.descriptionTv.setText(vSChild);
									vBlChild.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View p1) {
											AdReview.dismiss();
											vLChildPath = new ArrayList();
											vLChildPath.add("head");
											showChild(vENow);
										}
									});
									vBlChild.setOnLongClickListener(new View.OnLongClickListener() {

										@Override
										public boolean onLongClick(View p1) {
											Dl dl = new Dl(MainActivity.main);
											dl.builder.setTitle(MainActivity.main
													.getString(R.string.review_element_del_qu1) + vENow.tagName()
													+ MainActivity.main.getString(R.string.review_element_del_qu2));
											dl.builder.setPositiveButton(R.string.ok,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface p1, int p2) {
															AdReview.dismiss();
															delChild(vENow);
														}
													});
											dl.show();
											return true;
										}
									});
									vOlHead.Ly.addView(vBlChild);
								}

							}
							{
								// body
								ButtonLayout vBlAdd = new ButtonLayout(MainActivity.main);
								vBlAdd.leftImg.setImageDrawable(getAddDrawable());
								vBlAdd.titleTv.setText(R.string.review_element_add);
								vBlAdd.descriptionTv.setText(R.string.review_element_add_description);
								vBlAdd.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										AdReview.dismiss();
										addChild(vDocHtml.body());
									}
								});
								vOlBody.Ly.addView(vBlAdd);

								String vSText = vDocHtml.body().html();
								ButtonLayout vBlTxt = new ButtonLayout(MainActivity.main);
								vBlTxt.leftImg.setImageDrawable(getTextDrawable());
								vBlTxt.titleTv.setText(R.string.review_element_text);
								vBlTxt.descriptionTv.setText(vSText.length() > 20 ? vSText.substring(0, 21) + "..."
										: (vSText.length() == 0
												? MainActivity.main.getString(R.string.review_element_no_texts)
												: vSText));
								vBlTxt.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										AdReview.dismiss();
										try {
											editText(vDocHtml.body());
										} catch (Exception e) {
											Do.showErrDialog(MainActivity.main, e);
										}
									}
								});
								vOlBody.Ly.addView(vBlTxt);
								String attrs = "";
								for (Attribute nowAttr : vDocHtml.body().attributes().asList()) {
									attrs += (nowAttr.getKey() + ";");
								}
								if (attrs.equals("")) {
									attrs = MainActivity.main.getString(R.string.review_element_no_attrs);
								}
								ButtonLayout vBlAttr = new ButtonLayout(MainActivity.main);
								vBlAttr.leftImg.setImageDrawable(getAttrDrawable());
								vBlAttr.titleTv.setText(R.string.review_element_attr);
								vBlAttr.descriptionTv.setText(attrs);
								vBlAttr.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										AdReview.dismiss();
										showAttrs(vDocHtml.body());
									}
								});
								vOlBody.Ly.addView(vBlAttr);
								for (int i = 0; i < vEsBody.size(); i++) {
									final int index = i;

									boolean vBIsBox = false;

									final Element vENow = vEsBody.get(i);
									if (vENow.children().size() > 0) {
										vBIsBox = true;
									}

									String vSChild = "";
									if (vBIsBox) {
										vSChild = MainActivity.main.getString(R.string.review_element_child_elements);
										for (Element vENowChild : vENow.children()) {
											String name = vENowChild.tagName();
											vSChild += (name + ";");
										}
									} else {
										vSChild = MainActivity.main
												.getString(R.string.review_element_no_child_elements);
									}

									ButtonLayout vBlChild = new ButtonLayout(MainActivity.main);
									vBlChild.leftImg.setImageDrawable(getElementDrawable());
									vBlChild.titleTv.setText(vENow.tagName());
									vBlChild.descriptionTv.setText(vSChild);
									vBlChild.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View p1) {
											AdReview.dismiss();
											vLChildPath = new ArrayList();
											vLChildPath.add("body");
											showChild(vENow);
										}
									});
									vBlChild.setOnLongClickListener(new View.OnLongClickListener() {

										@Override
										public boolean onLongClick(View p1) {
											Dl dl = new Dl(MainActivity.main);
											dl.builder.setTitle(MainActivity.main
													.getString(R.string.review_element_del_qu1) + vENow.tagName()
													+ MainActivity.main.getString(R.string.review_element_del_qu2));
											dl.builder.setPositiveButton(R.string.ok,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface p1, int p2) {
															AdReview.dismiss();
															delChild(vENow);
														}
													});
											dl.show();
											return true;
										}
									});
									vOlBody.Ly.addView(vBlChild);
								}
							}

						} catch (Exception e) {
							Do.showErrDialog(MainActivity.main, e);
						}
					}
				});
			}
		});
	}

	private void showChild(final Element input) {
		Elements vEsChildren = input.children();

		ScrollView Sv = new ScrollView(MainActivity.main);
		Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		Sv.setFillViewport(true);
		RelativeLayout Rl = new RelativeLayout(MainActivity.main);
		Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		LinearLayout Ly = new LinearLayout(MainActivity.main);
		Ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		Ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10));
		Ly.setOrientation(LinearLayout.VERTICAL);
		Sv.addView(Rl);
		Rl.addView(Ly);

		vLChildPath.add(input.tagName());
		String path = vLChildPath.get(0).toString();
		for (int i = 1; i < vLChildPath.size(); i++) {
			path += (">" + vLChildPath.get(i));
		}
		Dl AdbReview = new Dl(MainActivity.main);
		AdbReview.builder.setTitle(input.tagName());
		AdbReview.builder.setView(Sv);
		AdbReview.builder.setPositiveButton(R.string.close, null);
		final AlertDialog AdReview = AdbReview.create();
		AdReview.setButton(AlertDialog.BUTTON_NEUTRAL, MainActivity.main.getString(R.string.review_element_parent),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2) {
						AdReview.dismiss();
						vLChildPath.remove(vLChildPath.size() - 1);
						vLChildPath.remove(vLChildPath.size() - 1);
						if (vLChildPath.size() == 0) {
							show();
						} else {
							showChild(input.parent());
						}
					}
				});
		AdReview.show();

		TextView vActv = new TextView(MainActivity.main);
		vActv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		vActv.setTextColor(Color.GRAY);
		vActv.setTextSize(16);
		vActv.setText(path);

		Ly.addView(vActv);

		ButtonLayout vBlAdd = new ButtonLayout(MainActivity.main);
		vBlAdd.leftImg.setImageDrawable(getAddDrawable());
		vBlAdd.titleTv.setText(R.string.review_element_add);
		vBlAdd.descriptionTv.setText(R.string.review_element_add_description);
		vBlAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AdReview.dismiss();
				addChild(input);
			}
		});
		Ly.addView(vBlAdd);

		String attrs = "";
		for (Attribute nowAttr : input.attributes().asList()) {
			attrs += (nowAttr.getKey() + ";");
		}
		if (attrs.equals("")) {
			attrs = MainActivity.main.getString(R.string.review_element_no_attrs);
		}
		ButtonLayout vBlAttr = new ButtonLayout(MainActivity.main);
		vBlAttr.leftImg.setImageDrawable(getAttrDrawable());
		vBlAttr.titleTv.setText(R.string.review_element_attr);
		vBlAttr.descriptionTv.setText(attrs);
		vBlAttr.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AdReview.dismiss();
				showAttrs(input);
			}
		});
		Ly.addView(vBlAttr);

		String vSText = input.html();
		ButtonLayout vBlTxt = new ButtonLayout(MainActivity.main);
		vBlTxt.leftImg.setImageDrawable(getTextDrawable());
		vBlTxt.titleTv.setText(R.string.review_element_text);
		vBlTxt.descriptionTv.setText(vSText.length() > 20 ? vSText.substring(0, 21) + "..."
				: (vSText.length() == 0 ? MainActivity.main.getString(R.string.review_element_no_texts) : vSText));
		vBlTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AdReview.dismiss();
				try {
					editText(input);
				} catch (Exception e) {
					Do.showErrDialog(MainActivity.main, e);
				}
			}
		});
		Ly.addView(vBlTxt);

		for (int i = 0; i < vEsChildren.size(); i++) {
			final int index = i;

			boolean vBIsBox = false;

			final Element vENow = vEsChildren.get(i);
			if (vENow.children().size() > 0) {
				vBIsBox = true;
			}

			String vSChild = "";
			if (vBIsBox) {
				vSChild = MainActivity.main.getString(R.string.review_element_child_elements);
				for (Element vENowChild : vENow.children()) {
					String name = vENowChild.tagName();
					vSChild += (name + ";");
				}
			} else {
				vSChild = MainActivity.main.getString(R.string.review_element_no_child_elements);
			}

			ButtonLayout vBlChild = new ButtonLayout(MainActivity.main);
			vBlChild.leftImg.setImageDrawable(getElementDrawable());
			vBlChild.titleTv.setText(vENow.tagName());
			vBlChild.descriptionTv.setText(vSChild);
			vBlChild.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					AdReview.dismiss();
					showChild(vENow);
				}
			});
			vBlChild.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1) {
					Dl dl = new Dl(MainActivity.main);
					dl.builder.setTitle(MainActivity.main.getString(R.string.review_element_del_qu1) + vENow.tagName()
							+ MainActivity.main.getString(R.string.review_element_del_qu2));
					dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface p1, int p2) {
							AdReview.dismiss();
							delChild(vENow);
						}
					});
					dl.show();
					return true;
				}
			});
			Ly.addView(vBlChild);
		}
	}

	private void addChild(final Element input) {
		ScrollView Sv = new ScrollView(MainActivity.main);
		Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		Sv.setFillViewport(true);
		RelativeLayout Rl = new RelativeLayout(MainActivity.main);
		Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		LinearLayout ly = new LinearLayout(MainActivity.main);
		ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10));
		ly.setOrientation(LinearLayout.VERTICAL);
		Rl.addView(ly);
		Sv.addView(Rl);

		final TextLayout vTlTag = new TextLayout(MainActivity.main);
		vTlTag.build(-1, null, R.string.review_element_add_name, null);
		ly.addView(vTlTag);
		final AttrsList vAl = new AttrsList(MainActivity.main, null);
		ly.addView(vAl);

		Dl vAdbNew = new Dl(MainActivity.main);
		vAdbNew.builder.setTitle(MainActivity.main.getString(R.string.review_element_add_to) + input.tagName());
		vAdbNew.builder.setView(Sv);
		vAdbNew.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				if (vTlTag.getText("$") == null) {
					MainActivity.main.toast(R.string.review_element_add_name_err);
					return;
				}
				Attributes attr = vAl.getAttrs();

				Element vENew = new Element(Tag.valueOf(vTlTag.getText("$")),
						"file://" + MainActivity.main.getNowEpi().getFile().getParent(), attr);
				input.appendChild(vENew);
				writeElements();
				MainActivity.main.toast(R.string.done);
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(input);
				}
			}
		});
		vAdbNew.builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(input);
				}
			}
		});
		vAdbNew.show();
	}

	private void showAttrs(final Element input) {
		ScrollView Sv = new ScrollView(MainActivity.main);
		Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		Sv.setFillViewport(true);
		RelativeLayout Rl = new RelativeLayout(MainActivity.main);
		Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		LinearLayout ly = new LinearLayout(MainActivity.main);
		ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10));
		ly.setOrientation(LinearLayout.VERTICAL);
		Rl.addView(ly);
		Sv.addView(Rl);

		final AttrsList vAl = new AttrsList(MainActivity.main, input);
		ly.addView(vAl);

		Dl vAdbNew = new Dl(MainActivity.main);
		vAdbNew.builder.setTitle(input.tagName() + MainActivity.main.getString(R.string.review_element_attrs_whos));
		vAdbNew.builder.setView(Sv);
		vAdbNew.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				Attributes attrs = vAl.getAttrs();

				Element vENew = new Element(Tag.valueOf(input.tagName()),
						"file://" + MainActivity.main.getNowEpi().getFile().getParent(), attrs);
				for (int i = 0; i < input.children().size(); i++) {
					vENew.appendChild(input.child(i));
				}
				Element parent = input.parent();
				input.remove();
				parent.appendChild(vENew);
				writeElements();
				MainActivity.main.toast(R.string.done);
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(vENew);
				}
			}
		});
		vAdbNew.builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(input);
				}
			}
		});
		vAdbNew.show();
	}

	public class AttrsList extends ObjectLayout {
		public Attributes attrs;
		public List<Attribute> vLAttrs;

		public AttrsList(final Context c, Element input) {
			super(c);
			build(-1, c.getString(R.string.review_element_attr), R.string.longclick_del, null);
			if (input == null) {
				attrs = new Attributes();
				vLAttrs = new ArrayList<Attribute>();
			} else {
				attrs = input.attributes().clone();
				vLAttrs = attrs.asList();
			}
			ButtonLayout vBlAdd = new ButtonLayout(MainActivity.main);

			vBlAdd.leftImg.setImageDrawable(getAddDrawable());
			vBlAdd.titleTv.setText(R.string.review_element_add_attr);
			vBlAdd.descriptionTv.setText(R.string.review_element_add_attr_description);
			vBlAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					ScrollView Sv = new ScrollView(MainActivity.main);
					Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					Sv.setFillViewport(true);
					RelativeLayout Rl = new RelativeLayout(MainActivity.main);
					Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					LinearLayout ly = new LinearLayout(MainActivity.main);
					ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
							Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
					ly.setOrientation(LinearLayout.VERTICAL);
					Rl.addView(ly);
					Sv.addView(Rl);

					final TextLayout TlKey = new TextLayout(c);
					TlKey.build(-1, null, R.string.review_element_attr_key, null);

					final TextLayout TlValue = new TextLayout(c);
					TlValue.build(-1, null, R.string.review_element_attr_value, null);

					ly.addView(TlKey);
					ly.addView(TlValue);

					Dl AdbEdit = new Dl(c);
					AdbEdit.builder.setTitle(R.string.review_element_edit_attr);
					AdbEdit.builder.setView(Sv);
					AdbEdit.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface p1, int p2) {
							if (TlKey.getText("$") == null) {
								MainActivity.main.toast(R.string.review_element_edit_attr_err);
								return;
							}

							attrs.put(TlKey.getText("$"), TlValue.getText("$") == null ? "" : TlValue.getText("$"));

							addButton(new Attribute(TlKey.getText("$"), attrs.get(TlKey.getText("$"))), c);
						}
					});
					AdbEdit.show();
				}
			});
			Ly.addView(vBlAdd);
			for (final Attribute attr : vLAttrs) {
				addButton(attr, c);
			}
		}

		private void addButton(final Attribute attr, final Context c) {
			final ButtonLayout bl = new ButtonLayout(MainActivity.main);
			bl.leftImg.setVisibility(View.GONE);
			bl.titleTv.setText(attr.getKey());
			bl.descriptionTv.setText(attr.getValue());
			bl.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					ScrollView Sv = new ScrollView(MainActivity.main);
					Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					Sv.setFillViewport(true);
					RelativeLayout Rl = new RelativeLayout(MainActivity.main);
					Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					LinearLayout ly = new LinearLayout(MainActivity.main);
					ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
					ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
							Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
					ly.setOrientation(LinearLayout.VERTICAL);
					Rl.addView(ly);
					Sv.addView(Rl);

					final TextLayout TlKey = new TextLayout(c);
					TlKey.build(-1, null, R.string.review_element_attr_key, null);
					TlKey.Acet.setText(bl.titleTv.getText());

					final TextLayout TlValue = new TextLayout(c);
					TlValue.build(-1, null, R.string.review_element_attr_value, null);
					TlValue.Acet.setText(bl.descriptionTv.getText());

					ly.addView(TlKey);
					ly.addView(TlValue);

					Dl AdbEdit = new Dl(c);
					AdbEdit.builder.setTitle(R.string.review_element_edit_attr);
					AdbEdit.builder.setView(Sv);
					AdbEdit.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface p1, int p2) {
							if (TlKey.getText("$") == null) {
								MainActivity.main.toast(R.string.review_element_edit_attr_err);
								return;
							}
							attrs.remove(attr.getKey());
							attrs.put(TlKey.getText("$"), TlValue.getText("$") == null ? "" : TlValue.getText("$"));

							bl.titleTv.setText(TlKey.getText("$"));
							bl.descriptionTv.setText(attrs.get(TlKey.getText("$")));
						}
					});
					AdbEdit.show();
				}
			});
			bl.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1) {
					attrs.remove(attr.getKey());
					Ly.removeView(bl);
					return true;
				}
			});
			Ly.addView(bl);
		}

		public Attributes getAttrs() {
			return attrs;
		}
	}

	private void delChild(Element input) {
		Element parent = input.parent();
		input.remove();
		writeElements();
		if (vLChildPath.size() == 0) {
			show();
		} else {
			vLChildPath.remove(vLChildPath.size() - 1);
			showChild(parent);
		}
	}

	private void editText(final Element input) throws Exception {
		final String vSText = input.html();

		final LinearLayout vLy = new LinearLayout(MainActivity.main);

		vLy.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		vLy.setOrientation(LinearLayout.VERTICAL);

		final CopyBored vCb = new CopyBored(MainActivity.main);
		vLy.addView(vCb);
		vCb.setVisibility(View.GONE);

		final Editor editor = new Editor(MainActivity.main, -1, false);
		editor.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		if (SettingsClass.ITheme == 1) {
			editor.setDark();
		} else {
			editor.setLight();
		}
		editor.setPasteBar(null, vCb, null, null);
		if (input.tagName().equals("script")) {
			editor.build(1, vSText);
		} else if (input.tagName().equals("style")) {
			editor.build(2, vSText);
		} else {
			editor.build(0, vSText);
		}
		editor.inNames();
		vLy.addView(editor);

		EditorPage.reSetCopyBoredClick(editor, Do.dp2px(MainActivity.main, 50));

		Dl Adb = new Dl(MainActivity.main);
		Adb.builder.setTitle(MainActivity.main.getString(R.string.edit) + input.tagName());
		Adb.builder.setView(vLy);
		Adb.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				input.html(editor.getString());
				writeElements();
				MainActivity.main.binding.Ep.open(MainActivity.main.getNowEditor().sid);
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(input);
				}
			}
		});
		Adb.builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				MainActivity.main.binding.Ep.open(MainActivity.main.getNowEditor().sid);
				if (vLChildPath.size() == 0) {
					show();
				} else {
					vLChildPath.remove(vLChildPath.size() - 1);
					showChild(input);
				}
			}
		});
		Adb.show();
	}

	private void writeElements() {
		vDocHtml.outputSettings().indentAmount(4);
		MainActivity.main.getNowEditor().replaceAll(vDocHtml.html());
		readElements();
	}

	private void readElements() {
		vSHTML = MainActivity.main.getNowEditor().getString();
		vDocHtml = Jsoup.parse(vSHTML);
		vDocHtml.outputSettings().indentAmount(4);
		vEsHead = vDocHtml.head().children();
		vEsBody = vDocHtml.body().children();
	}
}
