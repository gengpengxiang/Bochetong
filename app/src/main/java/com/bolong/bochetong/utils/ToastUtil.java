package com.bolong.bochetong.utils;

/**
 * Created by admin on 2017/5/11.
 */
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
public class ToastUtil {
    private static Toast toast;

    /**
     * 短时间显示  Toast
     *
     * @param context
     * @param sequence
     */
    public static void showShort(Context context, CharSequence sequence) {

        if (toast == null) {
            toast = Toast.makeText(context, sequence, Toast.LENGTH_SHORT);

        } else {
            toast.setText(sequence);
        }
        toast.show();

    }


}
