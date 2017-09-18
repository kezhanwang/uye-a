package com.common.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;

import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MyLog;
import com.common.common.NetCommon;
import com.common.controller.LoginController;
import com.common.entity.PHttpHeader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/****
 * Http相关工具类
 * @date 2016/06/27
 */
public class HttpUtils {

    public static final String TAG = "HttpUtils";

    private static byte netByte = (byte) -1;

    // 未知网络类型
    static final byte NET_TYPE_UNKNOWN = (byte) 99;

    // wap类网络但是具体是哪个wap不清楚
    static final byte NET_TYPE_WAP = (byte) 98;

    // 默认连接超时时间
    static final int DEFAULT_CONNECTION_TIMEOUT = 20;

    // 默认两个数据分片之间超时
    static final int DEFAULT_SO_TIMEOUT = 45;

    // 默认数据分片大小
    static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

    public static final byte APN_WIFI = 1;

    public static final byte APN_CMWAP = 4;

    public static final byte APN_CMNET = 5;

    public static final byte APN_UNIWAP = 6;

    public static final byte APN_UNINET = 7;

    public static final byte APN_3GWAP = 8;

    public static final byte APN_3GNET = 9;

    public static final byte APN_CTWAP = 10;

    public static final byte APN_CTNET = 11;
    
    static final HashMap<Integer, Byte> NET_TYPES = new HashMap<Integer, Byte>();

    private static long mReleaseTime;
    
    static {
        NET_TYPES.put(NetWorkUtils.APN_WIFI, APN_WIFI);
        NET_TYPES.put(NetWorkUtils.APN_CMWAP, APN_CMWAP);
        NET_TYPES.put(NetWorkUtils.APN_CMNET, APN_CMNET);
        NET_TYPES.put(NetWorkUtils.APN_UNIWAP, APN_UNIWAP);
        NET_TYPES.put(NetWorkUtils.APN_UNINET, APN_UNINET);
        NET_TYPES.put(NetWorkUtils.APN_3GWAP, APN_3GWAP);
        NET_TYPES.put(NetWorkUtils.APN_3GNET, APN_3GNET);
        NET_TYPES.put(NetWorkUtils.APN_CTWAP, APN_CTWAP);
        NET_TYPES.put(NetWorkUtils.APN_CTNET, APN_CTNET);
    }

    protected static void refreshNetByte() {
        synchronized (NET_TYPES) {
            final int apn = NetWorkUtils.getNetworkType(Global.getContext());
            if (NET_TYPES.containsKey(apn)) {
                netByte = NET_TYPES.get(apn);
            } else {
                if (NetWorkUtils.isWap()) {
                    netByte = NET_TYPE_WAP;
                } else {
                    netByte = NET_TYPE_UNKNOWN;
                }
            }
        }
    }

    public static byte getNetByte() {
        if (netByte == (byte) -1) {
            refreshNetByte();
        }
        synchronized (NET_TYPES) {
            return netByte;
        }
    }

    public static String getApnName() {
        return NetWorkUtils.getNetworkName(Global.getContext());
    }

    public static boolean isNetworkActive() {
        return NetWorkUtils.isNetworkActive();
    }

    public static boolean isWifi() {
        return getNetByte() == (byte) 1;
    }
    
    /***
     * Get方式获取数据                                                                                                                                                                         
     * @param url
     * @param host
     * @param entity
     * @return
     */
    public static byte[] getJson(String url, int interfaceType, String host, HttpEntity entity){
    	 final DefaultHttpClient client = getApiHttpClient();
    	 String refer = HttpEngine.getInstance().getRefer(interfaceType);
    	 HttpGet httpGet = new HttpGet(refer + url);
         httpGet.addHeader("Accept-Language", "zh-cn");
         httpGet.addHeader("User-Agent", Global.getQUA());
         try {
             HttpResponse response = client.execute(httpGet);
             return decodeResponse(url, response,null);
         }catch(Exception ee){
        	 MyLog.error(TAG, ee);
        	 return new byte[]{NetCommon.ERROR_HTTP_CLENT_PROTOCOL_EXCEPTION};
         }
    }
    
    /***
     * 带有文件上传的接口
     * @param url
     * @param entity
     * @return
     * @throws Exception
     */
    public static byte[] postJsonV2(String url, int interfaceType, MultipartEntity entity, boolean isForceUse, PHttpHeader pHeader) throws Exception {
        final DefaultHttpClient client = getApiHttpClient();
        //每次请求client的cookieStore，免得服务器的cookie覆盖了客户端的cookie
        client.getCookieStore().clear();
        String refer = HttpEngine.getInstance().getRefer(interfaceType);
        HttpPost httpPost = null;
        if(isForceUse){
        	httpPost = new HttpPost(url);
        }else{
        	httpPost = new HttpPost(refer + url);
        }
     	httpPost.addHeader("Referer",refer);
//        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.addHeader("charset", HTTP.UTF_8);
        httpPost.addHeader("kzua",Global.getQUA());
        String ua = System.getProperty("http.agent");
        httpPost.addHeader("User-Agent",ua);
        String loginStatus = getLoginStatus();
        if (!TextUtils.isEmpty(loginStatus)) {
            httpPost.setHeader("Cookie", loginStatus);
        }
        if (entity != null) {
            httpPost.setEntity(entity);
        }
        {
        	try {

                // https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
                // 2.5. Connection eviction policy
                // resolve the problem which throw SocketException caused by the server side close the connections;
                long time = System.currentTimeMillis();
                if(time - mReleaseTime > 30*1000) {
                    // Close expired connections
                    client.getConnectionManager().closeExpiredConnections();
                    // Optionally, close connections
                    // that have been idle longer than 300 sec
                    client.getConnectionManager().closeIdleConnections(300, TimeUnit.SECONDS);

                    mReleaseTime = time;
                }

                HttpResponse response = client.execute(httpPost);
                return decodeResponse(url, response,pHeader);
            } catch (ClientProtocolException e) {
                // 其他异常，记录上报
            	if(MyLog.isDebugable()){
            		MyLog.error(TAG,e);
            	}
                return new byte[]{NetCommon.ERROR_HTTP_CLENT_PROTOCOL_EXCEPTION};
            } catch (IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    // 连接超时，记录上报
                    return new byte[]{NetCommon.ERROR_HTTP_CONNECT_TIMEOUT};
                } else if (e instanceof SocketTimeoutException) {
                    // 分片接收超时，记录上报
                    return new byte[]{NetCommon.ERROR_HTTP_SO_TIMEOUT};
                } else {
                    return new byte[]{NetCommon.ERROR_HTTP_SO_TIMEOUT};
                }
            } catch (Exception e) {
            	if(MyLog.isDebugable()){
            		MyLog.error(TAG,e);
            	}
                return new byte[]{NetCommon.ERROR_HTTP_EXCEPTION};
            } finally {
                //            client.getConnectionManager().shutdown();
            }
        }//while
    }
    
    
    /****
     * Post方式获取数据
     * @param url
     * @param entity
     * @return
     * @throws Exception
     */
    public static byte[] postJson(String url, int interfaceType, HttpEntity entity) throws Exception {
        final DefaultHttpClient client = getApiHttpClient();
        //每次请求client的cookieStore，免得服务器的cookie覆盖了客户端的cookie
        client.getCookieStore().clear();
        String refer = HttpEngine.getInstance().getRefer(interfaceType);
        HttpPost httpPost = new HttpPost(refer + url);
        httpPost.addHeader("Referer",refer);
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.addHeader("User-Agent", Global.getQUA());
        
        String loginStatus = getLoginStatus();
        if (!TextUtils.isEmpty(loginStatus)) {
            httpPost.setHeader("Cookie", loginStatus);
        }
        if (entity != null) {
            httpPost.setEntity(entity);
        }
        int cnt = 0;
        {
        	try {

                // https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
                // 2.5. Connection eviction policy
                // resolve the problem which throw SocketException caused by the server side close the connections;
                long time = System.currentTimeMillis();
                if(time - mReleaseTime > 30*1000) {
                    // Close expired connections
                    client.getConnectionManager().closeExpiredConnections();
                    // Optionally, close connections
                    // that have been idle longer than 600 sec
                    client.getConnectionManager().closeIdleConnections(300, TimeUnit.SECONDS);

                    mReleaseTime = time;
                }

                HttpResponse response = client.execute(httpPost);
                return decodeResponse(url, response,null);
            } catch (ClientProtocolException e) {
                // 其他异常，记录上报
                return new byte[]{NetCommon.ERROR_HTTP_CLENT_PROTOCOL_EXCEPTION};
            } catch (IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    // 连接超时，记录上报
                    return new byte[]{NetCommon.ERROR_HTTP_CONNECT_TIMEOUT};
                } else if (e instanceof SocketTimeoutException) {
                    // 分片接收超时，记录上报
                    return new byte[]{NetCommon.ERROR_HTTP_SO_TIMEOUT};
                } else {
                    return new byte[]{NetCommon.ERROR_HTTP_IO_EXCEPTION};
                }
            } catch (Exception e) {
                return new byte[]{NetCommon.ERROR_HTTP_EXCEPTION};
            } finally {
                //            client.getConnectionManager().shutdown();
            }
        }//while
    }

    /***
     * 登录身份信息
     * @return
     */
    public static String getLoginStatus(){
    	String cookie = LoginController.getInstance().getCookie();
    	if(MyLog.isDebugable()){
    		MyLog.debug(TAG,"[getLoginStatus]" + " cookie:" + cookie);
    	}
    	return cookie;
   	}

    private static DefaultHttpClient mImageHttpClient;
    
    private static Object mImageHttpClientLock = new Object();
    
    private static DefaultHttpClient getImageHttpClient(){
        synchronized (mImageHttpClientLock) {
            if(mImageHttpClient == null){
                mImageHttpClient = getAutoHttpClient();
            }
            return mImageHttpClient;
        }
    }
    
    private static DefaultHttpClient mApiHttpClient;
    
    private static Object mApiHttpClientLock = new Object();
    
    private static DefaultHttpClient getApiHttpClient(){
        synchronized (mApiHttpClientLock) {
            if(mApiHttpClient == null){
                mApiHttpClient = getAutoHttpClient();
            }
            return mApiHttpClient;
        }
    }

    /**
     * 根据网络类型自动进行超时设置
     * 
     * @return
     */
    private static DefaultHttpClient getAutoHttpClient() {
        int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        int soTimeout = DEFAULT_SO_TIMEOUT;
        int bufferSize = DEFAULT_SOCKET_BUFFER_SIZE;
        final byte apn = getNetByte();
        switch (apn) {
            case 1: // wifi环境
                connectionTimeout = 5;
                soTimeout = 30;
                break;
            case 4:
            case 5:
            case 6:
            case 7: // 2g网络
                connectionTimeout = 15;
                soTimeout = 45;
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                connectionTimeout = 10;
                soTimeout = 40;
                break;
            default:
                // 未知网络类型，给用默认超时设置
        }
        return getHttpClient(connectionTimeout, soTimeout, bufferSize);
    }

    /**
     * 获取HttpClient对象
     * 
     * @param connectionTimeout
     *            连接超时时间 单位：秒
     * @param soTimeout
     *            数据获取超时时间 单位：秒
     * @return
     */
    private static DefaultHttpClient getHttpClient(int connectionTimeout, int soTimeout, int bufferSize) {
        // Shamelessly cribbed from AndroidHttpClient
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(50));
        ConnManagerParams.setTimeout(params, 30000);
        

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 10 seconds. Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout * 1000);
        HttpConnectionParams.setSoTimeout(params, soTimeout * 1000);
        HttpConnectionParams.setSocketBufferSize(params, bufferSize);

        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes);
        DefaultHttpClient client = new DefaultHttpClient(ccm, params);

        // -----for proxy
        String defaultHost = Proxy.getDefaultHost();
        int defaultPort = Proxy.getDefaultPort();
        ConnectivityManager connectivityManager = (ConnectivityManager) Global.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        int type = activeNetworkInfo == null ? 0 : activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE && defaultHost != null && defaultPort != -1) {
            HttpHost proxy = new HttpHost(defaultHost, defaultPort);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        // -----for proxy end

        return client;
    }

    /**
     * 获取网络连接
     * 
     * @return
     */
    public static DefaultHttpClient getHttpClient() {
        return getHttpClient(10, 40, DEFAULT_SOCKET_BUFFER_SIZE);
    }

    private static byte[] decodeResponse(String url, HttpResponse response, PHttpHeader pHeader) {
        if (response == null) {
            return new byte[] { NetCommon.ERROR_HTTP_RESPONSE_NULL };
        }
        final int code = response.getStatusLine().getStatusCode();
        if (code == 200) {
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                //get header
                if(pHeader != null){
                    Header[] headers = response.getHeaders(pHeader.key);
                    if(headers != null && headers.length > 0){
                        for(int i = 0;i < headers.length;i++){
                            Header header = headers[i];
                            String val = header.getValue();
                            if(val.contains(pHeader.key_kz_vp)){
                                pHeader.kz_vp = val;
                                if(MyLog.isDebugable()){
                                    MyLog.debug(TAG,"[decodeResponse]" + " kz_vp:" + val);
                                }
                            }
                        }
                    }
                }
                //end header
                is = entity.getContent();
                baos = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len = 0;
                boolean firstReq = true;
                int totalReader = 0;
                while ((len = is.read(buff, 0, buff.length)) != -1) {
                    totalReader = totalReader + len;
                    baos.write(buff, 0, len);
                }
                baos.flush();
                return baos.toByteArray();
            } catch (Exception e) {
                return new byte[] { NetCommon.ERROR_HTTP_RESPONSE_EXCEPTION };
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        MyLog.error(TAG,e);
                    }
                }
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(entity != null){
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if(code == 500){
            return new byte[] { NetCommon.ERROR_HTTP_RESPONSE_HTTP_CODE_500 };
        }else{
            return new byte[] { NetCommon.ERROR_HTTP_RESPONSE_HTTP_CODE };
        }
    }
}
