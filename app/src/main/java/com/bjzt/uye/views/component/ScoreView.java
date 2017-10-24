package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.common.listener.NoConfusion;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/24
 */
public class ScoreView extends LinearLayout implements NoConfusion{

    @BindView(R.id.img_score_star1)
    ImageView img_score_star1;
    @BindView(R.id.img_score_star2)
    ImageView img_score_star2;
    @BindView(R.id.img_score_star3)
    ImageView img_score_star3;
    @BindView(R.id.img_score_star4)
    ImageView img_score_star4;
    @BindView(R.id.img_score_star5)
    ImageView img_score_star5;
    @BindView(R.id.text_score)
    TextView text_score;

    private Drawable star_half;
    private Drawable star;
    private Drawable starGrey;



    public ScoreView(Context context) {
        super(context);
        init();
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.score_view,this,true);
        ButterKnife.bind(this);

        star_half = Global.getContext().getResources().getDrawable(R.drawable.star_half);
        star = Global.getContext().getResources().getDrawable(R.drawable.star);
        starGrey = getResources().getDrawable(R.drawable.star_grey);
    }

    public void setData(float data){
        if(data==0){
            img_score_star1.setImageDrawable(starGrey);
            img_score_star2.setImageDrawable(starGrey);
            img_score_star3.setImageDrawable(starGrey);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=0.3f&&data<=0.7f){
            img_score_star1.setImageDrawable(star_half);
            img_score_star2.setImageDrawable(starGrey);
            img_score_star3.setImageDrawable(starGrey);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=0.8f&&data<=1.2f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(starGrey);
            img_score_star3.setImageDrawable(starGrey);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=1.3f&&data<=1.7f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star_half);
            img_score_star3.setImageDrawable(starGrey);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=1.8f&&data<=2.2f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(starGrey);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=2.3f&&data<=2.7f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star_half);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=2.8f&&data<=3.2f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star);
            img_score_star4.setImageDrawable(starGrey);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=3.3f&&data<=3.7f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star);
            img_score_star4.setImageDrawable(star_half);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=3.8f&&data<=4.2f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star);
            img_score_star4.setImageDrawable(star);
            img_score_star5.setImageDrawable(starGrey);
        }else if(data>=4.3f&&data<=4.7f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star);
            img_score_star4.setImageDrawable(star);
            img_score_star5.setImageDrawable(star_half);
        }else if(data>=4.8f){
            img_score_star1.setImageDrawable(star);
            img_score_star2.setImageDrawable(star);
            img_score_star3.setImageDrawable(star);
            img_score_star4.setImageDrawable(star);
            img_score_star5.setImageDrawable(star);
        }
        text_score.setText(data+"分");
    }
}
