package com.haoweixiu.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 图片缓存类
 */
public class MyLruCache {
    private static MyLruCache lruCache;
    private LruCache<String,Bitmap> bitmapLruCache;
    // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
// LruCache通过构造函数传入缓存值，以KB为单位。这里设置为占用可用内存的1/8
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    public MyLruCache() {
        bitmapLruCache = new LruCache<>(maxMemory/8);
    }
    public static MyLruCache getInstance(){
        if(lruCache==null){
            lruCache=new MyLruCache();
        }
        return lruCache;
    }
    //清除缓存
    public void clearCache(){
        if (bitmapLruCache != null) {
            if (bitmapLruCache.size() > 0) {
                bitmapLruCache.evictAll();
            }
            bitmapLruCache = new LruCache<>(maxMemory/8);
        }
    }
    //添加进缓存列表
    public void addBitmapCache(String key,Bitmap bitmap){
        bitmapLruCache.put(key,bitmap);
    }
    public Bitmap getBitmapLruCache(String key){
        return bitmapLruCache.get(key);
    }
}
