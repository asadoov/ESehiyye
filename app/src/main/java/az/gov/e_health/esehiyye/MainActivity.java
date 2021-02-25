package az.gov.e_health.esehiyye;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import az.gov.e_health.esehiyye.ui.main.AppFragment;

import static android.app.AlarmManager.INTERVAL_DAY;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_WRITE_PERMISSION = 786;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        requestPermission();

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, ShowNotification.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //  am.cancel(contentIntent);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_DAY/2 , INTERVAL_DAY, contentIntent);

        //am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5000,contentIntent);
//am.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5000,contentIntent);
        //Toast.makeText(this, "Rem set!", Toast.LENGTH_SHORT).show();

        AppRater.app_launched(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AppFragment.newInstance())
                    .commitNow();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // openFilePicker();
        }
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //  openFilePicker();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Yeniliklər";
            String description = "Yeniliklər barədə bildirişlər";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("M_CH_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
