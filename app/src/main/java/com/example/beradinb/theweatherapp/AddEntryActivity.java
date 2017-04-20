package com.example.beradinb.theweatherapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beradinb.theweatherapp.db.DatabaseOpenHelper;


public class AddEntryActivity extends AppCompatActivity {

    private EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        titleEditText = (EditText) findViewById(R.id.editTextTitle);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    public void add() {
        String title = titleEditText.getText().toString().trim();

        if(title.isEmpty()) {
            Toast.makeText(this, "The 'title'sections must be non-empty", Toast.LENGTH_SHORT).show();
        } else {
            // store in DB
            DatabaseOpenHelper doh = new DatabaseOpenHelper(this);
            SQLiteDatabase db = doh.getWritableDatabase(); // note we get a ‘writable’ DB
            ContentValues contentValues = new ContentValues(); // a map data structure
            contentValues.put("title", title);
            db.insert("entries", null, contentValues);
            finish(); // causes this activity to terminate, and return to the parent one
        }
    }

}

