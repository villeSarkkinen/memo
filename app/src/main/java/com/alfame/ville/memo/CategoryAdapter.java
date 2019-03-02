package com.alfame.ville.memo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> categories;
    private HashMap<String, List<Note> > categoryHashMap;
    private static IMainActivityActionListener MainActivityActionListener;

    public CategoryAdapter(Context context, List<String> categories, HashMap<String, List<Note>> categoryHashMap) {
        this.context = context;
        this.categories = categories;
        this.categoryHashMap = categoryHashMap;
    }

    public interface IMainActivityActionListener {
        abstract void launchEditDialog(int groupPosition, int childPosition, Note note);
        abstract void strikeItem(int groupPosition, int childPosition, boolean isStruck);
        abstract void removeItem(int groupPosition, int childPosition);
    }

    public static void setMainActivityActionListener(IMainActivityActionListener listener) {
        MainActivityActionListener = listener;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryHashMap.get(categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryHashMap.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryHashMap.get(categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView tv = convertView.findViewById(R.id.categoryName);
        tv.setText(categories.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView title = convertView.findViewById(R.id.noteTitle);
        TextView text = convertView.findViewById(R.id.noteText);
        TextView date = convertView.findViewById(R.id.noteDate);
        Button editBtn = convertView.findViewById(R.id.itemBtn);
        Button removeBtn = convertView.findViewById(R.id.itemRemove);
        Button doneBtn = convertView.findViewById(R.id.itemDone);

        final Note note = categoryHashMap.get(categories.get(groupPosition)).get(childPosition);

        if (note.isStruck())
            doneBtn.setText(R.string.btnUndo);
        else
            doneBtn.setText(R.string.btndone);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityActionListener.launchEditDialog(groupPosition, childPosition, note);
            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityActionListener.removeItem(groupPosition, childPosition);
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityActionListener.strikeItem(groupPosition, childPosition, !note.isStruck());
            }
        });

        setTextAndStrike(title, note.getTitle(), note.isStruck());
        setTextAndStrike(text, note.getText(), note.isStruck());
        setTextAndStrike(date, note.getDeadlineString(), note.isStruck());
        return convertView;
    }

    private void setTextAndStrike(TextView v, String txt, boolean strike) {
        v.setText(txt);
        if (strike)
            v.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        else
            v.setPaintFlags(0);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
