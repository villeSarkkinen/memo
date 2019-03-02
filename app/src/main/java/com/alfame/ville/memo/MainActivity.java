package com.alfame.ville.memo;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.IMainActivityActionListener, View.OnClickListener {

    private ExpandableListView listView;
    private CategoryAdapter categoryAdapter;
    private List<String> categories;
    private HashMap<String, List<Note>> categoryHashMap;
    private IStorageOperations storageOperations;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up SQLserver as method of operations
        res = getResources();
        File file = this.getDatabasePath(res.getString(R.string.dbName));
        //file.delete();
        storageOperations = new StorageOperations(new SQLiteDriver(file));

        listView = findViewById(R.id.noteListView);
        categories = new ArrayList<>();
        categoryHashMap = new HashMap<>();
        categoryAdapter = new CategoryAdapter(this, categories, categoryHashMap);
        listView.setAdapter(categoryAdapter);

        Button addBtn = findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(this);

        CategoryAdapter.setMainActivityActionListener(this);

        getNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.removeAllOpt:
                launchRemoveAllDialog();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchRemoveAllDialog() {

        AlertDialog.Builder bldr = new AlertDialog.Builder(this);
        bldr.setTitle(R.string.clrTitle);
        bldr.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                storageOperations.removeAll();
                categoryHashMap.clear();
                categories.clear();
                Toast.makeText(getApplicationContext(),R.string.toastClr,Toast.LENGTH_LONG).show();
                categoryAdapter.notifyDataSetChanged();

            }
        });
        bldr.setCancelable(true);
        bldr.setNeutralButton(R.string.btnCancel,null);
        bldr.create().show();
    }

    //get notes from database
    private void getNotes() {
        ArrayList<Note> notesList;
        notesList=storageOperations.loadItemsList();
        if(notesList!=null){
            for(int i=0;i<notesList.size();i++){
                if (categoryHashMap.containsKey(notesList.get(i).getCategory())) {
                    categoryHashMap.get(notesList.get(i).getCategory()).add(notesList.get(i));
                }
                else
                {
                    categories.add(notesList.get(i).getCategory());
                    ArrayList<Note> notes = new ArrayList<>();
                    notes.add(notesList.get(i));
                    categoryHashMap.put(categories.get(categories.size() - 1), notes);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        }else System.out.println("notesList null!");
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
                if (titleET.getText().toString().equals("") || textET.getText().toString().equals("") ||
                        (cb.isChecked() && newCategory.getText().toString().equals("")) ||
                        !cb.isChecked() && categories.size() == 0)
                {
                    validationToast();
                    return;
                }
                if (cb.isChecked())
                    category = newCategory.getText().toString();
                else
                    category = oldCategory.getSelectedItem().toString();

                //remove apostrophes for sql
                category=category.replace("'","");
                String title=titleET.getText().toString().replace("'","");
                String text=textET.getText().toString().replace("'","");

                Note note = new Note(storageOperations.getidCount(), title, text, category);
                if (categoryHashMap.containsKey(category)) {
                    categoryHashMap.get(category).add(note);
                } else {
                    categories.add(category);
                    ArrayList<Note> notes = new ArrayList<>();
                    notes.add(note);
                    categoryHashMap.put(categories.get(categories.size() - 1), notes);
                }
                storageOperations.addItem(note);
                categoryAdapter.notifyDataSetChanged();
                spa.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    private void validationToast() {
        Toast.makeText(this, R.string.toastMsg, Toast.LENGTH_LONG).show();
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
                String category;

                if (titleET.getText().toString().equals("") || textET.getText().toString().equals("") ||
                        (cb.isChecked() && newCategory.getText().toString().equals("")) ||
                        !cb.isChecked() && categories.size() == 0)
                {
                    validationToast();
                    return;
                }
                if (cb.isChecked()){
                    category = newCategory.getText().toString();
                }
                else{
                    category = oldCategory.getSelectedItem().toString();
                }
                category.replace("'","");
                //redirect all edit things
                editItemReal(groupPosition,childPosition,note,category,titleET.getText().toString(),textET.getText().toString());
            }
        });

        builder.create().show();
    }

    //redirect from EditDialog
    private void editItemReal(int groupPosition, int childPosition,Note note,String category,
                              String titleET,String textET) {

        //remove apostrophes
        category=category.replace("'","");
        titleET=titleET.replace("'","");
        textET=textET.replace("'","");

        //moving to a new category
        if (!note.getCategory().equals(category)) {

            System.out.println("chpos "+childPosition);
            categoryHashMap.get(categories.get(groupPosition)).remove(childPosition);
            //if last one in category, deletes category
            if (categoryHashMap.get(categories.get(groupPosition)).size() == 0) {
                categoryHashMap.remove(categories.get(groupPosition));
                categories.remove(groupPosition);
            }
            note.setNote(titleET, textET, category);


            //adds to category
            if (categoryHashMap.containsKey(category)) {
                categoryHashMap.get(category).add(note);
            }
            else//when category doesn't exist in hashMap adds category
            {
                categories.add(category);
                ArrayList<Note> notes = new ArrayList<>();
                notes.add(note);
                categoryHashMap.put(categories.get(categories.size() - 1), notes);
            }

        }
        else {//if category doesn't change
            note.setNote(titleET, textET, category);

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
        }
        storageOperations.editItem(note);
        categoryAdapter.notifyDataSetChanged();

    }



    @Override
    public void strikeItem(int groupPosition, int childPosition, boolean isStruck) {
        Note note = categoryHashMap.get(categories.get(groupPosition)).get(childPosition);
        note.setStruck(isStruck);
        categoryHashMap.get(categories.get(groupPosition)).set(childPosition, note);
        categoryAdapter.notifyDataSetChanged();
        storageOperations.editItem(note);
    }

    @Override
    public void removeItem(int groupPosition, int childPosition) {
        Note removeNote=categoryHashMap.get(categories.get(groupPosition)).get(childPosition);
        categoryHashMap.get(categories.get(groupPosition)).remove(childPosition);
        if (categoryHashMap.get(categories.get(groupPosition)).size() == 0)        {
            categoryHashMap.remove(categories.get(groupPosition));
            categories.remove(groupPosition);
        }
        storageOperations.removeItem(removeNote);
        categoryAdapter.notifyDataSetChanged();


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
