package whut.dongdong.easynote.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdong on 2016/12/9.
 */

public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static Activity getTopActivity() {
        if (activityList.isEmpty()) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }

}
