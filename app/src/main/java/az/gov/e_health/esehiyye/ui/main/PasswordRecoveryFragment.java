package az.gov.e_health.esehiyye.ui.main;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;


import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRecoveryFragment extends Fragment {


    public PasswordRecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_recovery, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Şifrənin bərpası");
        ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        WebView webView = view.findViewById(R.id.passRecoveryWebView);
        webView.loadUrl("https://eservice.e-health.gov.az/Login/Login/PasswordReset");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Yüklənir. Gözləyin...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        });
        return view;
    }

}
