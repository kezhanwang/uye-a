package com.bjzt.uye.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.download.DownloadController;
import com.common.download.IDownloadListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import java.io.File;
import butterknife.BindView;

/**
 * 打开PDFUI
 * Created by billy on 2017/12/12.
 */

public class PDFActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.pdf_view)
    PDFView pdfView;

    private String mTitle;
    private String mUrl;

    private final int FLAG_OPEN_PDF = 0x10;
    private final int FLAG_HIDE_LOADING = 0x11;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pdf_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        if(!TextUtils.isEmpty(mTitle)){
            mHeader.setTitle(mTitle);
        }

        showLoading();
        DownloadController.getInstance().setListener(mListener);
        DownloadController.getInstance().startDownloadFile(mUrl);
    }

    private IDownloadListener mListener = new IDownloadListener() {
        @Override
        public void downloadError(String tips) {
            Message msg = Message.obtain();
            msg.what = FLAG_HIDE_LOADING;
            sendMsg(msg);
        }

        @Override
        public void downloadSucc(String filePath) {
            Message msg = Message.obtain();
            msg.what = FLAG_HIDE_LOADING;
            sendMsg(msg);

            msg = Message.obtain();
            msg.what = FLAG_OPEN_PDF;
            msg.obj = filePath;
            sendMsgDelay(msg,800);
        }

        @Override
        public void downloading(int process) {
            String tips = "下载中" + process + "%";
            setLoadingTips(tips);
        }
    };


    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        int what = msg.what;
        switch(what){
            case FLAG_OPEN_PDF:
                String filePath = (String) msg.obj;
                if(!TextUtils.isEmpty(filePath)){
                    File file = new File(filePath);
                    if(file != null && file.exists()){
                        pdfView.setVisibility(View.VISIBLE);
                        pdfView.fromFile(file)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .onPageChange(onPageChangeListener)
                                .load();
                    }
                }
                break;
            case FLAG_HIDE_LOADING:
                hideLoadingDialog();
                break;
        }
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {

        }
    };

    @Override
    protected void initExtras(Bundle bundle) {
        this.mTitle = bundle.getString(IntentUtils.PARA_KEY_TITLE);
        this.mUrl = bundle.getString(IntentUtils.PARA_KEY_URL);
    }
}
