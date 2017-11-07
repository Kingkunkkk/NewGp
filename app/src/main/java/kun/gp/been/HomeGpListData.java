package kun.gp.been;


/**
 * Created by kun on 2017-05-15.
 */

public class HomeGpListData{
    private String GpNum;
    private String GpName = "";
    private double GpNowMoney;
    private String GpUpdateTime = "";
    private String GpChengJiaoLiang = "";

    public String getGpName() {
        return GpName;
    }

    public void setGpName(String gpName) {
        GpName = gpName;
    }

    public double getGpNowMoney() {
        return GpNowMoney;
    }

    public void setGpNowMoney(double gpNowMoney) {
        GpNowMoney = gpNowMoney;
    }

    public String getGpUpdateTime() {
        return GpUpdateTime;
    }

    public void setGpUpdateTime(String gpUpdateTime) {
        GpUpdateTime = gpUpdateTime;
    }

    public String getGpChengJiaoLiang() {
        return GpChengJiaoLiang;
    }

    public void setGpChengJiaoLiang(String gpChengJiaoLiang) {
        GpChengJiaoLiang = gpChengJiaoLiang;
    }

    public String getGpNum() {
        return GpNum;
    }

    public void setGpNum(String gpNum) {
        GpNum = gpNum;
    }



    private double byPrice;  //买入价格
    private int gpAmount;   //股数
    private double Price1; //经纪佣金
    private double Price2; //从价印花税
    private double Price3; //交易徵費
    private double Price4; //交易費
    private double Price5; //交易處理費
    private String Price6; //升價提醒
    private String Price7; //跌价提醒

    private String isVoiceSelect; //选中有声提醒    1为选中，0为未选中
    private boolean startAnim; //是否闪动item

    public boolean isStartAnim() {
        return startAnim;
    }

    public void setStartAnim(boolean startAnim) {
        this.startAnim = startAnim;
    }

    public String isVoiceSelect() {
        return isVoiceSelect;
    }

    public void setVoiceSelect(String voiceSelect) {
        isVoiceSelect = voiceSelect;
    }

    public double getByPrice() {
        return byPrice;
    }

    public void setByPrice(double byPrice) {
        this.byPrice = byPrice;
    }

    public int getGpAmount() {
        return gpAmount;
    }

    public void setGpAmount(int gpAmount) {
        this.gpAmount = gpAmount;
    }

    public double getPrice1() {
        return Price1;
    }

    public void setPrice1(double price1) {
        Price1 = price1;
    }

    public double getPrice2() {
        return Price2;
    }

    public void setPrice2(double price2) {
        Price2 = price2;
    }

    public double getPrice3() {
        return Price3;
    }

    public void setPrice3(double price3) {
        Price3 = price3;
    }

    public double getPrice4() {
        return Price4;
    }

    public void setPrice4(double price4) {
        Price4 = price4;
    }

    public double getPrice5() {
        return Price5;
    }

    public void setPrice5(double price5) {
        Price5 = price5;
    }

    public String getPrice6() {
        return Price6;
    }

    public void setPrice6(String price6) {
        Price6 = price6;
    }

    public String getPrice7() {
        return Price7;
    }

    public void setPrice7(String price7) {
        Price7 = price7;
    }
}
