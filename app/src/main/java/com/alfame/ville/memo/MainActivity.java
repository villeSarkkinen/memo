package com.alfame.ville.memo;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.IMainActivityActionListener, View.OnClickListener {

    private ExpandableListView listView;
    private CategoryAdapter categoryAdapter;
    private List<String> categories;
    private HashMap<String, List<Note>> categoryHashMap;
    //private Spinner sp;
    //private EditText et;
    IStorageOperations storageOperations;


    //ANTTI ADD
    private ArrayList<Note> notes;//Same as hashmap?
    private String[] cats;//Same as categories?

    private Resources res;
    // private int idCount;//tracks last ID
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LayoutInflater inflater = LayoutInflater.from(this);

        //View v = inflater.inflate(R.layout.dialog_layout, null);
        //sp = v.findViewById(R.id.noteOldCategory);

        //initData();


        /*
        res=getResources();
        idCount=0;

         */


        //Setting up SQLserver as method of operations
        res = getResources();
        File file = this.getDatabasePath(res.getString(R.string.dbName));
        System.out.println(this.getDatabasePath(res.getString(R.string.dbName)));
        //ANTTI need filepath for database!!!
        storageOperations = new StorageOperations(new SQLiteDriver(file));

        //storageOperations.createDatabase();

        listView = findViewById(R.id.noteListView);
        //initData();

        categories = new ArrayList<>();
        categoryHashMap = new HashMap<>();

        categoryAdapter = new CategoryAdapter(this, categories, categoryHashMap);
        listView.setAdapter(categoryAdapter);


        Button addBtn = findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(this);


        CategoryAdapter.setMainActivityActionListener(this);

    }

    private void initData() {

        //categories.add("First");
        //categories.add("Second");

        //ANTTI ADD

        //END
        /* ////For testing
        List<String> firstItems = new ArrayList<>();
        firstItems.add("FirstFirst item");

        List<String> secondItems = new ArrayList<>();
        secondItems.add("SecondFirst item");

        categoryHashMap.put(categories.get(0), firstItems);
        categoryHashMap.put(categories.get(1), secondItems);*/
    }



    private void launchAddDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);


        final CheckBox cb = v.findViewById(R.id.newCategoryCB);
        final TextView newCategory = v.findViewById(R.id.noteNewCategory);

        final EditText titleET = v.findViewById(R.id.noteEditTitle);
        final EditText textET = v.findViewById(R.id.noteEditText);

        final ArrayAdapter<String> spa;
        final Spinner oldCategory = v.findViewById(R.id.noteOldCategory);
        spa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        spa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        oldCategory.setAdapter(spa);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    newCategory.setVisibility(View.VISIBLE);
                    oldCategory.setVisibility(View.GONE);
                } else {
                    newCategory.setVisibility(View.GONE);
                    oldCategory.setVisibility(View.VISIBLE);
                }

            }


        });

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
                String category;
                if (cb.isChecked())
                    category = newCategory.getText().toString();
                else
                    category = oldCategory.getSelectedItem().toString();

                //ANTTI WAS HERE, fixed constructor, gets id from storage and struck=false
                Note note = new Note(storageOperations.getidCount(),titleET.getText().toString(), textET.getText().toString(), category);
                if (categoryHashMap.containsKey(category)) {
                    categoryHashMap.get(category).add(note);
                }
                else
                {
                    categories.add(category);
                    ArrayList<Note> notes = new ArrayList<>();
                    notes.add(note);
                    categoryHashMap.put(categories.get(categories.size() - 1), notes);
                }
                //storageOperations.addItem(note);
                categoryAdapter.notifyDataSetChanged();
                spa.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }


    @Override
    public void launchEditDialog(final int groupPosition, final int childPosition, final Note note) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);


        final CheckBox cb = v.findViewById(R.id.newCategoryCB);
        final TextView newCategory = v.findViewById(R.id.noteNewCategory);

        final EditText titleET = v.findViewById(R.id.noteEditTitle);
        final EditText textET = v.findViewById(R.id.noteEditText);
        titleET.setText(note.getTitle());
        textET.setText(note.getText());


        final ArrayAdapter<String> spa;
        final Spinner oldCategory = v.findViewById(R.id.noteOldCategory);
        spa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        spa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        oldCategory.setAdapter(spa);


        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    newCategory.setVisibility(View.VISIBLE);
                    oldCategory.setVisibility(View.GONE);
                } else {
                    newCategory.setVisibility(View.GONE);
                    oldCategory.setVisibility(View.VISIBLE);
                }

            }


        });

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
                String category;

                if (cb.isChecked())
                    category = newCategory.getText().toString();
                else
                    category = oldCategory.getSelectedItem().toString();

                if (!note.getCategory().equals(category)) {
                    if (categoryHashMap.get(note.getCategory()).size() == 1) {
                        categoryHashMap.remove(category);
                        categories.remove(category);
                    }
                    categoryHashMap.get(note.getCategory()).remove(childPosition);
                }
                note.setNote(titleET.getText().toString(), textET.getText().toString(),
                        category, note.isStruck());
                    //ANTTI WAS HERE

                if (categoryHashMap.containsKey(category)) {
                    categoryHashMap.get(category).set(childPosition, note);
                }
                else
                {
                    categories.add(category);
                    ArrayList<Note> notes = new ArrayList<>();
                    notes.add(note);
                    categoryHashMap.put(categories.get(categories.size() - 1), notes);
                }
                //storageOperations.editItem(note);
                categoryAdapter.notifyDataSetChanged();
                spa.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    @Override
    public void strikeItem(int groupPosition, int childPosition, boolean isStruck) {
        Note note = categoryHashMap.get(categories.get(groupPosition)).get(childPosition);
        note.setStruck(isStruck);
        categoryHashMap.get(categories.get(groupPosition)).set(childPosition, note);
        categoryAdapter.notifyDataSetChanged();
        //storageOperations.editItem(note)
    }

    @Override
    public void removeItem(int groupPosition, int childPosition) {
        if (categoryHashMap.get(categories.get(groupPosition)).size() == 1)
        {
            categoryHashMap.remove(categories.get(groupPosition));
            categories.remove(groupPosition);
        }
        else {
            categoryHashMap.get(categories.get(groupPosition)).remove(childPosition);
        }
        categoryAdapter.notifyDataSetChanged();
        //storageOperations.removeItem(categoryHashMap.get(categories.get(groupPosition)).get(childPosition));
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
