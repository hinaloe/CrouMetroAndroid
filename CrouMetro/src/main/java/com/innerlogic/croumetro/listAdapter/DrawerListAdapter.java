package com.innerlogic.croumetro.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innerlogic.croumetro.R;

import java.util.ArrayList;

/**
 * Created by Timothy on 14/02/14.
 */
public class DrawerListAdapter extends ArrayAdapter {

    private ArrayList<String> menuOptions;

    public DrawerListAdapter(Context context, int resource, ArrayList<String> menuOptions) {
        super(context, resource, menuOptions);
        this.menuOptions = menuOptions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.drawer_listview_item, null);
        }
        TextView menuTextView = (TextView) v.findViewById(R.id.menuText);
        menuTextView.setText(menuOptions.get(position));
        return v;
    }
}
