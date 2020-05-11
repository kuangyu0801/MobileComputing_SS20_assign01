package mcteam08.assign.task02;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SensorReaderService extends Service {
    private static String TAG = SensorReaderService.class.getCanonicalName();
    private SensorReaderServiceImpl impl;

    private class SensorReaderServiceImpl extends ISensorReaderService.Stub {
        @Override
        public String getHelloWorld() throws RemoteException {
            return "This is Hello World from SensorReaderService";
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Create Service");
        super.onCreate();
        impl = new SensorReaderServiceImpl();
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
    }
}
