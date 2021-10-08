package com.example.labs;

import static com.example.labs.MainFragment.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddEditFragment extends Fragment {
    Button btnSave;
    TextView textBoxName;
    TextView textBoxPrice;
    CheckBox check;
    String name = null;
    String price = null;
    Integer position = null;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();

    public AddEditFragment() {
        // Required empty public constructor
    }

    public AddEditFragment(CharSequence name, CharSequence price, int position) {
        this.name = name.toString();
        this.price = price.toString();
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);

        textBoxName = v.findViewById(R.id.textBoxAddEditName);
        textBoxPrice = v.findViewById(R.id.textBoxAddEditPrice);
        check = v.findViewById(R.id.check);

        if (name != null || price != null) {
            textBoxName.setText(name);
            textBoxPrice.setText(price);
        }
        View.OnClickListener clickButton = view -> {
            if (name == null && price == null) {
                JSONObject newJSON = new JSONObject();
                try {
                    newJSON.put("name", textBoxName.getText());
                    newJSON.put("price", textBoxPrice.getText());
                    newJSON.put("checked", false);
                    listItems.add(newJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String name;
                    String price;
                    for (int i = 0; i < listItems.size(); i++) {
                        if (i == position) {
                            name = textBoxName.getText().toString();
                            price = textBoxPrice.getText().toString();
                        } else {
                            name = listItems.get(i).get("name").toString();
                            price = listItems.get(i).get("price").toString();
                        }
                        names.add(name);
                        prices.add(price);
                    }
                    int count = listItems.size();
                    listItems = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        JSONObject newJSON = new JSONObject();
                        newJSON.put("name", names.get(i));
                        newJSON.put("price", prices.get(i));
                        listItems.add(newJSON);
                    }
                    adapter = new FruitAdapter(getContext(), R.layout.raw, R.id.name, listItems);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            saveData();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();

        };
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(clickButton);
        return v;
    }

    private void saveData() {
        try {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray(listItems);
            obj.put("fruits", arr);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString("data", obj.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}