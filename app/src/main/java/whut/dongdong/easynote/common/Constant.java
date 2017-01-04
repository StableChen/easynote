package whut.dongdong.easynote.common;

import android.os.Environment;

/**
 * Created by dongdong on 2016/12/27.
 */

public interface Constant {

    int TYPE_EASY_NOTE = 0;
    int TYPE_SECRET_NOTE = 1;

    String NOTE_IMAGE_PATH = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

}
