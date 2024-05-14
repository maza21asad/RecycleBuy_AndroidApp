package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.FontEditText;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;


/**
 * @author Monster_4y
 * changePassword
 */
public class EditPasswordActivity extends DefaultActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setActionBarTitle(getString(R.string.update_password));
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        FontEditText editOldPassword = findViewById(R.id.edit_old_password);
        FontEditText editNewPassword = findViewById(R.id.edit_new_password);
        FontEditText editPasswordAgain = findViewById(R.id.edit_new_password_agin);
        AppCompatButton btnUpdate = findViewById(R.id.btn_update);

        SharedPreferences sharedPreferences = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long id = sharedPreferences.getLong("id", 0);
        User hostUser = UserProxy.getInstance().findUser(id);
        btnUpdate.setOnClickListener(v -> {
            if (editOldPassword.getText() == null || editOldPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditPasswordActivity.this, getString(R.string.old_password_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (!editOldPassword.getText().toString().trim().equals(hostUser.getPassword())) {
                Toast.makeText(EditPasswordActivity.this, getString(R.string.old_password_not_right), Toast.LENGTH_SHORT).show();
            } else if (editNewPassword.getText() == null || editNewPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditPasswordActivity.this, getString(R.string.new_password_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (editPasswordAgain.getText() == null || editPasswordAgain.getText().toString().trim().isEmpty() || !editNewPassword.getText().toString().trim().equals(editPasswordAgain.getText().toString().trim())) {
                Toast.makeText(EditPasswordActivity.this, getString(R.string.new_password_not_equals), Toast.LENGTH_SHORT).show();
            } else if (editPasswordAgain.getText().toString().trim().length() < 6) {
                Toast.makeText(EditPasswordActivity.this, getString(R.string.password_to_simple), Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog progress = new LoadingDialog(
                        this,
                        R.style.LoadingDialogStyle
                );
                progress.setMessage(getString(R.string.update_ing));
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                progress.show();
                User user = new User();
                user.setPassword(editNewPassword.getText().toString().trim());

                if (id != 0){
                    user.setId(id);
                    progress.dismiss();

                    try {
                        UserProxy.getInstance().updatePassword(user);
                        EventBus.getDefault().post(new RefreshTimeLineEvent());
                        Toast.makeText(EditPasswordActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditPasswordActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progress.dismiss();
                    Toast.makeText(EditPasswordActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}