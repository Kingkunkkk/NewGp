//package kun.gp;
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.graphics.Color;
//import android.media.MediaPlayer;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//
//import kun.gp.been.HomeGpListData;
//
///**
// * Created by kun on 2017-05-12.
// */
//
//public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.Viewholder> {
//    private Context mContext;
//    private List<HomeGpListData> mList;
//    private OnClick onClick;
//    private MediaPlayer mediaPlayers;
//
//    public MainRvAdapter(Context mContext, List<HomeGpListData> mList, MediaPlayer mediaPlayer) {
//        this.mContext = mContext;
//        this.mList = mList;
////        this.mediaPlayer = mediaPlayer;
//        mediaPlayers = MediaPlayer.create(mContext,R.raw.testmusic);
//
//    }
//
//    public void onClick(OnClick onClick){
//        this.onClick = onClick;
//    }
//
//    public interface OnClick{
//        void OnClickListener(int position);
//        void OnLongClickListener(int position);
//    }
//
//    @Override
//    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new,parent,false);
//        return new Viewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(Viewholder holder, final int position) {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView,"alpha",1f,0f,1f);
//        objectAnimator.setRepeatCount(7);
//        objectAnimator.setDuration(1000);
//
//        holder.num.setText(mList.get(position).getGpNum());
//        holder.xianjia.setText(NumberUtils.getMoneyType(mList.get(position).getGpNowMoney()+""));
//
//        if(mList.get(position).getGpChengJiaoLiang().isEmpty()){
//            holder.chengjiaoliang.setText("0");
//        }else {
//            holder.chengjiaoliang.setText( NumberUtils.getMoneyType(mList.get(position).getGpChengJiaoLiang()));
//        }
//
//        holder.updatetime.setText(mList.get(position).getGpUpdateTime());
//        holder.name.setText(mList.get(position).getGpName());
//
//        if (mList.get(position).getByPrice() == 0){
//            holder.shengdie.setText("暫未填購入價");
//            holder.shengdie.setTextColor(Color.GRAY);
//        }else {
//            BigDecimal b = new BigDecimal((mList.get(position).getPrice1()+mList.get(position).getPrice2()+mList.get(position).getPrice3()+mList.get(position).getPrice4())/100);
////            double x = 1+(mList.get(position).getPrice1()+mList.get(position).getPrice2()+mList.get(position).getPrice3()+mList.get(position).getPrice4())/100;
//            double x = 1+b.doubleValue();
//            double byPrice = mList.get(position).getByPrice()*mList.get(position).getGpAmount()*x+mList.get(position).getPrice5();
//            double nowPrice = mList.get(position).getGpNowMoney()*mList.get(position).getGpAmount()*(1-b.doubleValue())-mList.get(position).getPrice5();
//            int lirun = (int) (nowPrice - byPrice);
//            //绿色升 红色跌
//            if (lirun > 0){
//                holder.shengdie.setTextColor(Color.GREEN);
//            }else {
//                holder.shengdie.setTextColor(Color.RED);
//            }
//            holder.shengdie.setText(NumberUtils.getMoneyType(String.valueOf(lirun)));
//        }
//
////        Log.e("onBindViewHolder: ",mList.get(position).getGpNowMoney()+"   "+MainActivity.stringTodouble(mList.get(position).getPrice6())+"   "+mList.get(position).getPrice7() );
//        if (mList.get(position).isStartAnim()){
//            if ((mList.get(position).getGpNowMoney() <= MainActivity.stringTodouble(mList.get(position).getPrice7()) && ! mList.get(position).getPrice7().equals("0.0"))
//                    || (mList.get(position).getGpNowMoney()>= MainActivity.stringTodouble(mList.get(position).getPrice6())&& !mList.get(position).getPrice6().equals("0.0"))){
//                if (mList.get(position).getGpNowMoney() <= MainActivity.stringTodouble(mList.get(position).getPrice7()) && ! mList.get(position).getPrice7().equals("0.0")){
//                    holder.itemView.setBackgroundColor(Color.RED);
//                }
//                if (mList.get(position).getGpNowMoney() >= MainActivity.stringTodouble(mList.get(position).getPrice6())  && !mList.get(position).getPrice6().equals("0.0")){
//                    holder.itemView.setBackgroundColor(Color.GREEN);
//                }
//
//                if (mList.get(position).isVoiceSelect().equals("1")){
//                    MainActivity.mMediaPlayer.start();
//
//                }else {
//                    if (MainActivity.mMediaPlayer.isPlaying()){
//                        MainActivity.mMediaPlayer.pause();
//                        MainActivity.mMediaPlayer.seekTo(0);
//                    }
//                }
//                objectAnimator.start();
//                holder.shengdie.setTextColor(Color.WHITE);
//
//            }else {
//                objectAnimator.clone();
//                objectAnimator.cancel();
//                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.tr));
//                mList.get(position).setStartAnim(false);
//                if (MainActivity.mMediaPlayer.isPlaying()){
//                    MainActivity.mMediaPlayer.pause();
//                    MainActivity.mMediaPlayer.seekTo(0);
//                }
//            }
//        }else {
//            objectAnimator.clone();
//            objectAnimator.cancel();
//            if (MainActivity.mMediaPlayer.isPlaying()){
//                MainActivity.mMediaPlayer.pause();
//                MainActivity.mMediaPlayer.seekTo(0);
//            }
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.OnClickListener(position);
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onClick.OnLongClickListener(position);
//                return true;
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mList == null? 0:mList.size();
//    }
//
//    class Viewholder extends RecyclerView.ViewHolder {
//        private TextView name,num,xianjia,shengdie,lirun,updatetime,chengjiaoliang;
//        public Viewholder(View itemView) {
//            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.gpName);
//            xianjia = (TextView) itemView.findViewById(R.id.gpXianjia);
//            shengdie = (TextView) itemView.findViewById(R.id.gpShengdie);
//            chengjiaoliang = (TextView) itemView.findViewById(R.id.gpChengJiaoLiang);
//            num = (TextView) itemView.findViewById(R.id.gpNum);
//            updatetime = (TextView) itemView.findViewById(R.id.gpUpdateTime);
//
////            lirun = (TextView) itemView.findViewById(R.id.gpl);
//        }
//    }
//}
