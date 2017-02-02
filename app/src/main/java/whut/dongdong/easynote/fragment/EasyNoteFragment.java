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

public class EasyNoteFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private View tvEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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
            noteList = DataSupport.where("isSecret = ?", "0").order("createTime desc").find(Note.class);
        } else if (sortOrder == Constant.SORT_BY_UPDATE_TIME) {
            noteList = DataSupport.where("isSecret = ?", "0").order("updateTime desc").find(Note.class);
        }
        if (noteList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
        noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
    }

}
