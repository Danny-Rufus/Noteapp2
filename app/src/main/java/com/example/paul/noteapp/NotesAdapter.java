package com.example.paul.noteapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Created by PAUL on 16-04-2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private List<Note> noteList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView note;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if( mListener != null){
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }

                }


            });
        }

    }

public NotesAdapter(Context context,List<Note> noteList) {
        this.context=context;
        this.noteList=noteList;

}
@Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row,parent,false);

        return new MyViewHolder(itemView);
}

@Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Note note=noteList.get(position);
        holder.note.setText(note.getNote());

    // Displaying dot from HTML character code
    holder.dot.setText(Html.fromHtml("&#8226;"));

    // Formatting and displaying timestamp
    holder.timestamp.setText(formatDate(note.getTimestamp()));
}
@Override
    public int getItemCount(){
        return noteList.size();
}
private String formatDate(String dateStr){
        try{
            SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date= fmt.parse(dateStr);
            SimpleDateFormat fmtout= new SimpleDateFormat("MMM d");
            return fmtout.format(date);


        }catch (ParseException e){

        } return "";
}

}
