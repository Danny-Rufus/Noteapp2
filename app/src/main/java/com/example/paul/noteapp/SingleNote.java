package com.example.paul.noteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.paul.noteapp.Note.COLUMN_ID;
import static com.example.paul.noteapp.Note.COLUMN_NOTE;
import static com.example.paul.noteapp.Note.COLUMN_TIMESTAMP;

public class SingleNote extends AppCompatActivity implements NotesAdapter.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        Intent i =  getIntent();
       // int id  = i.getIntExtra(COLUMN_ID,0);
        String note = i.getStringExtra(COLUMN_NOTE);
        String timestamp = i.getStringExtra(COLUMN_TIMESTAMP);


        TextView timeStampText = findViewById(R.id.timestamp_single);
        TextView notesText = findViewById(R.id.note_single);

        timeStampText.setText(timestamp);
        notesText.setText(note);

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Clicked" + position, Toast.LENGTH_SHORT).show();
    }
}
