package com.micang.baozhu.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/29 13:48
 * @describe describe
 */
public class GyrosensorUtils {
    String TAG = "GyrosensorUtils";
    private volatile static GyrosensorUtils uniqueInstance;
    private Context mContext;
    private SensorManager sensorManager;
    private CoordinatesBean coordinatesBean = new CoordinatesBean();
    private Sensor gyroscopeSensor;     //陀螺仪
    private Sensor magneticSensor;      //磁力
    private Sensor accelerometerSensor; //加速度
    // 将纳秒转化为秒
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];

    private GyrosensorUtils(Context context) {
        mContext = context;
        getAngle();
    }

    //采用Double CheckLock(DCL)实现单例
    public static GyrosensorUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (GyrosensorUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new GyrosensorUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    private void getAngle() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //注册陀螺仪传感器，并设定传感器向应用中输出的时间间隔类型是SensorManager.SENSOR_DELAY_GAME(20000微秒)
//SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能
//SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别
//SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象
//SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
//注册陀螺仪传感器，并设定传感器向应用中输出的时间间隔类型是SensorManager.SENSOR_DELAY_GAME(20000微秒)
//sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
//sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(listener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public CoordinatesBean getCoordinates() {
        return coordinatesBean;
    }

    public void removeListener() {
        if (sensorManager != null) {
            uniqueInstance = null;
            sensorManager.unregisterListener(listener);
        }
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (timestamp != 0) {
                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                final float dT = (event.timestamp - timestamp) * NS2S;
                // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                angle[2] += event.values[2] * dT;
                // 将弧度转化为角度
                float anglex = (float) Math.toDegrees(angle[0]);
                float angley = (float) Math.toDegrees(angle[1]);
                float anglez = (float) Math.toDegrees(angle[2]);
                coordinatesBean.setAnglex(anglex);
                coordinatesBean.setAngley(angley);
                coordinatesBean.setAnglez(anglez);

//                System.out.println("anglex------------>" + anglex);
//                System.out.println("angley------------>" + angley);
//                System.out.println("anglez------------>" + anglez);
//                System.out.println("gyroscopeSensor.getMinDelay()----------->" + gyroscopeSensor.getMinDelay());
            }
            //将当前时间赋值给timestamp
            timestamp = event.timestamp;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
