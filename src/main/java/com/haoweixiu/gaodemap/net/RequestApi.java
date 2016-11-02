package com.haoweixiu.gaodemap.net;

public interface RequestApi {
    //地图订单接口
    int map_order(String keys, Object obj, RequestListener listener);
    //
    int no_receive_order(String keys, String did, Object obj, RequestListener listener);
    //
    int received_order(String keys, String did, Object obj, RequestListener listener);

}
