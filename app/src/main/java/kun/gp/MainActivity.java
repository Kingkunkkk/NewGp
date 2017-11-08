package kun.gp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kun.gp.been.HomeGpListData;
import kun.gp.been.IndexData;
import kun.gp.been.RvListData1;
import kun.gp.been.RvListData2;
import kun.gp.utils.DataToSdUtils;
import okhttp3.Call;


//低了現價紅色  高了綠色 普通灰色
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private FloatingActionButton button;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private MainRvTestAdapter mainRvAdapter;

    private AlertDialog.Builder myDialog;
    private EditText edittext;

    private TextView hengzhi,guozhi,shangzhi,shenzhi;

    private List<HomeGpListData> mList = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                reFresh(Api.HSI,1);
                reFresh(Api.HSCEI,2);
                reFresh(Api.OTHER_,3);
                handler.sendEmptyMessageDelayed(1,10000);
            }else if (msg.what == 2){
                for (int y = 0;y<mList.size();y++){
                    reFreshRvList(mList.get(y).getGpNum(),y);
                    reFreshRvList2(mList.get(y).getGpNum(),y);
                }
                handler.sendEmptyMessageDelayed(2,10000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        initView();
        initOnClick();
        initDialog();
    }

    private void initOnClick() {
        button.setOnClickListener(this);
    }

    public static SoundPool mSoundPool;
    public static int mSoundId;
    public static int mPlayId;
    public static boolean isReady;
    private void initView() {
        String string = DataToSdUtils.convertCodeAndGetText("HKStock.txt");
        try {
            JSONObject jsonObject = new JSONObject(string);
            if (string.length()>0){
                 List<HomeGpListData> mLists = new Gson().fromJson(jsonObject.getJSONArray("DoorList").toString(),new TypeToken<List<HomeGpListData>>(){}.getType());
                 mList.addAll(mLists);
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isReady = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        }
        mSoundId = mSoundPool.load(MainActivity.this, R.raw.testmusiccc, 1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isReady = true;
            }
        });

        button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag start");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag end");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                writeToTxt();
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        };
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "view swiped start: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "View reset: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                writeToTxt();
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "View Swiped: " + pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(MainActivity.this, R.color.color_light_blue));
//                canvas.drawText("Just some text", 0, 40, paint);
            }
        };

//            mainRvAdapter = new MainRvAdapter(this,mList,mMediaPlayer);
        mainRvAdapter = new MainRvTestAdapter(mList);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mainRvAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mainRvAdapter.enableSwipeItem();
        mainRvAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mainRvAdapter.enableDragItem(mItemTouchHelper);
        mainRvAdapter.setOnItemDragListener(listener);
        mRecyclerView.setAdapter(mainRvAdapter);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mCoordinatorLayout);

        hengzhi = (TextView) findViewById(R.id.henzhi);
        guozhi = (TextView) findViewById(R.id.guozhi);
        shangzhi = (TextView) findViewById(R.id.shangzhi);
        shenzhi = (TextView) findViewById(R.id.shenzhi);

        handler.sendEmptyMessageDelayed(1,1000);
    }

    public static void play(){
        if (isReady){
            mPlayId = mSoundPool.play(mSoundId, 1, 1, 1, -1, 1);
        }else {
            play();
        }
    }

    public static void stop(){
        mSoundPool.stop(mPlayId);
    }


    private CheckBox checkBox;
    private void initDialog(){

        mainRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View layout = getLayoutInflater().inflate(R.layout.dialog_itemclick,null);
                final EditText et1,et2,et3,et4,et5,et6,et7,et8,et9;

                et1 = (EditText) layout.findViewById(R.id.et1);
                et2 = (EditText) layout.findViewById(R.id.et2);
                et3 = (EditText) layout.findViewById(R.id.et3);
                et4 = (EditText) layout.findViewById(R.id.et4);
                et5 = (EditText) layout.findViewById(R.id.et5);
                et6 = (EditText) layout.findViewById(R.id.et6);
                et7 = (EditText) layout.findViewById(R.id.et7);
                et8 = (EditText) layout.findViewById(R.id.et8);
                et9 = (EditText) layout.findViewById(R.id.et9);
                checkBox = (CheckBox) layout.findViewById(R.id.checkBox);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            for (int x = 0;x<mList.size();x++){
                                mList.get(x).setVoiceSelect("0");
                            }
                            mList.get(position).setVoiceSelect("1");
                        }else {
                            mList.get(position).setVoiceSelect("0");
                        }
                    }
                });

                if (mList.get(position).isVoiceSelect().equals("1")){
                    checkBox.setSelected(true);
                    checkBox.setChecked(true);
                }else {
                    checkBox.setSelected(false);
                    checkBox.setChecked(false);
                }

                if (mList.get(position).getByPrice() != 0){
                    et1.setText(mList.get(position).getByPrice()+"");
                }

                if (mList.get(position).getGpAmount() != 0){
                    et2.setText(mList.get(position).getGpAmount()+"");
                }

                if (mList.get(position).getPrice1() != 0){
                    et3.setText(mList.get(position).getPrice1()+"");
                }

                if (mList.get(position).getPrice2() != 0){
                    et4.setText(mList.get(position).getPrice2()+"");
                }

                if (mList.get(position).getPrice3() != 0){
                    et5.setText(mList.get(position).getPrice3()+"");
                }

                if (mList.get(position).getPrice4() != 0){
                    et6.setText(mList.get(position).getPrice4()+"");
                }

                if (mList.get(position).getPrice5() != 0){
                    et7.setText(mList.get(position).getPrice5()+"");
                }

                if (stringTodouble(mList.get(position).getPrice6()) != 0){
                    et8.setText(mList.get(position).getPrice6()+"");
                }

                if (stringTodouble(mList.get(position).getPrice7()) != 0){
                    et9.setText(mList.get(position).getPrice7()+"");
                }

                builder.setView(layout);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //四个税率
                        BigDecimal bigDecimal = new BigDecimal(String.valueOf(et3.getText().toString().isEmpty()?0:et3.getText().toString()));
                        BigDecimal bigDecima2 = new BigDecimal(String.valueOf(et4.getText().toString().isEmpty()?0:et4.getText().toString()));
                        BigDecimal bigDecima3 = new BigDecimal(String.valueOf(et5.getText().toString().isEmpty()?0:et5.getText().toString()));
                        BigDecimal bigDecima4 = new BigDecimal(String.valueOf(et6.getText().toString().isEmpty()?0:et6.getText().toString()));

                        mList.get(position).setPrice1(bigDecimal.doubleValue());
                        mList.get(position).setPrice2(bigDecima2.doubleValue());
                        mList.get(position).setPrice3(bigDecima3.doubleValue());
                        mList.get(position).setPrice4(bigDecima4.doubleValue());

                        //买入价格
                        BigDecimal byPrice = new BigDecimal(String.valueOf(et1.getText().toString().isEmpty()?0:et1.getText().toString()));
                        mList.get(position).setByPrice(byPrice.doubleValue());

                        //买入股数
                        mList.get(position).setGpAmount(et2.getText().toString().isEmpty()?0:Integer.valueOf(et2.getText().toString()));

                        //交易处理费
                        mList.get(position).setPrice5(et7.getText().toString().isEmpty()?0:Double.valueOf(et7.getText().toString()));

                        BigDecimal bigDecima8 = new BigDecimal(String.valueOf(et8.getText().toString().isEmpty()?"0":Double.valueOf(et8.getText().toString().trim())));
                        BigDecimal bigDecima9 = new BigDecimal(String.valueOf(et9.getText().toString().isEmpty()?"0":Double.valueOf(et9.getText().toString().trim())));

                        //升價提醒
                        mList.get(position).setPrice6(doubleTostring(bigDecima8.doubleValue()));

                        //跌價提醒
                        mList.get(position).setPrice7(doubleTostring(bigDecima9.doubleValue()));


                        if(((bigDecima8.toString().isEmpty()||bigDecima8.toString().equals("0")) &&( !bigDecima9.toString().equals("0")|| !bigDecima9.toString().isEmpty()))
                                || ((bigDecima9.toString().isEmpty()||bigDecima9.toString().equals("0")) &&( !bigDecima8.toString().equals("0")|| !bigDecima8.toString().isEmpty()))){
                            mList.get(position).setStartAnim(true);
                        }
                        else {
                            mList.get(position).setStartAnim(false);
                        }
                        writeToTxt();
                        mainRvAdapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
    }

    public static double stringTodouble(String string){
       return string.length()==0?0:Double.valueOf(string);
    }

    public static String doubleTostring(double doubleValue){
        return String.valueOf(doubleValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton:
                if (edittext != null){
                    edittext = null;
                }
                edittext = new EditText(this);
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                edittext.setHint("請輸入5位數股票代碼");
                new AlertDialog.Builder(this).setTitle("輸入股票代碼")
                        .setView(edittext)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (edittext.getText().toString().length()==5){
                                    boolean isHas = false;
                                    for (int x = 0;x<mList.size();x++){
                                        if (mList.get(x).getGpNum().equals(edittext.getText().toString())){
                                            isHas = true;
                                        }
                                    }

                                    if (!isHas){
                                        HomeGpListData homeGpListData = new HomeGpListData();
                                        homeGpListData.setGpNum(edittext.getText().toString());
                                        homeGpListData.setGpName("暂未填写");
                                        homeGpListData.setGpNowMoney(0);
                                        homeGpListData.setGpUpdateTime("暂未更新");
                                        homeGpListData.setGpChengJiaoLiang("0");
                                        homeGpListData.setByPrice(0);
                                        homeGpListData.setGpAmount(0);
                                        homeGpListData.setPrice1(0.23);
                                        homeGpListData.setPrice2(0.1);
                                        homeGpListData.setPrice3(0.0027);
                                        homeGpListData.setPrice4(0.005);
                                        homeGpListData.setPrice5(20);
                                        homeGpListData.setPrice6("0.0");
                                        homeGpListData.setPrice7("0.0");
                                        homeGpListData.setVoiceSelect("0");
                                        homeGpListData.setStartAnim(false);

                                        mList.add(homeGpListData);
                                        writeToTxt();
                                        mainRvAdapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(MainActivity.this, "已添加過此股票代號", Toast.LENGTH_SHORT).show();
                                    }
//                                    doUpdateList();
                                    handler.sendEmptyMessage(2);
                                }else {
                                    Toast.makeText(MainActivity.this, "填入5位數的股票代碼", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                }).show();
                break;
        }
    }

    private void writeToTxt() {
        DataToSdUtils.writeFileToSDCard(null,"HKStock.txt",false,false,MainActivity.this, JsonUtils.ListToString(mList));
    }

//    private void doUpdateList(){
//        for (int x = 0;x<mList.size()-1;x++){
//            for (int y = 1;y<mList.size()-x;y++){
//                HomeGpListData g;
//                if (Integer.valueOf(mList.get(y-1).getGpNum()).compareTo(Integer.valueOf(mList.get(y).getGpNum()))>0){
//                    g = mList.get(y-1);
//                    mList.set((y-1),mList.get(y));
//                    mList.get(y-1).setGpNum (mList.get(y).getGpNum());
//                    mList.get(y-1).setPrice1(mList.get(y).getPrice1());
//                    mList.get(y-1).setPrice2(mList.get(y).getPrice2());
//                    mList.get(y-1).setPrice3(mList.get(y).getPrice3());
//                    mList.get(y-1).setPrice4(mList.get(y).getPrice4());
//                    mList.get(y-1).setPrice5(mList.get(y).getPrice5());
//                    mList.get(y-1).setPrice6(mList.get(y).getPrice6());
//                    mList.get(y-1).update(y-1);
//
//                    mList.set(y,g);
//                    mList.get(y).setGpNum(mList.get(y).getGpNum());
//                    mList.get(y).setPrice1(mList.get(y).getPrice1());
//                    mList.get(y).setPrice2(mList.get(y).getPrice2());
//                    mList.get(y).setPrice3(mList.get(y).getPrice3());
//                    mList.get(y).setPrice4(mList.get(y).getPrice4());
//                    mList.get(y).setPrice5(mList.get(y).getPrice5());
//                    mList.get(y).setPrice6(mList.get(y).getPrice6());
//                    mList.get(y).update(y);
////                    HomeGpListData homeGpListData = new HomeGpListData();
////                    homeGpListData.setGpNum(mList.get(y).getGpNum());
////                    homeGpListData.setPrice1(mList.get(y).getPrice1());
////                    homeGpListData.setPrice2(mList.get(y).getPrice2());
////                    homeGpListData.setPrice3(mList.get(y).getPrice3());
////                    homeGpListData.setPrice4(mList.get(y).getPrice4());
////                    homeGpListData.setPrice5(mList.get(y).getPrice5());
////                    homeGpListData.setPrice6(mList.get(y).getPrice6());
////                    homeGpListData.setPrice7(mList.get(y).getPrice7());
////                    homeGpListData.update(y-1);
//                }
//            }
//        }
//        for (HomeGpListData s : mList){
//            Log.e("doUpdateList: ",  s.getGpNum()+" ");
//        }
//    }

    //更新4种类指数
    private void reFresh(final String uri, final int num){
        OkHttpUtils.post().url(uri).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(uri,e.toString() );
            }

            @Override
            public void onResponse(String response, int id) {
                String newtext = response.substring(response.indexOf("{"),response.length()).replace(";","");
//                Log.e("onResponse: ", uri+"             "+newtext);
                if (num == 1){
                    IndexData in = new Gson().fromJson(newtext,new TypeToken<IndexData>(){}.getType());
                    if (Double.valueOf(in.getPc())>Double.valueOf(in.getValue())){
                        hengzhi.setTextColor(Color.RED);
                    }else {
                        hengzhi.setTextColor(Color.GREEN);
                    }
                    BigDecimal b = new BigDecimal(Double.valueOf(in.getDifference())/Double.valueOf(in.getPc())*100);
                    hengzhi.setText("恆指："+NumberUtils.getMoneyType(in.getValue())+"   "+NumberUtils.getMoneyType(in.getDifference())+"("+b.setScale(2,BigDecimal.ROUND_DOWN)+")");
                }else if (num == 2){
                    IndexData in = new Gson().fromJson(newtext,new TypeToken<IndexData>(){}.getType());
                    if (Double.valueOf(in.getPc())>Double.valueOf(in.getValue())){
                        guozhi.setTextColor(Color.RED);
                    }else {
                        guozhi.setTextColor(Color.GREEN);
                    }
                    BigDecimal b = new BigDecimal(Double.valueOf(in.getDifference())/Double.valueOf(in.getPc())*100);
                    guozhi.setText("國指："+NumberUtils.getMoneyType(in.getValue())+"   "+NumberUtils.getMoneyType(in.getDifference())+"("+b.setScale(2,BigDecimal.ROUND_DOWN)+")");
                }else if (num == 3){
                    try {
                        JSONObject js = new JSONObject(newtext);
                        List<IndexData.NewData> newData = new Gson().fromJson(js.getJSONArray("member").toString(),new TypeToken<List<IndexData.NewData>>(){}.getType());

                        if (Double.valueOf(newData.get(6).getDifference())>0){
                            BigDecimal a = new BigDecimal(Double.valueOf(newData.get(6).getDifference())/(Double.valueOf(newData.get(6).getDifference())+Double.valueOf(newData.get(6).getPoint()))*100);
                            shangzhi.setTextColor(Color.GREEN);
                            shangzhi.setText("上指："+NumberUtils.getMoneyType(newData.get(6).getPoint())+"   "+NumberUtils.getMoneyType(newData.get(6).getDifference())+"("+a.setScale(2,BigDecimal.ROUND_DOWN)+")");
                        }else {
                            BigDecimal a = new BigDecimal(Double.valueOf(newData.get(6).getDifference())/(-Double.valueOf(newData.get(6).getDifference())+Double.valueOf(newData.get(6).getPoint()))*100);
                            shangzhi.setTextColor(Color.RED);
                            shangzhi.setText("上指："+NumberUtils.getMoneyType(newData.get(6).getPoint())+"   "+NumberUtils.getMoneyType(newData.get(6).getDifference())+"("+a.setScale(2,BigDecimal.ROUND_DOWN)+")");
                        }

                        if (Double.valueOf(newData.get(7).getDifference())>0){
                            BigDecimal a = new BigDecimal(Double.valueOf(newData.get(7).getDifference())/(Double.valueOf(newData.get(7).getDifference())+Double.valueOf(newData.get(7).getPoint()))*100);
                            shenzhi.setTextColor(Color.GREEN);
                            shenzhi.setText("深指："+newData.get(7).getPoint()+"   "+newData.get(7).getDifference()+"("+a.setScale(2,BigDecimal.ROUND_DOWN)+")");
                        }else {
                            BigDecimal a = new BigDecimal(Double.valueOf(newData.get(7).getDifference())/(-Double.valueOf(newData.get(7).getDifference())+Double.valueOf(newData.get(7).getPoint()))*100);
                            shenzhi.setTextColor(Color.RED);
                            shenzhi.setText("深指："+newData.get(7).getPoint()+"   "+newData.get(7).getDifference()+"("+a.setScale(2,BigDecimal.ROUND_DOWN)+")");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * M18.r_00700 = {
             ltt: '2017/05/15 14:08',
             np: '259.800',
             iep: '0.000',
             iev: '0',
             ltp: '259.800',
             vol: '12940427',
             tvr: '3354513538',
             dyh: '260.600',
             dyl: '257.800'
     };
     * @param GpNum
     */
    private void reFreshRvList(String GpNum, final int num){
        OkHttpUtils.get().url("http://money18.on.cc/js/real/quote/"+ GpNum +"_r.js?t=")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                String newtext = response.substring(response.indexOf("{"),response.length()).replace(";","");
                RvListData1 rvData = new Gson().fromJson(newtext,new TypeToken<RvListData1>(){}.getType());
                mList.get(num).setGpNowMoney(rvData.getNp());
                mList.get(num).setGpChengJiaoLiang(rvData.getVol()+"");
                mList.get(num).setGpUpdateTime(rvData.getLtt()+"");
//                writeToTxt();
                mainRvAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * M18.d_00700 = {
     名： “腾讯”，nameChi： “腾讯控股”，preCPrice： “258.20”，lotSize： “100.00”，
                     currCode： “HKD”，currUnit： “”，sspnFlag： “N”，instType： “EQTY”，SPCODE：“01”，
                     mthHigh： “259.20”，mthLow： “228.00”，wk52High： “259.20”，wk52Low： “155.30”，
                     MA10：248.28，MA20：241.15，MA50：227.924，rsi10：80.1684，rsi14：77.4328，rsi20： 75.0944，
                     分红： “0.61”，每股收益： “4.952”，cnvRatio： “0.000”，stkPrice： “0.00”，成熟度：
                     “空”，CPType表： “”，cbbcCPrice： “0.00”，ParentType的： “指数”
     }
     * @param GpNum
     */
    private void reFreshRvList2(String GpNum, final int num){
//        OkHttpUtils.get().url("http://money18.on.cc/js/daily/quote/"+ GpNum+"_d.js?t=")
        OkHttpUtils.get().url("http://hq.sinajs.cn/list=hk"+GpNum)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                String[] s = response.split(",");
                mList.get(num).setGpName(s[1]+"");
                if (!mList.get(num).getGpName().equals(s[1])){
                    writeToTxt();
                }
                mainRvAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 检测权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
