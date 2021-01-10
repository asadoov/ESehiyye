package az.gov.e_health.esehiyye.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.Adapters.NewsAdapter;
import az.gov.e_health.esehiyye.Model.NewsStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileCats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileCats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    public FileCats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicalDocCats.
     */
    // TODO: Rename and change types and number of parameters
    public static FileCats newInstance(String param1, String param2) {
        FileCats fragment = new FileCats();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("aa",String.valueOf(this.getId()));
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_file_cats, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Tibbi sənədlərim");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });

                ListView catList = view.findViewById(R.id.docCategories);

        final String[] values = new String[] {
                "Reseptlər",
                "Epikrizlər",
                "Göndərişlər",
                "Arayışlar",
                "Laborator müayinələr",
                "Digər"

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        catList.setAdapter(adapter);





        catList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                switch (position){
                    case 0:
                        bundle.putInt("catID",2);
                        break;
                    case 1:
                        bundle.putInt("catID",3);
                        break;
                    case 2:
                        bundle.putInt("catID",4);
                        break;
                    case 3:
                        bundle.putInt("catID",5);
                        break;

                    case 4:
                        bundle.putInt("catID",7);
                        break;
                    case 5:
                        bundle.putInt("catID",6);
                        break;

                }
                bundle.putString("catName",values[position]);
                FileList fragment = new FileList();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}