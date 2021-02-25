package az.gov.e_health.esehiyye;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import az.gov.e_health.esehiyye.Model.CovidStruct;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;

public class ShowNotification extends BroadcastReceiver {
DbSelect select = new DbSelect();
    // private final static String TAG = "az.gov.e_health.esehiyye.ShowNotification";

//    @Override
//    public void onCreate() {
//        super.onCreate();
//
////        Intent mainIntent = new Intent(this, MainActivity.class);
////
////        NotificationManager notificationManager
////                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
////
////        Notification noti = new NotificationCompat.Builder(this)
////                .setAutoCancel(true)
////                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
////                        PendingIntent.FLAG_UPDATE_CURRENT))
////                .setContentTitle("HELLO " + System.currentTimeMillis())
////                .setContentText("PLEASE CHECK WE HAVE UPDATED NEWS")
////                .setDefaults(Notification.DEFAULT_ALL)
////                .setSmallIcon(R.drawable.ic_launcher_background)
////                .setTicker("ticker message")
////                .setWhen(System.currentTimeMillis())
////                .build();
////
////        notificationManager.notify(0, noti);
//
//       // Log.i(TAG, "Notification created");
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
//
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setTicker("Hearty365")
//                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
//                .setContentTitle("Default notification")
//                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
//                .setContentInfo("Info");
//
//        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notificationBuilder.build());
//    }


    @Override
    public void onReceive(final Context context, Intent intent) {
//        final String[] values = new String[]{
//                "Artıq tibbi sənədlərinizi tətbiqdə 'Tibbi sənədlərim' bölməsinə yükləyə bilərsiniz",
//                "Dərman vasitələri haqqında məlumat almaq üçün müvafiq bölüməyə daxil olaraq məlumat ala bilərsiniz",
//                "Ən son xəbərləri 'Xəbərlər' bölməsindən izləyə bilərsiniz",
//                "İmmunoprofilaktika barədə məlumat almaq üçün müvafiq bölümdən istifadə edə bilərsiniz",
//                "Həkim tərəfindən sizə təyin olunmuş reseptləri əsas səhifədəki müvafiq bölümdən görə bilərsiniz",
//                "Tətbiqimizdə, Azərbaycanda fəaliyyət göstərən bütün tibb müəssisələri, aptek, poliklinikalar və sair səhiyyə obyektləri barədə ətraflı məlumat ala bilərsiniz"
//
//
//        };

//        Random rand = new Random();
//        int rand_int1 = rand.nextInt(values.length);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CovidStruct>  covidList = select.GetCovidLive();

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID");

//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                // .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.es_logo)
//                .setTicker("Hearty365")
//                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
//                .setContentTitle("Bilirdinizmi?")
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(values[rand_int1]))
//                .setContentText(values[rand_int1])
//                .setContentInfo("Info");
                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        // .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.es_logo)
                        .setTicker("Hearty365")
                        .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                        .setContentTitle("COVID-19: AZƏRBAYCANDA CARİ VƏZİYYƏT")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Aktiv xəstə sayı: "+covidList.get(covidList.size()-1).Active+"\nÖlüm halı: "+covidList.get(covidList.size()-1).Deaths))
                        .setContentText("")
                        .setContentInfo("Info");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(200, notificationBuilder.build());
            }
        }).start();
    }
}