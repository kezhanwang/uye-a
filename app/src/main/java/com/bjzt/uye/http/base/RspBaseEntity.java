package com.bjzt.uye.http.base;

import com.common.common.MyLog;
import com.common.common.NetCommon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/***
 * 回复包的基类
 * @data 2015/06/27
 */
public abstract class RspBaseEntity implements Serializable {
	protected final String TAG = getClass().getSimpleName();
	
	/**返回错误码**/
	public int code;
	/**返回错误消息提示**/
	public String msg;
	/**返回的数据域信息**/
	public JSONObject data;
	/**是否返回正确**/
	public boolean isSucc;
	//请回序列号,客户端自定义使用
	public int seqNo;
	/*时间*/
	public long time;
	/**数据的来源 net cache mem**/
	public int src;
	/**token信息**/
	public String token;

	public RspBaseEntity() {
		// TODO Auto-generated constructor stub
	}
	
	/***
	 * 框架改造V2调用该方法
	 * @param jsonObj
	 * @param seqNo
	 * @return
	 */
	public boolean preParseV2(JSONObject jsonObj, int seqNo, boolean isJsonArray, int interfaceType, ReqBaseEntity reqEntity){
		this.seqNo = seqNo;
		try {
			try{
				this.code = jsonObj.getInt("code");
			}catch(JSONException e){
				MyLog.error(TAG, e);
				this.code = NetCommon.ERROR_CODE_CODE_EXCEPTION;
			}
			if(interfaceType == NetCommon.NET_INTERFACE_TYPE_UYE){
				this.isSucc = true;
				if(!isJsonArray){
					if(this.code == NetCommon.ERROR_CODE_SUCC){
						try{
							JSONObject obj = jsonObj.getJSONObject("data");
							parseData(obj,null,false,reqEntity);
						}catch(JSONException ee){
							if(MyLog.isJsonDebugable()){
								MyLog.error(TAG, ee);
							}
						}catch(Exception ee){
							if(MyLog.isJsonDebugable()){
								MyLog.error(TAG,ee);
							}
						}
					}
				}else{
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					parseData(null,jsonArray,true,reqEntity);
				}
				//init time key
				if(jsonObj.has("timestamp") && !jsonObj.isNull("timestamp")){
					this.time = jsonObj.getLong("timestamp");
				}
				//init msg
				if(jsonObj.has("msg")){
					this.msg = jsonObj.getString("msg");
				}
				//init token
				if(jsonObj.has("token")){
					this.token = jsonObj.getString("token");
				}
			}else{
				if(jsonObj.has("msg")){
					this.msg = jsonObj.getString("msg");
				}
			}
		} catch (JSONException ee) {
			MyLog.error(TAG, ee);
		}
		//返回是否成功
		return this.isSucc;
	}

	public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity){};
}
