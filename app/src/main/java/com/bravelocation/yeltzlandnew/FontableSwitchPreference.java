package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by John on 01/06/2016.
 */
public class FontableSwitchPreference  extends SwitchPreference {

    private Typeface textFont;

    public FontableSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.textFont = Typeface.createFromAsset(context.getAssets(), "american_typewriter_regular.ttf");
    }

    public FontableSwitchPreference(Context context) {
        super(context);
        this.textFont = Typeface.createFromAsset(context.getAssets(), "american_typewriter_regular.ttf");
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView title = (TextView) view.findViewById(android.R.id.title);
        title.setTypeface(this.textFont);

        TextView summary = (TextView) view.findViewById(android.R.id.summary);
        summary.setTypeface(this.textFont);
    }
}
