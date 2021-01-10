package az.gov.e_health.esehiyye.ui.main.Institutions;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import az.gov.e_health.esehiyye.Model.Institutions.InstStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;


public class InstFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    InstStruct inst;
    Bundle bundle;

    public InstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_inst, container, false);
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        final TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        final TextView instPhone = view.findViewById(R.id.instPhone);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });

        instPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);
            }
        });
        bundle = this.getArguments();
        if (bundle != null) {
            String obj = bundle.getString("obj");
            Gson gson = new GsonBuilder().setLenient().create();
            inst = gson.fromJson(obj, InstStruct.class);
            if (inst.Lang == null || inst.Lang == "") {
                inst.Lang = "0";
            }
            if (inst.Lat == null || inst.Lat == "") {
                inst.Lat = "0";
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    TextView instName = view.findViewById(R.id.instName);
                    instName.setText(inst.Name);
                    toolbarTitle.setText(inst.Name);
                    TextView instLeader = view.findViewById(R.id.instLeader);
                    instLeader.setText(inst.LeadName);
                    TextView instAddress = view.findViewById(R.id.instAddress);
                    instAddress.setText(inst.Address);
                    instPhone.setPaintFlags(instPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    instPhone.setText(inst.Mobile);

                }
            });
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(Double.valueOf(inst.Lat), Double.valueOf(inst.Lang));
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(inst.Name)).showInfoWindow();
        pointToPosition(position);
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void pointToPosition(LatLng position) {
        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(10).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}