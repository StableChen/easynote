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

/**
 * Created by dongdong on 2016/12/27.
 */

public class EasyNoteFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        noteList = DataSupport.where("isSecret = ?", "0").find(Note.class);
        noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
    }

}
