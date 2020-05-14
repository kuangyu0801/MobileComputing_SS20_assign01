package mcteam08.assign.task03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DownloaderBroadcastReceiver extends BroadcastReceiver {
    final private static String TAG = DownloaderBroadcastReceiver.class.getCanonicalName();
    final private static String ACTION_DOWNLOADER_BROADCAST = BuildConfig.APPLICATION_ID + "ACTION_DOWNLOADER_BROADCAST";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Broadcast intent received");
        String intentAction = intent.getAction();
        if (intentAction.equals(ACTION_DOWNLOADER_BROADCAST)) {
            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
        }

    }
}
