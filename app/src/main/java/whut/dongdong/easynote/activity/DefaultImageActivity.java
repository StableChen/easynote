package whut.dongdong.easynote.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.adapter.ImageAdapter;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.ImageSelectListener;
import whut.dongdong.easynote.common.SPUtil;

public class DefaultImageActivity extends BaseActivity {

    private ImageView defaultImage;

    private int[] imageIdArr = {R.drawable.default_note_image0, R.drawable.default_note_image1,
            R.drawable.default_note_image2, R.drawable.default_note_image3,
            R.drawable.default_note_image4, R.drawable.default_note_image5,
            R.drawable.default_note_image6, R.drawable.default_note_image7};

    private List<Integer> imageIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_image);

        for (int i = 0; i < imageIdArr.length; i++) {
            imageIdList.add(imageIdArr[i]);
        }
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        defaultImage = (ImageView) findViewById(R.id.default_image);
        int imageId = SPUtil.getInt(this, Constant.DEFAULT_IMAGE);
        if (imageId == -1) {
            Glide.with(this).load(R.drawable.default_note_image0).into(defaultImage);
        } else {
            Glide.with(this).load(imageId).into(defaultImage);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new ImageAdapter(imageIdList, new ImageSelectListener() {
            @Override
            public void onSelect(int imageId) {
                Glide.with(DefaultImageActivity.this).load(imageId).into(defaultImage);
                SPUtil.put(DefaultImageActivity.this, Constant.DEFAULT_IMAGE, imageId);
            }
        }));
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
