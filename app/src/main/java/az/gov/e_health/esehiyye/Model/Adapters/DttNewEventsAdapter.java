package az.gov.e_health.esehiyye.Model.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import az.gov.e_health.esehiyye.Model.DrugsStruct;
import az.gov.e_health.esehiyye.Model.DttNewEventStruct;
import az.gov.e_health.esehiyye.R;

public class DttNewEventsAdapter extends BaseAdapter {
    private Context context; //context
    private List<DttNewEventStruct> items; //data source of the list adapter
    //public constructor
    public DttNewEventsAdapter(Context context, List<DttNewEventStruct> items) {
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
                    inflate(R.layout.dtt_new_events_item, parent, false);
        }
// get current item to be displayed
        DttNewEventStruct currentItem = (DttNewEventStruct) getItem(position);

        // get the TextView for item name and item description
        TextView headerText =
                convertView.findViewById(R.id.newEventHeader);
        TextView descriptionText =
                convertView.findViewById(R.id.newEventDescription);
        TextView dateText =
                convertView.findViewById(R.id.newEventDate);
        TextView creditText =
                convertView.findViewById(R.id.newEventCredit);
        CheckBox dttCheckBox = convertView.findViewById(R.id.newEventCheckBox);
        dttCheckBox.setChecked(currentItem.isChecked);


        //sets the text for item name and item description from the current item object
        headerText.setText(currentItem.KURS_AD);
        descriptionText.setText(currentItem.KURS_MUES);
        dateText.setText(String.format("(%s) - (%s)",currentItem.BEGINDATE,currentItem.ENDDATE));
        creditText.setText(String.format("Kredit sayı: %s",currentItem.KREDIT));
//        switch (currentItem.QEYDIYYAT_NOMRE)
//        {
//
//            case "Yenidən qeydiyyat prosedurundadır":
//                textViewItemDate.setImageResource(R.drawable.warning_ico);
//
//                break;
//            default:
//                textViewItemDate.setImageResource(R.drawable.checked_ico);
//
//                break;
//        }

        // returns the view for the current row
        return convertView;
    }
}
