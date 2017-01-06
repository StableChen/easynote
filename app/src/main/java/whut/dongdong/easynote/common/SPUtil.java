package whut.dongdong.easynote.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dongdong on 2017/1/6.
 */

public class SPUtil {

    public static SharedPreferences getSP(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp;
    }

    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        }
        edit.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getString(key, "");
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getBoolean(key, false);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getInt(key, -1);
    }

}
