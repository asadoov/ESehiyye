package az.gov.e_health.esehiyye.Model.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.gov.e_health.esehiyye.Model.NewRecipeStruct;
import az.gov.e_health.esehiyye.R;

public class NewRecipeAdapter extends BaseAdapter {
    private Context context; //context
    private NewRecipeStruct item; //data source of the list adapter
    //public constructor
    public NewRecipeAdapter(Context context, NewRecipeStruct item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.GetRecipeListResult.TotalCount; //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return item.GetRecipeListResult.Results.get(position); //returns list item at the specified position
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
                    inflate(R.layout.recipe_item_new, parent, false);
        }
// get current item to be displayed
        NewRecipeStruct.NewRecipe currentItem = (NewRecipeStruct.NewRecipe) getItem(position);

        // get the TextView for item name and item description
        TextView recipePos =
                convertView.findViewById(R.id.recipePos);
        TextView recipeNum =
                convertView.findViewById(R.id.recipeNum);
        TextView recipeDoctor =
                convertView.findViewById(R.id.recipeDoctor);
        TextView recipeDate =
                convertView.findViewById(R.id.recipeDate);
        TextView recipeStatus =
                convertView.findViewById(R.id.recipeStatus);
//        CheckBox dttCheckBox = convertView.findViewById(R.id.newEventCheckBox);
//        dttCheckBox.setChecked(currentItem.isChecked);


        //sets the text for item name and item description from the current item object
        recipePos.setText(Integer.toString(position+1));
        recipeNum.setText("Resept № "+Integer.toString(currentItem.Id));
        recipeDoctor.setText("Həkim: "+currentItem.DoctorName);
        recipeDate.setText("Yazılma tarixi: "+currentItem.RecipeDate);
        recipeStatus.setText("Reseptin statusu: "+currentItem.StatusText);

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
