package com.hiweb.ide.add.addViewWidget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hiweb.ide.R;

public class ChoiceLayout extends LinearLayout {
    public Spinner Acs;
    public String Snow;

    public ChoiceLayout(Context ctx) {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void build(final boolean IsDefault, int id, String name, int description, Object other) {
        TextView Tv = new TextView(getContext());
        Tv.setTextSize(12);
        Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.display_color));
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        Acs = new Spinner(getContext());
        Acs.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

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
            String STemp = (getContext().getString(R.string.default_)) + "|";
            if (!IsDefault)
                STemp = "";
            final String[] Sa = (STemp + (String) other).split("\\|");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),

                    android.R.layout.simple_spinner_item, Sa);
            Acs.setAdapter(adapter);
            Acs.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                    if (p3 == 0 && IsDefault)
                        Snow = null;
                    else
                        Snow = Sa[p3];
                }

                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                    Snow = null;
                }
            });
        }

        addView(Tv);
        addView(Acs);
        if (id >= 0)
            setId(id);
    }

    public String getText(String code) {
        if (Snow == null)
            return null;
        else
            return code.replace("$", Snow);

    }

}
