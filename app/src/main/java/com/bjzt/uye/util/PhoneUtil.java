package com.bjzt.uye.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.bjzt.uye.entity.PhoneUserEntity;
import com.common.common.MyLog;

import java.util.ArrayList;
import java.util.List;

/***
 * @date电话相关工具类
 * 1.读取手机通讯录
 */
public class PhoneUtil {
	private static final String TAG = "PhoneUtil";

	public static synchronized final List<PhoneUserEntity> listAllPhoneUserV2(Context mContext){
		List<PhoneUserEntity> mList = new ArrayList<PhoneUserEntity>();
//		Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
		ContentResolver resolver = mContext.getContentResolver();
		// 获得_id属性
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
		try {
			if(cursor != null && cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					// 获得id并且在data中寻找数据
					String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if(Integer.parseInt(hasPhone) > 0){
							PhoneUserEntity entity = new PhoneUserEntity();
							Cursor pCur = resolver.query(
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
									new String[]{id}, null);

							if(pCur != null){
								while (pCur.moveToNext()) {
									String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									entity.Phones.add(phoneNo);
								}
							}

							Cursor pCurEmail = resolver.query(
									ContactsContract.CommonDataKinds.Email.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?",
									new String[]{id}, null);
							if(pCurEmail != null){
								while (pCurEmail.moveToNext()) {
									String email = pCurEmail.getString(pCurEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
									entity.Email.add(email);
								}
							}

							entity.LastName = name;

							if(pCur != null){
								pCur.close();
							}

							if(pCurEmail != null){
								pCurEmail.close();
							}
							mList.add(entity);
						}

	//				// data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
	//				Cursor cursor2 = resolver.query(uri, new String[] {Data.DATA1,Data.MIMETYPE }, null, null, null);
	//				while (cursor2.moveToNext()) {
	//
	//					String data = cursor2.getString(cursor2.getColumnIndex("data1"));
	//					if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){ // 如果是名字
	//						entity.LastName = data;
	//					} else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) { // 如果是电话
	//						entity.Phones.add(data);
	//					} else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")) { // 如果是email
	//						entity.Email.add(data);
	//					} else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")) { // 如果是地址
	////						buf.append(",address=" + data);
	//					} else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")) { // 如果是组织
	////						buf.append(",organization=" + data);
	//					}
	//				}
	//				if(cursor2 != null && !cursor2.isClosed()){
	//					cursor2.close();
	//				}
				}//while
				if(!cursor.isClosed()){
					cursor.close();
				}
			}
		} catch (NumberFormatException ee) {
			MyLog.error(TAG,ee);
		}catch(Exception ee){
			MyLog.error(TAG,ee);
		}

		if(MyLog.isDebugable()){
			MyLog.debug(TAG, "listAllPhoneUserV2:"+mList.size());
		}
		return mList;
	}


	/***
	 * 获取选择的user实体类
	 * @param data
	 * @param mContext
	 * @return
	 */
	public static final PhoneUserEntity getSelectPhoneUser(Intent data, Activity mContext){
		PhoneUserEntity mEntity = new PhoneUserEntity();
		List<String> rList = new ArrayList<String>();
		if(data != null){
			Uri contactData = data.getData();
			if(mContext != null){
				Cursor cursor =  mContext.managedQuery(contactData, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String contact = cursor.getString(nameFieldColumnIndex);
					//init name
					mEntity.LastName = contact;
					int hasPhoneNumber = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
					String strHas = cursor.getString(hasPhoneNumber);
					int indexID  = cursor.getColumnIndex(ContactsContract.Contacts._ID);
					String contactId = cursor.getString(indexID);
					if(!TextUtils.isEmpty(strHas) && strHas.equalsIgnoreCase("1")){
						Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " = " + contactId, null, null);
						while(phones != null && phones.moveToNext()){
							String phoneNumber= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							rList.add(phoneNumber);
						}
					}
				}
			}
		}
		mEntity.Phones = rList;
		return mEntity;
	}

	/***
	 * 通过系统通讯录获取电话号码
	 * @param data
	 * @param mContext
     * @return
     */
	public static final PhoneUserEntity getPhonesByDetetail(Intent data, Activity mContext){
		List<String> mList = new ArrayList<>();
		PhoneUserEntity mEntity = new PhoneUserEntity();
		if(data != null){
			Uri uri = data.getData();
			ContentResolver contentResolver = mContext.getContentResolver();
			if(contentResolver != null){
				Cursor cursor = contentResolver.query(uri,null, null, null, null);
				if(cursor != null){
					while(cursor.moveToNext()){
						String num = cursor.getString(cursor.getColumnIndex("data1"));
						mList.add(num);
					}
					cursor.close();
				}
			}
		}
		mEntity.Phones = mList;
		return mEntity;
	}
}
