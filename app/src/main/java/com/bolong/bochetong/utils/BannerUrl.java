package com.bolong.bochetong.utils;

import android.util.Log;

import com.bolong.bochetong.bean.HomeContent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/4/4.
 */

public class BannerUrl {
    public static ArrayList<String> imgesUrl;
    private static String url = Param.IP + "/app/getHomeContent";
   /* public static List<String> getImgesUrl() {

        imgesUrl = new ArrayList<>();
        imgesUrl.add("https://imgsa.baidu.com/news/q%3D100/sign=1236f908753e6709b80041ff0bc69fb8/ac4bd11373f08202ef1f941041fbfbedab641b0b.jpg");
        imgesUrl.add("https://imgsa.baidu.com/news/q%3D100/sign=5ddf1b32de2a283445a6320b6bb4c92e/2e2eb9389b504fc2e8e4bb2aefdde71190ef6d61.jpg");
        //imgesUrl.add("http://imageprocess.yitos.net/images/public/20160906/1291473163104906.jpg");
        return imgesUrl;
    }*/
   public static List<String> getImgesUrl() {

       imgesUrl = new ArrayList<>();

       Map<String, String> map = new HashMap<>();
       map.put("citycode", "101010100");
       HttpUtils.post(url, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               Log.e("onFailure","onFailure");
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String jsonDatas = response.body().string();
               Log.e("首页数据",jsonDatas);
               try {
                   JSONObject jsonObject = new JSONObject(jsonDatas);
                   String content = jsonObject.optString("content");
                   Gson gson = new Gson();
                   HomeContent homeContent = gson.fromJson(content, HomeContent.class);
                   List<HomeContent.AdPositionIdBean> adPositionId = homeContent.getAdPositionId();

                   List<String> picList = new ArrayList<>();
                   for(int i=0;i<adPositionId.size();i++){
                       String adUrl = adPositionId.get(i).getPicture();
                       imgesUrl.add(adUrl);

                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, map);

       return imgesUrl;
   }
}
