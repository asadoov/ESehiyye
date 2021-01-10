package az.gov.e_health.esehiyye.Model.Adapters.Dtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import az.gov.e_health.esehiyye.Model.DTT.DttDocImproveCourseStruct;
import az.gov.e_health.esehiyye.R;

public class DttDocImproveAdapter extends BaseAdapter {
    private Context context; //context
    private List<DttDocImproveCourseStruct> items; //data source of the list adapter
    //public constructor
    public DttDocImproveAdapter(Context context, List<DttDocImproveCourseStruct> items) {
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
        DttDocImproveCourseStruct currentItem = (DttDocImproveCourseStruct) getItem(position);

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
        headerText.setText(currentItem.KURSUN_ADI);
        descriptionText.setText(currentItem.KURS_MUES);
        dateText.setText(String.format("(%s) - (%s)",currentItem.TARIX_BASH,currentItem.TARIX_SON));
        creditText.setText(String.format("Kredit sayı: %s",currentItem.MUDAVIM_SAYI));
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
