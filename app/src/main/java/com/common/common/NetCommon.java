package com.common.common;

/***
 * 网络相关常量
 * @date 2015/06/27
 */
public class NetCommon {

	/** 协议异常**/
	public static final int ERROR_HTTP_CLENT_PROTOCOL_EXCEPTION = 105;
	/** 连接超时 */
    public static final int ERROR_HTTP_CONNECT_TIMEOUT = 102;
    /** 分片接收超时 */
    public static final int ERROR_HTTP_SO_TIMEOUT = 103;
    /** IO异常 */
    public static final int ERROR_HTTP_IO_EXCEPTION = 104;
    /** 其他http连接异常 */
    public static final int ERROR_HTTP_EXCEPTION = 106;
    /** HTTP回包为空异常 */
    public static final int ERROR_HTTP_RESPONSE_NULL = 111;
    /** HTTP解包异常 */
    public static final int ERROR_HTTP_RESPONSE_EXCEPTION = 113;
    /** 服务器发生异常 */
    public static final int ERROR_HTTP_RESPONSE_HTTP_CODE = 112;
    /**服务器发生500异常**/
    public static final int ERROR_HTTP_RESPONSE_HTTP_CODE_500 = 113;

    /**正式域名**/
    public static final int NET_TYPE_INDEX_OFFICAL = 0;
    /**开发域名**/
    public static final int NET_TYPE_INDEX_DEV = 1;
    /**测试域名**/
    public static final int NET_TYPE_INDEX_TEST1 = 2;
    public static final int NET_TYPE_INDEX_TEST3 = 3;
    public static final int NET_TYPE_INDEX_TEST4 = 4;
    public static final int NET_TYPE_PRE = 5;

    //接口类型;
    public static final int NET_INTERFACE_TYPE_KEZHAN = 0;
    public static final int NET_INTERFACE_TYPE_SAAS = 1;
    public static final int NET_INTERFACE_TYPE_LOAN = 2;

}
