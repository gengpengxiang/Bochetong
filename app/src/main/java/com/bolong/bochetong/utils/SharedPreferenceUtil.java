package com.bolong.bochetong.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.bolong.bochetong.activity.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceUtil {
    private static SharedPreferences mSharedPreferences;

    private static synchronized SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            // mSharedPreferences = App.context.getSharedPreferences(
            // PREFERENCE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        }
        return mSharedPreferences;
    }

    public static void print() {
        System.out.println(getPreferneces().getAll());
    }

    public static void clear() {
        getPreferneces().edit().clear().commit();
    }

    public static void putString(String key, String value) {
        getPreferneces().edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return getPreferneces().getString(key, defValue);

    }

    public static void putInt(String key, int value) {
        getPreferneces().edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        return getPreferneces().getInt(key, 0);
    }

    public static void putBoolean(String key, Boolean value) {
        getPreferneces().edit().putBoolean(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getPreferneces().edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        return getPreferneces().getLong(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferneces().getBoolean(key, defValue);

    }

    public static void removeString(String key) {
        getPreferneces().edit().remove(key).commit();
    }

    public static void putBean(Context context, String key, Object obj) {
        if (obj instanceof Serializable) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences.Editor editor = getPreferneces().edit();
                editor.putString(key, string64);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    public static Object getBean(Context context, String key) {
        Object obj = null;
        try {
            String base64 = getPreferneces().getString(key, "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static <T> void putDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        SharedPreferences.Editor editor = getPreferneces().edit();

        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static <T> List<T> getDataList(String tag) {
        List<T> datalist=new ArrayList<T>();
        String strJson = mSharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }
}
