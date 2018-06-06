package com.example.paul.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.example.paul.noteapp.Note.COLUMN_NOTE;
import static com.example.paul.noteapp.Note.COLUMN_TIMESTAMP;


public class MainActivity extends AppCompatActivity implements NotesAdapter.OnItemClickListener {



    private NotesAdapter mAdapter;
    private List<Note> noteList= new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotes;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotes = findViewById(R.id.empty_notes);

        db = new DatabaseHelper(this);

        noteList.addAll(db.getAllNotes());

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, noteList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);

        toggleEmptyNotes();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {


            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionDialog(position);
            }
        }));
    }

    //CREATING A NOTE

    private void createNote(String  note){
        long id=db.insertNote(note);
        Note n=db.getNote(id);
        if(n!=null){
            noteList.add(0,n);
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }

    }

    // UPDATING A NOTE

    private void updateNote(String note, int position){
        Note n= noteList.get(position);
        n.setNote(note);
        db.updateNote(n);

        noteList.set(position,n);
        mAdapter.notifyItemChanged(position);
        toggleEmptyNotes();
    }

    //DELETING A NOTE

    public void deleteNote(int position){
        db.deleteNote(noteList.get(position));
        noteList.remove(position);
        mAdapter.notifyItemRemoved(position);
        toggleEmptyNotes();
    }


public void  showActionDialog(final int position){
        CharSequence colors[]= new CharSequence[]{"Edit", "Delete"};

    AlertDialog.Builder builder= new AlertDialog.Builder(this);
    builder.setTitle("choose option");
    builder.setItems(colors, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i==0){
                showNoteDialog(true,noteList.get(position),position);
            }else {
                deleteNote(position);
            }
        }
    });
builder.show();
}

//OPENING NOTE DIALOG TO ENTER NEW NOTES

    private void showNoteDialog(final boolean shouldUpdate,final Note note,final int position){

        LayoutInflater layoutInflaterobj=LayoutInflater.from(getApplicationContext());
        View view=layoutInflaterobj.inflate(R.layout.note_dialog,null);

        AlertDialog.Builder builderUserInput=new AlertDialog.Builder(MainActivity.this);
        builderUserInput.setView(view);

        final EditText inputNote=view.findViewById(R.id.note);
        TextView dialogTitle=view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate?getString(R.string.new_note_title):getString(R.string.edit_note));

        if (shouldUpdate&& note!=null){
            inputNote.setText(note.getNote());

        }
        builderUserInput.setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog alertDialog= builderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(inputNote.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter Note!", Toast.LENGTH_SHORT).show();
                   return;
                }else {
                    alertDialog.dismiss();
                }
                if (shouldUpdate && note!=null){
                    updateNote(inputNote.getText().toString(),position);
                }else {
                    createNote(inputNote.getText().toString());
                }

            }
        });
    }


    private void toggleEmptyNotes() {

        if (db.getNotesCount()>0){
            noNotes.setVisibility(View.GONE);
        }else {
            noNotes.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent singleIntent = new Intent(this,SingleNote.class);
        Note clickedItem = noteList.get(position);

       // singleIntent.putExtra(COLUMN_ID,clickedItem.getId());
        singleIntent.putExtra(COLUMN_NOTE,clickedItem.getNote());
        singleIntent.putExtra(COLUMN_TIMESTAMP,formatDate(clickedItem.getTimestamp()));

        startActivity(singleIntent);

    }
    private String formatDate(String dateStr){
        try{
            SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date= fmt.parse(dateStr);
            SimpleDateFormat fmtout= new SimpleDateFormat(" d MMM yy");
            return fmtout.format(date);


        }catch (ParseException e){

        } return "";
    }
}
