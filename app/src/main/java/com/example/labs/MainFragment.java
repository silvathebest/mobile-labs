package com.example.labs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListIterator;

public class MainFragment extends Fragment {
    Button btnAdd;
    Button btnSelectAll;
    Button btnResetAll;
    Button btnDelete;
    Button btnSearch;
    TextView selectedTextView;
    public ListView list;
    public static final ArrayList<String> listItems = new ArrayList<>();
    public static ArrayAdapter<String> adapter = null;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        list = v.findViewById(R.id.list);
        //создаем adapter для добавления записей в listView
        adapter = new ArrayAdapter<>(v.getContext(),
                android.R.layout.simple_list_item_multiple_choice, listItems);
        list.setAdapter(adapter);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        list.setOnItemLongClickListener((parent, v1, position, id) -> {
            // по позиции получаем выбранный элемент
            String selectedItem = listItems.get(position);
            // установка текста элемента TextView
            AddEditFragment editFragment = new AddEditFragment(selectedItem, position);
            fragmentTransaction.replace(R.id.main_fragment, editFragment, "tag");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return false;
        });


        selectedTextView = v.findViewById(R.id.selectedTextView);
        @SuppressLint({"NonConstantResourceId", "SetTextI18n"}) View.OnClickListener clickButtons = view -> {
            SparseBooleanArray selected = list.getCheckedItemPositions();
            switch (view.getId()) {
                case R.id.btnAdd:
                    AddEditFragment addFragment = new AddEditFragment();
                    fragmentTransaction.replace(R.id.main_fragment, addFragment, "tag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
                case R.id.btnDelete:
                    ArrayList<String> checkedItems = new ArrayList<>();
                    for (int i = 0; i < list.getCount(); i++) {
                        if (selected.get(i)) {
                            checkedItems.add(listItems.get(i));
                        }
                    }
                    listItems.removeAll(checkedItems);
                    adapter.notifyDataSetChanged();
                    list.clearChoices();
                    break;
                case R.id.btnSearch:
                    String searchString = selectedTextView.getText().toString().toLowerCase().trim();
                    if (searchString.equals("")) {
                        Toast.makeText(v.getContext(), "Заполните поле поиска", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ArrayList<String> searchResultString = new ArrayList<>();
                    for (int i = 0; i < list.getCount(); i++) {
                        if (listItems.get(i).toLowerCase().contains(searchString)) {
                            searchResultString.add(listItems.get(i));
                        }
                    }
                    if (searchResultString.size() == 0) {
                        Toast.makeText(v.getContext(), "Результатов не найдено", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(getActivity(), SearchActivityResult.class);
                    intent.putExtra("SearchResult", searchResultString);
                    startActivity(intent);
            }
        };

        //назначем событие на кнопки
        btnAdd = v.findViewById(R.id.btnAdd);
        btnSelectAll = v.findViewById(R.id.btnSelectAll);
        btnResetAll = v.findViewById(R.id.btnResetAll);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnSearch = v.findViewById(R.id.btnSearch);
        btnAdd.setOnClickListener(clickButtons);
        btnSelectAll.setOnClickListener(clickButtons);
        btnResetAll.setOnClickListener(clickButtons);
        btnDelete.setOnClickListener(clickButtons);
        btnSearch.setOnClickListener(clickButtons);
        // Inflate the layout for this fragment
        return v;
    }
}