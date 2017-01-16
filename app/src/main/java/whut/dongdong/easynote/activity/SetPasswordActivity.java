package whut.dongdong.easynote.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.SPUtil;
import whut.dongdong.easynote.common.ToastUtil;

public class SetPasswordActivity extends BaseActivity {

    private EditText etSetPassword;
    private EditText etConfirmPassword;
    private Button btSetPassword;
    private Button btChangePassword;
    private Button btCancelPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

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

        etSetPassword = (EditText) findViewById(R.id.et_set_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btSetPassword = (Button) findViewById(R.id.bt_set_password);
        btChangePassword = (Button) findViewById(R.id.bt_change_password);
        btCancelPassword = (Button) findViewById(R.id.bt_cancel_password);

        if (SPUtil.getString(this, Constant.SECRET_NOTE_PASSWORD).equals("")) {
            btChangePassword.setVisibility(View.GONE);
            btCancelPassword.setVisibility(View.GONE);
        } else {
            etSetPassword.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
            btSetPassword.setVisibility(View.GONE);
        }

        btSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setPassword = etSetPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (setPassword.equals("") || confirmPassword.equals("")) {
                    ToastUtil.showToast(SetPasswordActivity.this, "输入信息不全");
                    return;
                }
                if (!setPassword.equals(confirmPassword)) {
                    ToastUtil.showToast(SetPasswordActivity.this, "两次密码输入不一致");
                    return;
                }
                SPUtil.put(SetPasswordActivity.this, Constant.SECRET_NOTE_PASSWORD, setPassword);
                Snackbar.make(v, "密码设置成功", Snackbar.LENGTH_SHORT).show();
                etSetPassword.setVisibility(View.GONE);
                etConfirmPassword.setVisibility(View.GONE);
                btSetPassword.setVisibility(View.GONE);
                btChangePassword.setVisibility(View.VISIBLE);
                btCancelPassword.setVisibility(View.VISIBLE);

            }
        });

        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        btCancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelPasswordDialog();
            }
        });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_change_password_dialog, null);
        dialog.setView(dialogView);
        final EditText etCurrentPassword = (EditText) dialogView.findViewById(R.id.et_current_password);
        final EditText etNewPassword = (EditText) dialogView.findViewById(R.id.et_new_password);
        final EditText etConfirmPassword = (EditText) dialogView.findViewById(R.id.et_confirm_password);
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
                String currentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (!currentPassword.equals(SPUtil.getString(SetPasswordActivity.this, Constant.SECRET_NOTE_PASSWORD))) {
                    ToastUtil.showToast(SetPasswordActivity.this, "当前密码错误");
                } else if (newPassword.equals("") || confirmPassword.equals("")) {
                    ToastUtil.showToast(SetPasswordActivity.this, "信息输入不全");
                } else if (!newPassword.equals(confirmPassword)) {
                    ToastUtil.showToast(SetPasswordActivity.this, "两次新密码输入不一致");
                } else {
                    SPUtil.put(SetPasswordActivity.this, Constant.SECRET_NOTE_PASSWORD, newPassword);
                    Snackbar.make(btChangePassword, "密码修改成功", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void showCancelPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_cancel_password_dialog, null);
        dialog.setView(dialogView);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.et_password);
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
                String password = etPassword.getText().toString().trim();
                if (!password.equals(SPUtil.getString(SetPasswordActivity.this, Constant.SECRET_NOTE_PASSWORD))) {
                    ToastUtil.showToast(SetPasswordActivity.this, "密码错误");
                } else {
                    SPUtil.put(SetPasswordActivity.this, Constant.SECRET_NOTE_PASSWORD, "");
                    Snackbar.make(btCancelPassword, "密码取消成功", Snackbar.LENGTH_SHORT).show();
                    etSetPassword.setVisibility(View.VISIBLE);
                    etConfirmPassword.setVisibility(View.VISIBLE);
                    btSetPassword.setVisibility(View.VISIBLE);
                    btChangePassword.setVisibility(View.GONE);
                    btCancelPassword.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
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
