package whut.dongdong.easynote.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import whut.dongdong.easynote.R;

public class NoteDetailActivity extends AppCompatActivity {

    private String noteTitle;
    private int noteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        getNoteInfo();
        initView();
    }

    private void getNoteInfo() {
        int noteId = getIntent().getIntExtra("note_id", -1);
        if (noteId == -1) {
            noteTitle = getIntent().getStringExtra("note_title");
            noteType = getIntent().getIntExtra("note_type", -1);
        }
    }

    private void initView() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView noteImage = (ImageView) findViewById(R.id.note_image);

        collapsingToolbar.setTitle(noteTitle);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Glide.with(this).load(R.drawable.default_note_image).into(noteImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
