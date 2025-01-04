package com.hiweb.ide.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.hiweb.ide.Editor;
import com.hiweb.ide.MainActivity;
import com.hiweb.ide.R;
import com.hiweb.ide.add.addViewWidget.ChoiceLayout;
import com.hiweb.ide.add.addViewWidget.NumLayout;
import com.hiweb.ide.add.addViewWidget.TextLayout;
import com.hiweb.ide.Dl;

public class AddTable {
    public static boolean isShowNums = false;
    public static final int HEADER_LEFT = 1;
    public static final int HEADER_NONE = 0;
    public static final int HEADER_TOP = 2;
    private Editor Eo;
    LinearLayout Ly;
    private Context ctx;

    class AnonymousClass100000000 implements OnClickListener {
        private final AddTable this$0;
        private final ChoiceLayout val$Accl;
        private final Context val$ctx;

        AnonymousClass100000000(AddTable addTable, ChoiceLayout choiceLayout, Context context) {
            this.this$0 = addTable;
            this.val$Accl = choiceLayout;
            this.val$ctx = context;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String text = this.val$Accl.getText("$");
            int i2 = text.equals("None") ? AddTable.HEADER_NONE
                    : text.equals("Top") ? AddTable.HEADER_TOP : text.equals("Left") ? AddTable.HEADER_LEFT : -1;
            String text2 = ((NumLayout) this.this$0.Ly.findViewById(AddTable.HEADER_NONE)).getText("$");
            String text3 = ((NumLayout) this.this$0.Ly.findViewById(AddTable.HEADER_LEFT)).getText("$");
            if (text2 == null || text3 == null) {
                MainActivity.main.toast(R.string.add_table_err);
            } else {
                String SAll = "";
                String SBorder = ((NumLayout) (Ly.findViewById(3))).getText("border=\"$\" ");
                String SCellpadding = ((NumLayout) (Ly.findViewById(4))).getText("cellpadding=\"$\" ");
                String SFrame = ((ChoiceLayout) (Ly.findViewById(5))).getText("frame=\"$\" ");
                String SRules = ((ChoiceLayout) (Ly.findViewById(6))).getText("rules=\"$\" ");
                String SWidth = ((TextLayout) (Ly.findViewById(7))).getText("width=\"$\" ");
                isShowNums = ((ChoiceLayout) (Ly.findViewById(8))).getText("$").equals(ctx.getString(R.string.enable));
                if (SBorder != null) {
                    SAll += SBorder;
                }
                if (SCellpadding != null) {
                    SAll += SCellpadding;
                }
                if (SFrame != null) {
                    SAll += SFrame;
                }
                if (SRules != null) {
                    SAll += SRules;
                }
                if (SWidth != null) {
                    SAll += SWidth;
                }
                this.this$0.OutputTable(SAll, text2, text3, i2);
            }
        }
    }

    public AddTable(Context ctx, Editor Eo) {
        this.ctx = ctx;
        this.Eo = Eo;
        ScrollView scrollView = new ScrollView(ctx);
        scrollView.setFillViewport(true);
        RelativeLayout relativeLayout = new RelativeLayout(ctx);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -2));
        this.Ly = new LinearLayout(ctx);
        this.Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.Ly.setOrientation(HEADER_LEFT);
        this.Ly.setPadding(30, 30, 30, 30);
        scrollView.addView(relativeLayout);
        relativeLayout.addView(this.Ly);
        NumLayout numLayout = new NumLayout(ctx);
        numLayout.build(false, HEADER_NONE, "Row", R.string.add_table_row, null);
        this.Ly.addView(numLayout);
        numLayout = new NumLayout(ctx);
        numLayout.build(false, HEADER_LEFT, "Column", R.string.add_table_column, null);
        this.Ly.addView(numLayout);
        ChoiceLayout choiceLayout = new ChoiceLayout(ctx);
        choiceLayout.build(false, HEADER_TOP, "Header", R.string.add_table_th, "None|Top|Left");
        this.Ly.addView(choiceLayout);
        numLayout = new NumLayout(ctx);
        numLayout.build(true, 3, "Border", R.string.add_table_border, null);
        Ly.addView(numLayout);
        numLayout = new NumLayout(ctx);
        numLayout.build(true, 4, "Cellpadding ", R.string.add_table_cellpadding, null);
        Ly.addView(numLayout);
        choiceLayout = new ChoiceLayout(ctx);
        choiceLayout.build(true, 5, "Frame", R.string.add_table_frame,
                "void|above|below|hsides|vsides|lhs|rhs|box|border");
        this.Ly.addView(choiceLayout);
        choiceLayout = new ChoiceLayout(ctx);
        choiceLayout.build(true, 6, "Rules", R.string.add_table_rules, "none|groups|rows|cols|all");
        this.Ly.addView(choiceLayout);
        TextLayout Tl = new TextLayout(ctx);
        Tl.build(7, "Width", R.string.add_table_width, R.string.add_table_width_hint);
        this.Ly.addView(Tl);
        choiceLayout = new ChoiceLayout(ctx);
        choiceLayout.build(false, 8, null, R.string.is_show_table_nums,
                ctx.getString(R.string.enable) + "|" + ctx.getString(R.string.disable));
        choiceLayout.Acs.setSelection(isShowNums ? 0 : 1, false);
        this.Ly.addView(choiceLayout);

        Dl dl = new Dl(ctx);
        dl.builder.setTitle(ctx.getString(R.string.insert) + ctx.getString(R.string.add_em_table));
        dl.builder.setView(scrollView);
        dl.builder.setPositiveButton(R.string.insert,
                new AnonymousClass100000000(this, (ChoiceLayout) Ly.findViewById(2), ctx));
        dl.show();
    }

    public void OutputTable(String SAll, String str, String str2, int i) {
        try {
            int parseInt = Integer.parseInt(str);
            int parseInt2 = Integer.parseInt(str2);
            String str3 = "    ";
            StringBuilder stringBuilder = null;
            int i2;
            int i3;
            stringBuilder = new StringBuilder();
            stringBuilder.append("<table " + SAll + ">\n");
            switch (i) {
                case HEADER_NONE /* 0 */:
                    for (i2 = HEADER_NONE; i2 < parseInt; i2 += HEADER_LEFT) {
                        stringBuilder.append(new StringBuffer().append(str3).append("<tr>\n").toString());
                        for (i3 = HEADER_NONE; i3 < parseInt2; i3 += HEADER_LEFT) {
                            stringBuilder.append(new StringBuffer()
                                    .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                                            .append(new StringBuffer().append(str3).append(str3).toString())
                                            .append("<td>").toString()))));
                            if (isShowNums)
                                stringBuilder.append("(").append(i3).append(",").append(i2).append(")");
                            stringBuilder.append("</td>\n");
                        }
                        stringBuilder.append(new StringBuffer().append(str3).append("</tr>\n").toString());
                    }
                    stringBuilder.append("</table>\n");
                    break;
                case HEADER_LEFT /* 1 */:
                    for (i2 = HEADER_NONE; i2 < parseInt; i2 += HEADER_LEFT) {
                        stringBuilder.append(new StringBuffer().append(str3).append("<tr>\n").toString());
                        stringBuilder.append(new StringBuffer()
                                .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                                        .append(new StringBuffer().append(str3).append(str3).toString()).append("<th>")
                                        .toString()))));
                        if (isShowNums)
                            stringBuilder.append("HEADER").append(i2 + HEADER_LEFT).toString();
                        stringBuilder.append("</th>\n").toString();
                        for (i3 = HEADER_LEFT; i3 < parseInt2; i3 += HEADER_LEFT) {
                            stringBuilder.append(new StringBuffer()
                                    .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                                            .append(new StringBuffer().append(str3).append(str3).toString())
                                            .append("<td>").toString()))));
                            if (isShowNums)
                                stringBuilder.append("(").append(i3).append(",").append(i2).append(")");
                            stringBuilder.append("</td>\n");
                        }
                        stringBuilder.append(new StringBuffer().append(str3).append("</tr>\n").toString());
                    }
                    stringBuilder.append("</table>\n");
                    break;
                case HEADER_TOP /* 2 */:
                    stringBuilder.append(new StringBuffer().append(str3).append("<tr>\n").toString());
                    for (i3 = HEADER_NONE; i3 < parseInt2; i3 += HEADER_LEFT) {
                        stringBuilder.append(new StringBuffer()
                                .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                                        .append(new StringBuffer().append(str3).append(str3).toString()).append("<th>")
                                        .toString()))));
                        if (isShowNums)
                            stringBuilder.append("HEADER").append(i3 + HEADER_LEFT).toString();
                        stringBuilder.append("</th>\n").toString();
                    }
                    stringBuilder.append(new StringBuffer().append(str3).append("</tr>\n").toString());
                    for (i2 = HEADER_LEFT; i2 < parseInt; i2 += HEADER_LEFT) {
                        stringBuilder.append(new StringBuffer().append(str3).append("<tr>\n").toString());
                        for (i3 = HEADER_NONE; i3 < parseInt2; i3 += HEADER_LEFT) {
                            stringBuilder.append(new StringBuffer()
                                    .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                                            .append(new StringBuffer().append(str3).append(str3).toString())
                                            .append("<td>").toString()))));
                            if (isShowNums)
                                stringBuilder.append("(").append(i3).append(",").append(i2).append(")");
                            stringBuilder.append("</td>\n");
                        }
                        stringBuilder.append(new StringBuffer().append(str3).append("</tr>\n").toString());
                    }
                    stringBuilder.append("</table>\n");
                    break;
            }
            Eo.insert(stringBuilder.toString());
        } catch (Exception e) {
            MainActivity.main.toast(R.string.add_table_out_err);
        }
    }
}
