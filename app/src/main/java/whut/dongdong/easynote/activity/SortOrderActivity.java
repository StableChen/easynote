package whut.dongdong.easynote.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.SPUtil;

public class SortOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_order);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        View byCreateTime = findViewById(R.id.order_by_create_time);
        View byUpdateTime = findViewById(R.id.order_by_update_time);
        final View checkByCreate = findViewById(R.id.check_by_create);
        final View checkByUpdate = findViewById(R.id.check_by_update);

        int sortOrder = SPUtil.getInt(this, Constant.SORT_ORDER);
        if (sortOrder == -1 || sortOrder == Constant.SORT_BY_CREATE_TIME) {
            checkByCreate.setVisibility(View.VISIBLE);
            checkByUpdate.setVisibility(View.INVISIBLE);
        } else if (sortOrder == Constant.SORT_BY_UPDATE_TIME) {
            checkByCreate.setVisibility(View.INVISIBLE);
            checkByUpdate.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        byCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkByCreate.setVisibility(View.VISIBLE);
                checkByUpdate.setVisibility(View.INVISIBLE);
                SPUtil.put(SortOrderActivity.this, Constant.SORT_ORDER, Constant.SORT_BY_CREATE_TIME);
            }
        });
        byUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkByCreate.setVisibility(View.INVISIBLE);
                checkByUpdate.setVisibility(View.VISIBLE);
                SPUtil.put(SortOrderActivity.this, Constant.SORT_ORDER, Constant.SORT_BY_UPDATE_TIME);
            }
        });
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
