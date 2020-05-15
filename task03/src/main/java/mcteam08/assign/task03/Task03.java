package mcteam08.assign.task03;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Task03 extends AppCompatActivity {
    final private static String TAG = Task03.class.getCanonicalName();
    final private static int MY_PERMISSION_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 1;

    // Starting from Android 8.0 (API level 26 and higher),
    // you can't use static receivers to receive most Android system broadcasts
    private DownloaderBroadcastReceiver dReceiver = new DownloaderBroadcastReceiver();
    // declaring app-specific costumed broadcast
    final private static String ACTION_DOWNLOADER_BROADCAST = BuildConfig.APPLICATION_ID + "ACTION_DOWNLOADER_BROADCAST";

    Button startButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task03);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startButton = findViewById(R.id.button0);
        stopButton = findViewById(R.id.button1);
        final Intent iDS = new Intent(this, DownloaderService.class);
        IntentFilter iFilter = new IntentFilter(ACTION_DOWNLOADER_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(dReceiver, iFilter);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSION_REQUEST_READ_WRITE_EXTERNAL_STORAGE);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start service");
                startService(iDS);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Send broadcast");
                sendDownloaderBroadcast();
            }
        });

        Log.i(TAG, "Activity created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task03, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendDownloaderBroadcast() {
        Log.i(TAG, "Send download complete broadcast");
        Intent i0 = new Intent(ACTION_DOWNLOADER_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i0);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Activity destroyed");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dReceiver);
        super.onDestroy();
    }

}
