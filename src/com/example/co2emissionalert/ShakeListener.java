package com.example.co2emissionalert;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener {

	  //���μ���ʱ����
	private static final int INTERVAL_TIME = 70;
	//�ٶ���ֵ����ҡ���ٶȴﵽ��ֵ���������
	private static final int SPEED_SHRESHOLD = 4500;	 
	private SensorManager sensorManager; 
	private Sensor sensor; 
  private Context context; 
	private OnShakeListener onShakeListener; 
  //�ֻ���һ��λ��ʱ������Ӧ����	 
	private float lastX; 
	private float lastY; 
  private float lastZ;

	private long lastUpdateTime;
  
    public interface OnShakeListener {  
        public void onShake();  
    }  
  
    public ShakeListener(Context context) {  
        this.context = context;  
        resume();  
    }  
  
    public void setOnShakeListener(OnShakeListener listener) {  
    	onShakeListener = listener;  
    }  
  
    public void resume() {  
    	sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE); 
    	if(sensorManager == null) { 
    		throw new UnsupportedOperationException("Sensors are not supported");
    	  } 
    	//������������� 
    	sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
    	//ע�� 
       if(sensor != null) { 
    	sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME); 
    	 }  
    }  
  
    public void pause() {  
        if (sensorManager != null) {  
        	sensorManager.unregisterListener(this);  
        	sensorManager = null;  
        } 
    }  
  
      
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  
          
    }  
  
     
    public void onSensorChanged(SensorEvent event) {  
  
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {  
            return;  
        } 
    	long currentUpdateTime = System.currentTimeMillis(); 
    	//���μ���ʱ����	 
    	long interval = currentUpdateTime - lastUpdateTime;   
    	//�ж��Ƿ�ﵽ�˼��ʱ���� 
    	if(interval < INTERVAL_TIME) 
    	return; 
    	lastUpdateTime = currentUpdateTime; 
    	//���x,y,z���� 
    	float x = event.values[0]; 
    	float y = event.values[1]; 
    	float z = event.values[2]; 
         //���x,y,z�ı仯ֵ
          float changeX = x - lastX;
          float changeY = y - lastY;
          float changeZ = z - lastZ;

          lastX = x;
          lastY = y;
          lastZ = z;
         double speed = Math.sqrt(changeX*changeX + changeY*changeY + changeZ*changeZ)/interval * 10000;
         //�ﵽ�ٶȷ�ֵ��������ʾ
         if(speed >= SPEED_SHRESHOLD)
         onShakeListener.onShake(); 
    } 
}
