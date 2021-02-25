package az.gov.e_health.esehiyye;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

public class AppRater {
    private final static String APP_TITLE = "Elektron Səhiyyə";// App Name
    private final static String APP_PNAME = "az.gov.e_health.esehiyye";// Package Name

    ReviewManager manager;
    ReviewInfo reviewInfo;
    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    private void initReviewInfo(final Context mContext) {
        manager = ReviewManagerFactory.create(mContext);
        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                } else {
                }
            }
        });
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

        dialog.setTitle("\"" + APP_TITLE + "\"-nin istifadəsindən zövq ala bildiniz?");
        dialog.setMessage("\nBir dəqiqənizi ayırıb tətbiqimizi qiymətləndirsəniz, bizə çox xoş olar :)\n\nDəstəyiniz üçün təşəkkür edirik!");
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        // tv.setText("Zəhmət olmasa bir dəqiqənizi ayırıb \"" + APP_TITLE + "\"-ni, qiymətləndirin. Dəstəyiniz üçün təşəkkür edirik!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setAllCaps(false);
        b1.setText("\"" + APP_TITLE + "\"-ni qiymətləndir");
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setAllCaps(false);
        b2.setText("Sonra, xatırlat");
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setAllCaps(false);
        b3.setText("Xeyr");
        ll.addView(b3);

        dialog.setView(ll);
        final AlertDialog ad = dialog.show();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                ad.dismiss();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                ad.dismiss();
            }
        });


    }
}