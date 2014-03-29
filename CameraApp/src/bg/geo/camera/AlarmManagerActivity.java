package bg.geo.camera;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
 
public class AlarmManagerActivity extends Activity {
 
 private AlarmManagerBroadcastReceiver alarm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        alarm = new AlarmManagerBroadcastReceiver();
    }
     
    @Override
 protected void onStart() {
  super.onStart();
 }
 
    public void startRepeatingTimer(View view) {
     Context context = this.getApplicationContext();
     if(alarm != null){
      alarm.setAlarm(context);
     }else{
      Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
     }
    }
     
    public void cancelRepeatingTimer(View view){
     Context context = this.getApplicationContext();
     if(alarm != null){
      alarm.cancelAlarm(context);
     }else{
      Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
     }
    }
     
    public void onetimeTimer(View view){
     Context context = this.getApplicationContext();
     if(alarm != null){
      alarm.setOnetimeTimer(context);
     }else{
      Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
     }
    }
     
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_manager, menu);
        return true;
    }
}