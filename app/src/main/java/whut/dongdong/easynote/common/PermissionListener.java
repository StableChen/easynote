package whut.dongdong.easynote.common;

import java.util.List;

/**
 * Created by dongdong on 2017/1/4.
 */

public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);

}
