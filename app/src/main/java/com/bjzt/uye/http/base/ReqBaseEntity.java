package com.bjzt.uye.http.base;

import com.common.common.NetCommon;
import com.common.entity.PHttpHeader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by billy on 2017/10/12.
 */

public abstract class ReqBaseEntity {

    //请求链接为全链接
    public boolean useAllUrl = false;
    //是否需要cache系统
    public boolean needCache = false;
    //是否需要内存缓存
    public boolean needMemCache = false;
    //Header相关信息
    public PHttpHeader pHeader;
    public long _t;					//请求时间
    //请求是否是jsonArray
    public boolean isJsonArray = false;

    //请求接口域名
    public int interfaceType = NetCommon.NET_INTERFACE_TYPE_UYE;

    /**分页请求**/
    public long begin;
    public int pageSize;
    public int pageReqType = REQ_TYPE_DOWN;

    public static final int REQ_TYPE_UP = 1;	//向上分页请求
    public static final int REQ_TYPE_DOWN = 2;	//向下分页请求

    /**请求序列号**/
    public int seqNo = -1;

    /**获取请求地址**/
    public abstract String getReqUrl();

    /**获取请求数据Data
     * key value形式保存
     * **/
    public abstract Map<String,Object> getReqData();

    /***
     * 数据库Cache generaralID使用
     * @return
     */
    protected String getID(){
        return "";
    }


    /***
     * 对Map进行排序
     * @param mMap
     * @return
     */
    protected List<Map.Entry<String,Object>> sortMap(Map<String,Object> mMap){
        List<Map.Entry<String,Object>> mappingList = new ArrayList(mMap.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String,Object>>(){
            public int compare(Map.Entry<String,Object> mapping1,Map.Entry<String,Object> mapping2){
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });
        return mappingList;
    }

    /***
     * 获取MD5字符串
     * @param mList
     * @return
     */
    protected String getSrcStr(int src,List<Map.Entry<String,Object>> mList){
        StringBuilder builder = new StringBuilder();
        if(mList != null){
            for(int i = 0;i < mList.size();i++){
                Map.Entry<String,Object> map = mList.get(i);
                String key = map.getKey();
                Object val = map.getValue();
                builder.append(key+"=" + val + "&");
            }
        }
        return builder.toString();
    }

}
