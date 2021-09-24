package com.example.labs;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ListIterator;

public class MainFragment extends Fragment {
    Button btnAdd;
    Button btnSelectAll;
    Button btnResetAll;
    Button btnShowList;
    Button btnDelete;
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
                case R.id.btnShowList:
                    StringBuilder selectedItems = new StringBuilder();
                    for (int i = 0; i < listItems.size(); i++) {
                        if (selected.get(i))
                            selectedItems.append(listItems.get(i)).append(" ");
                    }
                    selectedTextView.setText(selectedItems.toString());
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
            }
        };


        //назначем событие на кнопки
        btnAdd = v.findViewById(R.id.btnAdd);
        btnSelectAll = v.findViewById(R.id.btnSelectAll);
        btnResetAll = v.findViewById(R.id.btnResetAll);
        btnShowList = v.findViewById(R.id.btnShowList);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnAdd.setOnClickListener(clickButtons);
        btnSelectAll.setOnClickListener(clickButtons);
        btnResetAll.setOnClickListener(clickButtons);
        btnShowList.setOnClickListener(clickButtons);
        btnDelete.setOnClickListener(clickButtons);
        // Inflate the layout for this fragment
        return v;
    }
}