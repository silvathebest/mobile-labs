package com.example.labs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivityResult extends AppCompatActivity {
    ArrayAdapter<String> adapter = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Bundle data = getIntent().getExtras();
        listView = findViewById(R.id.searchListView);
        if (data.containsKey("SearchResult")) {
            Object results = data.get("SearchResult");
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) results);
            listView.setAdapter(adapter);
        }
    }
}