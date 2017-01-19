package whut.dongdong.easynote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import whut.dongdong.easynote.R;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        View sortOrder = findViewById(R.id.sort_order);
        View chooseDefaultImage = findViewById(R.id.choose_default_image);
        View secretNotePassword = findViewById(R.id.secret_note_password);
        View about = findViewById(R.id.about);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        sortOrder.setOnClickListener(this);
        chooseDefaultImage.setOnClickListener(this);
        secretNotePassword.setOnClickListener(this);
        about.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_order:
                startActivity(new Intent(this, SortOrderActivity.class));
                break;
            case R.id.secret_note_password:
                startActivity(new Intent(this, SetPasswordActivity.class));
                break;
            case R.id.choose_default_image:
                startActivity(new Intent(this, DefaultImageActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

}
