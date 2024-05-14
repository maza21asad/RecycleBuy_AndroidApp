package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.LoadingDialog;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private String sex = "MAN";
    private int curType = 0;//The default is the login type

    EditText userPhone;
    EditText userName;
    EditText userPassword;
    EditText userAge;
    EditText userAddress;
    TabLayout tabLayout;
    Button btnLogin;
    LoadingDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {

        userPhone = findViewById(R.id.edit_phone);
        userName = findViewById(R.id.edit_user_name);
        userPassword = findViewById(R.id.edit_user_password);
        userAge = findViewById(R.id.edit_user_age);
        userAddress = findViewById(R.id.edit_user_address);
        tabLayout = findViewById(R.id.tabLayout);
        btnLogin = findViewById(R.id.btn_login);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    curType = 0;
                    userName.setVisibility(View.GONE);
                    userAge.setVisibility(View.GONE);
                    userAddress.setVisibility(View.GONE);
                    btnLogin.setText(R.string.login);
                } else if (tab.getPosition() == 1) {
                    curType = 1;
                    btnLogin.setText(R.string.register);
                    userName.setVisibility(View.VISIBLE);
                    userAge.setVisibility(View.VISIBLE);
                    userAddress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnLogin.setOnClickListener(v -> {
            if (curType == 1) {
                //REGISTER
                progress = new LoadingDialog(this, R.style.LoadingDialogStyle);
                progress.setMessage(getString(R.string.tip_is_register_state));
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                progress.show();
                register();
            } else {
                //LOGIN
                progress = new LoadingDialog(this, R.style.LoadingDialogStyle);
                progress.setMessage(getString(R.string.waiting_login));
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                progress.show();
                login();
            }
        });
    }

    public void login() {
        if (userPhone.getText() == null || userPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.phone_cannot_empty), Toast.LENGTH_SHORT).show();
        }
        if (userPassword.getText() == null || userPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.password_cannot_empty), Toast.LENGTH_SHORT).show();
        } else {
            List<User> users = UserProxy.getInstance().checkNumber(userPhone.getText().toString().trim());
            if (users != null && users.size() > 0) {
                progress.dismiss();
                User user = users.get(0);
                if (user.getPassword().equals(userPassword.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("userids", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("id", user.getId());
                    editor.putString("phone", user.getPhone());
                    editor.apply();
                    Log.d("y2358125699", "username ID：" + user.getId());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.password_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.user_not_register), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void register() {
        if (userName.getText() == null || userName.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.user_name_cannot_empty), Toast.LENGTH_SHORT).show();
        } else if (userAddress.getText() == null || userAddress.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.address_cannot_empty), Toast.LENGTH_SHORT).show();
        } else if (userPhone.getText() == null || userPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.phone_cannot_empty), Toast.LENGTH_SHORT).show();
        } else if (userPhone.getText().toString().trim().length() != 11) {
            Toast.makeText(LoginActivity.this, getString(R.string.phone_is_not_right), Toast.LENGTH_SHORT).show();
        } else if (userPassword.getText() == null || userPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.password_cannot_empty), Toast.LENGTH_SHORT).show();
        } else if (userPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(LoginActivity.this, getString(R.string.password_is_not_safe), Toast.LENGTH_SHORT).show();
        } else {
            List<User> users = UserProxy.getInstance().checkNumber(userPhone.getText().toString().trim());
            if (users != null && users.size() > 0) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.user_has_exists), Toast.LENGTH_SHORT).show();
            } else {
                progress.dismiss();
                User user = new User();
                user.setPhone(userPhone.getText().toString().trim());
                if (userAge.getText() != null) {
                    user.setAge(userAge.getText().toString());
                }
                //TODO Initial points
                user.setPoints(RecycleBuyApplication.USER_POINTS);
                user.setSex(sex);
                user.setUserName(userName.getText().toString());
                user.setPassword(userPassword.getText().toString().trim());
                user.setAddress(userAddress.getText().toString());
                UserProxy.getInstance().insertData(user);
                Toast.makeText(LoginActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                tabLayout.getTabAt(0).select();
            }
        }
    }
}
