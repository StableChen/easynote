package whut.dongdong.easynote.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.bean.Note;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.PermissionListener;

public class NoteDetailActivity extends BaseActivity {

    private LocationClient locationClient;

    private Note note;
    private EditText noteContent;
    private TextView noteLocation;
    private ImageView noteImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private boolean isEdit;
    private boolean hasEdit;

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
            String noteTitle = getIntent().getStringExtra("note_title");
            int noteType = getIntent().getIntExtra("note_type", -1);
            note = new Note();
            note.setTitle(noteTitle);
            note.setSecret(noteType == Constant.TYPE_SECRET_NOTE ? true : false);
            note.setCreateTime(System.currentTimeMillis());
            note.save();
            isEdit = true;
            initLocationClient();
        } else {
            note = DataSupport.where("id = ?", noteId + "").find(Note.class).get(0);
        }
    }

    private void initView() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noteImage = (ImageView) findViewById(R.id.note_image);
        FloatingActionButton fabSetImage = (FloatingActionButton) findViewById(R.id.fab_set_image);
        TextView noteTime = (TextView) findViewById(R.id.note_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        noteTime.setText("创建时间: " + dateFormat.format(note.getCreateTime()) +
                (note.getUpdateTime() == 0 ? "" : "\n" + "更新时间: " + dateFormat.format(note.getUpdateTime())));
        noteLocation = (TextView) findViewById(R.id.note_location);
        noteLocation.setText(note.getLocation() == null ? "" : note.getLocation());
        noteContent = (EditText) findViewById(R.id.note_content);
        noteContent.setText(note.getContent());
        if (isEdit) {
            noteContent.setEnabled(true);
            noteContent.setSelection(noteContent.getText().length());
        } else {
            noteContent.setEnabled(false);
        }

        noteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setContent(s.toString());
                if (!hasEdit) hasEdit = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    private void initLocationClient() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new LocationListener());

        requestRuntimePermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        requestLocation();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissions) {
                        Toast.makeText(NoteDetailActivity.this, "拒绝权限无法显示当前位置", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasEdit) {
            note.setUpdateTime(System.currentTimeMillis());
            note.setContent(noteContent.getText().toString());
            note.save();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                if (isEdit) {
                    noteContent.setEnabled(false);
                    isEdit = false;
                } else {
                    noteContent.setEnabled(true);
                    noteContent.setSelection(noteContent.getText().length());
                    isEdit = true;
                }
                break;
            case R.id.change_title:
                showChangeTitleDialog();
                break;
            case R.id.set_notify:

                break;
        }
        return true;
    }

    private void showChangeTitleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_change_title_dialog, null);
        dialog.setView(dialogView);
        final EditText etTitle = (EditText) dialogView.findViewById(R.id.et_title);
        View cancel = dialogView.findViewById(R.id.cancel);
        View confirm = dialogView.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                if (!title.equals("")) {
                    collapsingToolbar.setTitle(title);
                    note.setTitle(title);
                    hasEdit = true;
                    dialog.dismiss();
                } else {
                    Toast.makeText(NoteDetailActivity.this, "请输入新标题", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private Uri imageUri;

    private void showSetImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_set_image_dialog, null);
        dialog.setView(dialogView);
        View takePhoto = dialogView.findViewById(R.id.take_photo);
        View choosePicture = dialogView.findViewById(R.id.choose_picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File noteImage = new File(Constant.NOTE_IMAGE_PATH, "image_" + System.currentTimeMillis() + ".png");
                imageUri = Uri.fromFile(noteImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 0);
                dialog.dismiss();
            }
        });
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File noteImage = new File(Constant.NOTE_IMAGE_PATH, "image_" + System.currentTimeMillis() + ".png");
                imageUri = Uri.fromFile(noteImage);
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    if (data != null) {
                        intent.setDataAndType(data.getData(), "image/*");
                    } else {
                        intent.setDataAndType(imageUri, "image/*");
                    }
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Glide.with(this).load(imageUri).into(noteImage);
                    if (note.getImageUrl() != null) {
                        File oldImage = new File(note.getImageUrl());
                        if (oldImage.exists()) {
                            oldImage.delete();
                        }
                    }
                    note.setImageUrl(imageUri.getPath());
                    note.save();
                    hasEdit = true;
                }
                break;
        }
    }

}
