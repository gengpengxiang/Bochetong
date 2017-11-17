package com.bolong.bochetong.utils;

import android.app.Dialog;
import android.content.DialogInterface;

import java.util.LinkedList;
import java.util.Queue;

public class QueueUtil {
    private static Queue<Dialog> dialogQueue = new LinkedList<>();
    private static Dialog currentDialog = null; // 当前显示的对话框
    public static void showDialog(Dialog dialog) {
        if (dialog != null) {
            dialogQueue.offer(dialog);
        }
        if (currentDialog == null) {
            currentDialog = dialogQueue.poll();
            if (currentDialog != null) {
                currentDialog.show();
                currentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        currentDialog = null;
                        showDialog(null);
                    }
                });
            }
        }
    }

}
