package whut.dongdong.easynote.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.bean.Note;
import whut.dongdong.easynote.common.Constant;

public class NoteDetailActivity extends AppCompatActivity {

    private LocationClient locationClient;

    private Note note;
    private EditText noteContent;
    private TextView noteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        getNoteInfo();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                requestLocation();
                break;
        }
    }

    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    private void getNoteInfo() {
        int noteId = getIntent().getIntExtra("note_id", -1);
        if (noteId == -1) {
            String noteTitle = getIntent().getStringExtra("note_title");
            int noteType = getIntent().getIntExtra("note_type", -1);
            note = new Note();
            note.setTitle(noteTitle);
            note.setSecret(noteType == Constant.TYPE_SECRET_NOTE ? true : false);
            note.setCreateTime(System.currentTimeMillis());
            note.save();
            initLocationClient();
        } else {
            note = DataSupport.where("id = ?", noteId + "").find(Note.class).get(0);
        }
    }

    private void initLocationClient() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new LocationListener());

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        note.setUpdateTime(System.currentTimeMillis());
        note.setContent(noteContent.getText().toString());
        note.save();
    }

    private void initView() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView noteImage = (ImageView) findViewById(R.id.note_image);
        FloatingActionButton fabSetImage = (FloatingActionButton) findViewById(R.id.fab_set_image);
        TextView noteTime = (TextView) findViewById(R.id.note_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        noteTime.setText("create :" + dateFormat.format(note.getCreateTime()) +
                (note.getUpdateTime() == 0 ? "" : "\n" + "update:" + dateFormat.format(note.getUpdateTime())));
        noteLocation = (TextView) findViewById(R.id.note_location);
        noteLocation.setText(note.getLocation() == null ? "" : note.getLocation());
        noteContent = (EditText) findViewById(R.id.note_content);
        noteContent.setText(note.getContent());

        collapsingToolbar.setTitle(note.getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (note.getImageUrl() == null) {
            Glide.with(this).load(R.drawable.default_note_image).into(noteImage);
        } else {
            Glide.with(this).load(note.getImageUrl()).into(noteImage);
        }

        fabSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetImageDialog();
            }
        });
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

    private void showSetImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_set_image_dialog, null);
        dialog.setView(dialogView);
        View takePhoto = dialogView.findViewById(R.id.take_photo);
        View choosePicture = dialogView.findViewById(R.id.choose_picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            note.setLocation(bdLocation.getCity() + " " + bdLocation.getDistrict()
                    + " " + bdLocation.getStreet());
            noteLocation.setText(note.getLocation());
            note.save();
        }
    }
}
