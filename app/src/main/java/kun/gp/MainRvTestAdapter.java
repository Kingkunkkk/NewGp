package kun.gp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import kun.gp.been.HomeGpListData;

/**
 * Created by kun on 2017-11-07.
 */

public class MainRvTestAdapter extends BaseItemDraggableAdapter<HomeGpListData,BaseViewHolder>{


    public MainRvTestAdapter(List<HomeGpListData> data) {
        super(R.layout.item_new, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeGpListData item) {



        helper.setText(R.id.gpNum,item.getGpNum());
        helper.setText(R.id.gpName,item.getGpName());
        helper.setText(R.id.gpXianjia,NumberUtils.getMoneyType(item.getGpNowMoney()+""));
        helper.setText(R.id.gpUpdateTime,item.getGpUpdateTime());

        if (item.getGpChengJiaoLiang().isEmpty()){
            helper.setText(R.id.gpChengJiaoLiang,"0");
        }else {
            helper.setText(R.id.gpChengJiaoLiang,NumberUtils.getMoneyType(item.getGpChengJiaoLiang()));
        }

        if (item.getByPrice() == 0){
            helper.setText(R.id.gpShengdie,"暫未填購入價");
            helper.setTextColor(R.id.gpShengdie,Color.GRAY);
        }else {
            BigDecimal b = new BigDecimal((item.getPrice1()+item.getPrice2()+item.getPrice3()+item.getPrice4())/100);
//            double x = 1+(item.getPrice1()+item.getPrice2()+item.getPrice3()+item.getPrice4())/100;
            double x = 1+b.doubleValue();
            double byPrice = item.getByPrice()*item.getGpAmount()*x+item.getPrice5();
            double nowPrice = item.getGpNowMoney()*item.getGpAmount()*(1-b.doubleValue())-item.getPrice5();
            int lirun = (int) (nowPrice - byPrice);
            helper.setText(R.id.gpShengdie,item.getGpName());
            //绿色升 红色跌
            if (lirun > 0){
                helper.setTextColor(R.id.gpShengdie,Color.GREEN);
            }else {
                helper.setTextColor(R.id.gpShengdie,Color.RED);
            }
            helper.setText(R.id.gpShengdie,NumberUtils.getMoneyType(String.valueOf(lirun)));
        }

        if ((item.getGpNowMoney() <= MainActivity.stringTodouble(item.getPrice7()) && ! item.getPrice7().equals("0.0"))
                || (item.getGpNowMoney()>= MainActivity.stringTodouble(item.getPrice6())&& !item.getPrice6().equals("0.0"))){
            if (item.getGpNowMoney() <= MainActivity.stringTodouble(item.getPrice7()) && ! item.getPrice7().equals("0.0")){
                helper.itemView.setBackgroundColor(Color.RED);
            }
            if (item.getGpNowMoney() >= MainActivity.stringTodouble(item.getPrice6())  && !item.getPrice6().equals("0.0")){
                helper.itemView.setBackgroundColor(Color.GREEN);
            }

                if (item.isVoiceSelect().equals("1")){
                    MainActivity.play();
                }else {
                    MainActivity.stop();
                }
            layoutAnimation(helper.itemView);
            helper.setTextColor(R.id.gpShengdie,Color.WHITE);

        }else {
            helper.itemView.clearAnimation();
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.tr));
            item.setStartAnim(false);
            MainActivity.stop();
        }
    }

    private void layoutAnimation(final View view){
        view.clearAnimation();
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"alpha",1f,0f,1f);
        objectAnimator.setRepeatCount(7);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }
}
