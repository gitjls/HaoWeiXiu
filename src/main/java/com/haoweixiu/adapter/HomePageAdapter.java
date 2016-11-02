package com.haoweixiu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoweixiu.R;

import java.util.List;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;

/**
 * 首页
 * Created by jls on 2016/8/22.
 */
public class HomePageAdapter extends BaseAdapter {
    private List<Map> mapList;
    private Context mcontext;
    private LayoutInflater inflater;
    //磁盘缓存
    private DiskLruCache mDiskLruCache;
    //内存缓存
    private LruCache<String,Bitmap> mLrucache;
    public HomePageAdapter(Context context, List<Map> mapList) {
        super();
        this.mcontext = context;
        this.mapList = mapList;
       /* // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mLrucache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        try {
            // 获取图片缓存路径
            File cacheDir = new Utils().getDiskCacheDir(context, "thumb");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            int versionCode=1;
            try {
                versionCode=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            // 创建DiskLruCache实例，初始化缓存数据

            mDiskLruCache = DiskLruCache
                    .create(cacheDir, versionCode, 1, 10 * 1024 * 1024);*/
      /*  } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.list_weijie_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvcname = (TextView) convertView.findViewById(R.id.tvcname);
            viewHolder.tvphoneNumber = (TextView) convertView.findViewById(R.id.tvphoneNumber);
            viewHolder.tvaddress = (TextView) convertView.findViewById(R.id.tvaddress);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.btn_commit = (Button) convertView.findViewById(R.id.btn_commit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvcname.setText("姓名"+ mapList.get(position).get("tvcname"));
        viewHolder.tvphoneNumber.setText( "电话号码" + mapList.get(position).get("tvphoneNumber"));
        viewHolder.tvaddress.setText("住址" + mapList.get(position).get("tvaddress"));
        viewHolder.tv_type.setText("型号"+ mapList.get(position).get("tv_type"));
        return convertView;
    } 
    class ViewHolder {
        TextView tvcname;
        TextView tvphoneNumber;
        TextView tvaddress;
        TextView tv_type;
        ImageView iv_image;
        Button btn_commit;
    }
}
