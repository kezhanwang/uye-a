package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.LoanDateUtil;
import com.bjzt.uye.util.StrUtil;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/31.
 */
public class ExperiItemView extends BaseItemView<PExperiEntity> implements View.OnClickListener, NoConfusion{
    private PExperiEntity mEntity;

    @BindView(R.id.txt_time)
    TextView mTxtTitle;
    @BindView(R.id.txt_contents)
    TextView mTxtContent;

    private IItemListener mListener;

    public ExperiItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PExperiEntity pExperiEntity) {
        this.mEntity = pExperiEntity;
        int type = this.mEntity.type;
        String strInfo = "";
        String strTitle = getTitle();
        mTxtTitle.setText(strTitle);
        if(type == ReqExperiListEntity.TYPE_DEGREE){
            strInfo = this.mEntity.school_name;
        }else if(type == ReqExperiListEntity.TYPE_OCC){
            strInfo = this.mEntity.work_name;
        }
        mTxtContent.setText(strInfo);
    }

    private String getTitle(){
        String title = "";
        title = StrUtil.getTimeShowStyle(this.mEntity.date_start) + " ~ " + StrUtil.getTimeShowStyle(this.mEntity.date_end);
        return title;
    }

    @Override
    public PExperiEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.expri_itemview_layout,this,true);
        ButterKnife.bind(this);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onItemClick(this.mEntity,-1);
        }
    }
}
