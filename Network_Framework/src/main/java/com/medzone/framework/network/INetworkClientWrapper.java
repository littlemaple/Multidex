package com.medzone.framework.network;


import com.medzone.framework.network.exception.RestException;
import com.medzone.framework.network.serializer.ISerializer;

import java.net.HttpURLConnection;


/**
 * 网络客户端基础属性
 */
public interface INetworkClientWrapper<I, O, C> {

    String TAG = "NETWORK_FRAMEWORK";

    /**
     * 创建一个用于网络请求的客户端。
     *
     * @return An URLConnection for HTTP (RFC 2616) used to send and receive data over the web
     */
    C createClient();

    /**
     * 执行一次网络请求，返回结果将同步返回。通常，我们会使用{@link ISerializer}完成对结果的解析以及包装。
     *
     * @param uri    资源连接，诸如：“/api/contacts/”
     * @param params 封装包括请求实体等。
     * @return 通常，我们会使用{@link ISerializer}完成对结果的解析以及包装。
     * @throws RestException 当请求过程中遇到异常，会使用RestException封装异常结果。
     */
    O call(String uri, NetworkParams.Builder params) throws RestException;

    /**
     * 销毁一个网络请求的客户端，通常他会关闭Client的连接池。
     */
    void destroyClient();

}
