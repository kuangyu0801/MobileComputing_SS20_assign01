package mcteam08.assign.task02;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class SensorReaderService extends Service implements SensorEventListener, LocationListener {
    final private static String TAG = SensorReaderService.class.getCanonicalName();
    final private static int DEFAULT_SAMPLE_PERIOD = 10000; // 10 seconds
    final private static long MIN_TIME = 10000; // 10 seconds
    final private static long MIN_DISTANCE = 1; // 1 meter

    private int samplePeriod;
    private SensorReaderServiceImpl impl;
    private SensorManager sensorManager;
    private Sensor sensorGryro, sensorAcc;
    private LocationManager locationManager;
    private float[] dataGyro = new float[3];
    private float[] dataAcc = new float[3];
    private double[] dataLocation = new double[2];

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

    @Override
    public void onLocationChanged(Location location) {
        dataLocation[0] = location.getLongitude();
        dataLocation[1] = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
        public double[] getLocation() throws RemoteException {
            Log.i(TAG, "Get Statics from GPS");
            return dataLocation;
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

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

        if (!gpsEnabled) {
            // DONE: how to make sure a service always has enabled GPS?
            // App bind to this service should enable this GPS
            Log.i(TAG, "GPS not enabled! Please enable it!");
            //enableLocationSettings();
        } else {
            if ((Build.VERSION.SDK_INT >= 23) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) {
                // DONE: add method requestPermission
                // App bind to this service should set the permission
                Log.i(TAG, "GPS permission not granted! Please authorize it!");
            } else {
                Log.i(TAG, "GPS permission granted!");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,this);
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Bind Service");
        return impl;
    }

    // DONE: check life cycle on service, make sure activity unbind doesn't kill service
    // Service would be destroy only is it is created by bind
    // first start Service and then bind to Service

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroy Service");
        super.onDestroy();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

}
