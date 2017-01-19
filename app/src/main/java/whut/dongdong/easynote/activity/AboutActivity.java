package whut.dongdong.easynote.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import whut.dongdong.easynote.R;

public class AboutActivity extends BaseActivity {

    String about = "简记EasyNote是一款开源记事本app, 用户可以设置普通的EasyNote和私密的SecretNote, "
            + "并为SecretNote添加密码; \n" + "可以为每一条Note设置一张背景图片, "
            + "可以随手一拍, 也可以从相册中选取, 当然系统也提供了多张精美壁纸以供选择; \n"
            + "用户还可以将Note设置为提醒, Note将会以通知的形式提醒用户待办事项; \n"
            + "不过, 需要注意的是, 所有Note均存储在本地, 如将app删除数据也会一并丢失, "
            + "所以重要数据请另行妥善保存. \n\n\n"
            + "author: StableChen \n"
            + "email: StableChen@126.com \n"
            + "github: https://github.com/StableChen/easynote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        TextView tvAbout = (TextView) findViewById(R.id.tv_about);
        tvAbout.setText(about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
