package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.util.LoanDateUtil;
import com.bjzt.uye.util.TimeUtils;
import com.bjzt.uye.views.component.datepicker.adapter.NumericWheelAdapter;
import com.bjzt.uye.views.component.datepicker.view.LoanOnWheelScrollListener;
import com.bjzt.uye.views.component.datepicker.view.LoanWheelView;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yas on 2016/3/1.
 */
public class DialogDateSelector extends Dialog implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    public static final int TYPE_ADD_COURSE_DATE=1;//选择上课日期
    public static final int TYPE_WORK_DESC=2;   //职业
    public static final int TYPE_EDU_DATE=3;    //选择毕业时间
    public static final int TYPE_OPEN_COURSE_TIME=4;    //选择开课时间
    public static final int TYPE_TIME_INROLLYEAR = 5;       //入学年份

    private final int START_YEAR = 1980;
    private TextView btnOk;
    private TextView txtTitle;
    private TextView txtCancel;
    private LoanWheelView mWheelFirst;
    private LoanWheelView mWheelSecond;
    private LoanWheelView mWheelThird;
    private IDatePickerListener mListener;
    private NumericWheelAdapter mAdapterFirst=null;
    private NumericWheelAdapter mAdapterSecond=null;
    private NumericWheelAdapter mAdapterThird=null;
    private int mType;
    private int currentYear= Calendar.getInstance().get(Calendar.YEAR);
    private int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
    private int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int deadYear;
    private int deadMonth;
    private int deadDay;
    int start_year;

    private final int OFFSET_YEAR = 30;
    private final int OFFSET_YEAR_17 = 17;

    public DialogDateSelector(Context context) {
        super(context);
        init();
    }

    public DialogDateSelector(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected DialogDateSelector(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.loan_popwin_animation);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView=li.inflate(R.layout.loan_my_time_selector_dialog, null);

        btnOk= (TextView) mView.findViewById(R.id.text_ok);
        btnOk.setOnClickListener(this);
        txtTitle= (TextView) mView.findViewById(R.id.text_title);
        txtCancel= (TextView) mView.findViewById(R.id.text_cancel);
        txtCancel.setOnClickListener(this);
        mWheelFirst= (LoanWheelView) mView.findViewById(R.id.wheel_first);
        mWheelSecond= (LoanWheelView) mView.findViewById(R.id.wheel_second);
        mWheelThird= (LoanWheelView) mView.findViewById(R.id.wheel_third);
        mWheelFirst.setDrawShadows(true);
        mWheelSecond.setDrawShadows(true);
        mWheelThird.setDrawShadows(true);
        int mWidth = DeviceUtil.mWidth;
        int mHeight = (int) getContext().getResources().getDimension(R.dimen.loan_datepicker_height);
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, mHeight);
        setContentView(mView, llp);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void updateType(int type){
        mType=type;
        initDate(type);
    }
    private void initDate(int type){
        switch (type){
            case TYPE_ADD_COURSE_DATE:
                int start = currentYear;
                int end = currentYear + 60;
                mAdapterFirst=new NumericWheelAdapter(getContext(),start,end);
                mAdapterFirst.setLabel("年");
                mWheelFirst.setViewAdapter(mAdapterFirst);
                mWheelFirst.addScrollingListener(mYScrollListener);

                mAdapterSecond=new NumericWheelAdapter(getContext(),1,12);
                mAdapterSecond.setLabel("月");
                mWheelSecond.setViewAdapter(mAdapterSecond);
                mWheelSecond.addScrollingListener(mYScrollListener);
                mWheelSecond.setCurrentItem(currentMonth-1);

                boolean isLeapYear= LoanDateUtil.isLeapYear(currentYear);
                int numDays= LoanDateUtil.getDaysOfMonth(isLeapYear, currentMonth);
                mAdapterThird=new NumericWheelAdapter(getContext(),1,numDays);
                mAdapterThird.setLabel("日");
                mWheelThird.setViewAdapter(mAdapterThird);
                mWheelThird.setCurrentItem(currentDay-1);
                txtTitle.setText("选择日期");
                break;
            case TYPE_EDU_DATE:
                int startyear = currentYear - OFFSET_YEAR;
                int endyear = currentYear;
                mAdapterFirst=new NumericWheelAdapter(getContext(),startyear,endyear);
                mAdapterFirst.setLabel("年");
                mWheelFirst.setViewAdapter(mAdapterFirst);
                mWheelFirst.addScrollingListener(mYScrollListener);

                mAdapterSecond=new NumericWheelAdapter(getContext(),1,12);
                mAdapterSecond.setLabel("月");
                mWheelSecond.setViewAdapter(mAdapterSecond);
                mWheelSecond.addScrollingListener(mYScrollListener);
                mWheelSecond.setCurrentItem(currentMonth-1);

                boolean isLeapYear2= LoanDateUtil.isLeapYear(currentYear);
                int numDays2= LoanDateUtil.getDaysOfMonth(isLeapYear2, currentMonth);
                mAdapterThird=new NumericWheelAdapter(getContext(),1,numDays2);
                mAdapterThird.setLabel("日");
                mWheelThird.setViewAdapter(mAdapterThird);
                mWheelThird.setCurrentItem(currentDay-1);
                txtTitle.setText("选择日期");
                break;
            case TYPE_OPEN_COURSE_TIME:
                Date currentDate=new Date();
                getEndDay(currentDate,61);
                start_year = currentYear;
                int end_year = deadYear;
                mAdapterFirst=new NumericWheelAdapter(getContext(),start_year,end_year);
                mAdapterFirst.setLabel("年");
                mWheelFirst.setViewAdapter(mAdapterFirst);
                mWheelFirst.addScrollingListener(mYScrollListener);

                mAdapterSecond=new NumericWheelAdapter(getContext(),1,12);
                mAdapterSecond.setLabel("月");
                mWheelSecond.setViewAdapter(mAdapterSecond);
                mWheelSecond.addScrollingListener(mYScrollListener);
                mWheelSecond.setCurrentItem(currentMonth-1);

                boolean isLeapYear3= LoanDateUtil.isLeapYear(currentYear);
                int numDays3= LoanDateUtil.getDaysOfMonth(isLeapYear3, currentMonth);
                mAdapterThird = new NumericWheelAdapter(getContext(),1,numDays3);
                mAdapterThird.setLabel("日");
                mWheelThird.setViewAdapter(mAdapterThird);
                mWheelThird.setCurrentItem(currentDay-1);
                txtTitle.setText("选择日期");
                break ;
            case TYPE_TIME_INROLLYEAR:
                end_year = currentYear;
                start_year = currentYear - OFFSET_YEAR_17;
                mAdapterFirst=new NumericWheelAdapter(getContext(),start_year,end_year);
                mAdapterFirst.setLabel("年");
                mWheelFirst.setViewAdapter(mAdapterFirst);
                mWheelSecond.setVisibility(View.GONE);
                mWheelThird.setVisibility(View.GONE);
                txtTitle.setText("选择入学年份");
                break;
        }
    }

    private void getEndDay(Date currentDate, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, +i);//+2当前的时间加2天
        currentDate = calendar.getTime();
        deadYear=calendar.get(Calendar.YEAR);
        deadMonth=calendar.get(Calendar.MONTH)+1;
        deadDay=calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setNextDay(){
        Calendar c = Calendar.getInstance();
        long cur = System.currentTimeMillis();
        long nextTime = cur + TimeUtils.ONE_DAY * 2;
        c.setTimeInMillis(nextTime);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        mWheelSecond.setCurrentItem(currentMonth-1);
        mWheelThird.setCurrentItem(currentDay-1);
    }

    public void setCurDaySelected(){
        Calendar c = Calendar.getInstance();
        long cur = System.currentTimeMillis();
        c.setTimeInMillis(cur);
        if(mType == TYPE_EDU_DATE){
            int currentMonth = c.get(Calendar.MONTH)+1;
            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            mWheelFirst.setCurrentItem(OFFSET_YEAR);
            mWheelSecond.setCurrentItem(currentMonth-1);
            mWheelThird.setCurrentItem(currentDay-1);
        }
    }

    private LoanOnWheelScrollListener mYScrollListener = new LoanOnWheelScrollListener() {
        @Override
        public void onScrollingStarted(LoanWheelView wheel) {

        }

        @Override
        public void onScrollingFinished(LoanWheelView wheel) {
            int year = 0;
            if(mType == TYPE_WORK_DESC || mType == TYPE_ADD_COURSE_DATE){
                year = mWheelFirst.getCurrentItem() + currentYear;
            }else if(mType == TYPE_EDU_DATE){
                year = mWheelFirst.getCurrentItem() + currentYear - OFFSET_YEAR;
            }else{
                year = mWheelFirst.getCurrentItem() + currentYear;
            }
            int month = mWheelSecond.getCurrentItem()+1;
            if(MyLog.isDebugable()){
                MyLog.debug(TAG,"[onScrollingFinished]" + " year:" + year + " month:" + month);
            }
            reSetDays(year,month);
        }
    };

    private void reSetDays(int year,int month){
        boolean isLeapYear=LoanDateUtil.isLeapYear(year);
        int numDays= LoanDateUtil.getDaysOfMonth(isLeapYear,month);
        if(mAdapterThird != null){
            mAdapterThird =new NumericWheelAdapter(getContext(),1,numDays);
            mAdapterThird.setLabel("日");
            int curItem = mWheelThird.getCurrentItem();
            mWheelThird.setViewAdapter(mAdapterThird);
            if(curItem > numDays){
                mWheelThird.setCurrentItem(numDays-1);
            }
        }
    }

    public void setIDatePickerListener(IDatePickerListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if (mListener!=null){
            if (view==btnOk){
                VDateEntity entity = getCurDateEntity();
                mListener.onPickerClick(entity);
            }else if(view == this.txtCancel){
                mListener.onPickCancle();
            }
        }
        dismiss();
    }

    private VDateEntity getCurDateEntity(){
        VDateEntity entity=new VDateEntity();
        int year = -1;
        if(mType == TYPE_ADD_COURSE_DATE || mType == TYPE_WORK_DESC){
            year=mWheelFirst.getCurrentItem()+currentYear;
        }else if(mType == TYPE_EDU_DATE){
            year=mWheelFirst.getCurrentItem()+currentYear-OFFSET_YEAR;
        }else if(mType == TYPE_TIME_INROLLYEAR){
            year = mWheelFirst.getCurrentItem() + currentYear - OFFSET_YEAR_17;
        }else{
            year=mWheelFirst.getCurrentItem()+currentYear;
        }
        int month=mWheelSecond.getCurrentItem()+1;
        int day=mWheelThird.getCurrentItem()+1;
        entity.year=year;
        entity.month=month;
        entity.date=day;
        return entity;
    }

    public void setPos(int year,int month,int day){
        int mIndex = mAdapterFirst.getIndexByVal(year);
        if(mIndex >= 0){
            mWheelFirst.setCurrentItem(mIndex);
        }
        if(mAdapterSecond != null){
            mIndex = mAdapterSecond.getIndexByVal(month);
            if(mIndex >= 0){
                mWheelSecond.setCurrentItem(mIndex);
            }
        }
        if(mAdapterThird != null){
            mIndex = mAdapterThird.getIndexByVal(day);
            if(mIndex >= 0){
                mWheelThird.setCurrentItem(mIndex);
            }
        }
    }


    public static abstract class IDatePickerListener {
        /****
         * 获取相关日期
         * @param vEntity
         */
        public void onPickerClick(VDateEntity vEntity){};

        public void onPickCancle(){};
    }

}
