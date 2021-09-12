package com.example.labs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    Button btnSelectAll;
    Button btnResetAll;
    Button btnShowList;
    TextView textBox;
    TextView selectedTextView;
    ListView list;
    String textItem;
    private static final ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Нашли кнопки и текстбокс
        btnAdd = findViewById(R.id.btnAdd);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnResetAll = findViewById(R.id.btnResetAll);
        btnShowList = findViewById(R.id.btnShowList);
        textBox = findViewById(R.id.textBox);
        selectedTextView = findViewById(R.id.selectedTextView);
        list = findViewById(R.id.list);

        //создаем adapter для добавления записей в listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, listItems);
        list.setAdapter(adapter);

        @SuppressLint({"NonConstantResourceId", "SetTextI18n"}) View.OnClickListener clickButtons = view -> {
            switch (view.getId()) {
                case R.id.btnAdd:
                    textItem = textBox.getText().toString().trim();
                    if(textItem.isEmpty()) return;
                    listItems.add(textItem);
                    adapter.notifyDataSetChanged();
                    textBox.setText("");
                    break;
                case R.id.btnSelectAll:
                    for (int i = 0; i < listItems.size(); i++) {
                        list.setItemChecked(i, true);
                    }
                    break;
                case R.id.btnResetAll:
                    for (int i = 0; i < listItems.size(); i++) {
                        list.setItemChecked(i, false);
                    }
                    break;
                case R.id.btnShowList:
                    SparseBooleanArray selected = list.getCheckedItemPositions();
                    StringBuilder selectedItems = new StringBuilder();
                    for (int i = 0; i < listItems.size(); i++) {
                        if (selected.get(i))
                            selectedItems.append(listItems.get(i)).append(" ");
                    }
                    selectedTextView.setText(selectedItems.toString());
                    break;
            }
        };
        //назначем событие на кнопки
        btnAdd.setOnClickListener(clickButtons);
        btnSelectAll.setOnClickListener(clickButtons);
        btnResetAll.setOnClickListener(clickButtons);
        btnShowList.setOnClickListener(clickButtons);
    }
}