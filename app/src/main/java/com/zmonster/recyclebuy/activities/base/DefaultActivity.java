package com.zmonster.recyclebuy.activities.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zmonster.recyclebuy.R;


public class DefaultActivity extends AppCompatActivity {

    protected ActionBar actionBar;

    public ImageView actionBackBtn;
    public ImageView actionRightBtn;
    private TextView actionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.myactionbar_layout);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            Toolbar toolbar = (Toolbar) actionBar.getCustomView().getParent();
            toolbar.setContentInsetsRelative(0, 0);
            actionBackBtn =  findViewById(R.id.actionbar_back_btn);
            actionRightBtn =  findViewById(R.id.actionbar_recommond_btn);
            actionBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DefaultActivity.this.onBackPressed();
                }
            });
            actionTitle = findViewById(R.id.actionbar_title);
        }
    }

    public void setActionBarTitle(String title) {
        if (actionTitle != null) {
            actionTitle.setText(title);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (actionBackBtn != null) {
            if (menu.size() == 0) {
                actionTitle.setPadding(0, 0, actionBackBtn.getLayoutParams().width, 0);
            } else {
                actionTitle.setPadding(0, 0, 0, 0);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }


}
