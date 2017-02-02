package whut.dongdong.easynote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.adapter.NoteAdapter;
import whut.dongdong.easynote.bean.Note;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.SPUtil;
import whut.dongdong.easynote.common.ToastUtil;

/**
 * Created by dongdong on 2016/12/27.
 */

public class SecretNoteFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private LinearLayout passwordLayout;
    private EditText etPassword;
    private Button btConfirm;
    private View tvEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secret_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        passwordLayout = (LinearLayout) view.findViewById(R.id.password_layout);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btConfirm = (Button) view.findViewById(R.id.bt_confirm);
        tvEmpty = view.findViewById(R.id.tv_empty);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        int sortOrder = SPUtil.getInt(getActivity(), Constant.SORT_ORDER);
        if (sortOrder == -1 || sortOrder == Constant.SORT_BY_CREATE_TIME) {
            noteList = DataSupport.where("isSecret = ?", "1").order("createTime desc").find(Note.class);
        } else if (sortOrder == Constant.SORT_BY_UPDATE_TIME) {
            noteList = DataSupport.where("isSecret = ?", "1").order("updateTime desc").find(Note.class);
        }
        if (noteList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
        noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                if (!password.equals(SPUtil.getString(getActivity(), Constant.SECRET_NOTE_PASSWORD))) {
                    ToastUtil.showToast(getActivity(), "密码错误");
                } else {
                    passwordLayout.setVisibility(View.GONE);
                    SPUtil.put(getActivity(), Constant.SHOULD_CHECK_PASSWORD, false);
                }
            }
        });
        checkPassword();
    }

    private void checkPassword() {
        if (SPUtil.getString(getActivity(), Constant.SECRET_NOTE_PASSWORD).equals("")) {
            passwordLayout.setVisibility(View.GONE);
        } else {
            if (SPUtil.getBoolean(getActivity(), Constant.SHOULD_CHECK_PASSWORD)) {
                passwordLayout.setVisibility(View.VISIBLE);
            } else {
                passwordLayout.setVisibility(View.GONE);
            }
        }
    }

}
