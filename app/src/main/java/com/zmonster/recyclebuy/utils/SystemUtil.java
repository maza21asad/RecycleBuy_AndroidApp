package com.zmonster.recyclebuy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.zmonster.recyclebuy.activities.PickPhotoActivity.INTENT_REQUEST_CAMERA_PERMISSION;


public class SystemUtil {

    public static boolean requestCameraPermission(Activity context) {
        List<String> permissions = new ArrayList<String>();
        boolean result = true;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
            result = false;
        }
        if (permissions.size() > 0) {
            String[] permissionArrays = new String[permissions.size()];
            permissionArrays = permissions.toArray(permissionArrays);
            ActivityCompat.requestPermissions(context,
                    permissionArrays,
                   INTENT_REQUEST_CAMERA_PERMISSION);
        }
        return result;
    }


    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    public static boolean isChinese(Context context) {
        Locale contextLocale = context.getResources().getConfiguration().locale;
        if (contextLocale.getLanguage().equals("zh")) {
            return true;
        } else {
            return false;
        }
    }
}
