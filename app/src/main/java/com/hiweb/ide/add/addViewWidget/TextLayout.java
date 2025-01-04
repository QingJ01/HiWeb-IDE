package com.hiweb.ide.add.addViewWidget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hiweb.ide.R;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

public class TextLayout extends LinearLayout {
    public EditText Acet;
    public LinearLayout vLyText;
    public TextView Tv;

    public TextLayout(Context ctx) {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public TextLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public TextLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public TextLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void build(int id, String name, int description, Object other) {
        Tv = new TextView(getContext());
        Tv.setTextSize(12);
        Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.display_color));
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        vLyText = new LinearLayout(getContext());
        vLyText.setOrientation(LinearLayout.HORIZONTAL);
        vLyText.setGravity(Gravity.CENTER);
        vLyText.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        Acet = new EditText(getContext());
        Acet.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        String title = "";
        if (description == -1) {
            title = null;
            Tv.setVisibility(View.GONE);
        } else {
            if (name == null) {
                title = getContext().getString(description);
            } else {
                title = name + ":" + getContext().getString(description);
            }
            Tv.setText(title);
        }
        if (other != null) {
            Acet.setHint(getContext().getString((Integer) other));
        }

        vLyText.addView(Acet);

        addView(Tv);
        addView(vLyText);
        if (id >= 0)
            setId(id);
    }

    private void corner() {
        setBackgroundDrawable(getContext().getDrawable(R.drawable.textlayout_shape));
        Acet.setBackground(null);
        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                Acet.requestFocus();
            }
        });
    }

    public String getText(String code) {
        if (Acet.getText().toString().equals("")) {
            return null;
        } else {
            return code.replace("$", Acet.getText().toString());
        }

    }

}
