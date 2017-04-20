package com.example.beradinb.theweatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beradinb.theweatherapp.db.DatabaseOpenHelper;

public class FavActivity extends AppCompatActivity {

    private ListView listView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        listView = (ListView) findViewById(R.id.cityview);
        Button addEntryButton = (Button) findViewById(R.id.addEntry);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavActivity.this, AddEntryActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // we first need a database open helper to even touch the DB...
        DatabaseOpenHelper doh = new DatabaseOpenHelper(this);
        // we then get a readable handler to the DB...
        SQLiteDatabase db = doh.getReadableDatabase();
        // and then we run a raw SQL query which returns a cursor pointing to the results
        Cursor cursor = db.rawQuery("SELECT * FROM entries", null);
        // number of rows in the result set
        int numOfRows = cursor.getCount();
        final String [] titles = new String[numOfRows]; // the titles of the blog entries
        cursor.moveToFirst();
        int columnTitleIndex = cursor.getColumnIndex("title");
        for(int i = 0; i < numOfRows; i++) {
            titles[i] = cursor.getString(columnTitleIndex);
            cursor.moveToNext();
        }
        cursor.close();

        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String myCity = ((TextView)v).getText().toString();
                Intent cityIntent = new Intent(FavActivity.this, CityActivity.class);
                cityIntent.putExtra("city", myCity);
                startActivity(cityIntent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {

                // delete entry
                deleteEntry(titles[pos]);
                // explicitly update UI after modifying the list view
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(FavActivity.this, "Delete entry with title: " +
                        titles[pos], Toast.LENGTH_SHORT).show();
                Intent refreshIntent = new Intent(FavActivity.this, FavActivity.class);
                startActivity(refreshIntent);
                return false;
            }
        });
    }

    public void deleteEntry(String title) {
        DatabaseOpenHelper doh = new DatabaseOpenHelper(this);
        SQLiteDatabase db = doh.getWritableDatabase();
        db.delete("entries", "title=" + DatabaseUtils.sqlEscapeString(title) + "", null);
    }

}
