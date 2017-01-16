package whut.dongdong.easynote.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dongdong on 2017/1/15.
 */

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
