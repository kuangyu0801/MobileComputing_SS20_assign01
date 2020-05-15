package mcteam08.assign.task03;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloaderService extends Service {
    final private static String TAG = DownloaderService.class.getCanonicalName();
    final private static String ACTION_DOWNLOADER_BROADCAST = BuildConfig.APPLICATION_ID + "ACTION_DOWNLOADER_BROADCAST";
    final private static String FILENAME = "targetFileFromDownload.jpg";
    private String sourceVideo = "https://drive.google.com/uc?id=1WAi7vvbAwodbBhkEftAQsf3Kv2thFZeV&export=download";
    private String sourcePhoto = "https://drive.google.com/uc?id=1TKPT65RAA8cchYocaF4J6YNJvZJrvwXE&export=download";
    private String sourceText = "https://drive.google.com/file/d/1NnG-KfCma6Bn1OIEDPms4GPY_Quj8TWb/view";
    private String source = sourcePhoto;


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
        checkMounted();
        checkPermission();
        isExternalStorageWritable();
        isExternalStorageReadable();
        testWrite();

        try {
            download();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        super.onDestroy();
    }

    // 重写该方法，为界面上的按钮提供事件响应方法
    public void download() throws MalformedURLException {
        DownTask task = new DownTask(this);
        URL sourceUrl = new URL(source);
        task.execute(sourceUrl);
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
            int count;
            try
            {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File outputFile = new File(path.getCanonicalPath(), FILENAME);
                if (outputFile.exists()) {
                    Log.i(TAG, "Output file creation success");
                } else {
                    Log.i(TAG, "Output file creation failure");
                }

                URLConnection conn = params[0].openConnection();
                conn.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conn.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(params[0].openStream(), 8192);

                // Output stream
                //extension must change (mp3,mp4,zip,apk etc.)
                FileOutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
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
            Log.i(TAG, "Download" + values + "%");
            // 更新进度

        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.i(TAG, "External storage writable");
            return true;
        }
        Log.i(TAG, "External storage not writable");
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.i(TAG, "External storage readable");
            return true;
        }
        Log.i(TAG, "External storage not readable");
        return false;
    }

    public void testWrite() {
        Log.i(TAG, "testWrite()!");

        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            try {
                File textFile = new File(path.getCanonicalPath(), "testWriteFile.txt");
                Log.i(TAG, path.getCanonicalPath());
                FileOutputStream output = new FileOutputStream(textFile);
                output.write("my first file in android".getBytes());
                output.close();
                Log.i(TAG, "Test file created!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "permission denied");
            return false;
        }
        Log.i(TAG, "permission granted");
        return true;
    }

    private boolean checkMounted() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD card mounted");
            return true;
        }
        Log.i(TAG, "SD card not mounted");
        return false;
    }
}
