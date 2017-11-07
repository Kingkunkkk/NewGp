package kun.gp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kun.gp.been.HomeGpListData;

/**
 * Created by kun on 2017-11-07.
 */

public class JsonUtils {
    public static String ListToString(List<HomeGpListData> mBigList){
        JSONObject dataObj = new JSONObject();
        JSONArray dataArray = new JSONArray();
        try {
            for (int x = 0;x<mBigList.size();x++){
                JSONObject obj = new JSONObject();
                obj.put("GpNum",mBigList.get(x).getGpNum());
                obj.put("GpName",mBigList.get(x).getGpName());
                obj.put("GpNowMoney",mBigList.get(x).getGpNowMoney());
                obj.put("GpUpdateTime",mBigList.get(x).getGpUpdateTime());
                obj.put("GpChengJiaoLiang",mBigList.get(x).getGpChengJiaoLiang());
                obj.put("byPrice",mBigList.get(x).getByPrice());
                obj.put("gpAmount",mBigList.get(x).getGpAmount());
                obj.put("Price1",mBigList.get(x).getPrice1());
                obj.put("Price2",mBigList.get(x).getPrice2());
                obj.put("Price3",mBigList.get(x).getPrice3());
                obj.put("Price4",mBigList.get(x).getPrice4());
                obj.put("Price5",mBigList.get(x).getPrice5());
                obj.put("Price6",mBigList.get(x).getPrice6());
                obj.put("Price7",mBigList.get(x).getPrice7());
                obj.put("isVoiceSelect",mBigList.get(x).isVoiceSelect());
                obj.put("startAnim",mBigList.get(x).isStartAnim());
                dataArray.put(obj);
            }
            dataObj.put("DoorList",dataArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataObj.toString();
    }
}
