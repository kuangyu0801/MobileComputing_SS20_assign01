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

public class SensorReaderService extends Service implements SensorEventListener{
    final private static String TAG = SensorReaderService.class.getCanonicalName();
    final private static int DEFAULT_SAMPLE_PERIOD = 100000;
    private int samplePeriod;
    private SensorReaderServiceImpl impl;
    SensorManager sensorManager;
    Sensor sensorGryro, sensorAcc;
    float[] dataGyro = new float[3];
    float[] dataAcc = new float[3];


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.equals(sensorGryro)) {
            // Log.i(TAG, "Gyroscope Changed");
            dataGyro[0] = sensorEvent.values[0];
            dataGyro[1] = sensorEvent.values[1];
            dataGyro[2] = sensorEvent.values[2];
        } else if (sensorEvent.sensor.equals(sensorAcc)) {
            // Log.i(TAG, "Accelerometer Changed");
            dataAcc[0] = sensorEvent.values[0];
            dataAcc[1] = sensorEvent.values[1];
            dataAcc[2] = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class SensorReaderServiceImpl extends ISensorReaderService.Stub {
        @Override
        public String getHelloWorld() throws RemoteException {
            return "This is Hello World from SensorReaderService";
        }

        @Override
        public float[] getSensorGyroscope() throws RemoteException {
            Log.i(TAG, "Get Statics from Gyroscope");
            return dataGyro;
        }

        @Override
        public float[] getSensorAccelerometer() throws RemoteException {
            Log.i(TAG, "Get Statics from Accelerometer");
            return dataAcc;
        }

        @Override
        public int setSamplePeriod(int period) throws RemoteException {
            Log.i(TAG, "Set Sample Period");
            int prevPeriod = samplePeriod;
            samplePeriod = period;
            return prevPeriod;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Create Service");
        super.onCreate();
        impl = new SensorReaderServiceImpl();
        samplePeriod = DEFAULT_SAMPLE_PERIOD;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGryro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        dataGyro = new float[3];
        if (sensorManager.registerListener(this, sensorGryro, SensorManager.SENSOR_DELAY_NORMAL)) {
            Log.i(TAG, "GYROSCOPE Registered");
        } else {
            Log.i(TAG, "GYROSCOPE Not Registered");
        }
        if (sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL)) {
            Log.i(TAG, "ACCELEROMETER Registered");
        } else {
            Log.i(TAG, "ACCELEROMETER Not Registered");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Bind Service");
        return impl;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroy Service");
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
