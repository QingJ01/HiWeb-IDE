package com.hiweb.ide.add;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.hiweb.ide.MainActivity;
import com.hiweb.ide.R;
import java.util.Arrays;
import java.util.ArrayList;
import com.hiweb.ide.Dl;
import android.app.AlertDialog;

public class AddStyle {
	public String OutputString = "";

	public void add(final Context c, final EditText edit) {
		String[] ListArr = new String[] {
				MainActivity.main.getString(R.string.main_menu_add_style_font),
				MainActivity.main.getString(R.string.main_menu_add_style_backgroon),
				MainActivity.main.getString(R.string.main_menu_add_style_margin),
				MainActivity.main.getString(R.string.main_menu_add_style_border),
				MainActivity.main.getString(R.string.main_menu_add_style_positioning),
				MainActivity.main.getString(R.string.main_menu_add_style_amin),
				MainActivity.main.getString(R.string.main_menu_add_style_color),
				MainActivity.main.getString(R.string.main_menu_add_style_paged_media),
				MainActivity.main.getString(R.string.main_menu_add_style_dimension),
				MainActivity.main.getString(R.string.main_menu_add_style_content),
				MainActivity.main.getString(R.string.main_menu_add_style_table),
				MainActivity.main.getString(R.string.main_menu_add_style_hyperlink),
				MainActivity.main.getString(R.string.main_menu_add_style_print),
				MainActivity.main.getString(R.string.main_menu_add_style_transition),
				MainActivity.main.getString(R.string.main_menu_add_style_ui)
		};

		Dl ListStyleBuilder = new Dl(c);
		ListStyleBuilder.builder.setTitle(R.string.main_menu_add_style_title);
		ListStyleBuilder.builder.setItems(ListArr, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				switch (p2) {
					case 0:
						InStyle(c, StyleTable.FontListArr, edit);
						break;
					case 1:
						InStyle(c, StyleTable.BackgroonListArr, edit);
						break;
					case 2:
						InStyle(c, StyleTable.MarginListArr, edit);
						break;
					case 3:
						InStyle(c, StyleTable.BoxListArr, edit);
						break;
					case 4:
						InStyle(c, StyleTable.PositioningListArr, edit);
						break;
					case 5:
						InStyle(c, StyleTable.AminListArr, edit);
						break;
					case 6:
						InStyle(c, StyleTable.ColorListArr, edit);
						break;
					case 7:
						InStyle(c, StyleTable.PagedMediaListArr, edit);
						break;
					case 8:
						InStyle(c, StyleTable.DimensionListArr, edit);
						break;
					case 9:
						InStyle(c, StyleTable.ContentListArr, edit);
						break;
					case 10:
						InStyle(c, StyleTable.TableListArr, edit);
						break;
					case 11:
						InStyle(c, StyleTable.HyperlinkListArr, edit);
						break;
					case 12:
						InStyle(c, StyleTable.PrintListArr, edit);
						break;
					case 13:
						InStyle(c, StyleTable.TransitionListArr, edit);
						break;
					case 14:
						InStyle(c, StyleTable.UserInterfaceListArr, edit);
						break;
				}
			}
		});
		ListStyleBuilder.show();
	}

	EditText et;
	AlertDialog aDialog;
	String[] array;

	private void InStyle(final Context c, String[] arr, final EditText edittext) {
		array = arr;
		Arrays.sort(array);
		Dl StyleListBuilder = new Dl(c);
		StyleListBuilder.builder.setItems(array, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, final int p2) {
				Dl EditBuilder = new Dl(c);
				EditBuilder.builder.setTitle(array[p2]);
				et = new EditText(c);
				EditBuilder.builder.setView(et);
				EditBuilder.builder.setNeutralButton(R.string.main_menu_add_choose_color_title, null);
				EditBuilder.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p21, int p22) {
						edittext.getText().insert(edittext.getText().toString().length(),
								array[p2] + ":" + et.getText().toString() + ";");
					}
				});
				aDialog = EditBuilder.show();
				aDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View p1) {
						new ChooseColor().choose(c, "#FFFFFF", et);
					}
				});
			}
		});
		StyleListBuilder.show();
	}

	public static class StyleTable {
		public static String[] getAll() {
			ArrayList<String> list = new ArrayList<String>();
			for (String s : FontListArr) {
				list.add(s);
			}
			for (String s : BackgroonListArr) {
				list.add(s);
			}
			for (String s : MarginListArr) {
				list.add(s);
			}
			for (String s : BoxListArr) {
				list.add(s);
			}
			for (String s : PositioningListArr) {
				list.add(s);
			}
			for (String s : AminListArr) {
				list.add(s);
			}
			for (String s : ColorListArr) {
				list.add(s);
			}
			for (String s : PagedMediaListArr) {
				list.add(s);
			}
			for (String s : DimensionListArr) {
				list.add(s);
			}
			for (String s : ContentListArr) {
				list.add(s);
			}
			for (String s : HyperlinkListArr) {
				list.add(s);
			}
			for (String s : PrintListArr) {
				list.add(s);
			}
			for (String s : TableListArr) {
				list.add(s);
			}
			for (String s : TransitionListArr) {
				list.add(s);
			}
			for (String s : UserInterfaceListArr) {
				list.add(s);
			}

			String[] result = new String[list.size()];
			list.toArray(result);
			Arrays.sort(result);
			return result;
		}

		public static void sortAll() {
			Arrays.sort(FontListArr);
			Arrays.sort(BackgroonListArr);
			Arrays.sort(MarginListArr);
			Arrays.sort(BoxListArr);
			Arrays.sort(PositioningListArr);
			Arrays.sort(AminListArr);
			Arrays.sort(ColorListArr);
			Arrays.sort(PagedMediaListArr);
			Arrays.sort(DimensionListArr);
			Arrays.sort(ContentListArr);
			Arrays.sort(HyperlinkListArr);
			Arrays.sort(PrintListArr);
			Arrays.sort(TableListArr);
			Arrays.sort(TransitionListArr);
			Arrays.sort(UserInterfaceListArr);
		}

		public static final String[] FontListArr = new String[] {
				"color",
				"direction",
				"letter-spacing",
				"line-height",
				"text-align",
				"text-decoration",
				"text-indent",
				"text-transform",
				"unicode-bidi",
				"vertical-align",
				"white-space",
				"word-spacing",
				"text-emphasis",
				"hanging-punctuation",
				"punctuation-trim",
				"text-align-last",
				"text-justify",
				"text-outline",
				"text-overflow",
				"text-shadow",
				"text-wrap",
				"word-break",
				"word-wrap",
				"font",
				"font-family",
				"font-size",
				"font-style",
				"font-variant",
				"font-weight",
				"font-size-adjust",
				"font-stretch",
		};
		public static final String[] BackgroonListArr = new String[] {
				"background",
				"background-attachment",
				"background-color",
				"background-image",
				"background-position",
				"background-repeat",
				"background-clip",
				"background-origin",
				"background-size"
		};
		public static final String[] MarginListArr = new String[] {
				"margin",
				"margin-bottom",
				"margin-left",
				"margin-right",
				"margin-top",
				"padding",
				"padding-bottom",
				"padding-left",
				"padding-right",
				"padding-top"
		};
		public static final String[] BoxListArr = new String[] {
				"overflow-x",
				"overflow-y",
				"overflow-style",
				"rotation",
				"rotation-point",
				"box-align",
				"box-direction",
				"box-flex",
				"box-flex-group",
				"box-lines",
				"box-ordinal-group",
				"box-orient",
				"box-pack",
				"border",
				"border-bottom",
				"border-bottom-color",
				"border-bottom-style",
				"border-bottom-width",
				"border-color",
				"border-left",
				"border-left-color",
				"border-left-style",
				"border-left-width",
				"border-right",
				"border-right-color",
				"border-right-style",
				"border-right-width",
				"border-style",
				"border-top",
				"border-top-color",
				"border-top-style",
				"border-top-width",
				"border-width",
				"outline",
				"outline-color",
				"outline-style",
				"outline-width",
				"border-bottom-left-radius",
				"border-bottom-right-radius",
				"border-image",
				"border-image-outset",
				"border-image-repeat",
				"border-image-slice",
				"border-image-source",
				"border-image-width",
				"border-radius",
				"border-top-left-radius",
				"border-top-right-radius",
				"box-decoration-break",
				"box-shadow",
		};
		public static final String[] PositioningListArr = new String[] {
				"bottom",
				"clear",
				"clip",
				"cursor",
				"display",
				"float",
				"left",
				"overflow",
				"position",
				"right",
				"top",
				"vertical-align",
				"visibility",
				"z-index"
		};
		public static final String[] AminListArr = new String[] {
				"animation",
				"animation-name",
				"animation-duration",
				"animation-timing-function",
				"animation-delay",
				"animation-iteration-count",
				"animation-direction",
				"animation-play-state",
				"animation-fill-mode",
				"marquee-direction",
				"marquee-play-count",
				"marquee-speed",
				"marquee-style",
		};
		public static final String[] ColorListArr = new String[] {
				"color-profile",
				"opacity",
				"rendering-intent",
		};
		public static final String[] PagedMediaListArr = new String[] {
				"bookmark-label",
				"bookmark-level",
				"bookmark-target",
				"float-offset",
				"hyphenate-after",
				"hyphenate-before",
				"hyphenate-character",
				"hyphenate-lines",
				"hyphenate-resource",
				"hyphens",
				"image-resolution",
				"marks",
				"string-set",
				"fit",
				"fit-position",
				"image-orientation",
				"page",
				"size",
		};
		public static final String[] DimensionListArr = new String[] {
				"height",
				"max-height",
				"max-width",
				"min-height",
				"min-width",
				"width",
		};
		public static final String[] ContentListArr = new String[] {
				"content",
				"counter-increment",
				"counter-reset",
				"quotes",
				"crop",
				"move-to",
				"page-policy",
		};
		public static final String[] HyperlinkListArr = new String[] {
				"target",
				"target-name",
				"target-new",
				"target-position",
		};
		public static final String[] PrintListArr = new String[] {
				"orphans",
				"page-break-after",
				"page-break-before",
				"page-break-inside",
				"widows",
		};
		public static final String[] TableListArr = new String[] {
				"border-collapse",
				"border-spacing",
				"caption-side",
				"empty-cells",
				"table-layout",
				"grid-columns",
				"grid-rows",
				"list-style",
				"list-style-image",
				"list-style-position",
				"list-style-type",
				"marker-offset",
				"column-count",
				"column-fill",
				"column-gap",
				"column-rule",
				"column-rule-color",
				"column-rule-style",
				"column-rule-width",
				"column-span",
				"column-width",
				"columns",
		};
		public static final String[] TransitionListArr = new String[] {
				"transition",
				"transition-property",
				"transition-duration",
				"transition-timing-function",
				"transition-delay",
				"transform",
				"transform-origin",
				"transform-style",
				"perspective",
				"perspective-origin",
				"backface-visibility",
		};
		public static final String[] UserInterfaceListArr = new String[] {
				"appearance",
				"box-sizing",
				"icon",
				"nav-down",
				"nav-index",
				"nav-left",
				"nav-right",
				"nav-up",
				"outline-offset",
				"resize",
		};
	}
}
