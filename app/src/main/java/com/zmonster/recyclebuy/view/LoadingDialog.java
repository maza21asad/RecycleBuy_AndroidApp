package com.zmonster.recyclebuy.view;

import android.app.Dialog;
import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.zmonster.recyclebuy.R;


public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView loadingView;
    private FontTextView loadingTxt;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.loading_layout);
        loadingView = findViewById(R.id.loading_view);
        loadingTxt = findViewById(R.id.loading_txt);
    }

    public LoadingDialog setMessage(String message) {
        loadingTxt.setText(message);
        return this;
    }

    @Override
    public void show() {
        super.show();
        loadingView.smoothToShow();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loadingView.smoothToHide();
    }
}