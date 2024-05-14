package com.zmonster.recyclebuy.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtil {

    static final String TAG = "FileUtil";
    public static String ROOT_DIR;
    public static String SDCARD_DIR;
    public static final String PICTURE_DIR = "picture";


    public static File getRootStorageDirectory(String directory_name) {
        File result;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "Using sdcard");
            result = new File(Environment.getExternalStorageDirectory(), directory_name);
        } else {
            Log.d(TAG, "Using internal storage");
            result = new File(SDCARD_DIR, directory_name);
        }
        Log.d("getSDCard", Environment.getExternalStorageDirectory().getAbsolutePath() + " isExist " + Environment.getExternalStorageDirectory().exists());
        Log.d("getRootStorageDirectory", result.getAbsolutePath() + " isExist " + result.exists());
        return result;
    }


    public static File getStorageDirectory(File parent_directory, String new_child_directory_name) {

        File result = new File(parent_directory, new_child_directory_name);
        Log.d("getStorageDirectory", "directory ready: " + result.getAbsolutePath());
        return result;
    }



    public static File getDirectory(String dirName) {
        File root = getRootStorageDirectory(ROOT_DIR);
        File dir = getStorageDirectory(root, dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    public static void deleteDirectory(File fileOrDirectory) {
        deleteDirectory(fileOrDirectory, true);
    }


    public static void deleteDirectory(File fileOrDirectory, boolean delDir) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteDirectory(child);

        if (delDir) {
            fileOrDirectory.delete();
        }
    }

    public static void saveBitmap(Bitmap bitmap, File file) {
        try {
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress((bitmap.hasAlpha()) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}