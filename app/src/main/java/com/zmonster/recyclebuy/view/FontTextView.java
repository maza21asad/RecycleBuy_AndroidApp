package com.zmonster.recyclebuy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.zmonster.recyclebuy.R;


public class FontTextView extends AppCompatTextView {

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

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs, defStyleAttr);
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView, 0, 0);
            try {
                int fontId = a.getInteger(R.styleable.FontTextView_type_face_font, 0);
                if (fontId > 0 && fontId < 7) {
                    Typeface face = Typeface.createFromAsset(getContext().getAssets(), FONTS[fontId]);
                    setTypeface(face);
                }
            } finally {
                a.recycle();
            }
        }
    }

    public void setFontTypeFace(String fontName) {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), fontName);
        setTypeface(face);
    }

}
