package com.example.labs;

import static com.example.labs.FruitAdapter.checkedArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    Button btnAdd;
    Button btnDelete;
    Button btnSearch;
    Button btnLoad;
    TextView selectedTextView;
    public static SharedPreferences sPref;
    public static ArrayList<JSONObject> SearchArrayResult;

    @SuppressLint("StaticFieldLeak")
    public static ListView list;
    public static ArrayList<JSONObject> listItems = new ArrayList<>();
    public static ArrayAdapter<JSONObject> adapter = null;
    public static FragmentManager fm;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        list = v.findViewById(R.id.list);
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //создаем adapter для добавления записей в listView
        adapter = new FruitAdapter(getContext(), R.layout.raw, R.id.name, listItems);
        list.setAdapter(adapter);
        fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (listItems.size() == 0) loadData();

        selectedTextView = v.findViewById(R.id.selectedTextView);

        @SuppressLint({"NonConstantResourceId", "SetTextI18n"}) View.OnClickListener clickButtons = view -> {
            switch (view.getId()) {
                case R.id.btnAdd:
                    AddEditFragment addFragment = new AddEditFragment();
                    fragmentTransaction.replace(R.id.main_fragment, addFragment, "tag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case R.id.btnDelete:
                    listItems.removeAll(checkedArray);
                    adapter = new FruitAdapter(getContext(), R.layout.raw, R.id.name, listItems);
                    list.setAdapter(adapter);
                    checkedArray = new ArrayList<>();
                    saveData();
                    break;
                case R.id.btnSearch:
                    String searchString = selectedTextView.getText().toString().toLowerCase().trim();

                    if (searchString.equals("")) {
                        Toast.makeText(v.getContext(), "Заполните поле поиска", Toast.LENGTH_LONG).show();
                        return;
                    }

                    SearchArrayResult = new ArrayList<>();

                    for (int i = 0; i < list.getCount(); i++) {
                        try {
                            if (listItems.get(i).get("name").toString().toLowerCase().contains(searchString) ||
                                    listItems.get(i).get("price").toString().toLowerCase().contains(searchString)) {
                                SearchArrayResult.add(listItems.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (SearchArrayResult.size() == 0) {
                        Toast.makeText(v.getContext(), "Результатов не найдено", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(getActivity(), SearchActivityResult.class);
                    startActivity(intent);
                    break;
                case R.id.btnLoad:
                    try {
                        String jsonString = getJSonData();
                        JSONObject obj = new JSONObject(jsonString);
                        JSONArray array = obj.getJSONArray("fruits");
                        listItems = getArrayListFromJSONArray(array);
                        adapter = new FruitAdapter(getContext(), R.layout.raw, R.id.name, listItems);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };

        //назначем событие на кнопки
        btnAdd = v.findViewById(R.id.btnAdd);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnSearch = v.findViewById(R.id.btnSearch);
        btnLoad = v.findViewById(R.id.btnLoad);
        btnAdd.setOnClickListener(clickButtons);
        btnDelete.setOnClickListener(clickButtons);
        btnSearch.setOnClickListener(clickButtons);
        btnLoad.setOnClickListener(clickButtons);
        // Inflate the layout for this fragment
        return v;
    }

    private String getJSonData() {
        String json = null;
        try {
            InputStream inputStream = getResources().getAssets().open("fruitsData.json");
            int size = inputStream.available();
            byte[] data = new byte[size];
            inputStream.read(data);
            inputStream.close();
            json = new String(data, StandardCharsets.UTF_8);
            //jsonArray = new JSONArray(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return jsonArray;
        return json;
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray) {
        ArrayList<JSONObject> aList = new ArrayList<>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException js) {
            js.printStackTrace();
        }
        return aList;
    }

    public void saveData() {
        try {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray(listItems);
            obj.put("fruits", arr);
            sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString("data", obj.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void loadData() {
        try {
            String localData = sPref.getString("data", "data");
            JSONObject obj = new JSONObject(localData);
            JSONArray array = obj.getJSONArray("fruits");
            listItems = getArrayListFromJSONArray(array);
            adapter = new FruitAdapter(getContext(), R.layout.raw, R.id.name, listItems);
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}