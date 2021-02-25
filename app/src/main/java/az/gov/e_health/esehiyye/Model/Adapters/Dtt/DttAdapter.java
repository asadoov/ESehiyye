package az.gov.e_health.esehiyye.Model.Adapters.Dtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.gov.e_health.esehiyye.Model.DTT.DttStruct;
import az.gov.e_health.esehiyye.R;

public class DttAdapter extends BaseAdapter {
    private Context context; //context
    private List<DttStruct> items; //data source of the list adapter

    //public constructor
    public DttAdapter(Context context, List<DttStruct> items) {
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
                    inflate(R.layout.dtt_item, parent, false);
        }
// get current item to be displayed
        DttStruct currentItem = (DttStruct) getItem(position);

        // get the TextView for item name and item description
        TextView courseTitle =
                convertView.findViewById(R.id.dttTitle);
        TextView courseStart =
                convertView.findViewById(R.id.courseStart);
        TextView courseEnd =
                convertView.findViewById(R.id.courseEnd);
        TextView courseCredit =
                convertView.findViewById(R.id.credit);
        TextView courseAccepted =
                convertView.findViewById(R.id.accepted);
        //sets the text for item name and item description from the current item object
        courseTitle.setText(currentItem.KURS_ADI);
        courseStart.setText("Başlama tarixi: " + currentItem.KURS_BASHLAMA_TARIXI);
        courseEnd.setText("Bitmə tarixi: " + currentItem.KURS_BITME_TARIXI);
        courseCredit.setText("Kredit: " + currentItem.KREDIT);
        if (currentItem.TESTIQLENIB.equals("1")) {
            courseAccepted.setText("Status: təsdiqlənib");
        } else
        {
            courseAccepted.setText("Status: gözləyir");
        }            // returns the view for the current row
            return convertView;
    }
}
