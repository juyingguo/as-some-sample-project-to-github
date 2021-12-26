package com.ryg.chapter_12;

import java.util.ArrayList;
import java.util.List;

import com.ryg.chapter_12.R;
import com.ryg.chapter_12.loader.ImageLoader;
import com.ryg.chapter_12.utils.MyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnScrollListener {
    private static final String TAG = "MainActivity";

    private List<String> mUrList = new ArrayList<String>();
    ImageLoader mImageLoader;
    private GridView mImageGridView;
    private BaseAdapter mImageAdapter;

    private boolean mIsGridViewIdle = true;
    private int mImageWidth = 0;
    private boolean mIsWifi = false;
    private boolean mCanGetBitmapFromNetWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        mImageLoader = ImageLoader.build(MainActivity.this);
    }

    private void initData() {
        String[] imageUrls = {
                "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
                "https://up.enterdesk.com/23/pic/8a/48/c2/8a48c23ddbbb6b48cd80cb00649c281a.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c0f1f6e9efff2b21192138ac0.jpg",
                "http://b.zol-img.com.cn/desk/bizhi/image/3/960x600/1375841395686.jpg",//old
                "http://picview01.baomihua.com/photos/20120917/m_14_634834710114218750_41852580.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_5aee8f8acbf641fb89f19582a64f4e84.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_2750e9a2bd4e3d110b1f34c8dc1542ec.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_d34ec71e17fa7aa9e8e2ebf36c0c5101.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_2b9c7e60063255ea0100ff4e77a2fa68.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_9a79a0f642786b7310ac35a02a07f285.jpg",
                "http://pic.5tu.cn/uploads/allimg/2112/pic_5tu_big_6383727_37e5cab31d92926f4fd135626dedbf2a.jpg",
                "http://www.renyugang.cn/emlog/content/plugins/kl_album/upload/201004/852706aad6df6cd839f1211c358f2812201004120651068641.jpg"
        };
        for (String url : imageUrls) {
            mUrList.add(url);
        }
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;
        mIsWifi = MyUtils.isWifi(this);
        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }

    private void initView() {
        mImageGridView = (GridView) findViewById(R.id.gridView1);
        mImageAdapter = new ImageAdapter(this);
        mImageGridView.setAdapter(mImageAdapter);
        mImageGridView.setOnScrollListener(this);

        if (!mIsWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("初次使用会从网络下载大概5MB的图片，确认要下载吗？");
            builder.setTitle("注意");
            builder.setPositiveButton("是", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanGetBitmapFromNetWork = true;
                    mImageAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;

        private ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
        }

        @Override
        public int getCount() {
            return mUrList.size();
        }

        @Override
        public String getItem(int position) {
            return mUrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item,parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = holder.imageView;
            final String tag = (String)imageView.getTag();
            final String uri = getItem(position);
            if (!uri.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }
            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
                imageView.setTag(uri);
                mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
            }
            return convertView;
        }

    }

    private static class ViewHolder {
        public ImageView imageView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // ignored
        
    }
}
