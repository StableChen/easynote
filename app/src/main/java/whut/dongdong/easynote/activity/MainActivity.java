package whut.dongdong.easynote.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.fragment.EasyNoteFragment;
import whut.dongdong.easynote.fragment.SecretNoteFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vpMain;
    private View circleIndicator;
    private TextView tvEasyNote;
    private TextView tvSecretNote;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FloatingActionButton fabMain;
    private View createEasyNote;
    private View createSecretNote;
    private View whiteBg;
    private boolean isMenuOpen;
    private int halfWindowWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private boolean isFirstChange = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirstChange) {
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            halfWindowWidth = metrics.widthPixels / 2;
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) circleIndicator.getLayoutParams();
            lp.leftMargin = halfWindowWidth / 2 - circleIndicator.getWidth() / 2;
            circleIndicator.setLayoutParams(lp);
            isFirstChange = false;
        }
    }

    private void initView() {
        vpMain = (ViewPager) findViewById(R.id.vp_main);
        circleIndicator = findViewById(R.id.circle_indicator);
        tvEasyNote = (TextView) findViewById(R.id.tv_easy_note);
        tvSecretNote = (TextView) findViewById(R.id.tv_secret_note);
        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        createEasyNote = findViewById(R.id.create_easy_note);
        createSecretNote = findViewById(R.id.create_secret_note);
        whiteBg = findViewById(R.id.view_white_bg);

        tvEasyNote.setOnClickListener(this);
        tvSecretNote.setOnClickListener(this);
        fabMain.setOnClickListener(this);
        createEasyNote.setOnClickListener(this);
        createSecretNote.setOnClickListener(this);
        whiteBg.setOnClickListener(this);

        fragmentList.add(new EasyNoteFragment());
        fragmentList.add(new SecretNoteFragment());

        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) circleIndicator.getLayoutParams();
                lp.leftMargin = halfWindowWidth / 2 - circleIndicator.getWidth() / 2 + (int) ((position + positionOffset) * halfWindowWidth);
                circleIndicator.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvEasyNote.setTextColor(Color.parseColor("#3F51B5"));
                        tvSecretNote.setTextColor(Color.parseColor("#AAAAAA"));
                        break;
                    case 1:
                        tvSecretNote.setTextColor(Color.parseColor("#3F51B5"));
                        tvEasyNote.setTextColor(Color.parseColor("#AAAAAA"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_easy_note:
                vpMain.setCurrentItem(0);
                tvEasyNote.setTextColor(Color.parseColor("#3F51B5"));
                tvSecretNote.setTextColor(Color.parseColor("#AAAAAA"));
                break;
            case R.id.tv_secret_note:
                vpMain.setCurrentItem(1);
                tvSecretNote.setTextColor(Color.parseColor("#3F51B5"));
                tvEasyNote.setTextColor(Color.parseColor("#AAAAAA"));
                break;
            case R.id.fab_main:
                if (isMenuOpen) {
                    closeMenuAnim();
                } else {
                    openMenuAnim();
                }
                break;
            case R.id.view_white_bg:
                closeMenuAnim();
                break;
            case R.id.create_easy_note:
                closeMenuAnim();
                showCreateNoteDialog(Constant.TYPE_EASY_NOTE);
                break;
            case R.id.create_secret_note:
                closeMenuAnim();
                showCreateNoteDialog(Constant.TYPE_SECRET_NOTE);
                break;
        }
    }

    private void showCreateNoteDialog(final int noteType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.view_create_note_dialog, null);
        dialog.setView(dialogView);
        final EditText etTitle = (EditText) dialogView.findViewById(R.id.et_title);
        View cancel = dialogView.findViewById(R.id.cancel);
        View create = dialogView.findViewById(R.id.create);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                if (!title.equals("")) {
                    Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                    intent.putExtra("note_title", title);
                    intent.putExtra("note_type", noteType);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "please set a title", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void openMenuAnim() {
        whiteBg.setVisibility(View.VISIBLE);
        createEasyNote.setVisibility(View.VISIBLE);
        createSecretNote.setVisibility(View.VISIBLE);

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(fabMain, "rotation", 0, 225);
        ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(createEasyNote, "scaleX", 0, 1);
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(createSecretNote, "scaleX", 0, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator, scaleAnimator1, scaleAnimator2);
        animatorSet.start();
        isMenuOpen = true;
    }

    private void closeMenuAnim() {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(fabMain, "rotation", 225, 0);
        ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(createEasyNote, "scaleX", 1, 0);
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(createSecretNote, "scaleX", 1, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator, scaleAnimator1, scaleAnimator2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                whiteBg.setVisibility(View.GONE);
                createEasyNote.setVisibility(View.GONE);
                createSecretNote.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        isMenuOpen = false;
    }

    private long currentTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - currentTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "press again to quit", Toast.LENGTH_SHORT).show();
            currentTime = System.currentTimeMillis();
        }
    }
}
