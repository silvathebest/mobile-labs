package com.example.labs;

import static com.example.labs.MainFragment.listItems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddEditFragment extends Fragment {
    String textItem;
    TextView textBox;
    Button btnSave;
    Integer index;

    public AddEditFragment() {
        // Required empty public constructor
    }

    public AddEditFragment(String text, int index) {
        this.index = index;
        textItem = text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);
        textBox = v.findViewById(R.id.addEditTextView);
        btnSave = v.findViewById(R.id.btnSaveChanges);
        if (textItem != null) textBox.setText(textItem);

        btnSave.setOnClickListener((view) -> {
            textItem = textBox.getText().toString().trim();
            if (textItem.isEmpty()) return;

            if (index == null) {
                listItems.add(textItem);
            } else {
                listItems.set(index, textItem);
            }

            MainFragment.adapter.notifyDataSetChanged();
            textBox.setText("");
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        });
        // Inflate the layout for this fragment
        return v;
    }
}