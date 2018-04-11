package com.example.dell.minesweeper;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;


public class ServiceMotion extends Service implements SensorEventListener{
    private final float DEFLECTION = 0.6f;

    private final IBinder mBinder = new LocalBinder();
    private MotionListener mMotionListener;
    private SensorManager mSensorManager;
    private float[] mAccValues = new float[3];
    private float[] mMagValues = new float[3];
    private float[] mRotation = new float[9];
    private float[] mOrientation = new float[3];
    private float[] mFirstOrientation = new float[3];
    private Sensor mAccSensor;
    private Sensor mMagSensor;
    private Handler mHandler;
    private boolean mFlag = false;
    private boolean mFirstTime = true;
    private int mSecondTime = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null &&
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(this, mMagSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        mHandler.post(processSensors);
    }

    // set delay for sensors sampling
    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            mFlag = true;
            mHandler.postDelayed(this, 2000);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, mMagValues, 0, sensorEvent.values.length);
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, mAccValues, 0, sensorEvent.values.length);
        }

        if (mFlag) {
            if (mFirstTime){

                mSecondTime++;

                if (mSecondTime == 2) {
                    mFirstTime = false;
                    SensorManager.getRotationMatrix(mRotation, null, mAccValues, mMagValues);
                    SensorManager.getOrientation(mRotation, mFirstOrientation);
                }
            }

            SensorManager.getRotationMatrix(mRotation, null, mAccValues, mMagValues);
            SensorManager.getOrientation(mRotation, mOrientation);

            if (    mOrientation[0] >  mFirstOrientation[0] + DEFLECTION  || mOrientation[0] <  mFirstOrientation[0] - DEFLECTION  ||
                    mOrientation[1] >  mFirstOrientation[1] + DEFLECTION  || mOrientation[1] <  mFirstOrientation[1] - DEFLECTION  ||
                    mOrientation[2] >  mFirstOrientation[2] + DEFLECTION  || mOrientation[2] <  mFirstOrientation[2] - DEFLECTION
                    ) {

                if (mMotionListener != null && mFirstTime == false);
                mMotionListener.positionChanged();
            }
            mFlag = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public boolean onUnbind(Intent intent) {
        mSensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {

        public ServiceMotion getService() {
            return ServiceMotion.this;
        }

        public void registerListener (MotionListener listener){
            mMotionListener = listener;
        }
    }


    public interface MotionListener {

        void positionChanged();

    }
}

