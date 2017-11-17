package com.bolong.bochetong.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.bolong.bochetong.activity.MyApplication;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.MsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAnsy extends AsyncTask<String, Integer, Integer> {

    public static final int ACTION_DOWNLOAD_FINISH = 1877;

    private Notification nf;
    private NotificationCompat.Builder builder;
    private NotificationManager nm = (NotificationManager) MyApplication.getInstance().getSystemService(Activity.NOTIFICATION_SERVICE);

    @Override
    protected Integer doInBackground(String... params) {

        //add

        String versionNumber = SharedPreferenceUtil.getString("versionNumber","1.0.1");
        HttpURLConnection con = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(params[0]);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);  //设置超时时间
            if (con.getResponseCode() == 200) { //判断是否连接成功
                int fileLength = con.getContentLength();
                is = con.getInputStream();    //获取输入
                os = new FileOutputStream(Param.FILEPATH+versionNumber+".apk");
                byte[] buffer = new byte[1024 * 1024 * 10];
                long total = 0;
                int count;
               /* while ((count = is.read(buffer)) != -1) {
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));  //传递进度（注意顺序）
                    os.write(buffer, 0, count);
                }*/
                int pro1=0;
                int pro2=0;
                while ((count=is.read(buffer))!=-1){
                    total+=count;
                    if (fileLength > 0)
                        pro1=(int) (total * 100 / fileLength);  //传递进度（注意顺序）
                    if(pro1!=pro2)
                        publishProgress(pro2=pro1);
                    os.write(buffer,0,count);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        if (result == 1) {
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder= new NotificationCompat.Builder(MyApplication.getInstance().getBaseContext())
                .setSmallIcon(R.mipmap.icon_logo_little).setContentInfo("下载中...").setContentTitle("正在下载");
        nf=builder.build();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        builder.setProgress(100,values[0],false);
        nf=builder.build();
        nm.notify(0,nf);


        if (values[0] == 100) {
            //EventBus.getDefault().post(new MsgEvent(ACTION_DOWNLOAD_FINISH));
            nm.cancel(0);
            String versionNumber = SharedPreferenceUtil.getString("versionNumber","1.0.1");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Param.FILEPATH+versionNumber+".apk")), "application/vnd.android.package-archive");
            MyApplication.getInstance().startActivity(intent);
//            PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.setContentTitle("下载完成").setContentText("点击安装").setContentIntent(pendingIntent);
//            nf = builder.build();
//            nm.notify(0, nf);
        }
    }
}
