package whut.dongdong.easynote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.crud.DataSupport;

import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.adapter.NoteAdapter;
import whut.dongdong.easynote.bean.Note;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.SPUtil;

/**
 * Created by dongdong on 2016/12/27.
 */

public class SecretNoteFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secret_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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
        noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
    }

}
