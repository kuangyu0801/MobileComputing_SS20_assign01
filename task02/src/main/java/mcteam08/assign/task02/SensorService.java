package mcteam08.assign.task02;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class SensorService extends Service implements SensorEventListener {
    private static String TAG = SensorService.class.getCanonicalName();

    private SensorServiceImpl impl;

    final private int samplingPeriod = 100000;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private boolean sensorListenerRegistered =false;
    private String[] accStr= new String[]{"","",""};
    private String[] gyroStr= new String[]{"","",""};

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor source = event.sensor;
        if (source.equals(accelerometer)) {
            accStr[0] = Float.toString(event.values[0]);
            accStr[1] = Float.toString(event.values[1]);
            accStr[2] = Float.toString(event.values[2]);
        } else if (source.equals(gyroscope)) {
            gyroStr[0] = Float.toString(event.values[0]);
            gyroStr[1] = Float.toString(event.values[1]);
            gyroStr[2] = Float.toString(event.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class SensorServiceImpl extends ISensorService.Stub {
        @Override
        public String[] getAccelerometer() {
            return accStr;
        }

        @Override
        public String[] getGyroscope() {
            return gyroStr;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Creating service");
        super.onCreate();

        impl = new SensorServiceImpl();

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Binding service");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (sensorManager.registerListener(this, accelerometer, samplingPeriod)
                && sensorManager.registerListener(this, gyroscope, samplingPeriod))
            sensorListenerRegistered = true;
        return impl;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (sensorListenerRegistered)
            sensorManager.unregisterListener(this);
        Log.i(TAG, "Destroying service");
        super.onDestroy();
    }
}
