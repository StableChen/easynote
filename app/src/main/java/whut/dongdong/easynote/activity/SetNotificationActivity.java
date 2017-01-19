package whut.dongdong.easynote.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.bean.Note;

public class SetNotificationActivity extends BaseActivity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int noteId;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);

        noteId = getIntent().getIntExtra("noteId", -1);
        note = DataSupport.find(Note.class, noteId);
        initView();
    }

    private void initView() {
        final TextView tvNotifyTime = (TextView) findViewById(R.id.tv_notify_time);
        View back = findViewById(R.id.back);
        View cancel = findViewById(R.id.cancel);
        View confirm = findViewById(R.id.confirm);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);

        if (note.getNotifyTime() == 0) {
            tvNotifyTime.setText("没有设置提醒");
        } else {
            tvNotifyTime.setText(dateFormat.format(note.getNotifyTime()));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setNotifyTime(0);
                note.save();
                tvNotifyTime.setText("没有设置提醒");
                cancelNotification();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String notifyTimeStr = year + "-" + (month < 10 ? "0" + month : month) + "-" +
                        (day < 10 ? "0" + day : day) + " " + (hour < 10 ? "0" + hour : hour) + ":" +
                        (minute < 10 ? "0" + minute : minute);
                try {
                    long time = dateFormat.parse(notifyTimeStr).getTime();
                    note.setNotifyTime(time);
                    note.save();
                    tvNotifyTime.setText(notifyTimeStr);
                    setNotification(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNotification(long time) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("whut.dongdong.easynote.NOTIFY_BROADCAST");
        intent.putExtra("noteId", noteId);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(this, noteId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private void cancelNotification() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("whut.dongdong.easynote.NOTIFY_BROADCAST");
        intent.putExtra("noteId", noteId);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(this, noteId, intent, PendingIntent.FLAG_NO_CREATE);
        manager.cancel(pendingIntent);
    }

}
