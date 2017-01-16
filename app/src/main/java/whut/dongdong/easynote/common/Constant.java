package whut.dongdong.easynote.common;

import android.os.Environment;

/**
 * Created by dongdong on 2016/12/27.
 */

public interface Constant {

    int TYPE_EASY_NOTE = 0;
    int TYPE_SECRET_NOTE = 1;

    String SORT_ORDER = "sort_order";
    int SORT_BY_CREATE_TIME = 0;
    int SORT_BY_UPDATE_TIME = 1;

    String SECRET_NOTE_PASSWORD = "secret_note_password";
    String SHOULD_CHECK_PASSWORD = "should_check_password";

    String DEFAULT_IMAGE = "default_image";

    String NOTE_IMAGE_PATH = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

}
