package az.gov.e_health.esehiyye.Model.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.NewsStruct;

import az.gov.e_health.esehiyye.R;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private Context context; //context
    private List<NewsStruct> items; //data source of the list adapter

    //public constructor
    public NewsAdapter(Context context, List<NewsStruct> items) {
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
                    inflate(R.layout.news_item, parent, false);
        }
// get current item to be displayed
        NewsStruct currentItem = (NewsStruct) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName =
                convertView.findViewById(R.id.newsTitle);
        TextView textViewItemDescription =
                convertView.findViewById(R.id.newsDescription);
        TextView textViewItemDate =
                convertView.findViewById(R.id.newsDate);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.NAME);
        textViewItemDescription.setText(currentItem.TEXT);
        textViewItemDate.setText(currentItem.DT);
        // returns the view for the current row
        return convertView;
    }
}
