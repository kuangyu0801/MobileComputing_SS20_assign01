package mcteam08.assign.task03;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloaderService extends Service {
    final private static String TAG = DownloaderService.class.getCanonicalName();
    final private static String ACTION_DOWNLOADER_BROADCAST = BuildConfig.APPLICATION_ID + "ACTION_DOWNLOADER_BROADCAST";

    private URL sourceVideo = new URL("https://drive.google.com/uc?id=1WAi7vvbAwodbBhkEftAQsf3Kv2thFZeV&export=download");
    private URL sourcePhoto = new URL("https://drive.google.com/uc?id=1TKPT65RAA8cchYocaF4J6YNJvZJrvwXE&export=download");
    private URL source = new URL("https://drive.google.com/file/d/1NnG-KfCma6Bn1OIEDPms4GPY_Quj8TWb/view");
    public DownloaderService() throws MalformedURLException {

    }

    // URL: https://drive.google.com/uc?id=1WAi7vvbAwodbBhkEftAQsf3Kv2thFZeV&export=download
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        download();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        super.onDestroy();
    }

    // 重写该方法，为界面上的按钮提供事件响应方法
    public void download() {
        DownTask task = new DownTask(this);
        task.execute(source);
    }

    private void sendDownloaderBroadcast() {
        Log.i(TAG, "Send download complete broadcast");
        Intent i0 = new Intent(ACTION_DOWNLOADER_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i0);
    }

    class DownTask extends AsyncTask<URL, Integer, String>
    {
        int hasRead = 0;
        Context mContext;
        public DownTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected String doInBackground(URL... params)
        {
            Log.i(TAG, "Download task started in background");
            StringBuilder sb = new StringBuilder();
            try
            {
                URLConnection conn = params[0].openConnection();
                // 打开conn连接对应的输入流，并将它包装成BufferedReader
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                    hasRead++;
                    publishProgress(hasRead);
                    Thread.sleep(1);
                }
                return sb.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // 返回HTML页面的内容
            // 设置进度条不可见
            Log.i(TAG, "Download task post-processing in main thread");
            sendDownloaderBroadcast();
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Download task preparing in main thread");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "Download ongoing thread");
            // 更新进度

        }
    }

}
