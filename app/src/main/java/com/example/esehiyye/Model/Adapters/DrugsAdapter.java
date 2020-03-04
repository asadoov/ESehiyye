package com.example.esehiyye.Model.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esehiyye.Model.DrugsStruct;
import com.example.esehiyye.R;

import java.util.List;

public class DrugsAdapter extends BaseAdapter {
    private Context context; //context
    private List<DrugsStruct> items; //data source of the list adapter
    //public constructor
    public DrugsAdapter(Context context, List<DrugsStruct> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.drugs_item, parent, false);
        }
// get current item to be displayed
        DrugsStruct currentItem = (DrugsStruct) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName =
                convertView.findViewById(R.id.drugTitle);
        TextView textViewItemDescription =
                convertView.findViewById(R.id.drugDescription);
        ImageView textViewItemDate =
                convertView.findViewById(R.id.drugIco);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.AD);
        textViewItemDescription.setText(currentItem.BPA_DOZA);
        switch (currentItem.QEYDIYYAT_NOMRE)
        {

            case "Yenidən qeydiyyat prosedurundadır":
                textViewItemDate.setImageResource(R.drawable.warning_ico);

                break;
            default:
                textViewItemDate.setImageResource(R.drawable.checked_ico);

                break;
        }

        // returns the view for the current row
        return convertView;
    }
}
