package com.alfame.ville.memo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.ILaunchEditDialogListener, View.OnClickListener {

    private ExpandableListView listView;
    private CategoryAdapter categoryAdapter;
    private List<String> categories;
    private HashMap<String, List<String>> categoryHashMap;
    private EditText et;
    IStorageOperations storageOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageOperations = new StorageOperations(new SQLiteDriver());

        listView = findViewById(R.id.noteListView);
        initData();
        categoryAdapter = new CategoryAdapter(this, categories, categoryHashMap);
        listView.setAdapter(categoryAdapter);

        Button addBtn = findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(this);


        CategoryAdapter.setLaunchEditDialogListener(this);

    }

    private void initData() {
        categories = new ArrayList<>();
        categoryHashMap = new HashMap<>();

        categories.add("First");
        categories.add("Second");

        List<String> firstItems = new ArrayList<>();
        firstItems.add("FirstFirst item");

        List<String> secondItems = new ArrayList<>();
        secondItems.add("SecondFirst item");

        categoryHashMap.put(categories.get(0), firstItems);
        categoryHashMap.put(categories.get(1), secondItems);
    }


    private void launchAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);

        et = v.findViewById(R.id.eTValue);

        builder.setTitle(R.string.titlAdd);
        builder.setView(v);
        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryHashMap.get(categories.get(0)).add(et.getText().toString());
                categoryAdapter.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }


    @Override
    public void launchEditDialog(final int groupPosition, final int childPosition) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);

        et = v.findViewById(R.id.eTValue);

        builder.setTitle(R.string.titlEdit);
        builder.setView(v);
        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryHashMap.get(categories.get(groupPosition)).set(childPosition, et.getText().toString());
                categoryAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                launchAddDialog();
                break;
            default:
                break;
        }
    }

}
