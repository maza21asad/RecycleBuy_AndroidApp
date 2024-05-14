package com.zmonster.recyclebuy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.zmonster.recyclebuy.R;


public class FontEditText extends AppCompatEditText {

    private final static String[] FONTS = new String[]{
            "",
            "avenir.ttf",
            "MuseoSans-300.otf",
            "MuseoSans-500.otf",
            "MuseoSans-700.otf",
            "ProximaNova-Light.otf",
            "ProximaNova-Semibold.otf",
            "HiraginoSansGB_W3.otf"
    };

    public FontEditText(Context context) {
        super(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleStyleable(context, attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs);
    }

    private void handleStyleable(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontEditText, 0, 0);
            try {
                int fontId = a.getInteger(R.styleable.FontEditText_type_face_font_edit, 0);
                if (fontId > 0 && fontId < 7) {
                    Typeface face = Typeface.createFromAsset(getContext().getAssets(), FONTS[fontId]);
                    setTypeface(face);
                }
            } finally {
                a.recycle();
            }
        }
    }

}
