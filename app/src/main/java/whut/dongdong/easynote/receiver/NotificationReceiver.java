package whut.dongdong.easynote.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.litepal.crud.DataSupport;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.bean.Note;

/**
 * Created by dongdong on 2017/1/19.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int noteId = intent.getIntExtra("noteId", -1);
        Note note = DataSupport.find(Note.class, noteId);
        if (noteId != -1) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(note.getTitle())
                    .setContentText(note.getContent())
                    .setSmallIcon(R.drawable.easynote_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.easynote_logo))
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            manager.notify(noteId, notification);
        }
    }
}
