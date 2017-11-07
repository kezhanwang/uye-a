package com.bjzt.uye.fragments;

import android.app.Activity;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.LoginActivity;
import com.bjzt.uye.activity.MainActivity;
import com.bjzt.uye.activity.dialog.DialogKF;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.controller.CacheController;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.controller.OtherController;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.MyInfoHeader;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.views.component.MyInfoHeaderView;
import com.bjzt.uye.views.component.MyInfoItemView;
import com.common.controller.LoginController;
import com.common.file.SharePreLogin;
import com.common.listener.ILoginListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人中心
 * Created by billy on 2017/10/13.
 */

public class FragmentMyInfo extends BaseFragment implements  View.OnClickListener{

    @BindView(R.id.myinfo_header)
    MyInfoHeader mHeader;
    @BindView(R.id.myinfo_headerview)
    MyInfoHeaderView mHeaderView;
    @BindView(R.id.item_clean_cache)
    MyInfoItemView mItemCleanCache;
    @BindView(R.id.item_contact_us)
    MyInfoItemView mItemContactUs;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private final int FLAG_LOGOUT = 10;
    private final int FLAG_HIDE_LOADING = 11;
    private final int FLAG_TOAST_SHOW = 12;

    private DialogKF mDialogKF;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onRightClick() {
                String tips = getResources().getString(R.string.dev_ing);
                showToast(tips);
            }
        });

        mHeaderView.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                if(LoginController.getInstance().isLogin()){
                    IntentUtils.startProfileActivity(getActivity(),MainActivity.REQ_PROFILE);
                }else{
                    IntentUtils.startLoginActivity(getActivity(),MainActivity.REQ_CODE_LOGIN);
                }
            }
        });

        mItemCleanCache.updateType(MyInfoItemView.TYPE_CACHE_CLEAN);
        mItemCleanCache.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                String tips = "缓存清理中...";
                Activity ac = getActivity();
                if(ac != null && ac instanceof  MainActivity){
                    MainActivity mainAc = (MainActivity) ac;
                    mainAc.showLoading(tips);
                }
                Global.postDelay(rCacheClear);
            }
        });
        mItemCleanCache.showBottomLine(true);

        mItemContactUs.updateType(MyInfoItemView.TYPE_CONTACT_US);
        mItemContactUs.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                String strTel = OtherController.getInstance().getKF();
                if(!TextUtils.isEmpty(strTel)){
                    showDialogKF(strTel);
                }else{
                    String tips = getResources().getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }
        });

        btnLogin.setOnClickListener(this);
        LBSController.getInstance().registerListener(mLBSListener);
        LoginController.getInstance().registerListener(mLoginListener);
        refresh();
    }

    private void refresh(){
        boolean isLogin = LoginController.getInstance().isLogin();
        if(isLogin){
            String btnTxt = getResources().getString(R.string.login_logout);
            btnLogin.setText(btnTxt);
            String faceUrl = LoginController.getInstance().getFaceUrl();
            String nickName = LoginController.getInstance().getNickName();
            String strLoc = LBSController.getInstance().getLocStr();
            mHeaderView.setInfo(faceUrl,nickName,strLoc);
        }else{
            String btnTxt = getResources().getString(R.string.login_title);
            btnLogin.setText(btnTxt);
            mHeaderView.setInfo("","游客","位置");
        }
    }

    private ILoginListener mLoginListener = new ILoginListener() {
        @Override
        public void loginSucc() {
            refresh();
        }

        @Override
        public void logout() {
            refresh();
        }
    };

    LBSController.ILBSListener mLBSListener = new LBSController.ILBSListener() {
        @Override
        public void onLBSNotify() {
            refresh();
        }
    };

    private void showDialogKF(final String strTel){
        hideDialogKF();
        final Activity mAc = getActivity();
        if(mAc != null){
            mDialogKF = new DialogKF(mAc,R.style.MyDialogBg);
            mDialogKF.setTel(strTel,strTel);
            mDialogKF.updateType(DialogKF.TYPE_KF);
            mDialogKF.setDialogClickListener(new DialogPicSelect.DialogClickListener() {
                @Override
                public void ItemMiddleClick() {
                    boolean isSucc = IntentUtils.startSysCallActivity(mAc,strTel);
                    if(!isSucc){
                        String tips = getResources().getString(R.string.myinfo_phone_nocall);
                        showToast(tips);
                    }
                }
            });
            mDialogKF.show();
        }
    }

    private void hideDialogKF(){
        if(this.mDialogKF != null){
            this.mDialogKF.dismiss();
            this.mDialogKF = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialogKF();
        LBSController.getInstance().unRegisterListener(mLBSListener);
        LoginController.getInstance().unRegisterListener(mLoginListener);
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnLogin){
            boolean isLogin = LoginController.getInstance().isLogin();
            if(isLogin){
                Activity activity = getActivity();
                if(activity != null && activity instanceof  MainActivity){
                    String tips = getResources().getString(R.string.login_logouting);
                    MainActivity mainAc = (MainActivity) activity;
                    mainAc.showLoading(tips);
                    Global.postDelay(rLogout);
                }
            }else{
                IntentUtils.startLoginActivity(getActivity(), LoginActivity.TYPE_PHONE_VERIFY_CODE, MainActivity.REQ_CODE_LOGIN);
            }
        }
    }

    private Runnable rLogout = new Runnable() {
        @Override
        public void run() {
            LoginController.getInstance().logout();
            Message msg = Message.obtain();
            msg.what = FLAG_LOGOUT;
            sendMsg(msg);
        }
    };

    private Runnable rCacheClear = new Runnable() {
        @Override
        public void run() {
            CacheController.getInstance().clearCache();
            Message msg = Message.obtain();
            msg.what = FLAG_HIDE_LOADING;
            sendMsg(msg);

            msg = Message.obtain();
            msg.what = FLAG_TOAST_SHOW;
            msg.obj = getResources().getString(R.string.myinfo_cache_clear_succ);
            sendMsg(msg);
        }
    };

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        switch(what){
            case FLAG_LOGOUT:
                Activity activity = getActivity();
                if(activity != null && activity instanceof  MainActivity){
                    MainActivity mainAc = (MainActivity) activity;
                    mainAc.hideLoadingDialog();
                }
                LoginController.getInstance().notifyLogout();
                break;
            case FLAG_HIDE_LOADING:
                activity = getActivity();
                if(activity != null && activity instanceof  MainActivity){
                    MainActivity mainAc = (MainActivity) activity;
                    mainAc.hideLoadingDialog();
                }
                break;
            case FLAG_TOAST_SHOW:
                String tips = (String) msg.obj;
                if(!TextUtils.isEmpty(tips)){
                    activity = getActivity();
                    if(activity != null && activity instanceof  MainActivity){
                        MainActivity mainAc = (MainActivity) activity;
                        mainAc.showToast(tips);
                    }
                }
                break;
        }
    }

}
