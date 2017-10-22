package com.bjzt.uye.photo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.photo.adapter.LoanAdapterPhotoAlbum;
import com.bjzt.uye.photo.entity.LoanVAblumItemEntity;
import com.bjzt.uye.photo.listener.LoanIAblumBtnListener;
import com.bjzt.uye.photo.view.LoanAblumButtonArea;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.thread.ThreadPool;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * 自定义本地相册
 * 支持图片多选效果
 * @date 2015/08/23
 */
public class LoanPhotoAlblumActivity extends BaseActivity{
	private int maxSize;
	private ArrayList<String> mSelectList;
	private YHeaderView mHeader;
	private GridView mGirdView;
	private LoanAdapterPhotoAlbum mAdapter;
	private final int FLAG_SET_LIST = 0x100;
	private LoanAblumButtonArea btnArea;
	private ArrayList<LoanVAblumItemEntity> mSelect = new ArrayList<LoanVAblumItemEntity>();
	private final int REQUEST_CODE_PIC_SCANE = 0x200;
	
	@Override
	protected int getLayoutID() {
		return R.layout.loan_photo_ablum_activity;
	}

	private void delayTask(){
		ThreadPool.getInstance().submmitJob(rLoad);
	}

	protected void initLayout(){
		mHeader = (YHeaderView) this.findViewById(R.id.header);
		String str = getResources().getString(R.string.loan_alblum);
		mHeader.setTitle(str);
		mHeader.updateType(YHeaderView.TYPE_ABOUT);
		mHeader.setIListener(new IHeaderListener() {
			@Override
			public void onLeftClick() {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});
		this.btnArea = (LoanAblumButtonArea) this.findViewById(R.id.btn_area);
		this.btnArea.setIAblumBtnListener(mAblumListener);

		this.mGirdView = (GridView) this.findViewById(R.id.grid_view);
		this.mGirdView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				if(scrollState == SCROLL_STATE_IDLE){
					mAdapter.onPageStop(listView);
					mAdapter.setPageFlag(true);
				}else{
					mAdapter.setPageFlag(false);
				}
			}
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {}
		});
//		this.mGirdView.setColumnWidth(columnWidth);

		delayTask();
	}

	@Override
	protected void initExtras(Bundle bundle) {
		Intent intent = getIntent();
		maxSize = intent.getIntExtra(IntentUtils.PARA_KEY_SIZE, MConfiger.MAX_SIZE_PIC_ABLUEM_SIZE);
		mSelectList = intent.getStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC);
	}

	private void checkDataGoBack(){
		Intent intent = new Intent();
		intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,mSelect);
		setResult(Activity.RESULT_OK,intent);
	}
	
	private LoanIAblumBtnListener mAblumListener = new LoanIAblumBtnListener(){
		@Override
		public void picScane() {
			//scane select pic
			if(mSelect != null && mSelect.size() > 0){
				ArrayList<String> mList = new ArrayList<String>();
				for(int i = 0;i < mSelect.size();i++){
					LoanVAblumItemEntity vEntity = mSelect.get(i);
					mList.add(vEntity.url);
				}
				IntentUtils.startPicScanAblueActivity(LoanPhotoAlblumActivity.this,REQUEST_CODE_PIC_SCANE,mList,0);
			}
		}

		@Override
		public void btnOk() {
			// TODO Auto-generated method stub
			checkDataGoBack();
			finish();
		}
		
		@Override
		public void itemClick(int pos) {
			LoanVAblumItemEntity entity = (LoanVAblumItemEntity) mAdapter.getItem(pos);
			if(!entity.isSelect){
				int size = mSelect.size();
				if(size >= maxSize){
					String str = getResources().getString(R.string.loan_pic_scanne_tips_tips_more,size);
					showToast(str);
					return;
				}
			}
			entity.isSelect = !entity.isSelect;
			mAdapter.notifyDataSetChanged();
			if(entity.isSelect){
				mSelect.add(entity);
			}else{
				mSelect.remove(entity);
			}
			refreshBtn();
		};
	};
	
	private void refreshBtn(){
		int size = mSelect.size();
		if(size > 0){
			btnArea.setBtnEnable(true);
		}else{
			btnArea.setBtnEnable(false);
		}
		btnArea.setButtonTxt(size);
	}
	
	private Runnable rLoad = new Runnable() {
		@Override
		public void run() {
			String[] array = listAllImgs();
			List<LoanVAblumItemEntity> mList = new ArrayList<LoanVAblumItemEntity>();
			for(int i = array.length - 1;i >= 0;i--){
				String path = array[i];
				if(!TextUtils.isEmpty(path)){
					File file = new File(path);
					if(file.exists()){
						LoanVAblumItemEntity entity = new LoanVAblumItemEntity();
						entity.isSelect = false;
						entity.url = path;
						mList.add(entity);
					}
				}
			}
			Message msg = Message.obtain();
			msg.what = FLAG_SET_LIST;
			msg.obj = mList;
			sendMsg(msg);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThreadPool.getInstance().removeJob(rLoad);
	};
	
	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		int what = msg.what;
		if(what == FLAG_SET_LIST){
			List<LoanVAblumItemEntity> mList = (List<LoanVAblumItemEntity>) msg.obj;
			mAdapter = new LoanAdapterPhotoAlbum(mList);
			mAdapter.setIAblumeListener(mAblumListener);
			mGirdView.setAdapter(mAdapter);
		}
	}

	 /***
     * 新加入的接口，罗列所有本地的图片，按照时间排序
     * @return
     */
    protected String[] listAllImgs(){
    	final String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
    	//order by date modified
        final String orderBy = MediaStore.Images.Media.DATE_MODIFIED;
        //Stores all the images from the gallery in Cursor
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        //Total number of images
        //对于一些机型，插上usb线获取不到图片资源，返回的cursor为空
        if(null != cursor){
            int count = cursor.getCount();
            //Create an array to store path to all the images
            String[] arrPath = new String[count];
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //Store the path of the image
                arrPath[i]= cursor.getString(dataColumnIndex);
            }
            return arrPath;
        }else{
            return new String[]{};
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == REQUEST_CODE_PIC_SCANE){
    		if(resultCode != Activity.RESULT_OK){
    			return;
    		}
    		ArrayList<String> resultList = data.getStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC);
    		if(resultList != null){
    			if(resultList.size() != mSelect.size()){
    				List<String> mDelList = new ArrayList<String>();
    				for(int i = 0;i < mSelect.size();i++){
    					LoanVAblumItemEntity vEntity = mSelect.get(i);
    					String url = vEntity.url;
    					if(!resultList.contains(url)){
    						mDelList.add(url);
    						mSelect.remove(vEntity);
    						i--;
    					}
    				}
    				mAdapter.setPicFlagFalse(mDelList);
    				refreshBtn();
    			}
    		}
    	}
    }
}
