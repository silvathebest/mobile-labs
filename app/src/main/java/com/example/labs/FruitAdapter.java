package com.example.labs;

import static com.example.labs.MainFragment.fm;
import static com.example.labs.MainFragment.listItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FruitAdapter extends ArrayAdapter<JSONObject> {
    int listLayout;
    ArrayList<JSONObject> list;
    Context context;
    Button btnEditData;
    public static ArrayList<JSONObject> checkedArray = new ArrayList<>();

    public FruitAdapter(Context context, int listLayout, int field, ArrayList<JSONObject> list) {
        super(context, listLayout, field, list);
        this.context = context;
        this.listLayout = listLayout;
        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View itemView = inflater.inflate(listLayout, parent, false);

        TextView name = itemView.findViewById(R.id.name);
        TextView price = itemView.findViewById(R.id.price);
        CheckBox check = itemView.findViewById(R.id.check);
        btnEditData = itemView.findViewById(R.id.btnEditData);

        FragmentManager fragmentManager = fm;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            name.setText(list.get(position).getString("name"));
            price.setText(list.get(position).getString("price"));
            check.setChecked(list.get(position).getBoolean("checked"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnEditData.setOnClickListener(v -> {
            AddEditFragment editFragment = new AddEditFragment(name.getText(), price.getText(), position);
            fragmentTransaction.replace(R.id.main_fragment, editFragment, "tag");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        check.setOnClickListener(view -> {
            check.setChecked(check.isChecked());
            JSONObject item = listItems.get(position);

            if (checkedArray.contains(item)) {
                checkedArray.remove(item);
            } else {
                checkedArray.add(item);
            }

        });

        return itemView;
    }
}
