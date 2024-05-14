package com.zmonster.recyclebuy.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.utils.DateUtil;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PickVideoActivity extends DefaultActivity {

    private static final int INTENT_ADD_CONTENT_SHORTVIDEO = 26;
    public static final int INTENT_PICK_VIDEO = 10;
    private final String[] mediaColumns = new String[]{
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION
    };

    private ImageAdapter listAdapter;

    private int max;

    Set<VideoInfo> selectedList = new HashSet<>();

    int column = 3;
    int width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);
        setActionBarTitle(getString(R.string.video));

        max = getIntent().getIntExtra("max", 1);
        width = SystemUtil.getScreenWidth(this) / column;

        RecyclerView listView = (RecyclerView) findViewById(R.id.list_view);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, column, GridLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listAdapter = new ImageAdapter();
        listView.setAdapter(listAdapter);
        loadPicInLiberay();
    }

    public void loadPicInLiberay() {
        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                CursorLoader cursorLoader = new CursorLoader(
                        PickVideoActivity.this,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        mediaColumns,
                        null,
                        null,
                        MediaStore.Video.Media.DATE_ADDED + " DESC");
                cursorLoader.loadInBackground();
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                cursor.moveToFirst();
                List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
                while (!cursor.isAfterLast()) {
                    VideoInfo info = new VideoInfo();

                    info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    if (!(new File(info.filePath).exists()) || info.filePath.endsWith(".ts")) {
                        cursor.moveToNext();
                        continue;
                    }
                    info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

                    Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    info.size = (Util.parseDouble(size.doubleValue() / 1024 / 1024, "0.00")) + "mb";
                    Long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    info.duration = DateUtil.getDurationTime(duration.intValue() / 1000);

                    videoInfos.add(info);
                    cursor.moveToNext();
                }
                listAdapter.setData(videoInfos);
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
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_next) {
            String[] paths = new String[selectedList.size()];
            int i = 0;
            Iterator<VideoInfo> it = selectedList.iterator();
            boolean hasMax = false;
            while (it.hasNext()) {
                VideoInfo info = it.next();
                String fileSize = info.size.substring(0, info.size.indexOf("mb"));
                Double size = Double.valueOf(fileSize);
                if (size > 30) {
                    hasMax = true;
                    break;
                }
                paths[i++] = info.filePath;
            }
            if (hasMax) {
                Toast.makeText(this, getString(R.string.video_size_than_max), Toast.LENGTH_SHORT).show();
                return true;
            }
            returnVideos(paths);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void returnVideos(String[] paths) {
        Intent intent = new Intent();
        Bundle paras = new Bundle();
        paras.putStringArray("videos", paths);
        intent.putExtras(paras);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<VideoInfo> infos = new ArrayList<>();

        public ImageAdapter() {
        }

        public void setData(List<VideoInfo> vs) {
            infos.clear();
            infos.addAll(vs);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            holder = new PhotoViewHolder(LayoutInflater.from(PickVideoActivity.this).inflate(R.layout.photo_in_liberay_tile, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
            photoHolder.info = infos.get(position);
            if (selectedList.contains(photoHolder.info)) {
                photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_red);
            } else {
                photoHolder.checkMarkView.setBackgroundResource(R.drawable.oval_grey);
            }
            photoHolder.infoView.setText(photoHolder.info.duration + " " + photoHolder.info.size);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(photoHolder.info.filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                photoHolder.imageView.setImageBitmap(bitmap);
            } else {
                photoHolder.imageView.setImageDrawable(new ColorDrawable(Color.BLACK));
            }
        }

        @Override
        public int getItemCount() {
            return infos.size();
        }

    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        public TextView infoView;
        public ImageView imageView;
        public ImageView checkMarkView;

        public VideoInfo info;

        public PhotoViewHolder(final View itemView) {
            super(itemView);
            infoView = (TextView) itemView.findViewById(R.id.info_view);
            infoView.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.video_flag).setVisibility(View.VISIBLE);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = width;
            checkMarkView = (ImageView) itemView.findViewById(R.id.mark_check_view);
            itemView.setSelected(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemView.isSelected()) {
                        selectedList.remove(info);
                        checkMarkView.setBackgroundResource(R.drawable.oval_grey);
                        itemView.setSelected(!itemView.isSelected());
                        supportInvalidateOptionsMenu();
                    } else {
                        if (selectedList.size() < max) {
                            selectedList.add(info);
                            checkMarkView.setBackgroundResource(R.drawable.oval_red);
                            itemView.setSelected(!itemView.isSelected());
                            supportInvalidateOptionsMenu();
                        } else {
//                            Toast.makeText(PickVideoActivity.this, max + " photos at most", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }

    public static void pickVideo(Activity activity) {
        Intent intent = new Intent(activity, PickVideoActivity.class);
        intent.putExtra("max", 1);
        activity.startActivityForResult(intent, INTENT_PICK_VIDEO);
    }

    public static void pickVideoForContent(Activity activity) {
        Intent intent = new Intent(activity, PickVideoActivity.class);
        intent.putExtra("max", 1);
        activity.startActivityForResult(intent, INTENT_ADD_CONTENT_SHORTVIDEO);
    }

    static class VideoInfo {
        String filePath;
        String size;
        String duration;
        String title;
    }

}
