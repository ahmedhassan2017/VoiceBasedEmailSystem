package com.example.abanoub.voicebasedemailsystem.Shaking;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.example.abanoub.voicebasedemailsystem.SignInActivity;

/**
 * Created by Mahmoud Heshmat on 3/7/2018.
 */

public class MyService  extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Prefs prefs;

    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        super.onCreate();

        startForeground(1, new Notification());
        SignInActivity.isServiceRunning = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        prefs = new Prefs(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, new Notification());
        SignInActivity.isServiceRunning = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        prefs = new Prefs(this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        SignInActivity.isServiceRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
// TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float accelationSquareRoot = (x * x + y * y + z * z) / 50;
        if (accelationSquareRoot >= Integer.parseInt(prefs.getStringPrefs(
                Prefs.sensitivity, "8"))) {
            Intent i = getPackageManager().getLaunchIntentForPackage(
                    prefs.getStringPrefs(Prefs.appPackage,
                            "com.example.abanoub.voicebasedemailsystem"));
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
//        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }


}
