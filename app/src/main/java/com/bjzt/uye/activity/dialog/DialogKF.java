package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import com.bjzt.uye.R;

/**
 * Created by billy on 2017/10/20.
 */
public class DialogKF extends Dialog{

    public DialogKF(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.loan_popwin_animation);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }


}
