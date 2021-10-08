package com.example.labs;

import static com.example.labs.MainFragment.SearchArrayResult;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

public class SearchActivityResult extends AppCompatActivity {
    ArrayAdapter<JSONObject> adapter = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        listView = findViewById(R.id.searchListView);
        adapter = new FruitAdapter(this, R.layout.raw, R.id.name, SearchArrayResult);
        listView.setAdapter(adapter);
    }
}