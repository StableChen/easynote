package whut.dongdong.easynote.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;

import whut.dongdong.easynote.R;
import whut.dongdong.easynote.activity.NoteDetailActivity;
import whut.dongdong.easynote.bean.Note;
import whut.dongdong.easynote.common.Constant;
import whut.dongdong.easynote.common.SPUtil;

/**
 * Created by dongdong on 2016/12/29.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.view_note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("note_id", noteList.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteNoteDialog(noteList.get(holder.getAdapterPosition()));
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note.getImageUrl() == null) {
            Glide.with(context).load(R.drawable.default_note_image).into(holder.noteImage);
        } else {
            Glide.with(context).load(note.getImageUrl()).into(holder.noteImage);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        int sortOrder = SPUtil.getInt(context, Constant.SORT_ORDER);
        if (sortOrder == -1 || sortOrder == Constant.SORT_BY_CREATE_TIME) {
            holder.noteTime.setText(dateFormat.format(note.getCreateTime()));
        } else if (sortOrder == Constant.SORT_BY_UPDATE_TIME) {
            holder.noteTime.setText(dateFormat.format(note.getUpdateTime()));
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    private void showDeleteNoteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.view_delete_note_dialog, null);
        dialog.setView(dialogView);
        View cancel = dialogView.findViewById(R.id.cancel);
        View delete = dialogView.findViewById(R.id.delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.delete(Note.class, note.getId());
                notifyItemRemoved(noteList.indexOf(note));
                noteList.remove(note);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView noteImage;
        TextView noteTime;

        public ViewHolder(View itemView) {
            super(itemView);
            noteImage = (ImageView) itemView.findViewById(R.id.note_image);
            noteTime = (TextView) itemView.findViewById(R.id.note_time);
        }
    }
}
