package com.zmonster.recyclebuy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.utils.FileUtil;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Monster_4y
 */
public class PickPhotoActivity extends DefaultActivity {

    private final static String TAG = "PickPhotoActivity";
    public final static int INTENT_REQUEST_CAMERA_PERMISSION = 14;
    public final static int SELECT_ALL = 0;
    public final static int SELECT_ALBUM = 1;
    private int selectType = SELECT_ALL;//Choose normal mode or just enter the album
    private ImageAdapter listAdapter;
    public final static int INTENT_PICK_PHOTO = 2;
    public final static int INTENT_TAKE_PHOTO = 3;

    private int max;

    Set<String> selectedList = new HashSet<>();

    int column = 3;
    int width;
    private long tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);
        setActionBarTitle(getString(R.string.picture));

        max = getIntent().getIntExtra("max", 1);
        selectType = getIntent().getIntExtra("selectType", 0);
        width = SystemUtil.getScreenWidth(this) / column;

        RecyclerView listView = (RecyclerView) findViewById(R.id.list_view);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, column, GridLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listAdapter = new ImageAdapter();
        listView.setAdapter(listAdapter);
        loadPicInLiberay();
    }

    public void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = null;
        File dir = FileUtil.getDirectory(FileUtil.PICTURE_DIR);
        dir.mkdirs();
        tag = System.currentTimeMillis();
        File mTmpFile = new File(FileUtil.getDirectory(FileUtil.PICTURE_DIR), tag + ".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(mTmpFile);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mTmpFile.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, INTENT_TAKE_PHOTO);
    }

    public void loadPicInLiberay() {
        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                CursorLoader cursorLoader = new CursorLoader(
                        PickPhotoActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{
                                MediaStore.Images.Media.DATA
                        },
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC");
                cursorLoader.loadInBackground();
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String filePath = cursor.getString(0);
                    if ((new File(filePath).exists())) {
                        listAdapter.paths.add(filePath);
                    }
                    cursor.moveToNext();
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (selectedList.size() > 0) {
            getMenuInflater().inflate(R.menu.menu_action_next, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        } else if (itemId == R.id.action_next) {
            String[] paths = new String[selectedList.size()];
            paths = selectedList.toArray(paths);
            new DealPhotoThread(paths).start();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTENT_REQUEST_CAMERA_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                useCamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_TAKE_PHOTO) {
                File photoFile = new File(FileUtil.getDirectory(FileUtil.PICTURE_DIR), tag + ".jpg");
                new DealPhotoThread(new String[]{photoFile.getAbsolutePath()}).start();
            }
        }
    }

    private void returnPhotos(String[] paths) {
        Intent intent = new Intent();
        Bundle paras = new Bundle();
        paras.putStringArray("photos", paths);
        intent.putExtras(paras);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<String> paths = new ArrayList<>();

        public ImageAdapter() {
            if (selectType == SELECT_ALBUM) {
            } else {
                paths.add("");
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (selectType == SELECT_ALBUM) {
                holder = new PhotoViewHolder(LayoutInflater.from(PickPhotoActivity.this).inflate(R.layout.photo_in_liberay_tile, parent, false));
            } else {
                if (viewType == 0) {
                    holder = new CameraViewHolder(LayoutInflater.from(PickPhotoActivity.this).inflate(R.layout.take_camera_photo_tile, parent, false));
                } else {
                    holder = new PhotoViewHolder(LayoutInflater.from(PickPhotoActivity.this).inflate(R.layout.photo_in_liberay_tile, parent, false));
                }
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (selectType == SELECT_ALBUM) {
                PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
                photoHolder.path = paths.get(position);
                if (selectedList.contains(photoHolder.path)) {
                    photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_red);
                } else {
                    photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_grey);
                }
                Picasso.get()
                        .load(new File(photoHolder.path))
                        .resize(width, width)
                        .centerCrop()
                        .into(photoHolder.imageView);
            } else {
                if (position > 0) {
                    PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
                    photoHolder.path = paths.get(position);
                    if (selectedList.contains(photoHolder.path)) {
                        photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_red);
                    } else {
                        photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_grey);
                    }
                    Picasso.get()
                            .load(new File(photoHolder.path))
                            .resize(width, width)
                            .centerCrop()
                            .into(photoHolder.imageView);

                }
            }

        }


        @Override
        public int getItemCount() {
            return paths.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? 0 : 1;
        }

    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView checkMarkView;

        public String path;

        public PhotoViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = width;
            checkMarkView = (ImageView) itemView.findViewById(R.id.mark_check_view);
            itemView.setSelected(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemView.isSelected()) {
                        selectedList.remove(path);
                        checkMarkView.setBackgroundResource(R.drawable.oval_grey);
                        itemView.setSelected(!itemView.isSelected());
                        supportInvalidateOptionsMenu();
                    } else {
                        if (selectedList.size() < max) {
                            selectedList.add(path);
                            checkMarkView.setBackgroundResource(R.drawable.oval_red);
                            itemView.setSelected(!itemView.isSelected());
                            supportInvalidateOptionsMenu();
                        } else {
//                            Toast.makeText(PickPhotoActivity.this, max + " photos at most", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                boolean goNext = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!SystemUtil.requestCameraPermission(PickPhotoActivity.this)) {
                        goNext = false;
                    }
                    if (goNext) {
                        useCamera();
                    }
                }
            });
        }
    }

    private class DealPhotoThread extends Thread {

        public String[] paths;

        public DealPhotoThread(String[] ps) {
            this.paths = ps;
        }

        public void run() {
            String[] newPaths = new String[paths.length];
            for (int i = 0; i < paths.length; i++) {
                try {
                    String oldPath = paths[i];
                    File oldFile = new File(oldPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(oldPath, options);

                    Picasso picasso = Picasso.get();
                    RequestCreator creator = picasso.load(oldFile);
                    if (options.outWidth > 1024 || options.outHeight > 1024) {
                        int targetWidth, targetHeight;
                        if (options.outWidth > options.outHeight) {
                            targetWidth = 1024;
                            targetHeight = options.outHeight * targetWidth / options.outWidth;
                        } else {
                            targetHeight = 1024;
                            targetWidth = options.outWidth * targetHeight / options.outHeight;
                        }
                        creator.resize(targetWidth, targetHeight);
                    }
                    Bitmap bitmap = creator.get();
                    String newFileName = Util.getUnique() + "-" + bitmap.getWidth() + "x" + bitmap.getHeight() + ".img";
                    File folder = FileUtil.getDirectory(FileUtil.PICTURE_DIR);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    File newFile = new File(folder, newFileName);
                    FileUtil.saveBitmap(bitmap, newFile);
                    newPaths[i] = newFile.getAbsolutePath();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            Message msg = returnHandler.obtainMessage(0, newPaths);
            returnHandler.sendMessage(msg);
        }
    }

    private Handler returnHandler = new Handler() {
        public void handleMessage(Message message) {
            String[] ps = (String[]) message.obj;
            returnPhotos(ps);
        }
    };

    public static void pickPhoto(Activity activity, int max) {
        Intent intent = new Intent(activity,PickPhotoActivity.class);
        intent.putExtra("max", max);
        activity.startActivityForResult(intent, INTENT_PICK_PHOTO);
    }

}
