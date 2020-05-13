package mcteam08.assign.task02;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Task02 extends AppCompatActivity implements ServiceConnection {

    final private String TAG = Task02.class.getCanonicalName();

    private ISensorService sensorServiceProxy = null;

    private Timer timer = new Timer();
    private TimerTask task;

    private TextView accelerometerX;
    private TextView accelerometerY;
    private TextView accelerometerZ;
    private TextView gyroscopeX;
    private TextView gyroscopeY;
    private TextView gyroscopeZ;

    final private int samplingPeriod = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task02);

        Intent i = new Intent(this, SensorService.class);
        startService(i);
        bindService(i, this, BIND_AUTO_CREATE);

        accelerometerX = findViewById(R.id.accelerometerX);
        accelerometerY = findViewById(R.id.accelerometerY);
        accelerometerZ = findViewById(R.id.accelerometerZ);
        gyroscopeX = findViewById(R.id.gyroscopeX);
        gyroscopeY = findViewById(R.id.gyroscopeY);
        gyroscopeZ = findViewById(R.id.gyroscopeZ);

        task = new TimerTask() {
            @Override
            public void run() {
                String[] acc;
                String[] gyro;

                try {
                    acc = sensorServiceProxy.getAccelerometer();
                    accelerometerX.setText(acc[0]);
                    accelerometerY.setText(acc[1]);
                    accelerometerZ.setText(acc[2]);
                    gyro = sensorServiceProxy.getGyroscope();
                    gyroscopeX.setText(gyro[0]);
                    gyroscopeY.setText(gyro[1]);
                    gyroscopeZ.setText(gyro[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service connected");
        sensorServiceProxy = ISensorService.Stub.asInterface(service);

        timer.schedule(task,0, samplingPeriod);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Service disconnected");
        sensorServiceProxy = null;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Activity destroyed");
        super.onDestroy();
        unbindService(this);
    }
}
